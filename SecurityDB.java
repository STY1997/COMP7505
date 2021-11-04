public class SecurityDB extends SecurityDBBase {
    private HashTable hashTable = new HashTable(
            this.getNumPassengersPerPlane()*this.getNumPlanes());

    /**
     * Creates an empty hashtable and a variable to count non-empty elements.
     *
     * @param numPlanes             number of planes per day
     * @param numPassengersPerPlane number of passengers per plane
     */
    public SecurityDB(int numPlanes, int numPassengersPerPlane) {
        super(numPlanes, numPassengersPerPlane);
    }

    /* Implement all the necessary methods here */
    @Override
    public int calculateHashCode(String key) {
        return hashTable.calculateHashCode(key);
    }

    @Override
    public int size() {
        return hashTable.getSize();
    }

    @Override
    public String get(String passportId) {
        return hashTable.get(passportId);
    }

    @Override
    public boolean remove(String passportId) {
        return hashTable.remove(passportId);
    }

    @Override
    public boolean addPassenger(String name, String passportId) {
        return hashTable.addPassenger(name, passportId);
    }

    @Override
    public int count() {
        return hashTable.getCount();
    }

    @Override
    public int getIndex(String passportId) {
        return hashTable.getIndex(passportId);
    }


}

/* Add any additional helper classes here */
class HashTable{
    private int count;
    private Passenger[] passengers;
    private int size;
    private int maxSize;


    public HashTable(int passengerNum){
        this.count = 0;
        this.size = countPrime(passengerNum);
        this.passengers = new Passenger[size];
        this.maxSize = 1021;
    }

    /**
     * Return the closest prime number larger than num
     * @param num number of passengers
     * @return ans
     */
    private int countPrime(int num){
        int ans = num + 1;
        while(!isPrime(ans)){
            ans++;
        }
        return ans;
    }

    private boolean isPrime(int n) {
        if(n < 2) return false;
        for(int i = 2; i < Math.sqrt(n); i++)
            if(n%i == 0) return false;
        return true;
    }


    /**
     * Return the current size of the hashtable (include null);
     * @return size
     */
    public int getSize(){
        return size;
    }

    public int calculateHashCode(String key){
        int ans = 0;
        int temp = 0;
        for(int i = 0; i < key.length(); i++){
            int code = key.charAt(i);
            ans = ans + code + temp + 1;
            temp = temp + code;
        }
        return ans;
    }

    public boolean addPassenger(String name, String passportId){
        int hashCode = calculateHashCode(passportId)%getSize();
        Passenger passenger = new Passenger(name, passportId);
        //检测乘客合法性
        for (Passenger value : passengers) {
            if (value != null){
                if (passenger.doesThisPassengerHasProb(value)) {
                    System.err.print("suspicious");
                    return false;
                }
                if (value.getTimes() >= 5){
                    System.err.print("suspicious");
                    return false;
                }
                if (value.equals(passenger)){
                    value.addOneTime();
                    return true;
                }
            }
        }
        if (passengers[hashCode] == null){
            this.passengers[hashCode] = passenger;
            this.passengers[hashCode].addOneTime();
            //System.out.println(this.passengers[hashCode].getTimes());
            count++;
            if (getCount() >= passengers.length){
                resize();
            }
            //System.out.print("success1");
            return true;
        } else {
            //linear Probing
            int flag = 0;
            while (passengers[hashCode] != null && flag < 2){
                hashCode++;
                //when index is larger than the size of hashtable, set index as 0
                if (hashCode >= passengers.length){
                    hashCode = 0;
                    flag++;
                }
                //hashCode = hashCode%passengers.length;
            }

            passengers[hashCode] = passenger;
            passengers[hashCode].addOneTime();
            count++;

            //System.out.println(this.passengers[hashCode].getTimes());

            if (getCount() >= passengers.length){
                resize();
            }
            //System.out.print("success2");
            return true;
        }
    }

    public String get(String passportID){
        int index = getIndex(passportID);
        if (index == -1){
            return null;
        }
        return passengers[index].getName();
    }

    public boolean remove(String passportID){
        int hashCode = calculateHashCode(passportID);
        int index = hashCode%getSize();
        int flag = 0;
        while (flag < 2 && passengers[index] != null &&
                !passengers[index].getPassport().equals(passportID)){
            index++;
            if (index == passengers.length){
                index = 0;
                flag++;
            }
        }
        if (passengers[index] == null){
            return false;
        }
        if (passengers[index].getPassport().equals(passportID)){
            passengers[index] = null;
            count--;
            return true;
        }
        return false;
    }

    public int getIndex(String passportID){
        //System.out.println(getSize());
        //System.out.println(passengers.length);
        int hashCode = calculateHashCode(passportID);
        int index = hashCode%getSize();
        //System.out.println(index);

        for (int i = index; i < getSize(); i++){
            if (this.passengers[i] != null){
                if (this.passengers[i].getPassport().equals(passportID)){
                    //System.out.println(i);
                    return i;
                }
            }
        }
        //System.out.println("______");
        for (int i = 0; i < index; i++){
            if (this.passengers[i] != null){
                if (this.passengers[i].getPassport().equals(passportID)){
                    return i;
                }
            }
        }
        return -1;


    }

    /**
     * Return the number of passengers in hashtable
     * @return count
     */
    public int getCount(){
        return count;
    }



    /**
     * Resize the hashtable to 1021.
     */
    private void resize(){
        Passenger[] newPassengers = new Passenger[maxSize];
        int oldSize = getSize();
        for (int i = 0; i < oldSize; i++){
            add(passengers[i], newPassengers);
        }
        size = this.maxSize;
        passengers = newPassengers;
    }

    /**
     * Add passengers from old hashtable to new table
     * @param passenger
     * @param newList
     */
    private void add(Passenger passenger, Passenger[] newList){
        int index = calculateHashCode(passenger.getPassport())%maxSize;

        if (newList[index] == null){
            newList[index] = passenger;
        } else {
            int flag = 0;
            while (flag < 2 && newList[index] != null){
                index++;
                if (index >= maxSize){
                    index = 0;
                    flag++;
                }
            }
            if (newList[index] != null){
                System.out.print("Error!");
            }
            newList[index] = passenger;
        }
    }
}

class Passenger{
    private String name;
    private String passport;
    private int times = 0;

    Passenger(String name, String passport){
        super();
        this.name = name;
        this.passport = passport;
    }

    String getName(){
        return this.name;
    }

    String getPassport(){
        return this.passport;
    }

    int getTimes(){
        return this.times;
    }

    void addOneTime(){
        times++;
    }

    boolean doesThisPassengerHasProb(Passenger passenger){
        return this.getPassport().equals(passenger.getPassport()) &&
                !this.getName().equals(passenger.getName());
    }

    boolean equals(Passenger passenger){
        return this.getPassport().equals(passenger.getPassport()) &&
                this.getName().equals(passenger.getName());
    }
}