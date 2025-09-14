package dev.lqwd.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class JsonSerialization {

    private final ObjectMapper objectMapper;

    public JsonSerialization() {
        this.objectMapper = new ObjectMapper();
    }

    public <T> List<T> serialize(String json, Class<T> type) throws JsonProcessingException {
        if (json == null || json.trim().isEmpty()) {
            return Collections.emptyList();
        }

        JsonNode rootNode = objectMapper.readTree(json);

        if (rootNode.isArray()) {
            return serializeArray(json, type);
        } else if (rootNode.isObject()) {
            return serializeObject(json, type);
        } else {
            throw new JsonParseException("Unsupported JSON format");
        }
    }

    private <T> List<T> serializeArray(String json, Class<T> type) throws JsonProcessingException {
        return objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    }

    private <T> List<T> serializeObject(String json, Class<T> type) throws JsonProcessingException {
        T singleObject = objectMapper.readValue(json, type);
        return List.of(singleObject);
    }
}
