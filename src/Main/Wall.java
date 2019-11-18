package Main;

import java.awt.*;

public class Wall {
    public double x1 ;
    public double y1;
    public double x2;
    public double y2;

    public Wall(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    public void show(Graphics g, double xStart, double yStart, double xEnd, double yEnd){
        if (xStart==-1&&yStart==-1&&xEnd==-1&&yEnd==-1) {
            g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }else{
            g.drawLine((int) xStart, (int) yStart, (int) xEnd, (int) yEnd);
        }
    }
}
