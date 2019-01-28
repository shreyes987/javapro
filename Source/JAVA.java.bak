import java.awt.event.*;
import java.awt.*;
import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JWindow;
import javax.swing.JButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JFrame;
import javax.swing.event.ChangeListener;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeEvent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.*;

/* <APPLET code=JAVA.class height=650 width=650>
 </APPLET>*/
 
public class JAVA extends JApplet implements ActionListener,MouseListener,MouseMotionListener,ChangeListener, ItemListener
{
	ImageIcon not, and ,or;
	JPanel p1, p2, p3;
	JLabel temp, temp1, temp2;
	Checkbox chkand, chkor, chknot;
	JLabel truth;
	private JMenuBar menubar;
	private JMenu file,look,othermetals,help,circolor;
	private JMenuItem exit,open,Windows,Motif,GTK,tutor,bcolor,bdcolor,newhelp,about,credits;
	private JTextArea output;
	private JToolBar toolbar;
	private JButton delete,ticonify,tdeicon,tnew,tsave,topen,tundo;
	static private JTabbedPane tabbedpane;
	private java.awt.Component mainpanel;
	private ImageIcon icon,buttonsi;
	private JTextField text1,text2,num,num1;
	private varFinder1 vf;
	private JLabel b1,b2,b3,b4,b5;
	public JLabel status;
	private static JLabel splashLabel,logixlogo,logixlogo1;
	private String eqnCopy;
	private JCheckBox check1;
	public JPanel statuspanel;
	static private LogixPanel circuitPanel;
	private ButtonGroup group;
	static public JFrame frame;
	public Thread t;
	public String path="images/";

	private	MetalTheme[] themes = { new DefaultMetalTheme(),
  									new GreenMetalTheme(),
									  new AquaMetalTheme(),
									  new KhakiMetalTheme(),
									  new DemoMetalTheme(),
									  new ContrastMetalTheme(),
									  new Banana()};
private boolean closed = false;

   public void init() {
   	   allocateMemory();
   	    additions();
        setShortKeys();
        settingTooltipText(); 
        addListeners();
        setJMenuBar(menubar);
		menubar.setOpaque(true);
  }
   private void allocateMemory() 
	{
		and = new ImageIcon("Images/and1.jpg");
		or = new ImageIcon("Images/or.jpg");
		not = new ImageIcon("Images/not.jpg");
		temp = new JLabel(and);
		temp1 = new JLabel(or);
		temp2 = new JLabel(not);
	   truth = new JLabel("Truth Table");
	   chkand = new Checkbox("AND");
	   chkor = new Checkbox("OR");
	   chknot = new Checkbox("NOT");
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p1.setLayout(new BorderLayout());
		p2.setLayout(new GridLayout(4,1));
		p3.setLayout(new GridLayout(4,1));
		p2.add(truth);
		p2.add(chkand);
		p2.add(chkor);
		p2.add(chknot);
		p1.add(p2, BorderLayout.NORTH);
		p3.add(temp);
		p3.add(temp1);
		p3.add(temp2);
		temp.setVisible(false);
		temp1.setVisible(false);
		temp2.setVisible(false);
		p1.add(p3, BorderLayout.CENTER);
		icon = new ImageIcon("images/middle.gif");
        logixlogo=new JLabel(new ImageIcon("images/aboutus.jpg"));
        logixlogo1=new JLabel(new ImageIcon("images/credits.jpg"));
        menubar=new JMenuBar();
		file=new JMenu("File");
		look=new JMenu("Look & Feel");
		help=new JMenu("Help");
		circolor=new JMenu("Circuit Colours");
		bcolor=new JMenuItem("Board Colors");
		bdcolor=new JMenuItem("Border Colours");
		//newf =new JMenuItem("New",new ImageIcon("images/new.gif"));
		tutor=new JMenuItem("Tutorials");
		open=new JMenuItem("Open",new ImageIcon("images/open.gif"));
		exit=new JMenuItem("Exit",new ImageIcon("images/exit.gif"));
		newhelp=new JMenuItem("Help");
     	Windows=new JMenuItem("Windows");
	    othermetals=new JMenu("JAVA");
   		GTK=new JMenuItem("GTK+");
		Motif=new JMenuItem("Motif");
		about=new JMenuItem("About us..!");
		credits=new JMenuItem("Credits ");
		toolbar=new JToolBar();
		delete=new JButton(new ImageIcon("images/delete24.gif"));
		ticonify=new JButton(new ImageIcon("images/down24.gif"));
		tdeicon=new JButton(new ImageIcon("images/up24.gif"));
		topen=new JButton(new ImageIcon("images/open24.gif"));
		tsave=new JButton(new ImageIcon("images/save24.gif"));
		tnew=new JButton(new ImageIcon("images/new24.gif"));
     	tundo=new JButton(new ImageIcon("images/refresh24.gif"));
     	tabbedpane=new JTabbedPane();
   		status=new JLabel("Mouse at :");
   		statuspanel=new JPanel();
   		group = new ButtonGroup();
   		for (int i = 0; i < themes.length; i++) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem( themes[i].getName() );
		group.add(item);
        othermetals.add( item );
		item.setActionCommand(i+"");
		item.addActionListener(this);
		if ( i == 0)
	    item.setSelected(true);
		add(p1, BorderLayout.EAST);
	}

/*****************Main Logics*******************************/	    
     	vf=new varFinder1();
     	circuitPanel =new LogixPanel(this.frame);
     	mainpanel=makepanel();
/***********************************************************/     	
    }
    
    private void additions() {
		menubar.add(file);
		menubar.add(look);
		menubar.add(circolor);
		menubar.add(help);
	    file.add(open);
	    file.add(exit);
	    look.add(othermetals);
	    look.add(Windows);
   		look.add(GTK);
		look.add(Motif);
		circolor.add(bcolor);
		circolor.add(bdcolor);
		help.add(tutor);
		help.add(newhelp);
		help.add(about);
		help.add(credits);
		toolbar.add(tnew);
		toolbar.add(topen);
		toolbar.add(tsave);
		toolbar.add(delete);
		toolbar.add(ticonify);
		toolbar.add(tdeicon);
		toolbar.add(tundo);
		getContentPane().add(toolbar, java.awt.BorderLayout.NORTH);
		tabbedpane.addTab("Circuit Designer",icon,circuitPanel,"Draw and execute your circuit diagrams");
        tabbedpane.addTab("Reducer",icon,mainpanel,"This reduces entered SOP equations");
		getContentPane().add(tabbedpane);
		getContentPane().add(statuspanel,java.awt.BorderLayout.SOUTH);
		statuspanel.add(status);
    	tabbedpane.setSelectedIndex(0);
   }
   private void setShortKeys() {
        file.setMnemonic('F');
		look.setMnemonic('L');
		help.setMnemonic('H');
   }
   private void settingTooltipText()
   {
   	//newf.setToolTipText("New Sheet");
   	exit.setToolTipText("Exit Logix");
   	open.setToolTipText("Open file");
   	Windows.setToolTipText("Set Windows theme");
   	Motif.setToolTipText("Set Motif theme");
   	GTK.setToolTipText("Set GTK+ theme");
   	tutor.setToolTipText("Go to Tutorials");
   	delete.setToolTipText("Delete");
   	ticonify.setToolTipText("Iconify Circuit");
   	tdeicon.setToolTipText("Deiconify Circuit");
   	tnew.setToolTipText("New Sheet");
   	tsave.setToolTipText("Save");
   	topen.setToolTipText("Open File");
   	tundo.setToolTipText("Undo & Redo");
   	circuitPanel.setToolTipText("Drag and drop to draw gates");
   }
   
   private void addListeners() {
	   chkand.addItemListener(this);
	   chkor.addItemListener(this);
	   chknot.addItemListener(this);
   		tnew.addActionListener(this);
   		topen.addActionListener(this);
   		tsave.addActionListener(this);
   		ticonify.addActionListener(this);
   		tdeicon.addActionListener(this);
   		delete.addActionListener(this);
   		tnew.addMouseMotionListener(this);
   		topen.addMouseMotionListener(this);
   		tsave.addMouseMotionListener(this);
   		ticonify.addMouseMotionListener(this);
   		tdeicon.addMouseMotionListener(this);
   		delete.addMouseMotionListener(this);
   		tundo.addMouseMotionListener(this);
       // newf.addActionListener(this);
        bcolor.addActionListener(this);
        bdcolor.addActionListener(this);
        newhelp.addActionListener(this);
        about.addActionListener(this);
        credits.addActionListener(this);
        tundo.addActionListener(this);
        open.addActionListener(this);
		exit.addActionListener(this);
		tutor.addActionListener(this);
		GTK.addActionListener(this);
		Motif.addActionListener(this);
 	    Windows.addActionListener(this);
 	    tabbedpane.addChangeListener(this);
/********************Adding MouseMotionListeners****************/
        menubar.addMouseMotionListener(this);
        file.addMouseMotionListener(this);
        look.addMouseMotionListener(this);
        help.addMouseMotionListener(this);
        mainpanel.addMouseMotionListener(this);
        b1.addMouseMotionListener(this);
        b2.addMouseMotionListener(this);
        b3.addMouseMotionListener(this);
        b4.addMouseMotionListener(this);
        b5.addMouseMotionListener(this);
        text1.addMouseMotionListener(this);
        text2.addMouseMotionListener(this);
        output.addMouseMotionListener(this);
        num.addMouseMotionListener(this);
        num1.addMouseMotionListener(this);
        check1.addMouseMotionListener(this);
        circolor.addMouseMotionListener(this);
        circuitPanel.clearButton.addMouseMotionListener(this);
        circuitPanel.deIconifyButton.addMouseMotionListener(this);
        circuitPanel.deleteButton.addMouseMotionListener(this);
        circuitPanel.iconifyButton.addMouseMotionListener(this);
        circuitPanel.loadButton.addMouseMotionListener(this);
        circuitPanel.nameInput.addMouseMotionListener(this);
        circuitPanel.powerCheckbox.addMouseMotionListener(this);
        circuitPanel.saveButton.addMouseMotionListener(this);
        circuitPanel.scroll.addMouseMotionListener(this);
        circuitPanel.speedChoice.addMouseMotionListener(this);
        circuitPanel.undoButton.addMouseMotionListener(this);
        menubar.addMouseListener(this);
        file.addMouseListener(this);
        look.addMouseListener(this);
        help.addMouseListener(this);
        mainpanel.addMouseListener(this);
        b1.addMouseListener(this);
        b2.addMouseListener(this);
        b3.addMouseListener(this);
        b4.addMouseListener(this);
        b5.addMouseMotionListener(this);
        text1.addMouseListener(this);
        text2.addMouseListener(this);
        output.addMouseListener(this);
        num.addMouseListener(this);
        num1.addMouseListener(this);
        check1.addMouseListener(this);
        circolor.addMouseListener(this);

   }
	public void itemStateChanged(ItemEvent ie)
	{
		if(ie.getSource()==chkand && chkand.getState()==false)
			temp.setVisible(false);
		else if(ie.getSource()==chkand && chkand.getState()==true)
			temp.setVisible(true);
		if(ie.getSource()==chkor && chkor.getState()==false)
			temp1.setVisible(false);
		else if(ie.getSource()==chkor && chkor.getState()==true)
			temp1.setVisible(true);
		if(ie.getSource()==chknot && chknot.getState()==false)
			temp2.setVisible(false);
		else if(ie.getSource()==chknot && chknot.getState()==true)
			temp2.setVisible(true);
	}
	private java.awt.Component makepanel()
	{
		JPanel qpanel=new JPanel();
        JPanel bpanel=new JPanel();
		JPanel tpanel=new JPanel();
		output=new JTextArea();
		JScrollPane scroll=new JScrollPane(output);
	    qpanel.setLayout(new java.awt.BorderLayout());
		bpanel.setLayout(new java.awt.GridLayout(6,0));
		tpanel.setLayout(new tLayout());
		JSplitPane split=new JSplitPane();
		split.setDividerLocation(140);
		split.setDividerSize(1);
		split.setEnabled(false);
		qpanel.setSize(300,500);
    	 b1=new JLabel("Reduce",new ImageIcon("images/rbs.gif"),SwingConstants.LEADING);
		 b1.addMouseListener(this);
		 b1.setToolTipText("Click here to reduce the entered SOP equation");
		 b2=new JLabel("Standardise",new ImageIcon("images/rbs.gif"),SwingConstants.LEADING);
		 b2.addMouseListener(this);
		 b2.setToolTipText("Click here to standardise the entered SOP equation");
		 b3=new JLabel("Clear",new ImageIcon("images/rbs.gif"),SwingConstants.LEADING);
		 b3.addMouseListener(this);
		 b3.setToolTipText("Click here to clear the equation");
		 b4=new JLabel("Previous",new ImageIcon("images/rbs.gif"),SwingConstants.LEADING);
		 b4.addMouseListener(this);
		 b4.setToolTipText("Click here to view previous entered SOP equation");
		 b5=new JLabel("Example",new ImageIcon("images/rbs.gif"),SwingConstants.LEADING);
		 b5.addMouseListener(this);
		 b5.setToolTipText("Click here to view example");
		 bpanel.add(b1);bpanel.add(b2);bpanel.add(b3);bpanel.add(b4);
		 bpanel.add(b5);
		 split.setLeftComponent(bpanel);
		 output.setText ("");
		 output.setEditable (false);
		 output.setLineWrap (true);
		 output.setToolTipText("Displays standardised equation");
		 output.setBackground(new java.awt.Color(200,195,245));
		 text1=new JTextField(55);
		 text1.setToolTipText("Enter SOP equation");
		 text2=new JTextField(55);
		 text2.setBackground(new java.awt.Color(200,195,245));
		 text2.setEditable(false);
		 text2.setToolTipText("Displays reduced equation");
		 num=new JTextField(10);
		 num.setBackground(new java.awt.Color(200,195,245));
		 num.setEditable(false);
		 num.setToolTipText("Displays no. of terms in the standardised equation");
		 num1=new JTextField(10);
		 num1.setToolTipText("Displays no. of variables in the entered SOP equation");
		 num1.setBackground(new java.awt.Color(200,195,245));
		 num1.setEditable(false);
		 JButton bu1= new JButton("Example");
		 bu1.addActionListener(this);
		 tpanel.add(new JLabel("Enter your equation : "));
		 tpanel.add(text1);
		 tpanel.add(new JLabel("Reduced Equation : "));
		 tpanel.add(text2);
		 tpanel.add(new JLabel("Standardised form : "));
		 check1=new JCheckBox("view both standardised form & reduced form");
		check1.setToolTipText("Click here to view both standarised and reduced equation");
		 tpanel.add(scroll);
		 tpanel.add(check1);
		 tpanel.add(new JLabel("Note : "));
		 split.setRightComponent(tpanel);
		 tpanel.add(new JLabel("Capital letters stand for '1' & small letters stand for '0'. "));
		 tpanel.add(new JLabel("CheckBox overrides both options. "));
		 tpanel.add(new JLabel("This program works only on SOP terms. "));
		 tpanel.add(new JLabel("No. of terms :"));
		 tpanel.add(num);
		 tpanel.add(new JLabel("No. of variables :"));
		 tpanel.add(num1);

		 qpanel.add("Center",split);
		 qpanel.add("Center",split);

	     return qpanel;
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getActionCommand().equals("Exit"))
		         {System.exit(0);}
		else if(ae.getActionCommand().equals("Windows"))
		{
			try {
        	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
                       
           }catch(Exception e){}
        }
		else if(ae.getActionCommand().equals("GTK+"))
		{
			try {
        	UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
            this.getRootPane().updateUI();}catch(Exception e){}
        }
		else if(ae.getActionCommand().equals("Motif"))
		{
			try {
        	UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
            this.getRootPane().updateUI();}catch(Exception e){}
        }
        else if(ae.getSource()==tutor)
        {
        	Runtime r1=Runtime.getRuntime();

        	Process p1=null;
        	try {
        		p1=r1.exec("C:\\Program Files\\Internet Explorer\\Iexplore.exe "+getClass().getResource("tutor/logix.htm"));
        	  }catch(Exception e){}
        }
        else if(ae.getSource()==newhelp)
        {
        	Runtime r1=Runtime.getRuntime();

        	Process p1=null;
        	try {
        		p1=r1.exec("C:\\Program Files\\Internet Explorer\\Iexplore.exe "+getClass().getResource("newhelp/trial.html")); 
        	  }catch(Exception e){}
        }
        else if(ae.getSource()==topen||ae.getSource()==open)
        {
         circuitPanel.doLoad();        
        }
        else if(ae.getSource()==tnew)
         {
        Object[] options = {"Yes I want to","No not this time"};
        int n=JOptionPane.showOptionDialog(null,"Do you want to save the current Circuit","Message",JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);	
        if(n==JOptionPane.YES_OPTION)
		{
			 circuitPanel.doSave();
			 circuitPanel.canvas.doClear();
		}
        else
        circuitPanel.canvas.doClear();
        }
        
        else if(ae.getSource()==tsave)
        {
        circuitPanel.doSave();
        }
        else if(ae.getSource()==ticonify)
        {
        	circuitPanel.canvas.doIconify();
        }
        else if(ae.getSource()==tdeicon)
        {
        	circuitPanel.canvas.doDeIconify();
        }
        else if(ae.getSource()==delete)
        {
        	circuitPanel.canvas.doDelete();
        }
        else if(ae.getSource()==tundo)
        {
        	circuitPanel.canvas.undoer.click();
        	     	
        }
        else if(ae.getSource()==bcolor) {
        
        circuitPanel.canvas.bc.setVisible(true);
        java.awt.Color color=circuitPanel.canvas.bc.showDialog(this,"Board Color",new java.awt.Color(255,255,153));
        circuitPanel.canvas.boardColor=color;
        circuitPanel.canvas.forceCircuitRedraw();
        
        }
        else if (ae.getSource()==bdcolor) {
        circuitPanel.canvas.bdc.setVisible(true);
       java.awt.Color color= circuitPanel.canvas.bdc.showDialog(this,"Border Color",java.awt.Color.cyan);
       circuitPanel.canvas.borderColor=color;
        circuitPanel.canvas.forceCircuitRedraw();

        }
        else if (ae.getSource()==about)
        {
        	JFrame window=new JFrame("About us..");
        	window.setResizable(false);
        	window.setLocation(160,140);
        	window.setSize(450,365);
		  	window.getContentPane().add(logixlogo);
         	window.setVisible(true);
        	window.show();
        	window.addWindowListener(new WindowAdapter() 
        	{
        		public void windowClosing(WindowEvent we) 
        		{
        			destroy();
        		}
        	});
    	}
        else if (ae.getSource()==credits)
        {
        	JFrame window1=new JFrame("Credits");
        	window1.setResizable(false);
        	window1.setSize(450,365);
        	window1.setLocation(160,140);
           	window1.getContentPane().add(logixlogo1);
          	window1.setVisible(true);
        	window1.show();
        	window1.addWindowListener(new WindowAdapter()
        	 {
        		public void windowClosing(WindowEvent we)
        		{
        			destroy();
        		}
    		});
        }
        else 
        {
        	    String numStr = ae.getActionCommand();
    MetalTheme selectedTheme = themes[ Integer.parseInt(numStr) ];
    MetalLookAndFeel.setCurrentTheme(selectedTheme);
    try {
	UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	SwingUtilities.updateComponentTreeUI(this);
    } catch (Exception ex) {
        System.out.println("Failed loading Metal");
	System.out.println(ex);
       }
	}

}
	
public void stateChanged(ChangeEvent ce)
	{
		if(ce.getSource()==tabbedpane)
		{
			if(tabbedpane.getSelectedIndex()==1)
			{
				toolbar.setEnabled(false);
				tnew.setEnabled(false);
				topen.setEnabled(false);
				tsave.setEnabled(false);
				ticonify.setEnabled(false);
				tdeicon.setEnabled(false);
				delete.setEnabled(false);
				tundo.setEnabled(false);
			}
			else {
				toolbar.setEnabled(true);
				tnew.setEnabled(true);
				topen.setEnabled(true);
				tsave.setEnabled(true);
				ticonify.setEnabled(true);
				tdeicon.setEnabled(true);
				delete.setEnabled(true);
				tundo.setEnabled(true);
			}
		}
	}
	public void mouseMoved(MouseEvent me)
	{
		if(me.getSource()==file)
		status.setText("On File");
		else if(me.getSource()==look)
		status.setText("Click Here to Change the Look & Feel");
		else if(me.getSource()==help)
		status.setText("Want some help? Click Here..!");
		else if(me.getSource()==mainpanel)
		status.setText("Reducer Module");
		else if(me.getSource()==menubar)
		status.setText("On MenuBar");
		else if(me.getSource()==b1)
		status.setText("Click to Reduce");
		else if(me.getSource()==b2)
		status.setText("Click to Standardise");
		else if(me.getSource()==b3)
		status.setText("Clear inputs");
		else if(me.getSource()==b4)
		status.setText("Previous Equation");
		else if(me.getSource()==b5)
		status.setText("An Example");
		else if(me.getSource()==text1)
		status.setText("Enter SOP equation and click on Reduce or Standardise button");
		else if(me.getSource()==text2)
		status.setText("Displays reduced equation");
		else if(me.getSource()==output)
		status.setText("Displays standardised equation");
		else if(me.getSource()==num1)
		status.setText("Displays number of variables in the entered equation");
		else if(me.getSource()==num)
		status.setText("Displays number of terms in the standardised equation");
		else if(me.getSource()==check1)
		status.setText("Click here to view both reduced and standardised equation");
		else if(me.getSource()==circolor)
		status.setText("Click here to change Canvas Colors");
		else if(me.getSource()==circuitPanel.clearButton)
		status.setText("Click here to clear the canvas");
		else if(me.getSource()==circuitPanel.deIconifyButton)
		status.setText("Click here to enlarge the iconified item");
		else if(me.getSource()==circuitPanel.deleteButton)
		status.setText("Click here to delete selected item");
		else if(me.getSource()==circuitPanel.iconifyButton)
		status.setText("Click here to iconify circuit");
		else if(me.getSource()==circuitPanel.loadButton)
		status.setText("Click here to open a file");
		else if(me.getSource()==circuitPanel.nameInput)
		status.setText("Click here to give title to the circuit");
		else if(me.getSource()==circuitPanel.powerCheckbox)
		status.setText("Click here to give power to the circuit");
		else if(me.getSource()==circuitPanel.saveButton)
		status.setText("Click here to save your circuit");
		else if(me.getSource()==circuitPanel.scroll)
		status.setText("Click here to scroll the template");
		else if(me.getSource()==circuitPanel.speedChoice)
		status.setText("Click here to select the speed for circuit operation");
		else if(me.getSource()==circuitPanel.undoButton)
		status.setText("Click here to undo or redo the action");
		
		else if(me.getSource()==tdeicon)
		status.setText("Click here to enlarge the iconified item");
		else if(me.getSource()==delete)
		status.setText("Click here to delete selected item");
		else if(me.getSource()==ticonify)
		status.setText("Click here to iconify circuit");
		else if(me.getSource()==topen)
		status.setText("Click here to open a file");
		else if(me.getSource()==tsave)
		status.setText("Click here to save your circuit");
		else if(me.getSource()==tnew)
		status.setText("Click here to open a new canvas");
		else if(me.getSource()==tundo)
		status.setText("Click here to undo or redo the action");
		else
		status.setText("Mouse at :"+me.getX()+","+me.getY());				
	}
	public void mouseDragged(MouseEvent me){}

	public void mouseClicked(MouseEvent me)
	{
		if(me.getSource()==b1)
		{
			String Sr=new String();
			String Sr1=new String();
			String St=new String();
			eqnCopy= new String();
			Sr=text1.getText();
			eqnCopy=Sr;
			if(check1.isSelected()==false)
			
			{
				try
				{
					Sr1=vf.eqnReducer(Sr);
        		}catch(Exception e){text2.setText(Sr1);}
        		text2.setText(Sr1);
        		output.setText ("");
        		num.setText ("");
        	}
        	if(check1.isSelected())
        	{
        		try
				{
					Sr1=vf.eqnReducer(Sr);
        		}catch(Exception e){System.out.print("ssssssss");}
        		text2.setText(Sr1);
        		St=vf.viewStd(Sr);
        		num.setText (vf.Sz);
        		output.setText(St);
        	}
        	num1.setText(vf.sz2);
        	b1.setIcon(new ImageIcon("images/rbrs.gif"));
		}

		if(me.getSource()==b2)
		{
			String St=new String();
			String Sr=new String();
			String Sr1=new String();
			Sr=text1.getText ();
			if(check1.isSelected()==false)
			{
				St=vf.viewStd(Sr);
				text2.setText ("");
				text2.setEditable (false);
				num.setText (vf.Sz);
			}
			if(check1.isSelected())
			{
				try
				{
					Sr1=vf.eqnReducer(Sr);
        		}catch(Exception e){System.out.print(e);}
        		St=vf.viewStd(Sr);
        		text2.setText(Sr1);
        		num.setText (vf.Sz);
        	}
			output.setText(St);
			num1.setText(vf.sz2);
			b2.setIcon(new ImageIcon("images/rbrs.gif"));
	
		}
		if(me.getSource()==b3)
		{
			text1.setText ("");
			text2.setText ("");
			output.setText ("");
			num.setText ("");
			num1.setText ("");
			b3.setIcon(new ImageIcon("images/rbrs.gif"));
		}
		if(me.getSource()==b4)
		{
			String Sr1=new String();
			text1.setText(eqnCopy);
			b4.setIcon(new ImageIcon("images/rbrs.gif"));

		}
		if(me.getSource()==b5)
		{
			String Str="Type the equation in the text field and press 'Reduce' \n"+
        				"Your reduced equation will be displayed in the other text field\n"+
        				"Enter your equation : \n"+
        				"abc+ab+a\n"+
        				"Reduced Equation : \n"+
        				"a\n";


        	JOptionPane.showMessageDialog(null,Str,"Example",JOptionPane.INFORMATION_MESSAGE);
			b5.setIcon(new ImageIcon("images/rbrs.gif"));
		}
	}
	public void mousePressed(MouseEvent me)
	{
		if(me.getSource()==b1)
			b1.setIcon(new ImageIcon("images/rbrs.gif"));
		if(me.getSource()==b2)
			b2.setIcon(new ImageIcon("images/rbrs.gif"));
		if(me.getSource()==b3)
			b3.setIcon(new ImageIcon("images/rbrs.gif"));
		if(me.getSource()==b4)
			b4.setIcon(new ImageIcon("images/rbrs.gif"));
		if(me.getSource()==b5)
			b5.setIcon(new ImageIcon("images/rbrs.gif"));
	}
	public void mouseReleased(MouseEvent me){}
	public void mouseEntered(MouseEvent me)
	{
		if(me.getSource()==b1)
		{
			b1.setIcon(new ImageIcon("images/rbr.gif"));
			b1.setIconTextGap (15);
		}
		if(me.getSource()==b2)
		{
			b2.setIcon(new ImageIcon("images/rbr.gif"));
			b2.setIconTextGap (15);
		}
		if(me.getSource()==b3)
		{
			b3.setIcon(new ImageIcon("images/rbr.gif"));
			b3.setIconTextGap (15);
		}
		if(me.getSource()==b4)
		{
			b4.setIcon(new ImageIcon("images/rbr.gif"));
			b4.setIconTextGap (15);
		}
		if(me.getSource()==b5)
		{
			b5.setIcon(new ImageIcon("images/rbr.gif"));
			b5.setIconTextGap (15);
		}
	}
	public void mouseExited(MouseEvent me)
	{
		if(me.getSource()==b1)
		{
			b1.setIcon(new ImageIcon("images/rbs.gif"));
			b1.setIconTextGap (5);
		}
		else if(me.getSource()==b2)
		{
			b2.setIcon(new ImageIcon("images/rbs.gif"));
			b2.setIconTextGap (5);
		}
		else if(me.getSource()==b3)
		{
			b3.setIcon(new ImageIcon("images/rbs.gif"));
			b3.setIconTextGap (5);
		}
		else if(me.getSource()==b4)
		{
			b4.setIcon(new ImageIcon("images/rbs.gif"));
			b4.setIconTextGap (5);
		}
		else if(me.getSource()==b5)
		{
			b5.setIcon(new ImageIcon("images/rbs.gif"));
			b5.setIconTextGap (5);
		}
		else  status.setText("Logix");
		
	}
	public void showSplashScreen()
	{
		JWindow window=new JWindow();
		t=new Thread();
		//progress=new JProgressBar(0,100);
		//progress.setStringPainted(true);
		splashLabel=new JLabel(new ImageIcon("images/lognew.gif"));
		window.getContentPane().add(splashLabel);
		//window.getContentPane().add("South",progress);
		window.setSize(530,350);
		window.setLocation(150,100);
		window.show();
		try {
			Thread.sleep(2500);
		}catch(Exception e){}
		window.dispose();
		
		return ;
  }


	public static void main(String args[])
	{
	    frame = new JFrame("Logix");
	    JAVA applet=new JAVA();
		applet.showSplashScreen();
		
		frame.setIconImage(new ImageIcon("images/logixicon.jpg").getImage());
 	    frame.setBounds(30,0,700,620);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
       	if(tabbedpane.getSelectedIndex()==0) {
        Object[] options = {"Yes I want to","No not this time"};
        int n=JOptionPane.showOptionDialog(null,"Do you want to save the current Circuit","Message",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);	
        if(n==JOptionPane.YES_OPTION)
        circuitPanel.doSave();
        else
        System.exit(0);}
            }
        });

        applet.init();

        frame.getContentPane().add("Center", applet);
        frame.pack();
        frame.setVisible(true);
        circuitPanel.doLoad();
            
	}

}


class tLayout implements java.awt.LayoutManager {

    public tLayout() {
    }

    public void addLayoutComponent(String name, java.awt.Component comp) {
    }

    public void removeLayoutComponent(java.awt.Component comp) {
    }

    public java.awt.Dimension preferredLayoutSize(java.awt.Container parent) {
        java.awt.Dimension dim = new java.awt.Dimension(0, 0);

        java.awt.Insets insets = parent.getInsets();
        dim.width =520 + insets.left + insets.right;
        dim.height = 540 + insets.top + insets.bottom;

        return dim;
    }

    public java.awt.Dimension minimumLayoutSize(java.awt.Container parent) {
        java.awt.Dimension dim = new java.awt.Dimension(0, 0);
        return dim;
    }

    public void layoutContainer(java.awt.Container parent) {
        java.awt.Insets insets = parent.getInsets();

        java.awt.Component c;
        c = parent.getComponent(0);
        if (c.isVisible()) {c.setBounds(insets.left+45,insets.top+15,120,24);}
        c = parent.getComponent(1);
        if (c.isVisible()) {c.setBounds(insets.left+45,insets.top+50,350,24);}
        c = parent.getComponent(2);
        if (c.isVisible()) {c.setBounds(insets.left+45,insets.top+80,120,24);}
        c = parent.getComponent(3);
        if (c.isVisible()) {c.setBounds(insets.left+45,insets.top+115,350,24);}
        c = parent.getComponent(4);
        if (c.isVisible()) {c.setBounds(insets.left+45,insets.top+150,120,24);}
        c = parent.getComponent(5);
        if (c.isVisible()) {c.setBounds(insets.left+45,insets.top+185,350,100);}
        c = parent.getComponent(6);
        if (c.isVisible()) {c.setBounds(insets.left+45,insets.top+300,300,24);}
        c = parent.getComponent(7);
        if (c.isVisible()) {c.setBounds(insets.left+45,insets.top+350,300,24);}
       // c = parent.getComponent(8);
        //if (c.isVisible()) {c.setBounds(insets.left+420,insets.top+50,100,24);}
        c = parent.getComponent(8);
        if (c.isVisible()) {c.setBounds(insets.left+100,insets.top+350,320,22);}
        c = parent.getComponent(9);
        if (c.isVisible()) {c.setBounds(insets.left+100,insets.top+365,320,22);}
        c = parent.getComponent(10);
        if (c.isVisible()) {c.setBounds(insets.left+100,insets.top+380,320,22);}
        c = parent.getComponent(11);
        if (c.isVisible()) {c.setBounds(insets.left+420,insets.top+150,120,24);}
        c = parent.getComponent(12);
        if (c.isVisible()) {c.setBounds(insets.left+420,insets.top+185,100,24);}
        c = parent.getComponent(13);
        if (c.isVisible()) {c.setBounds(insets.left+420,insets.top+15,100,24);}
        c = parent.getComponent(14);
        if (c.isVisible()) {c.setBounds(insets.left+420,insets.top+50,100,24);}
    }
    
}

