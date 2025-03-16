package repository.custom.impl;
import java.sql.Date;
import db.DBConnection;
import dto.BorrowRecord;
import dto.ReturnRecord;
import entity.BorrowRecordEntity;
import repository.custom.ReturnRecordDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ReturnRecordDaoImpl implements ReturnRecordDao {

    private static ReturnRecordDaoImpl instance;

    private ReturnRecordDaoImpl(){}

    public static ReturnRecordDaoImpl getInstance() {
        if (instance==null){
            instance=new ReturnRecordDaoImpl();
        }
        return instance;
    }

    @Override
    public ReturnRecord findById(Integer memberID) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        String sql = "SELECT " +
                "b.BorrowID, " +
                "b.BookID, " +
                "COALESCE(b.DueDate, DATE_ADD(b.BorrowDate, INTERVAL 14 DAY)) AS DueDate, " +
                "COALESCE(f.FineAmount, 0) AS FineAmount, " +
                "m.name " +
                "FROM borrowrecords b " +
                "JOIN members m ON b.MemberID = m.MemberID " +
                "LEFT JOIN fines f ON b.BorrowID = f.BorrowID " +
                "WHERE b.MemberID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, memberID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReturnRecord record = new ReturnRecord();
                    record.setBorrowID(rs.getInt("BorrowID"));
                    record.setBookID(rs.getInt("BookID"));
                    record.setDueDate(rs.getDate("DueDate") != null ? rs.getDate("DueDate").toLocalDate().toString() : null);
                    record.setMemberName(rs.getString("name"));

                    double fineAmount = rs.getObject("FineAmount") != null ? rs.getDouble("FineAmount") : 0.0;
                    record.setFineAmount(fineAmount);

                    return record;
                }
            }
        }
        return null; // If no record is found
    }





    @Override
    public boolean returnBookWithTransaction(Integer borrowId, String returnDate, Double fineAmount) throws SQLException {
        Connection conn = null;
        PreparedStatement psUpdateBorrow = null;
        PreparedStatement psSelectFine = null;
        PreparedStatement psSelectBook = null;
        PreparedStatement psUpdateBook = null;
        PreparedStatement psUpdateFine = null;
        ResultSet rsFine = null;
        ResultSet rsBook = null;

        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // 1) Update the borrow record with ReturnDate
            String updateBorrowSql = "UPDATE borrowrecords SET ReturnDate = ? WHERE BorrowID = ?";
            psUpdateBorrow = conn.prepareStatement(updateBorrowSql);

            if (returnDate != null && !returnDate.trim().isEmpty()) {
                psUpdateBorrow.setDate(1, java.sql.Date.valueOf(returnDate));
            } else {
                psUpdateBorrow.setNull(1, java.sql.Types.DATE);
            }

            psUpdateBorrow.setInt(2, borrowId);
            psUpdateBorrow.executeUpdate();

            // 2) Retrieve fine amount (if any)
            String selectFineSql = "SELECT FineAmount FROM fines WHERE BorrowID = ?";
            psSelectFine = conn.prepareStatement(selectFineSql);
            psSelectFine.setInt(1, borrowId);
            rsFine = psSelectFine.executeQuery();

            double calculatedFine = 0.0;
            boolean hasFine = false;
            if (rsFine.next() && rsFine.getObject("FineAmount") != null) {
                calculatedFine = rsFine.getDouble("FineAmount");
                hasFine = calculatedFine > 0; // ✅ Check if a fine exists
            }

            // 3) Get the BookID
            String selectBookSql = "SELECT BookID FROM borrowrecords WHERE BorrowID = ?";
            psSelectBook = conn.prepareStatement(selectBookSql);
            psSelectBook.setInt(1, borrowId);
            rsBook = psSelectBook.executeQuery();

            int bookId = -1;
            if (rsBook.next()) {
                bookId = rsBook.getInt("BookID");
            }

            if (bookId == -1) {
                throw new SQLException("Error: BookID not found for BorrowID: " + borrowId);
            }

            // 4) Update book’s availability
            String updateBookSql = "UPDATE books SET availabilityStatus = 'Available' WHERE BookID = ?";
            psUpdateBook = conn.prepareStatement(updateBookSql);
            psUpdateBook.setInt(1, bookId);
            psUpdateBook.executeUpdate();

            // 5) Update FineStatus to 'Paid' if a fine exists ✅
            if (hasFine) {
                String updateFineSql = "UPDATE fines SET FineStatus = 'Paid', PaymentDate = NOW() WHERE BorrowID = ?";
                psUpdateFine = conn.prepareStatement(updateFineSql);
                psUpdateFine.setInt(1, borrowId);
                psUpdateFine.executeUpdate();
            }

            // 6) Commit transaction
            conn.commit();
            System.out.println("Book returned successfully. Fine: RS " + calculatedFine);

            return true;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Error in returnBookWithTransaction: " + e.getMessage(), e);
        } finally {
            if (rsFine != null) rsFine.close();
            if (rsBook != null) rsBook.close();
            if (psUpdateBorrow != null) psUpdateBorrow.close();
            if (psSelectFine != null) psSelectFine.close();
            if (psSelectBook != null) psSelectBook.close();
            if (psUpdateBook != null) psUpdateBook.close();
            if (psUpdateFine != null) psUpdateFine.close(); // ✅ Close fine update statement
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }



}
