package com.safonov.galleryservice.ArtGalleryApplication.controllers;

import com.safonov.galleryservice.ArtGalleryApplication.data.actor.ArtistRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.CredentialsRepository;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertEquals;

/*@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ArtistControllerIntegrationTest {

    @Container
    public final static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withPassword("inmemory")
            .withUsername("inmemory");
    @Autowired
    public TestRestTemplate testRestTemplate;
    @Autowired
    private ArtistRepository artistRepository;

    @Test
    public void addArtworkCode200() throws Exception {

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "admin");
        jsonObject.put("password", "password");

        final HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), null);
        final ResponseEntity<String> result = testRestTemplate.postForEntity("/api/auth/sign-in", request, String.class);

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", result.getHeaders().get("Authorization").get(0));

        final ResponseEntity<Object> result1 = testRestTemplate.exchange("/api/artist/1/artworks", HttpMethod.GET, new HttpEntity<>(headers), Object.class);
        System.out.println(result1.getStatusCode());

        assertEquals(HttpStatus.OK, result1.getStatusCode());
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername()
            );
            values.applyTo(configurableApplicationContext);
        }
    }
}*/