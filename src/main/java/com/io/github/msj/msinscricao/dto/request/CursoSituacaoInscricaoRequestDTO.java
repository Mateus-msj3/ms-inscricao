package com.io.github.msj.msinscricao.dto.request;

import com.io.github.msj.msinscricao.enums.SituacaoInscricaoCurso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CursoSituacaoInscricaoRequestDTO {

    private SituacaoInscricaoCurso situacaoInscricao;

}

