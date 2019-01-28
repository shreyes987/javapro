//package Logix_ckt;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;


public class GreenMetalTheme extends DefaultMetalTheme {

    public String getName() { return "Emerald"; }

  // greenish colors
    private final ColorUIResource primary1 = new ColorUIResource(51, 102, 51);
    private final ColorUIResource primary2 = new ColorUIResource(102, 153, 102);
    private final ColorUIResource primary3 = new ColorUIResource(153, 204, 153); 

    protected ColorUIResource getPrimary1() { return primary1; }  
    protected ColorUIResource getPrimary2() { return primary2; } 
    protected ColorUIResource getPrimary3() { return primary3; } 

}
