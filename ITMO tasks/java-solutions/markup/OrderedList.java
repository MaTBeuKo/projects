package markup;

import java.util.List;

public class OrderedList extends AbstractList {
    public OrderedList(List<ListItem> elements) {
        super(elements);
    }

    public void toHtml(StringBuilder stringBuilder) {
        stringBuilder.append("<ol>");
        super.toHtml(stringBuilder);
        stringBuilder.append("</ol>");
    }
}
