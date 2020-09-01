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
import java.util.Arrays;

public class jdbcController {
    protected static final String url = "jdbc:mysql://localhost:3306/babylinapp_db?useSSL=false";// Allows you to connect to the database through the localhost

    //SQL Syntax To insert or access data in SQL

    protected static String userType="";
    protected static String emailUniversal="";

    //Credentials Section
    protected static final String SELECT_QUERY = "SELECT * FROM babylinapp_credentials WHERE Email = ? and password = ?";//EMPLOYEES
    protected static final String SELECT_QUERY_EMAIL = "SELECT * FROM babylinapp_credentials WHERE Email = ? ";//EMPLOYEES
    protected static final String INSERT_QUERY = "INSERT INTO babylinapp_credentials (id, firstName, lastName, DOB, email, password, address, phone) VALUES(?,?,?,?,?,?,?,?)";

    protected static final String SELECT_CUST_QUERY_ = "SELECT * FROM babylinapp_users WHERE userName= ? and email= ?";//CUSTOMERS
    protected static final String SELECT_CUST="SELECT * FROM babylinapp_users WHERE email= ? and password= ?";
    protected static final String SELECT_CUST_EMAIL="SELECT * FROM babylinapp_users WHERE email= ?";
    protected static final String INSERTS_CUST_QUERY = "INSERT INTO babylinapp_users (userId, userName, email, password, address, phone, DOB) VALUES(?,?,?,?,?,?,?)";

    //Products Section
    protected static final String INSERT_QUERY_PRODUCTS = "INSERT INTO babylinapp_products VALUES(?,?,?,?,?)";//INSERT INTO PRODUCTS TABLE
    protected static final String SELECT_QUERY_PRODUCTS = "SELECT * FROM babylinapp_products";//SELECT FROM PRODUCT TABLE
    protected static final String SELECT_QUERY_PRODUCTS_PRODUCT_NAME="SELECT ProductName FROM babylinapp_products";
    protected static final String SELECT_QUERY_PRODUCTS_ADD="SELECT * FROM babylinapp_products WHERE ProductName=?";//ADD PRODUCT STATEMENT
    protected static final String DELETE_QUERY_PRODUCTS_DELETE="DELETE FROM babylinapp_products WHERE ProductName=?";//DELETE PRODUCT STATEMENT
    protected static final String UPDATE_QUERY_PRODUCTS_QUANTITY="UPDATE babylinapp_products SET quantity = quantity + ? WHERE productName = ? ";// UPDATE PRODUCT STATEMENT
    protected static final String UPDATE_QUERY_PRODUCTS_ALL="UPDATE babylinapp_products SET productId=?, productName=?, unitPrice=?, quantity = ?, productDescription=? WHERE productName = ? ";// UPDATE PRODUCT STATEMENT
    protected static final String UPDATE_QUERY_PRODUCTS_QUANTITY_SUB="UPDATE babylinapp_products SET quantity = quantity - ? WHERE productName = ? ";// UPDATE PRODUCT STATEMENT

    //Raw Materials Section
    protected static final String INSERT_QUERY_STOCK = "INSERT INTO babylinapp_stock VALUES(?,?,?,?,?)";
    protected static final String SELECT_QUERY_RAW_NAME = "SELECT * FROM babylinapp_stock WHERE rawMaterialName=?";
    protected static final String SELECT_QUERY_RAW="SELECT * FROM babylinapp_stock";
    protected static final String UPDATE_QUERY_RAW ="UPDATE babylinapp_stock SET rawMaterialId= ?, rawMaterialName = ?, unitPrice = ?, quantity= ?, rawMaterialDescription = ? WHERE rawMaterialName= ?";
    protected static final String UPDATE_QUERY_RAW_MATERIALS_QUANTITY="UPDATE babylinapp_stock SET quantity = quantity + ?  WHERE rawMaterialName = ? ";// UPDATE RAW MATERIALS STATEMENT
    protected static final String DELETE_QUERY_RAW_DELETE="DELETE FROM babylinapp_stock WHERE rawMaterialName=?";//DELETE PRODUCT STATEMENT


    //Orders Section


    //DB User
    protected static final String user = "bravo";//MYSQL user name
    protected static final String password = "brav123";//MYSQL password



    public boolean validate(String a, String b, String c) {
        String pwdMD5 = getMd5(b);
        try
        {
            Connection connection = DriverManager.getConnection(url, user, password);//This makes you connect to the database using the login details
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
    public boolean register(String fn, String DOB, String u_type, String address, String phone, String email, String pwd, String pwd2) {// The registration function

        boolean pwdValidate = true;
        String hashpwd1 = getMd5(pwd);// used a hash function to encrypt the passwords before storing them.
        String hasdpwd2 = getMd5(pwd2);

        if (u_type.equals("Customer")) {//Ignore this is specific to my code
            try (
                    Connection connection = DriverManager.getConnection(url, user, password);
                    PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CUST_QUERY_)

            ) {
                preparedStatement.setString(1,fn);
                preparedStatement.setString(2,email);
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println(resultSet.getFetchSize());
                if(resultSet.getFetchSize()==1){
                   PreparedStatement preparedStatement1 =connection.prepareStatement(INSERTS_CUST_QUERY);
                    preparedStatement1.setString(1, null);
                    preparedStatement1.setString(2, fn);
                    preparedStatement1.setString(3, email);
                    preparedStatement1.setString(5, address);
                    preparedStatement1.setString(6, phone);
                    preparedStatement1.setString(7, DOB);
                    if (hashpwd1.equals(hasdpwd2)) {
                        preparedStatement.setString(4, hashpwd1);
                        System.out.println("Result:" + preparedStatement);
                        boolean resultSet1 = preparedStatement.execute();
                        connection.close();
                        if (!resultSet1) {
                            System.out.println("Worked register customer worked");
                            userType="Customer";
                            emailUniversal=email;
                        }
                    } else {
                        pwdValidate = false;
                        System.out.println("not working register customer ");
                    }
                }
            } catch (SQLException e) {
                printSQLException(e);
            }
        }
        if (u_type.equals("Employee")) {
            try (
                    Connection connection = DriverManager.getConnection(url, user, password);
                    PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)

            ) {
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, fn);
                preparedStatement.setString(3, email);
                preparedStatement.setString(5, address);
                preparedStatement.setString(6, phone);
                preparedStatement.setString(7, DOB);
                if (hashpwd1.equals(hasdpwd2)) {//User enters password twice and it is verified if they are equal
                    preparedStatement.setString(4, hashpwd1);//Replaces the question mark ? in the INSERT_QUERY
                    System.out.println("Result:" + preparedStatement);
                    boolean resultSet = preparedStatement.execute();//Here the method execute returns a boolean value
                    connection.close();
                    if (resultSet) {
                        // This is used to valid that the process worked. Hence at the end of the function I use a if statement to enable the function return true or false
                        System.out.println("Worked");
                        userType="Employee";
                        emailUniversal=email;
                    }
                } else {
                    pwdValidate = false;
                    System.out.println("Not working");
                }
            } catch (SQLException e) {
                printSQLException(e);
            }
        }
        return pwdValidate;
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

    //Got this function from geeksforgeeks
    private static String getMd5(String input) {//Refine
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}