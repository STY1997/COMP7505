public class Plane extends PlaneBase {

    public Plane(String planeNumber, String time) {
        super(planeNumber, time);
    }

    @Override
    public int compareTo(PlaneBase planeBase) {
        String[] thisPlaneTime = this.getTime().split(":");
        int hour1InMinutes = Integer.parseInt(thisPlaneTime[0]) * 60;
        int time1Minutes = Integer.parseInt(thisPlaneTime[1]);
        int planeTime1 = hour1InMinutes + time1Minutes;
        //int planeTime1 = Integer.parseInt(this.getTime().replace(":", ""));

        //int planeTime2 = Integer.parseInt(planeBase.getTime().replace(":", ""));

        String[] PlaneTime = planeBase.getTime().split(":");
        int hour2InMinutes = Integer.parseInt(PlaneTime[0]) * 60;
        int time2Minutes = Integer.parseInt(PlaneTime[1]);
        int planeTime2 = hour2InMinutes + time2Minutes;

        String planeNumber1 = this.getPlaneNumber();
        String planeNumber2 = planeBase.getPlaneNumber();

        if (planeTime1 != planeTime2) {
            return Integer.compare(planeTime1, planeTime2);
        }
        return Integer.compare(planeNumber1.compareTo(planeNumber2), 0);

    }
    /* Implement all the necessary methods of Plane here */
}
