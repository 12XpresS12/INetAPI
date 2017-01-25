package cz.sam.inet;

import cz.sam.inet.packet.INetPacket;
import cz.sam.inet.packet.INetPacketListener;
import cz.sam.inet.packet.INetPacketSerializer;

public interface INetSide {
	
	public void start();
	
	public void stop();
	
	public void sendPacket(INetPacket packet);
	
	public void sendPacket(INetPacket packet, int clientID);
	
	public void addPacketListener(INetPacketListener listener);
	
	public INetPacketSerializer getPacketSerializer();
	
}
