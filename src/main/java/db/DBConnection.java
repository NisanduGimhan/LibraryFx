package db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.ReturningWork;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private SessionFactory sessionFactory;

    private DBConnection() {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Hibernate session factory", e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        Session session = sessionFactory.openSession();
        return session.doReturningWork(new ReturningWork<Connection>() {
            @Override
            public Connection execute(Connection connection) throws SQLException {
                return connection.unwrap(Connection.class);
            }
        });
    }
}
