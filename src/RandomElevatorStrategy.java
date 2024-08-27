import java.util.List;

public class RandomElevatorStrategy implements BestElevatorStrategy{

    @Override
    public Elevator getBestElevator(List<Elevator> list) {
        return list.get((int)(Math.random()*list.size()));
    }
}
