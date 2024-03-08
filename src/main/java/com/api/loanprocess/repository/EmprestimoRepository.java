package com.api.loanprocess.repository;

import com.api.loanprocess.model.EmprestimoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface EmprestimoRepository extends JpaRepository<EmprestimoModel, UUID> {
}
