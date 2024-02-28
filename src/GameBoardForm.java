
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;

public class GameBoardForm extends JFrame{
    public JPanel panel1;
    private JTextField nameTextField;
    private JRadioButton radioButton2;
    private JButton lastButton;
    private JButton nextButton;
    private JLabel nameLabel;
    private JLabel boughtLabel;
    private JLabel ratingLabel;
    private JTextArea fileTextArea;
    private JRadioButton radioButton1;
    private JCheckBox boughtCheckBox;
    private JRadioButton radioButton3;
    private JCheckBox autosaveCheckBox;
    private JButton saveButton;
    private JMenuBar mainMenu = new JMenuBar();
        private JMenu menuSoubor = new JMenu("File");
            private JMenuItem menuItemLoad = new JMenuItem("Load");
            private JMenuItem menuItemSave = new JMenuItem("Save");
        private JMenu menuAkce = new JMenu("Action");
            private JMenuItem menuItemAdd = new JMenuItem("Add");
            private JMenuItem menuItemRemove = new JMenuItem("Remove");
            private JMenuItem menuItemSort = new JMenuItem("Sort");
        private JMenu menuSouhrn = new JMenu("Extra");
            private JMenuItem menuItemStats = new JMenuItem("Stats");
    private JFileChooser fc = new JFileChooser(".");
    private boolean autoSave = false;
    private boolean changed = false;
    private int order = 0;
    private int extraGames = 0;
    private File currentFile = new File("src/Deskovky.txt");
    final private List<BoardGame> gameList = new ArrayList<>();

    public static void main(String[] args) {



    }

    public GameBoardForm(){


        setJMenuBar(mainMenu);
        InitMenu();
        setTitle("BoardEvidence");
        setSize(500,500);
        //setContentPane(frame.panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        fc.setFileFilter(new FileNameExtensionFilter("Textové soubory", "txt"));
        readFile(currentFile);

        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(radioButton1);
        btnGroup.add(radioButton2);
        btnGroup.add(radioButton3);
        prepareWindow();



        saveButton.addActionListener(e -> saveGame(gameList.get(order)));



        /*addButton.addActionListener(e -> {

            addGame("New("+extraGames+")", false, 0);
            extraGames++;
        });*/
        //deleteButton.addActionListener(e -> delete());

        nextButton.addActionListener(e -> next());
        lastButton.addActionListener(e -> last());

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
    public void prepareWindow() {
        if (!gameList.isEmpty()){
            lastButton.setEnabled(false);
            displayGame(gameList.get(order));
        } else {
            JOptionPane.showMessageDialog(this, "Unable to display list, empty", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void InitMenu(){
        mainMenu.add(menuSoubor);
        mainMenu.add(menuAkce);
        mainMenu.add(menuSouhrn);

        menuSoubor.add(menuItemLoad);
        menuSoubor.add(menuItemSave);

        menuAkce.add(menuItemAdd);
        menuAkce.add(menuItemRemove);
        menuAkce.add(menuItemSort);

        menuSouhrn.add(menuItemStats);
        setJMenuBar(mainMenu);


        menuItemSave.addActionListener(e -> /*saveGame(gameList.get(order))*/ SaveFile());
        menuItemLoad.addActionListener(e -> LoadFile());

        menuItemRemove.addActionListener(e -> delete());
        menuItemAdd.addActionListener(e -> {

            addGame("New(" + extraGames + ")", false, 0);
            extraGames++;
        });
        menuItemSort.addActionListener(e -> Sort());

        menuItemStats.addActionListener(e -> ShowStats());
    }
    public void ShowStats(){
        JOptionPane.showMessageDialog(this, "Total Number of Games:" + gameList.size() + "\n" + "Favorite games: " + findFavorite() + " \n" + "Number of bought games: " + findBought() , "Stats", JOptionPane.INFORMATION_MESSAGE);
    }
    private void SaveFile(){
        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            System.out.println("Uživatel vybral soubor: "+selectedFile.getPath());
        } else {
            System.out.println("Uživatel ukončil dialog bez výběru souboru.");
            System.out.println("Například zvolil Storno/Cancel.");
        }
    }
    private void LoadFile(){
        fileTextArea.setText("src/Deskovky.txt");
        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            System.out.println(String.valueOf(fc.getSelectedFile()));
            showFile(fc.getSelectedFile());
        }

    }
    public void showFile (File file) {
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(file)))) {
            StringBuilder text = new StringBuilder();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                text.append(line).append("\n");

            }
            fileTextArea.setText(String.valueOf(text));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Could not find save file" + e.getLocalizedMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("nebyl nalezen soubor " + e.getLocalizedMessage());


        }
    }
    public StringBuilder findFavorite(){
        StringBuilder list = new StringBuilder();
        for (BoardGame game: gameList) {
            if (game.getRating() == 3) {
                list.append(game.getName()).append(";");
            }
        }
        System.out.println(list);
        return list;

    }
    public int findBought(){
        int boughtNum = 0;
        for (BoardGame game: gameList) {
            if (game.isBought()) {
                boughtNum++;
            }
        }
        return boughtNum;
    }
    public void Sort(){
        gameList.sort(new SortByName());
        displayGame(gameList.get(order));
    }
    public void readFile(File file){
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader("src/Deskovky")))){
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
