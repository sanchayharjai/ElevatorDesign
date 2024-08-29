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

    public Controller(int elevators){
        bestElevatorStrategy = BestElevatorStrategy.RANDOM;
        elevatorList = new ArrayList<>();
        for(int i = 0; i < elevators; i++) {
            Lock l = new ReentrantLock();
            elevatorList.add(new Elevator(100, i, l));
        }
        externalRequests = new LinkedBlockingQueue<>();
        lock = new ReentrantLock();
    }
    private void requestElevator(Request request){
        Elevator bestElevator;
        bestElevator = bestElevatorStrategy.getBestElevator(elevatorList);
        bestElevator.addFloor(request.floor);
        System.out.println(request + " " + " added to Elevator " + bestElevator.getId());

    }

    public void processRequest(){
        while(true){
            Request request = null;
            try {
                request = externalRequests.take(); // Blocks until a request is available
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("got request " + request);
            requestElevator(request);
//            System.out.println(Thread.currentThread().getName() + " process thread");
        }
    }
    public void addRequest(Request request){
        externalRequests.offer(request);

    }
}
