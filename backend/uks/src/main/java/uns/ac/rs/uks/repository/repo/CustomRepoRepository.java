package uns.ac.rs.uks.repository.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uns.ac.rs.uks.dto.request.search.SearchRequest;
import uns.ac.rs.uks.dto.response.SearchResponse;
import uns.ac.rs.uks.model.Repo;

import java.util.List;

public interface CustomRepoRepository {

    Page<SearchResponse> search(SearchRequest dto, Pageable pageable);
}
