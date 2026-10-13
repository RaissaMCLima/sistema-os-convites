package sistemaos.repository;

import sistemaos.model.OrdemServico;
import sistemaos.model.StatusOS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    List<OrdemServico> findByStatusNotOrderByPrazoEntregaAsc(StatusOS status);

    long countByDataEntradaBetween(LocalDateTime inicio, LocalDateTime fim);

    @Query("select sum(o.valor) from OrdemServico o where o.dataEntrada between :inicio and :fim")
    BigDecimal somaValorEntre(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}