public class Request {
    int floor;
    Direction direction;

    public Request(int floor, Direction direction){
        this.direction = direction;
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "Request{" +
                "floor=" + floor +
                ", direction=" + direction +
                '}';
    }
}
