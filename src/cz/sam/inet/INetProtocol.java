package cz.sam.inet;

public enum INetProtocol {
	
	API_SIDE(0),
	USER_SIDE(1);
	
	private final int id;
	
	INetProtocol(int id) {
		this.id = id;
	}
	
	public int getID() {
		return this.id;
	}
	
	public static INetProtocol fromId(int id) {
		for(INetProtocol protocol : INetProtocol.values()) {
			if(protocol.getID() == id) {
				return protocol;
			}
		}
		return null;
	}
	
}
