package transHelp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class TransHelpBk {
	private File rawTrans;
	private List<String> rawLines,transLines;

	private int ind=0;
	
	public static final String RAW_TAG1="<raw>", RAW_TAG2="</raw>", TRANS_TAG1="<trans>",TRANS_TAG2="</trans>";
	//private int pointer=0;

	public TransHelpBk(File rt){
		/* create rawLines
		 * create transLines to have same capacity as rawLines.
		 * TODO: obsolete?
		 * */
		
	}
	
	/**Creates a raw file from input.
	 * TODO: may need to make string smaller, buffer capacity...
	 * @param in TODO:input must come from GUI prompt.
	 * */
	public void makeRawFile(String rawText){
		try{
			PrintWriter o=new PrintWriter(new OutputStreamWriter(new FileOutputStream(rawTrans,false),"UTF-8"));
			//TODO: UTF
			o.println(RAW_TAG1);
			Scanner scan=new Scanner(rawText);
			boolean prevNotBlank=true;
			String prev="";
			while(scan.hasNextLine()){
				String s=scan.nextLine();
				if(!s.trim().isEmpty()){//TODO: check empty line condenser
					o.println(prev+s);
					prev="";
				}else if(prevNotBlank){
					prevNotBlank=false;
				}else if(!prevNotBlank){
					prev="\n ";
				}
			}
			scan.close();
			o.println(RAW_TAG2);
			o.println(TRANS_TAG1);
			o.println(TRANS_TAG2);
			o.flush();
			o.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();//Log file?
		}
	}
	
	/**Scans file, and loads it into memory for manipulation.
	 * @return True if no errors thrown
	 * */
	public boolean read(){
		try{
			Scanner scan=new Scanner(rawTrans);
			rawLines=new LinkedList<String>();
			if(!scan.nextLine().equals(RAW_TAG1)){
				scan.close();
				throw new IllegalArgumentException("Wrong Format");
			}
			while(scan.hasNextLine()){
				String s=scan.nextLine();
				if(s.equals(RAW_TAG2)){
					break;
				}
				rawLines.add(s);
			}
			if(!scan.nextLine().equals(TRANS_TAG1)){
				scan.close();
				throw new IllegalArgumentException("Wrong Format");
			}
			transLines=new ArrayList<String>(rawLines.size());
			while(scan.hasNextLine()){
				String s=scan.nextLine();
				if(s.equals(TRANS_TAG2)){
					break;
				}
				transLines.add(s);
			}
			scan.close();
			while(transLines.size()<rawLines.size()){
				transLines.add("");
			}
			return true;
		}catch(FileNotFoundException e){
			e.printStackTrace();
			return false;
		}catch(IllegalArgumentException e1){
			JOptionPane.showMessageDialog(null, "Format should match regex:\n"+
					"\"<raw>(.+\\n)+</raw>\\n<trans>(.+\\n)+</trans>\"");
			//TODO: null parent frame?
			return false;
		}
	}
	
	/**Writes everything back into the file rawTrans. Used for saving.
	 * */
	public void write(){
		try{
			PrintWriter o=new PrintWriter(new OutputStreamWriter(new FileOutputStream(rawTrans,false),"UTF-8"));
			//TODO: UTF
			o.println(RAW_TAG1);
			for(String x:rawLines){
				o.println(x);
			}
			o.println(RAW_TAG2);
			
			o.println(TRANS_TAG1);
			for(String x:transLines){
				if(!x.isEmpty()){
					o.println(x);
				}
			}
			o.println(TRANS_TAG2);
			
			o.flush();
			o.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	/**Returns the number of rawLines
	 * */
	public int getNumLines(){
		return rawLines.size();
	}
	
	/**Iterates through transLines and returns false if there is an emptyString
	 * */
	public boolean isComplete(){
		for(String x:transLines){
			if(x.isEmpty()){
				return false;
			}
		}
		return true;
	}
	
	/**Returns current position of the file
	 * */
	public int getPointer(){
		return ind;
	}
	
	/**Returns the raw at previous line, or empty string if none
	 * */
	public String getRawPrev(){
		try{
			return rawLines.get(ind-1);
		}catch(IndexOutOfBoundsException e){
			return "";
		}
	}

	/**Returns the translation at previous line, or empty string if none.
	 * */
	public String getTransPrev(){
		try{
			return transLines.get(ind-1);
		}catch(IndexOutOfBoundsException e){
			return "";
		}
	}
	/**Returns the raw at pointer
	 * */
	public String getRawCurr(){
		return rawLines.get(ind);
	}

	/**Returns the translation at pointer
	 * */
	public String getTransCurr(){
		return transLines.get(ind);
	}

	/**Returns the raw at next Line, or empty string if no next line
	 * */
	public String getRawNext(){
		try{
			return rawLines.get(ind+1);
		}catch(IndexOutOfBoundsException e){
			return "";
		}
	}

	/**Returns the translation at next Line, or empty string if no next line
	 * */
	public String getTransNext(){
		try{
			return transLines.get(ind+1);
		}catch(IndexOutOfBoundsException e){
			return "";
		}
	}
	
	
	public void movePtrFirstBlankLine(String in){
		int i=0;
		while(!transLines.get(i).isEmpty()&&i<getNumLines()){
			i++;
		}
		movePtr(i,in);
	}
	
	public void movePtr(int i,String in){
		transLines.set(ind, in);
		ind=(i+getNumLines())%getNumLines();
	}
		
	public void setFile(File f){
		rawTrans=f;
	}
	
	public List<String> getRaw(){
		return rawLines;
	}
	public List<String> getTrans(){
		return transLines;
	}
	
}

