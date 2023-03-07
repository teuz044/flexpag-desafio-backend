package com.flexpag.paymentscheduler.handler;

public class PagamentoRealizadoException extends RuntimeException {
    public PagamentoRealizadoException(String message) {
    super("O agendamento já foi pago");
    }
    public PagamentoRealizadoException() {}
}