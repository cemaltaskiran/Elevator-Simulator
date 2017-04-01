
public class ElevatorQueue {
	private int SIZE = 40;
	private int TOP=-1;
	private int[] queue = new int[SIZE];
	
	public ElevatorQueue() {
		int i;
		for(i=0;i<SIZE;i++)
			queue[i] = -1;
	}
	
	public void incTOP(){
		TOP++;
	}
	public void descTOP(){
		TOP--;
	}
	public boolean isEmpty(){
		if(TOP==-1)
			return true;
		else
			return false;
	}
	
	public void sortByNow(int now){
		int i=0;
		int j=0;
		int k;
		int key;
		int[] newArray = new int[SIZE];
		for(i=0;i<SIZE;i++)
			newArray[i] = -1;
		
		// sort low to high
		if(now < queue[0]){ 
			for(k=0;k<=TOP;k++){
				if(now < queue[k]){
					newArray[j] = queue[k];
					queue[k] = -1;
					j++;
				}
			}
			int aSize = j;
			for (j = 1; j < aSize; j++){
				key = newArray[j];
				for(i = j - 1; (i >= 0) && (newArray[i] > key); i--)
					newArray[i+1] = newArray[i];
					   
				newArray[i+1] = key;
		    }
			for(i=0;i<=TOP;i++){
				if(queue[i] != -1){
					newArray[aSize] = queue[i];
					aSize++;
				}
			}
			queue = newArray;
		}
		
		// sort high to low
		else if(now > queue[0]){ 
			for(k=0;k<=TOP;k++){
				if(now > queue[k]){
					newArray[j] = queue[k];
					queue[k] = -1;
					j++;
				}
			}
			int aSize = j;
			for (j = 1; j < aSize; j++){
				key = newArray[j];
				for(i = j - 1; (i >= 0) && (newArray[i] < key); i--)
					newArray[i+1] = newArray[i];
					   
				newArray[i+1] = key;
		    }
			for(i=0;i<=TOP;i++){
				if(queue[i] != -1){
					newArray[aSize] = queue[i];
					aSize++;
				}
			}
			queue = newArray;
		}
	}
	public void add(int element){
		int i=0;
		boolean canBeAdded = true;
		while(queue[i]!=-1 && i<=TOP){
			if(queue[i] == element){
				canBeAdded = false;
			}
			i++;
		}
		if(canBeAdded){
			incTOP();
			queue[TOP] = element;
		}
	}
	public void poll(){
		if(!isEmpty()){
			queue[0] = -1;
			for(int i=0;i<TOP;i++){
				queue[i] = queue[i+1];
			}
			TOP--;
		}
	}
	public int element(){
		return queue[0];
	}
	public boolean contains(int element){
		int i = 0;
		for(i=0;i<=TOP;i++){
			if(queue[i] == element)
				return true;
		}
		return false;
	}
	public String toString(){
		String s="";
		if(isEmpty()){
			s = "No target";
		}
		else{
			for(int i=0;i<=TOP;i++)
				s += queue[i]+" ";
		}
		return s;
	}
	public int size(){
		return TOP+1;
	}
	
}
