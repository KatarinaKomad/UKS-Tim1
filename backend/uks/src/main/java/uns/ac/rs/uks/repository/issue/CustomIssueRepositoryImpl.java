package uns.ac.rs.uks.repository.issue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import uns.ac.rs.uks.dto.request.search.SearchRequest;
import uns.ac.rs.uks.dto.request.search.keywords.IssuePRKeywords;
import uns.ac.rs.uks.dto.request.search.keywords.Keyword;
import uns.ac.rs.uks.dto.request.search.keywords.Operations;
import uns.ac.rs.uks.dto.request.search.sortTypes.SortType;
import uns.ac.rs.uks.dto.response.SearchResponse;
import uns.ac.rs.uks.mapper.IssueMapper;
import uns.ac.rs.uks.model.Issue;
import uns.ac.rs.uks.model.State;
import uns.ac.rs.uks.util.SearchUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uns.ac.rs.uks.dto.request.search.keywords.IssuePRKeywords.*;
import static uns.ac.rs.uks.dto.request.search.sortTypes.IssuePrSortType.LEAST_COMMENTS;
import static uns.ac.rs.uks.dto.request.search.sortTypes.IssuePrSortType.MOST_COMMENTS;
import static uns.ac.rs.uks.dto.request.search.sortTypes.IssuePrSortType.NEWEST;
import static uns.ac.rs.uks.dto.request.search.sortTypes.IssuePrSortType.OLDEST;

public class CustomIssueRepositoryImpl implements CustomIssueRepository {
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public Page<SearchResponse> search(SearchRequest searchRequest, Pageable pageable) {
        StringBuilder select  = new StringBuilder("SELECT r FROM Issue r ");
        StringBuilder count  = new StringBuilder("SELECT COUNT(r) FROM Issue r ");

        if(searchRequest.getKeywords().contains(ASSIGNEE)){
            String joinAssignees = "JOIN r.assignees a ";
            select.append(joinAssignees);
            count.append(joinAssignees);
        }
        if(searchRequest.getKeywords().contains(LABEL)){
            String joinLabels = "JOIN r.labels l ";
            select.append(joinLabels);
            count.append(joinLabels);
        }
        select.append("WHERE ");
        count.append("WHERE ");

        Map<String, Object> parameterValues = new HashMap<>();
        String conditions = constructConditions(searchRequest, parameterValues);
        select.append(conditions);
        count.append(conditions);

        TypedQuery<Issue> selectQuery = entityManager.createQuery(select.toString(), Issue.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(count.toString(), Long.class);

        // set query param values
        for (Map.Entry<String, Object> entry : parameterValues.entrySet()) {
            selectQuery.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        // Apply pagination on select
        selectQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        selectQuery.setMaxResults(pageable.getPageSize());

        List<SearchResponse> result = new ArrayList<>(selectQuery.getResultList().stream().map(IssueMapper::toDTO).toList());
        return new PageImpl<>(result, pageable, countQuery.getSingleResult());
    }

    @Override
    @Transactional
    public Long count(SearchRequest searchRequest) {
        Map<String, Object> parameterValues = new HashMap<>();

        String conditions = constructConditions(searchRequest, parameterValues);

        String count = "SELECT COUNT(r) FROM Issue r WHERE " + conditions;
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
        conditions.append(" AND r.repository.isPublic = true ");
        conditions.append(sortCondition(searchRequest.getSortType()));
        return conditions.toString();
    }

    private String sortCondition(SortType sortType) {
        if (sortType.equals(NEWEST)) {
            return " GROUP BY r.id ORDER BY r.createdAt DESC";
        } else if (sortType.equals(OLDEST)) {
            return " GROUP BY r.id ORDER BY r.createdAt ASC";
        } else if (sortType.equals(MOST_COMMENTS)) {
            return " GROUP BY r.id ORDER BY SIZE(r.comments) DESC";
        } else if (sortType.equals(LEAST_COMMENTS)) {
            return " GROUP BY r.id ORDER BY SIZE(r.comments) ASC";
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



        if (keyword.getName().equals(IssuePRKeywords.IN_NAME.getName())) {
            String attribute = "LOWER(r.name)";
            String paramName = "inName";
            params.put(paramName, inputValue);
            return SearchUtil.likeCondition(operation, attribute, paramName);

        } else if (keyword.getName().equals(IN_DESCRIPTION.getName())) {
            String attribute = "LOWER(r.description)";
            String paramName = "inDescription";
            params.put(paramName, inputValue);
            return SearchUtil.likeCondition(operation, attribute, paramName);

        } else if (keyword.getName().equals(IS_CLOSED.getName())) {
            String attribute = "r.state";
            return SearchUtil.stateCondition(operation, attribute, State.CLOSE);

        } else if (keyword.getName().equals(IS_OPEN.getName())) {
            String attribute = "r.state";
            return SearchUtil.stateCondition(operation, attribute, State.OPEN);

        } else if (keyword.getName().equals(NO_LABEL.getName())) {      // ??
            String attribute = "r.labels";
            String paramName = "labelsCount";
            params.put(paramName, "0");
            return SearchUtil.sizeCondition(operation, attribute, paramName, "=");
        } else if (keyword.getName().equals(NO_MILESTONE.getName())) {
            String attribute = "r.milestone";
            if (operation == Operations.NOT) {
                return SearchUtil.isNotNullCondition(Operations.AND, attribute);
            } else {
                return SearchUtil.isNullCondition(operation, attribute);
            }
        } else if (keyword.getName().equals(AUTHOR.getName())) {
            String attribute = "LOWER(r.author.customUsername)";
            String paramName = "authorUsername";
            return SearchUtil.parseStringKeyword(keyword, query, operation, params, attribute, paramName);

        } else if (keyword.getName().equals(ASSIGNEE.getName())) {
            String attribute = "a.customUsername";
            String paramName = "assigneeUsername";
            return SearchUtil.parseStringKeyword(keyword, query, operation, params, attribute, paramName);

        } else if (keyword.getName().equals(LABEL.getName())) {
            String attribute = "l.name";
            String paramName = "labelName";
            return SearchUtil.parseStringKeyword(keyword, query, operation, params, attribute, paramName);

        } else if (keyword.getName().equals(MILESTONE.getName())) {
            String attribute = "LOWER(r.milestone.name)";
            String paramName = "milestoneName";
            return SearchUtil.parseStringKeyword(keyword, query, operation, params, attribute, paramName);


        } else if (keyword.getName().equals(CREATED.getName())) {
            String attribute = "r.createdAt";
            String paramName = "created";
            return SearchUtil.parseDateCondition(keyword, query, operation, params, attribute, paramName);

        }
        return "";

    }

}
