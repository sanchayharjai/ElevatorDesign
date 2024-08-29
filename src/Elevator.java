import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
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
    private MoveStrategy moveStrategy;

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
        moveStrategy = MoveStrategy.SCAN;
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
        while (!Thread.currentThread().isInterrupted()) {
            moveStrategy.move(this);
        }
    }

    public void addFloor(int floor){
        if(currentFloor <= floor) floorsToVisit.get(Direction.UP).add(floor);
        else floorsToVisit.get(Direction.DOWN).add(floor);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }
}
