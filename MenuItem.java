import javax.swing.*;
import java.awt.*;

public class MenuItem
{
    private String name;
    private int price;
    private int quantity = 0;
    private String[] ingredients;
    private String customizedIngredients = "";

    private JPanel panel;
    private JLabel quantityLabel;

    public MenuItem(String name, int price, String[] ingredients, String imagePath)
    {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;

        panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(new Color(255, 235, 205));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        // Product image
        ImageIcon icon = new ImageIcon(imagePath);
        Image pic = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(pic));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        panel.add(imageLabel, gbc);

        // Customize button
        JButton customizeBtn = new JButton("Customize");
        customizeBtn.addActionListener(e -> customizeIngredients());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        panel.add(customizeBtn, gbc);

        // Quantity controls
        JButton minus = new JButton("-");
        JButton plus = new JButton("+");
        quantityLabel = new JLabel(" " + quantity + " ");

        minus.addActionListener(e ->
        {
            if (quantity > 0)
            {
                quantity--;
                updateQuantityLabel();
            }
        });

        plus.addActionListener(e ->
        {
            quantity++;
            updateQuantityLabel();
        });

        JPanel qtyPanel = new JPanel();
        qtyPanel.setBackground(new Color(255, 235, 205));
        qtyPanel.add(minus);
        qtyPanel.add(quantityLabel);
        qtyPanel.add(plus);

        gbc.gridy = 1;
        panel.add(qtyPanel, gbc);

        // Price and name
        gbc.gridy = 2;
        panel.add(new JLabel("Price: " + price + " Tk"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(new JLabel(name), gbc);
    }

    private void updateQuantityLabel()
    {
        quantityLabel.setText(" " + quantity + " ");
    }

    private void customizeIngredients()
    {
        JCheckBox[] checkBoxes = new JCheckBox[ingredients.length];
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select ingredients:"));

        for (int i = 0; i < ingredients.length; i++)
        {
            checkBoxes[i] = new JCheckBox(ingredients[i], true);
            panel.add(checkBoxes[i]);
        }

        int result = JOptionPane.showConfirmDialog(
            null, panel, "Customize " + name,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION)
        {
            StringBuilder custom = new StringBuilder();
            for (JCheckBox box : checkBoxes)
            {
                if (box.isSelected())
                {
                    custom.append(box.getText()).append(", ");
                }
            }
            if (custom.length() > 2)
            {
                custom.setLength(custom.length() - 2);
            }
            customizedIngredients = custom.toString();
        }
    }

    public JPanel getPanel()
    {
        return panel;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void reset()
    {
        quantity = 0;
        customizedIngredients = "";
        updateQuantityLabel();
    }

    public String getBillText()
    {
        if (quantity == 0) 
			return "";
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" x ").append(quantity)
          .append(" = ").append(quantity * price).append(" Tk\n");
        if (!customizedIngredients.isEmpty())
        {
            sb.append("  Ingredients: ").append(customizedIngredients).append("\n");
        }
        return sb.toString();
    }

    public String getCartText()
    {
        if (quantity == 0) 
			return "";
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" x ").append(quantity).append("\n");
        if (!customizedIngredients.isEmpty())
        {
            sb.append("  Ingredients: ").append(customizedIngredients).append("\n");
        }
        return sb.toString();
    }

    public int getTotal()
    {
        return quantity * price;
    }
}
