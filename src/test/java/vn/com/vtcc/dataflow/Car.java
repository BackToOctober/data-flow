package vn.com.vtcc.dataflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    private String color;
    private String type;

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
//        Car car = new Car("yellow", "renault");
//        objectMapper.writeValue(new File("data/car.jsonl"), car);
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car2 = objectMapper.readValue(json, Car.class);
        System.out.println(car2.color + car2.type);
    }
}
