package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.models.Paciente;
import com.br.fasipe.menubase.biomedicina.models.Procedimento;
import com.br.fasipe.menubase.biomedicina.repository.PacienteRepository;
import com.br.fasipe.menubase.biomedicina.repository.ProcedimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dados")
@CrossOrigin(origins = "http://localhost:5500")
public class DadosAuxiliaresController {

    @Autowired
    private PacienteRepository pacienteRepo;

    @Autowired
    private ProcedimentoRepository procedRepo;

    @GetMapping("/pacientes")
    public ResponseEntity<List<Paciente>> listarPacientes() {
        // Retorna lista com ID e RG
        return ResponseEntity.ok(pacienteRepo.findAll());
    }

    @GetMapping("/procedimentos")
    public ResponseEntity<List<Procedimento>> listarProcedimentos() {
        // Retorna lista com ID e Descrição
        return ResponseEntity.ok(procedRepo.findAll());
    }
}