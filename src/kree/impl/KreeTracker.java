package kree.impl;

import java.util.HashMap;
import java.util.Map;

import kree.handlers.KreeEnable;
import kree.util.StyledTextUtil;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.EditorReference;

public class KreeTracker {
    private final IWorkbench workbench;
    private final IWindowListener windowListener = new IWindowListener() {
        public void windowOpened(IWorkbenchWindow window) {
            window.getPartService().addPartListener(KreeTracker.this.partListener);
        }

        public void windowClosed(IWorkbenchWindow window) {
            window.getPartService().removePartListener(KreeTracker.this.partListener);
        }

        public void windowActivated(IWorkbenchWindow window) {
        }

        public void windowDeactivated(IWorkbenchWindow window) {
        }
    };
    private final IPartListener2 partListener = new IPartListener2() {
        public void partOpened(IWorkbenchPartReference partref) {
            annotateEditor(partref);
        }

        public void partActivated(IWorkbenchPartReference partref) {
        }

        public void partBroughtToTop(IWorkbenchPartReference partref) {
        }

        public void partVisible(IWorkbenchPartReference partref) {
        }

        public void partInputChanged(IWorkbenchPartReference partref) {
        }

        public void partClosed(IWorkbenchPartReference partref) {
            annotateEditor(partref);
        }

        public void partDeactivated(IWorkbenchPartReference partref) {
        }

        public void partHidden(IWorkbenchPartReference partref) {
        }
    };
    private final Map<StyledText, KreeIndentGuide> guides;

    public KreeTracker(IWorkbench workbench) {
        this.workbench = workbench;
        this.guides = new HashMap<StyledText, KreeIndentGuide>();
        workbench.getActiveWorkbenchWindow().getPages();
        IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
        for (int i = 0; i < windows.length; i++) {
            windows[i].getPartService().addPartListener(this.partListener);
        }
        workbench.addWindowListener(this.windowListener);
        annotateAllEditors(KreeEnable.self.showKree());
    }

    public void dispose() {
        this.workbench.removeWindowListener(this.windowListener);
        IWorkbenchWindow[] windows = this.workbench.getWorkbenchWindows();
        for (int i = 0; i < windows.length; i++) {
            windows[i].getPartService().removePartListener(this.partListener);
        }
    }

    private void annotateAllEditors(boolean show) {
        IWorkbenchWindow[] windows = this.workbench.getWorkbenchWindows();
        for (int i = 0; i < windows.length; i++) {
            IWorkbenchPage[] pages = windows[i].getPages();
            for (int j = 0; j < pages.length; j++) {
                IEditorReference[] editors = pages[j].getEditorReferences();
                for (int k = 0; k < editors.length; k++) {
                    annotateEditor(editors[k]);
                }
            }
        }
    }

    private void annotateEditor(final IWorkbenchPartReference partref) {
        IWorkbenchPart part = partref.getPart(false);
        if (partref instanceof EditorReference) {
            System.out.println("Annotating editor: " + partref.getPartName());
            final StyledText text = StyledTextUtil.getStyledText(((EditorReference) partref).getEditor(false));
            final KreeIndentGuide kree = new KreeIndentGuide(text);
            this.guides.put(text, kree);
            kree.drawGuides();
            if (KreeEnable.self.showKree()) {
                StyledTextUtil.addViewScrollListeners(new Listener() {
                    int lastIndex = text.getTopIndex();

                    public void handleEvent(Event e) {
                        int index = text.getTopIndex();
                        if (index != this.lastIndex) {
                            this.lastIndex = index;
                            kree.drawGuides();
                        }
                    }
                }, text);
                text.addModifyListener(new ModifyListener() {
                    public void modifyText(ModifyEvent arg0) {
                        kree.drawGuides();
                    }
                });
            } else {
            }
        }
    }
}