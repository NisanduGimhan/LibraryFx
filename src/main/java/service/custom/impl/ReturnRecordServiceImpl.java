package service.custom.impl;
import dto.ReturnRecord;
import repository.DaoFactory;
import repository.custom.ReturnRecordDao;
import service.custom.ReturnRecordService;
import utill.DaoType;

import java.sql.SQLException;

public class ReturnRecordServiceImpl implements ReturnRecordService {

    private static ReturnRecordServiceImpl instance;

    private ReturnRecordServiceImpl(){}

    public static ReturnRecordServiceImpl getInstance() {

        if (instance==null){
            instance=new ReturnRecordServiceImpl();
        }
        return instance;
    }
    ReturnRecordDao service=DaoFactory.getInstance().getDaoType(DaoType.RETURNRECORD);

    @Override
    public ReturnRecord findById(Integer borrowId) throws SQLException {

        return  service.findById(borrowId);
    }

    @Override
    public boolean returnBookWithTransaction(Integer borrowId, String returnDate, Double fineAmount) throws SQLException {
        return service.returnBookWithTransaction(borrowId,returnDate,fineAmount);

    }

}
