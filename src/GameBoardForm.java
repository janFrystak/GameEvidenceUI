import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private ButtonGroup btnGroup;
    private int order = 0;
    private int extraGames = 0;
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
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maker("New("+extraGames+")", false, 1);
                extraGames++;
            }
        });
        NextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (order+1 < gameList.size()){
                    order++;
                    System.out.println("Current item: " +String.valueOf(order+1) + "  " + "Item count: "+gameList.size());
                    displayGame(gameList.get(order));
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameList.remove(order);
                if (order <= gameList.size()){

                }
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("window closed");
                writer();
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
    public void writer(){
        try(PrintWriter wr = new PrintWriter(new BufferedWriter(new FileWriter("soubor")))) {
            fileCleaner("soubor");
            for (BoardGame game: gameList){
                wr.print(game.getName()+";"+game.isBought()+";"+game.getRating());
                wr.println("");
            }
        }
        catch (FileNotFoundException e){
            System.err.println("nebyl nalezen soubor " + e.getLocalizedMessage());
        }
        catch (IOException e){
            System.err.println("IOE problem" + e.getLocalizedMessage());
        }
    }
    public void fileCleaner(String soubor){
        try {
            File file = new File(soubor);
            FileWriter fWriter = new FileWriter(file);
            fWriter.write("");
            fWriter.close();
        }catch (IOException e){
            System.err.println("fileCleaner problem" + e.getLocalizedMessage());
        }
    }
    public void maker(String name, boolean bought, int rating){
        gameList.add(new BoardGame(name, bought , rating));
    }
    public void displayGame(BoardGame game){
        nameTextField.setText(game.getName());
        if (game.isBought()){
            boughtCheckBox.setSelected(true);
        }
        else {
            boughtCheckBox.setSelected(false);
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
        game.setName(nameTextField.getText());

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
    public void next() {

    }
    public void last() {

    }


}
