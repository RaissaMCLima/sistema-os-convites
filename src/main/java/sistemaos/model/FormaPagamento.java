package sistemaos.model;

public enum FormaPagamento {
    PIX("Pix"),
    CARTAO("Cartão"),
    DINHEIRO("Dinheiro"),
    OUTRO("Outro");

    private final String rotulo;

    FormaPagamento(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }
}