

background(255);
size(500,500);
noStroke();


//GEOMETRY
//triangle(x1,y1,x2,y2,x3,y3);

fill(200);
triangle(310,10,240,240,80,600);

//rect(x,y,w,h)
//rectMode default is corner x,y is upperleft corner h=h w=w 
//rectMODE(center) means x,y is the CENTER h=h w=w
//rectMode(radiaus) mean x,y is at the CENTER h=h/2 w=w/2

fill(100);
rect(10,10,100,100);

fill(150);
rectMode(CENTER);
rect(110,110,100,100);

fill(200);
rectMode(RADIUS);
rect(210,210,100,100);

fill(100);
point(10,210);


//ecllipseMode default is center 
//(since there is no corner in circle but there are corner mode here)

fill(100);
ellipse(10,310,100,100);

fill(150);
ellipseMode(CENTER);
ellipse(110,410,100,100);

fill(200);
ellipseMode(RADIUS);
ellipse(210,510,100,100);

//fill()
//fill(gray)
//...
