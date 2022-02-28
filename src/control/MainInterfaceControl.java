package control;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MainInterfaceControl {
    public AnchorPane uiJavaCompiler;
    public TextArea txtInput;
    public TextArea txtOutput;
    public MenuButton mnuBtnFile;
    public MenuButton mnuBtnEdit;
    public MenuButton mnuBtnExtra;
    public MenuButton mnuBtnAbout;
    public Label lblModeSelect;
    public Button btnCopy;
    public Button btnPaste;
    public Button btnSelectAll;
    public Button btnSave;
    public Label lblTotCharacters;
    public Button btnDelete;
    public JFXButton btnExit;
    public JFXButton btnReset;
    public JFXButton btnRun;
    public JFXToggleButton tglAdvancedMode;
    public TextField txtMainClass;
    public Button btnOpen;
    public Button btnCut;
    public JFXButton btnAddClass;


    public void initialize(){

        MenuItem save = new MenuItem("Save");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        MenuItem cut = new MenuItem("Cut");
        MenuItem anew = new MenuItem("New");
        MenuItem about = new MenuItem("About");
        MenuItem exit = new MenuItem("Exit");
        MenuItem select_all = new MenuItem("Select All");
        MenuItem open = new MenuItem("Open");
        MenuItem wrapText = new MenuItem("Wrap Text");

        mnuBtnFile.getItems().add(open);
        mnuBtnFile.getItems().add(anew);

        mnuBtnEdit.getItems().add(save);
        mnuBtnEdit.getItems().add(copy);
        mnuBtnEdit.getItems().add(cut);
        mnuBtnEdit.getItems().add(paste);
        mnuBtnEdit.getItems().add(select_all);

        mnuBtnAbout.getItems().add(about);

        mnuBtnExtra.getItems().add(exit);
        mnuBtnExtra.getItems().add(wrapText);

        save.setOnAction(this::btnSave_OnAction);
        copy.setOnAction(this::btnCopy_OnAction);
        cut.setOnAction(this::btnCut_OnAction);
        paste.setOnAction(this::btnPaste_OnAction);
        select_all.setOnAction(this::btnSelectAll_OnAction);
        open.setOnAction(this::btnOpen_OnAction);
        anew.setOnAction(this::btnReset_OnAction);
        about.setOnAction(event -> {

            try {
                URL root = getClass().getResource("../view/About_interface.fxml");
                Parent load = FXMLLoader.load(root);
                Scene scene = new Scene(load);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("About");
                stage.centerOnScreen();
                stage.setResizable(false);
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        wrapText.setOnAction(event -> {
            txtInput.setWrapText(true);
        });

        exit.setOnAction(this::btnExit_OnAction);



        //todo: new,exit,

        txtInput.textProperty().addListener((observable, oldValue, newValue) -> {
            characterCount();
        });

    }

    private void characterCount() {
        String characters = txtInput.getText();
        int count = 0;
        for (int i = 0; i < characters.length(); i++) {
            if (characters.charAt(i) != ' ') {
                count++;
            }

        }
        lblTotCharacters.setText(String.valueOf(count));

    }

    public void tglAdvancedMode_OnAction(ActionEvent actionEvent) {
        if(tglAdvancedMode.isSelected()){

            lblModeSelect.setText("Advanced Mode Activated");

        }else {
            lblModeSelect.setText("Easy Mode Activated");

        }
    }

    public void btnAddClass_OnAction(ActionEvent actionEvent) {
        if(!lblModeSelect.getText().equals("Easy Mode Activated")){
            String className = "public class "+txtMainClass.getText()+"{\n" +
                       "\n"+
                    "}";
            txtInput.setText(className);
        }
    }


    public void btnRun_OnAction(ActionEvent actionEvent) throws IOException {

        String main =txtMainClass.getText();

        if(lblModeSelect.getText().equals("Easy Mode Activated")){

            String command = "public class " + main+ "{\n" +
                        "public static void main(String args[]){\n"+
                        txtInput.getText()+
                        "\n}\n" +
                        "}";
            javaRun(command,main);

        }else if (lblModeSelect.getText().equals("Advanced Mode Activated")){

            String code = txtInput.getText();
            javaRun(code,main);

        }
    }

    private void javaRun(String command,String main) throws IOException {
        try {

            //get Temp dir

            String tempDir = System.getProperty("java.io.tmpdir");
            Path filePath = Paths.get(tempDir, main+".java");
            Files.write(filePath, command.getBytes());

            //file path to compiler
            Process javac = Runtime.getRuntime().exec("javac " + filePath);
            int exitCode = javac.waitFor();

            if (exitCode == 0){
                //compile success and to be run

                Process javaRun = Runtime.getRuntime().exec("java -cp " + tempDir +" "+main);
                exitCode = javaRun.waitFor();

                if (exitCode == 0) {
                    //successfully run
                    readStream(javaRun.getInputStream());

                }else {
                    //runtime error
                    readStream(javaRun.getErrorStream());
                }

            }else {
                readStream(javac.getErrorStream());
                //compile error
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        }finally {
            Path classPath = Paths.get(System.getProperty("java.io.tmpdir"), main+".class");
            Path javaPath = Paths.get(System.getProperty("java.io.tmpdir"), main+".java");

            Files.deleteIfExists(classPath);
            Files.deleteIfExists(javaPath);

        }
    }


    private void readStream(InputStream is) throws IOException {
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        txtOutput.setText(new String(buffer));
        is.close();

    }

    public void btnReset_OnAction(ActionEvent actionEvent) {
        txtMainClass.clear();
        txtInput.clear();
        txtOutput.clear();

    }

    public void btnCopy_OnAction(ActionEvent actionEvent) {
        txtInput.copy();
    }

    public void btnCut_OnAction(ActionEvent actionEvent) {
        txtInput.cut();
    }

    public void btnPaste_OnAction(ActionEvent actionEvent) {
        txtInput.paste();
    }

    public void btnSelectAll_OnAction(ActionEvent actionEvent) {
        txtInput.selectAll();
    }

    public void btnOpen_OnAction(ActionEvent actionEvent) {
        try {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open the preferred file");
        File file = fileChooser.showOpenDialog(null);
        byte[] buffer = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        txtInput.setText(new String(buffer));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
       try {
           FileChooser fileChooser = new FileChooser();
           fileChooser.setTitle("Save the file");
           fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.text","Text Files"));
           File file = fileChooser.showSaveDialog(null);

           Path path = Paths.get(file.getAbsolutePath());
           byte[] buffer = txtInput.getText().getBytes();

           OutputStream os = Files.newOutputStream(path);
           os.write(buffer);
           if(Files.exists(path)){
               new Alert(Alert.AlertType.INFORMATION,"File Saved Successfully").show();
           }else {
               new Alert(Alert.AlertType.ERROR,"File Save failed.Try Again").show();
           }
           os.close();


       } catch (IOException e) {
           throw new RuntimeException(e);

       }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        //ToDo: develop this as delete only selected
        txtInput.clear();
    }

    public void btnExit_OnAction(ActionEvent actionEvent) {
        System.exit(0);
        //ToDo: develop as to function
       new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to Exit,", ButtonType.OK, ButtonType.CANCEL).showAndWait();


    }

}
