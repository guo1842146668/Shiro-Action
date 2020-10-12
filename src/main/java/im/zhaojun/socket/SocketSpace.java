package im.zhaojun.socket;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

public class SocketSpace {
	private Socket socket;
    private InputStream is;
    private PrintStream out;
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public InputStream getIs() {
		return is;
	}
	public void setIs(InputStream is) {
		this.is = is;
	}
	public PrintStream getOut() {
		return out;
	}
	public void setOut(PrintStream out) {
		this.out = out;
	}

}
