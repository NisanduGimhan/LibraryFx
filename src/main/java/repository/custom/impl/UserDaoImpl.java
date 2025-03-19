package repository.custom.impl;

import db.DBConnection;
import dto.User;
import entity.UserEntity;
import org.hibernate.Session;
import repository.custom.UserDao;
import utill.HibernateUtill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    private static UserDaoImpl instance;

    private UserDaoImpl(){}

    public static UserDaoImpl getInstance() {
        if (instance == null) {
            instance = new UserDaoImpl();
        }
        return instance;
    }

    @Override
    public User findByEmail(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try (Connection connection= DBConnection.getInstance().getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveUser(UserEntity user) {
        Session session = HibernateUtill.getSession();
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
        session.close();
        return false;
    }
}
