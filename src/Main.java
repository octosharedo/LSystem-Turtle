
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public static Group root = new Group();
    private static VBox controls;
    private static Scene mainScene;
    private static Canvas canvas; private static HBox canvasContainer;
    Font font = Font.font(19);  

    private static void parseLSystem(String axiom, int iterations)
    {
        //очищаем правила
        Actions.clearRules(); LSystem.clearRules();
        //транслейтим рулбоксы и ловим ошибку, если какой-то не заполнен
        //транслейт заполняет правила в лсистеме и действиях
        RuleBox.transferInfo();
        //в действиях бежим по строке, собираем список действий для добавления
        Actions.translate(axiom, iterations);
        //добавляем команды из списка в панель команд
    }
    private static void run()
    {
        //обновляем/ресайзим канвас
        initCanvas(mainScene.getWidth()-537,mainScene.getHeight());
        //транслейтим команды в действия и делаем
        CommandBox.run();
    }

    public static void putBoxesInControls(VBox boxes) 
    {
        //controls.getChildren().add(boxes);
        ScrollPane sp = new ScrollPane(); sp.setContent(boxes); sp.setStyle("-fx-background: white; -fx-border-color: white;");
        sp.setMaxHeight(310); sp.setPrefWidth(522);
        sp.setHbarPolicy(ScrollBarPolicy.NEVER); 
        sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED); 
         
        controls.getChildren().add(sp);

    }

    private void initMenu()
    {
        Font font = Font.font(19);
        Line border = new Line(537,0,537,900);
        //делаем границу менюшки всегда до конца экрана
        mainScene.heightProperty().addListener((obs, oldValue, newValue) -> border.setEndY(newValue.doubleValue()));
        
        TextField axiom = new TextField(); axiom.setPrefSize(380, 20); axiom.setPromptText("Starting state");
        Text textAxiom = new Text("axiom"); textAxiom.setFont(font);

        controls = new VBox(); controls.setLayoutX(10); controls.setLayoutY(10); controls.setSpacing(28);
        controls.getChildren().add(new VBox(8,textAxiom,axiom));
        RuleBox.init();
        RuleBox rb1 = new RuleBox();

        //#region панель парсинга ЛСистемы
        Text textIterations = new Text("Total iterations: "); textIterations.setFont(font);
        TextField fieldIterations = new TextField(); fieldIterations.setPrefWidth(40);
        fieldIterations.textProperty().addListener( (observable, oldValue, newValue) -> 
        {   //validating double
            if (newValue.matches("[1-9]\\d*") || newValue.length()==0) fieldIterations.setText(newValue); 
            else fieldIterations.setText(oldValue);
        });
        Button btnAddCommands = new Button("Add Commands"); btnAddCommands.setTooltip(new Tooltip("Computes axiom, rules and iterations into commands for the Turtle"));
        btnAddCommands.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parseLSystem(axiom.getText(),Integer.parseInt(fieldIterations.getText()));                
            }
        });
        HBox iterationAndAdd = new HBox(10,textIterations,fieldIterations,btnAddCommands);
        controls.getChildren().add(iterationAndAdd);
        //#endregion
        
        //панель команд для черепахи
        CommandBox.init();
        CommandBox cm1 = new CommandBox();

        //панель финальных кнопок
        Button btnRun = new Button("Run"), btnStart = new Button("Place Start"), btnClear = new Button("Clear"), btnAbout = new Button("?");
        btnRun.setFont(font); btnClear.setFont(font); btnStart.setFont(font); btnAbout.setFont(font); 
        HBox btns = new HBox(15, btnRun,btnClear); btns.setAlignment(Pos.BASELINE_RIGHT);
        btnClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //initCanvas(mainScene.getWidth()-537,mainScene.getHeight());
                CommandBox.clearCommands();
            }
        });
        btnRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                run();
            }
        });

        canvasContainer = new HBox(); canvasContainer.setLayoutX(538); //canvasContainer.setLayoutY(0);

        controls.getChildren().addAll(btns);
        root.getChildren().addAll(controls,border,canvasContainer);
    }
    private static void initCanvas(double width, double height)
    {
        canvasContainer.getChildren().remove(canvas);
        canvas = new Canvas(width, height);
        canvasContainer.getChildren().add(canvas);
        Drawer.setGraphicsContext(canvas.getGraphicsContext2D(), width/5, height/5);
    }

    @Override
    public void start(Stage stage) {
        
        //setting stage
        mainScene = new Scene(root, 1400, 900);
        stage.setMinHeight(947); stage.setMinWidth(1418);
        stage.setTitle("L-System Turtle");
        stage.setScene(mainScene);
        stage.show();
        
        initMenu();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
