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
    private JButton NextButton;
    private JLabel nameLabel;
    private JLabel boughtLabel;
    private JLabel ratingLabel;
    private JRadioButton radioButton1;
    private JCheckBox boughtCheckBox;
    private JRadioButton radioButton3;
    private JButton addButton;
    private JButton deleteButton;
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
            displayGame(gameList.get(order));
        } else {
            JOptionPane.showMessageDialog(this, "Unable to display list, empty", "Error", JOptionPane.INFORMATION_MESSAGE);
        }



        saveButton.addActionListener(e -> saveGame(gameList.get(order)));

        lastButton.addActionListener(e -> last());

        addButton.addActionListener(e -> {

            addGame("New("+extraGames+")", false, 0);
            extraGames++;
        });

        NextButton.addActionListener(e -> next());

        deleteButton.addActionListener(e -> delete());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("window closed");
                writer();
            }
        });

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
            System.err.println("nebyl nalezen soubor " + e.getLocalizedMessage());
        }
    }
    public void writer(){
        try(PrintWriter wr = new PrintWriter(new BufferedWriter(new FileWriter("Deskovky")))) {
            //fileCleaner("Deskovky");
            for (BoardGame game : gameList){
                wr.print(game.getName()+";"+game.isBought()+";"+game.getRating());
                wr.println("");
            }
        }
        catch (FileNotFoundException e){
            System.err.println("nebyl nalezen Deskovky " + e.getLocalizedMessage());
        }
        catch (IOException e){
            System.err.println("IOE problem" + e.getLocalizedMessage());
        }
    }


    public void addGame(String name, boolean bought, int rating){
        if (!gameList.isEmpty()){
            gameList.add(new BoardGame(name, bought , rating));
        } else {
            gameList.add(new BoardGame(name, bought , rating));
            order=0;
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
            order--;
            displayGame(gameList.get(order));
        }
    }
    public void next(){
        if (order+1 < gameList.size() & !gameList.isEmpty()){
            order++;
            //System.out.println("Current item: " + (order + 1) + "  " + "Item count: "+gameList.size());
            displayGame(gameList.get(order));
        }
        /*else if (gameList.isEmpty()) {
            displayGame(gameList.get(0));
        }*/
    }



}
