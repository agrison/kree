package kree.util;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.JFaceTextUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IWorkbenchPart;

public class StyledTextUtil {
    public static class TextInfo {
        public static class LineInfo {
            public int line, level;
            public String text;
            public String indent;

            public LineInfo(int line, int level, String text, int tabSize) {
                this.line = line;
                this.level = level;
                this.text = text;
                StringBuilder b = new StringBuilder();
                for (int i = 0; i < level * tabSize; ++i)
                    b.append(" ");
                this.indent = b.toString();
            }
        }
        public int topIndex, bottomIndex, visibleLines;
        public List<LineInfo> lines;

        public TextInfo(int topIndex, int bottomIndex) {
            this.topIndex = topIndex;
            this.bottomIndex = bottomIndex;
            this.visibleLines = bottomIndex - topIndex;
        }

        public void sourceLines(StyledText text) {
            this.lines = new LinkedList<LineInfo>();
            int tabSize = text.getTabs();
            for (int i = this.topIndex; i < this.bottomIndex; ++i) {
                String line = text.getLine(i);
                this.lines.add(new LineInfo(i, computeLevel(line, tabSize), line, tabSize));
            }
        }

        public String getStruct() {
            StringBuilder b = new StringBuilder();
            for (LineInfo line : this.lines) {
                b.append(line.line).append("; ").append(line.level).append(": ").append(line.text).append("\n");
            }
            return b.toString();
        }

        public int computeLevel(String line, int tabSize) {
            int level = 0, spaces = 0;
            for (int i = 0; i < line.length(); ++i) {
                if (spaces == tabSize) {
                    ++level;
                    spaces = 0;
                }
                if (line.charAt(i) == '\t')
                    ++level;
                else if (line.charAt(i) == ' ')
                    ++spaces;
                else {
                    return level;
                }
            }
            return level;
        }
    }

    /**
     * Get information on text that is visible on the current text editor.
     * @param text the current StyledText
     * @return the information
     */
    public static TextInfo getVisibleTextInfo(StyledText text) {
        int bottomIndex = JFaceTextUtil.getPartialBottomIndex(text);
        int topIndex = JFaceTextUtil.getPartialTopIndex(text);
        TextInfo textInfo = new TextInfo(topIndex, bottomIndex);
        textInfo.sourceLines(text);
        return textInfo;
    }

    public static void addViewScrollListeners(final Listener listener, final StyledText text) {
        text.addListener(SWT.MouseDown, listener);
        text.addListener(SWT.MouseMove, listener);
        text.addListener(SWT.MouseUp, listener);
        text.addListener(SWT.KeyDown, listener);
        text.addListener(SWT.KeyUp, listener);
        text.addListener(SWT.Resize, listener);
        ScrollBar hBar = text.getHorizontalBar();
        if (hBar != null) {
            hBar.addListener(SWT.Selection, listener);
        }
        ScrollBar vBar = text.getVerticalBar();
        if (vBar != null) {
            vBar.addListener(SWT.Selection, listener);
        }
    }

    public static StyledText getStyledText(IWorkbenchPart editorPart) {
        if (editorPart != null) {
            Object obj = editorPart.getAdapter(org.eclipse.swt.widgets.Control.class);
            if (obj instanceof StyledText)
                return (StyledText) obj;
        }
        return null;
    }
}
