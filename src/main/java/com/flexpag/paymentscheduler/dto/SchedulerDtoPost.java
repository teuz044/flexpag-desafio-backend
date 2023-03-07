package com.flexpag.paymentscheduler.dto;

import com.flexpag.paymentscheduler.model.SchedulerModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class SchedulerDtoPost {
    @NotNull
    private Long valorPagamento;

    @NotBlank
    private String beneficiario;

    @NotBlank
    private LocalDate dataVencimento;

    public SchedulerDtoPost(Long valorPagamento, String beneficiario, LocalDate dataVencimento) {
        this.valorPagamento = valorPagamento;
        this.beneficiario = beneficiario;
        this.dataVencimento = dataVencimento;
    }

    public SchedulerModel toModel() {
        SchedulerModel model = new SchedulerModel();
        model.setValorPagamento(this.valorPagamento);
        model.setBeneficiario(this.beneficiario);
        model.setDataVencimento(this.dataVencimento);
        return model;
    }
}
