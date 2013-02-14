//String[] fontList = PFont.list();
//printin(fontList);

//PFont is a variable which can store the font for you!

PFont myFont;
PFont tryFont;

void setup(){
  size(500,500);
  background(255,0,127);
  myFont = createFont("Helvetica",60,true);
  //createFont load the font from the computer 
  //not recommended though, not universal
  //3rd arguement is anti-aliasing true=on false=off
  
  tryFont = loadFont("AndaleMono-48.vlw");
  //have to go to Tools>Create Fonts
  //font file will be in the folder of processing file
  //and here we go!, anywhere!!!!
  
  textFont(tryFont);
  text("hello world",80,250);
  text("hello universe",80,320);
}

void draw(){
}

// bouncing the ball in canvas
