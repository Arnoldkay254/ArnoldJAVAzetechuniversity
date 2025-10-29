import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PetSelector extends JFrame implements ActionListener {
    private JRadioButton birdBtn, catBtn, dogBtn, rabbitBtn, pigBtn;
    private JLabel imageLabel;
    private ButtonGroup group;

    public PetSelector() {
        setTitle("Pet Selector");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for radio buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1));

        birdBtn = new JRadioButton("Bird");
        catBtn = new JRadioButton("Cat");
        dogBtn = new JRadioButton("Dog");
        rabbitBtn = new JRadioButton("Rabbit");
        pigBtn = new JRadioButton("Pig");

        group = new ButtonGroup();
        group.add(birdBtn);
        group.add(catBtn);
        group.add(dogBtn);
        group.add(rabbitBtn);
        group.add(pigBtn);

        buttonPanel.add(birdBtn);
        buttonPanel.add(catBtn);
        buttonPanel.add(dogBtn);
        buttonPanel.add(rabbitBtn);
        buttonPanel.add(pigBtn);

        add(buttonPanel, BorderLayout.WEST);

        // Image label
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(imageLabel, BorderLayout.CENTER);

        // Add action listeners
        birdBtn.addActionListener(this);
        catBtn.addActionListener(this);
        dogBtn.addActionListener(this);
        rabbitBtn.addActionListener(this);
        pigBtn.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String pet = "";
        String imagePath = "";

        if (e.getSource() == birdBtn) {
            pet = "Bird";
            imagePath = "images/bird.png";
        } else if (e.getSource() == catBtn) {
            pet = "Cat";
            imagePath = "images/cat.png";
        } else if (e.getSource() == dogBtn) {
            pet = "Dog";
            imagePath = "images/dog.png";
        } else if (e.getSource() == rabbitBtn) {
            pet = "Rabbit";
            imagePath = "images/rabbit.png";
        } else if (e.getSource() == pigBtn) {
            pet = "Pig";
            imagePath = "images/pig.png";
        }

        imageLabel.setIcon(new ImageIcon(imagePath));
        JOptionPane.showMessageDialog(this, "MADE BY ARNOLD :) !! \n You selected: " + pet);
    }

    public static void main(String[] args) {
        new PetSelector();
    }
}


