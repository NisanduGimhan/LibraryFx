package service.custom.impl;

import dto.Book;
import dto.BorrowRecord;
import dto.Member;
import entity.BookEntity;
import entity.BorrowRecordEntity;
import entity.MemberEntity;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.BookDao;
import repository.custom.BorrowRecordDao;
import service.custom.BorrowRecordService;
import utill.DaoType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordServiceImpl implements BorrowRecordService {

    private static BorrowRecordServiceImpl instance;

    private BorrowRecordServiceImpl(){}

    public static BorrowRecordServiceImpl getInstance() {
        return instance==null?instance=new BorrowRecordServiceImpl():instance;

    }

      BorrowRecordDao dao= DaoFactory.getInstance().getDaoType(DaoType.BORROWRECORD);

    @Override
    public List<Integer> getAllMembers() {

        return dao.getAllMembers();
    }

    @Override
    public List<String> getAllBooks() {
        return dao.getAllBooks();
    }


    @Override
    public boolean placeBookBorrowRecord(BorrowRecord br) throws SQLException {

        BorrowRecordEntity map = new ModelMapper().map(br, BorrowRecordEntity.class);
        return dao.placeBookBorrowRecord(map);
    }




}
