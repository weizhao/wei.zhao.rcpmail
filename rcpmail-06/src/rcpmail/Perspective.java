package rcpmail;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	private final String NAVIGATIONVIEW_ID = "rcpmail.navigationView";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		layout.addStandaloneView(NAVIGATIONVIEW_ID, false, IPageLayout.LEFT,
				0.25f, editorArea);
		IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP,
				0.5f, editorArea);
		folder.addPlaceholder(View.ID + ":*");
		folder.addView(View.ID);

		layout.getViewLayout(NAVIGATIONVIEW_ID).setCloseable(false);
	}
}
