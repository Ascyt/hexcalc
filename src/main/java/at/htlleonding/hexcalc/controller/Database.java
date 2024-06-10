package at.htlleonding.hexcalc.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Database {
    private static Connection dbConnection;

    public static void initialize() {
        System.out.println("= Opening connection. =");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:db");
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbConnection = connection;
    }

    public static void close() {
        System.out.println("= Closing connection. =");
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        }
        catch (Exception ex) {

        }
    }

    public static List<HistoryItem> getAllHistoryItems() {
        System.out.println("Get all history items");
        List<HistoryItem> historyItems = new LinkedList<>();
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT expression, evaluation FROM history");

            while (resultSet.next()) {
                String expression = resultSet.getString("expression");
                String evaluation = resultSet.getString("evaluation");
                historyItems.add(new HistoryItem(expression, evaluation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyItems;
    }

    public static int addHistoryItem(HistoryItem historyItem) {
        System.out.println("Add history item (" + historyItem.expression + ", " + historyItem.evaluation + ")");
        try {
            PreparedStatement preparedStatement =
                    dbConnection.prepareStatement("INSERT INTO history (expression, evaluation) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, historyItem.expression);
            preparedStatement.setString(2, historyItem.evaluation);

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                return resultSet.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void clearHistory() {
        System.out.println("Clear history");
        try {
            PreparedStatement statement = dbConnection.prepareStatement("DELETE FROM history");

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isBanned() {
        System.out.println("Get input disabled");
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT is_banned FROM isBanned");

            if (resultSet.next()) {
                boolean isBanned = resultSet.getBoolean("is_banned");
                System.out.println("> " + (isBanned ? "true" : "false"));
                return isBanned;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setBanned(boolean banned) {
        System.out.println("Set input disabled " + (banned ? "true" : "false"));
        try {
            PreparedStatement preparedStatement =
                    dbConnection.prepareStatement("UPDATE isBanned SET is_banned = ?");

            preparedStatement.setBoolean(1, banned);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
