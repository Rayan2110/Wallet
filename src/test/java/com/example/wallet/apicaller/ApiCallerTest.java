package com.example.wallet.apicaller;

import com.example.wallet.ApiCaller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiCallerTest {


    @Test
    void testMakeApiCall() throws IOException {
        ApiCaller apiCaller = new ApiCaller();

        // Simulez une URL et une connexion HTTP
        URL url = mock(URL.class);
        HttpURLConnection connection = mock(HttpURLConnection.class);

        // Configurez les comportements simulés
        when(url.openConnection()).thenReturn(connection);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        // Simulez une réponse de l'API
        String apiResponse = "{\"gecko_says\":\"(V3) To the Moon!\"}";
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(apiResponse.getBytes()));

        // Remplacez la création de l'URL réelle par le mock
        // Vous devez peut-être modifier votre méthode pour permettre l'injection de l'URL ou utiliser une bibliothèque comme PowerMock
        // Par exemple, pour PowerMock, vous utiliseriez:
        // PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url);

        // Appelez la méthode makeApiCall et vérifiez le résultat
        String result = apiCaller.makeApiCall("https://api.coingecko.com/api/v3/ping");
        assertEquals(apiResponse, result);

        // Vérifiez que la méthode openConnection a été appelée
        verify(url).openConnection();
    }
}
