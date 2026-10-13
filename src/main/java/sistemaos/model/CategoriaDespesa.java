package sistemaos.model;

public enum CategoriaDespesa {
    ANUNCIOS("Anúncios"),
    CANVA_ASSINATURAS("Canva e assinaturas"),
    INTERNET("Internet"),
    TAXAS("Taxas"),
    IMPOSTOS("Impostos"),
    RETIRADAS("Retiradas"),
    OUTRAS("Outras");

    private final String rotulo;

    CategoriaDespesa(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }
}