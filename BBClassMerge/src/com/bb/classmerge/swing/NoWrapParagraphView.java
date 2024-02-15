package com.bb.classmerge.swing;

import javax.swing.text.Element;
import javax.swing.text.ParagraphView;

public class NoWrapParagraphView extends ParagraphView {

    public NoWrapParagraphView(Element elem) {
        super(elem);
    }

    @Override
    public void layout(int width, int height) {
        super.layout(Short.MAX_VALUE, height);
    }

    @Override
    public float getMinimumSpan(int axis) {
        return super.getPreferredSpan(axis);
    }
}