package markup;

import java.util.List;

public class Strikeout extends AbstractMarkdownElement {
    public Strikeout(List<IMarkup> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        stringBuilder.append("~");
        super.toMarkdown(stringBuilder);
        stringBuilder.append("~");
    }

    @Override
    public void toHtml(StringBuilder stringBuilder) {
        stringBuilder.append("<s>");
        super.toHtml(stringBuilder);
        stringBuilder.append("</s>");
    }
}
