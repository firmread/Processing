import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class sketch_aug03a extends PApplet {
  public void setup() {
// Firm.Tharit.Tothong

background(37,121,61);
size(600,600);

noStroke();
int x=100;

fill(52,180,224);
ellipse(x,x,30,50);

fill(237,149,34);
ellipse(x*2,x*2,30,50);

fill(52,180,224);
ellipse(x*3,x*3,30,50);

fill(237,149,34);
ellipse(x*4,x*4,30,50);

fill(52,180,224);
ellipse(x*5,x*5,30,50); 

print("Love ");
println("it will not betray you,");
println("dismay or enslave you,");
println("It will set you free,");
println("Be more like the man you were made to be.");

println("");
println("from the track 'Sigh No More' by Mumford and Sons");

  noLoop();
} 
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "sketch_aug03a" });
  }
}
