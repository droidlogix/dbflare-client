package com.droidlogix.dbflare.client.test;

import com.droidlogix.dbflare.client.DbFlareClient;
import com.droidlogix.dbflare.client.test.models.Jobsheet;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DbFlareClientTest {
    private static final Logger logger = LoggerFactory.getLogger(DbFlareClientTest.class);
    private static String baseURL = "";
    private static String apiKey = "";

    public ObjectMapper getObjectMapper()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    private GsonBuilder getGsonBuilder()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        return new GsonBuilder();
    }

    private DbFlareClient getDbFlareClient()
    {
        logger.info(() ->"WebConfig::getDbFlareClient");
        try
        {
            CloseableHttpAsyncClient x = HttpAsyncClients.custom().setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, (x509Certificates, s) -> true).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .useSystemProperties()
                    .build();
            Unirest.setAsyncHttpClient(x);
        }
        catch (Exception exception)
        {
            logger.error(() -> exception.getMessage());
        }

        return new DbFlareClient.Config()
                .objectMapper(getObjectMapper())
                .baseUrl(baseURL)
                .isKeyRequired(true)
                .apiKey(apiKey).build();
    }

    private DbFlareClient dbFlareClient = getDbFlareClient();

    @Test
    public void zgetListMap() {
    }
}
