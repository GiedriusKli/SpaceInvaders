public class Missile extends Character{

    boolean MoveUp;
    boolean isVisible;

    public Missile(int x, int y, int speed){
        super(x,y,speed);
        MoveUp = true;
        isVisible=true;
    }

}
