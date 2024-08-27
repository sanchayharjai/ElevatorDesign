import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Controller {
    List<Elevator> elevatorList;
    private final BlockingQueue<Request> externalRequests;
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
        externalRequests = new LinkedBlockingQueue<>();
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
            Request request = null;
            try {
                request = externalRequests.take(); // Blocks until a request is available
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            requestElevator(request);
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
