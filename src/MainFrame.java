import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * @author MDarmawan
 *
 */
public class MainFrame extends JApplet implements ActionListener {

	/**
	 * just removing the warning signs
	 */
	private static final long serialVersionUID = 1L;

	public JTextArea in,out,e;
	private int numFtn=0;
	private int numFtnH=0;
	private int numLines=0;

	/*
	 * Used to generate footnotes, with a link to and from the footnotes.
	 */
	public static final String tag="h";
	
	
	/*
	 * just random inline block comments.
	 * Was used to list lines where I was unsure of the line.
	 * Adds a separate footnote below the TL notes 
	 */
	public static final String tagH="q";

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd=e.getActionCommand();
		if(cmd.equals("format")){
			//System.out.println("hi1");
			this.format();
			this.e.setText("Number of lines: "+numLines+
					"\nNumber of footnotes: "+numFtn+
					"\nNumber of helps: "+numFtnH);
		}

	}

	public static void main(String[] args) {
		MainFrame x=new MainFrame();
		x.init();

		JFrame f=new JFrame("Translation Formatter");
		f.setContentPane(x);
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init(){
		JPanel main=new JPanel();
		main.setLayout(new BorderLayout());

		Dimension d=new Dimension(400,550);
		int r=35;
		int c=50;

		in=new JTextArea("Input TA translated text here.",r,c);
		/*/
		in.setSize(d);
		in.setMinimumSize(d);
		in.setPreferredSize(d);
		//*/
		in.setWrapStyleWord(true);

		out=new JTextArea("Output here.",r,c);
		//out.setEditable(false);
		/*/
		out.setSize(d);
		out.setMaximumSize(d);
		out.setPreferredSize(d);
		//*/
		out.setWrapStyleWord(true);

		JScrollPane a=new JScrollPane(in);
		a.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		a.setSize(d);
		a.setMaximumSize(d);
		a.setPreferredSize(d);
		//a.add(in);

		JScrollPane b=new JScrollPane(out);
		//b.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		b.setSize(d);
		b.setMaximumSize(d);
		b.setPreferredSize(d);
		//b.add(out);

		JPanel diag=new JPanel();
		diag.setLayout(new BoxLayout(diag,BoxLayout.Y_AXIS));

		JButton fb=new JButton("Format!-WP");
		fb.addActionListener(this);
		fb.setActionCommand("format");
		diag.add(fb);

		//*/
		e=new JTextArea("Diagnostics/Misc");
		d=new Dimension(800,200);
		e.setSize(d);
		e.setMinimumSize(d);
		e.setPreferredSize(d);
		e.setWrapStyleWord(true);
		JScrollPane g=new JScrollPane(e);
		//c.add(e);
		diag.add(g);
		//*/

		main.add(a,BorderLayout.WEST);
		main.add(b,BorderLayout.EAST);
		main.add(diag, BorderLayout.SOUTH);

		this.add(main);

	}

	public void format(){
		//System.out.println("hoi"+in.getText());		
		String s=formatN(in.getText());
		s=formatH(s);

		out.setText(s);
	}

	/**Formats the TNs 
	 * */
	public String formatN(String in){//TL Notes
		String out="";
		Queue<String> hq=new LinkedList<>();
		//System.out.println("hi3");
		Scanner scan=new Scanner(in);
		numLines=0;
		while(scan.hasNextLine()){
			String l=scan.nextLine();
			if(!l.startsWith("//")){
				l=l.replaceAll("--","&mdash;");
				numLines++;
				while(l.matches(".*<"+tag+">[^<]*</"+tag+">.*")){
					int start=l.indexOf("<"+tag+">");
					int end=l.indexOf("</"+tag+">");
					String a=l.substring(0,start);
					String b=l.substring(end+3+tag.length());
					String c=l.substring(start+2+tag.length(),end);
					//System.out.println(a+"\n"+b+"\n"+c);
					hq.add(c);
					l=a+TLanchorN(hq.size(),c)+b;
				}
				out+=l+"\n";
			}
		}
		out+="<hr/>\nTranslator notes\n";
		out+="<ol>\n";
		numFtn=hq.size();
		//System.out.println(numFtn);
		for(int i=1;i<=numFtn;i++){
			out+="<li>"+hq.poll()+TLftnN(i)+"</li>\n";
		}
		out+="</ol>\n<!--end-->";
		//System.out.println(out);
		scan.close();
		return out;
	}

	/**Formats the questions of the TL 
	 * */
	public String formatH(String in){//T Help
		String out="";
		Queue<String> hq=new LinkedList<>();
		//System.out.println("hi3");
		Scanner scan=new Scanner(in);
		//numLines=0;
		while(scan.hasNextLine()){
			String l=scan.nextLine();
			//numLines++;
			while(l.matches(".*<"+tagH+">[^<]*</"+tagH+">.*")){
				int start=l.indexOf("<"+tagH+">");
				int end=l.indexOf("</"+tagH+">");
				String a=l.substring(0,start);
				String b=l.substring(end+3+tagH.length());
				String c=l.substring(start+2+tagH.length(),end);
				hq.add(c);
				l=a+TLanchorH(hq.size(),c)+b;
			}
			out+=l+"\n";
		}
		
//		out+="<hr/>\nTranslator needs help\n";
//		out+="<ol>\n";
//		numFtnH=hq.size();
//		//System.out.println(numFtn);
//		for(int i=1;i<=numFtnH;i++){
//			out+="<li>"+hq.poll()+TLftnH(i)+"</li>\n";
//		}
//		out+="</ol>\n<!--end-->";
		//System.out.println(out);
		scan.close();
		return out;
	}

	private static String TLanchorN(int i, String alt){
		return "<sup><a href=\"#ftn"+i+"\" id=\"tl"+i+"\" title=\""+alt+"\">["+i+"]</a></sup>";
	}
	private static String TLanchorH(int i, String alt){
		return "";
//		return "<sup><a href=\"#ftnH"+i+"\" id=\"hn"+i+"\" title=\""+alt+"\">[?"+i+"?]</a></sup>";
	}


	private static String TLftnN(int i){
		return "--<a href=\"#tl" + i + "\" id=\"ftn" + i + "\">"+"back</a>";
	}
	private static String TLftnH(int i){
		return "--<a href=\"#hn" + i + "\" id=\"ftnH" + i + "\">"+"back</a>";
	}
}
