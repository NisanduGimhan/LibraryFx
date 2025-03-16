package service.custom.impl;

import dto.Book;
import dto.Category;
import entity.BookEntity;
import entity.CategoryEntity;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.BookDao;
import repository.custom.impl.BookDaoImpl;
import service.ServiceFactory;
import service.custom.BookService;
import utill.DaoType;
import utill.ServiceType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookServiceImpl implements BookService {

    private static BookServiceImpl instance;

    private BookServiceImpl(){}

    public static BookServiceImpl getInstance(){
        if (instance==null){
            instance=new BookServiceImpl();
        }
            return instance;

    }

    BookDao dao=DaoFactory.getInstance().getDaoType(DaoType.BOOK);

    @Override
    public boolean addBook(Book book) throws SQLException{

        BookEntity entity = new ModelMapper().map(book, BookEntity.class);
       return dao.add(entity);

    }

    @Override
    public boolean deleteBook(String id) throws SQLException {

        return  dao.Delete(String.valueOf(id));

    }


    @Override
    public boolean updateBook(Book book) {
        BookEntity entity = dao.Search(book.getIsbn());
        if (entity != null) {
            new ModelMapper().map(book, entity);
            return dao.update(entity);
        }
        return false;
    }


    @Override
    public Book searchBook(String isbn) {

        return new ModelMapper().map(dao.Search(String.valueOf(isbn)), Book.class);

    }

    @Override
    public List<Book> getAll() {

        ArrayList<Book> bkarrylst=new ArrayList<>();
        dao.getAll().forEach(bookEntity->bkarrylst.add(new ModelMapper().map(bookEntity,Book.class)));
        return bkarrylst;
    }

    @Override
    public int setValueToCard() {
        int count = dao.setValueToCard();
        return count;
    }

    @Override
    public int setDeuDateValueToCard() {
        int i = dao.setDueDateValueToCard();
        return i;
    }

    @Override
    public List<Category> getCategories() {
        ArrayList<Category> categoryArrayList=new ArrayList<>();
        dao.getAllCategories().forEach(categoryEntity ->categoryArrayList.add(new ModelMapper().map(categoryEntity,Category.class)) );
        return categoryArrayList;
    }
}
