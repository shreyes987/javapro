
//package Logix_ckt;

import java.awt.*;
import java.util.Vector;


class LogixCkt extends LogixCktIT {

   static Font nameFont, containerNameFont;
   static int nameCharWidth, nameCharHeight, nameCharLeading;
   static Font nameFontBig;
   static int nameCharWidthBig, nameCharHeightBig, nameCharLeadingBig;

   Vector inputs = new Vector();
   Vector outputs = new Vector();
   Vector lines = new Vector();
   Vector items = new Vector();
   
   boolean iconified = false;
   LogixCkt saveContainerWhileEnlarged;  // this is non-null ONLY when this is a contained circuit that has been enlarged
   FloatRect boundingBoxInContainer;

   FloatRect savedBoundingBox;

   String name = "Untitled";
   
   void iconify(int x, int y, int width, int height) {  // value of boundingBoxInContainer is used in CircuitCanvas
      iconified = true;
      saveContainerWhileEnlarged = null;
      boundingBoxInContainer = null;
      reshape(x,y,width,height);
      powerOff();
   }
   
   void deiconify(int x, int y, int width, int height, LogixCkt source) {
      iconified = false;
      saveContainerWhileEnlarged = source;
      if (source != null)
         boundingBoxInContainer = new FloatRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
      reshape(x,y,width,height);
      powerOff();
   }
      
   void reshape(float x, float y, float width, float height) {
      if (!iconified && width < 10 || height < 10)
         return;
      boundingBox.reshape(x,y,width,height);
      if (!iconified) {  // adjust sizes/positions of contained items
         if (savedBoundingBox != null) {
            float xFactor = boundingBox.width / savedBoundingBox.width;
            float yFactor = boundingBox.height / savedBoundingBox.height;
            for (int i = 0; i < items.size(); i++) {
               LogixCktIT it = (LogixCktIT)items.elementAt(i);
               if (it instanceof Tack)
                  it.reshape(5 + ((it.boundingBox.x - 5)*xFactor), 5 + ((it.boundingBox.y - 5)*yFactor), 
                              it.boundingBox.width, it.boundingBox.height);
               else
                 it.reshape( 5 + ((it.boundingBox.x - 5)*xFactor), 5 + ((it.boundingBox.y - 5)*yFactor),
                              Math.max(10,(it.boundingBox.width)*xFactor), Math.max(10,(it.boundingBox.height)*yFactor) );
            }
         }
         savedBoundingBox = new FloatRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
      }
      for (int i = 0; i < inputs.size(); i++) {
         if (iconified)
            ((LogixCktNub)inputs.elementAt(i)).getCoordsIconified(x,y,width,height);
         else 
            ((LogixCktNub)inputs.elementAt(i)).getCoords(x,y,width,height);
      }
      for (int i = 0; i < outputs.size(); i++) {
         if (iconified)
            ((LogixCktNub)outputs.elementAt(i)).getCoordsIconified(x,y,width,height);
         else
            ((LogixCktNub)outputs.elementAt(i)).getCoords(x,y,width,height);
      }
   }
   
   void addItem(LogixCktIT item) {
      if (item instanceof LogixCktNub) {
         if (((LogixCktNub)item).kind == LogixNub.INPUT)
            inputs.addElement(item);
         else
            outputs.addElement(item);
      }
      else if (item instanceof Logixline)
         lines.addElement(item);
      else
         items.addElement(item);
   }
   
   boolean computeTopLevel() { // returns true if some visible element in the circuit changes
      boolean changed = false;
      for (int i = 0; i < items.size(); i++) {
          LogixCktIT item = (LogixCktIT)items.elementAt(i);
          changed |= item.compute();
      }
      for (int i = 0; i < lines.size(); i++) {
          changed |= ((Logixline)lines.elementAt(i)).compute();
      }
      for (int i = 0; i < lines.size(); i++) {
          Logixline line = (Logixline)lines.elementAt(i);
          line.destination.on = line.on;
      }
      return changed;
   }
   
   boolean compute() {  // called for nested circuits, which don't change visible appearance
      for (int i = 0; i < items.size(); i++) {
          LogixCktIT item = (LogixCktIT)items.elementAt(i);
          item.compute();
      }
      for (int i = 0; i < lines.size(); i++) {
          ((Logixline)lines.elementAt(i)).compute();
      }
      for (int i = 0; i < lines.size(); i++) {
          Logixline line = (Logixline)lines.elementAt(i);
          line.destination.on = line.on;
      }
      return false;
   }
   
   void powerOff() {
      on = false;
      for (int i = 0; i < lines.size(); i++) {
          Logixline it = (Logixline)lines.elementAt(i);
          it.on = false;
      }
      for (int i = 0; i < items.size(); i++) {
          LogixCktIT it = (LogixCktIT)items.elementAt(i);
          it.powerOff();
      }
      for (int i = 0; i < inputs.size(); i++) {
          LogixCktNub it = (LogixCktNub)inputs.elementAt(i);
          it.on = false;
      }
      for (int i = 0; i < outputs.size(); i++) {
          LogixCktNub it = (LogixCktNub)outputs.elementAt(i);
          it.on = false;
      }
   }

   void draw(Graphics g) {
    Font saveFont = g.getFont();
    if (nameFont == null) {
       containerNameFont = new Font(saveFont.getName(),Font.BOLD,saveFont.getSize());
       int pts = saveFont.getSize();
       nameFontBig = new Font("Courier",Font.PLAIN,pts);
       FontMetrics fm = g.getFontMetrics(nameFontBig);
       nameCharWidthBig = fm.charWidth('W');
       nameCharHeightBig = fm.getHeight() - 1;
       nameCharLeadingBig = fm.getLeading();
       if (pts == 10)
          pts = 9;
       else if (pts > 10 && pts <= 12)
          pts = 10;
       else if (pts > 12 && pts < 15)
          pts = 12;
       else if (pts >= 15)
          pts = (int)((pts*5.0)/6.0);
       nameFont = new Font("Courier",Font.PLAIN,pts);
       fm = g.getFontMetrics(nameFont);
       nameCharWidth = fm.charWidth('W');
       nameCharHeight = fm.getHeight() - 1;
       nameCharLeading = fm.getLeading();
    }
    if (iconified) {
      if (selected)
         g.setColor(Color.blue);
      else
         g.setColor(Color.black);
      int x1 = Math.round(boundingBox.x+5);
      int y1 = Math.round(boundingBox.y+5);
      int x2 = Math.round(boundingBox.x+boundingBox.width-5);
      int y2 = Math.round(boundingBox.y+boundingBox.height-5);
      g.drawLine(x1,y1,x1,y2);
      g.drawLine(x1,y2,x2,y2);
      g.drawLine(x2,y2,x2,y1);
      g.drawLine(x2,y1,x1,y1);
      for (int i = 0; i < inputs.size(); i++)
         ((LogixCktNub)inputs.elementAt(i)).drawIconified(g);
      for (int i = 0; i < outputs.size(); i++)
         ((LogixCktNub)outputs.elementAt(i)).drawIconified(g);
       putName(g);
       g.setFont(saveFont);
     }
    else {
      for (int i = 0; i < lines.size(); i++) {
          Logixline it = (Logixline)lines.elementAt(i);
             it.draw(g);
      }
      for (int i = 0; i < items.size(); i++) {
          LogixCktIT it = (LogixCktIT)items.elementAt(i);
             it.draw(g);
      }
      for (int i = 0; i < inputs.size(); i++) {
          LogixCktNub it = (LogixCktNub)inputs.elementAt(i);
             it.draw(g);
      }
      for (int i = 0; i < outputs.size(); i++) {
          LogixCktNub it = (LogixCktNub)outputs.elementAt(i);
             it.draw(g);
      }
      if (saveContainerWhileEnlarged != null) {
         g.setFont(containerNameFont);
         g.setColor(Color.red);
         g.drawString("Enlarged from \"" + saveContainerWhileEnlarged.name + '\"',
                                (int)boundingBox.x+10, (int)(boundingBox.y+boundingBox.height)-12);
         g.setFont(saveFont);
      }
    }
   }
   
   private void putName(Graphics g) {
   
      int charWidth,charHeight,charLeading;
      int maxChars = ((int)boundingBox.width - 16) / nameCharWidthBig;
      if (maxChars < 6) {
         g.setFont(nameFont);
         charWidth = nameCharWidth;
         charHeight = nameCharHeight;
         charLeading = nameCharLeading;
         maxChars = ((int)boundingBox.width - 16) / nameCharWidth;
      }
      else {
         g.setFont(nameFontBig);
         charWidth = nameCharWidthBig;
         charHeight = nameCharHeightBig;
         charLeading = nameCharLeadingBig;
      }   
      if (selected)
         g.setColor(Color.blue);
      else
         g.setColor(Color.black);
      if (maxChars == 0)
         maxChars = 1;
      int maxLines = ((int)boundingBox.height - 14 + charLeading) / charHeight;
      if (maxLines <= 0)
         maxLines = 1;
      name = name.trim();
      int[] lineBreak = new int[maxLines+1];
      lineBreak[0] = -1;
      int line = 0;
      int pos = 0;
      while (true) {
         if (pos >= name.length()) {
            lineBreak[line+1] = pos;
            line++;
            break;
         }
         int charCt = 0;
         int lastSpace = -1;
         while (pos < name.length() && charCt <= maxChars) {
            if (name.charAt(pos) == ' ')
               lastSpace = pos;
            pos++;
            charCt++;
         }
         if (charCt > maxChars) {
            if (lastSpace >= 0) {
               lineBreak[line+1] = lastSpace;
               pos = lastSpace + 1;
            }
            else {
               while (pos < name.length() && name.charAt(pos) != ' ')
                  pos++;
               lineBreak[line+1] = pos;
               pos++;
            }
            line++;
            if (line >= maxLines || pos >= name.length())
               break;
         }
      }
      int center_x = (int)(boundingBox.x + (boundingBox.width+1)/2);
      int top_y =(int)(boundingBox.y + boundingBox.height/2) - (line*charHeight - charLeading)/2 + charHeight - charLeading;
      for (int i = 0; i < line; i++) {
         int ct = lineBreak[i+1] - lineBreak[i];
         if (ct <= maxChars) {
            int w = (lineBreak[i+1] - lineBreak[i] - 1)*charWidth;
            g.drawString(name.substring(lineBreak[i]+1,lineBreak[i+1]),
                            center_x - w/2, top_y + i*charHeight);
         }
         else {
            int w = maxChars * charWidth;
            g.drawString(name.substring(lineBreak[i]+1,lineBreak[i]+maxChars+1),
                            center_x - w/2, top_y + i*charHeight);
         }
      }
   }
   
   void drawWithLines(Graphics g) { 
      draw(g);
      if (iconified) {
         for (int i = 0; i < inputs.size(); i++) {
             LogixCktNub in = (LogixCktNub)inputs.elementAt(i);
             if (in.source != null)
                in.source.draw(g);
         }
         for (int i = 0; i < outputs.size(); i++) {
             LogixCktNub out = (LogixCktNub)outputs.elementAt(i);
             for (int j = 0; j < out.destination.size(); j++)
                ((Logixline)out.destination.elementAt(j)).draw(g);
         }
      }
   }

   Rectangle getCopyOfBoundingBox(boolean addInLines) { 
      FloatRect r = new FloatRect(boundingBox.x,boundingBox.y,boundingBox.width,boundingBox.height);
      if (addInLines) {
         for (int i = 0; i < inputs.size(); i++) {
            LogixCktNub in = (LogixCktNub)inputs.elementAt(i);
            if (in.source != null)
               r.add(in.source.boundingBox);
         }
         for (int i = 0; i < outputs.size(); i++) {
            LogixCktNub out = (LogixCktNub)outputs.elementAt(i);
            for (int j = 0; j < out.destination.size(); j++)
               r.add(((Logixline)out.destination.elementAt(j)).boundingBox);
         }
      }
      r.grow(1,1);
      return r.getIntRect();
   }
   
   void selectConnectedLines(boolean select) { 
      if (iconified) {
         for (int i = 0; i < inputs.size(); i++) {
             LogixCktNub in = (LogixCktNub)inputs.elementAt(i);
             if (in.source != null)
                in.source.selected = select;
         }
         for (int i = 0; i < outputs.size(); i++) {
             LogixCktNub out = (LogixCktNub)outputs.elementAt(i);
             for (int j = 0; j < out.destination.size(); j++)
                ((Logixline)out.destination.elementAt(j)).selected = select;
         }
      }
   }
   
   LogixCktIT itemHitForLineSource(int x, int y) {
      for (int i = inputs.size()-1; i >= 0; i--) {
          LogixCktNub it = (LogixCktNub)inputs.elementAt(i);  
          if (it.getLineSource(x,y) != null)
             return it;
      }
      for (int i = items.size()-1; i >= 0; i--) {
          LogixCktIT it = (LogixCktIT)items.elementAt(i);  
          if (it.getLineSource(x,y) != null)
             return it;
      }
      return null;
   }

   LogixCktIT itemHitForLineDestination(int x, int y) {
      for (int i = outputs.size()-1; i >= 0; i--) {
          LogixCktNub it = (LogixCktNub)outputs.elementAt(i);  
          if (it.getLineDestination(x,y) != null)
             return it;
      }
      for (int i = items.size()-1; i >= 0; i--) {
          LogixCktIT it = (LogixCktIT)items.elementAt(i);  
          if (it.getLineDestination(x,y) != null)
             return it;
      }
      return null;
   }

   LogixNub getLineDestination(int x, int y) { 
      if (x < boundingBox.x - 2 || x > boundingBox.x + boundingBox.width + 2 ||
             y < boundingBox.y - 2|| y > boundingBox.y + boundingBox.height + 2)
         return null;
      int i = 0;
      while (i < inputs.size() && ((LogixCktNub)inputs.elementAt(i)).source != null)
         i++;
      if (i >= inputs.size())
         return null;
      LogixCktNub it_best = (LogixCktNub)inputs.elementAt(i);
      double d_min = (x-it_best.connect_x)*(x-it_best.connect_x) + (y-it_best.connect_y)*(y-it_best.connect_y);
      for (int j = i+1; j < inputs.size(); j++) {
         LogixCktNub it = (LogixCktNub)inputs.elementAt(j);
         if (it.source == null) {
            double d = (x-it.connect_x)*(x-it.connect_x) + (y-it.connect_y)*(y-it.connect_y);
            if (d < d_min) {
               d_min = d;
               it_best = it;
            }
         }
      }
      return it_best;
   }

   LogixNub getLineSource(int x, int y) { 
      if (x < boundingBox.x - 2 || x > boundingBox.x + boundingBox.width + 2 ||
             y < boundingBox.y - 2|| y > boundingBox.y + boundingBox.height + 2)
         return null;
      if (outputs.size() == 0)
         return null;
      LogixCktNub it_best = (LogixCktNub)outputs.elementAt(0);
      double d_min = (x-it_best.connect_x)*(x-it_best.connect_x) + (y-it_best.connect_y)*(y-it_best.connect_y);
      for (int j = 1; j < outputs.size(); j++) {
         LogixCktNub it = (LogixCktNub)outputs.elementAt(j);
         double d = (x-it.connect_x)*(x-it.connect_x) + (y-it.connect_y)*(y-it.connect_y);
         if (d < d_min) {
            d_min = d;
            it_best = it;
         }
      }
      return it_best;
   }

   LogixCktIT copy() {  // copies lines on inside only
      LogixCkt it = new LogixCkt();
      it.selected = selected;
      it.on = on;
      it.iconified = iconified;
      it.name = name;
      it.savedBoundingBox = new FloatRect(savedBoundingBox.x,savedBoundingBox.y,
                                             savedBoundingBox.width,savedBoundingBox.height);
      
      it.inputs.setSize(inputs.size());
      it.outputs.setSize(outputs.size());
      it.lines.setSize(lines.size());
      it.items.setSize(items.size());
      
      for (int i = 0; i < inputs.size(); i++) {
         LogixCktIT x = (LogixCktIT)inputs.elementAt(i);
         it.inputs.setElementAt(x.copy(),i);
      }
      for (int i = 0; i < outputs.size(); i++) {
         LogixCktIT x = (LogixCktIT)outputs.elementAt(i);
         it.outputs.setElementAt(x.copy(),i);
      }
      for (int i = 0; i < items.size(); i++) {
         LogixCktIT x = (LogixCktIT)items.elementAt(i);
         it.items.setElementAt(x.copy(),i);
      }
      for (int i = 0; i < lines.size(); i++) {
         Logixline x = (Logixline)lines.elementAt(i);
         x.pos = i;
         it.lines.setElementAt(x.copy(),i);
      }

      for (int i = 0; i < inputs.size(); i++) {
         LogixCktNub x = (LogixCktNub)inputs.elementAt(i);
         LogixCktNub newx = (LogixCktNub)it.inputs.elementAt(i);
         newx.destination.setSize(x.destination.size());
         for (int j = 0; j < x.destination.size(); j++) {
            Logixline lin = (Logixline)it.lines.elementAt(((Logixline)x.destination.elementAt(j)).pos);
            newx.destination.setElementAt(lin,j);
            lin.source = newx;
         }
      }

      for (int i = 0; i < outputs.size(); i++) {
         LogixCktNub x = (LogixCktNub)outputs.elementAt(i);
         LogixCktNub newx = (LogixCktNub)it.outputs.elementAt(i);
         if (x.source != null) {
            Logixline lin = (Logixline)(it.lines.elementAt(x.source.pos));
            newx.source = lin;
            lin.destination = newx;
         }
      }

      for (int i = 0; i < items.size(); i++) {
         LogixCktIT x = (LogixCktIT)items.elementAt(i);
         if (x instanceof Tack) {
            Tack y = (Tack)x;
            Tack newx = (Tack)it.items.elementAt(i);
            if (y.source != null) {
               Logixline lin = (Logixline)(it.lines.elementAt(y.source.pos));
               newx.source = lin;
               lin.destination = newx;
            }
            newx.destination.setSize(y.destination.size());
            for (int j = 0; j < y.destination.size(); j++) {
               Logixline lin = (Logixline)it.lines.elementAt(((Logixline)y.destination.elementAt(j)).pos);
               newx.destination.setElementAt(lin,j);
               lin.source = newx;
            }
         }
         else if (x instanceof LogixGates) {
            LogixGates y = (LogixGates)x;
            LogixGates newx = (LogixGates)it.items.elementAt(i);
            if (y.in1.source != null) {
               Logixline lin = (Logixline)(it.lines.elementAt(y.in1.source.pos));
               newx.in1.source = lin;
               lin.destination = newx.in1;
            }
            if (y.in2 != null && y.in2.source != null) {
               Logixline lin = (Logixline)(it.lines.elementAt(y.in2.source.pos));
               newx.in2.source = lin;
               lin.destination = newx.in2;
            }
            newx.out.destination.setSize(y.out.destination.size());
            for (int j = 0; j < y.out.destination.size(); j++) {
               Logixline lin = (Logixline)it.lines.elementAt(((Logixline)y.out.destination.elementAt(j)).pos);
               newx.out.destination.setElementAt(lin,j);
               lin.source = newx.out;
            }
         }
         else if (x instanceof LogixCkt) {
            LogixCkt y = (LogixCkt)x;
            LogixCkt newx = (LogixCkt)it.items.elementAt(i);
            for (int k = 0; k < y.inputs.size(); k++) {
               LogixCktNub io = (LogixCktNub)y.inputs.elementAt(k);
               LogixCktNub newio = (LogixCktNub)newx.inputs.elementAt(k);
               if (io.source != null) {
                  Logixline lin = (Logixline)(it.lines.elementAt(io.source.pos));
                  newio.source = lin;
                  lin.destination = newio;
               }
            }
            for (int k = 0; k < y.outputs.size(); k++) {
               LogixCktNub io = (LogixCktNub)y.outputs.elementAt(k);
               LogixCktNub newio = (LogixCktNub)newx.outputs.elementAt(k);
               newio.destination.setSize(io.destination.size());
               for (int j = 0; j < io.destination.size(); j++) {
                  Logixline lin = (Logixline)it.lines.elementAt(((Logixline)io.destination.elementAt(j)).pos);
                  newio.destination.setElementAt(lin,j);
                  lin.source = newio;
               }
            }
         }
      }
      
      it.reshape(boundingBox.x,boundingBox.y,boundingBox.width,boundingBox.height);
      return it;
   }
   
   void delete(LogixCkt owner) {
      powerOff();
      owner.items.removeElement(this);
      for (int i = 0; i < inputs.size(); i++) {
         LogixCktNub in = (LogixCktNub)inputs.elementAt(i);
         if (in.source != null) {
            owner.lines.removeElement(in.source);
            in.source.source.destination.removeElement(in.source);
         }
      }
      for (int i = 0; i < outputs.size(); i++) {
         LogixCktNub out = (LogixCktNub)outputs.elementAt(i);
         for (int j = 0; j < out.destination.size(); j++) {
            Logixline line = (Logixline)out.destination.elementAt(j);
            owner.lines.removeElement(line);
            line.destination.source = null;           
         }
      }
   }
   
   void unDelete(LogixCkt owner) {
      owner.items.addElement(this);
      for (int i = 0; i < inputs.size(); i++) {
         LogixCktNub in = (LogixCktNub)inputs.elementAt(i);
         if (in.source != null) {
            owner.lines.addElement(in.source);
            in.source.source.destination.addElement(in.source);
         }
      }
      for (int i = 0; i < outputs.size(); i++) {
         LogixCktNub out = (LogixCktNub)outputs.elementAt(i);
         for (int j = 0; j < out.destination.size(); j++) {
            Logixline line = (Logixline)out.destination.elementAt(j);
            owner.lines.addElement(line);
            line.destination.source = line;           
         }
      }
   }
   
}
