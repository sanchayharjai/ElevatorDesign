public class ElevatorSystem {

    Controller controller;
    public ElevatorSystem(int elevators){
        controller = new Controller(elevators);
        GlobalConfig.getExecutor().submit(controller::processRequest);
    }
    public void requestElevator(Request request){
        controller.addRequest(request);
    }
}
