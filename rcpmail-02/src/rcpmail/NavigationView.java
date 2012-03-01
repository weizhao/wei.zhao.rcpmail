package rcpmail;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class NavigationView extends ViewPart {
	public static final String ID = "rcpmail.navigationView";
	private TreeViewer viewer;

	class TreeObject {
		private final String name;
		private TreeParent parent;

		public TreeObject(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setParent(TreeParent parent) {
			this.parent = parent;
		}

		public TreeParent getParent() {
			return parent;
		}

		@Override
		public String toString() {
			return getName();
		}
	}

	class TreeParent extends TreeObject {
		private final ArrayList children;

		public TreeParent(String name) {
			super(name);
			children = new ArrayList();
		}

		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}

		public TreeObject[] getChildren() {
			return (TreeObject[]) children.toArray(new TreeObject[children
					.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			return getChildren(parent);
		}

		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).hasChildren();
			}
			return false;
		}
	}

	class ViewLabelProvider extends LabelProvider {
		private Image createImage(String fileName) {
			return AbstractUIPlugin.imageDescriptorFromPlugin(
					Activator.PLUGIN_ID, "icons/silk/" + fileName)
					.createImage();
		}

		private final Image serverImage;
		private final Image folderImage;
		private final Image junkFolderImage;
		private final Image draftsFolderImage;
		private final Image sentFolderImage;
		private final Image trashFolderImage;

		public ViewLabelProvider() {
			serverImage = createImage("server.png");
			folderImage = createImage("folder.png");
			junkFolderImage = createImage("folder_bug.png");
			draftsFolderImage = createImage("folder_edit.png");
			sentFolderImage = createImage("folder_go.png");
			trashFolderImage = createImage("folder_delete.png");
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof TreeParent) {
				return serverImage;
			} else if (element instanceof TreeObject) {
				// Kai: Hack to get different images for different folders
				TreeObject treeObject = (TreeObject) element;
				if ("junk".equalsIgnoreCase(treeObject.getName())) {
					return junkFolderImage;
				} else if ("drafts".equalsIgnoreCase(treeObject.getName())) {
					return draftsFolderImage;
				} else if ("sent".equalsIgnoreCase(treeObject.getName())) {
					return sentFolderImage;
				} else if ("trash".equalsIgnoreCase(treeObject.getName())) {
					return trashFolderImage;
				}
				return folderImage;
			}
			return null;
		}

		@Override
		public String getText(Object obj) {
			return obj.toString();
		}
	}

	/**
	 * We will set up a dummy model to initialize tree heararchy. In real code,
	 * you will connect to a real model and expose its hierarchy.
	 */
	private TreeObject createDummyModel() {
		TreeObject to1 = new TreeObject("Inbox");
		TreeObject to2 = new TreeObject("Drafts");
		TreeObject to3 = new TreeObject("Sent");
		TreeParent p1 = new TreeParent("me@this.com");
		p1.addChild(to1);
		p1.addChild(to2);
		p1.addChild(to3);

		TreeObject to4 = new TreeObject("Inbox");
		TreeParent p2 = new TreeParent("other@aol.com");
		p2.addChild(to4);

		TreeParent root = new TreeParent("");
		root.addChild(p1);
		root.addChild(p2);
		return root;
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(createDummyModel());
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}