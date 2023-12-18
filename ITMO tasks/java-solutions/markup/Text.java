package markup;

public class Text implements IMarkup {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        stringBuilder.append(text);
    }

    @Override
    public void toHtml(StringBuilder stringBuilder) {
        toMarkdown(stringBuilder);
    }
}
