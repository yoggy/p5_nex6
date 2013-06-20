package net.sabamiso.processing.nex6;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import processing.core.PApplet;
import processing.core.PImage;

public class Nex6 {
	PApplet papplet;
	String host;
	int port;

	Socket socket;
	InputStream is;
	OutputStream os;
	
	byte [] recv_buf = new byte[1024];

	public Nex6(PApplet papplet) {
		this.papplet = papplet;
		this.host = "192.168.122.1";
		this.port = 8080;
	}
	
	public Nex6(PApplet papplet, String host, int port) {
		this.papplet = papplet;
		this.host = host;
		this.port = port;
	}
	
	public String getHost() {
		return this.host;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public boolean start() {
		boolean rv;
		
		rv = connect();
		if (rv == false) return false;
		
		// start preview
		rv = send("POST /sony/camera HTTP/1.1\r\nContent-Length: 60\r\n\r\n{\"method\":\"startRecMode\",\"params\":[],\"id\":5,\"version\":\"1.0\"}");
		if (rv == false) return false;

		// recv (dummy)
		int size = recv(recv_buf, 0, recv_buf.length);
		if (size <= 0) return false;
		
		close();
		
		return true;
	}

	public PImage takePicture() {
		boolean rv;
		
		rv = connect();
		if (rv == false) {
			close();
			return null;
		}
		
		// request take picture
		rv = send("POST /sony/camera HTTP/1.1\r\nContent-Length: 63\r\n\r\n{\"method\":\"actTakePicture\",\"params\":[],\"id\":10,\"version\":\"1.0\"}");
		if (rv == false) {
			close();
			return null;
		}

		// recv response
		int recv_size = recv(recv_buf, 0, recv_buf.length);
		if (recv_size <= 0) {
			close();
			return null;
		}

		// parse response & download picture
		PImage image = null;
		try {
			String res = new String(recv_buf, 0, recv_size);
			String body = res.split("\r\n\r\n")[1];
			int body_length = body.length();
			
			// {"id":10,"result":[["http://192.168.122.1:8080/postview/pict20130614_175010_0.JPG"]]}
			String url = body.substring(21, body_length - 4);
			System.out.println("url=" + url);
			image = papplet.loadImage(url);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		close();
		return image;
	}

	protected boolean connect() {
		close();
		try {
			socket = new Socket(host, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
		}
		catch(Exception e) {
			e.printStackTrace();
			close();
			return false;
		}
		
		return true;
	}
	
	protected void close() {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
			}
			is = null;
		}
		
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
			}
			os = null;
		}
		
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
			socket = null;
		}
	}
	
	protected boolean send(String str) {
		try {
			os.write(str.getBytes());
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	protected int recv(byte [] buf, int offset, int buf_size) {
		int read_size = -1;
		try {
			read_size = is.read(buf, offset, buf_size);
			return read_size;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}
}
