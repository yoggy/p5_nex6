p5_nex6
=========
Sony NEX-6 remote control library for Processsing.

How to use
=========
<pre>
  import net.sabamiso.processing.nex6.*;
  
  Nex6 nex6;
  PImage image;
  
  public void setup() {
    size(808, 540);
  
    nex6 = new Nex6(this);
    boolean rv = nex6.start();  // start preview
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
    image = nex6.takePicture(); // picture size : 1616 x 1080
    if (image == null) {
      // failed...
    }
  }
</pre>
