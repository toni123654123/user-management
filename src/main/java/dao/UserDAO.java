package dao;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public  class UserDAO implements IUserDAO {
    private static UserDAO instance;

    private String jdbcURL      = "jdbc:mysql://localhost:3306/demo";
    private String jdbcUsername = "root";
    private String jdbcPassword = "cclytn1105..";

    private static Connection connection = null;

    private static final String INSERT_USERS_SQL = "INSERT INTO users (name, email, country) VALUES (?, ?, ?);";

    private static final String SELECT_USER_BY_ID = "SELECT id,name,email,country FROM users WHERE id =?";

    private static final String SELECT_ALL_USERS  = "SELECT * FROM users";

    private static final String DELETE_USERS_SQL  = "delete FROM users WHERE id = ?;";
    private static final String UPDATE_USERS_SQL  = "update users SET name = ?,email= ?, country =? WHERE id = ?;";

    public UserDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            System.out.println("ok connecting...");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
    }

    public static UserDAO getInstance() {
        if (instance == null){
            synchronized (UserDAO.class){
                instance = new UserDAO();
            }
        }
        return instance;
    }

    public void insertUser(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public User selectUser(int id) {
        User user = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        // Step 1: Establishing a Connection
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println(rs);

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    public List<User> sortAllUsers(String sort) {
        if (!sort.equals("id") && !sort.equals("name") && !sort.equals("email") && !sort.equals("country")){
            sort = "id";
        }
        String SORT_ALL_USERS = "SELECT * FROM users ORDER BY `"+sort+"` ASC";
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();

            System.out.println(statement);
            ResultSet rs = statement.executeQuery(SORT_ALL_USERS);
            System.out.println(rs);
            System.out.println(rs.getFetchSize());

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }


    public List<User> searchAndSort(String type, String search, String sort) {

        if (!sort.equals("id") && !sort.equals("name") && !sort.equals("email") && !sort.equals("country")){
            sort = "id";
        }

        if (!type.equals("name") && !type.equals("country")){
            type = "country";
        }

        String query = "SELECT * FROM `users` WHERE `"+type+"` LIKE ? ORDER BY `"+sort+"` ASC;";

        List<User> users = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + search + "%");

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println(rs.getFetchSize());

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }



    public boolean deleteUser(int id) {
        boolean rowDeleted = false;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(DELETE_USERS_SQL);
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDeleted;
    }

    public boolean updateUser(User user) {
        boolean rowUpdated = false;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(UPDATE_USERS_SQL);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setInt(4, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowUpdated;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}