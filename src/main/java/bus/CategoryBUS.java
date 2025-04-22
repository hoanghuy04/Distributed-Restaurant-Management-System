/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dal.CategoryDAL;
import model.CategoryEntity;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author pc
 */
public class CategoryBUS implements BaseBUS<CategoryEntity, String>{
    private CategoryDAL categoryDAL;
    public CategoryBUS(EntityManager entityManager){
        this.categoryDAL = new CategoryDAL(entityManager);
    }

    @Override
    public boolean insertEntity(CategoryEntity category) {
        return categoryDAL.insert(category);
    }

    @Override
    public boolean updateEntity(CategoryEntity category) {
        return categoryDAL.update(category);
    }

    @Override
    public boolean deleteEntity(String id) {
        return categoryDAL.deleteById(id);
    }
    
    

    @Override
    public CategoryEntity getEntityById(String id) {
        return categoryDAL.findById(id);
    }

    @Override
    public List<CategoryEntity> getAllEntities() {
        return categoryDAL.findAll();
    }
    
    public CategoryEntity findByName(String name) {
        return categoryDAL.findByName(name);
    }

}
