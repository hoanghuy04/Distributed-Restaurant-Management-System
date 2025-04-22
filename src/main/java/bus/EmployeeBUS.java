package bus;

import common.Constants;
import dal.EmployeeDAL;
import jakarta.persistence.EntityManager;
import model.EmployeeEntity;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Random;

public class EmployeeBUS implements BaseBUS<EmployeeEntity, String> {

    private EmployeeDAL employeeDAL;
    private EntityManager em;

    public EmployeeBUS(EntityManager entityManager) {
        this.employeeDAL = new EmployeeDAL(entityManager);
    }

    @Override
    public boolean insertEntity(EmployeeEntity employee) {
        return employeeDAL.insert(employee);
    }

    @Override
    public boolean updateEntity(EmployeeEntity employee) {
        return employeeDAL.update(employee);
    }

    @Override
    public boolean deleteEntity(String id) {
        return employeeDAL.deleteById(id);
    }

    @Override
    public EmployeeEntity getEntityById(String id) {
        return employeeDAL.findById(id);
    }

    @Override
    public List<EmployeeEntity> getAllEntities() {
        return employeeDAL.findAll();
    }

    public List<EmployeeEntity> getListEmployeeActive() {
        return employeeDAL.getListEmployeeActive();
    }

    public EmployeeEntity checkLogin(String username, String password) {
        return employeeDAL.findAll().stream().filter(e -> e.getEmployeeId().equals(username) && varifyPassword(password, e.getPassword()))
                .findFirst()
                .orElse(null);
    }

    public List<EmployeeEntity> getEmployeesWithKeyword(String name, String phone, String address, String email, String pass, String roleId, boolean active) {
        return employeeDAL.getEmployeesWithKeyword(name, phone, address, email, pass, roleId, active);
    }

    public EmployeeEntity findByEmail(String email) {
        return employeeDAL.findAll().stream().filter(e -> e.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, Constants.SALT);
    }

    public boolean varifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    // Hàm tạo mã xác nhận gồm 6 ký tự và số
    public String generateConfirmationCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Tập hợp ký tự và số
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        // Tạo mã gồm 6 ký tự
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length()); // Chọn ngẫu nhiên một chỉ số
            code.append(characters.charAt(index)); // Thêm ký tự vào mã
        }

        return code.toString(); // Trả về mã xác nhận
    }
}
