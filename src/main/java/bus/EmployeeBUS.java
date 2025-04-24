package bus;

import model.Address;
import model.EmployeeEntity;

import java.rmi.RemoteException;
import java.util.List;

public interface EmployeeBUS extends BaseBUS<EmployeeEntity, String> {
    @Override
    boolean insertEntity(EmployeeEntity employee) throws RemoteException;

    @Override
    boolean updateEntity(EmployeeEntity employee) throws RemoteException;

    @Override
    boolean deleteEntity(String id) throws RemoteException;

    @Override
    EmployeeEntity getEntityById(String id) throws RemoteException;

    @Override
    List<EmployeeEntity> getAllEntities() throws RemoteException;

    List<EmployeeEntity> getListEmployeeActive() throws RemoteException;

    EmployeeEntity checkLogin(String username, String password) throws RemoteException;

    List<EmployeeEntity> getEmployeesWithKeyword(String name, String phone, Address address, String email, String pass, String roleId, boolean active) throws RemoteException;

    EmployeeEntity findByEmail(String email) throws RemoteException;

    String hashPassword(String plainPassword) throws RemoteException;

    boolean varifyPassword(String plainPassword, String hashedPassword) throws RemoteException;

    // Hàm tạo mã xác nhận gồm 6 ký tự và số
    String generateConfirmationCode() throws RemoteException;
}
