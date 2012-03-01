package rcpmail.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rcpmail.MessageView;

public class DeleteViewMessageHandler extends AbstractHandler {

	private MessageView messageView;

	public DeleteViewMessageHandler(MessageView view) {
		messageView = view;
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (messageView.getMessage() != null) {
			rcpmail.handlers.DeleteMessageHandler.trashMessage(messageView
					.getMessage());
		}
		return null;
	}

	public void setEnabled(boolean b) {
		setBaseEnabled(b);
	}
}
