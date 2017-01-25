package cz.sam.inet.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.sam.inet.INetProtocol;
import cz.sam.inet.INetSide;

public abstract class INetPacket {
	
	private static Map<Integer, INetPacket> packet_storage = new HashMap<>();
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private INetProtocol protocol;
	
	public INetPacket(INetProtocol protocol) {
		this.protocol = protocol;
		if(packet_storage.containsKey(this.getPacketID())) {
			this.logger.log(Level.INFO, "This packet with id: " + this.getPacketID() + " is already exist !");
			System.exit(1);
		}
		
		packet_storage.put(this.getPacketID(), this);
	}
	
	public INetPacket() {
		this(INetProtocol.USER_SIDE);
	}
	
	public static void sendPacket(INetSide netSide, INetPacket packet) throws Exception {
		INetPacketSerializer serializer = netSide.getPacketSerializer();
		serializer.writeInt(packet.getPacketID());
		packet.send(serializer);
	}
	
	public static INetPacket receivePacket(INetSide netSide) throws Exception {
		INetPacketSerializer serializer = netSide.getPacketSerializer();
		int packetID = serializer.readInt();
		INetPacket packet = null;
		if(packet_storage.containsKey(packetID)) {
			packet = packet_storage.get(packetID);
			packet.receive(serializer);
		}
		return packet;
	}
	
	public abstract void send(INetPacketSerializer serializer) throws Exception;
	
	public abstract void receive(INetPacketSerializer serializer) throws Exception;
	
	public abstract int getPacketID();
	
	public INetProtocol getProtocol() {
		return this.protocol;
	}
}
