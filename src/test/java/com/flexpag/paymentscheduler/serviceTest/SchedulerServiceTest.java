package com.flexpag.paymentscheduler.serviceTest;

import com.flexpag.paymentscheduler.handler.AgendamentoNaoEncontradoException;
import com.flexpag.paymentscheduler.handler.PagamentoRealizadoException;
import com.flexpag.paymentscheduler.model.SchedulerModel;
import com.flexpag.paymentscheduler.enums.StatusPagamento;
import com.flexpag.paymentscheduler.repository.SchedulerRepository;
import com.flexpag.paymentscheduler.service.SchedulerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SchedulerServiceTest {

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Test
    public void testAgendarPagamento() {
        // cria um SchedulerModel para ser agendado
        SchedulerModel schedulerModel = new SchedulerModel();
        schedulerModel.setValorPagamento(1000L);
        schedulerModel.setDataVencimento(LocalDate.now().plusDays(7));
        schedulerModel.setBeneficiario("John Doe");

        // chama o método a ser testado
        Integer agendamentoId = schedulerService.agendarPagamento(schedulerModel);

        // verifica se o agendamento foi criado corretamente
        SchedulerModel agendamento = schedulerRepository.findById(agendamentoId).orElse(null);
        assertNotNull(agendamento);
        assertEquals(schedulerModel.getValorPagamento(), agendamento.getValorPagamento());
        assertEquals(schedulerModel.getDataVencimento(), agendamento.getDataVencimento());
        assertEquals(schedulerModel.getBeneficiario(), agendamento.getBeneficiario());
        assertEquals(StatusPagamento.PENDING, agendamento.getStatus());
    }

    @Test
    public void testListarAgendamentoPorId() {
        // cria um SchedulerModel para ser agendado e adiciona ao banco de dados
        SchedulerModel schedulerModel = new SchedulerModel();
        schedulerModel.setValorPagamento(2000L);
        schedulerModel.setDataVencimento(LocalDate.now().plusDays(14));
        schedulerModel.setBeneficiario("Jane Doe");
        SchedulerModel agendamentoSalvo = schedulerRepository.save(schedulerModel);

        // chama o método a ser testado
        SchedulerModel agendamento = schedulerService.listarAgendamentoPorId(agendamentoSalvo.getId());

        // verifica se o agendamento retornado está correto
        assertNotNull(agendamento);
        assertEquals(agendamentoSalvo.getId(), agendamento.getId());
        assertEquals(agendamentoSalvo.getValorPagamento(), agendamento.getValorPagamento());
        assertEquals(agendamentoSalvo.getDataVencimento(), agendamento.getDataVencimento());
        assertEquals(agendamentoSalvo.getBeneficiario(), agendamento.getBeneficiario());
        assertEquals(StatusPagamento.PENDING, agendamento.getStatus());
    }

    @Test
    public void testPagarAgendamento() {
        // cria um SchedulerModel para ser agendado e adiciona ao banco de dados
        SchedulerModel schedulerModel = new SchedulerModel();
        schedulerModel.setValorPagamento(3000L);
        schedulerModel.setDataVencimento(LocalDate.now().plusDays(21));
        schedulerModel.setBeneficiario("Jose Silva");
        SchedulerModel agendamentoSalvo = schedulerRepository.save(schedulerModel);

        // paga o agendamento
        schedulerService.pagarAgendamento(agendamentoSalvo.getId());

        // verifica se o status do agendamento mudou para "PAID"
        SchedulerModel agendamento = schedulerRepository.findById(agendamentoSalvo.getId()).orElse(null);
        assertNotNull(agendamento);
        assertEquals(StatusPagamento.PAID, agendamento.getStatus());
    }

    @Test
    public void testAtualizarDataVencimento() throws PagamentoRealizadoException, AgendamentoNaoEncontradoException {
        // cria um SchedulerModel para ser agendado e adiciona ao banco de dados
        SchedulerModel schedulerModel = new SchedulerModel();
        schedulerModel.setValorPagamento(4000L);
        schedulerModel.setDataVencimento(LocalDate.now().plusDays(28));
        schedulerModel.setBeneficiario("Maria Souza");
        SchedulerModel agendamentoSalvo = schedulerRepository.save(schedulerModel);

        // atualiza a data de vencimento do agendamento
        LocalDate novaDataVencimento = LocalDate.now().plusDays(35);
        schedulerService.atualizarDataVencimento(agendamentoSalvo.getId(), novaDataVencimento);

        // verifica se a data de vencimento do agendamento foi atualizada corretamente
        SchedulerModel agendamento = schedulerRepository.findById(agendamentoSalvo.getId()).orElse(null);
        assertNotNull(agendamento);
        assertEquals(novaDataVencimento, agendamento.getDataVencimento());
    }

    @Test
    public void testDeletarAgendamento() throws PagamentoRealizadoException, AgendamentoNaoEncontradoException {
        // cria um SchedulerModel para ser agendado e adiciona ao banco de dados
        SchedulerModel schedulerModel = new SchedulerModel();
        schedulerModel.setValorPagamento(5000L);
        schedulerModel.setDataVencimento(LocalDate.now().plusDays(42));
        schedulerModel.setBeneficiario("Joao Santos");
        SchedulerModel agendamentoSalvo = schedulerRepository.save(schedulerModel);

        // exclui o agendamento
        schedulerService.deletarAgendamento(agendamentoSalvo.getId());

        // verifica se o agendamento foi excluído
        assertThrows(AgendamentoNaoEncontradoException.class, () -> {
            schedulerRepository.findById(agendamentoSalvo.getId()).orElseThrow(() -> new AgendamentoNaoEncontradoException());
        });
    }
}