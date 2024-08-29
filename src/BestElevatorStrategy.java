import java.util.List;

public enum BestElevatorStrategy implements BestElevatorStrategyInterface {
    RANDOM{
        @Override
        public Elevator getBestElevator(List<Elevator> list) {
            return list.get((int)(Math.random()*list.size()));
        }
    }
}
