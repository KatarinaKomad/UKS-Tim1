package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uns.ac.rs.uks.dto.request.search.SearchRequest;
import uns.ac.rs.uks.dto.response.SearchResponse;
import uns.ac.rs.uks.service.SearchService;

import java.util.List;

@RestController
@RequestMapping(value = "/search")
@Slf4j
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping()
    public Page<SearchResponse> search(@Valid @RequestBody SearchRequest searchRequest) {
        return searchService.search(searchRequest);
    }

    @PostMapping("/repoCount")
    public Long getRepoCount(@Valid @RequestBody SearchRequest searchRequest) {
        return searchService.getRepoCount(searchRequest);
    }
    @PostMapping("/issueCount")
    public Long getIssueCount(@Valid @RequestBody SearchRequest searchRequest) {
        return searchService.getIssueCount(searchRequest);
    }
    @PostMapping("/prCount")
    public Long getPrCount(@Valid @RequestBody SearchRequest searchRequest) {
        return searchService.getPrCount(searchRequest);
    }
    @PostMapping("/userCount")
    public Long getUserCount(@Valid @RequestBody SearchRequest searchRequest) {
        return searchService.getUserCount(searchRequest);
    }
}
