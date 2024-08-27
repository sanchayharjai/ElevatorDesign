import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        Elevator elevator = new Elevator(50, 1);
//        elevator.addFloor( 10);
//        elevator.addFloor( 5);
//        new Thread(() -> elevator.move()).start();
//        TimeUnit.SECONDS.sleep(6);
//        elevator.addFloor( 5);
//
//        TimeUnit.SECONDS.sleep(6);
//        elevator.addFloor( 10);
        ElevatorSystem elevatorSystem = new ElevatorSystem(3);
        elevatorSystem.requestElevator(new Request(10, Direction.UP));

        elevatorSystem.requestElevator(new Request(8, Direction.UP));

        elevatorSystem.requestElevator(new Request(7, Direction.UP));

        elevatorSystem.requestElevator(new Request(9, Direction.UP));

        elevatorSystem.requestElevator(new Request(3, Direction.UP));

        elevatorSystem.requestElevator(new Request(4, Direction.UP));

        elevatorSystem.requestElevator(new Request(3, Direction.UP));

        elevatorSystem.requestElevator(new Request(4, Direction.UP));
        TimeUnit.SECONDS.sleep(7);
        elevatorSystem.requestElevator(new Request(1, Direction.UP));

        elevatorSystem.requestElevator(new Request(1, Direction.UP));
        elevatorSystem.requestElevator(new Request(1, Direction.UP));
        elevatorSystem.requestElevator(new Request(1, Direction.UP));

        elevatorSystem.requestElevator(new Request(2, Direction.UP));


        elevatorSystem.requestElevator(new Request(2, Direction.UP));
        TimeUnit.SECONDS.sleep(10);

        elevatorSystem.requestElevator(new Request(10, Direction.UP));
        TimeUnit.SECONDS.sleep(20);

        elevatorSystem.requestElevator(new Request(5, Direction.UP));
        GlobalConfig.getExecutor().shutdown();
    }
}