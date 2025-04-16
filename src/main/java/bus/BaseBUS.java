/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bus;

import java.util.List;

/**
 *
 * @author hoang
 */
public interface BaseBUS <T, ID>{
    boolean insertEntity(T t);
    boolean updateEntity(T t);
    boolean deleteEntity(ID id);
    T getEntityById(ID id);
    List<T> getAllEntities();
}
//lam nay lam kia gibhub thu
//thu lai cai nua