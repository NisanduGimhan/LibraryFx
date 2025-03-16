package repository.custom.impl;


import db.DBConnection;
import entity.MemberEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import repository.custom.MemberDao;
import utill.HibernateUtill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;



public class MemberDaoImpl implements MemberDao {

    private static MemberDaoImpl instance;

    private MemberDaoImpl(){}

    public static MemberDaoImpl getInstance() {
        if (instance==null){
            instance=new MemberDaoImpl();
        }
        return instance;
    }

    @Override
    public boolean add(MemberEntity member) {
        Session session = HibernateUtill.getSession();
        session.beginTransaction();
        session.persist(member);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean Delete(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateUtill.getSession()) {
            transaction = session.beginTransaction();


            MemberEntity member = session.get(MemberEntity.class, id);
            if (member != null) {
                session.remove(member);
                transaction.commit();
                return true;
            }

            return false;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to delete Member with ID: " + id, e);
        }
    }

    @Override
    public boolean update(MemberEntity member) {
        Session session = HibernateUtill.getSession();
        session.beginTransaction();

        MemberEntity existingMember = session.get(MemberEntity.class, member.getMemberID());


        if (existingMember != null) {
            existingMember.setName(member.getName());
            existingMember.setContactInfo(member.getContactInfo());
            existingMember.setMembershipDate(member.getMembershipDate());


            session.update(existingMember);
            session.getTransaction().commit();
            session.close();
            return true;
        }
        session.close();
        return false;
    }

    @Override
    public MemberEntity Search(Integer id) {
        Session session = HibernateUtill.getSession();
        session.beginTransaction();
        MemberEntity member = session.get(MemberEntity.class, id);
        session.getTransaction().commit();
        session.close();
        return member;
    }

    @Override
    public List<MemberEntity> getAll() {
        Session session = HibernateUtill.getSession();
        session.beginTransaction();
        List<MemberEntity> mbr = session.createQuery("FROM MemberEntity", MemberEntity.class).list();
        session.getTransaction().commit();
        session.close();
        return mbr;

    }

    @Override
    public int setValueToCard() {
        Connection connection = DBConnection.getInstance().getConnection();
        int count=0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM books");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return count;
    }

}
