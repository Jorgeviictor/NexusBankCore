package com.nexusbank.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa uma transação financeira registrada em uma conta bancária.
 *
 * <p>Objetos desta classe são imutáveis após a criação, garantindo a
 * integridade do histórico de operações. Cada transação é um registro
 * auditável e rastreável.</p>
 *
 * @author NexusBank Core
 * @version 1.0
 */
public class Transacao {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final LocalDateTime dataHora;
    private final TipoTransacao tipo;
    private final double valor;
    private final String descricao;

    /**
     * Cria uma nova transação com os dados fornecidos.
     *
     * @param tipo      tipo da transação (não pode ser nulo)
     * @param valor     valor monetário envolvido na operação (deve ser positivo)
     * @param descricao texto descritivo complementar da transação
     * @throws IllegalArgumentException se tipo for nulo ou valor não positivo
     */
    public Transacao(TipoTransacao tipo, double valor, String descricao) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo da transação não pode ser nulo.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser positivo.");
        }
        this.dataHora = LocalDateTime.now();
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao != null ? descricao : "";
    }

    /**
     * Retorna a data e hora em que a transação foi registrada.
     *
     * @return data e hora da transação
     */
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    /**
     * Retorna o tipo classificado desta transação.
     *
     * @return tipo da transação
     */
    public TipoTransacao getTipo() {
        return tipo;
    }

    /**
     * Retorna o valor monetário envolvido nesta transação.
     *
     * @return valor da transação
     */
    public double getValor() {
        return valor;
    }

    /**
     * Retorna a descrição complementar desta transação.
     *
     * @return texto descritivo da transação
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna uma representação formatada da transação para uso em extratos.
     *
     * @return string formatada com data, tipo, descrição e valor
     */
    @Override
    public String toString() {
        String sinal = (tipo == TipoTransacao.DEPOSITO || tipo == TipoTransacao.TRANSFERENCIA_RECEBIDA)
                ? "+"
                : "-";
        return String.format("  [%s] %-28s | %s | %sR$ %.2f",
                dataHora.format(FORMATTER),
                tipo.getDescricao(),
                descricao,
                sinal,
                valor);
    }
}
