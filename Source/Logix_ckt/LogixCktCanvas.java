//package Logix_ckt;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Vector;
import javax.swing.*;
import java.awt.event.*;


public class LogixCktCanvas extends Canvas implements Runnable,MouseListener,MouseMotionListener
 {

   LogixPanel owner;
   
   boolean power;
   int c=0;
   public JColorChooser bc,bdc;
   
   int width = -1;
   int height = -1;
   int circuitWidth;
   int circuitHeight;
   static final int LEFT = 74;
   Image OSC;
   Graphics OSG;

  public  Color boardColor = new Color(255,255,255);
  public  Color borderColor = new Color(47,86,180);
   
   int untitledCt = 0;
   
   Logixscroll scroller;
   public Logixundo undoer;
   LogixIO loader;
   
   Vector circuitStack = new Vector();  // other circuits besides current one
   LogixCkt currentCircuit;
   
   final static int IDLE = 0, RUNNING = 1, DRAGGING = 2, DRAWING = 3, STOPPED = 4, LOADING = 5, MESSAGE = 6;
   int state = IDLE;   // STOPPED state is only for an applet whose stop() method has been called, at a time when the state was RUNNING
   Thread runner;
   
   
   boolean circuitChanged;
   int computeDelay = 5;

   LogixCktCanvas(LogixPanel owner, JScrollBar scr, JButton undoButton) {
      this.owner = owner;
      scroller = new Logixscroll(this,scr);
      undoer = new Logixundo(this,undoButton);
      loader = new LogixIO(this);
      setBackground(Color.white);
      currentCircuit = new LogixCkt();
      bc=new JColorChooser();
      bdc=new JColorChooser();
      addMouseListener(this);
      addMouseMotionListener(this);
      c=0;
   }
   
   // --------------------------------- shared state variables --------------------------
   
   final synchronized void setState(int state) {
      this.state = state;
      if (state == RUNNING)
         notify();
   }
   
   final synchronized int getState() {
      return state;
   }
   
   final synchronized void circuitHasChanged() {
      circuitChanged = true;
   }
   
   final synchronized int getComputeDelay() {
      return computeDelay;
   }
   
   // --------------------------------------- start/stop -------------------------------------
   
   void start() {
      if (getState() == STOPPED)
         setState(RUNNING);
      undoer.cancel();
      owner.undoButton.setEnabled(false);  // only here because Netscape didin't properly disable the buttons at startup
      if (selectedItem == null) {
        owner.deleteButton.setEnabled(false);
        owner.deIconifyButton.setEnabled(false);
      }
   }
   
   void stop() {
      if (getState() == RUNNING)
        setState(STOPPED);
   }
   
   //  -----------------------------------------------------------------------------

   public void reshape(int x, int y, int width, int height) {
      super.reshape(x,y,width,height);
      if (this.width != width || this.height != height) {
         undoer.cancel();
         this.width = width;
         this.height = height;
         circuitWidth = width - LEFT;
         circuitHeight = height - 1;
         OSC = null;
         scroller.reshape(LEFT,height);
         currentCircuit.reshape(5,5,circuitWidth-10,circuitHeight-10);
         if (selectedItem != null && !resizer.hidden) {
            if (selectionInScroller)
               resizer.show(selectedItem.boundingBox.x, selectedItem.boundingBox.y, 
                             selectedItem.boundingBox.width, selectedItem.boundingBox.height);
            else
               resizer.show(selectedItem.boundingBox.x+LEFT, selectedItem.boundingBox.y, 
                             selectedItem.boundingBox.width, selectedItem.boundingBox.height);
         }
      }
   }
   
   public void update(Graphics g) {
   	
      paint(g);
      
   }
   
   synchronized public void paint(Graphics g) {
      if (state == LOADING || state == MESSAGE) {
         putMessage(g);
         return;
      }
      Rectangle clip = g.getClipBounds();
      if (clip.x + clip.width > LEFT) {
        if (OSC == null) {
           circuitChanged = true;
           try {
              OSC = createImage(circuitWidth,circuitHeight);
              OSG = OSC.getGraphics();
              OSG.setFont(this.getFont());
           }
           catch (OutOfMemoryError e) {
              OSC = null;
              OSG = null;
              circuitChanged = false;
           }
        }
        if (OSG == null) {
           setMessage("Out of memory.  Try using a smaller window.");
           return;
        }
        if (circuitChanged) {
            OSG.setColor(boardColor);
            OSG.fillRect(0,0,width-LEFT,height-1);
            OSG.setColor(borderColor);
            OSG.drawRoundRect(5,5,circuitWidth-10,circuitHeight-10,15,15);
            currentCircuit.draw(OSG);
            OSG.setColor(Color.black);
            OSG.drawLine(0,0,circuitWidth,0);
            if (getState() == DRAWING) {
               OSG.setColor(Color.green);
               OSG.drawLine(lineSourceNub.connect_x,lineSourceNub.connect_y,newline_x,newline_y);
               
            }
            circuitChanged = false;
        }
        g.drawImage(OSC,LEFT,0,this);
      }
      if (clip.x < LEFT)
         scroller.draw(g);
      g.setColor(Color.black);
      g.drawLine(0,height-1,width,height-1);
      g.drawLine(LEFT-1,0,LEFT-1,height);
      if (selectedItem != null && selectionInScroller && getState() == DRAGGING)
         selectedItem.draw(g);
      if (selectedItem != null)
         resizer.draw(g);
      if(power==true)
      {
      	g.setColor(new Color(255,0,0));
      	g.drawString("Red stands for '1' and no colour stands for '0'",100,25);
      	g.drawString("Click on the 'input' to power that input with logic '1'",100,40);
      	//setPowerMessage();
      	//repaint();
   	}
   }
   
   void setPowerMessage() 
   {
   		
   		if(c==0)
   		{
   			c++;
   			JOptionPane.showMessageDialog(null,"Red stands for '1' and no colour stands for '0' \n Click on the 'input tack' to power that input with logic '1'","Help",JOptionPane.INFORMATION_MESSAGE);
   		}	
   			
   		//OSG.drawString("Red stands for '1' and no colour stands for '0'");
      	//OSG.drawString("Click on the 'input' to power that input with logic '1'");  
      	//OSG.drawString("jgfdhgfs",x,y);
   }
   public void forceCircuitRedraw() {
   	
      circuitHasChanged();
      repaint(LEFT,0,circuitWidth,circuitHeight);
   }

   
   //------------------------------------- Respond to buttons --------------------------
   
   synchronized void doSpeedChoice(int selectedIndex) {
      if (selectedIndex == 0)
         computeDelay = 5;
      else if (selectedIndex == 1)
         computeDelay = 100;
      else
         computeDelay = 1000;
      if (state == RUNNING)
         notify();
   }
   
   public synchronized void doDeIconify() {
      undoer.cancel();
      if (selectedItem == null || !(selectedItem instanceof LogixCkt))
         return;
      currentCircuit.name = owner.nameInput.getText();
      LogixCkt source;
      LogixCkt oldCircuit = currentCircuit;
      LogixCkt newCircuit = (LogixCkt)selectedItem;
      if (selectionInScroller) {
         scroller.deleteItem((LogixCkt)selectedItem); // can set selectedItem = null
         source = null;
         owner.iconifyButton.setText("Iconify");
         circuitStack.setSize(0);
      }
      else {
         source = currentCircuit;
         owner.iconifyButton.setText("Shrink");
         circuitStack.addElement(currentCircuit);
      }
      newCircuit.selected = false;
      selectedItem = null;
      owner.deleteButton.setEnabled(false);
      owner.deIconifyButton.setEnabled(false);
      newCircuit.deiconify(5,5,circuitWidth-10,circuitHeight-10,source); 
      currentCircuit = newCircuit;
      owner.nameInput.setText(currentCircuit.name);
      if ( source == null ) {
          while (oldCircuit.saveContainerWhileEnlarged != null) {
              LogixCkt next = oldCircuit.saveContainerWhileEnlarged;
              Rectangle r = oldCircuit.boundingBoxInContainer.getIntRect();
              oldCircuit.iconify(r.x,r.y,r.width,r.height);
              oldCircuit = next;
          }
          if ( oldCircuit.items.size() != 0 || oldCircuit.lines.size() != 0 
                                 || oldCircuit.inputs.size() != 0 || oldCircuit.outputs.size() != 0 )  {
             oldCircuit.iconify(0,0,LEFT-20,LEFT-20);
             scroller.addItem(oldCircuit);
          }
      }
      forceCircuitRedraw();
   }
   
   
   public synchronized void doIconify() {
      if (currentCircuit.inputs.size() == 0 && currentCircuit.outputs.size() == 0 && currentCircuit.items.size() == 0)
         return;
      undoer.cancel();
      if (selectedItem != null && !selectionInScroller) {
         selectedItem.selected = false;
         selectedItem = null;
      }
      currentCircuit.name = owner.nameInput.getText();
      LogixCkt container = currentCircuit.saveContainerWhileEnlarged;
      LogixCkt oldCircuit = currentCircuit;
      if (container == null) {
         currentCircuit.iconify(0,0,LEFT-20,LEFT-20);
         currentCircuit = new LogixCkt();
         owner.nameInput.setText("Untitled " + (++untitledCt));
         scroller.addItem(oldCircuit);
         selectItem(oldCircuit,true);
      }
      else {
         Rectangle r = currentCircuit.boundingBoxInContainer.getIntRect();
         currentCircuit.iconify(r.x,r.y,r.width,r.height);
         currentCircuit = (LogixCkt)circuitStack.lastElement();  // same as container; I could get rid of circuitStack (but it's used in Undoer and IOManager
         circuitStack.setSize(circuitStack.size() - 1);
         owner.nameInput.setText(currentCircuit.name);
         selectItem(oldCircuit,false);
      }
      if (currentCircuit.saveContainerWhileEnlarged == null)
         owner.iconifyButton.setText("Iconify");
      currentCircuit.reshape(5,5,circuitWidth-10,circuitHeight-10);
      forceCircuitRedraw();
   }
   
   public synchronized void doDelete() {
      if (selectedItem == null)
        return;
      if (selectionInScroller) {
         selectedItem.selected = false;
         if (selectedItem instanceof LogixCkt) {  // should always pass
            undoer.setDeleteFromScrollData(selectedItem,scroller.topItem);
            scroller.deleteItem((LogixCkt)selectedItem);
         }
         selectedItem = null;
      }
      else {
         undoer.setDeleteData(selectedItem);
         selectedItem.delete(currentCircuit);
         selectedItem.selected = false;
         selectedItem = null;
         owner.deleteButton.setEnabled(false);
         owner.deIconifyButton.setEnabled(false);
         forceCircuitRedraw();
      }
   }
   
  public synchronized void doClear() {
      if (state == MESSAGE || state == LOADING) {
         clearMessage();
         return;
      }
      selectItem(null,false);
      if (currentCircuit.saveContainerWhileEnlarged == null) {
          undoer.setClearMainCircuitData(currentCircuit.items,currentCircuit.lines,currentCircuit.inputs,currentCircuit.outputs);
          currentCircuit.inputs = new Vector();
          currentCircuit.outputs = new Vector();
      }
      else {
          undoer.setClearSubCircuitData(currentCircuit.items,currentCircuit.lines);
          for (int i = 0; i < currentCircuit.inputs.size(); i++) {
             LogixCktNub in = (LogixCktNub)currentCircuit.inputs.elementAt(i);
             in.destination.setSize(0);
          }
          for (int i = 0; i < currentCircuit.outputs.size(); i++) {
             LogixCktNub out = (LogixCktNub)currentCircuit.outputs.elementAt(i);
             out.source = null;
          }
      }
      currentCircuit.items = new Vector();
      currentCircuit.lines = new Vector();
      if (selectedItem != null && !selectionInScroller) {
         owner.deleteButton.setEnabled(false);
         owner.deIconifyButton.setEnabled(false);
         selectedItem.selected = false;
         selectedItem = null;
      }
      owner.nameInput.setText("");
      forceCircuitRedraw();
   }
   
   synchronized void doPower(boolean on) {
   	power=on;
      if (on) {
      	repaint();
         setState(RUNNING);
         if (runner == null || !runner.isAlive()) {
            runner = new Thread(this,"LogixCkt Runner");
            runner.start();
         }
      }
      else {
         setState(IDLE);
         currentCircuit.powerOff();
         forceCircuitRedraw();
      }
   }
   
   
   //----------------------- File/URL IO ---------------------------------------
   
   
   synchronized void loadURL(URL url) {  // called from applet during first start()
      try {
         InputStream in = url.openConnection().getInputStream();
         if (!loader.startReading(in))
            throw new IOException("Internal Error:  Another file is being loaded.");
         loadMessage("the URL" + url.toString());
      }
      catch (IOException e) {
         setMessage("An input error occured while+opening the URL " + url.toString() + " +(" + e + ")");
      }
      catch (SecurityException e) {
         setMessage("A security error occured while+trying to open the URL " + url.toString() +".+(" + e + ")");
      }
      catch (Throwable e) {
         setMessage("An error occured while+trying to open the URL " + url.toString() +".+(" + e + ")");
      }
   }
   
   synchronized void loadFile(String fileName, String directoryName) {
      try {
         File file = new File(directoryName,fileName);
         InputStream in = new FileInputStream(file);
         if (!loader.startReading(in))
            throw new IOException("Internal Error:  Another file is being loaded.");
         loadMessage("the file " + fileName);
      }
      catch (IOException e) {
         JOptionPane.showMessageDialog(null,"An Error occurred\nwhile opening the specified file:-"+fileName+"\n"+e,"Error",JOptionPane.ERROR_MESSAGE);
      }
      catch (SecurityException e) {
         setMessage("A security error occured while+trying to open the file " + fileName +".+(" + e + ")");
      }
      catch (Throwable e) {
         setMessage("An error occured while+trying to open the file " + fileName +".+(" + e + ")");
      }
   }
   
   synchronized void saveToFile(String fileName, String directoryName) {
      PrintStream out = null;
      try {
         File file = new File(directoryName,fileName);
         System.out.println("File:"+fileName+" & dir:"+directoryName);
         out = new PrintStream(new FileOutputStream(file));
      }
      catch (IOException e) {
         setMessage("An error occured while+opening the file " + fileName +".+(" + e + ")");
         return;
      }
      catch (SecurityException e) {
         setMessage("A security error occured while+trying to open the file " + fileName +".+(" + e + ")");
         return;
      }
      try {
         Vector scrollItems = new Vector();
         for (int i = scroller.standardItemCt; i < scroller.items.size(); i++)
            scrollItems.addElement(scroller.items.elementAt(i));
         currentCircuit.name = owner.nameInput.getText();
         loader.writeCircuitData(out,circuitStack,currentCircuit,scrollItems);
      }
      catch (IOException e) {
         setMessage("An output error occured while+writing to the file " + fileName +".+(" + e + ")");
     }
      catch (Throwable e) {
         setMessage("An error occured while+writing to the file " + fileName +".+(" + e + ")");
      }
      out.close();
   }
   
   synchronized void doneLoading(Vector circuitStackLoaded, LogixCkt currentCircuitLoaded, Vector scrollItemsLoaded) {
      selectItem(null,false);
      currentCircuit.name = owner.nameInput.getText();
      Vector scrollerCircuits = scroller.deleteAllCircuits();
      undoer.setLoadData(circuitStack,scrollerCircuits,currentCircuit);
      circuitStack = circuitStackLoaded;
      currentCircuit = currentCircuitLoaded;
      currentCircuit.reshape(5,5,circuitWidth-10,circuitHeight-10);
      owner.nameInput.setText(currentCircuit.name);
      owner.deIconifyButton.setEnabled(false);
      if (currentCircuit.saveContainerWhileEnlarged != null)
         owner.iconifyButton.setText("Shrink");
      else
         owner.iconifyButton.setText("Iconify");
      if (scrollItemsLoaded.size() != 0)
         scroller.addItems(scrollItemsLoaded);
      circuitHasChanged();
      setState(IDLE);
      clearMessage();
   }
   
   synchronized void doneLoadingWithError(String errorMessage) {
      setMessage("An error occurred while reading data:+" + errorMessage);
   }
   
   String message;
   
   synchronized void setMessage(String message) {
      if (state != IDLE) {
         state = IDLE;
         currentCircuit.powerOff();
         circuitHasChanged();
         owner.powerCheckbox.setEnabled(false);
      }
      state = MESSAGE;
      this.message = message;
      turnOffControls();
      repaint();
   }
   
   synchronized void loadMessage(String fileName) {
      if (state != IDLE) {
         state = IDLE;
         currentCircuit.powerOff();
         circuitHasChanged();
         owner.powerCheckbox.setEnabled(false);
      }
      setMessage("LOADING new circuit data from+" + fileName);
      state = LOADING;
      turnOffControls();
      repaint();
   }
   
   synchronized void clearMessage() {
      if (state == LOADING)
         loader.cancelLoad();
      setState(IDLE);
      message = null;
      restoreControls();
      repaint();
   }
   
   void putMessage(Graphics g) {
      g.setColor(Color.white);
      g.fillRect(0,0,width,height);
      g.setColor(Color.black);
      g.drawLine(0,0,width,0);
      g.drawLine(0,height-1,width,height-1);
      FontMetrics fm = g.getFontMetrics();
      int y = 3*fm.getHeight();
      int a = 0;
      while (a < message.length()) {
         int b = message.indexOf("+",a);
         if (b == -1)
            b = message.length();
         String str = message.substring(a,b);
         g.drawString(str, 25, y);
         y += fm.getHeight() + 3;
         a = b+1;
      }
      y += fm.getHeight();
      g.setColor(Color.red);
      if (getState() == LOADING)
         g.drawString("Click \"Clear\" button to abort.",25,y);
      else
         g.drawString("Click mouse to continue.",25,y);
   }
   
   
   void turnOffControls() {
      owner.deleteButton.setEnabled(false);
      owner.undoButton.setEnabled(false);
      owner.iconifyButton.setEnabled(false);
      owner.deIconifyButton.setEnabled(false);
      owner.loadButton.setEnabled(false);
      owner.saveButton.setEnabled(false);
 //     owner.newButton.setEnabled(false);
      owner.powerCheckbox.setEnabled(false);
      owner.scroll.setEnabled(false);
   }
   
   void restoreControls() {
      if (selectedItem != null) {
         owner.deleteButton.setEnabled(true);
         if (selectedItem instanceof LogixCkt)
            owner.deIconifyButton.setEnabled(true);
      }
      if (undoer.status != Logixundo.NONE)
         owner.undoButton.setEnabled(true);
      owner.iconifyButton.setEnabled(true);
      if (owner.canLoad)
         owner.loadButton.setEnabled(true);
      if (owner.canSave)
         owner.saveButton.setEnabled(true);
//      owner.newButton.setEnabled(true);
      owner.powerCheckbox.setEnabled(true);
      owner.scroll.setEnabled(true);
   }
   

   
   //----------------------- Selected Item Handling ----------------------------

   LogixCktIT selectedItem = null;
   boolean selectionInScroller;
   LogixRB resizer = new LogixRB();

   
   synchronized void selectItem(LogixCktIT it, boolean inScroller) {
      if (it == selectedItem)
         return;
      if (selectedItem != null) {
         if (!resizer.hidden)
            resizer.hide();
         selectedItem.selected = false;
         Rectangle r = selectedItem.getCopyOfBoundingBox(false);
         if (!selectionInScroller)
            r.x += LEFT;
         repaint(r.x,r.y,r.width,r.height);
         selectionInScroller = false;
      }
      selectedItem = it;
      if (it == null) {
         owner.deleteButton.setEnabled(false);
         owner.deIconifyButton.setEnabled(false);
      }
      else {
         it.selected = true;
         selectionInScroller = inScroller;
         if (selectionInScroller)
            scroller.setSelection(it);
         if (it instanceof LogixCkt || (!selectionInScroller && it instanceof LogixGates)) {
            if (selectionInScroller)
               resizer.show(it.boundingBox.x,it.boundingBox.y,
                                it.boundingBox.width,it.boundingBox.height);
            else
               resizer.show(it.boundingBox.x+LEFT,it.boundingBox.y,
                                it.boundingBox.width,it.boundingBox.height);
         }
         else
            resizer.hide();
         if ((it instanceof LogixCkt || !selectionInScroller) && getState() != DRAWING) {
            owner.deleteButton.setEnabled(true);
            if (it instanceof LogixCkt)
               owner.deIconifyButton.setEnabled(true);
            else
               owner.deIconifyButton.setEnabled(false);
         }
         else {
            owner.deleteButton.setEnabled(false);
            owner.deIconifyButton.setEnabled(false);
         }
         Rectangle r = it.getCopyOfBoundingBox(false);
         if (!selectionInScroller)
            r.x += LEFT;
         repaint(r.x,r.y,r.width,r.height);
      }
      circuitHasChanged();
   }
   
   //--------------------------- Mouse Events ---------------------------------------
   
   int saveState;
   
   
   public void mousePressed(MouseEvent me) {
   
      int st = getState();
      int x=me.getX();
      int y=me.getY();
      
      if (st == MESSAGE) {
         clearMessage();
        return ;// return true;
      }
      
      if (st == LOADING)
        return ;// return true;
   
      if (st == DRAGGING || st == DRAWING) {  // can't happen, except with Netscspe's bug!
         setState(saveState);
         undoer.cancel();
         forceCircuitRedraw();
      }
   
      saveState = st;
      
      if (selectedItem != null && !resizer.hidden) {
         if (selectionInScroller)
            resizing = resizer.beginSymmetricDrag(x,y,LEFT-8,LEFT-8);
         else
            resizing = resizer.beginDrag(x,y,new Rectangle(LEFT+5,5,circuitWidth-10,circuitHeight-10));
         if (resizing) {
            beginResize(x,y);
          return ;//  return true;
         }
      }
      
      resizing = false;
   
      LogixCktIT hitItem = null;
      
      if (x < LEFT) {
         hitItem = scroller.checkMouse(x,y);
         if (hitItem != null)
            selectItem(hitItem,true);
         else
            selectItem(null,false);
      }
      else {
         for (int i = currentCircuit.items.size() - 1; i >= 0; i--) {
            LogixCktIT it = (LogixCktIT)currentCircuit.items.elementAt(i);
            if (it.hit(x-LEFT,y)) {
               hitItem = it;
               break;
            }
         }
         if (hitItem == null) for (int i = currentCircuit.inputs.size() - 1; i >= 0; i--) {
            LogixCktIT it = (LogixCktIT)currentCircuit.inputs.elementAt(i);
            if (it.hit(x-LEFT,y)) {
               hitItem = it;
               break;
            }
         }
         if (hitItem == null) for (int i = currentCircuit.outputs.size() - 1; i >= 0; i--) {
            LogixCktIT it = (LogixCktIT)currentCircuit.outputs.elementAt(i);
            if (it.hit(x-LEFT,y)) {
               hitItem = it;
               break;
            }
         }
         if (hitItem == null) for (int i = currentCircuit.lines.size() - 1; i >= 0; i--) {
            LogixCktIT it = (LogixCktIT)currentCircuit.lines.elementAt(i);
            if (it.hit(x-LEFT,y)) {
               hitItem = it;
               break;
            }
         }
         selectItem(hitItem,false);
      }

      if (selectedItem == null)
        return ;// return true;
         
      if (me.isMetaDown() || me.isControlDown()) {
         beginDrag(x,y);
        return ;// return true;
      }
      
      if (me.getClickCount() == 2 && selectedItem != null && hitItem == selectedItem) {
        if (selectedItem instanceof LogixCkt) {
           doDeIconify();
          return ;// return true;
        }
        else if (selectedItem instanceof Logixline) {
           insertTack((Logixline)selectedItem,x-LEFT,y);
           beginDrag(x,y);
          return ;// return true;
        }
      }
      
      if (selectionInScroller) {
         beginDrag(x,y);
        return ;// return true;
      }
      
      if (selectedItem instanceof LogixCktNub)
        doIOClick((LogixCktNub)selectedItem);

      beginDraw(x,y);
      
     return ;// return true;
   }
   public void mouseClicked(MouseEvent me){}
   public void mouseExited(MouseEvent me){}
   public void mouseMoved(MouseEvent me){}
   public void mouseEntered(MouseEvent me){}
      

   public void mouseDragged(MouseEvent me) {
      int state = getState();
      int x=me.getX();
      int y=me.getY();
      if (state == DRAGGING) {
         if (resizing)
            continueResize(x,y);
         else
            continueDrag(x,y);
      }
      else if (state == DRAWING)
         continueDraw(x,y);
     return ;// return true;
   }

   public void mouseReleased(MouseEvent me) {
      int state = getState();
      int x=me.getX();
      int y=me.getY();
      if (state == DRAGGING) {
         if (resizing)
            endResize(x,y);
         else
            endDrag(x,y);
         setState(saveState);
      }
      else if (state == DRAWING) {
         endDraw(x,y);
         setState(saveState);
      }
     return ;// return true;
   }
   
   synchronized void insertTack(Logixline line, int x, int y) {
      Rectangle r = line.getCopyOfBoundingBox(false);
      Tack tack = new Tack();
      tack.reshape(x-2,y-2,5,5);
      LogixNub source = line.source;
      LogixNub dest = line.destination;
      line.delete(currentCircuit);
      Logixline line1 = new Logixline(source,tack);
      Logixline line2 = new Logixline(tack,dest);
      currentCircuit.lines.addElement(line1);
      currentCircuit.lines.addElement(line2);
      currentCircuit.items.addElement(tack);
      line1.on = line2.on = tack.on = line.on;
      selectItem(tack,false);  // forces draw to OSC
      r.add(tack.getCopyOfBoundingBox(true));
      repaint(r.x,r.y,r.width,r.height);
      undoer.setAddTackData(tack,line);
   }
   
   synchronized void doIOClick(LogixCktNub io) {
      if (io.kind != LogixNub.INPUT)
         return;
      if (getState() == IDLE)
         return;
      io.on = !io.on;
      circuitHasChanged();
      repaint(Math.round(io.boundingBox.x)+LEFT,Math.round(io.boundingBox.y),
                                  Math.round(io.boundingBox.width),Math.round(io.boundingBox.height));
      notify();
   }
   
   //-------------------------- Manage drag and draw ------------------------------
   
   boolean resizing;
   int last_x, last_y, start_x, start_y;
   int offset_x, offset_y;
   Rectangle changedRect;
   boolean draggingFromScroller;
   LogixCktIT saveScrollSelection;
   FloatRect saveOriginalBox;
   
   synchronized void beginDrag(int x, int y) {
      if (selectedItem == null || selectedItem instanceof Logixline)
         return;
      if (selectionInScroller)
         offset_x = Math.round(selectedItem.boundingBox.x) - x;
      else
         offset_x = Math.round(selectedItem.boundingBox.x) + LEFT - x;
      offset_y = Math.round(selectedItem.boundingBox.y) - y;
      draggingFromScroller = selectionInScroller;
      if (selectionInScroller) {
         LogixCktIT it = selectedItem.copy();
         selectedItem.selected = false;
         saveScrollSelection = selectedItem;
         selectedItem = it;
      }
      else {
         selectedItem.selectConnectedLines(true);
         saveOriginalBox = new FloatRect(selectedItem.boundingBox.x, selectedItem.boundingBox.y,
                                    selectedItem.boundingBox.width, selectedItem.boundingBox.height); 
      }
      changedRect = selectedItem.getCopyOfBoundingBox(true);
      setState(DRAGGING);
      resizer.hide();
      start_x = x;
      start_y = y;
   }
   
   synchronized void continueDrag(int x, int y) {
      int new_x = x + offset_x;
      int new_y = y + offset_y;
      if (selectionInScroller) {
         if (new_x < LEFT - 3)
            selectedItem.reshape(new_x,new_y,selectedItem.boundingBox.width,selectedItem.boundingBox.height);
         else {
            currentCircuit.addItem(selectedItem);
            selectionInScroller = false;
            changedRect.x -= LEFT;
            selectedItem.dragTo(new_x-LEFT,new_y,currentCircuit.boundingBox);
            owner.deleteButton.setEnabled(true);
         }
      }
      else
         selectedItem.dragTo(new_x-LEFT,new_y,currentCircuit.boundingBox);
      Rectangle r = selectedItem.getCopyOfBoundingBox(true);
      changedRect.add(r);
      circuitHasChanged();
      if (selectionInScroller)
          repaint(changedRect.x,changedRect.y,changedRect.width,changedRect.height);
      else
          repaint(changedRect.x+LEFT,changedRect.y,changedRect.width,changedRect.height);
      changedRect = r;
   }
   
   synchronized void endDrag(int x, int y) {
      continueDrag(x,y);  // doess circuitHasChanged();
      selectedItem.selectConnectedLines(false);
      if (selectionInScroller) {
         selectedItem = saveScrollSelection;
         saveScrollSelection.selected = true;
         if (x != start_x || y != start_y)
            repaint();
      }
      if (selectedItem instanceof LogixCkt || (!selectionInScroller && selectedItem instanceof LogixGates)) {
         if (selectionInScroller)
            resizer.show(selectedItem.boundingBox.x, selectedItem.boundingBox.y,
                         selectedItem.boundingBox.width, selectedItem.boundingBox.height);
         else
            resizer.show(selectedItem.boundingBox.x+LEFT, selectedItem.boundingBox.y,
                         selectedItem.boundingBox.width, selectedItem.boundingBox.height);
         repaint(resizer.x,resizer.y,resizer.width+1,resizer.height+1);
      }
      if (selectionInScroller || (x == start_x && y == start_y))
         return;
      else if (draggingFromScroller)
         undoer.setAddItemData(selectedItem);
      else
         undoer.setDragData(selectedItem,saveOriginalBox);
   }
   
   synchronized void beginResize(int x, int y) {
      setState(DRAGGING);
      selectedItem.selectConnectedLines(true);
      saveOriginalBox = new FloatRect(selectedItem.boundingBox.x, selectedItem.boundingBox.y,
                                    selectedItem.boundingBox.width, selectedItem.boundingBox.height); 
      changedRect = selectedItem.getCopyOfBoundingBox(true);
      circuitHasChanged();
      start_x = x;
      start_y = y;
   }
   
   synchronized void continueResize(int x, int y) {
      resizer.continueDrag(x,y);
      if (selectionInScroller)
         selectedItem.reshape(resizer.x,resizer.y,resizer.width,resizer.height);
      else
         selectedItem.reshape(resizer.x-LEFT,resizer.y,resizer.width,resizer.height);
      Rectangle r = selectedItem.getCopyOfBoundingBox(true);
      changedRect.add(r);
      if (!selectionInScroller)
         changedRect.x += LEFT;
      circuitHasChanged();
      repaint(changedRect.x,changedRect.y,changedRect.width,changedRect.height);
      changedRect = r;
   }
   
   synchronized void endResize(int x, int y) {
      continueResize(x,y);  // doesCircuitHasChanged
      resizer.endDrag(x,y);
      selectedItem.selectConnectedLines(false);
      if (x != start_x || y != start_y)
         if (selectionInScroller)
            undoer.setResizeInScrollData(selectedItem,saveOriginalBox);         
         else
            undoer.setResizeData(selectedItem,saveOriginalBox);         
   }
   
   LogixNub lineSourceNub, lineDestinationNub;
   LogixCktIT lineSourceItem, lineDestinationItem;
   int newline_x, newline_y;
   
   synchronized void beginDraw(int x, int y) {
      x = x - LEFT;
      lineSourceItem = currentCircuit.itemHitForLineSource(x,y);
      if (lineSourceItem == null)
         return;
      lineSourceNub = lineSourceItem.getLineSource(x,y);
      if (lineSourceNub == null)
         return;  // shouldn't be possible
      setState(DRAWING);
      last_x = newline_x = x;
      last_y = newline_y = y;
      changedRect = new Rectangle(x,y,1,1);
      changedRect.add(lineSourceNub.connect_x,lineSourceNub.connect_y);
      changedRect.grow(1,1);
      circuitHasChanged();
      repaint(changedRect.x,changedRect.y,changedRect.width,changedRect.height);
   }
   
   synchronized void continueDraw(int x, int y) {
      x = x - LEFT;
      last_x = x;
      last_y = y;
      x = Math.min(Math.max(5,x),circuitWidth - 6);
      y = Math.min(Math.max(5,y),circuitHeight - 6);
      LogixNub checkForNewSource = lineSourceItem.getLineSource(x,y);
      if (selectedItem == lineSourceItem && !lineSourceItem.boundingBox.inside(x,y))
         selectItem(null,false);
      boolean needsRefresh = false;
      if (checkForNewSource != null && checkForNewSource != lineSourceNub) {
         needsRefresh = true;
         lineSourceNub = checkForNewSource;
         changedRect.add(lineSourceNub.connect_x,lineSourceNub.connect_y);
      }
      lineDestinationItem = currentCircuit.itemHitForLineDestination(x,y);
      if (selectedItem != null && lineDestinationItem != selectedItem)
         selectItem(null,false);
      int x0,y0;
      if (lineDestinationItem == null || lineDestinationItem == lineSourceItem) {
         lineDestinationNub = null;
         x0 = x;
         y0 = y;
      }
      else {
         lineDestinationNub = lineDestinationItem.getLineDestination(x,y);
         x0 = lineDestinationNub.connect_x;
         y0 = lineDestinationNub.connect_y;
         selectItem(lineDestinationItem,false);
         resizer.hide();
      }
      if (newline_x != x0 || newline_y != y0) {
         needsRefresh = true;
         changedRect.add(x0,y0);
         newline_x = x0;
         newline_y = y0;
      }
      if (needsRefresh) {
         circuitHasChanged();
         changedRect.grow(1,1);
         repaint(changedRect.x+LEFT,changedRect.y,changedRect.width,changedRect.height);
      }
      changedRect.reshape(newline_x,newline_y,1,1);
      changedRect.add(lineSourceNub.connect_x,lineSourceNub.connect_y);
   }
   
   synchronized void endDraw(int x, int y) {
      x = x - LEFT;
      if (last_x != x || last_y != y)
         continueDraw(x,y);
      setState(saveState);
      if (lineDestinationItem != null && lineDestinationNub != null) {
         Logixline line = new Logixline(lineSourceNub,lineDestinationNub);
         currentCircuit.addItem(line);
         undoer.setDrawData(line);
         circuitHasChanged();
      }
      if (selectedItem != lineSourceItem)
         selectItem(null,false);
      changedRect.grow(1,1);
      circuitHasChanged();
      repaint(changedRect.x+LEFT,changedRect.y,changedRect.width,changedRect.height);
      lineSourceNub = null;
      lineDestinationNub = null;
      lineSourceItem = null;
      lineDestinationItem = null;
   }
   
   public void run() {
      while (true) {
         synchronized(this) {
            while (state != RUNNING) {
               try { wait(); }
               catch (InterruptedException e) { }
            }
         }
         boolean needsRepaint = false;
         while (getState() == RUNNING) {
            synchronized(this) {
               if (currentCircuit.computeTopLevel()) {
                  circuitHasChanged();
                  if (state != RUNNING)
                     continue;
                  Graphics g = getGraphics();
                  g.translate(LEFT,0);
                  for (int i = 0; i < currentCircuit.outputs.size(); i++) {
                     LogixCktNub out = (LogixCktNub)currentCircuit.outputs.elementAt(i);
                     out.changed = false;
                  }
                  for (int i = 0; i < currentCircuit.lines.size(); i++) {
                     Logixline line = (Logixline)currentCircuit.lines.elementAt(i);
                     line.draw(g);
                     line.destination.changed = true;
                  }
                  for (int i = 0; i < currentCircuit.outputs.size(); i++) {
                     LogixCktNub out = (LogixCktNub)currentCircuit.outputs.elementAt(i);
                      if (out.changed)
                         out.drawCenter(g);
                  }
                  g.dispose();
                  needsRepaint = true;
               }
               else if (needsRepaint) {
                  repaint(LEFT,0,circuitWidth,circuitHeight);
                  needsRepaint = false;
               }
               if (needsRepaint && computeDelay > 200) {
                  repaint(LEFT,0,circuitWidth,circuitHeight);
                  needsRepaint = false;
               }
            }
            if (state == RUNNING) {
               try { Thread.sleep(getComputeDelay()); }
               catch (InterruptedException e) { }
            }
         }
         repaint(LEFT,0,circuitWidth,circuitHeight);
      }
   }

}
