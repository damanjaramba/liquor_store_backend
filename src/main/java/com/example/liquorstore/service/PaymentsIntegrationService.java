package com.example.liquorstore.service;

import com.example.liquorstore.helpers.PhoneNumberHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;


@Service
public class PaymentsIntegrationService {
    @Autowired
    SessionUserService sessionUserService;

    public String generateAccessToken() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        //   RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .method("GET", null)
                .addHeader("Authorization", "Basic TXpaQmc5QlVEVDAxY0d2VmhtZmdCRjRYM3pJbmR0RFZUeEZQRzBKN1BibWVjbW95OkR2SzVnS255TmRTTnRWUHJUMFY0djZacnpHQWM3bU5EcjJ1Z0dVQ2p0WWlpQldJc1hneVJMU1ZzRWI3Sm5tNGc=")
                .addHeader("Cookie", "incap_ses_1020_2742146=f5xmfgLRXl8REr274MQnDiLbCGgAAAAAY6oZ1NDbcEtNTmBHUMpyXQ==; visid_incap_2742146=bNsHWDTSSKGE/RrZ374yBCLbCGgAAAAAQUIPAAAAAABY3UbU2f4mES4nL4s6ghDW")
                .build();
        Response response = client.newCall(request).execute();
        Gson gson = new Gson();
        String string = response.body().string();
        JsonObject jsonObject = gson.fromJson(string, JsonObject.class);
        String accessToken = jsonObject.get("access_token").getAsString();
        return accessToken;
    }

    public ResponseEntity<?> stkPush(String mobileNumber, BigDecimal amount) throws IOException {
        Gson gson = new Gson();
        if (mobileNumber == null || mobileNumber.isEmpty()) {
            mobileNumber = sessionUserService.getSessionUser().getMobileNumber();
        }
        PhoneNumberHelper phoneNumberHelper = new PhoneNumberHelper();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        String requestBodyContent = String.format(
                "{" +
                        "\"BusinessShortCode\": 174379," +
                        "\"Password\": \"MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMjUwNDIzMTUzMjUy\"," +
                        "\"Timestamp\": \"20250423153252\"," +
                        "\"TransactionType\": \"CustomerPayBillOnline\"," +
                        "\"Amount\": %s," +
                        "\"PartyA\": 254708374149," +
                        "\"PartyB\": 174379," +
                        "\"PhoneNumber\": %s," +
                        "\"CallBackURL\": \"https://mydomain.com/path\"," +
                        "\"AccountReference\": \"DAMARIS' LIQUOR STORE\"," +
                        "\"TransactionDesc\": \"Payment of LIQUOR\"" +
                        "}",
                amount, phoneNumberHelper.normalizeMobileNumber(mobileNumber)
        );
        RequestBody body = RequestBody.create(mediaType, requestBodyContent);

        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + generateAccessToken())
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        if (response.isSuccessful()) {
            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
            // Convert JsonObject to a Map for Spring compatibility
            Map<String, Object> responseMap = gson.fromJson(jsonObject, Map.class);
            return ResponseEntity.ok(responseMap);
        } else {
            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
            // Convert JsonObject to a Map for Spring compatibility
            Map<String, Object> errorMap = gson.fromJson(jsonObject, Map.class);
            return ResponseEntity.status(response.code()).body(errorMap);
        }
    }
}
