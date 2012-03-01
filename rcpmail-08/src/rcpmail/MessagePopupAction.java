package rcpmail;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;

public class MessagePopupAction extends Action {

	private final IWorkbenchWindow window;

	MessagePopupAction(String text, IWorkbenchWindow window) {
		super(text);
		this.window = window;
		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_OPEN_MESSAGE);
		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
		setImageDescriptor(rcpmail.Activator
				.getImageDescriptor("/icons/silk/application_form_add.png"));
	}

	@Override
	public void run() {
		WizardDialog dialog = new WizardDialog(window.getShell(),
				new CreateServerWizard());
		dialog.open();
	}
}