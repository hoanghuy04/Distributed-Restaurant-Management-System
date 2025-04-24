package bus.impl;

import bus.BaseBUS;
import common.Constants;
import dal.EmployeeDAL;
import jakarta.persistence.EntityManager;
import model.EmployeeEntity;
import org.mindrot.jbcrypt.BCrypt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Random;

public class EmployeeBUSImpl extends UnicastRemoteObject implements bus.EmployeeBUS {

    private EmployeeDAL employeeDAL;
    private EntityManager em;

    public EmployeeBUSImpl(EntityManager entityManager)  throws RemoteException {
        this.employeeDAL = new EmployeeDAL(entityManager);
    }

    @Override
    public EmployeeEntity insertEntity(EmployeeEntity employee)  throws RemoteException {
        return employeeDAL.insert(employee);
    }

    @Override
    public boolean updateEntity(EmployeeEntity employee)  throws RemoteException {
        return employeeDAL.update(employee);
    }

    @Override
    public boolean deleteEntity(String id)  throws RemoteException {
        return employeeDAL.deleteById(id);
    }

    @Override
    public EmployeeEntity getEntityById(String id)  throws RemoteException {
        return employeeDAL.findById(id);
    }

    @Override
    public List<EmployeeEntity> getAllEntities()  throws RemoteException {
        return employeeDAL.findAll();
    }

    @Override
    public List<EmployeeEntity> getListEmployeeActive()  throws RemoteException {
        return employeeDAL.getListEmployeeActive();
    }

    @Override
    public EmployeeEntity checkLogin(String username, String password)  throws RemoteException {
        return employeeDAL.findAll().stream().filter(e -> {
                    try {
                        return e.getEmployeeId().equals(username) && varifyPassword(password, e.getPassword());
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<EmployeeEntity> getEmployeesWithKeyword(String name, String phone, String address, String email, String pass, String roleId, boolean active)  throws RemoteException {
        return employeeDAL.getEmployeesWithKeyword(name, phone, address, email, pass, roleId, active);
    }

    @Override
    public EmployeeEntity findByEmail(String email)  throws RemoteException {
        return employeeDAL.findAll().stream().filter(e -> e.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String hashPassword(String plainPassword)  throws RemoteException {
        return BCrypt.hashpw(plainPassword, Constants.SALT);
    }

    @Override
    public boolean varifyPassword(String plainPassword, String hashedPassword)  throws RemoteException {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    // Hàm tạo mã xác nhận gồm 6 ký tự và số
    @Override
    public String generateConfirmationCode()  throws RemoteException {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Tập hợp ký tự và số
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        // Tạo mã gồm 6 ký tự
        for (int i = 0; i < 6; i++)  {
            int index = random.nextInt(characters.length()); // Chọn ngẫu nhiên một chỉ số
            code.append(characters.charAt(index)); // Thêm ký tự vào mã
        }

        return code.toString(); // Trả về mã xác nhận
    }
}
