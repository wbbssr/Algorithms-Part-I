/*************************************************************************
 *  Compilation:  javac-algs4 LineSegment.java
 *  Execution:    java-algs4 LineSegment input8.txt
 *  Dependencies: Point.java LineSegment.java
 *************************************************************************/

import java.util.*;
import java.lang.*;
import edu.princeton.cs.algs4.*;

public class BruteCollinearPoints {
    //private final Point n;  
    //private final Point q;  
    //private final Point r;  
    //private final Point s;
    private LineSegment[] linesegment = new LineSegment[1];
    private int numOfSeg = 0;  

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        if (points == null)
            throw new java.lang.IllegalArgumentException();
        for (int i = 0; i < points.length; i++)
            if (points[i] == null)
                throw new java.lang.IllegalArgumentException();

        // 对参数进行复制，防止操作改变原参数
        Point[] pointsActual = new Point[points.length];
        for (int i = 0; i < points.length; i++)
            pointsActual[i] = points[i];

        // 对points数组进行从小到大的排序
        Arrays.sort(pointsActual);
        //sort(pointsActual);

        for (int i = 0; i < pointsActual.length - 1; i++)
            if (0 == pointsActual[i].compareTo(pointsActual[i + 1]))
                throw new java.lang.IllegalArgumentException();
        /* CODE HERE */
        /*for (int i = 0; i < pointsActual.length; i++)
            StdOut.print(pointsActual[i].toString() + " ");
        StdOut.println("");

        int count = 0; */
        for (int i = 0; i <= pointsActual.length - 4; i++) {
            for (int j = i + 1; j <= pointsActual.length - 3; j++) {
                for (int k = j + 1; k <= pointsActual.length - 2; k++) {
                    for (int l = k + 1; l <= pointsActual.length - 1; l++) {
                        //StdOut.println(i + " " + j + " " + k + " " + l);
                        //count++;
                        if (pointsActual[i].slopeTo(pointsActual[j]) == pointsActual[i].slopeTo(pointsActual[k]) && pointsActual[i].slopeTo(pointsActual[k]) == pointsActual[i].slopeTo(pointsActual[l])) {
                            if (numOfSeg == linesegment.length)
                                resize(2 * linesegment.length);
                            linesegment[numOfSeg++] = new LineSegment(pointsActual[i], pointsActual[l]);
                        }
                    }
                }
            }
        }

        // 需要resize一下linesegement，否则可能会在最后出现null元素
        resize(numOfSeg);

        //StdOut.println("# of combination: " + count);
        //StdOut.println("# of segment: " + numOfSeg);

    }

    public           int numberOfSegments() {       // the number of line segments
        return numOfSeg;
    }

    public LineSegment[] segments() {               // the line segments
        LineSegment[] linesegmentCopy = new LineSegment[linesegment.length];
        for (int i = 0; i < linesegment.length; i++)
            linesegmentCopy[i] = linesegment[i];

        return linesegmentCopy;
    }

    private static void merge(Point[] a, Point[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++)
            aux[k] = a[k];

        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid)
                a[k] = aux[j++];
            else if (j > hi)
                a[k] = aux[i++];
            else if (aux[j].compareTo(aux[i]) < 0)
                a[k] = aux[j++];
            else
                a[k] = aux[i++];
        }
    }

    private static void sort(Point[] a, Point[] aux, int lo, int hi) {
        if (hi <= lo)
            return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    private static void sort(Point[] a) {
        Point[] aux = new Point[a.length];
        sort(a, aux, 0, a.length - 1);
    }

    // 使用resize array储存队列LineSegement，当存储满的时候double数组
    private void resize(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];

        for (int i = 0; i < numOfSeg; i++)
            copy[i] = linesegment[i];
        linesegment = copy;
    }

    public static void main(String[] args) {
        // read the n points from a file 
        In in = new In(args[0]); 
        int n = in.readInt(); 
        Point[] points = new Point[n]; 
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y); 
        }

        // draw the points 
        StdDraw.enableDoubleBuffering(); 
        StdDraw.setXscale(0, 32768); 
        StdDraw.setYscale(0, 32768); 
        for (Point p : points) { 
            p.draw();
        } 
        StdDraw.show();

        // print and draw the line segments 
        BruteCollinearPoints collinear = new BruteCollinearPoints(points); 
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw(); 
        } 
        StdDraw.show();
   }

}