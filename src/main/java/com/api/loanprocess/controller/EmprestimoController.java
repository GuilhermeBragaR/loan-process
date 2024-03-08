package com.api.loanprocess.controller;

import com.api.loanprocess.Dtos.EmprestimoDto;
import com.api.loanprocess.model.EmprestimoModel;
import com.api.loanprocess.model.PessoaModel;
import com.api.loanprocess.service.EmprestimoService;
import com.api.loanprocess.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/emprestimo")
public class EmprestimoController {

    final EmprestimoService emprestimoService;
    final PessoaService pessoaService;

    public EmprestimoController(EmprestimoService emprestimoService, PessoaService pessoaService){
        this.emprestimoService = emprestimoService;
        this.pessoaService = pessoaService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> realizarEmprestimo(@PathVariable(value = "id") UUID id, @RequestBody @Valid EmprestimoDto emprestimoDto) {
        var emprestimoModel = new EmprestimoModel();

        Optional<PessoaModel> emprestimoModelOptional = pessoaService.buscaPorId(id);
        if(!emprestimoModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id Inexistente");
        }

        String identificador = emprestimoModelOptional.get().getTipoIdentificador();
        Integer quantidadeCaracteres = identificador.length();


        emprestimoModel.setValor_Emprestimo(emprestimoDto.getValor_Emprestimo());
        emprestimoModel.setNumero_Parcelas(emprestimoDto.getNumero_Parcelas());
        BeanUtils.copyProperties(emprestimoDto, emprestimoModel);


        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.validaEmprestiomCNPJCPF(id, emprestimoModel));
    }
}
