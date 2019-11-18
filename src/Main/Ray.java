package Main;

import java.awt.*;

import static Main.Main.*;

public class Ray {
    double angle;
    double x1;
    double y1;
    double x2;
    double y2;
    double radius;
    double[] pt = null;
    public Ray(double x, double y, double angle) {
        this.x1 = x;
        this.y1 = y;
        this.angle = angle;
    }
    public void show(Graphics g){
        radius = Math.max(x1*x1+y1*y1,
                 Math.max((WIDTH-x1)*(WIDTH-x1)+y1*y1,
                 Math.max((WIDTH-x1)*(WIDTH-x1)+(HEIGHT-y1)*(HEIGHT-y1),
                 x1*x1+(WIDTH-x1)+(HEIGHT-y1))));
        radius = 350;
        x2 = x1+radius*Math.cos(Math.toRadians(angle));
        y2 = y1+radius*Math.sin(Math.toRadians(angle));
    }
    public double[] cast(Wall w){
        double den = ((x1-x2)*(w.y1-w.y2)-(y1-y2)*(w.x1-w.x2));
        double t =  ((x1-w.x1) * (w.y1-w.y2) - (y1-w.y1) * (w.x1-w.x2)) /den;
        double u = -((x1-x2)   * (y1-w.y1)   - (y1-y2)   * (x1-w.x1))   /den;
        if (den==0) {return null;}
        if ((t>=0&&t<=1&&u>=0&&u<=1)) {
            return new double[]{x1 + t * (x2 - x1), y1 + t * (y2 - y1)};
        }else{
            return null;
        }
    }
    public boolean cast(Battery bat){
        double dx = x2-x1;
        double dy = y2-y1;
        double a = dx*dx+dy*dy;
        double b = 2*(dx*(x1-bat.x)+dy*(y1-bat.y));
        double c = Math.pow(x1-bat.x,2)+Math.pow(y1-bat.y,2)-bat.size*bat.size/4f;
        double det = b*b-4*a*c;
        return (det>=0);
    }
}
