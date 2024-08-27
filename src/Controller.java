import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Controller {
    List<Elevator> elevatorList;
    Queue<Request> externalRequests;
    BestElevatorStrategy bestElevatorStrategy;
    private final Lock lock;

    public Controller(int elevators){
        bestElevatorStrategy = new RandomElevatorStrategy();
        elevatorList = new ArrayList<>();
        for(int i = 0; i < elevators; i++) elevatorList.add(new Elevator(100, i));
        externalRequests = new ArrayDeque<>();
        lock = new ReentrantLock();
    }
    private void requestElevator(Request request){
        Elevator bestElevator;
        lock.lock();
        try {
            bestElevator = bestElevatorStrategy.getBestElevator(elevatorList);
            bestElevator.addFloor(request.floor);
            System.out.println(request + " " + " added to Elevator " + bestElevator.getId());
        } finally {
            lock.unlock();
        }
        if(bestElevator != null) GlobalConfig.getExecutor().submit(bestElevator::setMove);
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
