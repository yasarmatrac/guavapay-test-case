package com.guavapay.authorization.deserializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonDeserialize(using = OAuth2TokenTypeDeserializer.class)
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class OAuth2TokenTypeMixin {
}


class OAuth2TokenTypeDeserializer extends JsonDeserializer<OAuth2TokenType> {

    @Override
    public OAuth2TokenType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode root = mapper.readTree(parser);
        return deserialize(parser, mapper, root);
    }

    private OAuth2TokenType deserialize(JsonParser parser, ObjectMapper mapper, JsonNode root)
            throws JsonParseException {
        String value = root.get("value").textValue();
        if (Objects.equals(value, OAuth2TokenType.ACCESS_TOKEN.getValue())) {
            return OAuth2TokenType.ACCESS_TOKEN;
        } else if (Objects.equals(value, OAuth2TokenType.REFRESH_TOKEN.getValue())) {
            return OAuth2TokenType.REFRESH_TOKEN;
        }
        return null;
    }
}
