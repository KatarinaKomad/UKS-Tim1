package uns.ac.rs.uks.repository.issue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import uns.ac.rs.uks.dto.request.search.RepoSearchDTO;
import uns.ac.rs.uks.dto.request.search.SearchRequest;
import uns.ac.rs.uks.repository.repo.CustomRepoRepository;

public class CustomIssueRepositoryImpl implements CustomIssueRepository {
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public void search(SearchRequest dto) {
        String select = "DELETE FROM ITEM_LABELS WHERE labels_id IN (SELECT id FROM LABEL WHERE id = :labelId) ";
        Query query = entityManager.createNativeQuery(select);
        query.setParameter("labelId", dto);
        query.executeUpdate();
    }
}
