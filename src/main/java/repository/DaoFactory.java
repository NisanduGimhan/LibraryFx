package repository;

import repository.custom.impl.*;
import service.SuperService;
import service.custom.impl.BookServiceImpl;
import service.custom.impl.UserServiceImpl;
import utill.DaoType;
import utill.ServiceType;

public class DaoFactory {

    private static DaoFactory instance;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }

    public static <T extends SuperDao> T getDaoType(DaoType type) {
        switch (type) {
            case BOOK:
                return (T) BookDaoImpl.getInstance();

            case MEMBER:
                return (T) MemberDaoImpl.getInstance();

            case BORROWRECORD:
                return (T) BorrowRecordDaoImpl.getInstance();

            case RETURNRECORD:
                return (T) ReturnRecordDaoImpl.getInstance();

            case USER:  return (T) UserDaoImpl.getInstance();
        }

        return null;
    }
}