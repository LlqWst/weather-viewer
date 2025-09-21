package dev.lqwd.service.weahter_api.infrastructure;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lqwd.exception.SerializationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class JsonResponseParser {

    private final ObjectMapper objectMapper;

    public JsonResponseParser() {
        this.objectMapper = new ObjectMapper();
    }

    public <T> List<T> deserialize(String json, Class<T> type) {
        if (json == null || json.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            if (rootNode.isArray()) {
                return deserializeArray(json, type);
            } else if (rootNode.isObject()) {
                return deserializeObject(json, type);
            }
            throw new JsonParseException("Unsupported JSON format");

        } catch (JsonProcessingException e) {
            log.warn("Failed to parse json: {}", json, e);
            throw new SerializationException("Failed to parse json", e);
        }
    }

    private <T> List<T> deserializeArray(String json, Class<T> type) throws JsonProcessingException {
        return objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    }

    private <T> List<T> deserializeObject(String json, Class<T> type) throws JsonProcessingException {
        T singleObject = objectMapper.readValue(json, type);
        return List.of(singleObject);
    }
}
