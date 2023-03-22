package markup;

import java.util.List;

public class Emphasis extends AbstractMarkdownElement {
    public Emphasis(List<IMarkup> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        stringBuilder.append("*");
        super.toMarkdown(stringBuilder);
        stringBuilder.append("*");
    }

    public void toHtml(StringBuilder stringBuilder) {
        stringBuilder.append("<em>");
        super.toHtml(stringBuilder);
        stringBuilder.append("</em>");
    }
}
