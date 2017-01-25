import cz.sam.inet.INetManager;
import cz.sam.inet.packet.INetPacket;
import cz.sam.inet.packet.INetPacketListener;
import cz.sam.inet.packet.INetPacketSerializer;

public class MainTest implements INetPacketListener {

	public static void main(String[] args) {
		MainTest m = new MainTest();
		m.start();
	}
	
	public class PacketMessage extends INetPacket {
		
		public String message;
		
		@Override
		public void send(INetPacketSerializer serializer) throws Exception {
			serializer.writeString("sdf");
		}

		@Override
		public void receive(INetPacketSerializer serializer) throws Exception {
			this.message = serializer.readString();
		}

		@Override
		public int getPacketID() {
			return 0;
		}
		
	};
	
	public PacketMessage packetMessage = new PacketMessage();
		
	public void start() {
		this.packetMessage.message = "Hello world !";
		
		
		INetManager server = new INetManager();
		server.createServer(25565);
		
		INetManager client = new INetManager();
		client.connect("127.0.0.1", 25565);
		client.addPacketListener(this);
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		server.sendPacket(this.packetMessage);
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		client.stop();
		server.stop();
	}

	@Override
	public void onPacketReceive(INetPacket packet) {
		System.out.println("Packet recieved with id: " + packet.getPacketID());
		if(packet.getPacketID() == this.packetMessage.getPacketID()) {
			PacketMessage messagePacket = (PacketMessage) packet;
			System.out.println("Client received message: " + messagePacket.message);
		}
	}
	
}
