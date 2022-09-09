package com.cognizant.userservice.service;

import com.cognizant.userservice.model.UserData;
import com.cognizant.userservice.repo.UserRepo;
import com.cognizant.userservice.util.Constants;
import com.cognizant.userservice.util.RequestResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepo userRepo;

    @Mock
    MongoOperations mongoperation;

    private UserData testUserData1;
    private UserData testUserData2;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        testUserData1 = new UserData(
                "test1@gmail.com",
                "testUserName1",
                "pass",
                "testFIrstName",
                "testLastName",
                "male",
                "09/14/1999",
                "7032370563",
                false
        );
        testUserData2 = new UserData(
                "test1@gmail.com",
                "testUserName1",
                "pass",
                "testFIrstName",
                "testLastName",
                "male",
                "09/14/1999",
                "7032370563",
                false
        );
    }

    @Test
    void register() {
        ResponseEntity<RequestResponse<String>> registerUser = userService.register(testUserData1);
        Assertions.assertEquals(ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, testUserData1.getEmail()+" "+ Constants.USER_NAME_REGISTERED_SUCCESSFULLY)),
                registerUser);
    }

    @Test
    void getAllusers() {
        List<UserData> userDataList = Arrays.asList(testUserData1, testUserData2);
        Mockito.when(userRepo.findAll()).thenReturn(userDataList);
        ResponseEntity<RequestResponse<List<UserData>>> allUserData = userService.getAllusers();
        Assertions.assertEquals(ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, userDataList)),
                allUserData);
    }

    @Test
    void searchBasedOnUserName() {
        Optional<UserData> userDataFound = Optional.of(testUserData1);
        Mockito.when(userRepo.findByUserName("testUserName1")).thenReturn(userDataFound);
        Optional<UserData> allUserData = userRepo.findByUserName("testUserName1");

        Assertions.assertEquals( allUserData,
              userDataFound);

    }
}