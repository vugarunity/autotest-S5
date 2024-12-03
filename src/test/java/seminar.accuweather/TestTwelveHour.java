package seminar.accuweather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTwelveHour extends AbstractTest{

    private static final Logger logger
            = LoggerFactory.getLogger(TestTwelveHour.class);

    @Test
    void get_shouldReturn200() throws IOException, URISyntaxException {
        logger.info("Тест код ответ 200 запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        HourlyForecast bodyOk = new HourlyForecast();
        String currentTime = LocalTime.now().toString();
        bodyOk.setTime(currentTime);

        logger.debug("Формирование мока для GET /forecasts/v1/hourly/12hour/294459");
        stubFor(get(urlPathEqualTo("/forecasts/v1/hourly/12hour/294459"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodyOk))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");
        //when

        HttpGet request = new HttpGet(getBaseUrl()+"/forecasts/v1/hourly/12hour/294459");
        URI uriOk = new URIBuilder(request.getURI())
                .build();
        request.setURI(uriOk);
        HttpResponse responseOk = httpClient.execute(request);

        //then

        verify(1, getRequestedFor(urlPathEqualTo("/forecasts/v1/hourly/12hour/294459")));
        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals(currentTime, mapper.readValue(responseOk.getEntity().getContent(), HourlyForecast.class).getTime());
    }

    @Test
    void get_shouldReturn503WhenApiKeyExpired() throws IOException, URISyntaxException {
        logger.info("Тест для истекшего API ключа запущен");

        // given
        ObjectMapper mapper = new ObjectMapper();
        HourlyForecast bodyError = new HourlyForecast();
        bodyError.setTime("API key expired");

        stubFor(get(urlPathEqualTo("/forecasts/v1/hourly/12hour/294459"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withBody(mapper.writeValueAsString(bodyError))));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(getBaseUrl() + "/forecasts/v1/hourly/12hour/294459");
            HttpResponse response = httpClient.execute(request);

            // then
            assertEquals(503, response.getStatusLine().getStatusCode());
            assertEquals("API key expired", mapper.readValue(response.getEntity().getContent(), HourlyForecast.class).getTime());
        }
    }
}

