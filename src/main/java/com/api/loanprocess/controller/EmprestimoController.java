package com.api.loanprocess.controller;

import com.api.loanprocess.Dtos.EmprestimoDto;
import com.api.loanprocess.execoes.ExecoesEmprestimo;
import com.api.loanprocess.execoes.ExecoesTipoIdentificador;
import com.api.loanprocess.model.EmprestimoModel;
import com.api.loanprocess.model.PessoaModel;
import com.api.loanprocess.service.EmprestimoService;
import com.api.loanprocess.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/emprestimo")
public class EmprestimoController {

    final EmprestimoService emprestimoService;
    final PessoaService pessoaService;

    public EmprestimoController(EmprestimoService emprestimoService, PessoaService pessoaService){
        this.emprestimoService = emprestimoService;
        this.pessoaService = pessoaService;
    }

    @GetMapping()
    public ResponseEntity<List<EmprestimoModel>> consultaEmprestimos(){
        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.consultaEmprestimo());
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> realizarEmprestimo(@PathVariable(value = "id") UUID id, @RequestBody @Valid EmprestimoDto emprestimoDto) {
        var emprestimoModel = new EmprestimoModel();

        Optional<PessoaModel> pessoaModelOptional = pessoaService.buscaPorId(id);
        if(!pessoaModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id Inexistente");
        }

        emprestimoModel.setValor_Emprestimo(emprestimoDto.getValor_Emprestimo());
        emprestimoModel.setNumero_Parcelas(emprestimoDto.getNumero_Parcelas());
        emprestimoModel.setId_Pessoa(pessoaModelOptional.get());
        emprestimoModel.setData_Criacao(LocalDateTime.now(ZoneId.of("UTC")));
        BeanUtils.copyProperties(emprestimoDto, emprestimoModel);

        String tipoIdentificador = pessoaModelOptional.get().getIdentificador().toString();
        Integer quantiadeCaracteres = tipoIdentificador.length();

        try {
            if(quantiadeCaracteres == 11 || quantiadeCaracteres == 14){
                return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.validaEmprestimoCNPJCPF(id,emprestimoModel));
            }else if( quantiadeCaracteres == 8 ){
                return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.validaEmprestimoEU(id,emprestimoModel));
            }else if (quantiadeCaracteres == 10 ) {
                return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.validaEmprestimoAP(id,emprestimoModel));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não encontrado");
        }catch (ExecoesEmprestimo e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> realizaPagamento(@PathVariable(value = "id") UUID id) {
        var emprestimoModel = new EmprestimoModel();

        Optional<EmprestimoModel> emprestimoModelOptional = emprestimoService.buscaPorId(id);
        if(!emprestimoModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id Inexistente");
        }


        EmprestimoModel emprestimoModel1 = emprestimoModelOptional.get();

        //BeanUtils.copyProperties(emprestimoModelOptional,emprestimoModel);


        emprestimoModel1.setStatus_Pagamento("Pago");
//        emprestimoModel.setId(emprestimoModelOptional.get().getId());
//        emprestimoModel.setValor_Emprestimo(emprestimoModelOptional.get().getValor_Emprestimo());
//        emprestimoModel.setNumero_Parcelas((emprestimoModelOptional.get().getNumero_Parcelas()));
//        emprestimoModel.setId_Pessoa(emprestimoModelOptional.get().getId_Pessoa());
//        emprestimoModel.setData_Criacao(emprestimoModelOptional.get().getData_Criacao());

        return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.salvarPagamento(emprestimoModel1));
    }
}
