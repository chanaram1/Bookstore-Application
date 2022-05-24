package ProjectFinal;

import java.io.BufferedReader;
import javafx.scene.control.CheckBox;
import java.io.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

/**
 *
 * @author group 45
 */
public class User {

    private String username;
    private String password;
    private int points;

    final int REWARD_POINTS_CONVERSION = 10;
    final int PAY_POINTS_CONVERSION = 100;

    public User(String username, String password, int points) {
        this.username = username;
        this.password = password;
        this.points = points;

    }

    public ObservableList<Book> selectedBooks(ObservableList<Book> books) {
        ObservableList<Book> selectBooks = FXCollections.observableArrayList();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getSelect().isSelected()) {
                selectBooks.add(books.get(i));
            }
        }
        return selectBooks;
    }

    public double payCash(ObservableList<Book> selectBooks) {
        double transactionCost = 0;

        System.out.println(selectBooks.size());
        for (int i = 0; i < selectBooks.size(); i++) {
            System.out.println(selectBooks.get(i).getPrice());
            transactionCost = transactionCost + selectBooks.get(i).getPrice();
        }
        points = points + (int) transactionCost * REWARD_POINTS_CONVERSION;
        return transactionCost;
    }

    public double payPoints(ObservableList<Book> selectBooks) {
        double transactionCost = 0;

        System.out.println(selectBooks.size());
        for (int i = 0; i < selectBooks.size(); i++) {
            System.out.println(selectBooks.get(i).getPrice());
            transactionCost = transactionCost + selectBooks.get(i).getPrice();
        }

        if (points >= (int) transactionCost * PAY_POINTS_CONVERSION) {
            points = points - (int) transactionCost * PAY_POINTS_CONVERSION;
            transactionCost = 0;

        } else {
            transactionCost = transactionCost - points / 100;
            points = points % 100;
            points = points + (int) transactionCost * REWARD_POINTS_CONVERSION;
        }

        return transactionCost;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getStatus() {
        if (points < 1000) {
            return "Silver";
        }
        return "Gold";
    }

}
