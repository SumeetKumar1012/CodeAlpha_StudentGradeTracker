import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class StudentGradeTracker extends JFrame {
    private JTextField idField, nameField, gradeField, searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private java.util.List<Student> students = new ArrayList<>();

    public StudentGradeTracker() {
        setTitle("Student Grade Tracking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);

        // Main container with spacing
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Main Heading
        container.add(createHeading("Student Grade Tracking System", true));
        container.add(Box.createVerticalStrut(15)); // space before Student Details

        // Student Details
        container.add(createHeading("Student Details", false));
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        idField = new JTextField(8);
        nameField = new JTextField(12);
        gradeField = new JTextField(5);

        detailsPanel.add(new JLabel("ID:"));
        detailsPanel.add(idField);
        detailsPanel.add(new JLabel("Name:"));
        detailsPanel.add(nameField);
        detailsPanel.add(new JLabel("Grade:"));
        detailsPanel.add(gradeField);
        container.add(detailsPanel);

        // Add button centered
        JButton addButton = new JButton("Add Student");
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addPanel.add(addButton);
        container.add(addPanel);

        container.add(Box.createVerticalStrut(15)); // space before Student Records

        // Student Records
        container.add(createHeadingWithTopLine("Student Records"));
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Grade"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        container.add(scrollPane);

        JPanel editDeletePanel = new JPanel();
        JButton editButton = new JButton("Edit Student");
        JButton deleteButton = new JButton("Delete Student");
        editDeletePanel.add(editButton);
        editDeletePanel.add(deleteButton);
        container.add(editDeletePanel);

        container.add(Box.createVerticalStrut(15)); // space before Controls

        // Controls
        container.add(createHeadingWithLines("Controls"));
        JPanel controlsPanel = new JPanel(new BorderLayout(10, 5));
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        searchField = new JTextField(15);
        leftPanel.add(new JLabel("Search:"));
        leftPanel.add(searchField);

        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        JButton summaryButton = new JButton("Summary");
        rightPanel.add(saveButton);
        rightPanel.add(loadButton);
        rightPanel.add(summaryButton);

        controlsPanel.add(leftPanel, BorderLayout.WEST);
        controlsPanel.add(rightPanel, BorderLayout.EAST);
        container.add(controlsPanel);

        container.add(Box.createVerticalStrut(15)); // space before Summary Report

        // Summary Report
        container.add(createHeadingWithLines("Summary Report"));
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JLabel averageLabel = new JLabel("Average: ");
        JLabel highestLabel = new JLabel("Highest: ");
        JLabel lowestLabel = new JLabel("Lowest: ");
        summaryPanel.add(averageLabel);
        summaryPanel.add(highestLabel);
        summaryPanel.add(lowestLabel);
        container.add(summaryPanel);

        add(container);

        // Event Listeners
        addButton.addActionListener(e -> addStudent());
        editButton.addActionListener(e -> editStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        saveButton.addActionListener(e -> saveStudents());
        loadButton.addActionListener(e -> loadStudents());
        summaryButton.addActionListener(e -> updateSummary(averageLabel, highestLabel, lowestLabel));
        searchField.addCaretListener(e -> searchStudents());
    }

    private JPanel createHeading(String text, boolean withTopLine) {
        JPanel panel = new JPanel(new BorderLayout());
        if (withTopLine) panel.add(new JSeparator(), BorderLayout.NORTH);

        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(new Color(0, 70, 140));
        panel.add(label, BorderLayout.CENTER);

        panel.add(new JSeparator(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createHeadingWithTopLine(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JSeparator(), BorderLayout.NORTH);

        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(0, 70, 140));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHeadingWithLines(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JSeparator(), BorderLayout.NORTH);

        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(0, 70, 140));
        panel.add(label, BorderLayout.CENTER);

        panel.add(new JSeparator(), BorderLayout.SOUTH);
        return panel;
    }

    private void addStudent() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String gradeText = gradeField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || gradeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields.");
            return;
        }

        try {
            int grade = Integer.parseInt(gradeText);
            Student student = new Student(id, name, grade);
            students.add(student);
            tableModel.addRow(new Object[]{id, name, grade});
            idField.setText("");
            nameField.setText("");
            gradeField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Grade must be a number.");
        }
    }

    private void editStudent() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String id = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        int grade = (int) tableModel.getValueAt(row, 2);

        idField.setText(id);
        nameField.setText(name);
        gradeField.setText(String.valueOf(grade));

        students.removeIf(s -> s.getId().equals(id));
        tableModel.removeRow(row);
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String id = (String) tableModel.getValueAt(row, 0);
        students.removeIf(s -> s.getId().equals(id));
        tableModel.removeRow(row);
    }

    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            oos.writeObject(students);
            JOptionPane.showMessageDialog(this, "Students saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving students.");
        }
    }

    private void loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"))) {
            students = (ArrayList<Student>) ois.readObject();
            tableModel.setRowCount(0);
            for (Student s : students) {
                tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getGrade()});
            }
            JOptionPane.showMessageDialog(this, "Students loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading students.");
        }
    }

    private void updateSummary(JLabel avg, JLabel high, JLabel low) {
        if (students.isEmpty()) return;

        double total = 0;
        int highest = Integer.MIN_VALUE;
        int lowest = Integer.MAX_VALUE;

        for (Student s : students) {
            int g = s.getGrade();
            total += g;
            highest = Math.max(highest, g);
            lowest = Math.min(lowest, g);
        }

        double average = total / students.size();
        avg.setText("Average: " + String.format("%.2f", average));
        high.setText("Highest: " + highest);
        low.setText("Lowest: " + lowest);
    }

    private void searchStudents() {
        String query = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        for (Student s : students) {
            if (s.getId().toLowerCase().contains(query) || s.getName().toLowerCase().contains(query)) {
                tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getGrade()});
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentGradeTracker().setVisible(true));
    }
}

class Student implements Serializable {
    private String id;
    private String name;
    private int grade;

    public Student(String id, String name, int grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getGrade() { return grade; }
}
