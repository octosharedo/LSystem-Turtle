import java.util.function.Consumer;

public class Action {

    private int id =-1;
    private double value = 0;
    private Consumer<Double> func;

    public void act(){func.accept(value);}
    
    public Action(int id, Double value)
    {
        this.id = id;
        this.value = value;

        switch (id) {
            case 0:
                func = val -> Drawer.forward(val);
                break;
            case 1:
                func = val -> Drawer.backwards(val);
                break;
            case 2:
                func = val -> Drawer.turnRight(val);
                break;
            case 3:
                func = val -> Drawer.turnLeft(val);
                break;
            case 4:
                func = val -> Drawer.liftPen();
                break;
            case 5:
                func = val -> Drawer.lowerPen();
                break;
            case 6:
                func = val -> Drawer.rememberPosition();
                break;
            case 7:
                func = val -> Drawer.recallPosition();
                break;
            default:
                System.out.println("Error assigning new action");
                break;
        }
    }
    
    public double getValue() {return value;}
    public int getId() {return id;}
}