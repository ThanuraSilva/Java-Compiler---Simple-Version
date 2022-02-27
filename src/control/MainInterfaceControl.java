package control;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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
    public Button btnCut_OnAction;
    public Button btnPaste;
    public Button btnSelectAll;
    public Button btnSave;
    public Label lblTotCharacters;
    public Button btnDelete;
    public JFXButton btnExit;
    public JFXButton btnReset;
    public JFXButton btnRun;
    public JFXToggleButton tglAdvancedMode;

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

        mnuBtnFile.getItems().add(open);
        mnuBtnFile.getItems().add(anew);

        mnuBtnEdit.getItems().add(save);
        mnuBtnEdit.getItems().add(copy);
        mnuBtnEdit.getItems().add(cut);
        mnuBtnEdit.getItems().add(paste);
        mnuBtnEdit.getItems().add(select_all);

        mnuBtnAbout.getItems().add(about);

        mnuBtnExtra.getItems().add(exit);

    }

    public void tglAdvancedMode_OnAction(ActionEvent actionEvent) {
        if(tglAdvancedMode.isSelected()){
            lblModeSelect.setText("Advanced Mode Activated");
        }else {
            lblModeSelect.setText("Easy Mode Activated");
        }
    }

    public void btnRun_OnAction(ActionEvent actionEvent) {
        if(lblModeSelect.getText().equals("Easy Mode Activated")){
            //todo: reduce boiler plates
            try {
                String command = "public class Demo{\n" +
                        "public static void main(String args[]){\n"+
                        txtInput.getText()+
                        "\n}\n" +
                        "}";
                //get Temp dir
                String tempDir = System.getProperty("java.io.tmpdir");
                Path filePath = Paths.get(tempDir, "Demo");
                Files.write(filePath,command.getBytes());

                //file path to compiler
                Process javac = Runtime.getRuntime().exec("javac " + filePath);
                int exitCode = javac.waitFor();

                if (exitCode == 0){
                    //compile success
                }else {
                    //compile error
                }







            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }
    public void btnReset_OnAction(ActionEvent actionEvent) {

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

    public void btnSave_OnAction(ActionEvent actionEvent) {

    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        //ToDo: develop this as delete only selected
        txtInput.clear();
    }

    public void btnExit_OnAction(ActionEvent actionEvent) {
        //ToDo: develop as to function
       new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to Exit,", ButtonType.OK, ButtonType.CANCEL).showAndWait();


    }

    public void mnuBtnFile_OnAction(ActionEvent actionEvent) {
    }

    public void mnuBtnEdit_OnAction(ActionEvent actionEvent) {
    }

    public void mnuBtnExtra_OnAction(ActionEvent actionEvent) {
    }

    public void mnuBtnAbout_OnAction(ActionEvent actionEvent) {
    }




}
