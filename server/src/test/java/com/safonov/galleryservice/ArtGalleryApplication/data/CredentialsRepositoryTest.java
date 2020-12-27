package com.safonov.galleryservice.ArtGalleryApplication.data;

import com.safonov.galleryservice.ArtGalleryApplication.data.actor.CredentialsRepository;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Credentials;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.validation.constraints.NotNull;

import static org.assertj.core.api.Assertions.assertThat;

/*@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {CredentialsRepositoryTest.Initializer.class})
public class CredentialsRepositoryTest {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @ClassRule
    public final static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    public void whenFindByName_thenReturnEmployee() {
        final Credentials appUser = new Credentials();
        appUser.setLogin("Alex");
        appUser.setPassword("password");
        appUser.setEmail("test@mail.ru");
        credentialsRepository.save(appUser);

        final Credentials found = credentialsRepository.findByLogin(appUser.getLogin()).orElse(null);

        assertThat(found).isNotEqualTo(null);
        assertThat(found.getLogin())
                .isEqualTo(appUser.getLogin());
    }
}*/
