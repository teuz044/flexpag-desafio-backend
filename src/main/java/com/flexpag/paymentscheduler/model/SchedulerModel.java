package com.flexpag.paymentscheduler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flexpag.paymentscheduler.enums.StatusPagamento;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "scheduler")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Modelo para agendamento de pagamento")
public class SchedulerModel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    @JsonIgnore
    private Integer id;

    @NotNull
    @Column(name = "valorPagamento", nullable = false)
    private Long valorPagamento;

    @NotBlank
    @Column(name = "dataVencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "dataAgendamento", nullable = false)
    private LocalDate dataAgendamento = LocalDate.now();

    @Column(name = "horaAgendamento", nullable = false)
    private LocalTime horaAgendamento = LocalTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPagamento status = StatusPagamento.PENDING;

    @NotBlank
    @Column(name = "beneficiario", nullable = false)
    private String beneficiario;

    public SchedulerModel() {
    }

}