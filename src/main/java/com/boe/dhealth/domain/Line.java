package com.boe.dhealth.domain;

public class Line {

    private Point point;
    private double slope; //斜率

    public double getSlope() {
        return slope;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public boolean isParallel(Line line) { //比较两个斜率是否相同
        if(this.slope==line.slope)return true;
        return false;
    }

    public Line(Point point,double slope){
        this.point=point;
        this.slope=slope;
    }
    public Line(Point p1,Point p2){
        this(p1,p2.getY()-p1.getY()/(p2.getX()-p1.getX()));//调用上面的构造器，求得斜率

    }
    public Line(int a,int b){
        this(new Point(a,0),new Point(0,b));
    }
    public static void main(String []args){
        Point p1 = new Point(1,10.5);
        Point p2 = new Point(2,3);
        Line l1=new Line(p1,p2);
        System.out.print(l1.getSlope());
    }
}
