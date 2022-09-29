package io.akeyless.examples.examplesdkencryptionasaservice.rest;

import io.akeyless.client.ApiClient;
import io.akeyless.client.ApiException;
import io.akeyless.client.Configuration;
import io.akeyless.client.api.V2Api;
import io.akeyless.client.model.Auth;
import io.akeyless.client.model.AuthOutput;
import io.akeyless.client.model.Encrypt;
import io.akeyless.client.model.EncryptOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @Autowired
    private Environment env;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World RESTful with Spring Boot";
    }

    @RequestMapping("/encrypted")
    public String helloEnc() {
        String plainText = "Hello World RESTful with Spring Boot Encrypted!!!!";
        String akeylessServerUrl = env.getProperty("AKEYLESS_API_URL", "https://api.akeyless.io");
        ApiClient client = Configuration.getDefaultApiClient();
        client.setBasePath(akeylessServerUrl);

        V2Api apiInstance = new V2Api(client);
        Auth bodyAuth = new Auth();
        bodyAuth.setAccessId(env.getProperty("AKEYLESS_ACCESS_ID"));
        bodyAuth.setAccessKey(env.getProperty("AKEYLESS_ACCESS_KEY"));
        String token = null;

        try {
            AuthOutput result = apiInstance.auth(bodyAuth);
            token = result.getToken();
        } catch  (ApiException e) {
            System.err.println("Exception when calling V2Api#auth");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }

        Encrypt bodyEncrypt = new Encrypt();
        bodyEncrypt.setKeyName("/MyKey256");
        bodyEncrypt.setPlaintext(plainText);
        bodyEncrypt.setToken(token);
        String output = null;

        try {
            EncryptOutput result = apiInstance.encrypt(bodyEncrypt);
            output = result.getResult();
        } catch (ApiException e) {
            System.err.println("Exception when calling V2Api#encrypt");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }

        return output;
    }
}