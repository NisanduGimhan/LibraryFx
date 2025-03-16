package repository.custom;

import entity.BorrowRecordEntity;
import entity.MemberEntity;
import repository.SuperDao;

import java.sql.SQLException;
import java.util.List;

public interface BorrowRecordDao extends SuperDao {
    List<Integer> getAllMembers();
    List<String> getAllBooks();

    boolean placeBookBorrowRecord(BorrowRecordEntity record)throws SQLException;
}
