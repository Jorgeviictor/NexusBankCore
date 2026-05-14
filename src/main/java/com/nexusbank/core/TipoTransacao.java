package com.nexusbank.core;

/**
 * Enum que representa os tipos de transação possíveis no sistema NexusBank.
 *
 * <p>A tipagem forte por meio de enum evita erros de categoria e garante
 * rastreabilidade precisa no extrato da conta.</p>
 *
 * @author NexusBank Core
 * @version 1.0
 */
public enum TipoTransacao {

    /** Entrada de dinheiro diretamente na conta. */
    DEPOSITO("Depósito"),

    /** Saída de dinheiro diretamente da conta. */
    SAQUE("Saque"),

    /** Transferência enviada para outra conta. */
    TRANSFERENCIA_ENVIADA("Transferência Enviada"),

    /** Transferência recebida de outra conta. */
    TRANSFERENCIA_RECEBIDA("Transferência Recebida");

    private final String descricao;

    TipoTransacao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição legível do tipo de transação.
     *
     * @return descrição em português do tipo
     */
    public String getDescricao() {
        return descricao;
    }
}
