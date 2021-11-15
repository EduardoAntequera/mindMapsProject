package daos;
import models.User;

import java.util.Set;

public interface UserDao {

    User getUser(Integer id);
    Set<User> getAllUsers();
    User getUserByUserName(String user);
    boolean insertUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(Integer id);
}