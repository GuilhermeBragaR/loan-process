package com.api.loanprocess.service;

import com.api.loanprocess.model.PessoaModel;
import com.api.loanprocess.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PessoaService {

    final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public PessoaModel cadastroPessoa(PessoaModel pessoaModel) {
        return pessoaRepository.save(pessoaModel);
    }

    public List<PessoaModel> consultaPessoa() {
        return pessoaRepository.findAll();
    }

    public Optional<PessoaModel> buscaPorId(UUID id) {
        return pessoaRepository.findById(id);
    }

    @Transactional
    public void deletaPessoa(PessoaModel pessoaModel){
        pessoaRepository.delete(pessoaModel);
    }
}
