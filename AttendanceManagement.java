/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication47;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


class Student {
    private String name;
    private String password;
    private Map<String, Boolean> attendanceByDate; // Track attendance by date

    public Student(String name, String password) {
        this.name = name;
        this.password = password;
        this.attendanceByDate = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void updateAttendance(String date) {
        attendanceByDate.put(date, true); // Mark attendance for the given date
    }

    public void markAbsent(String date) {
        attendanceByDate.put(date, false); // Mark absent for the given date
    }

    public int getDaysAttendedInMonth(int month, int year) {
        int daysAttended = 0;
        for (Map.Entry<String, Boolean> entry : attendanceByDate.entrySet()) {
            String[] dateParts = entry.getKey().split("-");
            int attMonth = Integer.parseInt(dateParts[1]);
            int attYear = Integer.parseInt(dateParts[0]);
            if (attMonth == month && attYear == year && entry.getValue()) {
                daysAttended++;
            }
        }
        return daysAttended;
    }

    public Map<String, Boolean> getAttendanceByDate() {
        return attendanceByDate;
    }

    public String getAttendanceForDate(String date) {
        Boolean attendance = attendanceByDate.get(date);
        if (attendance != null) {
            return attendance ? "Present" : "Absent";
        } else {
            return "Not Available";
        }
    }
}

class Teacher {
    private String username;
    private String password;

    public Teacher(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

class AttendanceManagementSystem {
    private Map<String, Object> users;
    private String loggedInUser;

    public AttendanceManagementSystem() {
        this.users = new HashMap<>();
        this.loggedInUser = null;
    }

    public void registerUser(String username, String password, boolean isStudent) {
        if (isStudent) {
            users.put(username, new Student(username, password));
        } else {
            users.put(username, new Teacher(username, password));
        }
    }

    public boolean login(String username, String password) {
        if (users.containsKey(username)) {
            Object user = users.get(username);
            if (user instanceof Student) {
                Student student = (Student) user;
                if (student.getPassword().equals(password)) {
                    loggedInUser = username;
                    return true;
                }
            } else if (user instanceof Teacher) {
                Teacher teacher = (Teacher) user;
                if (teacher.getPassword().equals(password)) {
                    loggedInUser = username;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isStudent(String username) {
        Object user = users.get(username);
        return user instanceof Student;
    }

    public void updateAttendanceByDate(String studentName) {
        if (loggedInUser != null) {
            Object user = users.get(loggedInUser);
            if (user instanceof Student) {
                if (loggedInUser.equals(studentName)) {
                    String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    ((Student) user).updateAttendance(currentDate);
                    JOptionPane.showMessageDialog(null, "Attendance updated for " + studentName + " on " + currentDate);
                } else {
                    JOptionPane.showMessageDialog(null, "You can only update attendance for yourself.");
                }
            } else if (user instanceof Teacher) {
                Object student = users.get(studentName);
                if (student instanceof Student) {
                    String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    ((Student) student).updateAttendance(currentDate);
                    JOptionPane.showMessageDialog(null, "Attendance updated for " + studentName + " on " + currentDate);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid student name.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please log in first.");
        }
    }

    public void markAbsent(String studentName, String date) {
        if (loggedInUser != null) {
            Object user = users.get(loggedInUser);
            if (user instanceof Student) {
                if (loggedInUser.equals(studentName)) {
                    ((Student) user).markAbsent(date);
                    JOptionPane.showMessageDialog(null, loggedInUser + " marked absent on " + date);
                } else {
                    JOptionPane.showMessageDialog(null, "You can only mark yourself absent.");
                }
            } else if (user instanceof Teacher) {
                Object student = users.get(studentName);
                if (student instanceof Student) {
                    ((Student) student).markAbsent(date);
                    JOptionPane.showMessageDialog(null, studentName + " marked absent on " + date);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid student name.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please log in first.");
        }
    }

    public String getAttendanceStatus() {
        StringBuilder message = new StringBuilder("Attendance Status:\n\n");

        for (Map.Entry<String, Object> entry : users.entrySet()) {
            if (entry.getValue() instanceof Student) {
                Student student = (Student) entry.getValue();
                int daysAttended = 0;
                int totalDays = student.getAttendanceByDate().size();

                for (boolean attended : student.getAttendanceByDate().values()) {
                    if (attended) {
                        daysAttended++;
                    }
                }

                String studentName = student.getName();
                String formattedStatus = String.format("%-15s Days Attended: %-5d Days Absent: %-5d\n",
                        studentName + ":", daysAttended, totalDays - daysAttended);
                message.append(formattedStatus);
            }
        }
        return message.toString();
    }

    public List<String> getStudentNames() {
        List<String> studentNames = new ArrayList<>();
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            if (entry.getValue() instanceof Student) {
                studentNames.add(entry.getKey());
            }
        }
        return studentNames;
    }

    public int getDaysAttendedInMonth(String studentName, int month, int year) {
        int daysAttended = 0;
        Object user = users.get(studentName);
        if (user instanceof Student) {
            Student student = (Student) user;
            for (Map.Entry<String, Boolean> entry : student.getAttendanceByDate().entrySet()) {
                String[] dateParts = entry.getKey().split("-");
                int attMonth = Integer.parseInt(dateParts[1]);
                int attYear = Integer.parseInt(dateParts[0]);
                if (attMonth == month && attYear == year && entry.getValue()) {
                    daysAttended++;
                }
            }
        }
        return daysAttended;
    }

    public String getAttendanceForDate(String studentName, String date) {
        Object user = users.get(studentName);
        if (user instanceof Student) {
            Student student = (Student) user;
            return student.getAttendanceForDate(date);
        } else {
            return "Invalid student name.";
        }
    }
public double calculateAttendancePercentage(String studentName) {
    Object user = users.get(studentName);
    if (user instanceof Student) {
        Student student = (Student) user;
        int daysAttended = 0;
        int totalDays = student.getAttendanceByDate().size();

        for (boolean attended : student.getAttendanceByDate().values()) {
            if (attended) {
                daysAttended++;
            }
        }

        if (totalDays > 0) {
            return (double) daysAttended / totalDays * 100;
        } else {
            return 0.0;
        }
    } else {
        return -1.0; // Invalid student name
    }
}
}
public class AttendanceManagement {
    private static JFrame loginFrame;
    private static JFrame mainMenuFrame;

    public static void main(String[] args) {
    AttendanceManagementSystem system = initializeSystem();

    // Initialize login frame
    initializeLoginFrame(system);
}

private static AttendanceManagementSystem initializeSystem() {
    AttendanceManagementSystem system = new AttendanceManagementSystem();
    system.registerUser("Sahana", "1234", true); // Register as student
    system.registerUser("Sanjana", "1234", true); // Register as student
    system.registerUser("Varshini", "1234", true); // Register as student
    system.registerUser("Vinutha", "1234", true); // Register as student
    system.registerUser("Preethi", "1234", true); // Register as student
    system.registerUser("Sinchana", "1234", true);
    system.registerUser("Tom", "1234", true);
    system.registerUser("Jerry", "1234", true);
    system.registerUser("Ram", "1234", true);
    system.registerUser("Sita", "1234", true);

    system.registerUser("John", "1234", false); // Register as teacher
    system.registerUser("Joseph", "1234", false); // Register as teacher
    system.registerUser("David", "1234", false); // Register as teacher
    system.registerUser("Krishna", "1234", false); // Register as teacher

    return system;
}

private static void initializeLoginFrame(AttendanceManagementSystem system) {
    // Create login GUI components
    loginFrame = new JFrame("Login - Student Attendance System");
    JPanel loginPanel = new JPanel();
    JLabel userLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField(10);
    JLabel passwordLabel = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField(10);
    JRadioButton studentRadioButton = new JRadioButton("Student");
    JRadioButton teacherRadioButton = new JRadioButton("Teacher");
    ButtonGroup userGroup = new ButtonGroup();
    userGroup.add(studentRadioButton);
    userGroup.add(teacherRadioButton);
    JButton loginButton = new JButton("Login");

    // Add components to the login panel
    loginPanel.setLayout(new GridLayout(5, 2));
    loginPanel.add(userLabel);
    loginPanel.add(usernameField);
    loginPanel.add(passwordLabel);
    loginPanel.add(passwordField);
    loginPanel.add(studentRadioButton);
    loginPanel.add(teacherRadioButton);
    loginPanel.add(loginButton);

    // Set up the login frame
    loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    loginFrame.setSize(400, 300); // Increased size
    loginFrame.setResizable(false); // Disable resizing
    loginFrame.getContentPane().add(BorderLayout.CENTER, loginPanel);
    loginFrame.setLocationRelativeTo(null); // Center on screen
    loginFrame.setVisible(true);

    // Add action listener to the login button
    loginButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);
            boolean isStudent = studentRadioButton.isSelected();
            if (system.login(username, password)) {
                if ((isStudent && system.isStudent(username)) || (!isStudent && !system.isStudent(username))) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    loginFrame.dispose(); // Close the login frame
                    showMainMenu(system, isStudent);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid user type. Please select the correct user type.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password. Try again.");
            }
            // Clear the password field
            passwordField.setText("");
        }
    });
}

private static void showMainMenu(AttendanceManagementSystem system, boolean isStudent) {
    // Create main menu GUI components
    mainMenuFrame = new JFrame("Main Menu - Student Attendance System");
    JPanel mainMenuPanel = new JPanel();
    JButton updateAttendanceButton = new JButton("Update Attendance");
    JButton markAbsentButton = new JButton("Mark Absent");
    JButton displayAttendanceStatusButton = new JButton("Display Attendance Status");
    JButton countDaysAttendedButton = new JButton("Count Days Attended");
    JButton viewAttendanceButton = new JButton("View Attendance for Date");
    JButton calculatePercentageButton = new JButton("Calculate Attendance Percentage");
    JButton logoutButton = new JButton("Logout");

    // Add components to the main menu panel
    mainMenuPanel.setLayout(new GridLayout(8, 1));
    mainMenuPanel.add(updateAttendanceButton);
    mainMenuPanel.add(markAbsentButton);
    mainMenuPanel.add(displayAttendanceStatusButton);
    mainMenuPanel.add(countDaysAttendedButton);
    mainMenuPanel.add(viewAttendanceButton);
    mainMenuPanel.add(calculatePercentageButton);
    mainMenuPanel.add(logoutButton);

    // Set up the main menu frame
    mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainMenuFrame.setSize(400, 450); // Increased size
    mainMenuFrame.setResizable(false); // Disable resizing
    mainMenuFrame.getContentPane().add(BorderLayout.CENTER, mainMenuPanel);
    mainMenuFrame.setLocationRelativeTo(null); // Center on screen
    mainMenuFrame.setVisible(true);

    // Add action listeners to the main menu buttons
    updateAttendanceButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> studentNames = system.getStudentNames();
            Object selectedName = JOptionPane.showInputDialog(null, "Select student name:", "Update Attendance",
                    JOptionPane.QUESTION_MESSAGE, null, studentNames.toArray(), studentNames.get(0));
            if (selectedName != null) {
                String studentName = selectedName.toString();
                system.updateAttendanceByDate(studentName);
            } else {
                JOptionPane.showMessageDialog(null, "No student selected.");
            }
        }
    });

    markAbsentButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> studentNames = system.getStudentNames();
            Object selectedName = JOptionPane.showInputDialog(null, "Select student name:", "Mark Absent",
                    JOptionPane.QUESTION_MESSAGE, null, studentNames.toArray(), studentNames.get(0));
            if (selectedName != null) {
                String studentName = selectedName.toString();
                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                system.markAbsent(studentName, currentDate);
            } else {
                JOptionPane.showMessageDialog(null, "No student selected.");
                }
            }
        });
    
    displayAttendanceStatusButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, system.getAttendanceStatus());
        }
    });

    countDaysAttendedButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> studentNames = system.getStudentNames();
            Object selectedName = JOptionPane.showInputDialog(null, "Select student name:", "Count Days Attended",
                    JOptionPane.QUESTION_MESSAGE, null, studentNames.toArray(), studentNames.get(0));
            if (selectedName != null) {
                String studentName = selectedName.toString();
                String input = JOptionPane.showInputDialog(null, "Enter month and year (MM/YYYY):", "Count Days Attended",
                        JOptionPane.QUESTION_MESSAGE);
                if (input != null) {
                    String[] parts = input.split("/");
                    if (parts.length == 2) {
                        try {
                            int month = Integer.parseInt(parts[0]);
                            int year = Integer.parseInt(parts[1]);
                            int daysAttended = system.getDaysAttendedInMonth(studentName, month, year);
                            JOptionPane.showMessageDialog(null, studentName + " attended " + daysAttended + " days in " + input);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid input. Please enter month and year in MM/YYYY format.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter month and year in MM/YYYY format.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No student selected.");
            }
        }
    });

    viewAttendanceButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> studentNames = system.getStudentNames();
            Object selectedName = JOptionPane.showInputDialog(null, "Select student name:", "View Attendance for Date",
                    JOptionPane.QUESTION_MESSAGE, null, studentNames.toArray(), studentNames.get(0));
            if (selectedName != null) {
                String studentName = selectedName.toString();
                String date = JOptionPane.showInputDialog(null, "Enter date (YYYY-MM-DD):", "View Attendance for Date",
                        JOptionPane.QUESTION_MESSAGE);
                if (date != null && !date.isEmpty()) {
                    String attendanceStatus = system.getAttendanceForDate(studentName, date);
                    JOptionPane.showMessageDialog(null, studentName + " was " + attendanceStatus + " on " + date);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid date.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No student selected.");
            }
        }
    });

    calculatePercentageButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> studentNames = system.getStudentNames();
            Object selectedName = JOptionPane.showInputDialog(null, "Select student name:", "Calculate Attendance Percentage",
                    JOptionPane.QUESTION_MESSAGE, null, studentNames.toArray(), studentNames.get(0));
            if (selectedName != null) {
                String studentName = selectedName.toString();
                double percentage = system.calculateAttendancePercentage(studentName);
                if (percentage >= 0) {
                    JOptionPane.showMessageDialog(null, studentName + "'s attendance percentage is " + percentage + "%");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid student name.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No student selected.");
            }
        }
    });

    logoutButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainMenuFrame.dispose(); // Close the main menu frame
            initializeLoginFrame(system); // Reinitialize login frame
        }
    });
}
}
