package service.custom;

import dto.Book;
import dto.Category;
import entity.CategoryEntity;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface BookService extends SuperService {

    boolean addBook( Book book)throws SQLException;
    boolean deleteBook(String id)throws SQLException;
    boolean updateBook(Book book);
    Book searchBook(String id);
    List<Book> getAll();
    int setValueToCard();
    int setDeuDateValueToCard();
    List<Category> getCategories();
}
