import javax.swing.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameBoardForm extends JFrame{
    private JPanel panel1;
    private JButton saveButton;
    private JTextField nameTextField;
    private JRadioButton radioButton2;
    private JButton lastButton;
    private JButton nextButton;
    private JLabel nameLabel;
    private JLabel boughtLabel;
    private JLabel ratingLabel;
    private JRadioButton radioButton1;
    private JCheckBox boughtCheckBox;
    private JRadioButton radioButton3;
    private JButton addButton;
    private JButton deleteButton;
    private JCheckBox autosaveCheckBox;
    private boolean error = false;
    private boolean autoSave = false;
    private boolean changed = false;
    private int order = 0;
    private int extraGames = 0;
    final private List<BoardGame> gameList = new ArrayList<>();
    public static void main(String[] args) {

        GameBoardForm frame = new GameBoardForm();
        frame.setTitle("BoardEvidence");
        frame.setSize(500,500);
        frame.setContentPane(frame.panel1);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public GameBoardForm(){
        readFile();
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(radioButton1);
        btnGroup.add(radioButton2);
        btnGroup.add(radioButton3);
        if (!gameList.isEmpty()){
            lastButton.setEnabled(false);
            displayGame(gameList.get(order));
        } else {
            JOptionPane.showMessageDialog(this, "Unable to display list, empty", "Error", JOptionPane.INFORMATION_MESSAGE);
        }



        saveButton.addActionListener(e -> saveGame(gameList.get(order)));



        addButton.addActionListener(e -> {

            addGame("New("+extraGames+")", false, 0);
            extraGames++;
        });

        nextButton.addActionListener(e -> next());
        lastButton.addActionListener(e -> last());

        deleteButton.addActionListener(e -> delete());
        boughtCheckBox.addActionListener(e -> changed = true);
        nameTextField.addActionListener(e -> changed = true);
        radioButton1.addActionListener(e -> changed = true);
        radioButton2.addActionListener(e -> changed = true);
        radioButton3.addActionListener(e -> changed = true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("window closed");
                write();
            }
        });
        autosaveCheckBox.addActionListener(e -> autoSave());


    }
    public void readFile(){
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader("Deskovky")))){
            while (sc.hasNext()){
                String line = sc.nextLine();
                String[] bloky = line.split(";");
                String name = bloky[0];
                boolean bought = bloky[1].equals("yes");
                int rating = Integer.parseInt(bloky[2]);
                gameList.add(new BoardGame(name, bought, rating));
            }
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Could not find save file"+  e.getLocalizedMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("nebyl nalezen soubor " + e.getLocalizedMessage());
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Wrong value in save file, check error message" +  e.getLocalizedMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("Nesprávný formát Integeru " + e.getLocalizedMessage());

        }
    }
    public void write(){
        try(PrintWriter wr = new PrintWriter(new BufferedWriter(new FileWriter("Deskovky")))) {
            for (BoardGame game : gameList){
                wr.print(game.getName()+";"+game.isBought()+";"+game.getRating());
                wr.println("");
            }
        }
        catch (FileNotFoundException e){
            System.err.println("Nebyl nalezen soubor " + e.getLocalizedMessage());
        }
        catch (IOException e){
            System.err.println("IOE problem" + e.getLocalizedMessage());
        }
    }


    public void addGame(String name, boolean bought, int rating){
        if (!gameList.isEmpty()){
            gameList.add(new BoardGame(name, bought , rating));
            checkAvailability();
        } else {
            gameList.add(new BoardGame(name, bought , rating));
            order=0;
            next();
        }
    }
    public void displayGame(BoardGame game){
        nameTextField.setText(game.getName());
        boughtCheckBox.setSelected(game.isBought());
        switch (game.getRating()) {
            case 1 -> radioButton1.setSelected(true);
            case 2 -> radioButton2.setSelected(true);
            case 3 -> radioButton3.setSelected(true);

        }
        autosaveCheckBox.setSelected(autoSave);
        changed = false;
        checkAvailability();
    }
    public void saveGame(BoardGame game){
        //Saving Name
        game.setName(nameTextField.getText());

        //Saving Bought
        game.setBought(boughtCheckBox.isSelected());

        //Saving Rating
        if (radioButton1.isSelected()){
            game.setRating(1);
        } else if (radioButton2.isSelected()) {
            game.setRating(2);
        } else if (radioButton3.isSelected()) {
            game.setRating(3);
        }
        JOptionPane.showMessageDialog(this, "Saved changes to memory.", "Message saved", JOptionPane.INFORMATION_MESSAGE);
    }
    public void delete() {
        if (!gameList.isEmpty()) gameList.remove(order);
        if (order <gameList.size()){

            //System.out.println("Current item: " +String.valueOf(order+1) + "  " + "Item count: "+gameList.size());
            displayGame(gameList.get(order));
            order++;
        } else {
            last();

        }

    }
    public void last(){
        if(order >= 1){
            //nextButton.setEnabled(true);
            if (autoSave & changed) saveGame(gameList.get(order));
            order--;
            displayGame(gameList.get(order));

        }
        //else lastButton.setEnabled(false);
    }
    public void next(){
        if (order+1 < gameList.size() & !gameList.isEmpty()){
            //lastButton.setEnabled(true);
            if (autoSave & changed) saveGame(gameList.get(order));
            order++;
            displayGame(gameList.get(order));



            //System.out.println("Current item: " + (order + 1) + "  " + "Item count: "+gameList.size());


        }
        //else nextButton.setEnabled(false);

    }
    public void autoSave(){
        autoSave = autosaveCheckBox.isSelected();

    }
    public void canGoLeft(){
        if (order == 0) lastButton.setEnabled(false);
        else lastButton.setEnabled(true);
    }
    public void canGoRight() {
        if (order == gameList.size()-1) nextButton.setEnabled(false);
        else nextButton.setEnabled(true);
    }
    public void checkAvailability(){
        canGoRight();
        canGoLeft();
    }



}
