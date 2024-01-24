import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private ButtonGroup btnGroup;
    private int order = 0;
    final private List<BoardGame> gameList = new ArrayList();
    public static void main(String[] args) {
        GameBoardForm frame = new GameBoardForm();
        frame.setTitle("BoardEvidence");

        frame.setSize(500,500);
        frame.setContentPane(frame.panel1);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public GameBoardForm(){
        reader();
        btnGroup = new ButtonGroup();
        btnGroup.add(radioButton1);
        btnGroup.add(radioButton2);
        btnGroup.add(radioButton3);
        displayGame(gameList.get(order));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame(gameList.get(order));
            }
        });
        lastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(order >= 1){
                    order--;
                    displayGame(gameList.get(order));
                }
            }
        });
        NextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (order <= gameList.size()){
                    order++;
                    displayGame(gameList.get(order));
                }
            }
        });

    }
    public void reader(){
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader("soubor")))){
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
    public void writer(String line, int order){
        try(PrintWriter wr = new PrintWriter(new BufferedWriter(new FileWriter("soubor")))) {
            wr.println(line);
        }
        catch (FileNotFoundException e){
            System.err.println("nebyl nalezen soubor " + e.getLocalizedMessage());
        }
        catch (IOException e){
            System.err.println("IOE problem" + e.getLocalizedMessage());
        }
    }
    public void displayGame(BoardGame game){
        nameTextField.setText(game.getName());
        if (game.isBought()){
            boughtCheckBox.setSelected(true);
        }
        switch (game.getRating()){
            case 1:
                radioButton1.setSelected(true);
                break;
            case 2:
                radioButton2.setSelected(true);
                break;
            case 3:
                radioButton3.setSelected(true);
        }
    }
    public void saveGame(BoardGame game){
        //Saving Name
        game.setName(nameTextField.getName());

        //Saving Bought
        if (boughtCheckBox.isSelected()){
            game.setBought(true);
        } else {
            game.setBought(false);
        }

        //Saving Rating
        if (radioButton1.isSelected()){
            game.setRating(1);
        } else if (radioButton2.isSelected()) {
            game.setRating(2);
        } else if (radioButton3.isSelected()) {
            game.setRating(3);
        }
    }


}
