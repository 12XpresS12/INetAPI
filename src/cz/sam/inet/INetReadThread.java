package cz.sam.inet;

import java.util.ArrayList;
import java.util.List;

import cz.sam.inet.packet.INetPacket;
import cz.sam.inet.packet.INetPacketListener;

public class INetReadThread extends INetRunnable {
	
	private List<INetPacketListener> listeners;
	private INetSide netSide;
	
	public INetReadThread(INetSide netSide) {
		this.netSide = netSide;
		this.listeners = new ArrayList<>();
	}
	
	@Override
	public void update() {
		try {
			INetPacket packet = INetPacket.receivePacket(this.netSide);
			for(INetPacketListener listener : this.listeners) {
				listener.onPacketReceive(packet);
			}
		} catch(Exception ex) {
			this.netSide.stop();
		}
	}
	
	public void addPacketListener(INetPacketListener listener) {
		this.listeners.add(listener);
	}

}
