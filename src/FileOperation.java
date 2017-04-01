import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileOperation {
	private FileWriter LogFile;
	public FileOperation(){
		try {
			LogFile = new FileWriter("LogFile.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void writeLog(String log){
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("'['HH:mm:ss']'");
		try {
			System.out.println(ft.format(dNow)+" "+log);
			LogFile.append(ft.format(dNow)+" "+log+"\r\n");
			LogFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void close(){
		try {
			LogFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void flush(){
		try {
			LogFile.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
