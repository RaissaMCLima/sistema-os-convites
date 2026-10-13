package sistemaos.controller;

import sistemaos.model.CategoriaDespesa;
import sistemaos.model.Despesa;
import sistemaos.model.FormaPagamento;
import sistemaos.repository.DespesaRepository;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Controller
public class DespesaController {

    private final DespesaRepository repository;

    public DespesaController(DespesaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/despesas")
    public String listar(@RequestParam(required = false)
                         @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes,
                         Model model) {
        YearMonth atual = (mes != null) ? mes : YearMonth.now();
        LocalDate inicio = atual.atDay(1);
        LocalDate fim = atual.atEndOfMonth();

        List<Despesa> despesas = repository
                .findByDataDespesaBetweenOrderByDataDespesaDescIdDesc(inicio, fim);
        BigDecimal total = despesas.stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String nome = atual.getMonth().getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"));
        nome = Character.toUpperCase(nome.charAt(0)) + nome.substring(1) + " de " + atual.getYear();

        model.addAttribute("despesas", despesas);
        model.addAttribute("total", total);
        model.addAttribute("nomeMes", nome);
        model.addAttribute("mesAnterior", atual.minusMonths(1).toString());
        model.addAttribute("proximoMes", atual.plusMonths(1).toString());
        return "despesas/lista";
    }

    @GetMapping("/despesas/nova")
    public String nova(Model model) {
        model.addAttribute("despesa", new Despesa());
        carregarOpcoes(model);
        return "despesas/form";
    }

    @GetMapping("/despesas/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Despesa despesa = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("despesa", despesa);
        carregarOpcoes(model);
        return "despesas/form";
    }

    @PostMapping("/despesas")
    public String salvar(@Valid @ModelAttribute("despesa") Despesa despesa,
                         BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            carregarOpcoes(model);
            return "despesas/form";
        }
        repository.save(despesa);
        return "redirect:/despesas?mes=" + YearMonth.from(despesa.getDataDespesa());
    }

    @PostMapping("/despesas/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/despesas";
    }

    private void carregarOpcoes(Model model) {
        model.addAttribute("categorias", CategoriaDespesa.values());
        model.addAttribute("formas", FormaPagamento.values());
    }
}