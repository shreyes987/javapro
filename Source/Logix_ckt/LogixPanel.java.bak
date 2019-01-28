//package Logix_ckt;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.applet.*;


public class LogixPanel extends JPanel implements ActionListener,ItemListener,AdjustmentListener,MouseWheelListener
 {

  public JButton deleteButton, undoButton, iconifyButton, deIconifyButton, loadButton, saveButton, clearButton; //newButton;
  public JCheckBox powerCheckbox;
  public LogixCktCanvas canvas;
  public JScrollBar scroll;
  public JTextField nameInput;
  public JComboBox speedChoice;
  public  JPanel circuit;
  public boolean canSave = true;
  public boolean canLoad = true;
  public JFrame owner;
  public String path="images/";

   public LogixPanel(JFrame owner) 
	{
   	   this.owner=owner;
   
       this.setBackground(Color.white);
       this.setOpaque(true);
       this.setToolTipText("You are on CircuitPanel");
       

       JPanel bottom = new JPanel();
       JPanel top = new JPanel();
       
       deleteButton = new JButton("Delete",new ImageIcon("images/rbs.gif"));
       deleteButton.setToolTipText("Click here to delete the selected item");
       deleteButton.addActionListener(this);
       undoButton = new JButton("Undo",new ImageIcon("images/rbs.gif"));
       undoButton.setToolTipText("Click here to undo the last action");
       undoButton.addActionListener(this);
       iconifyButton = new JButton("Iconify",new ImageIcon("images/rbs.gif"));
       iconifyButton.setToolTipText("Click here to iconify the circuit");
       iconifyButton.addActionListener(this);
       deIconifyButton = new JButton("Enlarge",new ImageIcon("images/rbs.gif"));
       deIconifyButton.setToolTipText("Click here to enlarge selected iconified item");
       deIconifyButton.addActionListener(this);
       loadButton = new JButton("Load",new ImageIcon("images/rbs.gif"));
       loadButton.setToolTipText("Click here to load saved files");
       loadButton.addActionListener(this);
       saveButton = new JButton("Save",new ImageIcon("images/rbs.gif"));
       saveButton.setToolTipText("Click here to save your circuit");
       saveButton.addActionListener(this);
       clearButton = new JButton("Clear",new ImageIcon("images/rbs.gif"));
       clearButton.setToolTipText("Click here to clear the circuit");
       clearButton.addActionListener(this);
 //      newButton = new Button("New");
       powerCheckbox = new JCheckBox("Power ",new ImageIcon("images/offline.gif"));
       powerCheckbox.setToolTipText("Click here to power the circuit \n  Red indicates 1 and no power indicates 0");
       powerCheckbox.addItemListener(this);
       deleteButton.setEnabled(false);
       undoButton.setEnabled(false);
       deIconifyButton.setEnabled(false);
       
       deleteButton.setRolloverIcon(new ImageIcon("images/rbr.gif"));
       undoButton.setRolloverIcon(new ImageIcon("images/rbr.gif"));
       iconifyButton.setRolloverIcon(new ImageIcon("images/rbr.gif"));
       deIconifyButton.setRolloverIcon(new ImageIcon("images/rbr.gif"));
       loadButton.setRolloverIcon(new ImageIcon("images/rbr.gif"));
       saveButton.setRolloverIcon(new ImageIcon("images/rbr.gif"));
       clearButton.setRolloverIcon(new ImageIcon("images/rbr.gif"));
       
       deleteButton.setPressedIcon(new ImageIcon("images/rbp.gif"));
       undoButton.setPressedIcon(new ImageIcon("images/rbp.gif"));
       iconifyButton.setPressedIcon(new ImageIcon("images/rbp.gif"));
       deIconifyButton.setPressedIcon(new ImageIcon("images/rbp.gif"));
       loadButton.setPressedIcon(new ImageIcon("images/rbp.gif"));
       saveButton.setPressedIcon(new ImageIcon("images/rbp.gif"));
       clearButton.setPressedIcon(new ImageIcon("images/rbp.gif"));
       
       powerCheckbox.setSelectedIcon(new ImageIcon("images/online.gif"));
       
       nameInput = new JTextField("Untitled",12);
       
       speedChoice = new JComboBox();
       speedChoice.addItem("Fast");
       speedChoice.addItem("Moderate");
       speedChoice.addItem("Slow");
       speedChoice.addActionListener(this);
       
       top.add(clearButton);
       top.add(loadButton);
       top.add(saveButton);
       top.add(new JLabel("  Title:"));
       top.add(nameInput);
       

       bottom.add(speedChoice);
       bottom.add(powerCheckbox);
       bottom.add(deleteButton);
       bottom.add(undoButton);
       bottom.add(iconifyButton);
       bottom.add(deIconifyButton);
       
       circuit = new JPanel();
       scroll = new JScrollBar(JScrollBar.VERTICAL);
       scroll.addAdjustmentListener(this);
       scroll.addMouseWheelListener(this);
       canvas = new LogixCktCanvas(this,scroll,undoButton);
       circuit.setBackground(Color.white);
       circuit.setLayout(new BorderLayout());
       circuit.add("Center",canvas);
       circuit.add("West",scroll);
       //circuit.setToolTipText("Drag and drop to draw gates");
                    
       this.setLayout(new BorderLayout());
       this.add("North",top);
       this.add("South",bottom);
       this.add("Center",circuit);
   }
   
   public void loadURL(java.net.URL url) {  // called from applet during first start()
      canvas.loadURL(url);
   }
   
   public void loadFile(String fileName) {
      canvas.loadFile(fileName,null);
   }

   public void start() {
      canvas.start();
   }
   
   public void stop() {
      canvas.OSC = null;
      canvas.OSG = null;
      canvas.stop();
   }
   
   public void destroy() {
      if (canvas.runner != null && canvas.runner.isAlive())
         canvas.runner.stop();
      if (canvas.loader.runner != null && canvas.loader.runner.isAlive())
         canvas.loader.runner.stop();
   }
   
  public void doLoad() {
      if (!canLoad)
         return;
      JFileChooser fd = null;
      try {
        Container c = this;  // find frame that contains this panel
        do {
           Container p = c.getParent();
           if (p == null)
              break;
           c = p; 
        } while (true);
        if (!(c instanceof JFrame))
           c = null;
        fd = new JFileChooser();
       int r=fd.showOpenDialog(c);
       if(r==1) return;
      }
      catch (AWTError e) {  // thrown by Netscape 3.0 on attempt to use file dialog
        canvas.setMessage("ERROR while trying to create a file dialog box.+It will not be possible to save files.");
        canLoad = false;
        loadButton.setEnabled(false);
        return;
      }
      catch (RuntimeException re) { // illegal typecast, maybe?
        canvas.setMessage("ERROR while trying to create a file dialog box.+It will not be possible to save files.");
        canLoad = false;
        loadButton.setEnabled(false);
        return;
      }
      String fileName = fd.getSelectedFile().getName();
      if (fileName == null)
         return;
      String dir = fd.getCurrentDirectory().getAbsolutePath();
      canvas.loadFile(fileName,dir);
       owner.setTitle("Logix:-"+fileName);
   }
   
   public void doSave() {
      if (!canSave)
         return;
      String fileName = null,directory = null;
      JFileChooser fd = null;
      try {
         Container c = this;  // find frame that contains this panel
         do {
            Container p = c.getParent();
            if (p == null)
               break;
            c = p; 
         } while (true);
         if (!(c instanceof JFrame))
            c = null;
         fd = new JFileChooser();
         int returnval=fd.showSaveDialog(c);
         if(returnval==1) return ;
      }
      catch (RuntimeException re) {
          canvas.setMessage("ERROR while trying to create a file dialog box.+It will not be possible to save files.");
          canSave = false;
          saveButton.setEnabled(false);
          return;
      }
      fileName = fd.getSelectedFile().getName();
      if (fileName == null)
         return;
      directory = fd.getCurrentDirectory().getAbsolutePath();
      canvas.saveToFile(fileName,directory);
   }
      
   public void actionPerformed(ActionEvent ae) {
      if (ae.getSource() == saveButton)
         doSave();
      else if (ae.getSource() == loadButton)
         doLoad();
      else if (ae.getSource() == clearButton)
         canvas.doClear();
      else if (ae.getSource() == iconifyButton)
         canvas.doIconify();
      else if (ae.getSource() == deleteButton)
         canvas.doDelete();
      else if (ae.getSource() == undoButton)
         canvas.undoer.click();
      else if (ae.getSource() == deIconifyButton)
         canvas.doDeIconify();
      else if (ae.getSource() == speedChoice)
         canvas.doSpeedChoice(speedChoice.getSelectedIndex());
      else
       return ;
   }
   public void itemStateChanged(ItemEvent i)
   {
   //	Graphics g;
   	int e=i.getStateChange();
   	if(e==1)
   	canvas.doPower(true);
   	else
   	canvas.doPower(false);

   }
   public void adjustmentValueChanged(AdjustmentEvent e)
   {
   	 if (e.getID() == Event.SCROLL_LINE_DOWN || e.getID() == Event.SCROLL_LINE_UP || 
         e.getID() == Event.SCROLL_PAGE_DOWN || e.getID() == Event.SCROLL_PAGE_UP || 
          e.getID() == Event.SCROLL_ABSOLUTE) {
         canvas.scroller.doScroll();
         return ;
      }
        	
   }
   public void mouseWheelMoved(MouseWheelEvent e) 
	  {
   	   	 if (e.getID() == Event.SCROLL_LINE_DOWN || e.getID() == Event.SCROLL_LINE_UP || 
         e.getID() == Event.SCROLL_PAGE_DOWN || e.getID() == Event.SCROLL_PAGE_UP || 
          e.getID() == Event.SCROLL_ABSOLUTE) {
         canvas.scroller.doScroll();
         return ;
      }

   	
   }
     

}