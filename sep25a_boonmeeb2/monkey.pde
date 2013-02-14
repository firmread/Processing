class Monkey{
  int mkx,mky;
  float mksc;
  PImage boonmee;
  Eye e1;
  
  Monkey(int _mkx,int _mky, float _mksc){
  mkx = _mkx;
  mky = _mky;

  mksc = _mksc;
  
  e1 = new Eye( 50,  16,  80,mkx,mky,mksc);
  }
  
  void drawMonkey(){
    translate (mkx,mky);
    scale(mksc);
    boonmee = loadImage ("boonmee.png");

    //IMAGE
    image(boonmee,0,0);
    e1.update(mouseX, mouseY);
    e1.display();
    
    //EYES
    //setup eye fill
    
    
    fill (255,0,0);
    //setup eye size
    
    float eyeS = 10;
    //map(second()%10,0,10,3,13);
    //eye1
    float eye1X = map(mouseX,0,width,226,236);
    float eye1Y = map(mouseY,0,height,208,216);
    ellipse(eye1X,eye1Y,eyeS,eyeS);
    //eye2
    float eye2X = map(mouseX,0,width,254,262);
    float eye2Y = map(mouseY,0,height,212,219);
    ellipse(eye2X,eye2Y,eyeS,eyeS);
    //eye3
    float eye3X = map(mouseX,0,width,116,125);
    float eye3Y = map(mouseY,0,height,260,267);
    ellipse(eye3X,eye3Y,eyeS,eyeS);
    //eye4
    float eye4X = map(mouseX,0,width,144,152);
    float eye4Y = map(mouseY,0,height,262,269);
    ellipse(eye4X,eye4Y,eyeS,eyeS);
    //eye5
    float eye5X = map(mouseX,0,width,326,335);
    float eye5Y = map(mouseY,0,height,250,255);
    ellipse(eye5X,eye5Y,eyeS,eyeS);  
    //eye6
    float eye6X = map(mouseX,0,width,355,360);
    float eye6Y = map(mouseY,0,height,255,260);
    ellipse(eye6X,eye6Y,eyeS,eyeS);  
  }
    
}
