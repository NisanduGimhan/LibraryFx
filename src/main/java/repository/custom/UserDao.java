package repository.custom;

import dto.User;
import entity.UserEntity;
import repository.SuperDao;

public interface UserDao extends SuperDao {
    User findByEmail(String email);

    boolean saveUser(UserEntity user);
}
