import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Controller {
    List<Elevator> elevatorList;
    Queue<Request> externalRequests;
    BestElevatorStrategy bestElevatorStrategy;
    private final Lock lock;
    private final Map<Integer, Lock> lockList;

    public Controller(int elevators){
        bestElevatorStrategy = new RandomElevatorStrategy();
        elevatorList = new ArrayList<>();
        lockList = new HashMap<>();
        for(int i = 0; i < elevators; i++) {
            Lock l = new ReentrantLock();
            elevatorList.add(new Elevator(100, i, l));
            lockList.put(i, l);
        }
        externalRequests = new ArrayDeque<>();
        lock = new ReentrantLock();
    }
    private void requestElevator(Request request){
        Elevator bestElevator;
        bestElevator = bestElevatorStrategy.getBestElevator(elevatorList);
        lockList.get(bestElevator.getId()).lock();
        try {
            bestElevator.addFloor(request.floor);
            System.out.println(request + " " + " added to Elevator " + bestElevator.getId());
        } finally {
            lockList.get(bestElevator.getId()).unlock();
        }
    }

    public void processRequest(){
        while(true){
            List<Request> requests = new ArrayList<>();
            lock.lock();
            try {
                while (!externalRequests.isEmpty()) requests.add(externalRequests.poll());
            } finally {
                lock.unlock();
            }
            requests.forEach(this::requestElevator);
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            System.out.println(Thread.currentThread().getName() + " process thread");
        }
    }
    public void addRequest(Request request){
        lock.lock();
        try {
            System.out.println(request + " added to queue");
            externalRequests.offer(request);
        } finally {
            lock.unlock();
        }
    }
}
