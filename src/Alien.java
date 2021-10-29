import java.util.stream.IntStream;

public class Alien extends Character{

    boolean moveRight;
    boolean moveLeft;
    boolean isVisible;

    public Alien(int x, int y, int speed){
        super(x,y,speed);
        moveLeft=false;
        moveRight=true;
        isVisible=true;
    }
}