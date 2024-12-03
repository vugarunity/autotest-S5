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
import seminar.accuweather.location.AdministrativeArea;
import seminar.accuweather.location.Country;
import seminar.accuweather.location.Location;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCountryList extends AbstractTest{

    private static final Logger logger
            = LoggerFactory.getLogger(TestCountryList.class);

    @Test
    void get_shouldReturnCorrectId() throws IOException, URISyntaxException {
        logger.info("Тест на правильный ID запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        Country bodyOk = new Country();
        bodyOk.setId("OK");
        bodyOk.setLocalizedName("OK");

        logger.debug("Формирование мока для GET /locations/v1/countries/Asia");
        stubFor(get(urlPathEqualTo("/locations/v1/countries/Asia"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodyOk))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");
        //when

        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/countries/Asia");
        URI uriOk = new URIBuilder(request.getURI())
                .build();
        request.setURI(uriOk);
        HttpResponse responseOk = httpClient.execute(request);

        //then

        verify(1, getRequestedFor(urlPathEqualTo("/locations/v1/countries/Asia")));
        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals("OK", mapper.readValue(responseOk.getEntity().getContent(), Country.class).getId());
    }

    @Test
    void get_shouldReturnCorrectLocalizedName() throws IOException, URISyntaxException {
        logger.info("Тест на правильное LocalizedName запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        Country bodyOk = new Country();
        bodyOk.setLocalizedName("OK");

        logger.debug("Формирование мока для GET /locations/v1/countries/Asia");
        stubFor(get(urlPathEqualTo("/locations/v1/countries/Asia"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodyOk))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");
        //when

        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/countries/Asia");
        URI uriOk = new URIBuilder(request.getURI())
                .build();
        request.setURI(uriOk);
        HttpResponse responseOk = httpClient.execute(request);

        //then

        verify(1, getRequestedFor(urlPathEqualTo("/locations/v1/countries/Asia")));
        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals("OK", mapper.readValue(responseOk.getEntity().getContent(), Country.class).getLocalizedName());
    }

    @Test
    void get_shouldReturn503WhenApiKeyExpired_Id() throws IOException, URISyntaxException {
        logger.info("Тест для истекшего API ключа запущен");

        // given
        ObjectMapper mapper = new ObjectMapper();
        Country bodyError = new Country();
        bodyError.setId("API key expired");

        stubFor(get(urlPathEqualTo("/locations/v1/countries/Asia"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withBody(mapper.writeValueAsString(bodyError))));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/countries/Asia");
            HttpResponse response = httpClient.execute(request);

            // then
            assertEquals(503, response.getStatusLine().getStatusCode());
            assertEquals("API key expired", mapper.readValue(response.getEntity().getContent(), Country.class).getId());
        }
    }

    @Test
    void get_shouldReturn503WhenApiKeyExpired_LocalizedName() throws IOException, URISyntaxException {
        logger.info("Тест для истекшего API ключа запущен");

        // given
        ObjectMapper mapper = new ObjectMapper();
        Country bodyError = new Country();
        bodyError.setLocalizedName("API key expired");

        stubFor(get(urlPathEqualTo("/locations/v1/countries/Asia"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withBody(mapper.writeValueAsString(bodyError))));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/countries/Asia");
            HttpResponse response = httpClient.execute(request);

            // then
            assertEquals(503, response.getStatusLine().getStatusCode());
            assertEquals("API key expired", mapper.readValue(response.getEntity().getContent(), Country.class).getLocalizedName());
        }
    }
}
