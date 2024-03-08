package com.api.loanprocess.controller;

import com.api.loanprocess.Dtos.PessoaDto;
import com.api.loanprocess.model.PessoaModel;
import com.api.loanprocess.enums.TipoIdentificador;
import com.api.loanprocess.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping()
    @Operation(summary = "Cadastro de Pessoas")
    public ResponseEntity<Object> cadastroPessoa(@RequestBody @Valid PessoaDto pessoaDto) {
        var pessoaModel = new PessoaModel();
        String identificador = pessoaDto.getIdentificador().toString();
        int quantidadeCaracteres = identificador.length();

        TipoIdentificador tipoIdentificador = TipoIdentificador.buscaPorIdentificador(quantidadeCaracteres);

        pessoaModel.setTipoIdentificador(tipoIdentificador.name());
        pessoaModel.setValorMaximoEmprestimo(tipoIdentificador.getValorMaximoMensal());
        pessoaModel.setValorMinimoMensal(tipoIdentificador.getValorMinimoMensal());

        BeanUtils.copyProperties(pessoaDto, pessoaModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.cadastroPessoa(pessoaModel));
    }

    @GetMapping()
    public ResponseEntity<List<PessoaModel>> consultaPessoas() {
        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.consultaPessoa());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> consultaPessoa(@PathVariable(value = "id") UUID id) {
        Optional<PessoaModel> pessoaModelObject = pessoaService.buscaPorId(id);
        if(!pessoaModelObject.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id inexistente");
        }
        return ResponseEntity.status(HttpStatus.OK).body(pessoaModelObject.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletaPessoa(@PathVariable(value = "id") UUID id) {
        Optional<PessoaModel> pessoaModelObject = pessoaService.buscaPorId(id);
        if(!pessoaModelObject.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id inexistente");
        }
        pessoaService.deletaPessoa(pessoaModelObject.get());
        return ResponseEntity.status(HttpStatus.OK).body("Pessoa deletada com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizaPessoa(@PathVariable(value = "id") UUID id, @RequestBody @Valid PessoaDto pessoaDto){
        Optional<PessoaModel> pessoaModelOptional = pessoaService.buscaPorId(id);
        if (!pessoaModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id inexsistente");
        }

        var pessoaModel = new PessoaModel();
        BeanUtils.copyProperties(pessoaDto, pessoaModel);
        pessoaModel.setId(pessoaModelOptional.get().getId());

        String identificador = pessoaDto.getIdentificador().toString();
        int quantidadeCaracteres = identificador.length();

        TipoIdentificador tipoIdentificador = TipoIdentificador.buscaPorIdentificador(quantidadeCaracteres);

        pessoaModel.setTipoIdentificador(tipoIdentificador.name());
        pessoaModel.setValorMaximoEmprestimo(tipoIdentificador.getValorMaximoMensal());
        pessoaModel.setValorMinimoMensal(tipoIdentificador.getValorMinimoMensal());

        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.cadastroPessoa(pessoaModel));
    }
}
