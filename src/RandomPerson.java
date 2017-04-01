import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class RandomPerson extends Thread{
	boolean run = true;
	private static int personId=1;
	private int currentFloor;
	private int targetFloor;
	private Elevator elevator;
	protected Label id = new Label();
	boolean inside=false;
	private ArrayList<Elevator> elevators = new ArrayList<Elevator>();
	private ElevatorController EC;
	private FileOperation fileOperation;
	
	public RandomPerson(int current, int target, ArrayList<Elevator> elevators, ElevatorController EC,  FileOperation fileOperation){
		this.elevators = elevators;
		this.EC = EC;
		currentFloor = current;
		targetFloor = target;
		id.setText(String.valueOf(personId));
		id.setTextFill(Color.WHITE);
		id.setStyle("-fx-background-color: BROWN;  -fx-font: 10 System");
		personId++;
		this.fileOperation = fileOperation;
		
	}

	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}
	
	public void pickAnotherElevator(){
		if(targetFloor > currentFloor){
			try {
				setElevator(elevators.get(EC.callElevatorUP(currentFloor)));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else{
			try {
				setElevator(elevators.get(EC.callElevatorDOWN(currentFloor)));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void getIntoElevator() throws InterruptedException, OverCapacityException{
		if(elevator.getNowCarrying() >= elevator.getCapcity())
			throw new OverCapacityException(" Over Capacity!! ");
		if(elevator.getNow() == currentFloor && elevator.getStatus() == "Door is open" && !inside){
			elevator.queue.add(targetFloor);
			fileOperation.writeLog("Person "+id.getText()+" got into to Elevator "+elevator.getEID()+" at floor "+currentFloor);
			inside = true;
			Platform.runLater(new Runnable(){
				 
                @Override
                public void run() {
                	elevator.flowpane.getChildren().add(id);
        			
                }
            });
			
			elevator.incCarriedPeople();
			elevator.incNowCarrying();
			elevator.capacityImg.setImage(elevator.greyCapacity);
		}
		else{
			sleep(10);
		}
	}
	
	public void getOutElevator() throws InterruptedException{
		if(elevator.getNow() == targetFloor && elevator.getStatus() == "Door is open" && inside){
			inside = false;
			Platform.runLater(new Runnable(){
				 
                @Override
                public void run() {
                	elevator.flowpane.getChildren().remove(id);
                }
            });
			
			elevator.descNowCarrying();
			elevator.capacityImg.setImage(elevator.greyCapacity);
			run = false;
		}
		else{
			sleep(10);
		}
	}
	
	public void run() {
		
		while(run){
			try {
				getOutElevator();
				if(elevator.capacityImg.getImage().equals(elevator.greyCapacity))
					getIntoElevator();
				RandomPerson.sleep(100);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (OverCapacityException e) {
				elevator.capacityImg.setImage(elevator.redCapacity);
				fileOperation.writeLog("Elevator "+elevator.getEID()+" over capacity!!");
				pickAnotherElevator();
			}
			
		}
		fileOperation.writeLog("Person "+this.id.getText()+" got out from Elevator "+elevator.getEID()+" at floor "+targetFloor);
	}
}
