import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Elevator extends Thread {
	private boolean run = true;
	private int capcity;
	private int now;
	private String status;
	private int carriedPeople=0;
	private int nowCarrying=0;
	private int iddleTime=0;
	private int sizeOfAFloor;
	private int EID;
	private static int elevatorID=1;
	protected Group group = new Group();
	protected FlowPane flowpane = new FlowPane();
	protected Button stop;
	protected Button emergency;
	protected ElevatorQueue queue = new ElevatorQueue();
	protected Rectangle cabin = new Rectangle();
	protected ImageView capacityImg = new ImageView();
	protected Image greyCapacity = new Image(Elevator.class.getResourceAsStream("over_capacity_grey.png"));
	protected Image redCapacity = new Image(Elevator.class.getResourceAsStream("over_capacity_red.png"));
	private FileOperation fileOperation;
	
	public Elevator(int capacity, FileOperation fileOperation){
		this.capcity = capacity;
		flowpane.setVgap(0.5);
		flowpane.setHgap(0.5);
		stop = null;
		emergency = null;
		now = 0;
		status = "Idle";
		EID = elevatorID++;
		this.fileOperation = fileOperation;
		this.start();
	}

	public int getNow() {
		return now;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCarriedPeople() {
		return carriedPeople;
	}
	
	public void incCarriedPeople() {
		carriedPeople++;
	}

	public int getNowCarrying() {
		return nowCarrying;
	}
	
	public void incNowCarrying() {
		nowCarrying++;
	}
	
	public void descNowCarrying() {
		nowCarrying--;
	}

	public int getCapcity() {
		return capcity;
	}

	public int getIddleTime() {
		return iddleTime;
	}

	public void setIddleTime(int iddleTime) {
		this.iddleTime = iddleTime;
	}

	public void setSizeOfAFloor(int sizeOfAFloor) {
		this.sizeOfAFloor = sizeOfAFloor;
	}

	public boolean isStop() {
		if(stop==null)
			return false;
		return true;
	}

	public Button getStop() {
		return stop;
	}

	public void setStop(Button stop) {
		this.stop = stop;
	}

	public boolean isEmergency() {
		if(emergency==null)
			return false;
		return true;
	}

	public Button getEmergency() {
		return emergency;
	}

	public void setEmergency(Button emergency) {
		this.emergency = emergency;
	}

	public Rectangle getCabin() {
		return cabin;
	}

	public void setCabin(Rectangle cabin) {
		this.cabin = cabin;
	}

	public int getEID() {
		return EID;
	}

	public void openDoor() throws InterruptedException{
		int MILIS = 500;
		
		TranslateTransition tt2 = new TranslateTransition(Duration.millis(250), group);
        tt2.setByX(-(sizeOfAFloor-4)/2);
        
        ScaleTransition st = new ScaleTransition(Duration.millis(100), group);
        st.setByX(-1f);
        
        TranslateTransition tt3 = new TranslateTransition(Duration.millis(250), group);
        tt3.setByX((sizeOfAFloor-4)/2);
        
        ScaleTransition st2 = new ScaleTransition(Duration.millis(100), group);
        st2.setByX(1f);

        SequentialTransition sequentialTransition1 = new SequentialTransition(tt2,st);
        sequentialTransition1.play();
        status = "Door is opening";
        sleep(350);
        status = "Door is open";
        sleep(2000);
        status = "Door is closing";
        SequentialTransition sequentialTransition2 = new SequentialTransition(st2,tt3);
        sequentialTransition2.play();
        
        Elevator.sleep(MILIS+250);
        fileOperation.writeLog("Elevator "+EID+" is idle at floor "+now);
	}
	public void reLocateCabin(int targetFloor) throws InterruptedException{
		int MILIS = 500;
		
		if(now > targetFloor)
			status = "Moving down";
		else if(now < targetFloor)
			status = "Moving up";
		else
			status = "Idle";
		
		
		TranslateTransition tt = new TranslateTransition(Duration.millis(MILIS), group);
		if(status == "Moving down")
			tt.setByY(sizeOfAFloor);
		else if(status == "Moving up")
			tt.setByY(-sizeOfAFloor);
        tt.setAutoReverse(true);
        tt.play();
        if(status == "Moving down")
        	now--;
        else if(status == "Moving up")
        	now++;
        
        Elevator.sleep(MILIS+2);
        
        /*
         * 	Open door simulation
         */
        if(now == targetFloor){
	        
	        openDoor();
	        queue.poll();
        }
	}
	
	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public void run(){
		fileOperation.writeLog("Elevator "+EID+" is idle at floor "+now);
		while(run){
			if(!queue.isEmpty()){
				try {
					queue.sortByNow(now);
					reLocateCabin(queue.element());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else{
				try {
					Elevator.sleep(100);
					iddleTime++;
					status = "Idle";
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		System.out.println("Elevator end");
	}
}
