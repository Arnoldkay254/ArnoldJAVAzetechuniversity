import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrationApp {
    private JFrame mainFrame;
    private JPanel formPanel, displayPanel;
    private JTextField txtName, txtMobile, txtDay, txtYear, txtID, txtSearchName;
    private JTextArea txtAddress;
    private JRadioButton maleRadio, femaleRadio;
    private JComboBox<String> monthCombo;
    private JCheckBox termsCheck;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private Connection connection;

    public RegistrationApp() {
        initializeDatabase();
        createGUI();
    }

    private void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/registration_db", 
                "root", 
                "password"
            );
            
            // Create table if not exists
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "mobile VARCHAR(15), " +
                "gender VARCHAR(10), " +
                "dob DATE, " +
                "address TEXT, " +
                "reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSQL);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        }
    }

    private void createGUI() {
        mainFrame = new JFrame("Registration System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new GridLayout(1, 2));
        
        createFormPanel();
        createDisplayPanel();
        
        mainFrame.pack();
        mainFrame.setSize(1000, 600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void createFormPanel() {
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Registration Form"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(20);
        formPanel.add(txtName, gbc);
        
        // Mobile
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Mobile:"), gbc);
        gbc.gridx = 1;
        txtMobile = new JTextField(20);
        formPanel.add(txtMobile, gbc);
        
        // Gender
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        formPanel.add(genderPanel, gbc);
        
        // DOB
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("DOB:"), gbc);
        gbc.gridx = 1;
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtDay = new JTextField(2);
        monthCombo = new JComboBox<>(new String[]{"Jan","Feb","Mar","Apr","May","Jun",
                                                 "Jul","Aug","Sep","Oct","Nov","Dec"});
        txtYear = new JTextField(4);
        dobPanel.add(txtDay);
        dobPanel.add(monthCombo);
        dobPanel.add(txtYear);
        formPanel.add(dobPanel, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        txtAddress = new JTextArea(3, 20);
        JScrollPane addressScroll = new JScrollPane(txtAddress);
        formPanel.add(addressScroll, gbc);
        
        // Terms Checkbox
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        termsCheck = new JCheckBox("Accept Terms And Conditions");
        formPanel.add(termsCheck, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton submitBtn = new JButton("Submit");
        JButton resetBtn = new JButton("Reset");
        
        submitBtn.addActionListener(e -> submitForm());
        resetBtn.addActionListener(e -> resetForm());
        
        buttonPanel.add(submitBtn);
        buttonPanel.add(resetBtn);
        formPanel.add(buttonPanel, gbc);
        
        mainFrame.add(formPanel);
    }

    private void createDisplayPanel() {
        displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createTitledBorder("Registered Users"));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Search Name:"));
        txtSearchName = new JTextField(15);
        searchPanel.add(txtSearchName);
        
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");
        JButton exitBtn = new JButton("Exit");
        
        searchBtn.addActionListener(e -> searchUsers());
        refreshBtn.addActionListener(e -> loadUsers());
        exitBtn.addActionListener(e -> System.exit(0));
        
        searchPanel.add(searchBtn);
        searchPanel.add(refreshBtn);
        searchPanel.add(exitBtn);
        
        // Table
        String[] columns = {"ID", "Name", "Mobile", "Gender", "DOB", "Address", "Reg Date"};
        tableModel = new DefaultTableModel(columns, 0);
        dataTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(dataTable);
        
        displayPanel.add(searchPanel, BorderLayout.NORTH);
        displayPanel.add(tableScroll, BorderLayout.CENTER);
        
        mainFrame.add(displayPanel);
        loadUsers();
    }

    private void submitForm() {
        if (!termsCheck.isSelected()) {
            JOptionPane.showMessageDialog(mainFrame, "Please accept terms and conditions!");
            return;
        }
        
        try {
            String name = txtName.getText().trim();
            String mobile = txtMobile.getText().trim();
            String gender = maleRadio.isSelected() ? "Male" : 
                           femaleRadio.isSelected() ? "Female" : "";
            String dob = txtYear.getText() + "-" + 
                        String.format("%02d", (monthCombo.getSelectedIndex() + 1)) + "-" + 
                        txtDay.getText();
            String address = txtAddress.getText().trim();
            
            if (name.isEmpty() || mobile.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please fill all required fields!");
                return;
            }
            
            String sql = "INSERT INTO users (name, mobile, gender, dob, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, mobile);
            pstmt.setString(3, gender);
            pstmt.setString(4, dob);
            pstmt.setString(5, address);
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(mainFrame, "Registration Successful!");
            resetForm();
            loadUsers();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Error: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtName.setText("");
        txtMobile.setText("");
        txtDay.setText("");
        txtYear.setText("");
        txtAddress.setText("");
        maleRadio.setSelected(false);
        femaleRadio.setSelected(false);
        termsCheck.setSelected(false);
        monthCombo.setSelectedIndex(0);
    }

    private void loadUsers() {
        try {
            tableModel.setRowCount(0);
            String sql = "SELECT * FROM users ORDER BY reg_date DESC";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            while (rs.next()) {
                Date dob = rs.getDate("dob");
                String dobStr = (dob != null) ? dateFormat.format(dob) : "N/A";
                
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("mobile"),
                    rs.getString("gender"),
                    dobStr,
                    rs.getString("address"),
                    rs.getTimestamp("reg_date")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Error loading data: " + e.getMessage());
        }
    }

    private void searchUsers() {
        try {
            String searchName = txtSearchName.getText().trim();
            tableModel.setRowCount(0);
            
            String sql = "SELECT * FROM users WHERE name LIKE ? ORDER BY reg_date DESC";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "%" + searchName + "%");
            ResultSet rs = pstmt.executeQuery();
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            while (rs.next()) {
                Date dob = rs.getDate("dob");
                String dobStr = (dob != null) ? dateFormat.format(dob) : "N/A";
                
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("mobile"),
                    rs.getString("gender"),
                    dobStr,
                    rs.getString("address"),
                    rs.getTimestamp("reg_date")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Error searching: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationApp());
    }
}
