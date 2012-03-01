package rcpmail;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcpmail.model.Server;

public class CreateServerPage extends WizardPage {
	
	private Text portText;
	private Text passwordText;
	private Text usernameText;
	private Text hostnameText;
	private final Server server;

	/**
	 * Create the wizard
	 * 
	 * @param server
	 */
	public CreateServerPage(Server server) {
		super("wizardPage");
		this.server = server;
		setTitle("Create Server Connection");
		setDescription("Please enter information to create a server connection.");
	}

	/**
	 * Create contents of the wizard
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		container.setLayout(gridLayout);
		//
		setControl(container);

		final Label hotnameLabel = new Label(container, SWT.NONE);
		hotnameLabel.setText("Hostname");

		hostnameText = new Text(container, SWT.BORDER);
		hostnameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		final Label usernameLabel = new Label(container, SWT.NONE);
		usernameLabel.setText("Username");

		usernameText = new Text(container, SWT.BORDER);
		usernameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		final Label passwordLabel = new Label(container, SWT.NONE);
		passwordLabel.setText("Password");

		passwordText = new Text(container, SWT.BORDER);
		passwordText.setEchoChar('*');
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		final Label portLabel = new Label(container, SWT.NONE);
		portLabel.setText("Port");

		portText = new Text(container, SWT.BORDER);
		portText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}

}
