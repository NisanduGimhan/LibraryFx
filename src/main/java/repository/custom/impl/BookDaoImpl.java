package repository.custom.impl;



import db.DBConnection;
import entity.BookEntity;
import entity.CategoryEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.custom.BookDao;
import utill.HibernateUtill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {

    private static BookDaoImpl instance;

    private BookDaoImpl(){}

    public static BookDaoImpl getInstance() {
        if (instance==null){
            instance=new BookDaoImpl();
        }
        return instance;
    }

    @Override
    public boolean add(BookEntity book) {
        Session session = HibernateUtill.getSession();
        session.beginTransaction();
        session.persist(book);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean Delete(String id) {
        Transaction transaction = null;
        try (Session session = HibernateUtill.getSession()) {
            transaction = session.beginTransaction();


            BookEntity book = session.get(BookEntity.class, id);
            if (book != null) {
                session.remove(book);
                transaction.commit();
                return true;
            }

            return false;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to delete book with ID: " + id, e);
        }
    }


    @Override
    public boolean update(BookEntity book) {
        Session session = HibernateUtill.getSession();
        session.beginTransaction();

        BookEntity existingBook = session.get(BookEntity.class, book.getIsbn());
        if (existingBook != null) {
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setCategoryId(book.getCategoryId());
            existingBook.setAvailabilityStatus(book.getAvailabilityStatus());

            session.update(existingBook);
            session.getTransaction().commit();
            session.close();
            return true;
        }

        session.close();
        return false;
    }


    @Override
    public BookEntity Search(String id) {
        Session session = HibernateUtill.getSession();
        session.beginTransaction();
        BookEntity book = session.get(BookEntity.class, id);
        session.getTransaction().commit();
        session.close();
        return book;
    }

    @Override
    public List<BookEntity> getAll() {
        Session session = HibernateUtill.getSession();
        session.beginTransaction();
        List<BookEntity> books = session.createQuery("FROM BookEntity", BookEntity.class).list();
        session.getTransaction().commit();
        session.close();
        return books;
    }

    @Override
    public int setValueToCard() {
        int count = 0;

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM members");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error executing SQL query", e);
        }

        return count;
    }


    @Override
    public List<CategoryEntity> getAllCategories() {
        List<CategoryEntity> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("CategoryID");
                String name = resultSet.getString("CategoryName");
                categories.add(new CategoryEntity(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public boolean updateStatus(Integer id) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Connection is not valid.");
            }


            System.out.println("Updating book status for BookID: " + id);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE books SET availabilityStatus = 'Borrowed' WHERE BookID  = ?"
            );
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();


            System.out.println("Rows affected: " + rowsAffected);

            if (rowsAffected > 0) {
                return true;
            } else {
                System.out.println("No rows were updated.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error updating book status: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int setDueDateValueToCard() {
        int count = 0;

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS DueBookCount FROM borrowrecords WHERE DueDate < CURDATE() AND ReturnDate IS NULL");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error executing SQL query", e);
        }

        return count;
    }

}
