package com.example.galleryservice.data;

import com.example.galleryservice.configuration.DataSourceConfig;
import com.example.galleryservice.model.user.Client;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataSourceConfig.class, StorageDAO.class})
public class StorageDAOTest {

    @Autowired
    StorageDAO storageDAO;

    @Test
    public void addUserTest(){
        final Client client = new Client("client3", "qwertyuio", "Vasya Pupkin", "vasya_pupkin@mail.ru");
        storageDAO.addUser(client);
    }

}
