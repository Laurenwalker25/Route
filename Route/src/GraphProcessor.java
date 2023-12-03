import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.io.File;
import java.io.FileInputStream;

/**
 * Models a weighted graph of latitude-longitude points
 * and supports various distance and routing operations.
 * To do: Add your name(s) as additional authors
 * @author Brandon Fain
 *
 */
public class GraphProcessor {
    /**
     * Creates and initializes a graph from a source data
     * file in the .graph format. Should be called
     * before any other methods work.
     * @param file a FileInputStream of the .graph file
     * @throws Exception if file not found or error reading
     */

     // instance variables]
     private HashMap<Integer, HashSet<Integer>> aList;
     private Point[] points;

     public void initialize(FileInputStream file) throws Exception {
        // TODO: Implement initialize
        Scanner sc = new Scanner(file);
        int num_vertices = sc.nextInt();
        int num_edges = sc.nextInt();
        points = new Point[num_vertices];

        for (int i = 0; i < num_vertices; i++) {
            String name = sc.next();
            double lon = Double.parseDouble(sc.next());
            double lat = Double.parseDouble(sc.next());
            points[i] = new Point(lon, lat);
        }


        HashSet<Integer> test = new HashSet<Integer>();
        test.add(1);
        test.add(2);
        aList = new HashMap<Integer, HashSet<Integer>>(num_vertices);
        for (int i = 0; i < num_vertices; i++) {
            aList.put(i, new HashSet<Integer>());
        }

        for (int j = 0; j < num_edges; j++) {
            int start = sc.nextInt();
            int end = sc.nextInt();
            aList.get(start).add(end);
            aList.get(end).add(start);
            if (sc.hasNext() && !sc.hasNextInt()) {
                String name = sc.next();
            }

        }
        sc.close();
        //intialize instance variables
    }


    /**
     * Searches for the point in the graph that is closest in
     * straight-line distance to the parameter point p
     * @param p A point, not necessarily in the graph
     * @return The closest point in the graph to p
     */
    public Point nearestPoint(Point p) {
        Point nearestPoint = points[0];
        double min = p.distance(nearestPoint);
        for (int i = 1; i < points.length; i++) {
            if (p.distance(points[i]) < min) {
                min = p.distance(points[i]);
                nearestPoint = points[i];
            }
        }
        return nearestPoint;
    }


    /**
     * Calculates the total distance along the route, summing
     * the distance between the first and the second Points, 
     * the second and the third, ..., the second to last and
     * the last. Distance returned in miles.
     * @param start Beginning point. May or may not be in the graph.
     * @param end Destination point May or may not be in the graph.
     * @return The distance to get from start to end
     */
    public double routeDistance(List<Point> route) {
        double sum = 0.0;
        for (int i = 1; i < route.size(); i++) {
            sum += route.get(i).distance(route.get(i - 1));
        }
        return sum;

        }
    
    
    

    /**
     * Checks if input points are part of a connected component
     * in the graph, that is, can one get from one to the other
     * only traversing edges in the graph
     * @param p1 one point
     * @param p2 another point
     * @return true if p2 is reachable from p1 (and vice versa)
     */
    public boolean connected(Point p1, Point p2) {
        int indxp1 = IndexOf(p1);
        int indxp2 = IndexOf(p2);

        HashSet<Integer> visited = new HashSet<>();
        Queue<Integer> toExplore = new LinkedList<>();
        visited.add(indxp1);
        toExplore.add(indxp1);
       
        while (toExplore.size() > 0) {
            int curr = toExplore.remove();
            if (curr == indxp2) {
                return true;
            }

            for (int n : aList.get(curr)) {
                if (!visited.contains(n)) {
                    visited.add(n);
                    toExplore.add(n);
                }
            }

        }
        return false;

    }


    /**
     * Returns the shortest path, traversing the graph, that begins at start
     * and terminates at end, including start and end as the first and last
     * points in the returned list. If there is no such route, either because
     * start is not connected to end or because start equals end, throws an
     * exception.
     * @param start Beginning point.
     * @param end Destination point.
     * @return The shortest path [start, ..., end].
     * @throws InvalidAlgorithmParameterException if there is no such route, 
     * either because start is not connected to end or because start equals end.
     */
    public List<Point> route(Point start, Point end) throws InvalidAlgorithmParameterException {
        // TODO: Implement route
        int indxStart = IndexOf(start);
        int indxEnd = IndexOf(end);

        if (indxStart == indxEnd) {
            return new ArrayList<Point>();
        }

        Map<Integer, Double> distance = new HashMap<>();
        distance.put(indxStart, 0.0);
        //for (int i: aList.keySet()) {
          //  distance.put(i, Double.MAX_VALUE);
        //}

        Comparator<Integer> comp = new Comparator<Integer>() {
            @Override
            public int compare(Integer one, Integer two) {
                return Double.compare(distance.get(one), distance.get(two));
            }
        };

        //Comparator<Double> comp = (a, b) -> distance.get(a) - distance.get(b);       
        PriorityQueue<Integer> toExplore = new PriorityQueue<>(comp);
        toExplore.add(indxStart);
        Map<Point, Point> pointMap = new HashMap<>();
       
        while (toExplore.size() > 0) {
            int curr = toExplore.remove();
            for (int n: aList.get(curr)) {
                if(!distance.containsKey(n) || distance.get(n) > distance.get(curr) + points[curr].distance(points[n])) {
                    distance.put(n, distance.get(curr) + points[curr].distance(points[n]));
                    toExplore.add(n);
                    pointMap.put(points[n], points[curr]);

                }
                

            }
        }

        Point key = points[indxEnd];
        ArrayList<Point> ret = new ArrayList<Point>();
        ret.add(points[indxEnd]);
        while (!pointMap.get(key).equals(start)) {
            ret.add(pointMap.get(key));
            key = pointMap.get(key);
        }
        ret.add(points[indxStart]);
        Collections.reverse(ret);





        return ret;

        
    }


    private int IndexOf(Point p) {
        for (int i = 0; i < points.length; i++) {
            if (points[i].equals(p)) {
                return i;
            }
        }
        return -1;
    }


    
}
