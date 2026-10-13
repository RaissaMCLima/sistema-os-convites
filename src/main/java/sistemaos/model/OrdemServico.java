package sistemaos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataEntrada = LocalDateTime.now();

    @NotNull(message = "Informe a data e o horário de entrega")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime prazoEntrega;

    @Enumerated(EnumType.STRING)
    private StatusOS status = StatusOS.NOVO;

    @NotBlank(message = "Informe o nome do cliente")
    private String clienteNome;

    private String clienteWhatsapp;

    private String clienteEmail;

    @NotBlank(message = "Informe o tema do convite")
    private String tema;

    @Enumerated(EnumType.STRING)
    private FormatoConvite formato = FormatoConvite.PDF;

    @Column(length = 3000)
    private String textoConvite;

    private String linkMaps;

    private String numeroConfirmacao;

    private String emailConfirmacao;

    @NotNull(message = "Informe o valor")
    private BigDecimal valor;

    @Column(length = 2000)
    private String observacoes;

    @OneToMany(mappedBy = "ordemServico")
    @OrderBy("dataPagamento ASC, id ASC")
    private List<Pagamento> pagamentos = new ArrayList<>();

    public boolean isAtrasada() {
        return LocalDateTime.now().isAfter(prazoEntrega);
    }

    // Fica amarelo quando faltam menos de 6 horas (troque o 6 se quiser)
    public boolean isUrgente() {
        return !isAtrasada() && Duration.between(LocalDateTime.now(), prazoEntrega).toHours() < 6;
    }

    public String getTempoRestante() {
        Duration d = Duration.between(LocalDateTime.now(), prazoEntrega);
        boolean atrasada = d.isNegative();
        d = d.abs();
        long dias = d.toDays();
        long horas = d.toHoursPart();
        long minutos = d.toMinutesPart();
        String texto = dias > 0 ? dias + "d " + horas + "h" : horas + "h " + minutos + "min";
        return atrasada ? "Atrasada " + texto : texto;
    }

    public BigDecimal getTotalPago() {
        return pagamentos.stream()
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getSaldoAReceber() {
        if (valor == null) {
            return BigDecimal.ZERO;
        }
        return valor.subtract(getTotalPago());
    }

    public StatusPagamento getStatusPagamento() {
        BigDecimal pago = getTotalPago();
        if (pago.signum() <= 0) {
            return StatusPagamento.PENDENTE;
        }
        if (valor != null && pago.compareTo(valor) >= 0) {
            return StatusPagamento.TOTAL;
        }
        return StatusPagamento.SINAL_50;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDateTime dataEntrada) { this.dataEntrada = dataEntrada; }

    public LocalDateTime getPrazoEntrega() { return prazoEntrega; }
    public void setPrazoEntrega(LocalDateTime prazoEntrega) { this.prazoEntrega = prazoEntrega; }

    public StatusOS getStatus() { return status; }
    public void setStatus(StatusOS status) { this.status = status; }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

    public String getClienteWhatsapp() { return clienteWhatsapp; }
    public void setClienteWhatsapp(String clienteWhatsapp) { this.clienteWhatsapp = clienteWhatsapp; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }

    public FormatoConvite getFormato() { return formato; }
    public void setFormato(FormatoConvite formato) { this.formato = formato; }

    public String getTextoConvite() { return textoConvite; }
    public void setTextoConvite(String textoConvite) { this.textoConvite = textoConvite; }

    public String getLinkMaps() { return linkMaps; }
    public void setLinkMaps(String linkMaps) { this.linkMaps = linkMaps; }

    public String getNumeroConfirmacao() { return numeroConfirmacao; }
    public void setNumeroConfirmacao(String numeroConfirmacao) { this.numeroConfirmacao = numeroConfirmacao; }

    public String getEmailConfirmacao() { return emailConfirmacao; }
    public void setEmailConfirmacao(String emailConfirmacao) { this.emailConfirmacao = emailConfirmacao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public List<Pagamento> getPagamentos() { return pagamentos; }
    public void setPagamentos(List<Pagamento> pagamentos) { this.pagamentos = pagamentos; }
}