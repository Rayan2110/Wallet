package com.example.wallet.apicaller;

import com.example.wallet.api.ApiCaller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ApiCallerTest {


    @Test
    void testMakeApiCall()  {
        ApiCaller apiCaller = new ApiCaller();

        String expected = "{\"gecko_says\":\"(V3) To the Moon!\"}";

        String result = apiCaller.makeApiCall("https://api.coingecko.com/api/v3/ping");
        assertEquals(expected, result);

    }
}
