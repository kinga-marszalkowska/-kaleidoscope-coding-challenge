package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

//static import
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.sql.BatchUpdateException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Math.*;

public class Main extends Application {

    static final int WIDTH = 800;
    static final int HEIGHT = 800;
    static Canvas canvas;
    static Group group;
    static StackPane stackPane;

    @Override
    public void start(Stage primaryStage) throws Exception{
        canvas = new Canvas(WIDTH, HEIGHT);
        group = new Group(canvas);

        ColorPicker colorPicker = new ColorPicker(Color.WHITE);
        colorPicker.setLayoutX(50);
        colorPicker.setLayoutY(50);

        Slider petals = new Slider(6,12,6);
        petals.setShowTickLabels(true);
        petals.setShowTickMarks(true);
        petals.setLayoutX(200);
        petals.setLayoutY(50);

        Spinner<Integer> brushSize = new Spinner<>(1, 10, 2, 1);
        brushSize.setLayoutX(400);
        brushSize.setLayoutY(50);

        ColorPicker bgColor = new ColorPicker(Color.BLACK);
        bgColor.setLayoutX(600);
        bgColor.setLayoutY(50);

        Button takeScreenshotButton = new Button("screenshot");
        takeScreenshotButton.setOnMouseClicked(event -> {
            File screenshoFile = new File("screenshot" + LocalDateTime.now().toString().replaceAll(":","-") + ".png");
            WritableImage image = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
            canvas.snapshot(null, image);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
            try {
                ImageIO.write(renderedImage, "png", screenshoFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        group.getChildren().addAll(takeScreenshotButton, colorPicker, petals, brushSize, bgColor);

        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(bgColor.getValue());

        scene.setOnMouseDragged(event -> {
            drawPoint(event.getSceneX(), event.getSceneY(), colorPicker.getValue(), petals.getValue(), brushSize.getValue());
        });

        Canvas background = new Canvas(WIDTH, HEIGHT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.clearRect(0, 0, WIDTH, HEIGHT);
            }
//            else if(event.getCode() == KeyCode.SPACE){
//                GraphicsContext gc = background.getGraphicsContext2D();
//                gc.setFill(bgColor.getValue());
//            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Kalejdoskop");
        primaryStage.show();
    }

    private void drawPoint(double x, double y, Color color, double petals, Integer brushSize){
        double angle = 2 * PI/petals;
        double xx = x - WIDTH/2;
        double yy = y - HEIGHT/2;

        for (int i = 0; i < petals; i++) {
            double xxx = xx * cos(i * angle) - yy * sin(i *angle) + WIDTH/2;
            double yyy = xx * sin(i * angle) + yy * cos(i * angle) + HEIGHT/2;

//            Line line = new Line(xxx,yyy,xxx,yyy);
//            line.setFill(color);
//            line.setStroke(color);
//            line.setStrokeWidth(brushSize);
//            group.getChildren().add(line);

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.fillOval(xxx, yyy, brushSize, brushSize);
            gc.setFill(color);

        }


    }


    public static void main(String[] args) {
        launch(args);
    }
}
