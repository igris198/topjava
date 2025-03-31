package ru.javawebinar.topjava.web.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.javawebinar.topjava.model.Meal;

import java.io.IOException;
import java.util.List;

public class CustomJsonSerializer extends JsonSerializer<List<Meal>> {
    @Override
    public void serialize(List<Meal> meals, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();

        for (Meal meal : meals) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("dateTime", meal.getDateTime().toString());
            jsonGenerator.writeStringField("description", meal.getDescription());
            jsonGenerator.writeNumberField("calories", meal.getCalories());
            jsonGenerator.writeEndObject();

        }
        jsonGenerator.writeEndArray();
    }
}
