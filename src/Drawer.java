import javafx.scene.paint.Color;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

/* Базовые команды:
повернуть направо / повернуть налево (на указанный угол)
переместиться вперёд / переместиться назад (на указанное расстояние, обычно в условных «шагах», часто равных по длине размеру пикселя экрана)
поднять перо / опустить перо
установить новый цвет пера / установить новую толщину пера
установить новый курс / установить новое место (поворот и перемещение относительно базовой Декартовой системы координат листа)
стереть всё
показать черепаху / спрятать черепаху
получить значения текущих координат, угла поворота черепахи, цвета и толщины пера
*/

public class Drawer {

    private static GraphicsContext gc;
    private static boolean isDrawing = true;    
    private static Color penColor = Color.BLACK;
    private static double penSize = 4;
    private static double angle; //градусы
    private static Point2D position;
    private static Point2D remembered;
    private static double step = 5; //длина отрезка при forward 1

    public static void setGraphicsContext(GraphicsContext graphxCont, double xStart, double yStart) 
    {
        gc = graphxCont; 
        gc.setFill(penColor);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(penSize);
        position = new Point2D(xStart, yStart);
        gc.moveTo(position.getX(), position.getY()); angle = 0;
    }
    
    public static void forward(double value) 
    {
        double nextY = position.getY() + Math.sin(angle*Math.PI/180)*step*value;
        //double nextYRight = position.getY() + Math.sin(-angle*Math.PI/180)*step*value;
        double nextX = position.getX() + Math.cos(angle*Math.PI/180)*step*value;
        //double nextXRight = position.getX() + Math.cos(-angle*Math.PI/180)*step*value;
        if (isDrawing) gc.lineTo(nextX, nextY);
        position = new Point2D(nextX, nextY); 
        gc.stroke();
    }
    public static void backwards(double value) {forward(-value);}
    public static void turnRight(double value) {angle += value;}
    public static void turnLeft(double value) {angle -= value;}
    public static void liftPen() {isDrawing = false;}
    public static void lowerPen() {isDrawing = true;}
    public static void rememberPosition() {remembered = position;}
    public static void recallPosition() {setPosition(remembered.getX(), remembered.getY());}
    
    public static Point2D getPosition() {return position;}
    public static void setPosition(double x, double y) {position = new Point2D(x, y); gc.moveTo(x, y);}

}