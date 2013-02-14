size(500,500);
background(255);
fill(255,0,127);

beginShape(TRIANGLES);
vertex(30,75); 
vertex(40,20); 
vertex(50,75); 
vertex(60,20); 
vertex(70,75); 
vertex(80,20); 

int x=100;
beginShape(TRIANGLE_STRIP);
vertex(x+30,75); 
vertex(x+40,20); 
vertex(x+50,75); 
vertex(x+60,20); 
vertex(x+70,75); 
vertex(x+80,20); 
endShape();

int y=200;
beginShape(QUADS);
vertex(y+30,75); 
vertex(y+40,20); 
vertex(y+50,75); 
vertex(y+60,20); 
vertex(y+70,75); 
vertex(y+80,20); 
endShape();


beginShape(QUADS);
vertex(y+30,75); 
vertex(y+40,20); 
vertex(y+50,75); 
vertex(y+60,20); 
vertex(y+70,75); 
vertex(y+80,20); 
endShape();
