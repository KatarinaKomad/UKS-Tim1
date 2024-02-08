package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.search.SearchRequest;
import uns.ac.rs.uks.dto.response.SearchResponse;
import uns.ac.rs.uks.repository.issue.IssueRepository;
import uns.ac.rs.uks.repository.repo.RepoRepository;
import uns.ac.rs.uks.repository.user.UserRepository;

@Service
public class SearchService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RepoRepository repoRepository;
    @Autowired
    private IssueRepository issueRepository;

    public Page<SearchResponse> search(SearchRequest searchRequest) {
        if(searchRequest.getKeywords().isEmpty()){
            return null;
        }
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());

        switch (searchRequest.getSearchType()) {
            case REPO -> { return repoRepository.search(searchRequest, pageable); }
            case USER -> { return userRepository.search(searchRequest, pageable); }
            case ISSUE -> { return issueRepository.search(searchRequest, pageable); }
//            case PR -> { return null; }
            default -> { return null; }
        }
    }

    public Long getUserCount(SearchRequest searchRequest) {
        return userRepository.count(searchRequest);
    }

    public Long getPrCount(SearchRequest searchRequest) {
//        return pullRequestRepository.count(searchRequest);
        return 0L;
    }

    public Long getRepoCount(SearchRequest searchRequest) {
        return repoRepository.count(searchRequest);
    }

    public Long getIssueCount(SearchRequest searchRequest) {
        return issueRepository.count(searchRequest);
    }

}
