/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author hoang
 */
public interface BaseBUS <T, ID>  {
    boolean insertEntity(T t) throws RemoteException;
    boolean updateEntity(T t) throws RemoteException;
    boolean deleteEntity(ID id) throws RemoteException;
    T getEntityById(ID id) throws RemoteException ;
    List<T> getAllEntities() throws RemoteException;
}
