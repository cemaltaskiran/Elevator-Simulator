import java.util.ArrayList;

public class ElevatorController {
	ArrayList<Elevator> elevators;
	public ElevatorController(ArrayList<Elevator> elevators){
		this.elevators = elevators;
	}
	
	public synchronized int callElevatorUP(int floor) throws InterruptedException{
		ArrayList<Integer> idleList = new ArrayList<Integer>();
		ArrayList<Integer> directionList = new ArrayList<Integer>();
		int i=0;
		boolean found = false;
		
		while(i < elevators.size() && !found){
			if(elevators.get(i).queue.contains(floor)){
				found=true;
			}
			i++;
		}
		if(!found){
			for(i=0;i<elevators.size();i++){
				if(elevators.get(i).getStatus() == "Idle")
					idleList.add(i);
			}
			if(!idleList.isEmpty()){
				int minDistanceToFloor;
				minDistanceToFloor = idleList.get(0).intValue();
				for(i=1;i<idleList.size();i++){
					if(Math.abs(elevators.get(minDistanceToFloor).getNow() - floor) > Math.abs(elevators.get(idleList.get(i).intValue()).getNow() - floor)){
						minDistanceToFloor = idleList.get(i).intValue();
					}
				}
				found = true;
				elevators.get(minDistanceToFloor).queue.add(floor);
				return minDistanceToFloor;
			}
		}
		else{
			for(i=0;i<elevators.size();i++){
				if(elevators.get(i).getNow() < floor)
					directionList.add(i);	
			}
			if(!directionList.isEmpty()){
				int minDistanceToFloor;
				minDistanceToFloor = directionList.get(0).intValue();
				for(i=1;i<directionList.size();i++){
					if(Math.abs(elevators.get(minDistanceToFloor).getNow() - floor) > Math.abs(elevators.get(directionList.get(i).intValue()).getNow() - floor)){
						minDistanceToFloor = directionList.get(i).intValue();
					}
				}
				found = true;
				elevators.get(minDistanceToFloor).queue.add(floor);
				return minDistanceToFloor;
			}
		}
		int minQueueSize=0;
		for(i=1;i<elevators.size();i++){
			if(elevators.get(minQueueSize).queue.size() > elevators.get(i).queue.size())
				minQueueSize = i;
		}
		found = true;
		elevators.get(minQueueSize).queue.add(floor);
		return minQueueSize;

	}
	
	public synchronized int callElevatorDOWN(int floor){
		ArrayList<Integer> idleList = new ArrayList<Integer>();
		ArrayList<Integer> directionList = new ArrayList<Integer>();
		int i=0;
		boolean found = false;
		
		while(i < elevators.size() && !found){
			if(elevators.get(i).queue.contains(floor))
				found=true;
			i++;
		}
		if(!found){
			for(i=0;i<elevators.size();i++){
				if(elevators.get(i).getStatus() == "Idle")
					idleList.add(i);
			}
			if(!idleList.isEmpty()){
				int minDistanceToFloor;
				minDistanceToFloor = idleList.get(0).intValue();
				for(i=1;i<idleList.size();i++){
					if(Math.abs(elevators.get(minDistanceToFloor).getNow() - floor) > Math.abs(elevators.get(idleList.get(i).intValue()).getNow() - floor)){
						minDistanceToFloor = idleList.get(i).intValue();
					}
				}
				found = true;
				elevators.get(minDistanceToFloor).queue.add(floor);
				return minDistanceToFloor;
			}
		}
		else{
			for(i=0;i<elevators.size();i++){
				if(elevators.get(i).getNow() > floor)
					directionList.add(i);	
			}
			if(!directionList.isEmpty()){
				int minDistanceToFloor;
				minDistanceToFloor = directionList.get(0).intValue();
				for(i=1;i<directionList.size();i++){
					if(Math.abs(elevators.get(minDistanceToFloor).getNow() - floor) > Math.abs(elevators.get(directionList.get(i).intValue()).getNow() - floor)){
						minDistanceToFloor = directionList.get(i).intValue();
					}
				}
				found = true;
				elevators.get(minDistanceToFloor).queue.add(floor);
				return minDistanceToFloor;
			}
		}
		int minQueueSize=0;
		for(i=1;i<elevators.size();i++){
			if(elevators.get(minQueueSize).queue.size() > elevators.get(i).queue.size())
				minQueueSize = i;
		}
		found = true;
		elevators.get(minQueueSize).queue.add(floor);
		return minQueueSize;
	}
	
	public void panelButtonPress(int floor, int elevatorId){
		if(!elevators.get(elevatorId).queue.contains(floor)){
			elevators.get(elevatorId).queue.add(floor);
		}
	}
	
	
}
