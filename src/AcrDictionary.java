package Ics201Project;

import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.util.*;


public class AcrDictionary extends Application{
    private Button[] b = new Button[6]; //first panel buttons
    private String[] keyboardnames = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "- .", "& /", "Shift", "Spc", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "0"};
    private Button[] keyboard = new Button[40];
    private Button checkText = new Button("Text Check ");
    private Button clearText = new Button("Clear Text");
    private TextField acrText = new TextField();
    private TextArea textAreaType = new TextArea();
    private static String fileName="acronym.txt";
    private static ArrayList<String> acrFile = new ArrayList<>(1000);

    public void start(Stage primarystage) throws FileNotFoundException {


        primarystage.setTitle("Acronym Dictionary");

        b[0] = new Button("Definition");
        b[1] = new Button("Add Word");
        b[2] = new Button("Delete Word");
        b[3] = new Button("File");
        b[4] = new Button("help");
        b[5] = new Button("clear");


        textAreaType.setPrefSize(300, 300);
        textAreaType.setWrapText(true);
        textAreaType.setId("areaField");
        clearText.setFont(new Font("Times New Roman", 22));
        checkText.setFont(new Font("Times New Roman", 22));
        acrText.setOnKeyTyped(keyEvent -> keyEvent.consume());         //DISABLING KEYBOARD
        acrText.setId("textField");

        //Initialising the keyboard, fixing the size and font
        for (int i = 0; i < 40; ++i) {
            keyboard[i] = new Button(keyboardnames[i]);
            keyboard[i].setMinSize(70, 40);
            keyboard[i].setMaxSize(70, 40);
            keyboard[i].setFont(new Font("Times New Roman", 20));
        }

        //Initialising the "to do" buttons(First panel, vbox), fixing the size and font
        for (int i = 0; i < 6; ++i) {
            b[i].setMinWidth(150);
            b[i].setFont(new Font("Times New Roman", 20));
        }

        //ADDING KEYBOARD TO DICTIONARY PANEL
        GridPane alphabets = new GridPane();
        int colindex = 0, rowindex = 0;
        for (int i = 0; i < 40; ++i) {
            alphabets.add(keyboard[i], colindex, rowindex);
            colindex++;
            if (colindex == 5) {
                colindex = 0;
                rowindex++;
            }
        }

        //BUTTON ACTIONS
        b[0].setOnAction(e ->  definition());         //DEFINITION
        b[1].setOnAction(e ->  addWord() );           //ADD WORD
        b[2].setOnAction(e ->  deleteWord());         //DELETE WORD
        b[3].setOnAction(e ->  {
                try {
                    file();
                } catch (FileNotFoundException f) {
                    f.printStackTrace();
                }
    });                //FILE
        b[4].setOnAction(e ->  JOptionPane.showMessageDialog(null,"This is an Acronym Dictionary \n " +
                "The functions of the acrnoym dictionary are as follows\n " +
                "1. Type the acronym through the virtual keyboard provided and if you press definition button the definition will show on the text field \n" +
                "If you click add word. you will be able to add the acrnoym and its definition to the file \n" +
                "If the delete word is pressed you just need to type the acronym and the word if it is in the dictionary will be delete it \n" +
                "By pressing file you will be able to choose another dictionary file \n" +
                "In the text area provided in the third panel you can write you essay with the acronyms and if you want their full forms, just click the check text button \n" +
                "The clear button will clear all strings and numbers from the text field while the clear text button will erase all characters from the text area")); //HELP
        b[5].setOnAction(e ->  acrText.setText(""));  //CLEAR TEXT FIELD

        //Keyboard action
        final boolean[] shiftCheck = {false};
        for(int i = 0; i < 40; ++i){
            int finalI = i;
            keyboard[i].setOnAction(e ->{
                    if(keyboard[finalI].getText().equals("Spc"))    //space
                        acrText.setText(acrText.getText()+" ");
                    else if(keyboard[finalI].getText().equals("Shift")){    //shift
                        acrText.setText(acrText.getText().toLowerCase());
                        shiftCheck[0] =!shiftCheck[0];
                    }
                    else if(keyboard[finalI].getText().equals("- .")){  // - .
                       if(shiftCheck[0])
                           acrText.setText(acrText.getText()+".");
                       else
                           acrText.setText(acrText.getText()+"-");
                    }
                    else if(keyboard[finalI].getText().equals("& /"))   //& /
                        if(shiftCheck[0])
                            acrText.setText(acrText.getText()+"/");
                        else
                            acrText.setText(acrText.getText()+"&");
                    else
                    acrText.setText(acrText.getText()+keyboard[finalI].getText());
                }
            );
        }
        //Third panel code TEXT AREA
        //TextArea: code to expand the acronyms
        clearText.setOnAction(e -> textAreaType.setText(""));
        checkText.setOnAction(e -> textAreaCheck());

        //Adding components to boxes
        //BUTTONS PANEL
        VBox buttonsbox = new VBox(b[0], b[1], b[2], b[3], b[4], b[5]);
        buttonsbox.setSpacing(25);
        VBox secondBox = new VBox(acrText, alphabets);
        alphabets.setHgap(5);
        alphabets.setVgap(5);

        //ADDING Buttons below text AREA
        HBox thirdBoxButtons = new HBox(checkText, clearText);
        thirdBoxButtons.setSpacing(40);

        //PARAGRAPH/TEXT AREA PANEL
        VBox thirdBox = new VBox(textAreaType, thirdBoxButtons);

        //SCENE box
        GridPane box = new GridPane();
        box.add(buttonsbox, 1, 1);
        box.add(secondBox, 2, 1);
        box.add(thirdBox, 3, 1);
        box.setHgap(20);
        box.setVgap(15);

        Scene scene = new Scene(box, 905, 410);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primarystage.setScene(scene);
        primarystage.show();

    }

    public void definition() throws NoSuchElementException, StringIndexOutOfBoundsException{
        boolean check = false;
        int i=0,equalcount=0;
        String definition="",acronym="";
        int splitindex=0;

        while(i < acrFile.size()){

            splitindex = acrFile.get(i).indexOf("—")-1;
            acronym = acrFile.get(i).substring(0,splitindex);

            if(acrText.getText().trim().equalsIgnoreCase(acronym.trim())){
                    definition += ", "+acrFile.get(i).substring(acrFile.get(i).indexOf("—")+1);
                check = true;
                equalcount++;
            }
            else
                check=false;
            if(!check && equalcount>0)
               break;
              ++i;
       }
        acrText.setText(acrText.getText()+definition);
        if(!check && equalcount==0)
            JOptionPane.showMessageDialog(null,"Acronym not found");

    }

    public void addWord() {
        String acr = JOptionPane.showInputDialog(null, "Enter acronym");
        String definition = JOptionPane.showInputDialog(null, "Enter the definition");

       if(acr==null || definition ==null || acr.equals("") || definition.equals(""))
           JOptionPane.showMessageDialog(null,"No, input given");
       else {
           String acrandDefinition= acr+" — "+definition;
           acrFile.add(acrandDefinition);
           JOptionPane.showMessageDialog(null, "The acronym has been added to the dictionary");
       }
    }

    public void deleteWord() {

        String delete_word = JOptionPane.showInputDialog("Enter the acronym to delete from the dictionary.");
        boolean check =false;
        int i=0;

        if(delete_word==null||delete_word.equals(""))
            JOptionPane.showMessageDialog(null,"No input");

        else{

            while(i < acrFile.size()){
            if(acrFile.get(i).substring(0,delete_word.length()).equalsIgnoreCase(delete_word)){
                check = true;
                acrFile.remove(i);
                JOptionPane.showMessageDialog(null,"deletion successful");
                break;
            }
            System.out.println(i);
            ++i;
        }
            if(!check)
                JOptionPane.showMessageDialog(null,"The acronym is not present in the dictionary");
        }
    }

    public void file() throws FileNotFoundException {
        saveFile();
        JOptionPane.showMessageDialog(null, fileName+" SAVED. Please choose another dictionary file");
        Stage fileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(fileStage);
         fileName = file.getPath();

     fileScanner();
    }

    public void textAreaCheck() throws NoSuchElementException{
        Scanner read = new Scanner(textAreaType.getText());
        String sentence ="";
        while(read.hasNext()){
            int i=0;
            String acronym="";
            String word = read.next();

            if(word.charAt(word.length()-1)==','|| word.charAt(word.length()-1)=='.')
                word =word.substring(0,word.length()-1);

            while(i < acrFile.size()) {
                acronym = acrFile.get(i).substring(0, (acrFile.get(i).indexOf("—") - 1));

                if (word.equalsIgnoreCase(acronym.trim()))
                    word = acrFile.get(i).substring(acrFile.get(i).indexOf("—") + 1) + " ";

                ++i;
            }
                sentence += word+" ";
        }
        textAreaType.setText(sentence);
    }

    public static void fileScanner()throws FileNotFoundException{
        acrFile.clear();
        Scanner fileScan = new Scanner(new FileInputStream(fileName));
        if(!fileScan.hasNext()) {
            JOptionPane.showMessageDialog(null, "File empty");
            System.exit(0);
        }

        while (fileScan.hasNextLine())
            acrFile.add(fileScan.nextLine());

        if(acrFile.get(0).equals(""))
            acrFile.remove(0);

        fileScan.close();
    }

    public static void saveFile() throws FileNotFoundException{
        PrintWriter pwriter = new PrintWriter(new FileOutputStream(fileName));
        Collections.sort(acrFile, new sortIgnoreCase());

        for(int index =0; index < acrFile.size()-1; ++index)
            pwriter.println(acrFile.get(index));

        pwriter.print(acrFile.get(acrFile.size()-1));
       pwriter.close();
    }

    public static class sortIgnoreCase implements Comparator<Object> {
        public int compare(Object o1, Object o2) {
            String s1 = (String) o1;
            String s2 = (String) o2;
            return s1.toLowerCase().compareTo(s2.toLowerCase());
        }
    }

    public static void main(String[] args) throws IOException {
        fileScanner(); //Scanning the file and storing it in acrFile
        Application.launch();
        saveFile();
    }
}