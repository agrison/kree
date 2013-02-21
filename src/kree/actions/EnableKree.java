package kree.actions;

import kree.Kree;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class EnableKree extends AbstractHandler implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow win;
    //    private KreeProviderRegistry registry;
	
    public EnableKree() {
    }

    public void init(IWorkbenchWindow window) {
        this.win = window;
    }

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        MessageDialog.openInformation(window.getShell(), "Kree", "Jaffa kree !");
        return null;
    }

    public void run(IAction action) {
        boolean checked = false;
        runCommand(checked);
        action.setChecked(checked);
    }

    public void selectionChanged(IAction iaction, ISelection iselection) {
    }

    private void runCommand(boolean isChecked) {
        if (this.win == null)
            this.win = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        //        if (!isChecked) {
        //            releaseDecorators();
        //        } else {
        //            getRegistry().setPartListener(this.win.getPartService(), new BoxDecoratorPartListener());
        //            IWorkbenchPart part = this.win.getActivePage().getActiveEditor();
        //            if (part != null)
        //                setVisible(part, true);
        //        }
        Kree.getDefault().setEnabled(isChecked);
    }
    //    @Override
    //    public void dispose() {
    //        releaseDecorators();
    //    }
    //
    //    private void releaseDecorators() {
    //        if (this.win != null)
    //            getRegistry().removePartListener(this.win.getPartService());
    //        getRegistry().releaseDecorators();
    //    }
    //
    //    protected KreeProviderRegistry getRegistry() {
    //        if (this.registry == null)
    //            this.registry = Kree.getDefault().getProviderRegistry();
    //        return this.registry;
    //    }
    //    protected void setVisible(IWorkbenchPartReference partRef, boolean visible) {
    //        IWorkbenchPart part = partRef.getPart(false);
    //        if (part != null)
    //            setVisible(part, visible);
    //    }
    //
    //    protected void setVisible(IWorkbenchPart part, boolean visible) {
    //        IBoxDecorator decorator = getRegistry().getDecorator(part);
    //        if (decorator == null && !visible)
    //            return;
    //        if (decorator == null)
    //            decorator = decorate(part);
    //        if (decorator != null)
    //            decorator.enableUpdates(visible);
    //    }
    //
    //    protected IBoxDecorator decorate(IWorkbenchPart part) {
    //        IBoxDecorator result = null;
    //        IBoxProvider provider = getRegistry().getBoxProvider(part);
    //        if (provider != null)
    //            result = provider.decorate(part);
    //        if (result != null)
    //            getRegistry().addDecorator(result, part);
    //        return result;
    //    }
    //
    //    protected void undecorate(IWorkbenchPartReference partRef) {
    //        IWorkbenchPart part = partRef.getPart(false);
    //        if (part != null) {
    //            IBoxDecorator decorator = getRegistry().removeDecorator(part);
    //            if (decorator != null)
    //                decorator.getProvider().releaseDecorator(decorator);
    //        }
    //    }
}
