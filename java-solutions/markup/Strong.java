package markup;

import java.util.List;

public class Strong extends AbstractMarkdownElement {
    public Strong(List<IMarkup> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        stringBuilder.append("__");
        super.toHtml(stringBuilder);
        stringBuilder.append("__");
    }

    @Override
    public void toHtml(StringBuilder stringBuilder) {
        stringBuilder.append("<strong>");
        super.toHtml(stringBuilder);
        stringBuilder.append("</strong>");
    }
}
