import java.util.*;
import java.util.stream.Collectors;

public class SecurityRoutine extends SecurityRoutineBase {

    /* Implement all the necessary methods here */
    static class Vertex {
        private final AreaBase area;
        private List<Edge> adjNode;
        private int inDegree = 0;

        public Vertex(AreaBase area) {
            this.area = area;
            adjNode = new ArrayList<>();
        }

        public void addAdjNode(Edge edge) {
            adjNode.add(edge);
        }

        public AreaBase getArea() {
            return this.area;
        }

        public void addInDegree() {
            inDegree++;
        }
    }

    static class Edge {
        private final Vertex end;

        public Edge(Vertex end) {
            this.end = end;
        }

        public Vertex getEnd() {
            return this.end;
        }
    }

    private Map<AreaBase, Vertex> vertices = new LinkedHashMap<>();
    //private List<Edge> edges = new ArrayList<>();



    @Override
    public AreaBase insertArea(AreaBase area) {
        Vertex v = new Vertex(area);
        vertices.put(area, v);
        return area;
    }

    @Override
    public void addOrder(AreaBase area1, AreaBase area2) {
        if (vertices.containsKey(area2) && vertices.containsKey(area1)) {
            Edge edge = new Edge(vertices.get(area2));
            vertices.get(area1).addAdjNode(edge);
            vertices.get(area2).addInDegree();
            //System.out.println("success");
        } else {
            System.out.println("error: Vertex does not exist");
        }
    }

    @Override
    public List<AreaBase> calculateTotalOrder() {
        int count = 0;
        List<AreaBase> output = new ArrayList<>();
        Queue<Vertex> vertexQueue = new LinkedList<>();
        Collection<Vertex> vertexList = vertices.values();
/*        System.out.println("______________");
        for (Vertex v: vertexList) {
            System.out.println(v.getArea().getId());
            System.out.println(v.inDegree);
            System.out.println(v.adjNode);
            System.out.println("______________");
        }*/

        for (Vertex vertex: vertexList) {
            if (vertex.inDegree == 0) {
                vertexQueue.add(vertex);
            }
        }
        while (vertexQueue.size() != 0) {
            Vertex vertex = vertexQueue.poll();
            //System.out.println(vertex.getArea().getId());
            output.add(vertex.getArea());
            count++;
            for (Edge edge: vertex.adjNode) {
                if (--edge.getEnd().inDegree == 0) {
                    vertexQueue.add(edge.getEnd());
                }
            }
        }
        //System.out.println(count);
        if (count != vertices.size()) {
            //System.out.println("Graph contains circle");
            return null;
        };
        return output;
    }
}
