package markup;

import java.util.List;

public class ListItem implements IHtml {
    private final List<markup.IList> elements;

    public ListItem(List<markup.IList> elements) {
        this.elements = elements;
    }

    @Override
    public void toHtml(StringBuilder stringBuilder) {
        stringBuilder.append("<li>");
        for (markup.IList element : elements) {
            element.toHtml(stringBuilder);
        }
        stringBuilder.append("</li>");
    }
}
