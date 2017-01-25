package cz.sam.inet;

import java.util.LinkedList;
import java.util.Queue;

import cz.sam.inet.packet.INetPacket;

public class INetWriteThread extends INetRunnable {
	
	private INetSide netSide;
	private volatile Queue<INetPacket> packetQueue;
	
	public INetWriteThread(INetSide netSide) {
		this.netSide = netSide;
		this.packetQueue = new LinkedList<INetPacket>();
	}

	@Override
	public void update() {
		if(this.packetQueue.size() > 0) {
			try {
				INetPacket packet = this.packetQueue.poll();
				INetPacket.sendPacket(this.netSide, packet);
			} catch(Exception ex) {
				ex.printStackTrace();
				this.netSide.stop();
			}
		}
	}
	
	public void sendPacket(INetPacket packet) {
		this.packetQueue.add(packet);
	}
	
}
