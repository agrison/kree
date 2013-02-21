package kree.impl;

import java.util.ArrayList;
import java.util.List;

import kree.handlers.KreeEnable;
import kree.util.StyledTextUtil;
import kree.util.StyledTextUtil.TextInfo;
import kree.util.StyledTextUtil.TextInfo.LineInfo;

import org.eclipse.jface.text.JFaceTextUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

public class KreeIndentGuide {
    protected StyledText text;
    protected static List<Color> colors = new ArrayList<Color>();

    //    static {
    //        colors.add(new Color(null, 200, 200, 200));
    //        colors.add(new Color(null, 100, 100, 100));
    //        colors.add(new Color(null, 0, 0, 255));
    //        colors.add(new Color(null, 0, 255, 0));
    //        colors.add(new Color(null, 255, 0, 0));
    //    }
    public KreeIndentGuide() {
        this(null);
    }

    public KreeIndentGuide(StyledText text) {
        setText(text);
    }

    public void setText(StyledText text) {
        this.text = text;
    }

    public void drawGuides() {
        boolean show = KreeEnable.self.showKree();
        Rectangle r0 = this.text.getClientArea();
        if (r0.width < 1 || r0.height < 1)
            return;
        Image newImage = new Image(this.text.getDisplay(), r0.width, r0.height);
        GC gc = new GC(newImage);
        if (show) {
            TextInfo textInfo = StyledTextUtil.getVisibleTextInfo(this.text);
            int lineHeight = this.text.getLineHeight();
            Color gray = new Color(gc.getDevice(), 200, 200, 200);
            //Color red = new Color(gc.getDevice(), 255, 0, 0);
            gc.setLineStyle(SWT.LINE_DOT);
            gc.setForeground(gray);
            int charWidth = JFaceTextUtil.getAverageCharWidth(this.text);
            int previousLevel = -1;
            int shownLine = -1;
            for (LineInfo li : textInfo.lines) {
                ++shownLine;
                if (li.level > 1) {
                    //if (previousLevel != li.level) {
                    //    int x = (li.level - 1) * text.getTabs() * charWidth;
                    //    gc.drawText(String.valueOf(li.level), x, li.line * lineHeight);
                    //}
                    previousLevel = li.level;
                    for (int level = 2; level <= li.level; ++level) {
                        int x = (level - 1) * this.text.getTabs() * charWidth + 2;
                        //gc.setForeground(colors.get(level % colors.size()));
                        gc.drawLine(x, shownLine * lineHeight, x, (shownLine + 1) * lineHeight);
                    }
                } else if (li.level == 0 && previousLevel > 2) { // empty lines
                    for (int level = 2; level <= previousLevel; ++level) {
                        int x = (level - 1) * this.text.getTabs() * charWidth + 2;
                        //gc.setForeground(colors.get(level % colors.size()));
                        gc.drawLine(x, shownLine * lineHeight, x, (shownLine + 1) * lineHeight);
                    }
                }
            }
        }
        Image oldImage = this.text.getBackgroundImage();
        this.text.setBackgroundImage(newImage);
        if (oldImage != null)
            oldImage.dispose();
        gc.dispose();
    }
}
