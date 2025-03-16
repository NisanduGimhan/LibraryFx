package service.custom;

import dto.BorrowRecord;
import dto.Member;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface BorrowRecordService extends SuperService {
    List<Integer> getAllMembers();
    List<String> getAllBooks();
    boolean placeBookBorrowRecord(BorrowRecord br) throws SQLException;
}
