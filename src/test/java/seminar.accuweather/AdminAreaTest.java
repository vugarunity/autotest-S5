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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminAreaTest extends AbstractTest{

    private static final Logger logger
            = LoggerFactory.getLogger(AdminAreaTest.class);

    @Test
    void get_shouldReturnCorrectId_AZ() throws IOException, URISyntaxException {
        logger.info("Тест на правильный ID для AZ запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        AdministrativeArea bodyOk = new AdministrativeArea();
        bodyOk.setId("OK");
        bodyOk.setLocalizedName("OK");

        logger.debug("Формирование мока для GET /locations/v1/adminareas/AZ");
        stubFor(get(urlPathEqualTo("/locations/v1/adminareas/AZ"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodyOk))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");
        //when

        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/adminareas/AZ");
        URI uriOk = new URIBuilder(request.getURI())
                .build();
        request.setURI(uriOk);
        HttpResponse responseOk = httpClient.execute(request);

        //then

        verify(1, getRequestedFor(urlPathEqualTo("/locations/v1/adminareas/AZ")));
        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals("OK", mapper.readValue(responseOk.getEntity().getContent(), AdministrativeArea.class).getId());
    }

    @Test
    void get_shouldReturnCorrectLocalizedName_AZ() throws IOException, URISyntaxException {
        logger.info("Тест на правильное LocalizedName для AZ запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        AdministrativeArea bodyOk = new AdministrativeArea();
        bodyOk.setLocalizedName("OK");

        logger.debug("Формирование мока для GET /locations/v1/adminareas/AZ");
        stubFor(get(urlPathEqualTo("/locations/v1/adminareas/AZ"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodyOk))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");
        //when

        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/adminareas/AZ");
        URI uriOk = new URIBuilder(request.getURI())
                .build();
        request.setURI(uriOk);
        HttpResponse responseOk = httpClient.execute(request);

        //then

        verify(1, getRequestedFor(urlPathEqualTo("/locations/v1/adminareas/AZ")));
        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals("OK", mapper.readValue(responseOk.getEntity().getContent(), AdministrativeArea.class).getLocalizedName());
    }

    @Test
    void get_shouldReturnCorrectId_RU() throws IOException, URISyntaxException {
        logger.info("Тест на правильный ID для AZ запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        AdministrativeArea bodyOk = new AdministrativeArea();
        bodyOk.setId("OK");
        bodyOk.setLocalizedName("OK");

        logger.debug("Формирование мока для GET /locations/v1/adminareas/RU");
        stubFor(get(urlPathEqualTo("/locations/v1/adminareas/RU"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodyOk))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");
        //when

        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/adminareas/RU");
        URI uriOk = new URIBuilder(request.getURI())
                .build();
        request.setURI(uriOk);
        HttpResponse responseOk = httpClient.execute(request);

        //then

        verify(1, getRequestedFor(urlPathEqualTo("/locations/v1/adminareas/RU")));
        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals("OK", mapper.readValue(responseOk.getEntity().getContent(), AdministrativeArea.class).getId());
    }

    @Test
    void get_shouldReturnCorrectLocalizedName_RU() throws IOException, URISyntaxException {
        logger.info("Тест на правильное LocalizedName для AZ запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        AdministrativeArea bodyOk = new AdministrativeArea();
        bodyOk.setLocalizedName("OK");

        logger.debug("Формирование мока для GET /locations/v1/adminareas/RU");
        stubFor(get(urlPathEqualTo("/locations/v1/adminareas/RU"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodyOk))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");
        //when

        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/adminareas/RU");
        URI uriOk = new URIBuilder(request.getURI())
                .build();
        request.setURI(uriOk);
        HttpResponse responseOk = httpClient.execute(request);

        //then

        verify(1, getRequestedFor(urlPathEqualTo("/locations/v1/adminareas/RU")));
        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals("OK", mapper.readValue(responseOk.getEntity().getContent(), AdministrativeArea.class).getLocalizedName());
    }

    @Test
    void get_shouldReturn503WhenApiKeyExpired_Id() throws IOException, URISyntaxException {
        logger.info("Тест для истекшего API ключа запущен");

        // given
        ObjectMapper mapper = new ObjectMapper();
        AdministrativeArea bodyError = new AdministrativeArea();
        bodyError.setId("API key expired");

        stubFor(get(urlPathEqualTo("/locations/v1/adminareas/"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withBody(mapper.writeValueAsString(bodyError))));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/adminareas/");
            HttpResponse response = httpClient.execute(request);

            // then
            assertEquals(503, response.getStatusLine().getStatusCode());
            assertEquals("API key expired", mapper.readValue(response.getEntity().getContent(), AdministrativeArea.class).getId());
        }
    }

    @Test
    void get_shouldReturn503WhenApiKeyExpired_LocalizedName() throws IOException, URISyntaxException {
        logger.info("Тест для истекшего API ключа запущен");

        // given
        ObjectMapper mapper = new ObjectMapper();
        AdministrativeArea bodyError = new AdministrativeArea();
        bodyError.setLocalizedName("API key expired");

        stubFor(get(urlPathEqualTo("/locations/v1/adminareas/"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withBody(mapper.writeValueAsString(bodyError))));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/adminareas/");
            HttpResponse response = httpClient.execute(request);

            // then
            assertEquals(503, response.getStatusLine().getStatusCode());
            assertEquals("API key expired", mapper.readValue(response.getEntity().getContent(), AdministrativeArea.class).getLocalizedName());
        }
    }
}
