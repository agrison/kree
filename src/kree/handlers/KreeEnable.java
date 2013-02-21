package kree.handlers;

import kree.impl.KreeTracker;
import kree.util.StyledTextUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * KreeEnable.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class KreeEnable extends AbstractHandler {
    //final protected KreeIndentGuide kree;
    protected IWorkbenchWindow win;
    protected boolean showKree = false;
    protected KreeTracker kreeTracker;
    public static KreeEnable self;

    /**
     * The constructor.
     */
    public KreeEnable() {
        //this.kree = new KreeIndentGuide();
        self = this;
        System.err.println("Creating a KreeEnable instance --------------------------------------");
    }

    private String findWord(int offset, StyledText text) {
        int lineIndex = text.getLineAtOffset(offset);
        int offsetInLine = offset - text.getOffsetAtLine(lineIndex);
        String line = text.getLine(lineIndex);
        StringBuilder word = new StringBuilder();
        if (offsetInLine > 0 && offsetInLine < line.length()) {
            for (int i = offsetInLine; i >= 0; --i) {
                if (!Character.isSpaceChar(line.charAt(i))) {
                    word.append(line.charAt(i));
                } else {
                    break;
                }
            }
            word = word.reverse();
        }
        if (offsetInLine < line.length()) {
            for (int i = offsetInLine; i < line.length(); ++i) {
                if (i == offsetInLine)
                    continue; // duplicate
                if (!Character.isSpaceChar(line.charAt(i))) {
                    word.append(line.charAt(i));
                } else {
                    break;
                }
            }
        }
        return word.toString();
    }

    /**
     * the command has been executed, so extract extract the needed information
     * from the application context.
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        final Command command = event.getCommand();
        this.showKree = !HandlerUtil.toggleCommandState(command);
        //final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        org.eclipse.ui.IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        final StyledText text = StyledTextUtil.getStyledText(activeEditor);
        text.addCaretListener(new CaretListener() {
            public void caretMoved(CaretEvent event) {
                int offset = event.caretOffset;
                String word = findWord(offset, text);
                if (word.length() > 0) {
                    System.out.println("Word under caret: " + word);
                }
            }
        });
        if (text == null)
            return null;
        this.kreeTracker = new KreeTracker(PlatformUI.getWorkbench());
        //        this.kree.setText(text);
        //        this.kree.drawGuides(this.showKree);
        //        StyledTextUtil.addViewScrollListeners(new Listener() {
        //            int lastIndex = text.getTopIndex();
        //
        //            public void handleEvent(Event e) {
        //                int index = text.getTopIndex();
        //                if (index != this.lastIndex) {
        //                    this.lastIndex = index;
        //                    KreeEnable.this.kree.drawGuides(KreeEnable.this.showKree);
        //                }
        //            }
        //        }, text, this.showKree);
        //        text.addModifyListener(new ModifyListener() {
        //            public void modifyText(ModifyEvent arg0) {
        //                KreeEnable.this.kree.drawGuides(KreeEnable.this.showKree);
        //            }
        //        });
        return null;
    }

    public boolean showKree() {
        return this.showKree;
    }
}
