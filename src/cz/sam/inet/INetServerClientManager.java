package cz.sam.inet;

import java.util.HashMap;
import java.util.Map;

public class INetServerClientManager {
	
	private Map<Integer, INetServerClient> clientList;
	
	public INetServerClientManager() {
		this.clientList = new HashMap<>();
	}
	
	public int add(INetServerClient client) {
		int id = 0;
		while(this.clientList.containsKey(id)) {
			id++;
		}
		client.setClientID(id);
		this.clientList.put(id, client);
		return id;
	}
	
	public void remove(int id) {
		this.clientList.remove(id);
	}
	
	public INetServerClient get(int id) {
		if(this.clientList.containsKey(id)) {
			return this.clientList.get(id);
		}
		return null;
	}
	
	public Map<Integer, INetServerClient> getClients() {
		return this.clientList;
	}
	
}
