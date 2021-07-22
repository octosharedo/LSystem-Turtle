import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

//TODO: Exception handling for when nothing is chosen

/**
 * A HBox interface to add a new rule.
 * Consists of a textField for one letter, TF for a rule, a drop-down list for action and an (in)visible TF for an argument (if appropriate action is chosen), buttons for adding another ruleBox or removing this one
 */
public class RuleBox {
    //#region variables
    private static ArrayList<RuleBox> ruleBoxesList = new ArrayList<>();
    private static VBox ruleBoxesBlock = new VBox();

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
    private static Font font = Font.font(19);
    private HBox ruleBox;
    private Character key = null; private String rule=""; private int actionId=-1; private double actionValue;
    //#endregion

    /** Adds all rules for LSystem and Acrions classes */
    public static void transferInfo()
    {
        for (RuleBox box : ruleBoxesList) {
            try {box.translate();} 
            catch (IncorrectInputException e) {
                System.out.println("В одном из полей "+e.getMessage());
            }
        }
    }
    /** Validates fields. Adds a rule for LSystem and Acrions classes */
    private void translate() throws IncorrectInputException
    {
        if (key == null) throw new IncorrectInputException("отсутствует значение ключа.");
        if (this.actionId == -1) throw new IncorrectInputException("отсутствует значение действия");
        LSystem.addRule(this.key, this.rule);
        Actions.addRule(this.key, new Action(this.actionId,this.actionValue));
    }

    /** VBox for ruleBoxes joins root */
    public static void init() 
    { 
        ruleBoxesBlock.setSpacing(8);
        Text textKey = new Text("key"); textKey.setFont(font);
        Text textRule = new Text("Rule            "); textRule.setFont(font);
        Text textAction = new Text("Action        "); textAction.setFont(font);
        Text textValue = new Text("Value"); textValue.setFont(font);
        HBox title = new HBox(45, textKey,textRule,textAction,textValue);
        
        ruleBoxesBlock.getChildren().add(title);
        Main.putBoxesInControls(ruleBoxesBlock);
    }
    
    /** Adds a rulebox right after this one*/
    private void addNext()
    {
        RuleBox tmp = new RuleBox();
        ruleBoxesBlock.getChildren().remove(tmp.ruleBox);
        ruleBoxesBlock.getChildren().add(ruleBoxesBlock.getChildren().indexOf(this.ruleBox)+1,tmp.ruleBox);
    }
    /** Removes this ruleBox from existance */
    private void remove()
    {
        ruleBoxesList.remove(this);
        ruleBoxesBlock.getChildren().remove(ruleBox);
        ruleBox.toBack();
    }
    
    

    //#region constructors
    /** Spawns a rulebox already filled with information */
    //public RuleBox(String key, String rule, int actionId, double value, RuleBox next)
    //{
    //    this();
    //    //TODO: make more, fill w info
    //}
    
    /**Spawns an empty ruleBox */
    public RuleBox()
    {
        //#region setting up field for a key
        Text keyText = new Text("key"); keyText.setFont(font);
        TextField fieldKey = new TextField(); fieldKey.setPrefSize(30, 20);
        fieldKey.textProperty().addListener( (observable, oldValue, newValue) -> 
        {   // allowing only one symbol
            if (newValue.length()<=1) fieldKey.setText(newValue); 
            else fieldKey.setText(oldValue);
            
            if (newValue.length() > 0) key = newValue.charAt(0);
            else key = null;
        });
        //#endregion
        // тирешки
        Text txtLine1 = new Text("-"); txtLine1.setFont(font); Text txtLine2 = new Text("-"); txtLine2.setFont(font);
        // поле для правила
        TextField fieldRule = new TextField(); fieldRule.setPrefSize(70, 20);
        fieldRule.textProperty().addListener( (observable, oldValue, newValue) -> 
        {   
            rule = (newValue.length()==0) ? "" : newValue;
        });

        //поле для значения. прячется, если выбрано действие, где не нужно значение
        TextField fieldValue = new TextField(); fieldValue.setPrefSize(62, 20); fieldValue.setPromptText("0");
        fieldValue.textProperty().addListener( (observable, oldValue, newValue) -> 
        {   //validating double
            if ( newValue.matches("[+-]?\\d+\\.?(\\d+)?") || newValue.length()==0) fieldValue.setText(newValue); 
            else fieldValue.setText(oldValue);

            actionValue = (newValue.length()==0 || newValue.equals("-")) ? 0 : Double.parseDouble(newValue);
        });
        
        // drop-down list
        ComboBox<String> actionField = new ComboBox<>(actions); actionField.setVisibleRowCount(4); actionField.setPrefWidth(189);
        actionField.valueProperty().addListener((obs, oldValue, newValue) -> { //hiding or showing value field
            actionId = actionField.getSelectionModel().getSelectedIndex();
            if (actionId < 4) fieldValue.setVisible(true); else fieldValue.setVisible(false);
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
                if (ruleBoxesList.size()>1) remove();
            }
        });
        
        

        ruleBox = new HBox(fieldKey,txtLine1,fieldRule,txtLine2, actionField, fieldValue, btnAdd, btnRemove); 
        ruleBox.setSpacing(10);
        ruleBoxesBlock.getChildren().add(ruleBox); 
        ruleBoxesList.add(this);
    }
    //#endregion
}