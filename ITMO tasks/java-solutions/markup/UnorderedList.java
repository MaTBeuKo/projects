package markup;

import java.util.List;

public class UnorderedList extends AbstractList {
    public UnorderedList(List<ListItem> list) {
        super(list);
    }

    public void toHtml(StringBuilder stringBuilder) {
        stringBuilder.append("<ul>");
        super.toHtml(stringBuilder);
        stringBuilder.append("</ul>");
    }
}
