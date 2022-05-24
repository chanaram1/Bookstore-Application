package ProjectFinal;

import javafx.application.Application;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author group 45
 */
public class UI extends Application {

    User currentUser;
    TextField addTitle, addPrice, addUsername, addPassword, addPoints;
    TableView<Book> bookTable;
    TableView<User> custTable;
    ObservableList<Book> books = FXCollections.observableArrayList();
    ObservableList<User> users = FXCollections.observableArrayList();
    ObservableList<User> selectUsers = FXCollections.observableArrayList();
    ObservableList<Book> selectBooks = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Bookstore App");

        Label welLabel = new Label("Welcome to the Bookstore App!");

        Label userLabel = new Label("Username:");
        TextField userText = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passText = new PasswordField();

        Button loginButton = new Button("Login");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(30, 10, 30, 30));

        grid.add(welLabel, 0, 0, 1, 1);
        grid.add(userLabel, 0, 1, 1, 1);
        grid.add(passLabel, 0, 2, 1, 1);
        grid.add(userText, 1, 1, 1, 1);
        grid.add(passText, 1, 2, 1, 1);
        grid.add(loginButton, 1, 3, 2, 1);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            Userlist customer = new Userlist("customers.txt");

            @Override
            public void handle(ActionEvent event) {
                String username = userText.getText();
                String password = passText.getText();

                if (username.equals("admin") && password.equals("admin")) {
                    ownerWindow(primaryStage);
                    System.out.println("Admin successfully logged in.");
                } else if (customer.verify(username, password)) {
                    userWindow(primaryStage, username, password);
                    System.out.println("Customer successfully logged in.");
                } else {
                    System.out.println("The username: " + username + " and password: " + password + " is invalid");
                }
            }
        });

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void ownerWindow(Stage primaryStage) {

        Button books = new Button("Books");
        books.setMinSize(100, 30);
        Button customers = new Button("Customers");
        customers.setMinSize(100, 30);
        Button logout = new Button("Logout");
        logout.setMinSize(100, 30);

        GridPane managerPane = new GridPane();

        managerPane.setAlignment(Pos.CENTER);
        managerPane.setHgap(10);
        managerPane.setVgap(10);
        managerPane.setPadding(new Insets(25, 25, 25, 25));

        managerPane.add(books, 0, 1);
        managerPane.add(customers, 0, 2);
        managerPane.add(logout, 0, 3);

        books.setOnAction((ActionEvent e) -> {
            manageBooks(primaryStage);
        });

        customers.setOnAction((ActionEvent e) -> {
            manageCustomers(primaryStage);
        });

        logout.setOnAction((ActionEvent e) -> {
            start(primaryStage);
        });

        Scene scene = new Scene(managerPane, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void userWindow(Stage primaryStage, String username, String password) {
        Booklist book = new Booklist("books.txt");
        Userlist customer = new Userlist("customers.txt");
        users = customer.getUser();
        System.out.println(users.get(0).getPoints());

        for (int i = 0; i < users.size(); i++) {

            if (users.get(i).getUsername().equals(username)) {
                currentUser = users.get(i);
            }
        }

        Text welcome = new Text("Welcome " + username + ". You have " + currentUser.getPoints() + " point(s)." + " Your status is " + currentUser.getStatus() + ".");

        TableView<Book> bookTable;

        //Title Column
        TableColumn<Book, String> nameColumn = new TableColumn<>("Title");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        //Price Column
        TableColumn<Book, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Select Column
        TableColumn<Book, Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setMinWidth(100);
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));

        Button buy = new Button("Buy");
        buy.setMinSize(100, 30);
        Button redNBuy = new Button("Redeem & Buy");
        redNBuy.setMinSize(100, 30);
        Button logout = new Button("Logout");
        logout.setMinSize(100, 30);

        GridPane customerPane = new GridPane();

        customerPane.setAlignment(Pos.BOTTOM_CENTER);
        customerPane.setHgap(10);
        customerPane.setVgap(10);
        customerPane.setPadding(new Insets(25, 25, 25, 25));

        bookTable = new TableView<>();
        bookTable.setItems(book.getBooks());
        bookTable.getColumns().addAll(nameColumn, priceColumn, selectColumn);

        buy.setOnAction((ActionEvent e) -> {

            books = bookTable.getItems();
            selectBooks = currentUser.selectedBooks(books);
            int currentPoints = currentUser.getPoints();

            customerCostScreen(primaryStage, currentUser.payCash(selectBooks), currentUser.getPoints(), currentUser.getStatus());
            customer.modifyUsers(currentUser.getUsername() + ", " + currentUser.getPassword() + ", " + currentPoints, currentUser.getUsername() + ", " + currentUser.getPassword() + ", " + currentUser.getPoints());
            int i;
            for (i = 0; i < selectBooks.size(); i++) {
                book.removeBooks("books.txt", selectBooks.get(i));
                System.out.println(selectBooks.get(i).getBookName());
            }
        });

        redNBuy.setOnAction((ActionEvent e) -> {
            books = bookTable.getItems();

            selectBooks = currentUser.selectedBooks(books);
            int currentPoints = currentUser.getPoints();
            customerCostScreen(primaryStage, currentUser.payPoints(selectBooks), currentUser.getPoints(), currentUser.getStatus());
            customer.modifyUsers(currentUser.getUsername() + ", " + currentUser.getPassword() + ", " + currentPoints, currentUser.getUsername() + ", " + currentUser.getPassword() + ", " + currentUser.getPoints());

            int i;
            for (i = 0; i < selectBooks.size(); i++) {
                book.removeBooks("books.txt", selectBooks.get(i));
                System.out.println(selectBooks.get(i).getBookName());
            }
        });

        logout.setOnAction((ActionEvent e) -> {
            start(primaryStage);
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(welcome, bookTable, buy, redNBuy, logout);
        vBox.setPadding(new Insets(35, 35, 35, 35));
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void customerCostScreen(Stage primaryStage, double TC, int points, String status) {

        Text transactionCost = new Text("Transaction cost: $ " + String.format("%.2f", TC));
        transactionCost.setFont(Font.font("Calibiri", FontWeight.NORMAL, 15));
        Text pointStatus = new Text("Points: " + points + ", Status: " + status);
        pointStatus.setFont(Font.font("Calibiri", FontWeight.NORMAL, 15));

        Button logout = new Button("Logout");

        GridPane customerPane = new GridPane();

        customerPane.setAlignment(Pos.BOTTOM_CENTER);
        customerPane.setHgap(10);
        customerPane.setVgap(10);
        customerPane.setPadding(new Insets(25, 25, 25, 25));

        logout.setOnAction((ActionEvent e) -> {
            start(primaryStage);
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(transactionCost, pointStatus, logout);
        vBox.setPadding(new Insets(35, 35, 35, 35));
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(vBox, 300, 200);
        primaryStage.setScene(scene1);
        primaryStage.show();

    }

    public void manageBooks(Stage primaryStage) {
        Booklist book = new Booklist("books.txt");
        //Title input
        addTitle = new TextField();
        addTitle.setPromptText("Title");
        addTitle.setMinWidth(100);

        //Price Input
        addPrice = new TextField();
        addPrice.setPromptText("Price");
        addPrice.setMinWidth(80);

        //Title Column
        TableColumn<Book, String> nameColumn = new TableColumn<>("Title");
        nameColumn.setMinWidth(400);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        //Price Column
        TableColumn<Book, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        bookTable = new TableView<>();
        bookTable.setItems(book.getBooks());
        bookTable.getColumns().addAll(nameColumn, priceColumn);

        Button delete = new Button("Delete");
        Button back = new Button("Back");
        Button add = new Button("Add");

        back.setOnAction((ActionEvent e) -> {
            ownerWindow(primaryStage);
        });

        add.setOnAction((ActionEvent e) -> {
            book.writeBooks("books.txt", new Book(addTitle.getText(), Double.parseDouble(addPrice.getText())));
            addTitle.clear();
            addPrice.clear();
        });

        delete.setOnAction((ActionEvent e) -> {
            books = bookTable.getItems();
            selectBooks = bookTable.getSelectionModel().getSelectedItems();
            //selectBooks = bookTable.getSel currentUser.selectedBooks(books);
            book.removeBooks("books.txt", selectBooks.get(0));
            System.out.println(selectBooks.get(0).getBookName());
        });

        HBox hBox = new HBox();

        hBox.setSpacing(10);
        hBox.getChildren().addAll(addTitle, addPrice, add);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(bookTable, hBox, delete, back);
        vBox.setPadding(new Insets(35, 35, 35, 35));
        vBox.setSpacing(10);

        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void manageCustomers(Stage primaryStage) {
        Userlist customer = new Userlist("customers.txt");

        //Username input
        TextField addUsername = new TextField();
        addUsername.setPromptText("Username");
        addUsername.setMinWidth(100);

        //Password Input
        TextField addPassword = new TextField();
        addPassword.setPromptText("Password");
        addPassword.setMinWidth(80);

        //Points Input
        TextField addPoints = new TextField();
        addPoints.setPromptText("Points");
        addPoints.setMinWidth(40);

        //Username Column
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setMinWidth(200);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        //Password Column
        TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setMinWidth(200);
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        //Points Column
        TableColumn<User, String> pointsColumn = new TableColumn<>("Points");
        pointsColumn.setMinWidth(100);
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        custTable = new TableView<>();
        custTable.setItems(customer.getUser());
        custTable.getColumns().addAll(usernameColumn, passwordColumn, pointsColumn);

        Button delete = new Button("Delete");
        Button back = new Button("Back");
        Button add = new Button("Add");

        back.setOnAction((ActionEvent e) -> {
            ownerWindow(primaryStage);
        });

        add.setOnAction((ActionEvent e) -> {
            customer.writeUser("customers.txt", new User(addUsername.getText(), addPassword.getText(), Integer.parseInt(addPoints.getText())));
            addUsername.clear();
            addPassword.clear();
            addPoints.clear();
        });

        delete.setOnAction((ActionEvent e) -> {
            users = custTable.getItems();
            selectUsers = custTable.getSelectionModel().getSelectedItems();
            customer.removeUser("customers.txt", selectUsers.get(0));
        });

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.getChildren().addAll(addUsername, addPassword, addPoints, add);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(custTable, hBox, delete, back);
        vBox.setPadding(new Insets(35, 35, 35, 35));
        vBox.setSpacing(10);

        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
