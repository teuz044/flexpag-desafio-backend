package com.flexpag.paymentscheduler.service;

import com.flexpag.paymentscheduler.handler.AgendamentoNaoEncontradoException;
import com.flexpag.paymentscheduler.handler.PagamentoRealizadoException;
import com.flexpag.paymentscheduler.model.SchedulerModel;
import com.flexpag.paymentscheduler.enums.StatusPagamento;
import com.flexpag.paymentscheduler.repository.SchedulerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SchedulerService {
    @Autowired
    private SchedulerRepository repository;

    public SchedulerModel listarAgendamentoPorId (Integer id) {
        Optional<SchedulerModel> optionalAgendamento = repository.findById(id);
        if (optionalAgendamento.isPresent()) {
            return optionalAgendamento.get();
        } else {
            return null;
        }
    }

    public Integer agendarPagamento(SchedulerModel schedulerModel) {
        SchedulerModel newScheduler = repository.save(schedulerModel);
        newScheduler.setStatus(StatusPagamento.PENDING);
        return newScheduler.getId();
    }

    public boolean pagarAgendamento(Integer id) {
        SchedulerModel scheduler = repository.findById(id).orElse(null);
        if (scheduler == null) {
            throw new AgendamentoNaoEncontradoException();
        }
        if (scheduler.getStatus() == StatusPagamento.PAID) {
            throw new PagamentoRealizadoException();
        }
        scheduler.setStatus(StatusPagamento.PAID);
        repository.save(scheduler);
        return true;
    }
    public void atualizarDataVencimento(Integer id, LocalDate novaDataVencimento) throws PagamentoRealizadoException, AgendamentoNaoEncontradoException {
        Optional<SchedulerModel> schedulerOptional = repository.findById(id);
        if (schedulerOptional.isPresent()) {
            SchedulerModel scheduler = schedulerOptional.get();
            if (scheduler.getStatus() == StatusPagamento.PAID) {
                throw new PagamentoRealizadoException();
            } else {
                scheduler.setDataVencimento(novaDataVencimento);
                repository.save(scheduler);
            }
        } else {
            throw new AgendamentoNaoEncontradoException();
        }
    }

    public void deletarAgendamento(Integer id) throws PagamentoRealizadoException, AgendamentoNaoEncontradoException {
        Optional<SchedulerModel> schedulerOptional = repository.findById(id);

        if (schedulerOptional.isPresent()) {
            SchedulerModel scheduler = schedulerOptional.get();
            if (scheduler.getStatus() == StatusPagamento.PAID) {
                throw new PagamentoRealizadoException();
            } else if (scheduler.getStatus() == StatusPagamento.PENDING){
                repository.delete(scheduler);
            }
        } else {
            throw new AgendamentoNaoEncontradoException();
        }

    }
}
