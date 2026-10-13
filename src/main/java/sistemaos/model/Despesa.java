package sistemaos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Informe a data")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataDespesa = LocalDate.now();

    @NotBlank(message = "Informe a descrição")
    private String descricao;

    @Enumerated(EnumType.STRING)
    private CategoriaDespesa categoria = CategoriaDespesa.OUTRAS;

    @NotNull(message = "Informe o valor")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private FormaPagamento forma = FormaPagamento.PIX;

    private boolean fixa;

    private String observacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDataDespesa() { return dataDespesa; }
    public void setDataDespesa(LocalDate dataDespesa) { this.dataDespesa = dataDespesa; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public CategoriaDespesa getCategoria() { return categoria; }
    public void setCategoria(CategoriaDespesa categoria) { this.categoria = categoria; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public FormaPagamento getForma() { return forma; }
    public void setForma(FormaPagamento forma) { this.forma = forma; }

    public boolean isFixa() { return fixa; }
    public void setFixa(boolean fixa) { this.fixa = fixa; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}