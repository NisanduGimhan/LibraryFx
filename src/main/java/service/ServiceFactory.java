package service;

import service.custom.impl.*;
import utill.ServiceType;

import static utill.ServiceType.BORROWRECORD;

public class ServiceFactory {

    private static ServiceFactory instance;

    private ServiceFactory(){}

    public static ServiceFactory getInstance() {
        return instance==null?instance=new ServiceFactory():instance;
    }

    public <T extends SuperService> T getServiceType(ServiceType type){

        switch (type){
            case BOOK:return (T) BookServiceImpl.getInstance();

            case MEMBER:return (T) MemberServiceImpl.getInstance();

            case BORROWRECORD:return (T) BorrowRecordServiceImpl.getInstance();

            case RETURNRECORD:return (T) ReturnRecordServiceImpl.getInstance();

           case USER: return (T) UserServiceImpl.getInstance();
        }
        return null;
    }
}
