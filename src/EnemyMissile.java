public class EnemyMissile extends Character{

    boolean MoveDown;
    boolean isVisible;

    public EnemyMissile(int x, int y, int speed){
        super(x,y,speed);
        MoveDown = true;
        isVisible=true;
    }
}