package repository.custom.impl;

import db.DBConnection;
import entity.BorrowRecordEntity;
import entity.MemberEntity;
import repository.DaoFactory;
import repository.custom.BookDao;
import repository.custom.BorrowRecordDao;
import utill.DaoType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordDaoImpl implements BorrowRecordDao {
    private static BorrowRecordDaoImpl instance;

    private  BorrowRecordDaoImpl(){}

    public static BorrowRecordDaoImpl getInstance() {
        return instance==null? instance=new BorrowRecordDaoImpl():instance;
    }

    BookDao dao= DaoFactory.getInstance().getDaoType(DaoType.BOOK);

    @Override
    public List<Integer> getAllMembers() {
        ArrayList<Integer> allMembersID = new ArrayList<>();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MemberID FROM Members");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                allMembersID.add(resultSet.getInt("MemberID"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allMembersID;
    }

    @Override
    public List<String> getAllBooks() {
        ArrayList<String> allBookIsbn= new ArrayList<>();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT isbn FROM Books");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                allBookIsbn.add(resultSet.getString("isbn"));
            }
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
        return allBookIsbn;
    }


    public boolean updateStatus(Integer id, Connection connection) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Connection is not valid.");
        }


        System.out.println("bk id : " + id);

        String status = "UPDATE books SET availabilityStatus = 'Borrowed' WHERE BookID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(status)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();


            System.out.println("row ok : " + rowsAffected);

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating book status: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean placeBookBorrowRecord(BorrowRecordEntity record) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);


            String recod = "INSERT INTO borrowrecords (MemberID, BookID, BorrowDate, DueDate, ReturnDate) " +
                    "VALUES (?, ?, ?, ?, NULL)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(recod)) {
                preparedStatement.setInt(1, record.getMemberID());
                preparedStatement.setInt(2, record.getBookID());
                preparedStatement.setString(3, record.getBorrowDate());
                preparedStatement.setString(4, record.getDueDate());

                boolean isAddBookRecord = preparedStatement.executeUpdate() > 0;
                System.out.println("Insert borrow record result: " + isAddBookRecord);

                if (isAddBookRecord) {

                    boolean updateStatus = updateStatus(record.getBookID(), connection);

                    if (updateStatus) {
                        connection.commit();
                        System.out.println("Transaction committed.");
                        return true;
                    } else {
                        System.out.println("Failed to update book status.");
                        connection.rollback();
                        return false;
                    }
                }
            }

            connection.rollback();
            return false;

        } catch (SQLException e) {
            System.out.println("Error while placing borrow record: " + e.getMessage());
            e.printStackTrace();
            connection.rollback();
            throw new RuntimeException("Error placing borrow record: " + e.getMessage(), e);
        } finally {
            connection.setAutoCommit(true);
        }
    }




}

