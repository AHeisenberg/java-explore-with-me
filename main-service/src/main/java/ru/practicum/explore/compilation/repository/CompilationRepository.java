package ru.practicum.explore.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore.compilation.model.Compilation;

import java.util.Collection;
import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("select c from Compilation c where c.pinned=?1 ORDER BY c.id")
    List<Compilation> findAllByPinnedOrderById(Boolean pinned, Pageable pageable);

    Page<Compilation> findAllByPinnedIsTrue(Pageable pageable);

    @Query("select c from Compilation as c where :pinned is null or c.pinned = :pinned")
    Collection<Compilation> findAll(@Param("pinned") Boolean pinned, PageRequest page);
}
