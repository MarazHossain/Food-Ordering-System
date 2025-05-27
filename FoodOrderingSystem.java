import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FoodOrderingSystem implements ActionListener
{
    private MenuItem[] menuItems;
    private JTextArea cartArea;
    private JButton billBtn, clearBtn;
    private JTextField nameField;

    public FoodOrderingSystem()
    {
        JFrame frame = new JFrame("Food Ordering System");
        frame.setSize(850, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Menu items
        menuItems = new MenuItem[]
        {
            new MenuItem("Burger", 120, new String[]{"Patty","Tomato", "Lettuce", "Cheese"}, "C:/Users/hredo/OneDrive/Desktop/Food Ordering System Project/Photos/burger.jpg"),
            new MenuItem("Pizza", 400, new String[]{"Cheese", "Sauce", "Capsicum", "Olives"}, "C:/Users/hredo/OneDrive/Desktop/Food Ordering System Project/Photos/pizza.jpg"),
            new MenuItem("Sandwich", 150, new String[]{"Lettuce", "Chicken","Cucumber", "Mayonese"}, "C:/Users/hredo/OneDrive/Desktop/Food Ordering System Project/Photos/sandwich.jpg"),
            new MenuItem("Cold Coffee", 100, new String[]{"Milk", "Ice","Sugar", "Coffee"}, "C:/Users/hredo/OneDrive/Desktop/Food Ordering System Project/Photos/coffee.jpg")
        };

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 2, 10, 10));
        for (MenuItem item : menuItems)
        {
            menuPanel.add(item.getPanel());
        }

        JScrollPane menuScroll = new JScrollPane(menuPanel);
        frame.add(menuScroll, BorderLayout.CENTER);

        // Cart Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 0));

        // Customer name field
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(new JLabel("Customer Name:"), BorderLayout.WEST);
        nameField = new JTextField();
        namePanel.add(nameField, BorderLayout.CENTER);
        rightPanel.add(namePanel, BorderLayout.NORTH);

        // Cart display
        cartArea = new JTextArea();
        cartArea.setEditable(false);
        rightPanel.add(new JScrollPane(cartArea), BorderLayout.CENTER);

        // Buttons
        billBtn = new JButton("Generate Bill");
        clearBtn = new JButton("Clear Cart");
        billBtn.addActionListener(this);
        clearBtn.addActionListener(this);

        JPanel btnPanel = new JPanel();
        btnPanel.add(billBtn);
        btnPanel.add(clearBtn);
        rightPanel.add(btnPanel, BorderLayout.SOUTH);

        frame.add(rightPanel, BorderLayout.EAST);
        frame.setVisible(true);

        // Live cart update
        Timer timer = new Timer(300, e -> updateCart());
        timer.start();
    }

    private void updateCart()
    {
        StringBuilder sb = new StringBuilder("Your Cart:\n\n");
        for (MenuItem item : menuItems)
        {
            sb.append(item.getCartText());
        }
        cartArea.setText(sb.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == billBtn)
        {
            String customerName = nameField.getText().trim();
            if (customerName.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Please enter the customer's name.", "Missing Name", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int total = 0;
            StringBuilder bill = new StringBuilder();
            bill.append("Customer Name: ").append(customerName).append("\n");
            bill.append("Date & Time: ")
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("\n\n");
            bill.append("Items:\n");

            for (MenuItem item : menuItems)
            {
                bill.append(item.getBillText());
                total += item.getTotal();
            }

            if (total == 0)
            {
                JOptionPane.showMessageDialog(null, "Your cart is empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            bill.append("\nTotal: ").append(total).append(" Tk\n");

            // Show in dialog
            JOptionPane.showMessageDialog(null, bill.toString(), "Bill", JOptionPane.INFORMATION_MESSAGE);

            // Save to txt file
            try
            {
                String billFolder = "Bills";
                File dir = new File(billFolder);
                if (!dir.exists()) dir.mkdir();

                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String fileName = billFolder + "/" + customerName.replaceAll("\\s+", "_") + "_" + timestamp + ".txt";

                Files.write(Paths.get(fileName), bill.toString().getBytes());
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, "Error saving bill file.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        if (e.getSource() == clearBtn)
        {
            for (MenuItem item : menuItems)
            {
                item.reset();
            }
            nameField.setText("");
            updateCart();
        }
    }
}
