package service.custom.impl;

import dto.User;
import entity.UserEntity;
import org.jasypt.util.text.BasicTextEncryptor;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.UserDao;
import service.custom.UserService;
import utill.DaoType;

public class UserServiceImpl implements UserService {

    private static UserServiceImpl instance;
    private final String key = "1234";
    private UserServiceImpl(){}

    public static UserServiceImpl getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }


    UserDao dao= DaoFactory.getInstance().getDaoType(DaoType.USER);

    @Override
    public User validateUser(String email, String password) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(key);

        User user = dao.findByEmail(email);
        if (user != null) {
            String decryptedPassword = textEncryptor.decrypt(user.getPassword());
            if (decryptedPassword.equals(password)) {
                return user;
            }
        }
        return null;
    }



    @Override
    public User saveUser(User user) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(key);

        String encryptedPassword = textEncryptor.encrypt(user.getPassword());
        User newuser = new User(0,user.getName(), user.getEmail(), encryptedPassword);



        UserEntity map = new ModelMapper().map(newuser, UserEntity.class);
        boolean isSaved = dao.saveUser(map);
        return isSaved ? user : null;
    }
}
