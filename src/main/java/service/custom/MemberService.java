package service.custom;

import dto.Book;
import dto.Member;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface MemberService extends SuperService {

    boolean addMember( Member member)throws SQLException;
    boolean deleteMember(Integer id)throws SQLException;
    boolean updateMember(Member member);
    Member searchMember(Integer id);
    List<Member> getAll();
    int setValueToCard();
}
