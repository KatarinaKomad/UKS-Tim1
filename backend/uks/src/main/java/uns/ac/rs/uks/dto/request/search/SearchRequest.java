package uns.ac.rs.uks.dto.request.search;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.dto.deserializer.KeywordDeserializer;
import uns.ac.rs.uks.dto.deserializer.SortTypeDeserializer;
import uns.ac.rs.uks.dto.request.search.keywords.Keyword;
import uns.ac.rs.uks.dto.request.search.sortTypes.SortType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    @JsonDeserialize(contentUsing = KeywordDeserializer.class)
    private List<Keyword> keywords;
    private List<String> query;
    private SearchType searchType;
    private String inputValue;
    @JsonDeserialize(using = SortTypeDeserializer.class)
    private SortType sortType;
    @PositiveOrZero
    private Integer page;
    @Positive
    private Integer size;
}
