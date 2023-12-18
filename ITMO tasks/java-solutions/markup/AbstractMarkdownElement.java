package markup;

import java.util.List;

public abstract class AbstractMarkdownElement implements IMarkup {
    private final List<IMarkup> elements;

    protected AbstractMarkdownElement(List<IMarkup> elements) {
        this.elements = elements;
    }

    public void toMarkdown(StringBuilder stringBuilder) {
        for (var element : elements) {
            element.toMarkdown(stringBuilder);
        }
    }

    public void toHtml(StringBuilder stringBuilder) {
        for (var element : elements) {
            element.toHtml(stringBuilder);
        }
    }
}

