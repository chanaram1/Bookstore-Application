package ProjectFinal;

/**
 *
 * @author group 45
 */
import javafx.scene.control.CheckBox;

public class Book {

    private String bookName;
    private double price;
    private CheckBox select;

    public Book(String bookName, double price) {
        this.bookName = bookName;
        this.price = price;

        select = new CheckBox();
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

}
