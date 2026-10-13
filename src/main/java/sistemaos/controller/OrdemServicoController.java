package sistemaos.controller;

import sistemaos.model.FormaPagamento;
import sistemaos.model.FormatoConvite;
import sistemaos.model.OrdemServico;
import sistemaos.model.StatusOS;
import sistemaos.repository.OrdemServicoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class OrdemServicoController {

    private final OrdemServicoRepository repository;

    public OrdemServicoController(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/os";
    }

    @GetMapping("/os")
    public String listar(Model model) {
        model.addAttribute("ordens",
                repository.findByStatusNotOrderByPrazoEntregaAsc(StatusOS.ENTREGUE));
        return "os/lista";
    }

    @GetMapping("/os/nova")
    public String nova(Model model) {
        model.addAttribute("os", new OrdemServico());
        carregarOpcoes(model);
        return "os/form";
    }

    @GetMapping("/os/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("os", buscar(id));
        carregarOpcoes(model);
        return "os/form";
    }

    @PostMapping("/os")
    public String salvar(@Valid @ModelAttribute("os") OrdemServico os,
                         BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            carregarOpcoes(model);
            return "os/form";
        }
        repository.save(os);
        return "redirect:/os/" + os.getId();
    }

    @GetMapping("/os/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        model.addAttribute("os", buscar(id));
        carregarOpcoes(model);
        return "os/detalhe";
    }

    @PostMapping("/os/{id}/status")
    public String mudarStatus(@PathVariable Long id, @RequestParam StatusOS status) {
        OrdemServico os = buscar(id);
        os.setStatus(status);
        repository.save(os);
        return "redirect:/os/" + id;
    }

    private OrdemServico buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void carregarOpcoes(Model model) {
        model.addAttribute("formatos", FormatoConvite.values());
        model.addAttribute("statuses", StatusOS.values());
        model.addAttribute("formas", FormaPagamento.values());
    }
}