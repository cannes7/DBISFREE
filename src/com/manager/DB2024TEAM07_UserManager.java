package com.manager;

import com.app.DB2024TEAM07_Main;
import com.jdbc.database.DB2024TEAM07_UserDAO;
import com.jdbc.view.DB2024TEAM07_UserVO;
import com.jdbc.model.DB2024TEAM07_User;

import java.util.List;
import java.util.Scanner;

public class DB2024TEAM07_UserManager {
    private static DB2024TEAM07_UserDAO userDAO;
    private static DB2024TEAM07_User loggedInUser;

    public DB2024TEAM07_UserManager(DB2024TEAM07_UserDAO userDAO) {
        DB2024TEAM07_UserManager.userDAO = userDAO;
    }

    // 사용자 로그인
    public boolean login(String username, String password) {
        int loginResult = userDAO.signIn(username, password);
        if (loginResult == 1) {
            loggedInUser = userDAO.getUser(username);
            return true;
        }
        return false;
    }

    // 사용자 로그아웃
    public void logout() {
        loggedInUser = null;
    }

    // 사용자 추가 (회원 가입)
    public boolean addUser(DB2024TEAM07_User user) {
        return userDAO.add(user) > 0;
    }


    public static void showMyInfo() {
        DB2024TEAM07_User user = userDAO.getUser(loggedInUser.getUser_id());
        System.out.println("\n=== My Information ===\n");
        System.out.println("ID: " + user.getUser_id());
        System.out.println("Name: " + user.getName());
        System.out.println("Student ID: " + user.getStudent_id());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Location: " + user.getLocation());
        System.out.println("\n======================");
    }

    public static void searchOtherUser(Scanner scanner) {
        System.out.print("Enter the user ID: ");
        String userId = scanner.nextLine();
        DB2024TEAM07_UserVO otherUser = userDAO.getOtherUser(userId);

        if (otherUser != null) {
            System.out.println("\n== User Information ==\n");
            System.out.println("ID: " + otherUser.getUser_id());
            System.out.println("Name: " + otherUser.getName());
            System.out.println("\n======================");
        } else {
            System.out.println("The user could not be found..");
        }
    }

    public static void deleteAccount(Scanner scanner) {
        System.out.print("\nAre you sure you want to delete your account?\n");
        System.out.print("All data related to your will be deleted.\n\n");
        System.out.print("Enter 'y' if you want to delete, or 'n': ");
        String confirm = scanner.nextLine().toLowerCase();

        if (confirm.equals("y")) {
            System.out.print("Please enter your PASSWORD: ");
            String password = scanner.nextLine();
            int result = userDAO.delete(loggedInUser.getUser_id(), password);

            if (result > 0) {
                System.out.println("Membership withdrawal completed.");
                DB2024TEAM07_Main.main(null);
                System.exit(0);
            } else {
                System.out.println("The password does not match.");
            }
        } else if (confirm.equals("n")) {
            System.out.println("Membership withdrawal canceled.");
        } else {
            System.out.println("Invalid input. Membership withdrawal canceled.");
        }

    }

    public static void update(Scanner scanner) {
        System.out.println("\n===== Update My information =====\n");
        System.out.print("New ID (Press enter to skip): ");
        String newUserId = scanner.nextLine();
        String userId = newUserId.isEmpty() ? loggedInUser.getUser_id() : newUserId;

        System.out.print("New Password (Press enter to skip): ");
        String newUserPw = scanner.nextLine();
        String userPw = newUserPw.isEmpty() ? loggedInUser.getUser_pw() : newUserPw;

        System.out.print("New Name (Press enter to skip): ");
        String newName = scanner.nextLine();
        String name = newName.isEmpty() ? loggedInUser.getName() : newName;

        System.out.print("New Student ID (Press enter to skip): ");
        String newStudentIdStr = scanner.nextLine();
        int studentId = newStudentIdStr.isEmpty() ? loggedInUser.getStudent_id() : Integer.parseInt(newStudentIdStr);

        System.out.print("New Email (Press enter to skip): ");
        String newEmail = scanner.nextLine();
        String email = newEmail.isEmpty() ? loggedInUser.getEmail() : newEmail;

        System.out.print("New Location (Press enter to skip): ");
        String newLocation = scanner.nextLine();
        String location = newLocation.isEmpty() ? loggedInUser.getLocation() : newLocation;

        DB2024TEAM07_User updatedUser = new DB2024TEAM07_User(userId, userPw, name, studentId, email, location);
        int result = userDAO.update(updatedUser, loggedInUser.getUser_id());

        if (result > 0) {
            System.out.println("Update successful!");
            loggedInUser = updatedUser; // 업데이트된 사용자 정보로 loggedInUser 업데이트
        } else {
            System.out.println("Failed to update information.");
        }

    }

    public static void displayAllUsers() {
        List<DB2024TEAM07_User> allUsers = userDAO.getAllUsers();

        System.out.println("\n=== All Users ===\n");
        System.out.printf("%-15s%-15s%-15s%-25s%-15s%n", "ID", "Name", "Student ID", "Email", "Location");
        System.out.println("-------------------------------------------------------------------------------");
        for (DB2024TEAM07_User user : allUsers) {
            System.out.printf("%-15s%-15s%-15d%-25s%-15s%n",
                    user.getUser_id(), user.getName(), user.getStudent_id(),
                    user.getEmail(), user.getLocation());
        }
    }

    public void addAccountByManager(Scanner scanner) {
        System.out.print("\nEnter the new user information:\n");
        System.out.print("ID: ");
        String userId = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Location: ");
        String location = scanner.nextLine();

        // 새로운 사용자 객체 생성
        DB2024TEAM07_User newUser = new DB2024TEAM07_User(userId, password, name, studentId, email, location);

        // DB2024TEAM07_UserDAO 객체 생성
        DB2024TEAM07_UserDAO userDAO = new DB2024TEAM07_UserDAO();

        // 사용자 추가 메서드 호출
        int success = userDAO.add(newUser);

        // 결과 출력
        if (success > 0) {
            System.out.println("The new user has been successfully added.");
        } else {
            System.out.println("Failed to add the new user.");
        }
    }

    public void updateAccountByManager(Scanner scanner) {
        System.out.print("\nEnter the ID of the user to update: ");
        String userId = scanner.nextLine();
        DB2024TEAM07_User userToUpdate = userDAO.getUser(userId);

        if (userToUpdate != null) {
            System.out.print("New Password (Press enter to skip): ");
            String newPassword = scanner.nextLine();
            String password = newPassword.isEmpty() ? userToUpdate.getUser_pw() : newPassword;

            System.out.print("New Name (Press enter to skip): ");
            String newName = scanner.nextLine();
            String name = newName.isEmpty() ? userToUpdate.getName() : newName;

            System.out.print("New Student ID (Press enter to skip): ");
            String newStudentIdStr = scanner.nextLine();
            int studentId = newStudentIdStr.isEmpty() ? userToUpdate.getStudent_id() : Integer.parseInt(newStudentIdStr);

            System.out.print("New Email (Press enter to skip): ");
            String newEmail = scanner.nextLine();
            String email = newEmail.isEmpty() ? userToUpdate.getEmail() : newEmail;

            System.out.print("New Location (Press enter to skip): ");
            String newLocation = scanner.nextLine();
            String location = newLocation.isEmpty() ? userToUpdate.getLocation() : newLocation;

            // 업데이트된 사용자 객체 생성
            DB2024TEAM07_User updatedUser = new DB2024TEAM07_User(userId, password, name, studentId, email, location);

            // 사용자 정보 업데이트 메서드 호출
            int result = userDAO.update(updatedUser, userId);

            if (result > 0) {
                System.out.println("User information updated successfully!");
            } else {
                System.out.println("Failed to update user information.");
            }
        } else {
            System.out.println("User not found.");
        }
    }

    public void searchAccountByManager(Scanner scanner) {
        System.out.print("\nEnter the ID of the user to search: ");
        String userId = scanner.nextLine();
        DB2024TEAM07_User user = userDAO.getUser(userId);

        if (user != null) {
            System.out.println("\n=== User Information ===\n");
            System.out.printf("%-15s%-15s%-15s%-25s%-15s%n", "ID", "Name", "Student ID", "Email", "Location");
            System.out.println("-------------------------------------------------------------------------------");
            System.out.printf("%-15s%-15s%-15d%-25s%-15s%n",
                    user.getUser_id(), user.getName(), user.getStudent_id(),
                    user.getEmail(), user.getLocation());
            System.out.println("\n======================");
        } else {
            System.out.println("User not found.");
        }
    }


    public void deleteAccountByManager(Scanner scanner) {
        System.out.print("\nEnter the user ID you want to delete: ");
        String userId = scanner.nextLine();

        System.out.print("Enter the password: ");
        String password = scanner.nextLine();

        int result = userDAO.delete(userId, password);

        if (result > 0) {
            System.out.println("The user account has been successfully deleted.");
        } else if (result == 0) {
            System.out.println("The password does not match.");
        } else {
            System.out.println("An error occurred while deleting the user account.");
        }
    }
}