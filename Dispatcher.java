public class Dispatcher extends DispatcherBase {

    private NodeList planeList = new NodeList();


    @Override
    public int size() {
        return planeList.getSize(planeList.getHead());
    }

    @Override
    public void addPlane(String planeNumber, String time) {
        Node plane = new Node(planeNumber, time);
        planeList.addNode(plane);
    }

    @Override
    public String allocateLandingSlot(String currentTime) {
        Node plane = planeList.getNodeByTime(currentTime);
        if (plane != null) {
            planeList.removeFirstNode();
            return plane.getPlaneNumber();
        }
        return null;
    }

    @Override
    public String emergencyLanding(String planeNumber) {
        Node plane = planeList.getNodeByPlaneNumber(planeNumber);
        if (plane != null) {
            planeList.removeNode(plane);
            return plane.getPlaneNumber();
        }
        return null;
    }

    @Override
    public boolean isPresent(String planeNumber) {
        return planeList.getNodeByPlaneNumber(planeNumber) != null;
    }

    /* Implement all the necessary methods of Dispatcher here */



}

/* Add any additional helper classes here */
class NodeList {

    private Node head = new Node(null, null);

    public void addNode(Node node) {
        Node temp = head;
        Plane plane = new Plane(node.planeNumber, node.time);
        while (temp.next != null) {
            if (plane.compareTo(new Plane(temp.next.planeNumber, temp.next.time)) < 0) {
                break;
            }
            temp = temp.next;
        }
        node.next = temp.next;
        temp.next = node;
    }

    public void printLinkedList(Node head) {
        if (head.next == null) {
            System.out.println("empty list");
            return;
        }
        Node temp = head.next;
        while (temp != null) {
            System.out.println(temp);
            temp = temp.next;
        }
    }

    public Node getNodeByPlaneNumber(String planeNumber) {
        Node temp = head;
        while (temp.next != null) {
            if (temp.next.planeNumber.equals(planeNumber)) {
                return temp.next;
            }
            temp = temp.next;
        }
        return null;
    }

    public void removeNode(Node node) {
        Node temp = head;
        while (temp.next != null) {
            if (node.planeNumber.equals(temp.next.planeNumber)) {
                temp.next = temp.next.next;
                break;
            }
            temp = temp.next;
        }
        if (temp.next == null) {
            System.out.println("");
        }
    }

    public void removeFirstNode() {
        Node temp = head;
        if (temp.next == null) {
            System.out.println("The list is empty");
        } else {
            temp.next = temp.next.next;
        }
    }


    public Node getNodeByTime(String time) {
        Node temp = head;
        if (temp.next == null) {
            return null;
        }
        String[] currentTime = time.split(":");
        int hour = Integer.parseInt(currentTime[0]) * 60;
        int time1Minutes = Integer.parseInt(currentTime[1]);
        int current = hour + time1Minutes;
        String[] PlaneTime = temp.next.time.split(":");
        int hour2 = Integer.parseInt(PlaneTime[0]) * 60;
        int time2Minutes = Integer.parseInt(PlaneTime[1]);
        int planeLandTime = hour2 + time2Minutes;
        if (planeLandTime - current <= 5) {
            return temp.next;
        }
        return null;
    }

    public int getSize(Node head) {
        if (head.next == null) {
            return 0;
        }
        int size = 0;
        Node temp = head.next;
        while (temp != null) {
            size = size + 1;
            temp = temp.next;
        }
        return size;
    }

    public Node getHead() {
        return head;
    }

}

class Node{
    String planeNumber;
    String time;
    Node next;

    Node(String planeNumber, String time) {
        super();
        this.planeNumber = planeNumber;
        this.time = time;
        //this.next = next;
    }

    String getPlaneNumber() {
        return this.planeNumber;
    }

    @Override
    public String toString() {
        return "Node{" +
                "planeNumber='" + planeNumber + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
