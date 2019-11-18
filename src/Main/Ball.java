package Main;

import java.awt.*;

import static Main.Main.*;
import static java.lang.Math.*;

public class Ball {
    public  double x = WIDTH/2f;
    public  double y = HEIGHT/2f;
    public double prevX = x;
    public double prevY = y;
    public  int size = 15;
    public  int vel = 0;
    public  double turnAngle=0;
    public  double turnVel = 0;
    public  double viewAngle = toRadians(75); //value in brackets is in degrees
    public float alpha=1.0f;
    public boolean isOn = true;
    public void show(Graphics g){

        prevX = x;
        prevY = y;
        x+=-vel*cos(turnAngle);
        y+=-vel*sin(turnAngle);
        checkWalls();
        checkBats();
        turnAngle+=turnVel;
        if (turnAngle>=2*PI) turnAngle -=2*PI;
        if (turnAngle<=-2*PI) turnAngle +=2*PI;

        if (x<1) {x = 1;}
        if (x+size/2>WIDTH-1) {x = WIDTH-1-size/2;}
        if (y<1) {y = 1;}
        if (y+size/2+20>HEIGHT-1) {y = HEIGHT-21-size/2;}

        g.setColor(Color.CYAN);
        g.fillOval((int)(x-size/2),(int)(y-size/2),size,size);
        if (isOn) {
            alpha = (float) (currCharge/100);
            if (alpha<=0) alpha = 0.05f;
            showRays(g);
            g.setColor(new Color(0.8f,0.8f,0.8f, alpha/2));
            g.drawLine((int) x, (int) y, (int) (x + rays[0].radius * cos(turnAngle + viewAngle / 2)), (int) (y + rays[0].radius * sin(turnAngle + viewAngle / 2)));
            g.drawLine((int) x, (int) y, (int) (x + rays[0].radius * cos(turnAngle - viewAngle / 2)), (int) (y + rays[0].radius * sin(turnAngle - viewAngle / 2)));
        }
    }
    public void checkBats(){
        for (int i = batteries.length-1; i >=0; i--) {
            if (batteries[i]!=null) {
                double dist = Math.sqrt(Math.pow(x - batteries[i].x, 2) + Math.pow(y - batteries[i].y, 2));
                if (dist <= batteries[i].size / 2f) {
                    batteries[i] = null;
                    charges++;
                }
            }
        }
    }
    public void checkWalls(){
        for (Wall w: walls) {
            double num = Math.abs((w.y2-w.y1)*x-(w.x2-w.x1)*y+w.x2*w.y1-w.y2*w.x1);
            double den = Math.sqrt(Math.pow(w.y2-w.y1,2)+Math.pow(w.x2-w.x1,2));
            if (num/den<=1&&x>=min(w.x1,w.x2)-1&&x<=max(w.x1,w.x2)+1&&y>=min(w.y1,w.y2)-1&&y<=max(w.y1,w.y2)+1){
                x = prevX;
                y = prevY;
            }
        }
    }
    public void showRays(Graphics g){
        blocks = new double[(int) Math.toDegrees(b.viewAngle)];
        for (int k = 0; k < rays.length; k++) {
            double[] closest = null;
            double record = 1281;
            double distance = 0;
            Wall closestWall = null;
            rays[k] = new Ray(x,y, toDegrees(turnAngle-viewAngle/2)+k);
            rays[k].show(g);
            double a = toRadians(rays[k].angle) - turnAngle;
            for (int j = 0; j < walls.length; j++) {
                rays[k].pt = rays[k].cast(walls[j]);
                if (k!=0) {
                    if (rays[k].pt != null && rays[k - 1].cast(walls[j]) != null) {
                        double dist = sqrt(pow(rays[k].x1 - rays[k].pt[0], 2) + pow(rays[k].y1 - rays[k].pt[1], 2));
                        dist*=cos(a);
                        if (dist < record) {
                            record = dist;
                            distance = dist;
                            closestWall = walls[j];
                            closest = rays[k].pt;
                        }
                    }
                }
            }
            for (int i = 0; i < batteries.length; i++) {
                if (batteries[i]!=null) {
                    double dist = sqrt(pow(rays[k].x1 - batteries[i].x, 2) + pow(rays[k].y1 - batteries[i].y, 2));
                    if (dist < record && rays[k].cast(batteries[i])) {
                        batteries[i].show(g);
                    }
                }
            }

            //1281 is the length of window diagonal == the longest possible distance in current scene
            //therefore no matter the distance the gray color values can never exceed [0,1] range

            if (closestWall!=null){
                g.setColor(new Color((float) (1 - distance / 1281), (float) (1 - distance / 1281), (float) (1 - distance / 1281), 1));
                closestWall.show(g, rays[k].cast(closestWall)[0], rays[k].cast(closestWall)[1], rays[k - 1].cast(closestWall)[0], rays[k - 1].cast(closestWall)[1]);
            }
            g.setColor(new Color(0.8f,0.8f,0.6f, alpha/2));

            if (closest!=null){
                g.drawLine((int) (rays[k].x1),(int) (rays[k].y1), (int) closest[0],(int) closest[1]);

            }else{
                g.drawLine((int) (rays[k].x1),(int) (rays[k].y1),(int) (rays[k].x2),(int) (rays[k].y2));
            }
//            if (k!=0){
//                g.drawLine(
//                        (int)((rays[k-1]!=null)?(rays[k-1].pt[0]):(rays[k-1].x2)),
//                        (int)((rays[k-1]!=null)?(rays[k-1].pt[1]):(rays[k-1].y2)),
//                        (int)((rays[k]!=null)?(rays[k].pt[0]):(rays[k].x2)),
//                        (int)((rays[k]!=null)?(rays[k].pt[1]):(rays[k].y2))
//                        );
//            }
            blocks[k] = record;
        }

    }
}
