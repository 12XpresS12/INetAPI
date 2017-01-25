package cz.sam.inet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.sam.inet.packet.INetPacket;
import cz.sam.inet.packet.INetPacketListener;
import cz.sam.inet.packet.INetPacketSerializer;

public class INetServer implements INetSide, Runnable {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private ServerSocket serverSocket;
	private INetServerClientManager clientManager;
	private int port;
	private boolean isRunning = true;
	private Thread thread;
	
	public INetServer(int port) {
		this.port = port;
		this.clientManager = new INetServerClientManager();
	}

	@Override
	public void start() {
		try {
			this.serverSocket = new ServerSocket(this.port);
			this.thread = new Thread(this);
			this.thread.start();
		} catch(Exception e) {
			this.logger.log(Level.INFO, "INetManager (SERVER): Cannot create server ! (" + e.getMessage() + ")");
		}
	}
	
	@Override
	public void run() {
		this.logger.log(Level.INFO, "INetManager (SERVER): Server created on port " + this.port);
		while(this.isRunning) {
			try {
				Socket socket = this.serverSocket.accept();
				INetServerClient client = new INetServerClient(socket, this.clientManager);
				client.start();
				this.clientManager.add(client);
				this.logger.log(Level.INFO, "INetManager (SERVER): Client connected from: " + socket.getInetAddress().getHostName());
			} catch(Exception e) {
				if(!this.isRunning) return;
				this.logger.log(Level.INFO, "INetManager (SERVER): Error client: " + e.getMessage());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void stop() {
		this.isRunning = false;
		this.thread.stop();
		for(Entry<Integer, INetServerClient> e : this.clientManager.getClients().entrySet()) {
			e.getValue().stop();
		}
		try {
			this.serverSocket.close();
		} catch (IOException e) { }
	}

	@Override
	public void sendPacket(INetPacket packet) {
		for(Entry<Integer, INetServerClient> e : this.clientManager.getClients().entrySet()) {
			e.getValue().sendPacket(packet);
		}
	}
	
	@Override
	public void sendPacket(INetPacket packet, int clientID) {
		INetServerClient client = this.clientManager.get(clientID);
		if(client != null) {
			client.sendPacket(packet);
		}
	}

	@Override
	public void addPacketListener(INetPacketListener listener) {
		
	}

	@Override
	public INetPacketSerializer getPacketSerializer() {
		return null;
	}

}
