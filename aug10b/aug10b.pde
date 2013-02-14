void mouseDragged() {
  loadPixels();
  fill(pixels[mouseY*width+mouseX]);
  rect(mouseX-mouseX%5,mouseY-mouseY%5,5,5);
}
