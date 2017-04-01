import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class ManuelPerson extends Thread{
	boolean run = true;
	private static int personId=1;
	private int currentFloor;
	private Elevator elevator;
	protected Label id = new Label();
	boolean inside=false;
	private FileOperation fileOperation;

	
	public ManuelPerson(Elevator elevator,  FileOperation fileOperation){
		this.elevator = elevator;
		currentFloor = elevator.getNow();
		id.setText(String.valueOf(personId));
		id.setTextFill(Color.WHITE);
		id.setStyle("-fx-background-color: BROWN;  -fx-font: 10 System");
		this.fileOperation = fileOperation;
		id.setOnMouseClicked(e->{
			try {
				getOutElevator();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		personId++;
	}

	public void getIntoElevator() throws InterruptedException, OverCapacityException{
		if(elevator.getNowCarrying() >= elevator.getCapcity())
			throw new OverCapacityException(" Over Capacity!! ");
		if(elevator.getNow() == currentFloor && (elevator.getStatus().equals("Door is open") || elevator.getStatus().equals("Idle")) && !inside){
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
			if(inside && (elevator.getStatus().equals("Idle") || elevator.getStatus().equals("Door is open"))){
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
	}
	public void run() {
		while(run){
			try {
				if(!inside && elevator.capacityImg.getImage().equals(elevator.greyCapacity))
					getIntoElevator();
				ManuelPerson.sleep(100);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (OverCapacityException e) {
				elevator.capacityImg.setImage(elevator.redCapacity);
				fileOperation.writeLog("Elevator "+elevator.getEID()+" over capacity!!");
			}
			
		}
		fileOperation.writeLog("Person "+this.id.getText()+" got out from Elevator "+elevator.getEID()+" at floor "+elevator.getNow());
		
	}
}
