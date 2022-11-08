package com.io.github.msj.msinscricao.controller;

import com.io.github.msj.msinscricao.dto.request.InscricaoRequestDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoFinalizadaResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoMensagemResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoResponseDTO;
import com.io.github.msj.msinscricao.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inscricoes")
public class InscricaoController {

    @Autowired
    private InscricaoService inscricaoService;

    @PostMapping("/salvar")
    public ResponseEntity<InscricaoMensagemResponseDTO> salvar(@RequestBody InscricaoRequestDTO inscricaoRequestDTO) {
        InscricaoMensagemResponseDTO inscricaoMensagemResponseDTO = inscricaoService.salvar(inscricaoRequestDTO);
        return ResponseEntity.ok().body(inscricaoMensagemResponseDTO);
    }

    @PostMapping("/finalizar")
    public ResponseEntity<InscricaoMensagemResponseDTO> finalizar(@RequestBody Integer idCurso) {
        InscricaoMensagemResponseDTO inscricaoMensagemResponseDTO = inscricaoService.finalizar(idCurso);
        return ResponseEntity.ok().body(inscricaoMensagemResponseDTO);
    }

    @GetMapping("/inscritos/{idCurso}")
    public ResponseEntity<List<InscricaoResponseDTO>> listarPorIdCurso(@PathVariable Integer idCurso) {
        List<InscricaoResponseDTO> responseDTOS = inscricaoService.listarPorIdCurso(idCurso);
        return ResponseEntity.ok().body(responseDTOS);
    }

    @GetMapping("/inscritos-finalizados/{idCurso}")
    public ResponseEntity<List<InscricaoFinalizadaResponseDTO>> listarInscritosFinalizados(@PathVariable Integer idCurso) {
        List<InscricaoFinalizadaResponseDTO> responseDTOS = inscricaoService.inscritosFinalizados(idCurso);
        return ResponseEntity.ok().body(responseDTOS);
    }

}
