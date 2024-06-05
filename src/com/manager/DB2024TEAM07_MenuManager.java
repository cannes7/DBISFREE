package com.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.jdbc.database.DB2024TEAM07_MenuDAO;
import com.jdbc.model.DB2024TEAM07_Menu;

public class DB2024TEAM07_MenuManager {

    private static DB2024TEAM07_MenuDAO menuDAO = new DB2024TEAM07_MenuDAO();

    /* Add Function */
    public static void addMenu(Scanner scanner) {
        System.out.print("Enter Menu ID: ");
        int menu_id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Menu Name: ");
        String menu_name = scanner.nextLine();

        System.out.print("Enter Restaurant ID: ");
        int res_id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Price: ");
        int price = scanner.nextInt();
        scanner.nextLine();

        DB2024TEAM07_Menu menu = new DB2024TEAM07_Menu(menu_id, menu_name, res_id, price);
        int result = menuDAO.add(menu);

        if (result > 0) {
            System.out.println("Menu added successfully.");
        } else {
            System.out.println("Error adding menu.");
        }
    }

    /* Update Function */
    public static void updateMenu(Scanner scanner, DB2024TEAM07_MenuDAO menuDAO) {
        System.out.print("Enter Restaurant ID: ");
        int res_id = scanner.nextInt();
        scanner.nextLine();

        displayAllMenu(res_id);

        System.out.print("Enter Menu ID: ");
        int menu_id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter New Menu Name (Press enter to skip): ");
        String newMenuName = scanner.nextLine();

        System.out.print("Enter New Price (Press enter to skip): ");
        String priceInput = scanner.nextLine();
        int newPrice = -1;
        if (!priceInput.isEmpty()) {
            newPrice = Integer.parseInt(priceInput);
        }

        DB2024TEAM07_Menu updatedMenu = new DB2024TEAM07_Menu(
                menu_id,
                newMenuName.isEmpty() ? null : newMenuName,
                res_id,
                newPrice
        );

        int result = menuDAO.update(updatedMenu, res_id, menu_id);

        if (result > 0) {
            System.out.println("Menu updated successfully.");
        } else {
            System.out.println("Error updating menu.");
        }
    }

    /* Search Functions */
    public static void searchByUsers(Scanner scanner) {
        System.out.print("Enter Restaurant Name (or press Enter to skip): ");
        String restaurantName = scanner.nextLine();
        if (restaurantName.trim().isEmpty()) restaurantName = null;

        System.out.print("Enter Menu Name (or press Enter to skip): ");
        String menuName = scanner.nextLine();
        if (menuName.trim().isEmpty()) menuName = null;

        System.out.print("Enter Minimum Price (or press Enter to skip): ");
        String minPriceInput = scanner.nextLine();
        Integer minPrice = null;
        if (!minPriceInput.trim().isEmpty()) {
            try {
                minPrice = Integer.parseInt(minPriceInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid minimum price input. Please enter a valid number or press Enter to skip.");
                return;
            }
        }

        System.out.print("Enter Maximum Price (or press Enter to skip): ");
        String maxPriceInput = scanner.nextLine();
        Integer maxPrice = null;
        if (!maxPriceInput.trim().isEmpty()) {
            try {
                maxPrice = Integer.parseInt(maxPriceInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid maximum price input. Please enter a valid number or press Enter to skip.");
                return;
            }
        }

        ResultSet rs = menuDAO.searchByUsers(restaurantName, menuName, minPrice, maxPrice);
        try {
            while (rs != null && rs.next()) {
                System.out.println("Restaurant Name: " + rs.getString("res_name"));
                System.out.println("Menu Name: " + rs.getString("menu_name"));
                System.out.println("Price: " + rs.getInt("price"));
                System.out.println("----------------------------");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public static void searchMenuByRestaurant(Scanner scanner) {
        System.out.print("Enter Restaurant ID: ");
        int restaurantId = scanner.nextInt();
        scanner.nextLine();

        ResultSet rs = menuDAO.searchMenuByRestaurant(restaurantId);
        try {
            while (rs != null && rs.next()) {
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Menu Name: " + rs.getString("menu_name"));
                System.out.println("Price: " + rs.getInt("price"));
                System.out.println("----------------------------");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public static void searchByManager(Scanner scanner) {
        System.out.print("Enter Restaurant ID: ");
        int resId = scanner.nextInt();

        ResultSet rs = menuDAO.searchByManager(resId);
        try {
            while (rs != null && rs.next()) {
                System.out.println("Menu ID: " + rs.getInt("menu_id"));
                System.out.println("Restaurant ID: " + rs.getInt("res_id"));
                System.out.println("Menu Name: " + rs.getString("menu_name"));
                System.out.println("Price: " + rs.getInt("price"));
                System.out.println("----------------------------");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    /* Delete Function */
    public static void deleteMenu(Scanner scanner) {
        System.out.print("Enter Restaurant ID: ");
        int res_id = scanner.nextInt();
        scanner.nextLine();

        displayAllMenu(res_id);

        System.out.print("Enter Menu ID: ");
        int menu_id = scanner.nextInt();
        scanner.nextLine();

        int result = menuDAO.delete(res_id, menu_id);

        if (result > 0) {
            System.out.println("Menu deleted successfully.");
        } else {
            System.out.println("Error deleting menu.");
        }
    }

    /* Print Function */
    public static void displayAllMenu(int res_id) {
        ResultSet resultSet = menuDAO.getAllMenuByRestaurant(res_id);

        // Print header
        System.out.println("--------------------------------");
        System.out.println("Menu ID\t\tMenu Name");
        System.out.println("--------------------------------");

        try {
            while (resultSet != null && resultSet.next()) {
                System.out.printf("%-8d\t%-30s%n", resultSet.getInt("menu_id"), resultSet.getString("menu_name"));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        // Footer
        System.out.println("--------------------------------");
    }
}