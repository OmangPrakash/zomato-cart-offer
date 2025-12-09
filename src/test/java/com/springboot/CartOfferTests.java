package com.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.ApplyOfferRequest;
import com.springboot.controller.ApplyOfferResponse;
import com.springboot.controller.OfferRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CartOfferTests {

    private ObjectMapper mapper = new ObjectMapper();

    // Test 1: Basic FLATX offer test
    @Test
    public void testFlatXOffer() throws Exception {
        // Create offer
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        OfferRequest offerRequest = new OfferRequest(1, "FLATX", 50, segments);
        createOffer(offerRequest);

        // Apply offer
        ApplyOfferRequest applyRequest = new ApplyOfferRequest();
        applyRequest.setCart_value(200);
        applyRequest.setUser_id(1);
        applyRequest.setRestaurant_id(1);

        ApplyOfferResponse response = applyOfferToCart(applyRequest);
        assertEquals(150, response.getCart_value());
    }

    // Test 2: Basic FLATPERCENT offer test
    @Test
    public void testFlatPercentOffer() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        OfferRequest offerRequest = new OfferRequest(2, "FLATPERCENT", 20, segments);
        createOffer(offerRequest);

        ApplyOfferRequest applyRequest = new ApplyOfferRequest();
        applyRequest.setCart_value(500);
        applyRequest.setUser_id(1);
        applyRequest.setRestaurant_id(2);

        ApplyOfferResponse response = applyOfferToCart(applyRequest);
        assertEquals(400, response.getCart_value());
    }

    // Test 3: No offer scenario
    @Test
    public void testNoOfferApplied() throws Exception {
        ApplyOfferRequest applyRequest = new ApplyOfferRequest();
        applyRequest.setCart_value(300);
        applyRequest.setUser_id(1);
        applyRequest.setRestaurant_id(99); // No offer for this restaurant

        ApplyOfferResponse response = applyOfferToCart(applyRequest);
        assertEquals(300, response.getCart_value());
    }

    // Test 4: Offer for multiple segments
    @Test
    public void testOfferForMultipleSegments() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        segments.add("p2");
        OfferRequest offerRequest = new OfferRequest(3, "FLATX", 30, segments);
        
        int status = createOffer(offerRequest);
        assertEquals(200, status);
    }

    // Test 5: FLATX when cart equals offer value
    @Test
    public void testFlatXCartEqualsOffer() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        OfferRequest offerRequest = new OfferRequest(4, "FLATX", 100, segments);
        createOffer(offerRequest);

        ApplyOfferRequest applyRequest = new ApplyOfferRequest();
        applyRequest.setCart_value(100);
        applyRequest.setUser_id(1);
        applyRequest.setRestaurant_id(4);

        ApplyOfferResponse response = applyOfferToCart(applyRequest);
        assertEquals(0, response.getCart_value());
    }

    // Test 6: 50% discount test
    @Test
    public void testFiftyPercentDiscount() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        OfferRequest offerRequest = new OfferRequest(5, "FLATPERCENT", 50, segments);
        createOffer(offerRequest);

        ApplyOfferRequest applyRequest = new ApplyOfferRequest();
        applyRequest.setCart_value(1000);
        applyRequest.setUser_id(1);
        applyRequest.setRestaurant_id(5);

        ApplyOfferResponse response = applyOfferToCart(applyRequest);
        assertEquals(500, response.getCart_value());
    }

    // Test 7: User segment mismatch
    @Test
    public void testUserSegmentMismatch() throws Exception {
        // Create offer only for p2 segment
        List<String> segments = new ArrayList<>();
        segments.add("p2");
        OfferRequest offerRequest = new OfferRequest(6, "FLATX", 100, segments);
        createOffer(offerRequest);

        // User 1 is in p1 segment, so offer shouldn't apply
        ApplyOfferRequest applyRequest = new ApplyOfferRequest();
        applyRequest.setCart_value(500);
        applyRequest.setUser_id(1);
        applyRequest.setRestaurant_id(6);

        ApplyOfferResponse response = applyOfferToCart(applyRequest);
        assertEquals(500, response.getCart_value());
    }

    // Test 8: Large cart value
    @Test
    public void testLargeCartValue() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("p1");
        OfferRequest offerRequest = new OfferRequest(7, "FLATPERCENT", 15, segments);
        createOffer(offerRequest);

        ApplyOfferRequest applyRequest = new ApplyOfferRequest();
        applyRequest.setCart_value(10000);
        applyRequest.setUser_id(1);
        applyRequest.setRestaurant_id(7);

        ApplyOfferResponse response = applyOfferToCart(applyRequest);
        assertEquals(8500, response.getCart_value());
    }

    // Helper method to create offer
    private int createOffer(OfferRequest offerRequest) throws Exception {
        String urlString = "http://localhost:9001/api/v1/offer";
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        String jsonData = mapper.writeValueAsString(offerRequest);
        OutputStream os = con.getOutputStream();
        os.write(jsonData.getBytes());
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            in.close();
        }
        
        return responseCode;
    }

    // Helper method to apply offer to cart
    private ApplyOfferResponse applyOfferToCart(ApplyOfferRequest applyRequest) throws Exception {
        String urlString = "http://localhost:9001/api/v1/cart/apply_offer";
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        String jsonData = mapper.writeValueAsString(applyRequest);
        OutputStream os = con.getOutputStream();
        os.write(jsonData.getBytes());
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            return mapper.readValue(response.toString(), ApplyOfferResponse.class);
        }
        
        throw new Exception("Failed to apply offer");
    }
}