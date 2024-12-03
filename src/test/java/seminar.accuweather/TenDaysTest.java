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

public class TenDaysTest extends AbstractTest {

    private static final Logger logger
            = LoggerFactory.getLogger(TenDaysTest.class);


    @Test
    void getAdminAreas_shouldReturn401ForUnauthorizedAccess() throws IOException, URISyntaxException {
        logger.info("Тест код ответа 401 запущен");

        // given
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse bodyError = new ErrorResponse();
        bodyError.setCode("Unauthorized");
        bodyError.setMessage("Api Authorization failed");
        bodyError.setReference("/forecasts/v1/daily/10day/27103");

        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/10day/27103"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withBody(mapper.writeValueAsString(bodyError))));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(getBaseUrl() + "/forecasts/v1/daily/10day/27103");
            HttpResponse response = httpClient.execute(request);

            // then
            assertEquals(401, response.getStatusLine().getStatusCode());

            // Десериализация тела ответа как ErrorResponse
            ErrorResponse errorResponse = mapper.readValue(response.getEntity().getContent(), ErrorResponse.class);
            assertEquals("Unauthorized", errorResponse.getCode());
            assertEquals("Api Authorization failed", errorResponse.getMessage());
            assertEquals("/forecasts/v1/daily/10day/27103", errorResponse.getReference());
        }
    }

    @Test
    void get_shouldReturn503WhenApiKeyExpired() throws IOException, URISyntaxException {
        logger.info("Тест для истекшего API ключа запущен");

        // given
        ObjectMapper mapper = new ObjectMapper();
        DailyForecast bodyError = new DailyForecast();
        bodyError.setDate("API key expired");

        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/10day/27103"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withBody(mapper.writeValueAsString(bodyError))));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(getBaseUrl() + "/forecasts/v1/daily/10day/27103");
            HttpResponse response = httpClient.execute(request);

            // then
            assertEquals(503, response.getStatusLine().getStatusCode());
            assertEquals("API key expired", mapper.readValue(response.getEntity().getContent(), DailyForecast.class).getDate());
        }
    }
}
