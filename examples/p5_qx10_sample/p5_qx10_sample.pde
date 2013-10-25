//
// for DSC-QX10 sample
//
import net.sabamiso.processing.nex6.*;

Nex6 qx10;
PImage image;

public void setup() {
  size(720, 540);

  qx10 = new Nex6(this, "10.0.0.1", 10000);
  boolean rv = qx10.start();
  if (rv == false) {
    println("error: nex6.start() failed...");
    return;
  }
}

public void draw() {
  if (image != null) {
    image(image, 0, 0, width, height);
  }
}

public void mousePressed() {
  image = qx10.takePicture(); // 5M mode -> picture size : 1440 x 1080
  
  // check window size
  if (image == null) {
    // failed...
  }
  else {
    println("width=" + image.width + ", height=" + image.height);  
  }
}
