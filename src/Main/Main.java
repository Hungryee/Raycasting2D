package Main;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class Main extends JPanel implements MouseMotionListener, KeyListener {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 800;
    public static Ball b;
    public static Battery[] batteries = new Battery[5];
    public static Wall[] walls= new Wall[]{
            new Wall(100,100,100,500),
            new Wall(900,200,900,600),
            new Wall(550,100,550,400),
            new Wall(250,700,700,700),

            //OUTER WALLS OF ROOM
            new Wall(2,2,WIDTH-2,2),
            new Wall(2,2,2,HEIGHT-2),
            new Wall(2,HEIGHT-2,WIDTH-2,HEIGHT-2),
            new Wall(WIDTH-2,2,WIDTH-2,HEIGHT-2),
    };
    public static Ray[] rays;
    public static double[] blocks;
    public static double charges;
    public static double currCharge=100;
    public static double time = 0;
    public static double blockSize;
    public static double viewOff = 0;
    Main(){
        setFocusable(true);
        b = new Ball();
        for (int i = 0; i < 1; i++) {
            batteries[i] = new Battery(Math.random()*WIDTH, Math.random()*HEIGHT);
        }
        rays = new Ray[(int) Math.toDegrees(b.viewAngle)];
        blocks = new double[(int) Math.toDegrees(b.viewAngle)];
        blockSize = 500/blocks.length;
        charges = 5;
        currCharge = 100;
        addKeyListener(this);
        //addMouseMotionListener(this);
    }
    @Override
    public void paint(Graphics g){
        time++;
        if (time%2200==0){
            for (int i = 0; i < batteries.length; i++) {
                if (batteries[i]==null){
                    batteries[i] = new Battery(Math.random()*WIDTH, Math.random()*HEIGHT);
                    if (Math.random()<0.2){
                        continue;
                    }
                    break;
                }
            }
        }
        if (b.isOn) {
            currCharge -= 0.03;
        }
        if (currCharge<=0) {
            charges--;
            currCharge = 100;
        }
        g.setColor(Color.BLACK);
        g.fillRect(0,0,WIDTH+500,HEIGHT);
        b.show(g);
        repaint();

        for (int i = 0; i < charges; i++) {
            g.setColor(Color.white);
            g.fillRect( 30+35*i, 10,30,50);
        }
        g.fillRect((int) (35*(charges+1)-5), (int) (10+50-currCharge/2),30, (int) (currCharge/2)+1);
        draw3DBlocks(g);
    }
    public void draw3DBlocks(Graphics g){
        if (b.isOn) {
            for (int i = 0; i < blocks.length; i++) {
                if (blocks[i] <= 1281) {
                    double gray = map(blocks[i], 0, 1281*Math.cos(1), 1, 0.01);
                    g.setColor(new Color((float) (gray*gray), (float) (gray*gray), (float) (gray*gray), 1));
                }
                double h = map(blocks[i]*blocks[i], 0, 1281*1281, 300, 0);
                g.fillRect((int) (1000 + i * blockSize), (int) (400+100 - h + 100+1), (int) blockSize, (int) (h - 100-100));
                g.fillRect((int) (1000 + i * blockSize), (int) (400+100-100), (int) blockSize, (int) (h - 100-100));
            }
        }
    }
    public double map(double value, double min, double max, double needMin, double needMax){
        return (value-min)/(max-min)*(needMax-needMin)+needMin;
    }
    public static void main(String[] args) {
        JFrame f = new JFrame("RAYCASTING");
        f.setBackground(Color.black);
        f.add(new Main());
        f.setSize(WIDTH+500,HEIGHT);
        f.setVisible(true);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        viewOff = HEIGHT/2f-e.getY();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key==KeyEvent.VK_W) b.vel=-1;
        if (key==KeyEvent.VK_S) b.vel=1;
        if (key==KeyEvent.VK_A) b.turnVel=-0.01;
        if (key==KeyEvent.VK_D) b.turnVel=0.01;
        if (key==KeyEvent.VK_E) {
            b.isOn=!b.isOn;
            b.alpha = 1;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key==KeyEvent.VK_W) b.vel=0;
        if (key==KeyEvent.VK_S) b.vel=0;
        if (key==KeyEvent.VK_A) b.turnVel=0;
        if (key==KeyEvent.VK_D) b.turnVel=0;
    }
}
