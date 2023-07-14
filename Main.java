
import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    // jdbc connetion
    static Connection conn;
    static Statement stmt;
    static PreparedStatement pstmt;
    static int id = -1;
    static boolean login = false;
    // regex
    static Pattern nameReg = Pattern.compile("[A-Za-z]\\w{4,29}");
    static Pattern numberReg = Pattern.compile("(0|91)?[6-9][0-9]{9}");
    static Pattern emailReg = Pattern.compile("[a-zA-Z0-9][a-zA-z0-9_.]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
    static Pattern passReg = Pattern.compile("(.)+{8,20}");
    static {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/userdata", "root", "");
            stmt = conn.createStatement();
            // pstmt = conn.prepareStatement(null);
            System.out.println("JDBC Connection Success  .. ..");
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    static Scanner sc = new Scanner(System.in);

    static Console cnsl = System.console();

    public static void main(String[] args) {

        while (true) {

            System.out.println("| |--------------------------||");
            System.out.println("|1| : Registration           ||");
            System.out.println("|2| : Login                  ||");
            System.out.println("|3| : Update                 ||");
            System.out.println("|4| : Delete                 ||");
            System.out.println("|5| : Exit                   ||");
            System.out.println("|6| : Show Profile           ||");
            System.out.println("| |--------------------------||");
            System.out.println("Enter your choise : ");

            int ch = sc.nextInt();
            if (cnsl == null) {
                System.out.println("No console available");
                return;
            }
            switch (ch) {
                case 1:
                    register();
                    break;

                case 2:
                    login();

                    break;

                case 3:
                    Update();
                    break;

                case 4:
                    delete();

                    break;

                case 6:
                    showData();
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Enter valid input .. ");

            }
            sc.nextLine();
            if (ch == 5)
                break;
        }

    }

    // ------------------------------------------------
    public static void register() {
        if (login) {
            System.out.println("You Are already register.");
            return;
        }
        sc.nextLine();
        boolean b = false;
        String name = "";
        String email = "";
        String num = "";
        String pass = "";
        // Name validate
        do {
            System.out.print("Enter Name        : ");
            name = sc.nextLine().trim();
            Matcher nameMacher = nameReg.matcher(name);
            b = nameMacher.matches();
            if (!b)
                System.out.println("Pleace Eneter valid name  :");
        } while (!b);
        // Email validate
        do {
            System.out.print("Enter Email id    :");
            email = sc.nextLine().trim();
            Matcher emailMacher = emailReg.matcher(email);
            b = emailMacher.matches();
            if (!b)
                System.out.println("Pleace Eneter valid email :  ");
        } while (!b);

        // Number validate
        do {

            System.out.println("Enter Mobile Number :");
            num = sc.nextLine().trim();
            Matcher numMacher = numberReg.matcher(num);
            b = numMacher.matches();
            if (!b)
                System.out.println("Pleace Eneter valid Number  :   ");
        } while (!b);

        // Password validate

        do {
            System.out.print("Enter password    :");

            char[] ch1 = cnsl.readPassword();
            pass = String.valueOf(ch1).trim();
            Matcher passMacher = passReg.matcher(pass);
            b = passMacher.matches();
            if (!b)
                System.out.println("Pleace Eneter valid Password   :  ");
        } while (!b);

        System.out.print("Enter Address     :");
        String Add = sc.nextLine().trim();

        // query
        String query = "insert into Student values('" + name + "' , '" + email + "','" + num + "' ,'"
                + pass + "',0,'" + Add + "')";

        System.out.print("Successfully Register  : ");

        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    // --------------------------------------------------------
    public static void login() {
        if (login) {
            System.out.println("You Are already Login.");
            return;
        }
        boolean b = false;
        String email = "";
        String pass = "";

        // System.out.println("Enter Email id :");
        // sc.nextLine();
        // String email = sc.nextLine().trim();

        do {
            sc.nextLine();
            System.out.print("Enter Email id    :");
            email = sc.nextLine().trim();
            Matcher emailMacher = emailReg.matcher(email);
            b = emailMacher.matches();
            if (!b)
                System.out.println("Pleace Eneter valid email :  ");
        } while (!b);

        do {
            System.out.print("Enter password    :");

            char[] ch1 = cnsl.readPassword();
            pass = String.valueOf(ch1).trim();
            Matcher passMacher = passReg.matcher(pass);
            b = passMacher.matches();
            if (!b)
                System.out.println("Pleace Eneter valid Password   :  ");
        } while (!b);

        // System.out.println("Enter password..");

        // char[] ch1 = cnsl.readPassword();
        // String pass = String.valueOf(ch1).trim();

        // other code
        String query = "Select pass,id from Student where email = '" + email + "'";
        try {
            ResultSet set = stmt.executeQuery(query);
            while (set.next()) {
                if (pass.equals(set.getString("pass"))) {
                    System.out.println("Login Successfull..");
                    id = set.getInt("id");
                    login = true;
                    break;
                }
            }
            if (!login) {
                System.out.println("Email or password  is wrong..");
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------------
    // update
    public static void Update() {
        if (!login) {
            System.out.println("you must login first");
            sc.nextLine();
            return;
        }

        System.out.println("|*| What you want to Update : ||");
        System.out.println("|1|  : Name                   ||");
        System.out.println("|2|  : Email                  ||");
        System.out.println("|3|  : Mobile                 ||");
        System.out.println("|4|  : Password               ||");
        System.out.println("|5|  : Exit                   ||");
        int ch = sc.nextInt();
        String query = "";
        sc.nextLine();
        boolean b = false;
        String name = "";
        String email = "";
        String phone = "";
        String pass = "";

        switch (ch) {

            case 1:
                // name
                do {
                    System.out.print("Enter Name        : ");
                    name = sc.nextLine().trim();
                    Matcher nameMacher = nameReg.matcher(name);
                    b = nameMacher.matches();
                    if (!b)
                        System.out.println("Pleace Enter valid name  :");
                } while (!b);
                query = "update Student set name = '" + name + "' where id = " + id;

                break;

            case 2:
                // email
                do {
                    System.out.print("Enter Email id    :");
                    email = sc.nextLine().trim();
                    Matcher emailMacher = emailReg.matcher(email);
                    b = emailMacher.matches();
                    if (!b)
                        System.out.println("Pleace Enter valid email :  ");
                } while (!b);

                query = "update Student set email = '" + email + "' where id = " + id;

                break;
            case 3:
                // mob
                System.out.println("Enter New Mobile Number : ");
                do {

                    System.out.println("Enter Mobile Number :");
                    phone = sc.nextLine().trim();
                    Matcher numMacher = numberReg.matcher(phone);
                    b = numMacher.matches();
                    if (!b)
                        System.out.println("Pleace Enter valid Number  :   ");
                } while (!b);
                // String phone = sc.nextLine().trim();
                query = "update Student set phone = '" + phone + "' where id = " + id;

                break;
            case 4:
                // password

                System.out.println("Enter New Password : ");

                do {
                    System.out.print("Enter password    :");

                    char[] ch1 = cnsl.readPassword();
                    pass = String.valueOf(ch1).trim();
                    Matcher passMacher = passReg.matcher(pass);
                    b = passMacher.matches();
                    if (!b)
                        System.out.println("Pleace Enter valid Password   :  ");
                } while (!b);

                query = "update Student set pass = '" + pass + "' where id = " + id;

                break;

            case 5:
                return;

            default:
                System.out.println("Enter valid input..  ");

        }

        try {
            stmt.executeUpdate(query);
            System.out.print("Updation Successfull..");

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    // ------------------------------------------------------1
    public static void delete() {

        if (!login) {
            System.out.println("Please Login first...");
            return;
        }
        System.out.println("Are you sure you want to delete Account(Y/N)");
        String c = sc.next();

        if (c.equalsIgnoreCase("y")) {
            String query = "delete from Student where id = " + id;
            try {
                stmt.executeUpdate(query);
                System.out.println("Account Delete Successfully..");
                login = false;
            } catch (SQLException e) {

                e.printStackTrace();
            }
        } else if (c.equalsIgnoreCase("N")) {
            System.out.println("Account not Affected");

        } else {
            System.out.println("Invalid  Input ..");
        }
    }

    // show Profile
    public static void showData() {
        if (!login) {
            System.out.println("No data to show, Please login first..");
        }
        String query = "Select * from Student where id =  " + id;
        try (ResultSet set = stmt.executeQuery(query)) {
            while (set.next()) {
                System.out.println("Name          : " + set.getString("name"));
                System.out.println("Email         : " + set.getString("email"));
                System.out.println("Address       : " + set.getString("address"));
                System.out.println("Mobile Number : " + set.getString("phone"));
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

}