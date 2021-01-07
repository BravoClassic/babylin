/*
  This file contains all SQL syntax and the hash function
 */




package babylinapp;

import javax.mail.MessagingException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;

public class jdbcController {
    protected static final String url = "jdbc:sqlite:babylinapp_db.db";// Allows you to connect to the database through the localhost

    //SQL Syntax To insert or access data in SQL

    protected static String userType="";
    protected static String emailUniversal="";

    //Credentials Section
    protected static final String SELECT_QUERY = "SELECT * FROM babylinapp_credentials WHERE Email = ? and password = ?";//EMPLOYEES
    protected static final String SELECT_QUERY_EMAIL = "SELECT * FROM babylinapp_credentials WHERE Email = ? ";//EMPLOYEES
    protected static final String INSERT_QUERY = "INSERT INTO babylinapp_credentials (id, firstName, lastName, DOB, email, password, address, phone) VALUES(?,?,?,?,?,?,?,?)";

    protected static final String SELECT_CUST_QUERY_ = "SELECT * FROM babylinapp_users WHERE userName= ? and email= ?";//CUSTOMERS
    protected static final String SELECT_CUST_QUERY_VALIDATE = "SELECT * FROM babylinapp_users WHERE email= ?";//CUSTOMERS
    protected static final String SELECT_CUST="SELECT * FROM babylinapp_users WHERE email= ? and password= ?";
    protected static final String INSERTS_CUST_QUERY = "INSERT INTO babylinapp_users (userId, userName, email, password, address, phone, DOB) VALUES(?,?,?,?,?,?,?)";

    //Products Section
    protected static final String INSERT_QUERY_PRODUCTS = "INSERT INTO babylinapp_products VALUES(?,?,?,?,?)";//INSERT INTO PRODUCTS TABLE
    protected static final String SELECT_QUERY_PRODUCTS = "SELECT * FROM babylinapp_products";//SELECT FROM PRODUCT TABLE
    protected static final String SELECT_QUERY_PRODUCTS_PRODUCT_NAME="SELECT ProductName FROM babylinapp_products";
    protected static final String SELECT_QUERY_PRODUCTS_ADD="SELECT * FROM babylinapp_products WHERE ProductName=?";//ADD PRODUCT STATEMENT
    protected static final String DELETE_QUERY_PRODUCTS_DELETE="DELETE FROM babylinapp_products WHERE ProductName=?";//DELETE PRODUCT STATEMENT
    protected static final String UPDATE_QUERY_PRODUCTS_QUANTITY="UPDATE babylinapp_products SET quantity = quantity + ? WHERE productName = ? ";// UPDATE PRODUCT STATEMENT
    protected static final String UPDATE_QUERY_PRODUCTS_ALL="UPDATE babylinapp_products SET productName=?, unitPrice=?, quantity = ?, productDescription= ? WHERE productName = ? ";// UPDATE PRODUCT STATEMENT
    protected static final String UPDATE_QUERY_PRODUCTS_QUANTITY_SUB="UPDATE babylinapp_products SET quantity = quantity - ? WHERE productName = ? ";// UPDATE PRODUCT STATEMENT

    //Raw Materials Section
    protected static final String INSERT_QUERY_STOCK = "INSERT INTO babylinapp_rawmaterials VALUES(?,?,?,?,?)";
    protected static final String SELECT_QUERY_RAW_NAME = "SELECT * FROM babylinapp_rawmaterials WHERE rawMaterialName=?";
    protected static final String SELECT_QUERY_RAW="SELECT * FROM babylinapp_rawmaterials";
    protected static final String UPDATE_QUERY_RAW ="UPDATE babylinapp_rawmaterials SET rawMaterialId= ?, rawMaterialName = ?, unitPrice = ?, quantity= ?, rawMaterialDescription = ? WHERE rawMaterialName= ?";
    protected static final String UPDATE_QUERY_RAW_MATERIALS_QUANTITY="UPDATE babylinapp_rawmaterials SET quantity = quantity + ?  WHERE rawMaterialName = ? ";// UPDATE RAW MATERIALS STATEMENT
    protected static final String DELETE_QUERY_RAW_DELETE="DELETE FROM babylinapp_rawmaterials WHERE rawMaterialName=?";//DELETE PRODUCT STATEMENT




    protected boolean validate(String a, String b, String c) {
        String pwdMD5 = getHash(b);
        try
        {
            Connection connection = DriverManager.getConnection(url);//This makes you connect to the database using the login details
            if (c.equals("Employee")) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);//This makes you connect to the database using the login details
                preparedStatement.setString(1, a);//Replaces the question mark ? in the SELECT_QUERY
                preparedStatement.setString(2, pwdMD5);//Replaces the question mark ? in the SELECT_QUERY
                ResultSet resultSet = preparedStatement.executeQuery();//Executes the SELECT_QUERY
                //Note the method executeQuery() of the object preparedStatement is only used for accessing data in from the database. SO the SELECT Command
                //The method executeUpdate() of the object preparedStatement is only used for insert into data in from the database. INSERT INTO, ALTER Command etc
                if (resultSet.next()) {
                    emailUniversal=a;
                    userType=c;
                    return true;
                }
                connection.close();
            }
            if (c.equals("Customer")){
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CUST);//This makes you connect to the database using the login details
                preparedStatement.setString(1, a);//Replaces the question mark ? in the SELECT_QUERY
                preparedStatement.setString(2, pwdMD5);//Replaces the question mark ? in the SELECT_QUERY
                ResultSet resultSet = preparedStatement.executeQuery();//Executes the SELECT_QUERY
                //Note the method executeQuery() of the object preparedStatement is only used for accessing data in from the database. SO the SELECT Command
                //The method executeUpdate() of the object preparedStatement is only used for insert into data in from the database. INSERT INTO, ALTER Command etc
                if (resultSet.next()) {
                    emailUniversal=a;
                    userType=c;
                    return true;
                }
                connection.close();
            }
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    }

    //Abbreviations :
    //1.DOB: Date of Birth
    //2.fn: fullname
    //4.u_type: user type
    //5.pwd & pwd2: First Password entered and Second Password entered
    protected boolean register(String fn, String ln, LocalDate DOB, String u_type, String address, String phone, String email, String pwd, String pwd2) {// The registration function
        if (checkEmail(email,u_type)){
            return false;
        }
        boolean pwdValidate = true;
        String hashpwd1 = getHash(pwd);
        String hasdpwd2 = getHash(pwd2);

        if (u_type.equals("Customer")) {
            try{
                Connection connection = DriverManager.getConnection(url);
//                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CUST_EMAIL);
//                preparedStatement.setString(1,email);
//                ResultSet resultSet = preparedStatement.executeQuery();
//                resultSet.next();
//                if(resultSet.getInt(1)==0){
                    PreparedStatement preparedStatement1 =connection.prepareStatement(INSERTS_CUST_QUERY);
                    preparedStatement1.setString(1, null);
                    preparedStatement1.setString(2, fn+" "+ln);
                    preparedStatement1.setString(3, email);
                    preparedStatement1.setString(5, address);
                    preparedStatement1.setString(6, phone);
                    preparedStatement1.setDate(7, Date.valueOf(DOB));
                    if (hashpwd1.equals(hasdpwd2)) {
                        preparedStatement1.setString(4, hashpwd1);
                        boolean resultSet1 = preparedStatement1.execute();
                        if (!resultSet1) {
                            userType="Customer";
                            emailUniversal=email;
                        }
//                        preparedStatement.close();
//                        resultSet.close();
                        preparedStatement1.close();
                        connection.close();
                    } else {
                        pwdValidate = false;
                        System.out.println("not working register customer ");
                    }
//                }
                connection.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }
        if (u_type.equals("Employee")) {
            try {
                    Connection connection = DriverManager.getConnection(url);
                    PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, fn);
                preparedStatement.setString(3, ln);
                preparedStatement.setDate(4, Date.valueOf(DOB));
                preparedStatement.setString(5, email);
                preparedStatement.setString(7, address);
                preparedStatement.setString(8, phone);
                if (hashpwd1.equals(hasdpwd2)) {//User enters password twice and it is verified if they are equal
                    preparedStatement.setString(6, hashpwd1);//Replaces the question mark ? in the INSERT_QUERY
                    System.out.println("Result:" + preparedStatement);
                    boolean resultSet = preparedStatement.execute();//Here the method execute returns a boolean value
                    connection.close();
                    if (!resultSet) {
                        // This is used to valid that the process worked. Hence at the end of the function I use a if statement to enable the function return true or false
                        System.out.println("Worked");
                        userType="Employee";
                        emailUniversal=email;
                    }
                } else {
                    pwdValidate = false;
                    System.out.println("Not working");
                }
                connection.close();
            } catch (SQLException e) {
                Controller.errBox(e.getMessage()+"\n"+e.getLocalizedMessage()+"\n","Error with generating reports","Error");
            }
        }
        return pwdValidate;
    }

    private boolean checkEmail(String email, String userType) {
        boolean check = false;
            if (userType.equals("Customer")) {
                try {
                    Connection connection = DriverManager.getConnection(url);
                    PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CUST_QUERY_VALIDATE);
                    preparedStatement.setString(1, email);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next())
                        check=true;
                    preparedStatement.close();
                    resultSet.close();
                    connection.close();
                } catch (SQLException e) {
                    printSQLException(e);
                }
            }
        if (userType.equals("Employee")) {
            try {
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY_EMAIL);
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next())
                    check=true;
                preparedStatement.close();
                resultSet.close();
                connection.close();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }
            return check;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                Controller.infoBox("SQLState: " + ((SQLException) e).getSQLState()+
                        "\n"+"Error Code: " + ((SQLException) e).getErrorCode()+
                        "\n"+"Message: " + e.getMessage(),null,"Error");
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    Controller.infoBox("Cause: "+t,null,"Error");
                    t = t.getCause();
                }
            }
        }
    }

    public static void printMessagingException(MessagingException mer){
            if (mer != null) {
                mer.printStackTrace(System.err);
                Controller.infoBox("Message Error: " + mer.getMessage()+
                        "\n"+"Error Code: " + Arrays.toString(mer.getStackTrace()) +
                        "\n"+"Message: " + mer.getLocalizedMessage(),null,"Error");
                Throwable t = mer.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    Controller.infoBox("Cause: "+t,null,"Error");
                    t = t.getCause();
                }
            }
    }

    public static void printIOExpection(IOException ioe){
        if (ioe != null) {
            ioe.printStackTrace(System.err);
            Controller.infoBox("IO Error: " + ioe.getMessage()+
                    "\n"+"Error Code: " + Arrays.toString(ioe.getStackTrace()) +
                    "\n"+"Message: " + ioe.getLocalizedMessage(),null,"Error");
            Throwable t = ioe.getCause();
            while (t != null) {
                System.out.println("Cause: " + t);
                Controller.infoBox("Cause: "+t,null,"Error");
                t = t.getCause();
            }
        }
    }


    protected static String getHash(String input) {
        StringBuilder hashtext = new StringBuilder();
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
        }

        catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return hashtext.toString();
    }

}