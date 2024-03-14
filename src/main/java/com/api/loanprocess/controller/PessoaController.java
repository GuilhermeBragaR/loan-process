package com.api.loanprocess.controller;

import com.api.loanprocess.Dtos.PessoaDto;
import com.api.loanprocess.execoes.ExecoesTipoIdentificador;
import com.api.loanprocess.model.PessoaModel;
import com.api.loanprocess.enums.TipoIdentificador;
import com.api.loanprocess.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            Integer quantidadeCaracteres = identificador.length();

        try {
            TipoIdentificador tipoIdentificador = TipoIdentificador.buscaPorIdentificador(quantidadeCaracteres);

            pessoaModel.setTipoIdentificador(tipoIdentificador.name());
            pessoaModel.setValorMaximoEmprestimo(tipoIdentificador.getValorMaximoEmprestimo());
            pessoaModel.setValorMinimoMensal(tipoIdentificador.getValorMinimoMensal());

            BeanUtils.copyProperties(pessoaDto, pessoaModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.cadastroPessoa(pessoaModel));
        }catch (ExecoesTipoIdentificador e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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

        String dataNascimentoString = pessoaDto.getDataNascimento().toString();
        LocalDate dataNascimento = LocalDate.parse(dataNascimentoString);


//        DateTimeFormatter dataFormatada = DateTimeFormatter.ofPattern("yyyyMMdd");
//        LocalDate dataNascimento = LocalDate.parse(pessoaDto.getDataNascimento(),dataFormatada);
//
//        DateTimeFormatter dataFormatadaFinal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String dataNascimentoFinal = dataNascimento.format(dataFormatadaFinal);

        try  {
            TipoIdentificador tipoIdentificador = TipoIdentificador.buscaPorIdentificador(quantidadeCaracteres);
            pessoaModel.setTipoIdentificador(tipoIdentificador.name());
            pessoaModel.setValorMaximoEmprestimo(tipoIdentificador.getValorMaximoEmprestimo());
            pessoaModel.setValorMinimoMensal(tipoIdentificador.getValorMinimoMensal());
            pessoaModel.setDataNascimento(dataNascimento);
            return ResponseEntity.status(HttpStatus.OK).body(pessoaService.cadastroPessoa(pessoaModel));
        }catch (ExecoesTipoIdentificador e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


    }
}
