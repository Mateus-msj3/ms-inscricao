package com.io.github.msj.msinscricao.repository;

import com.io.github.msj.msinscricao.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    @Query("SELECT i FROM Inscricao i WHERE i.idCurso = ?1")
    List<Inscricao> buscarInscricoes (Integer idCurso);

    List<Inscricao> findByIdCurso (Integer idCurso);

}
