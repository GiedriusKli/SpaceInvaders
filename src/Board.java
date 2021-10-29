import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.*;


public class Board  extends JPanel implements Runnable
{
    boolean ingame = true;
    private Dimension d;
    int BOARD_WIDTH=600;
    int BOARD_HEIGHT=400;
    private Thread animator;
    Player p;
    Missile m;
    EnemyMissile em;
    ArrayList<Alien> a = new ArrayList<>(15);
    public Board()
    {
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        p = new Player(BOARD_WIDTH/2, BOARD_HEIGHT, 4);
        int ax = 10;
        int ay = 10;
        for(int i = 0; i<15; i++){
            a.add(i, new Alien(ax, ay, 2));
            ax += 40;
            if(i==4){
                ax = 10;
                ay += 40;
            }
            if(i==9){
                ax=10;
                ay+= 40;
            }
        }
        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
        setDoubleBuffered(true);

        int delay = 3000;
        ActionListener enemyMissile = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int randomEnemyIndex = (int) (Math.random() * ((a.size())));
                    if(a.size()!=0){
                    em = new EnemyMissile(a.get(randomEnemyIndex).x + 10, a.get(randomEnemyIndex).y + 30, 2);
                    }
            }
        };
        Timer timer = new Timer(delay, enemyMissile);
        timer.start();
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        g.setColor(Color.white);
        g.fillRect(0, 0, d.width, d.height);

        g.setColor(Color.ORANGE);
        int [] x = {p.x-20, p.x+20, p.x};
        int [] y = {p.y, p.y, p.y-30};
        g.fillPolygon(x, y, 3);
        if(p.moveRight==true&&p.x<BOARD_WIDTH-33){
            if(p.x+p.speed>BOARD_WIDTH-33){
                p.x=BOARD_WIDTH-33;
            }
            else {
                p.x += p.speed;
            }
        }
        if(p.moveLeft == true&&p.x>20){
            if(p.x-p.speed <0){
                p.x=20;
            }
            else {
                p.x -= p.speed;
            }
        }
        g.setColor(Color.black);
        moveAliens();
        for(int i = 0; i<a.size(); i++){
            if(a.get(i).isVisible) {
                g.fillRect(a.get(i).x, a.get(i).y, 30, 30);
            }
        }
        g.setColor(Color.red);
        if(m!=null) {
            g.fillOval(m.x, m.y, 10, 10);
            moveMissile();
        }
        if(em!=null){
            g.fillOval(em.x, em.y, 10, 10);
            moveEnemyMissile();
        }
        if(!ingame){
            setVisible(false);
            g.dispose();
        };
    }
    public void moveAliens(){
        for(int i = 0; i<a.size(); i++){
            if(a.get(i).moveLeft==true && a.get(i).isVisible){
                a.get(i).x-= a.get(i).speed;
                if(a.get(i).y+30>=p.y-30 && a.get(i).x==p.x){
                    ingame=false;
                }
            }
            if(a.get(i).moveRight==true && a.get(i).isVisible){
                a.get(i).x += a.get(i).speed;
                if(a.get(i).y+30>=p.y-30 && a.get(i).x==p.x){
                    ingame=false;
                }
            }
        }
        for(int i = 0; i<a.size(); i++) {
            if (a.get(i).x >= BOARD_WIDTH-40 && a.get(i).isVisible)  {
                for (int j = 0; j < a.size(); j++) {
                    a.get(j).moveLeft = true;
                    a.get(j).moveRight = false;
                    a.get(j).y+=15;

                }
            }
            if (a.get(i).x < 0 && a.get(i).isVisible)
                for (int j = 0; j < a.size(); j++) {
                    a.get(j).moveRight = true;
                    a.get(j).moveLeft = false;
                    a.get(j).y+=15;
                    if(a.get(j).isVisible&& a.get(j).y+30>=p.y-30){
                        ingame=false;
                    }
                }
        }
    }
    public void moveEnemyMissile(){
        if(em!=null){
            if(em.y!=400){
                em.y+=5;
            }
            else{
                em=null;
            }

        }
        if(em!=null&&em.x>=p.x && em.x<=p.x+27 &&em.y>=p.y&&em.y<=p.y+27){
            ingame=false;
        }
    }

    public void moveMissile(){
        for(int i=0; i<a.size(); i++){
            if(a.get(i).isVisible&&m.x>= a.get(i).x && m.x<= a.get(i).x+27 && m.y>= a.get(i).y && m.y<= a.get(i).y+27){
                a.get(i).isVisible=false;
                m=null;
                a.remove(i);
                break;
            }
        }
        if(m!=null&&m.y!=0) {
            m.y -= 5;
        }
        else{
            m=null;
        }
        if(a.size()==0){
            ingame=false;
        }
    }
    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if(key==KeyEvent.VK_RIGHT){
                p.moveRight=false;
            }
            if(key==KeyEvent.VK_LEFT){
                p.moveLeft=false;
            }
        }

        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if(key==KeyEvent.VK_RIGHT){
                p.moveRight=true;
            }
            if(key==KeyEvent.VK_LEFT){
                p.moveLeft=true;
            }
            if(key==KeyEvent.VK_SPACE){
                if(m==null) {
                    m = new Missile(p.x - 5, p.y - 40, 2);
                }
            }
        }
    }
    public void run() {
        int animationDelay = 8;
        while (ingame==true) {
            repaint();
            try {
                Thread.sleep(animationDelay);
            }catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}
