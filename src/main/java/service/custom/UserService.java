package service.custom;

import dto.User;
import service.SuperService;

public interface UserService extends SuperService {
    User validateUser(String email, String password);

    User saveUser(User user);
}
