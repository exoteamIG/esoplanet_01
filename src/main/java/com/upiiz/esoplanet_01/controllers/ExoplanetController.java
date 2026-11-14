package com.upiiz.esoplanet_01.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/exoplanets")
@CrossOrigin(origins = "*")
public class ExoplanetController {

    private final RestTemplate restTemplate;

    @Value("${ia.api.url:https://gloriaitzel0609-exoplanet-ia.hf.space/predict}")
    private String fastApiUrl;

    public ExoplanetController() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(60000); // 60s para despertar Hugging Face si está dormido
        factory.setReadTimeout(60000);
        this.restTemplate = new RestTemplate(factory);
    }

    @PostMapping("/classify")
    public ResponseEntity<?> classifyExoplanet(@RequestBody Map<String, Object> datos) {
        try {
        System.out.println("Llamando a: " + fastApiUrl);
        System.out.println("Datos: " + datos);
        ResponseEntity<Object> response = restTemplate.postForEntity(fastApiUrl, datos, Object.class);
        return ResponseEntity.ok(response.getBody());
    } catch (HttpStatusCodeException e) {
        System.out.println("HTTP " + e.getStatusCode() + " -> " + e.getResponseBodyAsString());
        return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(Map.of("error", e.toString()));
    }
    }
}
