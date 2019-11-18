package Main;

import java.awt.*;

public class Battery {
    public double x;
    public double y;
    public int size=20;
    public Battery(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void show(Graphics g){
        g.setColor(Color.DARK_GRAY);
        g.fillOval((int)(x-size/2),(int)(y-size/2),size,size);
    }
}
