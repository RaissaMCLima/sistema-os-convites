package sistemaos.model;

public enum StatusOS {
    NOVO("Novo"),
    EM_PRODUCAO("Em produção"),
    PRONTO("Pronto"),
    ENTREGUE("Entregue");

    private final String rotulo;

    StatusOS(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }
}