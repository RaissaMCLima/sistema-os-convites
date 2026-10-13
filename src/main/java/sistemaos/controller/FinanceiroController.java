package sistemaos.controller;

import sistemaos.model.CategoriaDespesa;
import sistemaos.model.OrdemServico;
import sistemaos.repository.DespesaRepository;
import sistemaos.repository.OrdemServicoRepository;
import sistemaos.repository.PagamentoRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Controller
public class FinanceiroController {

    private final PagamentoRepository pagamentoRepository;
    private final DespesaRepository despesaRepository;
    private final OrdemServicoRepository osRepository;

    public FinanceiroController(PagamentoRepository pagamentoRepository,
                                DespesaRepository despesaRepository,
                                OrdemServicoRepository osRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.despesaRepository = despesaRepository;
        this.osRepository = osRepository;
    }

    @GetMapping("/financeiro")
    public String resumo(@RequestParam(required = false)
                         @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes,
                         Model model) {
        YearMonth atual = (mes != null) ? mes : YearMonth.now();
        LocalDate inicio = atual.atDay(1);
        LocalDate fim = atual.atEndOfMonth();
        LocalDateTime inicioHora = inicio.atStartOfDay();
        LocalDateTime fimHora = fim.atTime(LocalTime.MAX);

        BigDecimal entradas = nz(pagamentoRepository.somaEntre(inicio, fim));
        BigDecimal saidas = nz(despesaRepository.somaEntre(inicio, fim));
        BigDecimal saldo = entradas.subtract(saidas);

        YearMonth anterior = atual.minusMonths(1);
        BigDecimal saldoAnterior = nz(pagamentoRepository.somaEntre(anterior.atDay(1), anterior.atEndOfMonth()))
                .subtract(nz(despesaRepository.somaEntre(anterior.atDay(1), anterior.atEndOfMonth())));

        BigDecimal faturado = nz(osRepository.somaValorEntre(inicioHora, fimHora));
        long pedidos = osRepository.countByDataEntradaBetween(inicioHora, fimHora);
        BigDecimal ticketMedio = pedidos > 0
                ? faturado.divide(BigDecimal.valueOf(pedidos), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal aReceber = osRepository.findAll().stream()
                .map(OrdemServico::getSaldoAReceber)
                .filter(v -> v.signum() > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal maior = entradas.max(saidas);
        int percEntradas = percentual(entradas, maior);
        int percSaidas = percentual(saidas, maior);

        Map<String, BigDecimal> categorias = new LinkedHashMap<>();
        Map<String, Integer> percentuais = new LinkedHashMap<>();
        despesaRepository.somaPorCategoria(inicio, fim).stream()
                .sorted((a, b) -> ((BigDecimal) b[1]).compareTo((BigDecimal) a[1]))
                .forEach(linha -> {
                    CategoriaDespesa categoria = (CategoriaDespesa) linha[0];
                    BigDecimal total = (BigDecimal) linha[1];
                    categorias.put(categoria.getRotulo(), total);
                    percentuais.put(categoria.getRotulo(), percentual(total, saidas));
                });

        String nome = atual.getMonth().getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"));
        nome = Character.toUpperCase(nome.charAt(0)) + nome.substring(1) + " de " + atual.getYear();

        model.addAttribute("nomeMes", nome);
        model.addAttribute("mesAnterior", anterior.toString());
        model.addAttribute("proximoMes", atual.plusMonths(1).toString());
        model.addAttribute("entradas", entradas);
        model.addAttribute("saidas", saidas);
        model.addAttribute("saldo", saldo);
        model.addAttribute("saldoAnterior", saldoAnterior);
        model.addAttribute("aReceber", aReceber);
        model.addAttribute("faturado", faturado);
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("ticketMedio", ticketMedio);
        model.addAttribute("percEntradas", percEntradas);
        model.addAttribute("percSaidas", percSaidas);
        model.addAttribute("categorias", categorias);
        model.addAttribute("percentuais", percentuais);
        model.addAttribute("pagamentos",
                pagamentoRepository.findByDataPagamentoBetweenOrderByDataPagamentoDescIdDesc(inicio, fim));
        return "financeiro/resumo";
    }

    private BigDecimal nz(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }

    private int percentual(BigDecimal parte, BigDecimal total) {
        if (total.signum() <= 0) {
            return 0;
        }
        return parte.multiply(BigDecimal.valueOf(100))
                .divide(total, 0, RoundingMode.HALF_UP)
                .intValue();
    }
}