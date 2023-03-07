package com.flexpag.paymentscheduler.handler;

public class AgendamentoNaoEncontradoException extends RuntimeException {
    public AgendamentoNaoEncontradoException(String message) {
        super("Agendamento não encontrado");
    }
    public AgendamentoNaoEncontradoException() {
    }
}


