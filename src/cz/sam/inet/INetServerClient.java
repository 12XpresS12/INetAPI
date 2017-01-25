package cz.sam.inet;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.sam.inet.packet.INetPacket;
import cz.sam.inet.packet.INetPacketListener;
import cz.sam.inet.packet.INetPacketSerializer;

public class INetServerClient implements INetSide {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Socket socket;
	private INetServerClientManager clientManager;
	private INetPacketSerializer packetSeralizer;
	private INetWriteThread writeThread;
	private INetReadThread readThread;
	private int clientID;
	
	public INetServerClient(Socket socket, INetServerClientManager clientManager) {
		this.socket = socket;
		this.clientManager = clientManager;
	}
	
	@Override
	public void start() {
		try {
			this.packetSeralizer = INetPacketSerializer.fromSocket(this.socket);
			this.writeThread = new INetWriteThread(this);
			this.readThread = new INetReadThread(this);
			this.writeThread.start();
			this.readThread.start();
		} catch(Exception ex) {
			this.stop();
		}
	}

	@Override
	public void stop() {
		this.clientManager.remove(this.clientID);
		this.writeThread.stop();
		this.readThread.stop();
		try {
			this.socket.close();
		} catch (IOException e) { }
		this.logger.log(Level.INFO, "INetManager (SERVER): Client disconnected!");
	}

	@Override
	public void sendPacket(INetPacket packet) {
		this.writeThread.sendPacket(packet);
	}

	@Override
	public void sendPacket(INetPacket packet, int clientID) { }

	@Override
	public void addPacketListener(INetPacketListener listener) {
		this.readThread.addPacketListener(listener);
	}

	@Override
	public INetPacketSerializer getPacketSerializer() {
		return this.packetSeralizer;
	}
	
	public void setClientID(int id) {
		this.clientID = id;
	}
	
	public int getClientID() {
		return this.clientID;
	}

}
