package cz.sam.inet;

import java.util.logging.Level;
import java.util.logging.Logger;

import cz.sam.inet.packet.INetPacket;
import cz.sam.inet.packet.INetPacketListener;

public class INetManager {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private INetSideType netSideType;
	private INetSide netSide;
	
	public INetManager() { }
	
	public void createServer(int port) {
		if(this.netSideType != null) {
			this.logger.log(Level.INFO, "INetManager: Net side is already defined !");
		}
		this.netSideType = INetSideType.SERVER;
		this.netSide = new INetServer(port);
		this.netSide.start();
	}

	public void connect(String host, int port) {
		if(this.netSideType != null) {
			this.logger.log(Level.INFO, "INetManager: Net side is already defined !");
		}
		this.netSideType = INetSideType.CLIENT;
		this.netSide = new INetClient(host, port);
		this.netSide.start();
	}

	public void sendPacket(INetPacket packet) {
		if(this.netSide != null) {
			this.netSide.sendPacket(packet);
		}
	}

	public void addPacketListener(INetPacketListener listener) {
		if(this.netSide != null) {
			this.netSide.addPacketListener(listener);
		}
	}
	
	public void stop() {
		if(this.netSide != null) {
			this.netSide.stop();
		}
	}
	
	public INetSideType getNetSide() {
		return this.netSideType;
	}
	
	public Logger getLogger() {
		return this.logger;
	}

}
