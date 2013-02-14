size(500,500);
background(255);
fill(255,0,127);

beginShape();
curveVertex(0,0); //C1
curveVertex(0,0); //V1
curveVertex(200,200); //V2
curveVertex(300,20); //V3
curveVertex(500,400); //V4
curveVertex(500,500); //C2

//vertex(width,height);
//vertex(0,height);
vertex(width,0);
endShape(CLOSE);
//
//fill(150,200)
//beginShape();
//curveVertex(height,0); //C1
//curveVertex(0,0); //V1
//curveVertex(200,200); //V2
//curveVertex(300,20); //V3
//curveVertex(500,400); //V4
//curveVertex(500,500); //C2

endShape(CLOSE);
//look into bezier curve too!
