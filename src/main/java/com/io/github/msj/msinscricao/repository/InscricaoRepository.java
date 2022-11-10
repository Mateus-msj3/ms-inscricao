package com.io.github.msj.msinscricao.repository;

import com.io.github.msj.msinscricao.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    List<Inscricao> findByIdCurso (Integer idCurso);

    boolean existsByCpf(String cpf);

}
