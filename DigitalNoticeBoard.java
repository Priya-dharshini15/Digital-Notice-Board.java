import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class DigitalNoticeBoard extends JFrame {
    private JTextField noticeInput;
    private JButton postButton, updateButton, deleteButton;
    private JList<String> noticeList;
    private DefaultListModel<String> listModel;
    private ArrayList<Color> noticeColors; // Store colors for each notice
    private ArrayList<String> notices;

    public DigitalNoticeBoard() {
        // Initialize components
        notices = new ArrayList<>();
        listModel = new DefaultListModel<>();
        noticeColors = new ArrayList<>();

        setTitle("Digital Notice Board");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Input field
        noticeInput = new JTextField();
        noticeInput.setToolTipText("Enter your notice here");
        add(noticeInput, BorderLayout.NORTH);

        // Notice list
        noticeList = new JList<>(listModel);
        noticeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        noticeList.setToolTipText("Select a notice to update or delete");

        // Custom renderer for colored notices
        noticeList.setCellRenderer(new ListCellRenderer<String>() {
            @Override
            public Component getListCellRendererComponent(
                    JList<? extends String> list, 
                    String value, 
                    int index, 
                    boolean isSelected, 
                    boolean cellHasFocus) {
                JLabel label = new JLabel(value);
                label.setOpaque(true);

                // Set background and text color
                if (isSelected) {
                    label.setBackground(Color.LIGHT_GRAY);
                } else {
                    label.setBackground(Color.WHITE);
                }
                label.setForeground(noticeColors.get(index)); // Use assigned color
                label.setFont(new Font("Arial", Font.BOLD, 14));
                return label;
            }
        });

        add(new JScrollPane(noticeList), BorderLayout.CENTER);

        // Buttons
        postButton = createButton("Post Notice", "Add a new notice");
        updateButton = createButton("Update Notice", "Update the selected notice");
        deleteButton = createButton("Delete Notice", "Delete the selected notice");

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        buttonPanel.add(postButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        setupButtonActions();

        // List selection listener
        noticeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !noticeList.isSelectionEmpty()) {
                noticeInput.setText(noticeList.getSelectedValue());
            }
        });
    }

    private JButton createButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        return button;
    }

    private void setupButtonActions() {
        postButton.addActionListener(e -> postNotice());
        updateButton.addActionListener(e -> updateNotice());
        deleteButton.addActionListener(e -> deleteNotice());
    }

    private void postNotice() {
        String notice = noticeInput.getText().trim();
        if (!notice.isEmpty()) {
            notices.add(notice);
            listModel.addElement(notice);
            noticeColors.add(generateRandomColor()); // Assign random color
            noticeInput.setText("");
            showMessage("Notice posted successfully.");
        } else {
            showMessage("Notice cannot be empty!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateNotice() {
        int selectedIndex = noticeList.getSelectedIndex();
        if (selectedIndex != -1) {
            String updatedNotice = noticeInput.getText().trim();
            if (!updatedNotice.isEmpty()) {
                notices.set(selectedIndex, updatedNotice);
                listModel.set(selectedIndex, updatedNotice);
                noticeInput.setText("");
                showMessage("Notice updated successfully.");
            } else {
                showMessage("Updated notice cannot be empty!", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            showMessage("Please select a notice to update!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteNotice() {
        int selectedIndex = noticeList.getSelectedIndex();
        if (selectedIndex != -1) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this notice?",
                    "Delete Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                notices.remove(selectedIndex);
                listModel.remove(selectedIndex);
                noticeColors.remove(selectedIndex); // Remove the associated color
                noticeInput.setText("");
                showMessage("Notice deleted successfully.");
            }
        } else {
            showMessage("Please select a notice to delete!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private Color generateRandomColor() {
        Random rand = new Random();
        return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    private void showMessage(String message) {
        showMessage(message, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, "Notice Board", messageType);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DigitalNoticeBoard board = new DigitalNoticeBoard();
            board.setVisible(true);
        });
    }
}
