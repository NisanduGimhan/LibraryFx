package service.custom.impl;

import dto.Book;
import dto.Member;
import entity.BookEntity;
import entity.MemberEntity;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.BookDao;
import repository.custom.MemberDao;
import service.custom.MemberService;
import utill.DaoType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemberServiceImpl implements MemberService {

    private static MemberServiceImpl instance;

    private MemberServiceImpl(){}

    public static MemberServiceImpl getInstance() {
        if (instance==null){
            instance=new MemberServiceImpl();
        }
        return instance;
    }

    MemberDao dao= DaoFactory.getInstance().getDaoType(DaoType.MEMBER);

    @Override
    public boolean addMember(Member member) throws SQLException {
        MemberEntity entity = new ModelMapper().map(member, MemberEntity.class);
        return dao.add(entity);

    }

    @Override
    public boolean deleteMember(Integer id) throws SQLException {
        return  dao.Delete(Integer.valueOf(String.valueOf(id)));
    }

    @Override
    public boolean updateMember(Member member) {
        MemberEntity entity = dao.Search(member.getMemberID());
        if (entity != null) {
            new ModelMapper().map(member, entity);
            return dao.update(entity);
        }
        return false;
    }

    @Override
    public Member searchMember(Integer id) {
        return new ModelMapper().map(dao.Search(id), Member.class);
    }

    @Override
    public List<Member> getAll() {
        ArrayList<Member> bkarrylst=new ArrayList<>();
        dao.getAll().forEach(bookEntity->bkarrylst.add(new ModelMapper().map(bookEntity,Member.class)));
        return bkarrylst;
    }

    @Override
    public int setValueToCard() {
        int count = dao.setValueToCard();
        return count;
    }

}
