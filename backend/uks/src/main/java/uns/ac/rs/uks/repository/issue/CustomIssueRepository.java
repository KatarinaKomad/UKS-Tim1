package uns.ac.rs.uks.repository.issue;

import uns.ac.rs.uks.dto.request.search.IssuePrSearchDTO;
import uns.ac.rs.uks.dto.request.search.SearchRequest;

public interface CustomIssueRepository {

    void search(SearchRequest dto);
}
