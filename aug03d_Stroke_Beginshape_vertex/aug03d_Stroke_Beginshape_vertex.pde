size(500,500);
background(50);

strokeWeight(10);
stroke(0,0,255);
line(10,10,490,490);

strokeWeight(50);
stroke(255,0,0,150);
line(490,10,10,490);

noStroke();
fill(255,225);
rect(20,20,160,160);
//BEGINSHAPE TO CREATE MULTIPLE DOT SHAPE
//noFill();
strokeWeight(5);
stroke(0);
beginShape();
vertex(200,20);
vertex(400,20);
vertex(300,180);
endShape(CLOSE);

