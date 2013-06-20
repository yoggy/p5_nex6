package net.sabamiso.processing.nex6;

import processing.core.PApplet;
import processing.core.PImage;


public class TestMain extends PApplet{
	private static final long serialVersionUID = 8657253119950039899L;

	Nex6 nex6;
	PImage image;
	
	public void setup() {
		size(808, 540);

		nex6 = new Nex6(this);
		boolean rv = nex6.start();
		if (rv == false) {
			println("error: nex6.start() failed...");
			return;
		}
	}
	
	public void draw() {
		if (image != null) {
			image(image, 0, 0, 808, 540); 
		}
	}
	
	public void mousePressed() {
		image = nex6.takePicture(); // picture size : 1616 x 1080
		if (image == null) {
			// failed...
		}
	}
	
	public static void main(String[] args) {
		PApplet.main(new String[] { "net.sabamiso.processing.nex6.TestMain"});
	}
}
