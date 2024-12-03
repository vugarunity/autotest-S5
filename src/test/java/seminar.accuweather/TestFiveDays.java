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
import seminar.accuweather.weather.DailyForecast;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFiveDays extends AbstractTest{

    private static final Logger logger
            = LoggerFactory.getLogger(TestFiveDays.class);

    @Test
    void get_shouldReturn200_Novosibirsk() throws IOException, URISyntaxException {
        logger.info("Тест код ответ 200 запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        DailyForecast bodyOk = new DailyForecast();
        String currentDate = LocalDate.now().toString();
        bodyOk.setDate(currentDate);


        logger.debug("Формирование мока для GET /forecasts/v1/daily/5day/294459");
        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/5day/294459"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodyOk))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");
        //when

        HttpGet request = new HttpGet(getBaseUrl()+"/forecasts/v1/daily/5day/294459");
        URI uriOk = new URIBuilder(request.getURI())
                .build();
        request.setURI(uriOk);
        HttpResponse responseOk = httpClient.execute(request);

        //then

        verify(1, getRequestedFor(urlPathEqualTo("/forecasts/v1/daily/5day/294459")));
        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals(currentDate, mapper.readValue(responseOk.getEntity().getContent(), DailyForecast.class).getDate());
    }


    @Test
    void get_shouldReturn200_Baku() throws IOException, URISyntaxException {
        logger.info("Тест код ответ 200 запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        DailyForecast bodyOk = new DailyForecast();
        String currentDate = LocalDate.now().toString();
        bodyOk.setDate(currentDate);


        logger.debug("Формирование мока для GET /forecasts/v1/daily/5day/27103");
        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/5day/27103"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodyOk))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");
        //when

        HttpGet request = new HttpGet(getBaseUrl()+"/forecasts/v1/daily/5day/27103");
        URI uriOk = new URIBuilder(request.getURI())
                .build();
        request.setURI(uriOk);
        HttpResponse responseOk = httpClient.execute(request);

        //then

        verify(1, getRequestedFor(urlPathEqualTo("/forecasts/v1/daily/5day/27103")));
        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals(currentDate, mapper.readValue(responseOk.getEntity().getContent(), DailyForecast.class).getDate());
    }

    @Test
    void get_shouldReturn503WhenApiKeyExpired() throws IOException, URISyntaxException {
        logger.info("Тест для истекшего API ключа запущен");

        // given
        ObjectMapper mapper = new ObjectMapper();
        DailyForecast bodyError = new DailyForecast();
        bodyError.setDate("API key expired");

        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/5day/27103"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withBody(mapper.writeValueAsString(bodyError))));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(getBaseUrl() + "/forecasts/v1/daily/5day/27103");
            HttpResponse response = httpClient.execute(request);

            // then
            assertEquals(503, response.getStatusLine().getStatusCode());
            assertEquals("API key expired", mapper.readValue(response.getEntity().getContent(), DailyForecast.class).getDate());
        }
    }
}

