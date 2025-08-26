package h;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AddRoomTest {
    public static void main(String[] args) {
        RestAssured.baseURI = "https://webapps.tekstac.com/HotelAPI/RoomService";

        Response response = given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("roomId", "11")
            .formParam("hotelId", "H2101")
            .formParam("roomType", "SINGLE")
            .formParam("roomStatus", "AVAILABLE")
            .formParam("roomPrice", "2500")
        .when()
            .post("/addRoom")
        .then()
            .statusCode(200)
            .body("rooms.room.hotelId", hasItem("H2101"))
            .body("rooms.room.roomId", hasItem("11"))
            .body("rooms.room.roomType", hasItem("SINGLE"))
            .body("rooms.room.roomStatus", hasItem("AVAILABLE"))
            .body("rooms.room.roomPrice", hasItem("2500.0")).extract().response();
        System.out.println(response.asPrettyString());
    }
}