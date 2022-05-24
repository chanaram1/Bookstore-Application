package ProjectFinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author author group 45
 */
public class Userlist {

    private static String filename = "customers.txt";
    ObservableList<User> users = FXCollections.observableArrayList();

    public boolean verify(String user, String pw) {
        boolean verification = false;
        String username, password;
        int points;
        User c1, current;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("customers.txt"));

            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                String info[] = line.split(", ");
                username = info[0];
                password = info[1];
                points = Integer.parseInt(info[2]);
                c1 = new User(username, password, points);
                if ((user.equals(username)) && (pw.equals(password))) {
                    verification = true;
                    current = c1;
                    System.out.println(current.getUsername());
                    System.out.println(current.getPassword());
                    System.out.println(current.getPoints());
                }
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("User doesn't exist");
        }
        return verification;

    }

    public void modifyUsers(String oldString, String newString) {
        File fileToBeModified = new File("customers.txt");

        String oldContent = "";

        BufferedReader reader = null;

        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            //Reading all the lines of input text file into oldContent
            String line = reader.readLine();

            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();

                line = reader.readLine();
            }

            //Replacing oldString with newString in the oldContent
            String newContent = oldContent.replaceAll(oldString, newString);

            //Rewriting the input text file with newContent
            writer = new FileWriter(fileToBeModified);

            writer.write(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        } 
            try {
                //Closing the resources

                reader.close();

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        
    }

    public Userlist(String list) {
        filename = list;
    }

    public void writeUser(String file, User newuser) {
        users.add(newuser);
        try {//creates a file, and adds to it if it is called further
            FileWriter fw = new FileWriter(filename, true);
            fw.write(newuser.getUsername() + ", " + newuser.getPassword() + ", " + newuser.getPoints() + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    public ObservableList<User> getUser() {
        String username, password;
        int points;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("customers.txt"));

            String line = reader.readLine();
            while (line != null) {
                String info[] = line.split(", ");
                username = info[0];
                password = info[1];
                points = Integer.parseInt(info[2]);

                users.add(new User(username, password, points));
                // read next line
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            System.out.println("Invalid");
        }

        return users;
    }

    public void removeUser(String file, User user) {
        File inFile = new File(file);
        File temp = new File("temp.txt");
        String line;

        BufferedReader br = null;
        FileWriter fw = null;
        FileReader fr = null;
        users.remove(user);
        try {
            br = new BufferedReader(new FileReader(inFile));
            fw = new FileWriter(temp, true);
            while ((line = br.readLine()) != null) {
                if (line.equals(user.getUsername() + ", " + user.getPassword() + ", " + user.getPoints())) {//if line is the same as sometihng u want to delete, then it skips this and goes on to the next line withour printing it to the temp
                    System.out.println("deleting");
//leave empty it will execute nothing and move on if it finds any of these words 
                } else {
                    //print into the temp
                    fw.write(line + "\n");
                    System.out.println(user.getUsername() + ", " + user.getPassword() + ", " + user.getPoints() + "\n");
                }
            }
            fw.close();
            br.close();

            inFile.delete();
            temp.renameTo(inFile);

            // NOW delete the file and rename temp to the main file
            //the method generally works only thing that could malfunction are areas related to the book object as i did not test them
        } catch (IOException e) {
            System.out.println("error");
        }
    }

}
