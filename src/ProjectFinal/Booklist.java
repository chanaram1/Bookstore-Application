package ProjectFinal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author group 45
 */
public class Booklist {

    private static String filename = "books.txt";
    ObservableList<Book> books = FXCollections.observableArrayList();

    public Booklist(String list) {
        filename = list;
    }

    public ObservableList<Book> getBooks() {
        try {
            BufferedReader reader = new BufferedReader(new java.io.FileReader("books.txt"));

            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                String info[] = line.split(", ");
                books.add(new Book(info[0], Double.parseDouble(info[1])));

                // read next line 
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            System.out.println("Error");
        }
        return books;
    }

    public void writeBooks(String file, Book newbook) {
        books.add(newbook);
        try {//creates a file, and adds to it if it is called further
            FileWriter fw = new FileWriter(filename, true);
            fw.write(newbook.getBookName() + ", " + String.valueOf(newbook.getPrice()) + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    public void removeBooks(String file, Book book) {
        File inFile = new File(file);
        File temp = new File("temp.txt");
        String line;

        BufferedReader br = null;
        FileWriter fw = null;
        FileReader fr = null;
        books.remove(book);
        try {
            br = new BufferedReader(new FileReader(inFile));
            fw = new FileWriter(temp, true);
            while ((line = br.readLine()) != null) {
                if (line.equals(book.getBookName() + ", " + String.valueOf(book.getPrice()))) {//if line is the same as sometihng u want to delete, then it skips this and goes on to the next line withour printing it to the temp
                    //leave empty it will execute nothing and move on if it finds any of these words
                    System.out.println("deleting");
                } else {
                    //print into the temp
                    fw.write(line + "\n");
                    System.out.println("printing");
                    System.out.println(book.getBookName() + ", " + String.valueOf(book.getPrice()));
                }
            }
            fw.close();
            br.close();

            inFile.delete();
            temp.renameTo(inFile);
            System.out.println(inFile.getName());

            // NOW delete the file and rename temp to the main file
            //the method generally works only thing that could malfunction are areas related to the book object as i did not test them
        } catch (IOException e) {
            System.out.println("error");
        }

    }
    }
