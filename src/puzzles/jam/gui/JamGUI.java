package puzzles.jam.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.jam.model.JamModel;

import java.io.File;
import java.nio.file.Paths;

public class JamGUI extends Application  implements Observer<JamModel, String>  {
    // for demonstration purposes
    private final static int BUTTON_FONT_SIZE = 20;
    private final static int ICON_SIZE = 75;

    private JamModel model;
    private GridPane buttons = new GridPane();
    private final Label message = new Label();
    private final BorderPane borderPane = new BorderPane();
    private Stage mStage;

    public void init() {
        String filename = getParameters().getRaw().getFirst();
        this.model = new JamModel(filename);
        this.model.addObserver(this);

    }

    @Override
    public void start(Stage stage) throws Exception {


        for(int i = 0; i < this.model.getNumRows(); i++) {
            for(int k = 0; k < this.model.getNumCols(); k++) {
                Button button1 = new Button();
                if(this.model.getCar(i,k) != null) {
                    button1.setStyle(
                            "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-background-color: " + this.model.getCar(i, k).getColor() + ";" +
                                    "-fx-font-weight: bold;");
                    button1.setText(this.model.getCar(i, k).getName());
                }
                button1.setMinSize(ICON_SIZE, ICON_SIZE);
                button1.setMaxSize(ICON_SIZE, ICON_SIZE);
                int temp1 = i;
                int temp2 = k;
                button1.setOnAction(e -> this.model.selectCar(temp1, temp2));
                buttons.add(button1, k, i);
            }
        }
        BorderPane bPane2 = new BorderPane();
        bPane2.setCenter(message);
        borderPane.setTop(bPane2);

        borderPane.setCenter(buttons);

        HBox bottomButtonsBox = new HBox();

        Button loadFile = new Button("Load");
        loadFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            String userDir = System.getProperty("user.dir");
            String subdirectoryPath = Paths.get(userDir, "data/jam").toString();
            fileChooser.setInitialDirectory(new File(subdirectoryPath));

            File file = fileChooser.showOpenDialog(stage);
            String fileText = file.getAbsolutePath();
            String[] dirs = fileText.split("\\\\");

            String fileName = dirs[dirs.length - 3] + "/" + dirs[dirs.length - 2] + "/" + dirs[dirs.length - 1];

            this.model.loadFile(fileName);
        });
        bottomButtonsBox.getChildren().add(loadFile);

        Button reset = new Button("Reset");
        reset.setOnAction(e -> this.model.loadDefault());
        bottomButtonsBox.getChildren().add(reset);

        Button hint = new Button("Hint");
        hint.setOnAction(e -> this.model.getHint());
        bottomButtonsBox.getChildren().add(hint);

        bottomButtonsBox.setAlignment(Pos.CENTER);
        borderPane.setBottom(bottomButtonsBox);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
        mStage = stage;
        String filename = getParameters().getRaw().getFirst();
        this.model.loadFile(filename);
    }

    @Override
    public void update(JamModel jamModel, String msg) {
        if(msg != null) {
            this.message.setText(msg);
        } else {
            this.message.setText("");
        }

        if(jamModel.getNumRows() == buttons.getRowCount() && jamModel.getNumCols() == buttons.getColumnCount()) {
            for (int i = 0; i < this.model.getNumRows(); i++) {
                for (int k = 0; k < this.model.getNumCols(); k++) {
                    Node node = buttons.getChildren().get(i * this.model.getNumCols() + k);
                    Button button1 = (Button) node;
                    if (this.model.getCar(i, k) != null) {
                        button1.setStyle(
                                "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                        "-fx-background-color: " + this.model.getCar(i, k).getColor() + ";" +
                                        "-fx-font-weight: bold;");
                        button1.setText(this.model.getCar(i, k).getName());
                    } else {
                        button1.setStyle("");
                        button1.setText("");
                    }
                    button1.setMinSize(ICON_SIZE, ICON_SIZE);
                    button1.setMaxSize(ICON_SIZE, ICON_SIZE);
                    int temp1 = i;
                    int temp2 = k;
                    button1.setOnAction(e -> this.model.selectCar(temp1, temp2));
                }
            }
        } else {
            buttons = new GridPane();

            for(int i = 0; i < this.model.getNumRows(); i++) {
                for(int k = 0; k < this.model.getNumCols(); k++) {
                    Button button1 = new Button();
                    if(this.model.getCar(i,k) != null) {
                        button1.setStyle(
                                "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                        "-fx-background-color: " + this.model.getCar(i, k).getColor() + ";" +
                                        "-fx-font-weight: bold;");
                        button1.setText(this.model.getCar(i, k).getName());
                    }
                    button1.setMinSize(ICON_SIZE, ICON_SIZE);
                    button1.setMaxSize(ICON_SIZE, ICON_SIZE);
                    int temp1 = i;
                    int temp2 = k;
                    button1.setOnAction(e -> this.model.selectCar(temp1, temp2));
                    buttons.add(button1, k, i);
                }
            }
            borderPane.setCenter(buttons);
            mStage.sizeToScene();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
