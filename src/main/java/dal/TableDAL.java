package dal;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import model.CustomerEntity;
import model.TableEntity;
import util.IDGeneratorUtil;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class TableDAL implements BaseDAL<TableEntity, String>{
    private EntityManager em;
    @Override
    public boolean insert(TableEntity tableEntity) {
        tableEntity.setTableId(IDGeneratorUtil.generateSimpleID("T","tables","table_id",em));
        return BaseDAL.executeTransaction(em,()->em.persist(tableEntity));
    }

    @Override
    public boolean update(TableEntity tableEntity) {
        return BaseDAL.executeTransaction(em,()->em.merge(tableEntity));
    }

    @Override
    public boolean deleteById(String s) {
        return BaseDAL.executeTransaction(em, () -> {
            TableEntity entity = em.find(TableEntity.class, s);
            if (entity != null) {
                em.remove(entity);
            }
        });
    }

    @Override
    public Optional<TableEntity> findById(String s) {
        return Optional.ofNullable(em.find(TableEntity.class, s));
    }

    @Override
    public List<TableEntity> findAll() {
        return em.createNamedQuery("TableEntity.findAll", TableEntity.class).getResultList();
    }
}
