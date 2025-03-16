package repository.custom;

import dto.BorrowRecord;
import dto.ReturnRecord;
import entity.BorrowRecordEntity;
import repository.SuperDao;

import java.sql.SQLException;

public interface ReturnRecordDao extends SuperDao {
    ReturnRecord findById(Integer borrowId) throws SQLException;
    boolean returnBookWithTransaction(Integer borrowId, String returnDate, Double fineAmount) throws SQLException;
}

