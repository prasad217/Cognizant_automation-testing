package h;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
 
public class RestAssuredCodeWithAI {
	
private static final String OPENAI_API_KEY = readAPIKeyFromFile();
	
    private static String readAPIKeyFromFile() {
        String apiKey = "";
        try (BufferedReader br = new BufferedReader(new FileReader("APIKEY.txt"))) {
            apiKey = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiKey;
    }
    public static void main(String[] args) {
    	 
    	String prompt = "Write a complete RestAssured code in Java."
    			+ "Do not provide explanations or setup steps. Only return the full Java code."
    			+ "The test should send a POST request to 'https://webapps.tekstac.com/HotelAPI/RoomService/addRoom'"
    			+ "with following data'roomId' as '11', 'hotelId' as 'H2101', 'roomType' as 'SINGLE', 'roomStatus' as 'AVAILABLE' , and 'roomPrice' as '2500'."
    			+ "with content type as 'application/x-www-form-urlencoded"
    			+ "Then, response should be validated for a successful status code and check response contains added details"
    			+ "remove the extra lines like ```java and ```";
    	
 
        String testScript = callOpenAI(prompt);
        if (testScript != null) {
            saveToFile("AddRoomTest.java", testScript);
            System.out.println("Test script generated successfully: AddRoomTest.java");
        } else {
            System.out.println("Failed to generate test script.");
        }
    }
 
    private static String callOpenAI(String prompt) {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        json.put("model", "gpt-4o-mini");
        json.put("messages", new JSONArray().put(new JSONObject().put("role", "user").put("content", prompt)));
        json.put("max_tokens", 450);
        json.put("temperature", 0.5);
 
        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .build();
 
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JSONObject responseJson = new JSONObject(response.body().string());
                JSONArray choices = responseJson.getJSONArray("choices");
                return choices.getJSONObject(0).getJSONObject("message").getString("content").trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    private static void saveToFile(String filename, String content) {
        try (FileWriter file = new FileWriter(filename)) {
            file.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}