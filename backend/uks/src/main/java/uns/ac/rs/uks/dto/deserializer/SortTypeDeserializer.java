package uns.ac.rs.uks.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import uns.ac.rs.uks.dto.request.search.sortTypes.RepoSortType;
import uns.ac.rs.uks.dto.request.search.sortTypes.SortType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SortTypeDeserializer extends StdDeserializer<SortType> {
    private final Map<String, SortType> sortTypeMap = new HashMap<>();

    public SortTypeDeserializer() {
        super(SortType.class);
        // Initialize your sort type map with enum values
        for (RepoSortType sortType : RepoSortType.values()) {
            sortTypeMap.put(sortType.getName(), sortType);
        }
    }

    @Override
    public SortType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String sortTypeName = node.asText();
        return sortTypeMap.get(sortTypeName);
    }
}

