import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Elevator {
    private final int id;
    private Direction direction;
    private Status status;
    private int currentFloor;
    private int capacity;
    private final int maxCapacity;
    private Map<Direction, Set<Integer>> floorsToVisit;
    private final Lock lock;

    public Elevator(int maxCapacity, int id, Lock lock){
        this(Direction.IDLE, Status.IDLE, 0, 0, maxCapacity, id, lock);
    }

    public Elevator(Direction direction, Status status, int currentFloor, int capacity, int maxCapacity, int id, Lock lock) {
        this.direction = direction;
        this.status = status;
        this.currentFloor = currentFloor;
        this.capacity = capacity;
        this.maxCapacity = maxCapacity;
        this.floorsToVisit = new HashMap<>();
        Arrays.stream(Direction.values()).forEach(v -> floorsToVisit.put(v, new HashSet<>()));
        this.id = id;
        this.lock = lock;
        GlobalConfig.getExecutor().submit(this::move);
    }

    public Direction getDirection() {
        return direction;
    }

    public Status getStatus() {
        return status;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getId() {
        return id;
    }

    public Map<Direction, Set<Integer>> getFloorsToVisit() {
        return floorsToVisit;
    }
    private void move(){
        while (true) {
            for (var dir : Direction.values()) {
                if(!floorsToVisit.get(dir).isEmpty()){
                    direction = dir;
                    status = Status.MOVING;
                    while(!floorsToVisit.get(direction).isEmpty()){
                        if(floorsToVisit.get(direction).remove(currentFloor)) System.out.println("Dropped at " + currentFloor);;
                        if(floorsToVisit.get(direction).isEmpty()) continue;
                        System.out.println(Thread.currentThread().getName() + " Elevator : " + id + " is at floor " + currentFloor);
                        if(direction == Direction.UP){
                            currentFloor++;
                        } else if (direction == Direction.DOWN){
                            currentFloor--;
                        }
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    direction = Direction.IDLE;
                    status = Status.IDLE;
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addFloor(int floor){
        if(currentFloor <= floor) floorsToVisit.get(Direction.UP).add(floor);
        else floorsToVisit.get(Direction.DOWN).add(floor);
    }
}
