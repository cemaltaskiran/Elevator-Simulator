import java.util.ArrayList;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.geometry.Insets;

public class Main extends Application {
	Stage window;
	Scene numbers, configration, simulation;
	private int numberOfFloors;
	private int numberOfElevators;
	private boolean simulate=false;
	ArrayList<Elevator> elevators = new ArrayList<Elevator>();
	FileOperation fileOperation = new FileOperation();
	
	
	public static void main(String args[]){
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception{
		
		window = primaryStage;
		// close everything when close program
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
            	fileOperation.flush();
            	fileOperation.close();
                Platform.exit();
                System.exit(0);
            }
        });
		/*
		 * 	Number scene GUI
		 */
		Label numOfFloorsLabel = new Label("Number of floors: ");
		numOfFloorsLabel.setPrefSize(120, 25);
		Label numOfElevatorsLabel = new Label("Number of elevators: ");
		numOfElevatorsLabel.setPrefSize(120, 25);
		
		NumberTextField numOfFloors = new NumberTextField();
		numOfFloors.setPrefSize(120, 25);
		NumberTextField numOfElevators= new NumberTextField();
		numOfElevators.setPrefSize(120, 25);
		numOfElevators.setText("2");
		numOfFloors.setText("10");
		
		numOfFloorsLabel.setLabelFor(numOfFloors);
		numOfElevatorsLabel.setLabelFor(numOfElevators);
		
		CheckBox simuCheck = new CheckBox("Automatic Simulation?");
		
		Button save = new Button("Save");
		save.setPrefSize(60, 25);
		
		EventHandler<ActionEvent> saveButtonPressed = new EventHandler<ActionEvent>(){

			public void handle(ActionEvent event) {
				int nElevator = Integer.parseInt(numOfElevators.getText());
				int nFloor = Integer.parseInt(numOfFloors.getText());
				
				if(nFloor == 0 || nElevator == 0){
					AlertBox.display("Error!","\"0\" is not valid!");
				}
				else if(nElevator > 4){
					AlertBox.display("Error!","You can set maximum 4 elevators!");
					numOfElevators.setText("4");
				}
				else{
					if(simuCheck.isSelected())
						simulate = true;
					numberOfFloors = nFloor;
					numberOfElevators = nElevator;
					configration = createConfigrationScene(numberOfElevators, numberOfFloors, elevators);
					window.setScene(configration);
					window.setWidth(200*numberOfElevators+50);
					window.setHeight(300);
					window.setTitle("Elevator configration");
					window.centerOnScreen();
				}
			}
		};
		save.setOnAction(saveButtonPressed);
		
		Pane layout1 = new Pane();
		/*
		 * 	Add components to scene
		 */
		GridPane gridpane = new GridPane();
		gridpane.setPrefSize(240, 80);
		gridpane.setLayoutX(30);
		gridpane.setLayoutY(20);
		gridpane.setVgap(10);
		gridpane.add(numOfFloorsLabel, 0, 0);
		gridpane.add(numOfFloors, 1, 0);
		gridpane.add(numOfElevatorsLabel, 0, 1);
		gridpane.add(numOfElevators, 1, 1);
		gridpane.add(simuCheck, 0, 2);
		gridpane.add(save, 1, 2);
		GridPane.setMargin(save, new Insets(0,0,0,60));
		gridpane.setAlignment(Pos.CENTER);

		layout1.getChildren().add(gridpane);
		
		numbers = new Scene(layout1, 300, 130);
		
		window.setScene(numbers);
		window.setTitle("Enter numbers");
		window.centerOnScreen();
		window.show();
	}
	/*
	 * 	This method creates cofingration scene
	 */
	public Scene createConfigrationScene(int numberOfElevators, int numberOfFloors, ArrayList<Elevator> elevators){
		int i;
		
		Scene configration;
		Pane layout2 = new Pane();
		
		GridPane gridpane2 = new GridPane();
		gridpane2.setHgap(10);
		gridpane2.setVgap(10);
		gridpane2.setPrefSize(200*numberOfElevators, 250);
		gridpane2.setAlignment(Pos.CENTER);
		gridpane2.setLayoutX(20);
		
		ArrayList<VBox> vboxes = new ArrayList<VBox>();
		ArrayList<GridPane> grids = new ArrayList<GridPane>();
		ArrayList<NumberTextField> capacities = new ArrayList<NumberTextField>();
		ArrayList<CheckBox> emergency = new ArrayList<CheckBox>();
		ArrayList<CheckBox> stop = new ArrayList<CheckBox>();
		
		
		for(i=0;i<numberOfElevators;i++){
			vboxes.add(new VBox());
			vboxes.get(i).getChildren().add(new Label("Elevator configration "+(i+1)));
			
			grids.add(new GridPane());
			
			grids.get(i).add(new Label("Capacity"), 0, 0);
			grids.get(i).add(new Label("Emergency button"), 0, 1);
			grids.get(i).add(new Label("Stop button"), 0, 2);
			
			capacities.add(new NumberTextField());
			capacities.get(i).setPrefSize(35, 25);
			capacities.get(i).setText("13");
			
			emergency.add(new CheckBox());
			
			stop.add(new CheckBox());
			
			grids.get(i).add(capacities.get(i), 1, 0);
			grids.get(i);
			GridPane.setMargin(capacities.get(i), new Insets(0,0,0,20));
			grids.get(i).add(emergency.get(i), 1, 1);
			GridPane.setMargin(emergency.get(i), new Insets(0,0,0,20));
			grids.get(i).add(stop.get(i), 1, 2);
			GridPane.setMargin(stop.get(i), new Insets(0,0,0,20));
			grids.get(i).setPadding(new Insets(50,0,0,0));
			
			vboxes.get(i).getChildren().add(grids.get(i));
			vboxes.get(i).setStyle("-fx-border-style: solid; -fx-border-color: #DADADA; -fx-background-color: #F4F4F4; -fx-border-radius: 5;");
			vboxes.get(i).setPadding(new Insets(10));
			gridpane2.add(vboxes.get(i), i, 0);
		}
		Button saveButton2 = new Button("Save");
		saveButton2.setOnAction(e->{
			int j=0;
			boolean valid=true;
			while(valid && j<numberOfElevators){
				 if(Integer.parseInt(capacities.get(j).getText()) == 0){
					 valid=false;
				 }
				 j++;
			}
			if(valid){
				for(j=0;j<numberOfElevators;j++){
					elevators.add(new Elevator(Integer.parseInt(capacities.get(j).getText()), fileOperation));
					if(emergency.get(j).isSelected())
						elevators.get(j).setEmergency(new Button("E"));	
					if(stop.get(j).isSelected())
						elevators.get(j).setStop(new Button("S"));	
				}
				try {
					SimulationScene simu = new SimulationScene();
					Thread myRunnableThread = new Thread(simu);
	                myRunnableThread.start();
					
					simulation = simu.createSimulationScene(numberOfElevators, numberOfFloors, elevators);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				window.setScene(simulation);
				window.setTitle("Elevator Simulation");
				window.setWidth(1300);
				window.setHeight(760);
				window.centerOnScreen();
			}
			else{
				AlertBox.display("Error!", "\"0\" is not valid!");
			}
			
		});
		
		gridpane2.add(saveButton2, 0, 1);
		
		layout2.getChildren().add(gridpane2);
		
		configration = new Scene(layout2);
		
		return configration;
		
	}
	class SimulationScene implements Runnable{
		ArrayList<Label> elevatorStats = new ArrayList<Label>();

		@Override
		public void run() {
			while(true){
				Platform.runLater(new Runnable(){
                    public void run() {
                        for(int i=0;i<elevatorStats.size()/5;i++){
                        	elevatorStats.get(i*5+0).setText(String.valueOf(elevators.get(i).getCarriedPeople())+" ("+String.valueOf(elevators.get(i).getNowCarrying())+")");
                        	elevatorStats.get(i*5+1).setText(String.valueOf(elevators.get(i).getNow()));
                        	elevatorStats.get(i*5+2).setText(elevators.get(i).queue.toString());
                        	elevatorStats.get(i*5+3).setText(String.valueOf(elevators.get(i).getIddleTime()/10)+" s");
                        	elevatorStats.get(i*5+4).setText(elevators.get(i).getStatus());
                        }
                    }
                });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    
                }
			}
			
		}
		
		public Scene createSimulationScene(int numberOfElevators, int numberOfFloors, ArrayList<Elevator> elevators) throws InterruptedException{
			int i,j;
			
			ArrayList<RandomPerson> randomPerson = new ArrayList<RandomPerson>();
			ArrayList<ManuelPerson> manuelPerson = new ArrayList<ManuelPerson>();
			ArrayList<HBox> personWaitCabins = new ArrayList<HBox>();
			ArrayList<Button> panelButtons = new ArrayList<Button>();
			ArrayList<Rectangle> floors = new ArrayList<Rectangle>();

			Scene simulation;
			ElevatorController EC = new ElevatorController(elevators);
			
			Pane layout = new Pane();// Main pane = layout
			layout.setPrefSize(1280, 720);
			
			HBox hbox = new HBox();// HBox for elevator control panel and simulation
			hbox.setPrefSize(1260, 700);
			hbox.setLayoutX(10);
			hbox.setLayoutY(10);
			
			layout.getChildren().add(hbox);
			
			String[] colors = {" rgba(255,0,0,.5)"," rgba(68,100,18,.5)","rgba(0,0,255,.5)","rgba(255,255,0,.5)"};
			
			VBox vbox = new VBox();
			vbox.setSpacing(5);
			vbox.setPrefSize(382, 700);
			vbox.setStyle("-fx-border-style: solid; -fx-border-color: transparent gray transparent transparent;");
			
			/*
			 * 	Elevator Control Panel
			 */
			
			for(i=0;i<numberOfElevators;i++){
				
				Pane newPane = new Pane();
				newPane.setStyle("-fx-background-color: "+colors[i]+";");
				
				GridPane newGrid = new GridPane();
				newGrid.setHgap(10);
				newGrid.setPrefHeight(75);
				if(!simulate){
					final Button button = new Button("Add person");
					button.setId(String.valueOf(i));
					button.setPrefHeight(20);
					button.setPadding(new Insets(0,3,0,3));
					button.setCursor(Cursor.HAND);
					button.setStyle("-fx-border-radius:2; -fx-border-style:solid; -fx-border-color:rgba(0,0,0,.3); -fx-background-color:rgba(0,0,0,.15); -fx-font: 12 System");
					
					button.setOnAction(e->{
						Elevator elevator = elevators.get(Integer.parseInt(button.getId()));
						if(elevator.getStatus().equals("Idle") || elevator.getStatus().equals("Door is open")){
							manuelPerson.add(new ManuelPerson(elevator, fileOperation));
							personWaitCabins.get(elevator.getNow()).getChildren().add(manuelPerson.get(manuelPerson.size()-1).id);
							manuelPerson.get(manuelPerson.size()-1).start();
							elevator.queue.add(elevator.getNow());
						}
						
					});
					newGrid.add(button, 1, 0);
				}
				/*
				 * 	Elevator stats
				 */
				elevators.get(i).capacityImg.setImage(elevators.get(i).greyCapacity);

				newGrid.add(elevators.get(i).capacityImg, 2, 0);
				newGrid.add(new Label("Elevator "+(i+1)), 0, 0);
				newGrid.add(new Label("Carried people"), 0, 1);
				newGrid.add(new Label("Now"), 0, 2);
				newGrid.add(new Label("Target"), 0, 3);
				newGrid.add(new Label("Total idle time"), 0, 4);
				newGrid.add(new Label("Status"), 0, 5);
				
				elevatorStats.add(new Label("0"));
				newGrid.add(elevatorStats.get(i*5+0), 1, 1);
				elevatorStats.add(new Label("0"));
				newGrid.add(elevatorStats.get(i*5+1), 1, 2);
				elevatorStats.add(new Label("0"));
				newGrid.add(elevatorStats.get(i*5+2), 1, 3);
				elevatorStats.add(new Label("0"));
				newGrid.add(elevatorStats.get(i*5+3), 1, 4);
				elevatorStats.add(new Label("Idle"));
				newGrid.add(elevatorStats.get(i*5+4), 1, 5);
				
				Pane panel = new Pane();
				panel.setLayoutY(105);
				panel.setPadding(new  Insets(0,0,2,0));
				
				// 0 to number of floors buttons
				// emergency and stop buttons
				if(!simulate){
					if(numberOfFloors>11){
						for(j=0;j<11;j++){
							
							panelButtons.add(new Button(""+j));
							final Button thisButton = panelButtons.get(i*numberOfFloors+j);
							thisButton.setPrefSize(30, 30);
							thisButton.setLayoutX(35*j);
							thisButton.setId(String.valueOf(j));
							thisButton.setUserData(new Integer(i));
							thisButton.setOnAction(e->{
								EC.panelButtonPress(Integer.parseInt(thisButton.getId()), (int) thisButton.getUserData());
							});
							panel.getChildren().add(thisButton);
						}
						for(j=11;j<numberOfFloors;j++){
							panelButtons.add(new Button(""+j));
							final Button thisButton = panelButtons.get(i*numberOfFloors+j);
							thisButton.setPrefSize(30, 30);
							thisButton.setLayoutY(35);
							thisButton.setLayoutX(35*(j-11));
							thisButton.setId(String.valueOf(j));
							thisButton.setUserData(new Integer(i));
							thisButton.setOnAction(e->{
								EC.panelButtonPress(Integer.parseInt(thisButton.getId()), (int) thisButton.getUserData());
							});
							panel.getChildren().add(thisButton);
						}
						j--;
						if(elevators.get(i).isStop()){
							j++;
							final Button thisButton = elevators.get(i).getStop();
							thisButton.setPrefSize(30, 30);
							thisButton.setLayoutY(35);
							thisButton.setLayoutX(35*(j-11));
							panel.getChildren().add(thisButton);
						}
						if(elevators.get(i).isEmergency()){
							j++;
							final Button thisButton = elevators.get(i).getEmergency();
							thisButton.setPrefSize(30, 30);
							thisButton.setLayoutY(35);
							thisButton.setLayoutX(35*(j-11));
							panel.getChildren().add(thisButton);
						}
					}
					else{
						for(j=0;j<numberOfFloors;j++){
							panelButtons.add(new Button(""+j));
							final Button thisButton = panelButtons.get(i*numberOfFloors+j);
							thisButton.setPrefSize(30, 30);
							thisButton.setLayoutX(35*j);
							thisButton.setId(String.valueOf(j));
							thisButton.setUserData(new Integer(i));
							thisButton.setOnAction(e->{
								EC.panelButtonPress(Integer.parseInt(thisButton.getId()), (int) thisButton.getUserData());
							});
							panel.getChildren().add(thisButton);
						}
						j--;
						if(elevators.get(i).isStop()){
							j++;
							final Button thisButton = elevators.get(i).getStop();
							thisButton.setPrefSize(30, 30);
							thisButton.setLayoutX(35*j);
							panel.getChildren().add(thisButton);
						}
						if(elevators.get(i).isEmergency()){
							j++;
							final Button thisButton = elevators.get(i).getEmergency();
							thisButton.setPrefSize(30, 30);
							thisButton.setLayoutX(35*j);
							panel.getChildren().add(thisButton);
						}
					}
				}
				
				
				
				newPane.getChildren().addAll(newGrid, panel);
				vbox.getChildren().add(newPane);
			}
			
			
			hbox.getChildren().add(vbox);
			
			Pane pane = new Pane();
			pane.setPrefSize(878, 700);
			
			
			Rectangle thisRec;
			
			/*
			 * 	Put elevator holes
			 */
			Label FloorNumberLabel = null;
			int sizeOfAFloor = 700/numberOfFloors;
			int coordinateX = 878-sizeOfAFloor;
			int coordinateY;
			
			for(i=0;i<numberOfElevators;i++){
				coordinateY = 700-sizeOfAFloor;
				for(j=0;j<numberOfFloors;j++){
					floors.add(new Rectangle(sizeOfAFloor,sizeOfAFloor));
					thisRec = floors.get(i*numberOfFloors+j);
					thisRec.setFill(Color.TRANSPARENT);
					thisRec.setStroke(Color.BLACK);
					thisRec.setStrokeType(StrokeType.INSIDE);
					thisRec.setArcHeight(1);
					thisRec.setArcWidth(1);
					thisRec.setLayoutX(coordinateX);
					thisRec.setLayoutY(coordinateY);
					
					FloorNumberLabel = new Label();
					FloorNumberLabel.setText(String.valueOf(j));
					FloorNumberLabel.setLayoutX(coordinateX);
					FloorNumberLabel.setLayoutY(coordinateY);
					
					pane.getChildren().addAll(thisRec,FloorNumberLabel);
					
					coordinateY -= sizeOfAFloor;
				}
				coordinateX -= (sizeOfAFloor+10);
			}
			
			
			/*
			 * 	Put elevator cabins
			 */
			coordinateX = 878-sizeOfAFloor+2;
			coordinateY = 700-sizeOfAFloor+2;
			
			for(i=0;i<numberOfElevators;i++){
				elevators.get(i).setCabin(new Rectangle(sizeOfAFloor-4,sizeOfAFloor-4));
				thisRec = elevators.get(i).getCabin();
				elevators.get(i).getCabin().setStyle("-fx-fill: "+colors[i]+";");
				elevators.get(i).group.setLayoutY(coordinateY);
				elevators.get(i).group.setLayoutX(coordinateX);
				elevators.get(i).group.getChildren().addAll(elevators.get(i).cabin, elevators.get(i).flowpane);
				pane.getChildren().add(elevators.get(i).group);
				coordinateX -= (sizeOfAFloor+10);
			}
			if(!simulate){
				/*
				 * 	Put call buttons
				 */
				ArrayList<Label> callForUpLabels = new ArrayList<Label>();
				ArrayList<Label> callForDownLabels = new ArrayList<Label>();
	
				coordinateX = 10;
				coordinateY = 700;
				
				
				for(i=0;i<numberOfFloors;i++){
					coordinateY -= sizeOfAFloor;
					
					HBox nhbox = new HBox();
					nhbox.setSpacing(5);
					nhbox.setLayoutX(coordinateX);
					nhbox.setLayoutY(coordinateY);
					
					nhbox.getChildren().add(new Label(String.valueOf(i)));
					
					final Label thisUp;
					
					callForUpLabels.add(new Label("▲"));
					thisUp = callForUpLabels.get(i);
					thisUp.setStyle("-fx-border-radius:2; -fx-border-style:solid; -fx-border-color:rgba(0,0,0,.3); -fx-background-color:rgba(0,0,0,.15)");
					thisUp.setPrefSize(20, 20);
					thisUp.setCursor(javafx.scene.Cursor.HAND);
					thisUp.setAlignment(Pos.CENTER);
					thisUp.setId(String.valueOf(i));
					thisUp.setOnMouseClicked(e->{
						try {
							EC.callElevatorUP(Integer.parseInt(thisUp.getId()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					});
					nhbox.getChildren().add(thisUp);
					
					final Label thisDown;
					
					callForDownLabels.add(new Label("▼"));
					thisDown = callForDownLabels.get(i);
					thisDown.setStyle("-fx-border-radius:2; -fx-border-style:solid; -fx-border-color:rgba(0,0,0,.3); -fx-background-color:rgba(0,0,0,.15)");
					thisDown.setPrefSize(20, 20);
					thisDown.setCursor(Cursor.HAND);
					thisDown.setAlignment(Pos.CENTER);
					thisDown.setId(String.valueOf(i));
					thisDown.setOnMouseClicked(e->{
						EC.callElevatorDOWN(Integer.parseInt(thisDown.getId()));
					});
					nhbox.getChildren().add(thisDown);
					
					pane.getChildren().add(nhbox);
				}
				
				callForDownLabels.get(0).setDisable(true);
				callForUpLabels.get(numberOfFloors-1).setDisable(true);
			}
			
				
			/*
			 * 	Waiting Cabins
			 */
			coordinateX = 75;
			coordinateY = 700;
			for(i=0;i<numberOfFloors;i++){
				coordinateY -= sizeOfAFloor;
				
				HBox newHBox = new HBox(); 

				newHBox.setLayoutY(coordinateY);
				newHBox.setLayoutX(coordinateX);
				newHBox.setSpacing(1);
				personWaitCabins.add(newHBox);
				
				pane.getChildren().add(personWaitCabins.get(i));

			}
			if(simulate){
				new Thread()
				{
				    public void run() {
				    	while(true){
				    		try {
								sleep(500);
					    		createRandomPerson(randomPerson, EC, personWaitCabins);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
				    	}
				        
				    }
				}.start();
			}
			
			/*
			 * 	Add all components to scene
			 */
			hbox.getChildren().add(pane);
			
			simulation = new Scene(layout);
			for(Elevator tmp : elevators){
				tmp.setSizeOfAFloor(sizeOfAFloor);
				tmp.flowpane.setPrefWrapLength(sizeOfAFloor);
				tmp.flowpane.setMaxSize(sizeOfAFloor, sizeOfAFloor);
			}
			return simulation;
		}
		public void createRandomPerson(ArrayList<RandomPerson> person, ElevatorController EC, ArrayList<HBox> personWaitCabins){
			
			Random random = new Random();
			if(random.nextInt(1000)%7==0){
				int currentFloor = random.nextInt(numberOfFloors);
				int targetFloor = random.nextInt(numberOfFloors);
				
				while(targetFloor == currentFloor)
					targetFloor = random.nextInt(numberOfFloors);
				
				person.add(new RandomPerson(currentFloor, targetFloor, elevators, EC, fileOperation));
				
				if(targetFloor > currentFloor){
					try {
						person.get(person.size()-1).setElevator(elevators.get(EC.callElevatorUP(currentFloor)));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else{
					try {
						person.get(person.size()-1).setElevator(elevators.get(EC.callElevatorDOWN(currentFloor)));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				person.get(person.size()-1).start();
				
				
				Platform.runLater(new Runnable(){
					 
	                @Override
	                public void run() {
	                	personWaitCabins.get(currentFloor).getChildren().add(person.get(person.size()-1).id);
	                }
	            });
				
			}
		}
	}
	
	
}
