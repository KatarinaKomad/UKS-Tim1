package uns.ac.rs.uks.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import uns.ac.rs.uks.dto.request.search.keywords.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KeywordDeserializer extends StdDeserializer<Keyword> {
    private final Map<String, Keyword> keywordMap = new HashMap<>();

    public KeywordDeserializer() {
        super(Keyword.class);
        // Initialize your keyword map with enum values
        for (Operations keyword: Operations.values()){
            keywordMap.put(keyword.getName(), keyword);
        }
        for (RepoKeywords keyword : RepoKeywords.values()) {
            keywordMap.put(keyword.getName(), keyword);
        }
        for (IssuePRKeywords keyword: IssuePRKeywords.values()){
            keywordMap.put(keyword.getName(), keyword);
        }
        for (UserKeywords keyword: UserKeywords.values()){
            keywordMap.put(keyword.getName(), keyword);
        }
    }

    @Override
    public Keyword deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String keywordName = node.asText();
        return keywordMap.get(keywordName);
    }
}
