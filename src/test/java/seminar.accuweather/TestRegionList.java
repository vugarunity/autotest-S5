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
import seminar.accuweather.location.Location;

import javax.swing.plaf.synth.Region;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRegionList extends AbstractTest{

    private static final Logger logger
            = LoggerFactory.getLogger(TestRegionList.class);

    @Test
    void get_shouldReturnCorrectId() throws IOException, URISyntaxException {
        logger.info("Тест код ответ 200 запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        RegionList bodyOk = new RegionList();
        bodyOk.setId("OK");

        logger.debug("Формирование мока для GET /locations/v1/regions");
        stubFor(get(urlPathEqualTo("/locations/v1/regions"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodyOk))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");
        //when

        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/regions");
        URI uriOk = new URIBuilder(request.getURI())
                .build();
        request.setURI(uriOk);
        HttpResponse responseOk = httpClient.execute(request);

        //then

        verify(1, getRequestedFor(urlPathEqualTo("/locations/v1/regions")));
        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals("OK", mapper.readValue(responseOk.getEntity().getContent(), RegionList.class).getId());
    }

    @Test
    void get_shouldReturnCorrectLocalizedName() throws IOException, URISyntaxException {
        logger.info("Тест код ответ 200 запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        RegionList bodyOk = new RegionList();
        bodyOk.setLocalizedName("OK");

        logger.debug("Формирование мока для GET /locations/v1/regions");
        stubFor(get(urlPathEqualTo("/locations/v1/regions"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodyOk))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");
        //when

        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/regions");
        URI uriOk = new URIBuilder(request.getURI())
                .build();
        request.setURI(uriOk);
        HttpResponse responseOk = httpClient.execute(request);

        //then

        verify(1, getRequestedFor(urlPathEqualTo("/locations/v1/regions")));
        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals("OK", mapper.readValue(responseOk.getEntity().getContent(), RegionList.class).getLocalizedName());
    }

    @Test
    void get_shouldReturn503WhenApiKeyExpired() throws IOException, URISyntaxException {
        logger.info("Тест для истекшего API ключа запущен");

        // given
        ObjectMapper mapper = new ObjectMapper();
        RegionList bodyError = new RegionList();
        bodyError.setId("API key expired");

        stubFor(get(urlPathEqualTo("/locations/v1/regions"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withBody(mapper.writeValueAsString(bodyError))));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/regions");
            HttpResponse response = httpClient.execute(request);

            // then
            assertEquals(503, response.getStatusLine().getStatusCode());
            assertEquals("API key expired", mapper.readValue(response.getEntity().getContent(), RegionList.class).getId());
        }
    }
}
