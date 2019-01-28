//package Logix_ckt;
import java.awt.*;
import java.util.Vector;
import javax.swing.*;

public class Logixundo {

   JButton undoButton;
   
   final static int NONE = 0,   // values for status
                    UNDO = 1,
                    REDO = 2;

   int status = NONE;
   
   final static int                       // types of actions that can be undone
                    DELETE = 0,     // deleting an item from circuit
                    DELSCROLL = 1,  // deleting an item from 
                    CLEAR = 2,      // clear out current circuit only, when it's a top-level circuit
                    CLEARINC = 3,   // clear when it's part of a bigger circuit
                    ADDITEM = 4,    // for dragging item onto circuit
                    ADDTACK = 5,    // cut line in two to add tack
                    DRAW = 6,       // adding a line
                    RESIZE = 7,     // change size of circuit element (not window)
                    DRAG = 8,       // moved an item
                    NEWCIRCUIT = 9, // clear everything
                    RESIZEINSCROLL = 10,
                    LOADFILE = 11;
                    
    int actionType;
    
    Vector data = new Vector();
    int topItemForDELSCROLL;
    
    LogixCktCanvas owner;
    
    Logixundo(LogixCktCanvas owner, JButton undoButton) {
       this.owner = owner;
       this.undoButton = undoButton;
    }
    
    public void click() {
       synchronized(owner) {
          if (status == UNDO) {
             performUndo();
             status = REDO;
             undoButton.setText("Redo");
          }
          else if (status == REDO) {
             performRedo();
             status = UNDO;
             undoButton.setText("Undo");
          }
       }
    }
    
    void cancel() {
       if (status != NONE) {
          data.setSize(0);
          undoButton.setEnabled(false);
          if (status == REDO)
             undoButton.setText("Undo");
          status = NONE;
       }
    }
    
    private void newUndoData() {
       data.setSize(0);
       if (status == REDO)
          undoButton.setText("Undo");
       else if (status == NONE)
          undoButton.setEnabled(true);
       status = UNDO;
    }
    
    void setDeleteData(LogixCktIT deletedItem) {
       actionType = DELETE;
       newUndoData();
       data.addElement(deletedItem);
    }
    
    void setDeleteFromScrollData(LogixCktIT deletedItem, int topItem) {
       actionType = DELSCROLL;
       newUndoData();
       topItemForDELSCROLL = topItem;
       data.addElement(deletedItem);
    }
    
    void setClearMainCircuitData(Vector items, Vector lines, Vector inputs, Vector outputs) {
       actionType = CLEAR;
       newUndoData();
       data.addElement(items);
       data.addElement(lines);
       data.addElement(inputs);
       data.addElement(outputs);
    }
    
    void setClearSubCircuitData(Vector items, Vector lines) {
       actionType = CLEARINC;
       newUndoData();
       data.addElement(items);
       data.addElement(lines);
    }
    
    void setAddItemData(LogixCktIT itemAdded) {
       actionType = ADDITEM;
       newUndoData();
       data.addElement(itemAdded);
    }
    
    void setDrawData(Logixline lineDrawn) {
       actionType = DRAW;
       newUndoData();
       data.addElement(lineDrawn);
    }
    
    
    void setAddTackData(Tack tackAdded, Logixline lineRemoved) {
       actionType = ADDTACK;
       newUndoData();
       data.addElement(tackAdded);
       data.addElement(lineRemoved);
    }
    
    
    void setResizeData(LogixCktIT itemSized, FloatRect oldBoundingBox) {
       actionType = RESIZE;
       newUndoData();
       data.addElement(itemSized);
       data.addElement(oldBoundingBox);
    }
    
    void setResizeInScrollData(LogixCktIT itemSized, FloatRect oldBoundingBox) {
       actionType = RESIZEINSCROLL;
       newUndoData();
       data.addElement(itemSized);
       data.addElement(oldBoundingBox);
    }
    
    void setDragData(LogixCktIT draggedItem, FloatRect oldBoundingBox) {
       actionType = DRAG;
       newUndoData();
       data.addElement(draggedItem);
       data.addElement(oldBoundingBox);
    }

    void setNewCircuitData(Vector circuitStack, Vector scrollItems, LogixCkt currentCircuit) {
       actionType = NEWCIRCUIT;
       newUndoData();
       data.addElement(circuitStack);
       data.addElement(scrollItems);
       data.addElement(currentCircuit);
    }
    
    void setLoadData(Vector circuitStack, Vector scrollItems, LogixCkt currentCircuit) {
       actionType = LOADFILE;
       newUndoData();
       data.addElement(circuitStack);
       data.addElement(scrollItems);
       data.addElement(currentCircuit);
    }
    
    
    
    private void performUndo() {
      switch (actionType) {
         case DELETE:
            LogixCktIT del = (LogixCktIT)data.elementAt(0);
            del.unDelete(owner.currentCircuit);
            owner.selectItem(del,false);
            break;
         case DELSCROLL:
            LogixCkt scir = (LogixCkt)data.elementAt(0);
            owner.selectItem(null,false);
            owner.scroller.unDeleteItem(scir,topItemForDELSCROLL);
            owner.selectItem(scir,true);
            break;
         case ADDITEM:
         case DRAW:
            LogixCktIT add = (LogixCktIT)data.elementAt(0);
            owner.selectItem(null,false);
            add.delete(owner.currentCircuit);
            break;
         case ADDTACK:
            Tack tack = (Tack)data.elementAt(0);
            Logixline line = (Logixline)data.elementAt(1);
            if (owner.selectedItem == tack)
               owner.selectItem(null,false);
            tack.delete(owner.currentCircuit);
            line.unDelete(owner.currentCircuit);
            owner.selectItem(line,false);
            break;
         case RESIZE:
         case RESIZEINSCROLL:
         case DRAG:
            LogixCktIT sized = (LogixCktIT)data.elementAt(0);
            FloatRect oldBox = (FloatRect)data.elementAt(1);
            FloatRect newBox = new FloatRect(sized.boundingBox.x,sized.boundingBox.y,
                                              sized.boundingBox.width,sized.boundingBox.height);
            if (actionType == RESIZEINSCROLL)  // resize box in case item has been moved up or down by scrolling
               oldBox.y = (owner.scroller.items.indexOf(sized) - owner.scroller.topItem)*owner.scroller.width
                              + owner.scroller.width/2 - oldBox.height/2;
            if (owner.selectedItem == sized)
               owner.selectItem(null,false);
            else if (actionType == RESIZEINSCROLL)
               owner.repaint(Math.round(newBox.x)-1,Math.round(newBox.y)-1,Math.round(newBox.width)+2,Math.round(newBox.height)+2);  // make sure enough of scroll area will be redrawn
            sized.reshape(oldBox.x,oldBox.y,oldBox.width,oldBox.height);
            owner.selectItem(sized,actionType==RESIZEINSCROLL);
            data.setElementAt(newBox,1);
            break;
         case CLEAR:
            owner.currentCircuit.items = (Vector)data.elementAt(0);
            owner.currentCircuit.lines = (Vector)data.elementAt(1);
            owner.currentCircuit.inputs = (Vector)data.elementAt(2);
            owner.currentCircuit.outputs = (Vector)data.elementAt(3);
            break;
         case CLEARINC:
            owner.currentCircuit.items = (Vector)data.elementAt(0);
            owner.currentCircuit.lines = (Vector)data.elementAt(1);
            for (int i = 0; i < owner.currentCircuit.lines.size(); i++) {
               Logixline it = (Logixline)owner.currentCircuit.lines.elementAt(i);
               it.destination.source = it;
               if (it.source.destination.indexOf(it) < 0)
                  it.source.destination.addElement(it);
            }
            break;
         case NEWCIRCUIT:
            owner.circuitStack = (Vector)data.elementAt(0);
            owner.scroller.addItems((Vector)data.elementAt(1));
            owner.currentCircuit = (LogixCkt)data.elementAt(2);
            owner.currentCircuit.reshape(5,5,owner.circuitWidth-10,owner.circuitHeight-10);
            if (owner.currentCircuit.saveContainerWhileEnlarged != null)
               owner.owner.iconifyButton.setText("Shrink");
            else
               owner.owner.iconifyButton.setText("Iconify");
            owner.owner.nameInput.setText(owner.currentCircuit.name);
            break;
         case LOADFILE:
            owner.selectItem(null,false);
            Vector stack = owner.circuitStack;
            LogixCkt cc = owner.currentCircuit;
            Vector si = owner.scroller.deleteAllCircuits();
            owner.circuitStack = (Vector)data.elementAt(0);
            owner.scroller.addItems((Vector)data.elementAt(1));
            owner.currentCircuit = (LogixCkt)data.elementAt(2);
            owner.currentCircuit.reshape(5,5,owner.circuitWidth-10,owner.circuitHeight-10);
            data.setElementAt(stack,0);
            data.setElementAt(si,1);
            data.setElementAt(cc,2);
            if (owner.currentCircuit.saveContainerWhileEnlarged != null)
               owner.owner.iconifyButton.setText("Shrink");
            else
               owner.owner.iconifyButton.setText("Iconify");
            owner.owner.nameInput.setText(owner.currentCircuit.name);
            break;
      }
      if (actionType != DELSCROLL && actionType != RESIZEINSCROLL)
         owner.forceCircuitRedraw();
    }


    
    private void performRedo() {
      switch (actionType) {
         case DELETE:
            LogixCktIT del = (LogixCktIT)data.elementAt(0);
            owner.selectItem(null,false);
            del.delete(owner.currentCircuit);
            break;
         case DELSCROLL:
            LogixCkt scir = (LogixCkt)data.elementAt(0);
            owner.selectItem(null,false);
            owner.scroller.deleteItem(scir);
            break;
         case ADDITEM:
         case DRAW:
            LogixCktIT add = (LogixCktIT)data.elementAt(0);
            add.unDelete(owner.currentCircuit);
            owner.selectItem(add,false);
            break;
         case ADDTACK:
            Tack tack = (Tack)data.elementAt(0);
            Logixline line = (Logixline)data.elementAt(1);
            if (owner.selectedItem == line)
               owner.selectItem(null,false);
            line.delete(owner.currentCircuit);
            tack.unDelete(owner.currentCircuit);
            owner.selectItem(tack,false);
            break;
         case RESIZE:
         case RESIZEINSCROLL:
         case DRAG:
            performUndo();
            return;
         case CLEAR:
            owner.currentCircuit.items = new Vector();
            owner.currentCircuit.lines = new Vector();
            owner.currentCircuit.inputs = new Vector();
            owner.currentCircuit.outputs = new Vector();
            break;
         case CLEARINC:
            owner.currentCircuit.items = new Vector();
            owner.currentCircuit.lines = new Vector();
            for (int i = 0; i < owner.currentCircuit.inputs.size(); i++) {
               LogixCktNub in = (LogixCktNub)owner.currentCircuit.inputs.elementAt(i);
               in.destination.setSize(0);
            }
            for (int i = 0; i < owner.currentCircuit.outputs.size(); i++) {
               LogixCktNub out = (LogixCktNub)owner.currentCircuit.outputs.elementAt(i);
               out.source = null;
            }
            break;
         case NEWCIRCUIT:
            owner.selectItem(null,false);
            owner.scroller.deleteAllCircuits();
            owner.circuitStack = new Vector();
            owner.currentCircuit = new LogixCkt();
            owner.currentCircuit.reshape(5,5,owner.circuitWidth-10,owner.circuitHeight-10);
            owner.owner.iconifyButton.setText("Iconify");
            owner.owner.nameInput.setText("Untitled");
            break;
         case LOADFILE:
            performUndo();
            return;
      }
      if (actionType != DELSCROLL && actionType != RESIZEINSCROLL)
         owner.forceCircuitRedraw();
    }


}