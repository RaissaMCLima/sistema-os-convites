package sistemaos.model;

public enum FormatoConvite {
    PDF("PDF"),
    PNG("PNG"),
    MP4("MP4"),
    OUTRO("Outro");

    private final String rotulo;

    FormatoConvite(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }
}
