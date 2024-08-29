import java.util.concurrent.TimeUnit;

public enum MoveStrategy implements MoveStrategyInterface {
    SCAN {
        @Override
        public void move(Elevator elevator) {
            for (var dir : Direction.values()) {
                if(!elevator.getFloorsToVisit().get(dir).isEmpty()){
                    elevator.setDirection(dir);
                    elevator.setStatus(Status.MOVING);
                    while(!elevator.getFloorsToVisit().get(elevator.getDirection()).isEmpty()){
                        if(elevator.getFloorsToVisit().get(elevator.getDirection()).remove(elevator.getCurrentFloor() )) System.out.println("Elevator : " + elevator.getId() + " Dropped at " + elevator.getCurrentFloor() );;
                        if(elevator.getFloorsToVisit().get(elevator.getDirection()).isEmpty()) continue;
                        System.out.println(Thread.currentThread().getName() + " Elevator : " + elevator.getId() + " is at floor " + elevator.getCurrentFloor() );
                        if(elevator.getDirection() == Direction.UP){
                            elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
                        } else if (elevator.getDirection() == Direction.DOWN){
                            elevator.setCurrentFloor(elevator.getCurrentFloor()  - 1);
                        }
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    elevator.setDirection(Direction.IDLE);
                    elevator.setStatus(Status.IDLE);
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
