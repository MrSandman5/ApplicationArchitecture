package com.example.galleryservice.data;

import com.example.galleryservice.configuration.DataSourceConfig;
import com.example.galleryservice.model.user.Client;
import com.example.galleryservice.model.user.Owner;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataSourceConfig.class, StorageDAO.class})
public class StorageDAOTest {

    private StorageDAO storageDAO;

    @BeforeEach
    private void setUp(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);
        this.storageDAO = context.getBean(StorageDAO.class);
    }

    @Test
    public void addUserTest(){
        final Client client = new Client("client1", "qwertyuio", "Vasya Pupkin", "vasya_pupkin@mail.ru");
        long clientId = storageDAO.addUser(client);
        System.out.println("Generated Id: " + clientId);
        assertEquals(client.getLogin(), storageDAO.getUserDAO().findByID(clientId).getLogin());
    }

    @AfterEach
    private void setDown(){
        this.storageDAO = null;
    }

}
