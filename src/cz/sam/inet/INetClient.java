package cz.sam.inet;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.sam.inet.packet.INetPacket;
import cz.sam.inet.packet.INetPacketListener;
import cz.sam.inet.packet.INetPacketSerializer;

public class INetClient implements INetSide {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Socket clientSocket;
	private INetPacketSerializer packetSerializer;
	private INetWriteThread writeThread;
	private INetReadThread readThread;
	private String host;
	private int port;
	private boolean stopped;
	
	public INetClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void start() {
		try {
			this.clientSocket = new Socket(this.host, this.port);
			this.packetSerializer = INetPacketSerializer.fromSocket(this.clientSocket);
			this.writeThread = new INetWriteThread(this);
			this.readThread = new INetReadThread(this);
			this.writeThread.start();
			this.readThread.start();
			this.logger.log(Level.INFO, "INetManager (CLIENT): Client connected to " + this.host + ":" + this.port);
		} catch (Exception e) {
			this.logger.log(Level.INFO, "INetManager (CLIENT): Can't connect to server ! (" + e.getMessage() + ")");
		}
	}

	@Override
	public void stop() {
		if(this.stopped) return;
		this.stopped = true;
		this.writeThread.stop();
		this.readThread.stop();
		try {
			this.clientSocket.close();
		} catch (IOException e) { }
		this.logger.log(Level.INFO, "INetManager (CLIENT): Stopped");
	}

	@Override
	public void sendPacket(INetPacket packet) {
		this.writeThread.sendPacket(packet);
	}

	@Override
	public void addPacketListener(INetPacketListener listener) {
		this.readThread.addPacketListener(listener);
	}

	@Override
	public INetPacketSerializer getPacketSerializer() {
		return this.packetSerializer;
	}

	@Override
	public void sendPacket(INetPacket packet, int clientID) { }

}
