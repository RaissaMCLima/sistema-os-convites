package sistemaos.controller;

import sistemaos.model.FormaPagamento;
import sistemaos.model.OrdemServico;
import sistemaos.model.Pagamento;
import sistemaos.repository.OrdemServicoRepository;
import sistemaos.repository.PagamentoRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class PagamentoController {

    private final OrdemServicoRepository osRepository;
    private final PagamentoRepository pagamentoRepository;

    public PagamentoController(OrdemServicoRepository osRepository,
                               PagamentoRepository pagamentoRepository) {
        this.osRepository = osRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @PostMapping("/os/{id}/pagamentos")
    public String registrar(@PathVariable Long id,
                            @RequestParam BigDecimal valor,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                            @RequestParam FormaPagamento forma,
                            @RequestParam(required = false) String observacao) {
        OrdemServico os = osRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (valor.signum() > 0) {
            Pagamento pagamento = new Pagamento();
            pagamento.setOrdemServico(os);
            pagamento.setValor(valor);
            pagamento.setDataPagamento(data);
            pagamento.setForma(forma);
            pagamento.setObservacao(observacao);
            pagamentoRepository.save(pagamento);
        }
        return "redirect:/os/" + id;
    }

    @PostMapping("/pagamentos/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Long osId = pagamento.getOrdemServico().getId();
        pagamentoRepository.delete(pagamento);
        return "redirect:/os/" + osId;
    }
}