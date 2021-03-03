package com.company;
import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;
public class Database {
    public void start() {
        Scanner s = new Scanner(System.in);
        Connection connection = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            // Establish the connection
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ElectricityBillingSystem", "postgres", "jjustboss11");
            if (connection != null) {
                System.out.println("Opened the database \n");
            }
            stmt = connection.createStatement();


            while (true) {
                System.out.println("What do you want to do? Choose one of the options:");
                System.out.println("1. Calculate the total energy consumption");
                System.out.println("2. See energy consumption of different electrical techniques:");
                System.out.println("3. Calculate the total power of household techniques: ");
                System.out.println("4. Exit program");
                int input = s.nextInt();
                if (input == 1) {
                    System.out.println("How many electrical techniques you want to add?");
                    int number = s.nextInt();
                    ArrayList<String> techniques = new ArrayList<>();
                    ArrayList<Integer> techniqueConsumption = new ArrayList<>();
                    for (int i = 0; i < number; i++) {
                        System.out.println("Please type the name of the electrical technique: ");
                        String tempTechnique = s.next();
                        techniques.add(tempTechnique);
                    }
                    for (int i = 0; i < number; i++) {
                        String query = "SELECT techniqueName, techniqueConsumption FROM techniques WHERE techniqueName = ?";
                        try (PreparedStatement ps = connection.prepareStatement(query)) {
                            ps.setString(1, techniques.get(i));
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                techniqueConsumption.set(i, rs.getInt("techniqueConsumption"));
                            }
                            rs.close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                    for (int i = 0; i < number; i++) {
                        System.out.println("Technique: " + techniques.get(i) + " consumes " + techniqueConsumption.get(i) + "W energy!");
                    }
                }
                if (input == 2) {
                    ResultSet rs = stmt.executeQuery("SELECT * FROM techniques");
                    while (rs.next()){
                        String techniqueName = rs.getString("techniqueName");
                        int techniqueConsumption = rs.getInt("techniqueConsumption");
                        System.out.println("Technique: " + techniqueName + " consumes " + techniqueConsumption + "W energy!");
                    }
                }
                if (input == 3){
                    System.out.println("How many electrical techniques do you have in your house?");
                    int number = s.nextInt();
                    ArrayList<String> techniques = new ArrayList<>();
                    ArrayList<Integer> techniquePower = new ArrayList<>();
                    for (int i = 0; i < number; i++) {
                        System.out.println("Please type the name of the electrical technique: ");
                        String tempTechnique = s.next();
                        techniques.add(tempTechnique);
                    }
                    for (int i = 0; i < number; i++) {
                        String query = "SELECT techniqueName, techniquePower FROM techniques WHERE techniqueName = ?";
                        try (PreparedStatement ps = connection.prepareStatement(query)) {
                            ps.setString(1, techniques.get(i));
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                techniquePower.set(i, rs.getInt("techniqueConsumption"));
                            }
                            rs.close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                    for (int i = 0; i < number; i++) {
                        System.out.println("Technique: " + techniques.get(i) + " have power of " + techniquePower.get(i) + " amperes!");
                    }
                } else {
                    break;
                }
            }
            stmt.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}