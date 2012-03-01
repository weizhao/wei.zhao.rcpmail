package rcpmail;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	private final String NAVIGATIONVIEW_ID = "rcpmail.navigationView";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		IFolderLayout navFolder = layout.createFolder("navigate",
				IPageLayout.LEFT, 0.25f, editorArea);
		navFolder.addView(NAVIGATIONVIEW_ID);

		IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP,
				0.5f, editorArea);
		folder.addPlaceholder(View.ID + ":*");
		folder.addView(View.ID);
		layout.addView(MessageTableView.ID, IPageLayout.TOP, 0.45f, "messages");

		layout.getViewLayout(MessageTableView.ID).setCloseable(false);
		layout.getViewLayout(MessageTableView.ID).setMoveable(false);

		// for messages
		layout.getViewLayout(View.ID).setMoveable(false);
		layout.getViewLayout(View.ID).setCloseable(false);
	}
}
