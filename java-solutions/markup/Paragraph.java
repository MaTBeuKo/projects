package markup;

import java.util.List;

public class Paragraph implements IList {
    private final List<IMarkup> elements;

    public Paragraph(List<IMarkup> elements) {
        this.elements = elements;
    }

    @Override
    public void toHtml(StringBuilder stringBuilder) {
        for (IMarkup element : elements) {
            element.toHtml(stringBuilder);
        }
    }
}
