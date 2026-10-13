package sistemaos.repository;

import sistemaos.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findByDataDespesaBetweenOrderByDataDespesaDescIdDesc(LocalDate inicio, LocalDate fim);

    @Query("select sum(d.valor) from Despesa d where d.dataDespesa between :inicio and :fim")
    BigDecimal somaEntre(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("select d.categoria, sum(d.valor) from Despesa d where d.dataDespesa between :inicio and :fim group by d.categoria")
    List<Object[]> somaPorCategoria(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}