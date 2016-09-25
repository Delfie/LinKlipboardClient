package user_interface;

import javax.swing.JPanel;

import client_manager.LinKlipboardClient;

public abstract class BasePanel extends JPanel {

	protected LinKlipboardClient client;

	public BasePanel(LinKlipboardClient client) {
		this.client = client;
	}

	public LinKlipboardClient getClient() {
		return this.client;
	}
}
