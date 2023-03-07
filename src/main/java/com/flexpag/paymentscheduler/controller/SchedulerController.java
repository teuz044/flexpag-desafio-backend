package com.flexpag.paymentscheduler.controller;

import com.flexpag.paymentscheduler.dto.SchedulerDtoPost;
import com.flexpag.paymentscheduler.handler.AgendamentoNaoEncontradoException;
import com.flexpag.paymentscheduler.handler.PagamentoRealizadoException;
import com.flexpag.paymentscheduler.model.SchedulerModel;
import com.flexpag.paymentscheduler.service.SchedulerService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/scheduler")
public class SchedulerController {
    private final SchedulerService schedulerService;

    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchedulerModel> listarAgendamentoPorId(@ApiParam("ID do agendamento") @PathVariable Integer id) {
        SchedulerModel agendamento = schedulerService.listarAgendamentoPorId(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(agendamento);
        }
    }

    @PostMapping("/agendar")
    public ResponseEntity<String> agendarPagamento(@ApiParam("Dados do agendamento") @RequestBody SchedulerDtoPost schedulerDTO) {
        SchedulerModel schedulerModel = schedulerDTO.toModel();
        Integer agendamentoId = schedulerService.agendarPagamento(schedulerModel);
        return ResponseEntity.status(HttpStatus.CREATED).body("O id do seu agendamento é: " + agendamentoId);
    }
    @PostMapping("/pagar/{id}")
    public ResponseEntity<String> pagarAgendamento(@ApiParam("ID do agendamento") @PathVariable Integer id) {
        try {
            boolean sucesso = schedulerService.pagarAgendamento(id);
            if (sucesso) {
                return ResponseEntity.ok("Pagamento efetuado com sucesso");
            }
        } catch (PagamentoRealizadoException ex) {
            return ResponseEntity.badRequest().body("Pagamento já realizado");
        } catch (AgendamentoNaoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/atualizar/{id}/datavencimento")
    public ResponseEntity<String> atualizarDataVencimento(@ApiParam("ID do agendamento") @PathVariable Integer id, @ApiParam("Nova data de vencimento") @RequestBody SchedulerModel schedulerModel) {
        try {
            schedulerService.atualizarDataVencimento(id, schedulerModel.getDataVencimento());
            return ResponseEntity.ok("Data de vencimento do agendamento atualizada com sucesso.");
        } catch (PagamentoRealizadoException ex) {
            return ResponseEntity.badRequest().body("O agendamento já foi pago.");
        } catch (AgendamentoNaoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarAgendamento(@ApiParam("ID do agendamento") @PathVariable Integer id) {
        try {
            schedulerService.deletarAgendamento(id);
            return ResponseEntity.ok("Agendamento excluido");
        } catch (PagamentoRealizadoException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Pagamento já realizado, exclusão não permitida.");
        } catch (AgendamentoNaoEncontradoException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}