import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

//TODO: make checkers for when turtle will go step-by-step

public class CommandBox {

    private int actionId = -1; private double actionValue; 
    private HBox commandBox; private static VBox commandsBlock; private static ArrayList<CommandBox> commandsList = new ArrayList<>();
    private static Font font = Font.font(19);
    private TextField fieldValue; private ComboBox<String> fieldAction;
    private static ArrayList<Action> commandsToDo = new ArrayList<>();
    //#region список доступных действий
    private final ObservableList<String> actions = FXCollections.observableArrayList(
        "Forward",              //0
        "Bacwards",             //1
        "Turn Right (degrees)", //2
        "Turn Left (degrees)",  //3
        "Lift Pen",             //4
        "Lower Pen",            //5
        "Remember Postion",     //6
        "Recall Postion"        //7
    );
    //#endregion
    
    /** инициализация панели для панелек команд */
    public static void init()
    {
        Text textCommands = new Text("Commands:"); textCommands.setFont(Font.font(22)); 
        Text textAction = new Text("             Action"); textAction.setFont(font);
        Text textValue = new Text("Value"); textValue.setFont(font);
        HBox title = new HBox(83, textAction, textValue);
        commandsBlock = new VBox(8,textCommands,title);
        Main.putBoxesInControls(commandsBlock);
    }
    /** добавляет панельку команды следом за этой */
    public void addNext()
    {
        CommandBox tmp = new CommandBox();
        commandsBlock.getChildren().remove(tmp.commandBox);
        commandsBlock.getChildren().add(commandsBlock.getChildren().indexOf(this.commandBox)+1,tmp.commandBox);        
    }
    /** удаляет одну (эту) панельку команды */
    private void remove()
    {
        commandsBlock.getChildren().remove(commandBox);
        commandsList.remove(this);
    }
    
    /** читает все панельки команд и собирает из них список действий */
    public static void run()
    {
        commandsToDo.clear();
        for (CommandBox box : commandsList) {
            try {commandsToDo.add(box.translate());} 
            catch (IncorrectInputException e) 
            {
                System.out.println("В командах где-то "+e.getMessage()); 
                commandsToDo.clear();
                break;
            }
        }
        for (Action action : commandsToDo) {action.act();}
    }
    /** читает панельку команды и возвращает Action */
    public Action translate() throws IncorrectInputException
    {
        if (this.actionId == -1) throw new IncorrectInputException("отсутствует значение действия");
        return new Action(actionId, actionValue);
    }

    /** добавляет в список команд на интерфейсе данный список команд */
    public static void append(ArrayList<Action> appending) {for (Action action : appending) {CommandBox tmp = new CommandBox(action);}}
    /** очистить весь список панелек команд */
    public static void clearCommands() 
    {
        for (CommandBox box : commandsList) {commandsBlock.getChildren().remove(box.commandBox);} 
        commandsList.clear(); 
        commandsList.add(new CommandBox());
    }

    /** генерирует одну панельку команды */
    public CommandBox()
    {
        //поле для значения. прячется, если выбрано действие, где не нужно значение
        fieldValue = new TextField(); fieldValue.setPrefSize(62, 20); fieldValue.setPromptText("0");
        fieldValue.textProperty().addListener( (observable, oldValue, newValue) -> 
        {   //validating double, setting value
            if ( newValue.matches("[+-]?\\d+\\.?(\\d+)?") || newValue.length()==0) fieldValue.setText(newValue); 
            else fieldValue.setText(oldValue);

            actionValue = (newValue.length()==0 || newValue.equals("-")) ? 0 : Double.parseDouble(newValue);
        });

        // drop-down list
        fieldAction = new ComboBox<>(actions); fieldAction.setVisibleRowCount(4); fieldAction.setPrefWidth(189);
        fieldAction.valueProperty().addListener((obs, oldValue, newValue) -> { //hiding or showing value field, setting id
            this.actionId = fieldAction.getSelectionModel().getSelectedIndex();
            if (fieldAction.getSelectionModel().getSelectedIndex() < 4) fieldValue.setVisible(true);
            else fieldValue.setVisible(false);
        });

        // add button
        Button btnAdd = new Button("+"); int r = 16; btnAdd.setShape(new Circle(r));
        btnAdd.setStyle("-fx-background-radius: 5em; " + "-fx-min-width:  32px; " + "-fx-min-height: 32px; " + "-fx-max-width:  32px; " + "-fx-max-height: 32px; " + "-fx-background-color: -fx-body-color;" + "-fx-background-insets: 0px; " + "-fx-padding: 0px;"); 
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addNext();
            }
        });
        // remove button
        Button btnRemove = new Button("-"); btnRemove.setShape(new Circle(r)); 
        btnRemove.setStyle("-fx-background-radius: 5em; " + "-fx-min-width:  32px; " + "-fx-min-height: 32px; " + "-fx-max-width:  32px; " + "-fx-max-height: 32px; " + "-fx-background-color: -fx-body-color;" + "-fx-background-insets: 0px; " + "-fx-padding: 0px;"); 
        btnRemove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (commandsList.size()>1) remove();
            }
        });

        commandBox = new HBox(fieldAction, fieldValue, btnAdd, btnRemove); 
        commandBox.setSpacing(10);
        commandsBlock.getChildren().add(commandBox); 
        commandsList.add(this);

    }
    /** генерирует панельку команды и заполняет ее информацией */
    public CommandBox(Action action)
    {
        this();
        this.actionId = action.getId(); 
        this.actionValue = action.getValue();
        fieldValue.setText(actionValue+""); fieldAction.getSelectionModel().select(actionId);
    }

}