package uns.ac.rs.uks.repository.label;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CustomLabelRepositoryImpl implements CustomLabelRepository {
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public void deleteLabelRelations(Long labelId) {
        String select = "DELETE FROM ITEM_LABELS WHERE labels_id IN (SELECT id FROM LABEL WHERE id = :labelId) ";
        Query query = entityManager.createNativeQuery(select);
        query.setParameter("labelId", labelId);
        query.executeUpdate();
    }
}
