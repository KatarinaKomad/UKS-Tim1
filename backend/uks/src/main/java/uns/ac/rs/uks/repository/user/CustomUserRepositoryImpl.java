package uns.ac.rs.uks.repository.user;

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
import uns.ac.rs.uks.dto.request.search.sortTypes.SortType;
import uns.ac.rs.uks.dto.response.SearchResponse;
import uns.ac.rs.uks.mapper.UserMapper;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.util.SearchUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uns.ac.rs.uks.dto.request.search.keywords.RepoKeywords.CREATED;
import static uns.ac.rs.uks.dto.request.search.keywords.UserKeywords.*;
import static uns.ac.rs.uks.dto.request.search.sortTypes.UserSortType.*;

public class CustomUserRepositoryImpl implements CustomUserRepository {
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public Page<SearchResponse> search(SearchRequest searchRequest, Pageable pageable) {
        Map<String, Object> parameterValues = new HashMap<>();

        // construct conditions (e.g. AND r.name LIKE :repoName )
        String conditions = constructConditions(searchRequest, parameterValues);

        String select = "SELECT r FROM User r WHERE " + conditions;
        String count = "SELECT COUNT(r) FROM User r WHERE " + conditions;
        TypedQuery<User> selectQuery = entityManager.createQuery(select, User.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(count, Long.class);

        // set query param values
        for (Map.Entry<String, Object> entry : parameterValues.entrySet()) {
            selectQuery.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        // Apply pagination on select
        selectQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        selectQuery.setMaxResults(pageable.getPageSize());

        List<SearchResponse> result = new ArrayList<>(selectQuery.getResultList().stream().map(UserMapper::toDTO).toList());
        return new PageImpl<>(result, pageable, countQuery.getSingleResult());
    }
    @Override
    @Transactional
    public Long count(SearchRequest searchRequest) {
        Map<String, Object> parameterValues = new HashMap<>();

        String conditions = constructConditions(searchRequest, parameterValues);

        String count = "SELECT COUNT(r) FROM User r WHERE " + conditions;
        TypedQuery<Long> countQuery = entityManager.createQuery(count, Long.class);

        // set query param values
        for (Map.Entry<String, Object> entry : parameterValues.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        return countQuery.getSingleResult();
    }

    private String constructConditions(SearchRequest searchRequest, Map<String, Object> parameterValues) {
        StringBuilder conditions = new StringBuilder();
        // construct conditions (e.g. AND r.name LIKE :repoName )
        for (int i = 0; i < searchRequest.getKeywords().size(); i+=2) {
            String condition = constructCondition(searchRequest, i, parameterValues);
            if(i == 0){
                condition = condition.replace(Operations.NONE.toString(), "");;
            }
            conditions.append(condition);
        }
        // construct ORDER BY condition
        conditions.append(sortCondition(searchRequest.getSortType()));
        return conditions.toString();
    }

    private String sortCondition(SortType sortType) {
        if (sortType.equals(NEWEST)) {
            return " GROUP BY r.id ORDER BY r.createdAt DESC";
        } else if (sortType.equals(OLDEST)) {
            return " GROUP BY r.id ORDER BY r.createdAt ASC";
        } else if (sortType.equals(MOST_REPOS)) {
            return " GROUP BY r.id ORDER BY SIZE(r.repositories) DESC";
        } else if (sortType.equals(LEAST_REPOS)) {
            return " GROUP BY r.id ORDER BY SIZE(r.repositories) ASC";
        }
        return "";
    }

    private String constructCondition(SearchRequest searchRequest, int i,
                                      Map<String, Object> params) {

        Keyword keyword = searchRequest.getKeywords().get(i);
        String query = searchRequest.getQuery().get(i);
        String inputValue = searchRequest.getInputValue() != null
                ? searchRequest.getInputValue().toLowerCase() : "";
        Operations operation = i == 0 ? Operations.NONE: (Operations) searchRequest.getKeywords().get(i - 1);


        if (keyword.getName().equals(USERNAME.getName())) {
            String attribute = "LOWER(r.customUsername)";
            String paramName = "username";
            return SearchUtil.parseStringKeyword(keyword, query, operation, params, attribute, paramName);

        } else if (keyword.getName().equals(IN_EMAIL.getName())) {
            String attribute = "LOWER(r.email)";
            String paramName = "email";
            params.put(paramName, inputValue);
            return SearchUtil.likeCondition(operation, attribute, paramName);

        } else if (keyword.getName().equals(IN_NAME.getName())) {
            String attribute = "LOWER(CONCAT(r.firstName, ' ', r.lastName))";
            String paramName = "nameLike";
            params.put(paramName, inputValue);
            return SearchUtil.likeCondition(operation, attribute, paramName);

        } else if (keyword.getName().equals(FULLNAME.getName())) {
            String attribute = "CONCAT(r.firstName, ' ', r.lastName)";
            String paramName = "name";
            params.put(paramName, inputValue);
            return SearchUtil.parseStringKeyword(keyword, query, operation, params, attribute, paramName);

        } else if (keyword.getName().equals(REPO_COUNT.getName())) {
            String attribute = "r.repositories";
            String paramName = "repoCount";
            return SearchUtil.parseNumberCondition(keyword, query, operation, params, attribute, paramName);

        } else if (keyword.getName().equals(CREATED.getName())) {
            String attribute = "r.createdAt";
            String paramName = "created";
            return SearchUtil.parseDateCondition(keyword, query, operation, params, attribute, paramName);

        } else {
            return "";
        }
    }

}
