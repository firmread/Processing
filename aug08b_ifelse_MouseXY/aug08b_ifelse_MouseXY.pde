void setup(){
  size (500,500);
  //noStroke();
  //stroke(255,0,255);
  noFill();
  smooth();
  
}

void draw(){
  background(255);
  
  if(mouseX<width/2){
    line(0,0,width,height);
    
    if(mouseY>height/2){
      ellipse(width/4,height/4,width/4,height/4);
    }
    else {
      rect(0,height/2,width/4,height/4);
    }
  }
}

//if
//else if
//else if
//else if
//else


//HOMEWORK READING
// READ REPEAT(43-63) IN FORM AND CODE BOOK
// RECREATE PARADE ASSIGNMENT WITH FOR LOOPS AND USING
// A GRADIENT (CAN JUST BE IN BACKGROUND OR INCORPERATED
// IN PARADE PARAMETERS)
// *** GRADIENT IS ACTUALLY A LINES WITH COLORS CHANGING (LOOP ON LOOP) 
// CHECK FOR 'ELSE' ON 
