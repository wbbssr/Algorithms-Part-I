import java.util.Comparator;
import edu.princeton.cs.algs4.*;

public class PointSET {
    private SET<Point2D> set;

    public         PointSET() {                              // construct an empty set of points 
        set = new SET<Point2D>();
    }
    public           boolean isEmpty() {                     // is the set empty? 
        return set.isEmpty();
    }
    public               int size() {                        // number of points in the set 
        return set.size();
    }
    public              void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        set.add(p);
    }
    public           boolean contains(Point2D p) {            // does the set contain point p? 
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        return set.contains(p);
    }
    public              void draw() {                        // draw all points to standard draw 
        for (Point2D p : set)
            p.draw();
    }
    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle 
        if (rect == null)
            throw new java.lang.IllegalArgumentException();

        Stack<Point2D> stack = new Stack<Point2D>();

        for (Point2D p : set) {
            if (rect.contains(p))
                stack.push(p);
        }

        return stack;
    }
    public           Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        if (set.isEmpty()) 
            return null;

        Point2D nearestPoint = null;

        for (Point2D pt : set) {
            if (nearestPoint == null) {
                nearestPoint = pt;
            }
            else if (p.distanceSquaredTo(pt) < p.distanceSquaredTo(nearestPoint))
                nearestPoint = pt;
        }

        return nearestPoint;   
    }

    public static void main(String[] args) {                 // unit testing of the methods (optional) 
    }
}