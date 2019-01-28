//package Logix_ckt;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;


public class DemoMetalTheme extends DefaultMetalTheme {

    public String getName() { return "Presentation"; }

    private final FontUIResource controlFont = new FontUIResource("Dialog", Font.BOLD, 18);
    private final FontUIResource systemFont = new FontUIResource("Dialog", Font.PLAIN, 18);
    private final FontUIResource userFont = new FontUIResource("SansSerif", Font.PLAIN, 18);
    private final FontUIResource smallFont = new FontUIResource("Dialog", Font.PLAIN, 14);

    public FontUIResource getControlTextFont() { return controlFont;}
    public FontUIResource getSystemTextFont() { return systemFont;}
    public FontUIResource getUserTextFont() { return userFont;}
    public FontUIResource getMenuTextFont() { return controlFont;}
    public FontUIResource getWindowTitleFont() { return controlFont;}
    public FontUIResource getSubTextFont() { return smallFont;}

    public void addCustomEntriesToTable(UIDefaults table) {
         super.addCustomEntriesToTable(table);

         final int internalFrameIconSize = 22;
         table.put("InternalFrame.closeIcon", MetalIconFactory.getInternalFrameCloseIcon(internalFrameIconSize));
         table.put("InternalFrame.maximizeIcon", MetalIconFactory.getInternalFrameMaximizeIcon(internalFrameIconSize));
         table.put("InternalFrame.iconifyIcon", MetalIconFactory.getInternalFrameMinimizeIcon(internalFrameIconSize));
         table.put("InternalFrame.minimizeIcon", MetalIconFactory.getInternalFrameAltMaximizeIcon(internalFrameIconSize));


         table.put( "ScrollBar.width", new Integer(21) );



    }

}
