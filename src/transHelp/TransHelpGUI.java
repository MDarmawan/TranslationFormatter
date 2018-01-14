package transHelp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class TransHelpGUI extends JApplet implements ActionListener{

	private JTextArea rawDisp,transDisp;

	private TransHelpBk backEnd;
	private ShortcutListener keyList;//TODO: make sure of accessibility, low priority... 

	private JTextArea prevRawDisp, prevTransDisp;
	private JTextArea nextRawDisp, nextTransDisp;
	
	private JProgressBar pb;

	//private File in,out;

	/**Populates the JApplet
	 * ActionCommands:con{tog-up,tog-down,copy}
	 *TODO: JScrollPanes needed? Aesthetics later
	 * */
	public void init(){
		JPanel center=new JPanel();
		center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
		Dimension d=new Dimension(600,700);
		center.setMinimumSize(d);
		center.setPreferredSize(d);

		//*/Top panel
		JPanel top=new JPanel();
		top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));

		prevRawDisp=new JTextArea("prd");
		prevRawDisp.setEditable(false);
		top.add(prevRawDisp);
		top.add(Box.createRigidArea(new Dimension(0,1)));
		prevTransDisp=new JTextArea("ptd");
		prevTransDisp.setEditable(false);
		top.add(prevTransDisp);

		center.add(top);
		//*/Top Panel
		center.add(Box.createRigidArea(new Dimension(0,10)));
		//*/Middle panel
		JPanel mid=new JPanel();
		mid.setLayout(new BoxLayout(mid,BoxLayout.X_AXIS));

		JPanel toggle=new JPanel();
		toggle.setLayout(new BoxLayout(toggle,BoxLayout.Y_AXIS));
		JButton up=new JButton("\u25B2");
		up.addActionListener(this);
		up.setActionCommand("con/tog-up");
		//d=new Dimension(120,200);
		//up.setPreferredSize(d);
		//up.setMinimumSize(d);
		toggle.add(up);
		toggle.add(Box.createRigidArea(new Dimension(0,1)));

		JButton copyRawLine=new JButton("Copy Raw Line");
		copyRawLine.addActionListener(this);
		copyRawLine.setActionCommand("con/copy");
		toggle.add(copyRawLine);
		toggle.add(Box.createRigidArea(new Dimension(0,1)));
		
		JButton fbl=new JButton("First Blank Line");
		fbl.addActionListener(this);
		fbl.setActionCommand("con/fbl");
		toggle.add(fbl);
		toggle.add(Box.createRigidArea(new Dimension(0,1)));

		JButton down=new JButton("\u25BC");
		down.addActionListener(this);
		down.setActionCommand("con/tog-down");
		//down.setPreferredSize(d);
		//down.setMinimumSize(d);
		toggle.add(down);

		mid.add(toggle);

		mid.add(Box.createRigidArea(new Dimension(5,0)));

		JPanel focus=new JPanel();
		focus.setLayout(new BoxLayout(focus,BoxLayout.Y_AXIS));

		rawDisp=new JTextArea("rd");
		rawDisp.setEditable(true);
		focus.add(rawDisp);
		focus.add(Box.createRigidArea(new Dimension(0,1)));
		transDisp=new JTextArea("td");
		transDisp.setEditable(true);
		focus.add(transDisp);
		mid.add(focus);

		center.add(mid);
		//*/Middle Panel
		center.add(Box.createRigidArea(new Dimension(0,10)));
		//*/Bottom panel
		JPanel bot=new JPanel();
		bot.setLayout(new BoxLayout(bot,BoxLayout.Y_AXIS));

		nextRawDisp=new JTextArea("nrd");
		nextRawDisp.setEditable(false);
		bot.add(nextRawDisp);
		bot.add(Box.createRigidArea(new Dimension(0,1)));
		nextTransDisp=new JTextArea("ntd");
		nextTransDisp.setEditable(false);
		bot.add(nextTransDisp);

		center.add(bot);		
		//*/Bottom Panel
		this.add(center);

		this.add(initToolBar(),BorderLayout.NORTH);
		this.add(initProgBar(),BorderLayout.SOUTH);
	}

	/**Creates the menu bar for the JApplet. To be called only in init()
	 * ActionCommands:tb{file[new,open,save],clip[trans,merge,HTML],misc[raw,trans,help]} 
	 * */
	private JMenuBar initToolBar(){
		JMenuBar jmb=new JMenuBar();

		JMenu file=new JMenu("File");
		JMenuItem newFile,openFile,saveFile;
		newFile=new JMenuItem("New    Ctr+N");
		newFile.addActionListener(this);
		newFile.setActionCommand("tb/file/new");
		openFile=new JMenuItem("Open   Ctr+O");
		openFile.addActionListener(this);
		openFile.setActionCommand("tb/file/open");
		saveFile=new JMenuItem("Save   Ctr+S");
		saveFile.addActionListener(this);
		saveFile.setActionCommand("tb/file/save");
		file.add( newFile);
		file.add(openFile);
		file.add(saveFile);
		jmb.add(file);

		JMenu clipboard=new JMenu("Clipboard");
		JMenuItem copyTrans,copyMerge,copyTransHTML;
		copyTrans=new JMenuItem("Copy Translation");
		copyTrans.addActionListener(this);
		copyTrans.setActionCommand("tb/clip/trans");
		copyMerge=new JMenuItem("Copy Merged");
		copyMerge.addActionListener(this);
		copyMerge.setActionCommand("tb/clip/merge");
		copyTransHTML=new JMenuItem("Copy Translation, Formatted");
		copyTransHTML.addActionListener(this);
		copyTransHTML.setActionCommand("tb/clip/HTML");
		clipboard.add(copyTrans);
		clipboard.add(copyMerge);
		clipboard.add(copyTransHTML);
		jmb.add(clipboard);

		JMenu misc=new JMenu("Misc.");
		JMenuItem seeRaw,seeTrans,help;
		seeRaw=new JMenuItem("View Raw");
		seeRaw.addActionListener(this);
		seeRaw.setActionCommand("tb/misc/raw");
		seeTrans=new JMenuItem("View Translation");
		seeTrans.addActionListener(this);
		seeTrans.setActionCommand("tb/misc/trans");
		help=new JMenuItem("Help");
		help.addActionListener(this);
		help.setActionCommand("tb/misc/help");
		misc.add(seeRaw);
		misc.add(seeTrans);
		misc.add(help);
		jmb.add(misc);

		return jmb;
	}

	private JProgressBar initProgBar(){
		pb=new JProgressBar(0,1);
		pb.setBorderPainted(true);//TODO:Aesthetics
		return pb;
	}
	
	public static void main(String[] args) {
		JFrame f=new JFrame("TranSuite");

		TransHelpGUI a=new TransHelpGUI();
		a.init();
		a.initToolBar();//TODO:
		f.setContentPane(a);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		String cmd=e.getActionCommand();
		if(cmd.matches("tb/.+")){//
			//ActionCommands:tb{file[new,open,save],clip[trans,merge,HTML],misc[raw,trans,help]}
			String[]sa=cmd.split("/");
			if(sa[1].equals("file")){
				if(sa[2].equals("new")){
					newProject();
				}else if(sa[2].equals("open")){
					selectProject();
				}else if(sa[2].equals("save")){
					saveProject();
				}  
			}else if(sa[1].equals("clip")){
				String s="";
				if(sa[2].equals("trans")){
					s=getTransLinesAsString();
				}else if(sa[2].equals("merge")){
					s=getMergeLinesAsString();
				}else if(sa[2].equals("HTML")){
					s=getTransLinesAsHTML();
				}  
				//TODO: clipboard implementation
			}else if(sa[1].equals("misc")){//TODO: implement 
				String title="";
				String message="";
				if(sa[2].equals("raw")){
					
				}else if(sa[2].equals("trans")){
					
				}else if(sa[2].equals("help")){
					
				}  
				openDialog(title,message);
			}

		}else if(cmd.matches("con/.+")){
			//ActionCommands:con{tog-up,tog-down,copy,fbl}
			String[]sa=cmd.split("/");
			if(sa[1].equals("tog-up")){
				backEnd.movePtr(backEnd.getPointer()-1,transDisp.getText());
				rerender();
			}else if(sa[1].equals("tog-down")){
				backEnd.movePtr(backEnd.getPointer()+1,transDisp.getText());
				rerender();
			}else if(sa[1].equals("copy")){
				//TODO: copy raw onto clipboard
			}else if(sa[1].equals("fbl")){
				backEnd.movePtrFirstBlankLine(transDisp.getText());
				rerender();
			}
		}
		//*/
		else if(cmd.matches("bk/.+")){
			//ActionCommands:bk{new}
			String[]sa=cmd.split("/");
			if(sa[1].equals("new")){//TODO: One alternative of passing a property
				JFileChooser jfc=new JFileChooser();
				jfc.showSaveDialog(this);
				backEnd.setFile(jfc.getSelectedFile());
				String rawText=((JTextArea)((JButton)e.getSource()).getClientProperty("rawText")).getText();
				backEnd.makeRawFile(rawText);
				openProject();
			}else if(sa[1].equals("")){
			}
		}
		//*/

		/*/
		if(cmd.equals("con/copy")){
			System.out.println(((JButton)e.getSource()).getWidth());
		}
		//*/
	}

	/**Creates a file dialog to input raw text, then prompts for a file. 
	 * Next, it calls for the back end to create the file in the backend.
	 * Finally, it loads the newly created file into the backend by using openProject(); 
	 * */
	public void newProject(){
		JDialog jda=new JDialog();
		//*/
		JPanel in=new JPanel();
		in.setLayout(new BoxLayout(in,BoxLayout.Y_AXIS));
		in.add(new JLabel("Input Raw Text Below:"));
		JTextArea raw=new JTextArea();
		Dimension d=new Dimension(400,400);
		raw.setMinimumSize(d);
		raw.setPreferredSize(d);
		in.add(raw);	
		JButton b=new JButton("Okay");
		/*/
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser jfc=new JFileChooser();
				jfc.showSaveDialog(this);
				backEnd.setFile(jfc.getSelectedFile());
				String rawText=raw.getText();
				backEnd.makeRawFile(rawText);
			}
		});
		/*/
		b.addActionListener(this); 
		b.putClientProperty("rawText", raw);
		b.setActionCommand("bk/new");
		in.add(b);

		jda.add(in);
		d=new Dimension(400,500);
		jda.setMinimumSize(d);
		jda.setPreferredSize(d);
		jda.setVisible(true);
		//When button is pressed this.actionPerformed opens a JFileChooser, and creates rawTransFile

		//openProject();
	}

	/**Creates a prompt to select a file to open. Then, open
	 * */
	public void selectProject(){
		JFileChooser jfc=new JFileChooser();
		jfc.showOpenDialog(this);
		File f=jfc.getSelectedFile();
		if(f!=null){
			backEnd.setFile(f);
			openProject();
		}
	}

	public void openProject(){
		backEnd.read();
		pb.setMaximum(backEnd.getNumLines());
		pb.setValue(0);
		rerender();
	}
	public void saveProject(){
		backEnd.write();
	}

	/**Returns the translated lines as is.//TODO: move over WP formatter.
	 * */
	public String getTransLinesAsString(){
		String a="";
		for(String s:this.getLines(true)){
			a+=s+"\n";
		}
		return a;
	}
	public String getTransLinesAsHTML(){
		String a="";
		for(String s:this.getLines(true)){
			if(s.matches("<([^<>]+)>.+</\1>")){
				a+=s+"\n";
			}else{
				a+="<p>"+s+"</p>\n";
			}
		}
		return a;
	}
	
	public String getMergeLinesAsString(){
		String a="";
		List<String> r,t;
		r=getLines(false);
		t=getLines(true);
		for(int i=0;i<backEnd.getNumLines();i++){
			String sr=r.get(i);
			String st=t.get(i);
			a+=sr+"\n"+st+"\n";
		}
		return a;
	}

	/**returns a list from the backend.
	 * @param b - if true, returns transLines
	 * */
	public List<String> getLines(boolean b){
		return b?backEnd.getTrans():backEnd.getRaw();
	}

	//public String getMergedLines(){}

	public void openDialog(String title,String mess){
		JOptionPane.showMessageDialog(this, mess,title,JOptionPane.INFORMATION_MESSAGE);
	}
	

	//TODO:Window check saved?
	/**Is called in actionPerformed to refresh all the text areas and progress bar
	 * */
	private void rerender(){
		prevRawDisp.setText(backEnd.getRawPrev());
		rawDisp.setText(backEnd.getRawCurr());
		nextRawDisp.setText(backEnd.getRawNext());

		prevTransDisp.setText(backEnd.getTransPrev());
		transDisp.setText(backEnd.getTransCurr());
		nextTransDisp.setText(backEnd.getTransNext());
		
		pb.setValue(backEnd.getPointer());
	}
}
