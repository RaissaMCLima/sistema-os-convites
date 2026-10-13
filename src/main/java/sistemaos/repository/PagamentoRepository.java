package sistemaos.repository;

import sistemaos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByDataPagamentoBetweenOrderByDataPagamentoDescIdDesc(LocalDate inicio, LocalDate fim);

    @Query("select sum(p.valor) from Pagamento p where p.dataPagamento between :inicio and :fim")
    BigDecimal somaEntre(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}