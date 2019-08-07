import edu.princeton.cs.algs4.*;

public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;
    private Queue<Point2D> queueRange;
    private Point2D nearest;
    private double nearestSquareDistance;
    private KdNode root;

    public         KdTree() {                               // construct an empty set of points 
    }
    public           boolean isEmpty() {                     // is the set empty? 
        return size() == 0;
    }
    public               int size() {                        // number of points in the set 
        return size(root);
    }
    public              void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new java.lang.IllegalArgumentException();
        // 我们假设root上面还有一个KdNode, 它始终是HORIZONTAL
        root = insert(root, p, HORIZONTAL);
    }
    private KdNode insert(KdNode node, Point2D p, boolean parentVorH) {
        if (parentVorH == HORIZONTAL) {
            if (node == null) {
                return new KdNode(p, 1, VERTICAL);
            }

            double cmp = p.x() - node.point.x();
            if (cmp < 0)
                node.left = insert(node.left, p, VERTICAL);
            else if (cmp > 0)
                node.right = insert(node.right, p, VERTICAL);
            else {
                if (p.y() - node.point.y() < 0)
                    node.left = insert(node.left, p, VERTICAL);
                if (p.y() - node.point.y() > 0)
                    node.right = insert(node.right, p, VERTICAL);
            }
            node.size = 1 + size(node.left) + size(node.right);
            return node;

        }
        else {
            if (node == null)
                return new KdNode(p, 1, HORIZONTAL);

            double cmp = p.y() - node.point.y();
            if (cmp < 0)
                node.left = insert(node.left, p, HORIZONTAL);
            else if (cmp > 0)
                node.right = insert(node.right, p, HORIZONTAL);
            else {
                if (p.x() - node.point.x() < 0)
                    node.left = insert(node.left, p, HORIZONTAL);
                if (p.x() - node.point.x() > 0)
                    node.right = insert(node.right, p, HORIZONTAL);
            }
            node.size = 1 + size(node.left) + size(node.right);
            return node;
        }
    }
    public           boolean contains(Point2D p) {           // does the set contain point p?
        if (p == null)
            throw new java.lang.IllegalArgumentException();
        return get(p) != null;
    }
    private KdNode get(Point2D p) {
        return get(root, p);
    } 
    private KdNode get(KdNode x, Point2D p) {
        if (x == null)
            return null;

        if (x.isVertical == VERTICAL) {
            double cmp = p.x() - x.point.x();
            if (cmp < 0)
                return get(x.left, p);
            else if (cmp > 0)
                return get(x.right, p);
            else {
                if (p.y() - x.point.y() < 0)
                    return get(x.left, p);
                else if (p.y() - x.point.y() > 0)
                    return get(x.right, p);
                else
                    return x;
            }
        }
        else {
            double cmp = p.y() - x.point.y();
            if (cmp < 0)
                return get(x.left, p);
            else if (cmp > 0)
                return get(x.right, p);
            else {
                if (p.x() - x.point.x() < 0)
                    return get(x.left, p);
                else if (p.x() - x.point.x() > 0)
                    return get(x.right, p);
                else
                    return x;
            }
        }

    }
    public              void draw() {                        // draw all points to standard draw 
        draw(root);
    }
    private void draw(KdNode x) {
        if (x == null)
            return;

        x.point.draw();
        draw(x.left);
        draw(x.right);
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle 
        if (rect == null)
            throw new java.lang.IllegalArgumentException();

        queueRange = new Queue<Point2D>();
        range(rect, root);
        return queueRange;
        
    }

    private void range(RectHV rect, KdNode x) {
        if (x == null)
            return;
        if (rect.contains(x.point)) {
            queueRange.enqueue(x.point);
            range(rect, x.left);
            range(rect, x.right);
        }
        else {
            if (x.isVertical == VERTICAL) {
                if (x.point.x() > rect.xmax())
                    range(rect, x.left);
                else if (x.point.x() < rect.xmin())
                    range(rect, x.right);
                else {
                    range(rect, x.left);
                    range(rect, x.right);
                }

            }
            else {
                if (x.point.y() > rect.ymax())
                    range(rect, x.left);
                else if (x.point.y() < rect.ymin())
                    range(rect, x.right);
                else {
                    range(rect, x.left);
                    range(rect, x.right);
                }
            }
        }


    }
    public           Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null)
            throw new java.lang.IllegalArgumentException();

        nearest = root.point;
        nearestSquareDistance = p.distanceSquaredTo(root.point);

        RectHV unitSquare = new RectHV(0, 0, 1, 1);
        nearest(p, root, unitSquare);
        return nearest;
    }

    private void nearest(Point2D p, KdNode x, RectHV rect) {
        RectHV rectLeft;
        RectHV rectRight;
        if ( x.isVertical == VERTICAL) {
            rectLeft = new RectHV(rect.xmin(), rect.ymin(), x.point.x(), rect.ymax());
            rectRight = new RectHV(x.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
        }
        else {
            rectLeft = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.point.y());
            rectRight = new RectHV(rect.xmin(), x.point.y(), rect.xmax(), rect.ymax());
        }

        if (x.left != null) {
            if (p.distanceSquaredTo(x.left.point) < nearestSquareDistance) {
                nearestSquareDistance = p.distanceSquaredTo(x.left.point);
                nearest = x.left.point;
            }
        }

        if (x.right != null) {
            if(p.distanceSquaredTo(x.right.point) < nearestSquareDistance) {
                nearestSquareDistance = p.distanceSquaredTo(x.right.point);
                nearest = x.right.point;
            }
        }

        if (x.left != null) {
            if (rectLeft.distanceSquaredTo(p) < nearestSquareDistance) {
                nearest(p, x.left, rectLeft);
            }
        }

        if (x.right != null) {
            if (rectRight.distanceSquaredTo(p) < nearestSquareDistance) {
                nearest(p, x.right, rectRight);
            }
        }
    }
    
    /*public Iterable<Point2D> levelOrder() {
        Queue<Boolean> directs = new Queue<Boolean>();
        Queue<Point2D> pts = new Queue<Point2D>();

        Queue<KdNode> queue = new Queue<KdNode>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            KdNode x = queue.dequeue();
            if (x == null)
                continue;
            pts.enqueue(x.point);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }

        return pts;
    }*/
    private static class KdNode {
        private Point2D point;
        private int size;
        private boolean isVertical;
        private KdNode left, right;

        public KdNode(Point2D point, int size, boolean isVertical) {
            this.point = point;
            this.size = size;
            this.isVertical = isVertical;
        }
    }

    private int size(KdNode x) {
        if (x == null)
            return 0;
        else
            return x.size;
    }
    public static void main(String[] args) {                 // unit testing of the methods (optional) 
    }
}