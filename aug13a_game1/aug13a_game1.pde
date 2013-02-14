//Ship Vars
int ship_x, ship_y, ship_r_speed, max_speed;
float ship_x_speed, ship_y_speed, ship_rotation, ship_friction, ship_acceleration;
Boolean fireFlame = false;

//Bullet Vars
Boolean isBulletFired;
int bullet_x, bullet_y, bullet_speed;
float bullet_heading;

// key presses, handles multiple key presses
final int keyDownArraySize = 127;
boolean[] keyDown = new boolean[keyDownArraySize];

void setup() {
  size(720, 380);
  frameRate(60);
  smooth();
    
  initShip();
  initKeyDownArray();
  initAsteroids();
  
  //init Bullet. When it is fired, more stuff is assigned.
  //isBulletFired = false;
  //bullet_speed = 8;
} 
void initShip() {
  ship_x = int(width  * .5);
  ship_y = int(height * .5);
  ship_acceleration = .15;
  ship_friction = .99;
  ship_r_speed = 12;
  max_speed = 10;
    }
void initKeyDownArray() {
  for(int i=0; i<keyDownArraySize; i++) {
    keyDown[i] = false;
  }
}
void initAsteroids() {}
void draw() {
    background(0);
    
    updateShip();
      if (isBulletFired) {
     updateBullet();
   }
    
    handleInput();
}
    
void updateShip() {
    //move ship
  ship_x += ship_x_speed;
  ship_y += ship_y_speed;
    
    //draw ship    
    drawShip();
}
// constrain = limit the range of number input > output 
  constrain(ship_x_speed, -max_speed, max_speed);
  constrain(ship_y_speed, -max_speed, max_speed);
  ship_x += ship_x_speed;
  ship_y += ship_y_speed;
  
  //add friction
  ship_x_speed *= ship_friction;
  ship_y_speed *= ship_friction;
  //abs = absolute value
  //too small to see and processing will be loaded with floats so,,,
  if (abs(ship_x_speed) < .05) ship_x_speed = 0;
  if (abs(ship_y_speed) < .05) ship_y_speed = 0;
}

void drawShip() {
  noFill();
  stroke(0, 255, 0);
  strokeWeight(1);
  
  pushMatrix();
    translate(ship_x, ship_y);
// always start drawing ship at 0,0
// because you'll have to make the ship rotate freely
// using sin and cos
// so when we rotate the ship actually we rotate the whole space
// translate do all this shit!

    rotate(radians(ship_rotation));
// ship_rotation is the angle of the ship ex. 90 180 ...    

    beginShape();
    vertex(15, 0);
    vertex(-3, 6);
    vertex(-1, 0);
    vertex(-3, -6);
    endShape(CLOSE);
    
    if (fireFlame) {
      beginShape();
        vertex(-8, 0);
        vertex(-2, 2);
        vertex(-1, 0);
        vertex(-2, -2);
      endShape(CLOSE);
      fireFlame = false;
    }
    
  popMatrix();
}
void checkScreenBounds() {
  //wrap left and right of screen
  if (ship_x > width) {
    ship_x = 0;
  } else if (ship_x < 0) {
    ship_x = width;
  }
  //wrap top and bottom of screen
  if (ship_y > height) {
    ship_y = 0;
  } else if (ship_y < 0) {
    ship_y = height;
  }
}
//------------------------------------------------------------------------- BULLET CONTROLS
void updateBullet() {
  checkBulletEdge();
  
  bullet_x += bullet_speed * cos(radians(bullet_heading));
  bullet_y += bullet_speed * sin(radians(bullet_heading));
  
  strokeWeight(frameCount%4 + 3);
  point(bullet_x, bullet_y);
  
}
void checkBulletEdge() {
  if ( (bullet_x > width) || (bullet_x < 0) || (bullet_y < 0) || (bullet_y > height) ) {
    isBulletFired = false;
  }
}


//------------------------------------------------------------------------- KEYBOARD CONTROLS
void handleInput() {
  if(keyDown[RIGHT]) {
    ship_rotation += ship_r_speed;
  }
  if(keyDown[LEFT]) {
    ship_rotation -= ship_r_speed;
  }
  if(keyDown[UP]) {
    fireFlame = true;
    ship_x_speed += ship_acceleration * cos(radians(ship_rotation));
    ship_y_speed += ship_acceleration * sin(radians(ship_rotation));
  }
  if(keyDown[DOWN]) {
    ship_x_speed -= ship_acceleration * cos(radians(ship_rotation));
    ship_y_speed -= ship_acceleration * sin(radians(ship_rotation));
  }
  if(keyDown[32]) {//32 == space
    if(!isBulletFired) {
      //same as !isBulletFired
      isBulletFired = true;
      bullet_x = ship_x;
      bullet_y = ship_y;
      bullet_heading = ship_rotation;
    }
  }
}

void keyPressed() {
  println(keyCode);
  if(keyCode < keyDownArraySize) {
    keyDown[keyCode] = true;
  }
}
void keyReleased() {
  if(keyCode < keyDownArraySize) {
    keyDown[keyCode] = false;
  }
}


