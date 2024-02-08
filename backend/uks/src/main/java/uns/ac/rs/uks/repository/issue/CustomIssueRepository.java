package uns.ac.rs.uks.repository.issue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uns.ac.rs.uks.dto.request.search.SearchRequest;
import uns.ac.rs.uks.dto.response.SearchResponse;

public interface CustomIssueRepository {

    Page<SearchResponse> search(SearchRequest searchRequest, Pageable pageable);
    Long count(SearchRequest searchRequest);
}
