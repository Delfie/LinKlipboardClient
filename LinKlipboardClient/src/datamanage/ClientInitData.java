package datamanage;

import java.util.Vector;

import contents.Contents;

public class ClientInitData {
	Vector<Contents> history;
	Vector<String> clients;

	public Vector<Contents> getHistory() {
		return this.history;
	}

	public Vector<String> getClients() {
		return this.clients;
	}
}