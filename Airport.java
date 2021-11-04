import java.util.*;
import java.util.stream.Collectors;

public class Airport extends AirportBase {

    //private Graph graph = new Graph(getCapacity());
    private Graph2 graph2 = new Graph2(getCapacity());
    /**
     * Creates a new AirportBase instance with the given capacity.
     *
     * @param capacity capacity of the airport shuttles
     *                 (same for all shuttles)
     */
    public Airport(int capacity) {
        super(capacity);
    }

    public List<Shuttle> getShuttle() {
        return graph2.shuttles;
    }

    public List<Terminal> getTerminal() {
        return graph2.terminalSet;
    }


    @Override
    public TerminalBase opposite(ShuttleBase shuttle, TerminalBase terminal) {
        //return graph.opposite((Shuttle) shuttle, (Terminal) terminal);
        return graph2.opposite((Shuttle) shuttle, (Terminal) terminal);
    }

    @Override
    public TerminalBase insertTerminal(TerminalBase terminal) {
        //return graph.addTerminal((Terminal) terminal);
        return graph2.addNode((Terminal) terminal);
    }

    @Override
    public ShuttleBase insertShuttle(TerminalBase origin, TerminalBase destination, int time) {
        //return graph.addShuttle((Terminal) origin, (Terminal) destination, time);
        return graph2.addShuttle((Terminal) origin, (Terminal) destination, time);
    }

    @Override
    public boolean removeTerminal(TerminalBase terminal) {
        //return graph.removeTerminal((Terminal) terminal);
        return graph2.removeNode((Terminal) terminal);
    }

    @Override
    public boolean removeShuttle(ShuttleBase shuttle) {
        //return graph.removeShuttle((Shuttle) shuttle);
        return graph2.removeShuttle((Shuttle) shuttle);
    }

    @Override
    public List<ShuttleBase> outgoingShuttles(TerminalBase terminal) {
        //List<Shuttle> temp = graph.outgoingShuttles((Terminal) terminal);
        List<Shuttle> temp = graph2.outgoingShuffles((Terminal) terminal);
        return new ArrayList<>(temp);
    }

    @Override
    public Path findShortestPath(TerminalBase origin, TerminalBase destination) {
        return graph2.getShortestPath((Terminal) origin, (Terminal) destination);
    }

    @Override
    public Path findFastestPath(TerminalBase origin, TerminalBase destination) {
        return graph2.getFastestPath((Terminal) origin, (Terminal) destination);
    }

    /* Implement all the necessary methods of the Airport here */
    static class Graph2 {
        private List<Terminal> terminalSet = new ArrayList<>();
        private List<Shuttle> shuttles = new ArrayList<>();
        private int capacity;

        public Graph2(int capacity) {
            this.capacity = capacity;
        }

        public Terminal opposite(Shuttle shuttle, Terminal terminal) {
            if (!shuttles.contains(shuttle)) {
                return null;
            }
            if (!terminalSet.contains(terminal)) {
                return null;
            }
            if (!terminalSet.contains((Terminal) shuttle.getDestination())) {
                return null;
            }
            if (!terminalSet.contains((Terminal) shuttle.getOrigin())) {
                return null;
            }
            if (shuttle.getDestination().equals(terminal)) {
                return (Terminal) shuttle.getOrigin();
            }
            if (shuttle.getOrigin().equals(terminal)) {
                return (Terminal) shuttle.getDestination();
            }
            return null;
        }

        public List<Shuttle> outgoingShuffles(Terminal terminal) {
            List<Shuttle> shuttles1 = new ArrayList<>();

            for (Shuttle shuttle : shuttles) {
                if (shuttle.getDestination().equals(terminal) ||
                        shuttle.getOrigin().equals(terminal)) {
                    shuttles1.add(shuttle);
                }
            }

            //System.out.println(entry.getKey());
            //System.out.println(entry.getValue());

            return shuttles1;
        }

        public Terminal addNode(Terminal terminal) {
            terminalSet.add(terminal);
            return terminal;
        }

        public boolean removeNode(Terminal terminal) {
            if (!terminalSet.contains(terminal)) {
                return false;
            }
            //int size = terminal.adjNode.size();
            for (Map.Entry <Terminal, Integer> entry: terminal.adjNode.entrySet()) {
                entry.getKey().remove(terminal);
                //System.out.println(entry.getKey());
                //System.out.println(entry.getKey().adjNode);
                //System.out.println("______________");
            }
            shuttles.removeIf(s -> s.getOrigin().equals(terminal) || s.getDestination().equals(terminal));
            terminalSet.remove(terminal);
            //System.out.println(terminalSet.get(0).adjNode);
            return true;
        }

        public Shuttle addShuttle(Terminal origin, Terminal destination, int time) {
            for (Terminal terminal : terminalSet) {
                if (terminal.getId().equals(origin.getId())) {
                    terminal.addDestination(destination, time);
                }
                if (terminal.getId().equals(destination.getId())) {
                    terminal.addDestination(origin, time);
                }
            }
            Shuttle shuttle = new Shuttle(origin, destination, time);
            shuttle.setCapacity(capacity);
            shuttles.add(shuttle);
            return shuttle;
        }

        public boolean removeShuttle(Shuttle shuttle) {
            //System.out.println(shuttles.toString());
            if (!shuttles.contains(shuttle)) {
                return false;
            }
            Terminal origin = (Terminal) shuttle.getOrigin();
            Terminal destination = (Terminal) shuttle.getDestination();
            for (Terminal terminal : terminalSet) {
                if (terminal.equals(origin)) {
                    terminal.remove(destination);
                }
                if (terminal.equals(destination)) {
                    terminal.remove(origin);
                }
            }
            shuttles.remove(shuttle);
            return true;
        }


        public Path getFastestPath(Terminal origin, Terminal destination) {
            dijkstraHelper(origin);
            if (destination.getDistance() == Integer.MAX_VALUE) {
                return null;
            }
            List<Terminal> list = destination.getShortestPath();
            list.add(destination);
            reduceCapacity(list);
            List<TerminalBase> shortestPath = new ArrayList<>(list);
            //System.out.println(shortestPath.toString());
            Path path = new Path(shortestPath, destination.getDistance());
            //System.out.println(path.terminals.toString());
            return path;
        }

        public Path getShortestPath(Terminal origin, Terminal destination) {
            dijkstraHelper2(origin);
            if (destination.getWeight() == Integer.MAX_VALUE) {
                return null;
            }
            List<Terminal> list = destination.getShortestPath2();
            list.add(destination);
            reduceCapacity(list);
            List<TerminalBase> shortestPath = new ArrayList<>(list);
            //System.out.println(shortestPath.toString());
            Path path = new Path(shortestPath, destination.getTime());
            //System.out.println(path.terminals.toString());
            return path;
        }

        private void dijkstraHelper2(Terminal source) {
            source.setWeight(0);
            source.setDistance(0);
            Set<Terminal> settledNode = new HashSet<>();
            Set<Terminal> unsettledNode = new HashSet<>();
            unsettledNode.add(source);
            while (unsettledNode.size() != 0) {
                Terminal current = getLowestDistanceNode2(unsettledNode);
                //System.out.println(current.toString());
                unsettledNode.remove(current);
                for (Map.Entry<Terminal, Integer> entry: current.getAdjNode().entrySet()) {
                    Terminal adjNode = entry.getKey();
                    int weight = entry.getValue();
                    if (!settledNode.contains(adjNode)) {
                        calculateMinDistance2(adjNode, weight, current);
                        //calculateMinDistance(adjNode, weight, current);
                        unsettledNode.add(adjNode);
                    }
                }
                settledNode.add(current);
            }
        }

        private void reduceCapacity(List<Terminal> list) {
            //System.out.println("执行");
            //System.out.println(list.toString());
            //System.out.println("...............");
            for (int i = 1; i < list.size(); i++) {
                Terminal dest = list.get(i);
                Terminal ori = list.get(i - 1);
                //System.out.println(dest.toString());
                //System.out.println(ori.toString());
                //System.out.println("--------------");
                Iterator<Shuttle> temp = shuttles.iterator();
                while (temp.hasNext()) {
                    Shuttle s = temp.next();
                    if ((s.getOrigin().equals(dest) && s.getDestination().equals(ori)) ||
                            (s.getDestination().equals(dest) && s.getOrigin().equals(ori))) {
                        if (s.getCapacity() <= 0) {
                            temp.remove();
                        }
                        s.decreaseCapacity();
                        //System.out.println(s.toString());
                        //System.out.println("reduce one");
                    }
                }
            }
        }

        private void dijkstraHelper(Terminal source) {
            source.setDistance(0);
            Set<Terminal> settledNode = new HashSet<>();
            Set<Terminal> unsettledNode = new HashSet<>();
            unsettledNode.add(source);
            while (unsettledNode.size() != 0) {
                Terminal current = getLowestDistanceNode(unsettledNode);
                unsettledNode.remove(current);
                for (Map.Entry<Terminal, Integer> entry: current.getAdjNode().entrySet()) {
                    Terminal adjNode = entry.getKey();
                    int weight = entry.getValue();
                    if (!settledNode.contains(adjNode)) {
                        calculateMinDistance(adjNode, weight, current);
                        unsettledNode.add(adjNode);
                    }
                }
                settledNode.add(current);
            }
        }

        private Terminal getLowestDistanceNode(Set<Terminal> unsettledNodes) {
            Terminal lowestNode = null;
            int lowestDistance = Integer.MAX_VALUE;
            for (Terminal terminal: unsettledNodes) {
                int nodeDistance = terminal.getDistance();
                //System.out.println("==============");
                //System.out.println(nodeDistance);
                if (nodeDistance < lowestDistance) {
                    lowestDistance = nodeDistance;
                    lowestNode = terminal;
                }
            }
            return lowestNode;
        }

        private Terminal getLowestDistanceNode2(Set<Terminal> unsettledNodes) {
            //Set<Terminal> lowestNodeList = new HashSet<>();
            int lowestDistance = Integer.MAX_VALUE;
            Terminal result = null;
            for (Terminal terminal: unsettledNodes) {
                int nodeDistance = terminal.getWeight();
                if (nodeDistance < lowestDistance) {
                    lowestDistance = nodeDistance;
                    result = terminal;
                }
            }
            return result;
        }

        private void calculateMinDistance2(Terminal evaluation, int weight, Terminal source) {
            int distanceToSource = source.getWeight();
            int timeToSource = source.getTime();
            if (distanceToSource + 1 < evaluation.getWeight()) {
                evaluation.setTime(source.getWaitingTime() + weight + timeToSource);
                evaluation.setWeight(distanceToSource + 1);
                LinkedList<Terminal> shortestPath = new LinkedList<>(source.getShortestPath2());
                shortestPath.add(source);
                evaluation.setShortestPath2(shortestPath);
            }
        }

        private void calculateMinDistance(Terminal evaluation, int weight, Terminal source) {
            int distanceToSource = source.getDistance();
            if (distanceToSource + weight < evaluation.getDistance()) {
                evaluation.setDistance(distanceToSource + weight + source.getWaitingTime());
                LinkedList<Terminal> shortestPath = new LinkedList<>(source.getShortestPath());
                shortestPath.add(source);
                evaluation.setShortestPath(shortestPath);
            }

        }
    }

    static class Graph {
        private Map<Terminal, List<Shuttle>> graph1 = new HashMap<>();
        private Map<Terminal, List<Terminal>> graph2 = new HashMap<>();
        //private Map<Terminal, Map<Terminal, Integer>> graph3 = new HashMap<>();
        private List<Terminal> node = new ArrayList<>();
        private List<Shuttle> edge = new ArrayList<>();
        private int capacity;

        public Graph(int capacity) {
            this.capacity = capacity;
        }

        public Shuttle addShuttle(Terminal origin, Terminal destination, int time) {
            Shuttle newShuttle = new Shuttle(origin, destination, time);
            if (doesTerminalExist(origin) && doesTerminalExist(destination)) {
                graph1.get(origin).add(newShuttle);
                graph1.get(destination).add(newShuttle);
                addDestinationTerminal(newShuttle);
                edge.add(newShuttle);
                return newShuttle;
            }
            System.err.println("Cannot add this shuffle! It is illegal");
            return null;
        }

        public boolean removeShuttle(Shuttle shuttle) {
            if (!edge.contains(shuttle)){
                //System.out.println("wrong");
                return false;
            }
            Terminal origin = (Terminal) shuttle.getOrigin();
            Terminal destination = (Terminal) shuttle.getDestination();
            graph1.get(origin).remove(shuttle);
            graph1.get(destination).remove(shuttle);
            edge.remove(shuttle);
            //System.out.println("success");
            return true;
        }

        public Terminal addTerminal(Terminal terminal){
            graph1.put(terminal, new ArrayList<Shuttle>());
            graph2.put(terminal, new ArrayList<Terminal>());
            //graph3.put(terminal, new HashMap<Terminal, Integer>());
            node.add(terminal);
            return terminal;
        }

        public boolean removeTerminal(Terminal terminal){
            if (!doesTerminalExist(terminal)){
                return false;
            }
            //System.out.println(graph1.get(terminal).size());
            int size = graph1.get(terminal).size();
            for (int i = 0; i < size; i++){
                removeShuttle(graph1.get(terminal).get(0));
                //System.out.println(i + "time");
            }
            int size_ = graph2.get(terminal).size();
            for (int i = 0; i < size_; i++){
                Terminal target = graph2.get(terminal).get(0);
                graph2.get(target).remove(terminal);
            }
            //int size__ = graph3.get(terminal).size();
            node.remove(terminal);
            graph1.remove(terminal);
            graph2.remove(terminal);
            //graph3.remove(terminal);
            return true;
        }

        public Terminal opposite(Shuttle shuttle, Terminal terminal){
            if (!node.contains(terminal)){
                return null;
            }
            if (shuttle.getOrigin().getId().equals(terminal.getId())){
                return (Terminal) shuttle.getDestination();
            }
            if (shuttle.getDestination().getId().equals(terminal.getId())){
                return (Terminal) shuttle.getOrigin();
            }
            return null;
        }

        public List<Shuttle> outgoingShuttles(Terminal terminal){
            return graph1.get(terminal);
        }

        public Path findShortest(Terminal origin, Terminal destination){
            return null;
        }

        public Path findFastest(Terminal origin, Terminal destination){
            return null;
        }

        private void DijkstraHelper(Terminal origin){
            int time;
            Terminal currentNode = origin;
            List<Terminal> terminalList = new ArrayList<>();
            List<Terminal> unsetNodes = this.node;
            List<Terminal> settledNodes = new ArrayList<>();
            Map<Terminal, Terminal> previousNode = new HashMap<>();
            Map<Terminal, Integer> distance = new HashMap<>();

            unsetNodes.remove(origin);
            for (Terminal terminal : node) {
                distance.put(terminal, Integer.MAX_VALUE);
            }
            //distance.remove(origin);
            distance.put(origin, 0);
            for (Terminal terminal : node) {
                previousNode.put(terminal, null);
            }

        }


        private Terminal getSmallestWeightTerminal(Terminal currentTerminal, List<Terminal> settledNodes){
            List<Shuttle> shuttles = outgoingShuttles(currentTerminal);


            Terminal ans = null;
            int distance = Integer.MAX_VALUE;
            int currentDistance;
            for (Shuttle shuttle : shuttles) {
                Terminal o = (Terminal) shuttle.getDestination();
                Terminal t = (Terminal) shuttle.getOrigin();
                if (settledNodes.contains(o) || settledNodes.contains(t)){
                    continue;
                }
                currentDistance = shuttle.getTime() + shuttle.getOrigin().getWaitingTime();
                if (distance > currentDistance) {
                    distance = currentDistance;
                    if (shuttle.getOrigin().getId().equals(currentTerminal.getId())){
                        ans = (Terminal) shuttle.getDestination();
                    } else {
                        ans = (Terminal) shuttle.getOrigin();
                    }
                }
            }
            return ans;
        }

        private int calculateDistance(Terminal current, Terminal origin){
            return -1;
        }

        private void addDestinationTerminal(Shuttle shuttle){
            Terminal origin = (Terminal) shuttle.getOrigin();
            Terminal destination = (Terminal) shuttle.getDestination();
            graph2.get(origin).add(destination);
            graph2.get(destination).add(origin);
            //graph3.get(origin).put(destination, shuttle.getTime());
            //graph3.get(destination).put(origin, shuttle.getTime());
        }

        private boolean doesTerminalExist(Terminal terminal){
            return graph1.get(terminal) != null && graph2.get(terminal) != null;
        }
    }

    static class Terminal extends TerminalBase {
        /**
         * Creates a new TerminalBase instance with the given terminal ID
         * and waiting time.
         *
         * @param id          terminal ID
         * @param waitingTime waiting time for the terminal, in minutes
         */
        public Terminal(String id, int waitingTime) {
            super(id, waitingTime);
        }

        /* Implement all the necessary methods of the Terminal here */

        private int time = 0;
        private int weight = Integer.MAX_VALUE;
        private int distance = Integer.MAX_VALUE;
        private List<Terminal> shortestPath = new LinkedList<>();
        private List<Terminal> shortestPath2 = new LinkedList<>();
        Map<Terminal, Integer> adjNode = new HashMap<>();

        public void addDestination(Terminal destination, int distance){
            adjNode.put(destination, distance);
        }

        public void remove(Terminal destination) {
            adjNode.remove(destination);
        }

        public Map<Terminal, Integer> getAdjNode(){
            return this.adjNode;
        }


        public int getTime() {
            return this.time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getWeight(){
            return this.weight;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getDistance() {
            return this.distance;
        }

        public void setShortestPath(List<Terminal> shortestPath) {
            this.shortestPath = shortestPath;
        }

        public List<Terminal> getShortestPath() {
            return this.shortestPath;
        }

        public void setShortestPath2(List<Terminal> shortestPath2) {
            this.shortestPath2 = shortestPath2;
        }

        public List<Terminal> getShortestPath2() {
            return  this.shortestPath2;
        }



    }

    static class Shuttle extends ShuttleBase {
        /**
         * Creates a new ShuttleBase instance, travelling from origin to
         * destination and requiring 'time' minutes to travel.
         *
         * @param origin      origin terminal
         * @param destination destination terminal
         * @param time        time required to travel, in minutes
         */
        public Shuttle(TerminalBase origin, TerminalBase destination, int time) {
            super(origin, destination, time);
        }

        /* Implement all the necessary methods of the Shuttle here */
        private int capacity;

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public int getCapacity() {
            return capacity;
        }

        public void decreaseCapacity() {
            capacity--;
        }
    }

}
