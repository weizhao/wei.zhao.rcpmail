package rcpmail;

import java.util.HashMap;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.ISWTObservable;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rcpmail.model.Server;

public class CreateServerPage extends WizardPage {

	private static HashMap<Control, ControlDecoration> decoratorMap = new HashMap<Control, ControlDecoration>();

	public static class PortValidator implements IValidator {
		private final IStatus errorStatus = ValidationStatus
				.error("Please enter a positive number between 0 and 10000 for Port.");

		public IStatus validate(Object value) {
			int port = -1;
			try {
				port = (Integer) value;
			} catch (Exception e) {
				return errorStatus;
			}
			if (port < 0 || port > 10000) {
				return errorStatus;
			}
			return ValidationStatus.ok();
		}

	}

	public static class HostnameValidator implements IValidator {

		public IStatus validate(Object value) {
			String string = (String) value;
			if (string == null || string.trim().length() == 0) {
				return ValidationStatus
						.error("Please enter a value for hostname.");
			}
			if (!string.matches("^[-\\w.]+$")) {
				return ValidationStatus
						.error("Hostname must be of the form xxx.xxx.");
			}
			return ValidationStatus.ok();
		}

	}

	public static class NotEmptyValidator implements IValidator {

		private final String fieldname;

		public NotEmptyValidator(String fieldname) {
			this.fieldname = fieldname;
		}

		public IStatus validate(Object value) {
			String string = (String) value;
			if (string == null || string.trim().length() == 0) {
				return ValidationStatus.error("Please enter a value for "
						+ fieldname + ".");
			}
			return ValidationStatus.ok();
		}

	}

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
		createControlDecoration(hostnameText);

		final Label usernameLabel = new Label(container, SWT.NONE);
		usernameLabel.setText("Username");

		usernameText = new Text(container, SWT.BORDER);
		usernameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		createControlDecoration(usernameText);

		final Label passwordLabel = new Label(container, SWT.NONE);
		passwordLabel.setText("Password");

		passwordText = new Text(container, SWT.BORDER);
		passwordText.setEchoChar('*');
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		createControlDecoration(passwordText);

		final Label portLabel = new Label(container, SWT.NONE);
		portLabel.setText("Port");

		portText = new Text(container, SWT.BORDER);
		portText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		createControlDecoration(portText);

		final DataBindingContext dbc = new DataBindingContext();

		dbc.bindValue(SWTObservables.observeText(hostnameText, SWT.Modify),
				BeansObservables.observeValue(server, "hostname"),
				new UpdateValueStrategy()
						.setAfterConvertValidator(new HostnameValidator()),
				null);
		dbc.bindValue(SWTObservables.observeText(usernameText, SWT.Modify),
				BeansObservables.observeValue(server, "username"),
				new UpdateValueStrategy()
						.setAfterConvertValidator(new NotEmptyValidator(
								"username")), null);
		dbc.bindValue(SWTObservables.observeText(passwordText, SWT.Modify),
				BeansObservables.observeValue(server, "password"),
				new UpdateValueStrategy()
						.setAfterConvertValidator(new NotEmptyValidator(
								"password")), null);
		dbc.bindValue(SWTObservables.observeText(portText, SWT.Modify),
				BeansObservables.observeValue(server, "port"),
				new UpdateValueStrategy()
						.setAfterConvertValidator(new PortValidator()), null);

		final AggregateValidationStatus aggregateValidationStatus = new AggregateValidationStatus(
				dbc.getValidationStatusProviders(),
				AggregateValidationStatus.MAX_SEVERITY);

		aggregateValidationStatus
				.addValueChangeListener(new IValueChangeListener() {
					public void handleValueChange(ValueChangeEvent event) {
						// the invocation of the getValue method is necessary
						// the further changes will be fired
						aggregateValidationStatus.getValue();
						for (Object o : dbc.getBindings()) {
							Binding binding = (Binding) o;
							IStatus status = (IStatus) binding
									.getValidationStatus().getValue();
							Control control = null;
							if (binding.getTarget() instanceof ISWTObservable) {
								ISWTObservable swtObservable = (ISWTObservable) binding
										.getTarget();
								control = (Control) swtObservable.getWidget();
							}
							ControlDecoration decoration = decoratorMap
									.get(control);
							if (decoration != null) {
								if (status.isOK()) {
									decoration.hide();
								} else {
									decoration.setDescriptionText(status
											.getMessage());
									decoration.show();
								}
							}
						}
					}
				});

		// create this after binding the observables to avoid displaying an
		// error when the wizard page is shown:
		WizardPageSupport.create(this, dbc);
	}

	private void createControlDecoration(Control control) {
		ControlDecoration controlDecoration = new ControlDecoration(control,
				SWT.LEFT | SWT.TOP);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(fieldDecoration.getImage());
		controlDecoration.hide();
		decoratorMap.put(control, controlDecoration);
	}
}
