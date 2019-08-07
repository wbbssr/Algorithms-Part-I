import java.util.Comparator;
import edu.princeton.cs.algs4.*;

public class FastCollinearPoints {
    private LineSegment[] linesegment;
    private Point[] begin = new Point[1];
    private Point[] end = new Point[1];
    private int numOfSeg = 0;
    //private double[] slopes;
    //private Point[] pointsActual;
    //private Point[] pointsCopy;  

    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
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
        sort(pointsActual);

        for (int i = 0; i < pointsActual.length - 1; i++)
            if (0 == pointsActual[i].compareTo(pointsActual[i + 1]))
                throw new java.lang.IllegalArgumentException();

        //for (int i = 0; i < pointsActual.length; i++)
        //    StdOut.print(pointsActual[i].toString() + " ");
        //StdOut.println("");

        double[] slopes = new double[pointsActual.length];
        Point[] pointsCopy = new Point[pointsActual.length];

        for (int i = 0; i < pointsActual.length; i++) {
            //for (int j = 0; j < pointsActual.length; j++)
            //    StdOut.print(pointsActual[j].toString() + " ");
            //StdOut.println("");

            for (int j = 0; j < pointsActual.length; j++) {
                slopes[j] = pointsActual[i].slopeTo(pointsActual[j]);
                pointsCopy[j] = pointsActual[j];
                //StdOut.print(slopes[j] + " ");
            }
            //StdOut.println("");
            sort(pointsCopy, slopes);

            //for (int j = 0; j < pointsActual.length; j++) {
            //    StdOut.print(slopes[j] + " ");
            //}
            //StdOut.println("");

            //for (int j = 0; j < pointsActual.length; j++) {
            //    StdOut.print(pointsCopy[j].toString() + " ");
            //}
            //StdOut.println("");
            //StdOut.println("");

            // j从1开始，因为第一个个元素始终为-Infinity且永远只有一个
            // 因为必须有3个及以上的斜率相同且斜率单调递增，所以我们只需要隔一个比较
            for (int j = 1; j < pointsActual.length - 2; j++) {
                if (slopes[j] == slopes[j + 2]) {
                    // 记录三个及三个以上斜率相同点的第一个点的指标
                    int beginOfSameSlope = j;
                    // 此时必然存在四个点于一条直线，下面需要确定是否有更多的点在一条直线
                    j = j + 2;
                    while ((j + 1) < pointsActual.length && slopes[j] == slopes[j + 1])
                        j++;
                    // 记录三个及三个以上斜率相同点的最后一个点的指标
                    int endOfSameSlope = j;

                    // 比较pointsCopy[0], pointsCopy[beginOfSameSlope], pointsCopy[endOfSameSlope]的大小
                    if (pointsCopy[0].compareTo(pointsCopy[beginOfSameSlope]) < 0)
                        beginOfSameSlope = 0;
                    else if (pointsCopy[0].compareTo(pointsCopy[endOfSameSlope]) > 0) 
                        endOfSameSlope = 0;

                    // 判断是否有重复，必须确认与之前的不重复，才可以加到加到linesegment上
                    boolean haveSame = false;
                    for (int kk = 0; kk < numOfSeg; kk++) {
                        if (pointsCopy[beginOfSameSlope].compareTo(begin[kk]) == 0 && pointsCopy[endOfSameSlope].compareTo(end[kk]) == 0) {
                            haveSame = true;
                            break;
                        }
                    }
                    if (!haveSame) {
                        assert begin.length == end.length;

                        if (numOfSeg == begin.length)
                            resize(2 * begin.length);
                        begin[numOfSeg] = pointsCopy[beginOfSameSlope];
                        end[numOfSeg] = pointsCopy[endOfSameSlope];
                        numOfSeg++;
                    }

                }
            }
        }
        linesegment = new LineSegment[numOfSeg];
        for (int kk = 0; kk < numOfSeg; kk++)
            linesegment[kk] = new LineSegment(begin[kk], end[kk]);
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

    private static void merge(Point[] a, Point[] aux, double[] b, double[] bux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
            bux[k] = b[k];
        }

        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j];
                b[k] = bux[j];
                j++;
            }
            else if (j > hi) {
                a[k] = aux[i];
                b[k] = bux[i];
                i++;
            }
            else if (bux[j] < bux[i]) {
                a[k] = aux[j];
                b[k] = bux[j];
                j++;
            }
            else {
                a[k] = aux[i];
                b[k] = bux[i];
                i++;
            }
        }
    }

    private static void sort(Point[] a, Point[] aux, double[] b, double[] bux, int lo, int hi) {
        if (hi <= lo)
            return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, b, bux, lo, mid);
        sort(a, aux, b, bux, mid + 1, hi);
        merge(a, aux, b, bux, lo, mid, hi);
    }

    private static void sort(Point[] a, double[] b) {
        assert a.length == b.length;

        Point[] aux = new Point[a.length];
        double[] bux = new double[b.length];
        sort(a, aux, b, bux, 0, a.length - 1);
    }

    // 使用resize array储存队列LineSegement，当存储满的时候double数组
    private void resize(int capacity) {
        Point[] copyBegin = new Point[capacity];
        Point[] copyEnd = new Point[capacity];

        for (int i = 0; i < numOfSeg; i++) {
            copyBegin[i] = begin[i];
            copyEnd[i] = end[i];
        }
        begin = copyBegin;
        end = copyEnd;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points); 
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw(); 
        } 
        StdDraw.show();
   }
}