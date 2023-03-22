package markup;

import java.util.List;

public class AbstractList implements IList {
    protected List<ListItem> elements;

    public AbstractList(java.util.List<ListItem> elements) {
        this.elements = elements;
    }

    @Override
    public void toHtml(StringBuilder stringBuilder) {
        for (ListItem element : elements) {
            element.toHtml(stringBuilder);
        }
    }
}
