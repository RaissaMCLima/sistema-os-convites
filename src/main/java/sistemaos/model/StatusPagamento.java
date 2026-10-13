package sistemaos.model;

public enum StatusPagamento {
    PENDENTE("Pendente"),
    SINAL_50("Parcial"),
    TOTAL("Pago");

    private final String rotulo;

    StatusPagamento(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }
}