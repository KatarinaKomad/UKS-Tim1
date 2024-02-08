package uns.ac.rs.uks.repository.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import uns.ac.rs.uks.dto.request.search.SearchRequest;
import uns.ac.rs.uks.dto.request.search.keywords.Keyword;
import uns.ac.rs.uks.dto.request.search.keywords.Operations;
import uns.ac.rs.uks.dto.request.search.sortTypes.RepoSortType;
import uns.ac.rs.uks.dto.response.SearchResponse;
import uns.ac.rs.uks.mapper.RepoMapper;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.util.SearchUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uns.ac.rs.uks.dto.request.search.keywords.RepoKeywords.*;

public class CustomRepoRepositoryImpl implements CustomRepoRepository {
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public Page<SearchResponse> search(SearchRequest searchRequest, Pageable pageable) {
        StringBuilder conditions  = new StringBuilder();
        Map<String, Object> parameterValues = new HashMap<>();

        // construct conditions (e.g. AND r.name LIKE :repoName )
        for (int i = 0; i < searchRequest.getKeywords().size(); i+=2) {
            String condition = constructCondition(searchRequest, i, parameterValues);
            if(i == 0){
                condition = condition.replace(Operations.NONE.toString(), "");;
            }
            conditions.append(condition);
        }
        // construct ORDER BY condition
        conditions.append(sortCondition((RepoSortType) searchRequest.getSortType()));

        String select = "SELECT r FROM Repo r WHERE " + conditions;
        String count = "SELECT COUNT(r) FROM Repo r WHERE " + conditions;
        TypedQuery<Repo> selectQuery = entityManager.createQuery(select, Repo.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(count, Long.class);

        // set query param values
        for (Map.Entry<String, Object> entry : parameterValues.entrySet()) {
            selectQuery.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        // Apply pagination on select
        selectQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        selectQuery.setMaxResults(pageable.getPageSize());

        List<SearchResponse> result = new ArrayList<>(selectQuery.getResultList().stream().map(RepoMapper::toDTO).toList());
        return new PageImpl<>(result, pageable, countQuery.getSingleResult());
    }

    private String sortCondition(RepoSortType sortType) {
        switch (sortType) {
            case NEWEST -> { return " GROUP BY r.id ORDER BY r.createdAt DESC"; }
            case OLDEST -> { return " GROUP BY r.id ORDER BY r.createdAt ASC"; }
            case MOST_STARS -> { return " GROUP BY r.id ORDER BY SIZE(r.staredBy) DESC"; }
            case LEAST_STARS -> { return " GROUP BY r.id ORDER BY SIZE(r.staredBy) ASC"; }
            case MOST_FORKS -> { return " GROUP BY r.id ORDER BY SIZE(r.forkChildren) DESC"; }
            case LEAST_FORKS -> { return " GROUP BY r.id ORDER BY SIZE(r.forkChildren) ASC"; }
            default -> { return ""; }
        }
    }

    private String constructCondition(SearchRequest searchRequest, int i,
                                      Map<String, Object> params) {

        Keyword keyword = searchRequest.getKeywords().get(i);
        String query = searchRequest.getQuery().get(i);
        String inputValue = searchRequest.getInputValue() != null
                                    ? searchRequest.getInputValue().toLowerCase() : "";
        Operations operation = i == 0 ? Operations.NONE: (Operations) searchRequest.getKeywords().get(i - 1);


        if (keyword.getName().equals(REPO.getName())) {
            String attribute = "LOWER(r.name)";
            String paramName = "repoName";
            return SearchUtil.parseStringKeyword(keyword, query, operation, params, attribute, paramName);

        } else if (keyword.getName().equals(OWNER.getName())) {
            String attribute = "LOWER(r.owner.customUsername)";
            String paramName = "ownerName";
            return SearchUtil.parseStringKeyword(keyword, query, operation, params, attribute, paramName);

        } else if (keyword.getName().equals(IN_NAME.getName())) {
            String attribute = "LOWER(r.name)";
            String paramName = "inRepoName";
            params.put(paramName, inputValue);
            return SearchUtil.likeCondition(operation, attribute, paramName);

        } else if (keyword.getName().equals(IN_DESCRIPTION.getName())) {
            String attribute = "LOWER(r.description)";
            String paramName = "repoDescription";
            params.put(paramName, inputValue);
            return SearchUtil.likeCondition(operation, attribute, paramName);

        } else if (keyword.getName().equals(FORK_TRUE.getName())) {
            if (operation == Operations.NOT) {
                String attribute = "r.forkParent";
                return SearchUtil.isNullCondition(Operations.AND, attribute);
            }
        } else if (keyword.getName().equals(FORK_ONLY.getName())) {
            if (operation == Operations.AND) {
                String attribute = "r.forkParent";
                return SearchUtil.isNotNullCondition(operation, attribute);
            }
        } else if (keyword.getName().equals(NUMBER_FORKS.getName())) {
            String attribute = "r.forkChildren";
            String paramName = "forkCount";
            return SearchUtil.parseNumberCondition(keyword, query, operation, params, attribute, paramName);

        } else if (keyword.getName().equals(NUMBER_STARS.getName())) {
            String attribute = "r.staredBy";
            String paramName = "starCount";
            return SearchUtil.parseNumberCondition(keyword, query, operation, params, attribute, paramName);

        } else if (keyword.getName().equals(NUMBER_WATCHERS.getName())) {
            String attribute = "r.watchers";
            String paramName = "watchCount";
            return SearchUtil.parseNumberCondition(keyword, query, operation, params, attribute, paramName);

        } else if (keyword.getName().equals(IS_PRIVATE.getName())) {
            String attribute = "r.isPublic";
            return SearchUtil.booleanCondition(operation, attribute, false);

        } else if (keyword.getName().equals(IS_PUBLIC.getName())) {
            String attribute = "r.isPublic";
            return SearchUtil.booleanCondition(operation, attribute, true);

        } else if (keyword.getName().equals(CREATED.getName())) {
            String attribute = "r.createdAt";
            String paramName = "created";
            return SearchUtil.parseDateCondition(keyword, query, operation, params, attribute, paramName);

        } else {
            return "";
        }
        return "";
    }


}
