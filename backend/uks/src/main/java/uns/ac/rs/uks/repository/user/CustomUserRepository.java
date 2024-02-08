package uns.ac.rs.uks.repository.user;

import uns.ac.rs.uks.dto.request.search.SearchRequest;
import uns.ac.rs.uks.dto.request.search.UserSearchDTO;

public interface CustomUserRepository {

    void search(SearchRequest dto);
}
