package service.custom;

import dto.ReturnRecord;
import entity.BorrowRecordEntity;
import service.SuperService;

import java.sql.SQLException;

public interface ReturnRecordService extends SuperService {
    ReturnRecord findById(Integer borrowId) throws SQLException;
    boolean returnBookWithTransaction(Integer borrowId, String returnDate, Double fineAmount) throws SQLException;
}
