package controller;

import com.jfoenix.controls.JFXTextField;
import dto.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ServiceFactory;
import service.custom.BookService;
import utill.ServiceType;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class BookFormController implements Initializable {

    @FXML
    private TableColumn colAuthor;

    @FXML
    private TableColumn colCategoryID;

    @FXML
    private TableColumn colISBN;

    @FXML
    private TableColumn colStatus;

    @FXML
    private TableColumn colTitle;

    @FXML
    private TableView tblBooks;

    @FXML
    private JFXTextField txtAuthor;

    @FXML
    private JFXTextField txtCatogeryID;

    @FXML
    private JFXTextField txtISBN;

    @FXML
    private JFXTextField txtStatus;

    @FXML
    private JFXTextField txtTitle;
      BookService service= ServiceFactory.getInstance().getServiceType(ServiceType.BOOK);

    @FXML
    void btnAddOnAction(ActionEvent event)throws SQLException {

       boolean isAdd= service.addBook(new Book(
                null,
                txtISBN.getText(),
                txtTitle.getText(),
                txtAuthor.getText(),
                Integer.parseInt(txtCatogeryID.getText()),
                txtStatus.getText()
        ));
        if (isAdd){
            new Alert(Alert.AlertType.INFORMATION,"Member Added Successfully✅").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Member Added Unsuccessful⚠\uFE0F").show();
        }
        loadTable();
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event)throws SQLException {

       boolean idDeleted= service.deleteBook(txtISBN.getText());

        if (idDeleted){
            new Alert(Alert.AlertType.INFORMATION,"Book Deleted!✅").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Book Not Found⚠\uFE0F").show();
        }
        loadTable();
        clearFields();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

        Book book = service.searchBook(txtISBN.getText());

        if(book==null){
            new Alert(Alert.AlertType.ERROR, "Book not found⚠\uFE0F").show();
        }else {
            txtStatus.setText(book.getAvailabilityStatus());
            txtAuthor.setText(book.getAuthor());
            txtTitle.setText(book.getTitle());
            txtCatogeryID.setText(String.valueOf(book.getCategoryId()));
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Book existingBook = service.searchBook(txtISBN.getText());

        if (existingBook != null) {
            existingBook.setTitle(txtTitle.getText());
            existingBook.setAuthor(txtAuthor.getText());
            existingBook.setCategoryId(Integer.parseInt(txtCatogeryID.getText()));
            existingBook.setAvailabilityStatus(txtStatus.getText());

            boolean isUpdate = service.updateBook(existingBook);
            if (isUpdate) {
                new Alert(Alert.AlertType.INFORMATION, "Updated✅").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Not updated⚠\uFE0F").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Book not found").show();
        }

        loadTable();
        clearFields();
    }


    private void loadTable(){
        List<Book> bklst = service.getAll();
        ObservableList<Book> oblist = FXCollections.observableArrayList();
        oblist.addAll(bklst);
        tblBooks.setItems(oblist);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategoryID.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));

       loadTable();

        tblBooks.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldvalue, newValue) -> {
            if (newValue!=null){
                SetTextForFieldsWhenItSelected((Book) newValue);
            }
        }));
    }

    private void SetTextForFieldsWhenItSelected(Book bk){
        txtISBN.setText(bk.getIsbn());
        txtTitle.setText(bk.getTitle());
        txtAuthor.setText(bk.getAuthor());
        txtCatogeryID.setText(String.valueOf(bk.getCategoryId()));
        txtStatus.setText(bk.getAvailabilityStatus());
    }

    private void clearFields(){
        txtCatogeryID.clear();
        txtStatus.clear();
        txtISBN.clear();
        txtAuthor.clear();
        txtTitle.clear();
    }


}
