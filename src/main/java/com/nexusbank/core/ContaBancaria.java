package com.nexusbank.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa uma conta bancária no sistema NexusBank.
 *
 * <p>Encapsula as regras de negócio para depósitos, saques e transferências,
 * garantindo que nenhuma operação ilícita altere o estado da conta. Mantém
 * um histórico imutável de todas as transações realizadas.</p>
 *
 * <p>Exemplo de uso:</p>
 * <pre>{@code
 * ContaBancaria conta = new ContaBancaria("001-1", "Ana Lima", 500.00);
 * conta.depositar(200.00);
 * conta.sacar(50.00);
 * conta.imprimirExtrato();
 * }</pre>
 *
 * @author NexusBank Core
 * @version 1.0
 */
public class ContaBancaria {

    private final String numeroConta;
    private final String titular;
    private double saldo;
    private final List<Transacao> historico;

    /**
     * Cria uma nova conta bancária com saldo inicial.
     *
     * @param numeroConta identificador único da conta (não pode ser nulo ou vazio)
     * @param titular     nome completo do titular (não pode ser nulo ou vazio)
     * @param saldoInicial saldo de abertura da conta (deve ser não-negativo)
     * @throws IllegalArgumentException se qualquer parâmetro violar as regras de negócio
     */
    public ContaBancaria(String numeroConta, String titular, double saldoInicial) {
        if (numeroConta == null || numeroConta.isBlank()) {
            throw new IllegalArgumentException("O número da conta não pode ser nulo ou vazio.");
        }
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException("O nome do titular não pode ser nulo ou vazio.");
        }
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }

        this.numeroConta = numeroConta;
        this.titular = titular;
        this.saldo = saldoInicial;
        this.historico = new ArrayList<>();

        if (saldoInicial > 0) {
            historico.add(new Transacao(
                    TipoTransacao.DEPOSITO,
                    saldoInicial,
                    "Depósito de abertura de conta"
            ));
        }
    }

    // -------------------------------------------------------------------------
    // Operações bancárias
    // -------------------------------------------------------------------------

    /**
     * Deposita um valor na conta corrente.
     *
     * @param valor montante a ser creditado (deve ser positivo)
     * @throws IllegalArgumentException se o valor for zero ou negativo
     */
    public void depositar(double valor) {
        validarValorPositivo(valor, "depósito");
        saldo += valor;
        historico.add(new Transacao(TipoTransacao.DEPOSITO, valor, "Depósito em conta"));
        System.out.printf("  > Depósito de R$ %.2f realizado na conta %s.%n", valor, numeroConta);
    }

    /**
     * Saca um valor da conta corrente.
     *
     * @param valor montante a ser debitado (deve ser positivo e não exceder o saldo)
     * @throws IllegalArgumentException se o valor for inválido ou o saldo for insuficiente
     */
    public void sacar(double valor) {
        validarValorPositivo(valor, "saque");
        validarSaldoSuficiente(valor);
        saldo -= valor;
        historico.add(new Transacao(TipoTransacao.SAQUE, valor, "Saque em conta"));
        System.out.printf("  > Saque de R$ %.2f realizado na conta %s.%n", valor, numeroConta);
    }

    /**
     * Transfere um valor desta conta para a conta de destino.
     *
     * <p>A operação é atômica: o débito e o crédito ocorrem juntos ou nenhum
     * dos dois é aplicado, preservando a consistência entre as contas.</p>
     *
     * @param valor   montante a ser transferido (deve ser positivo e não exceder o saldo)
     * @param destino conta bancária que receberá o crédito (não pode ser nula ou a própria conta)
     * @throws IllegalArgumentException se qualquer parâmetro violar as regras de negócio
     */
    public void transferir(double valor, ContaBancaria destino) {
        if (destino == null) {
            throw new IllegalArgumentException("A conta de destino não pode ser nula.");
        }
        if (destino == this) {
            throw new IllegalArgumentException("Não é permitido transferir para a própria conta.");
        }
        validarValorPositivo(valor, "transferência");
        validarSaldoSuficiente(valor);

        // Débito na conta de origem
        saldo -= valor;
        String descOrigem = String.format("Transferência para conta %s (%s)", destino.getNumeroConta(), destino.getTitular());
        historico.add(new Transacao(TipoTransacao.TRANSFERENCIA_ENVIADA, valor, descOrigem));

        // Crédito na conta de destino
        destino.creditarTransferencia(valor, this);

        System.out.printf("  > Transferência de R$ %.2f de [%s] para [%s] realizada com sucesso.%n",
                valor, titular, destino.getTitular());
    }

    // -------------------------------------------------------------------------
    // Extrato
    // -------------------------------------------------------------------------

    /**
     * Imprime no console o extrato completo e formatado da conta,
     * listando todas as transações em ordem cronológica.
     */
    public void imprimirExtrato() {
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.printf("  EXTRATO BANCÁRIO — NexusBank Core%n");
        System.out.printf("  Titular: %-30s | Conta: %s%n", titular, numeroConta);
        System.out.println("=".repeat(80));

        if (historico.isEmpty()) {
            System.out.println("  Nenhuma transação registrada.");
        } else {
            historico.forEach(System.out::println);
        }

        System.out.println("-".repeat(80));
        System.out.printf("  Saldo atual: R$ %.2f%n", saldo);
        System.out.println("=".repeat(80));
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Retorna o número identificador da conta.
     *
     * @return número da conta
     */
    public String getNumeroConta() {
        return numeroConta;
    }

    /**
     * Retorna o nome do titular da conta.
     *
     * @return nome do titular
     */
    public String getTitular() {
        return titular;
    }

    /**
     * Retorna o saldo atual da conta.
     *
     * @return saldo disponível
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     * Retorna uma visão não-modificável do histórico de transações.
     *
     * @return lista imutável de transações
     */
    public List<Transacao> getHistorico() {
        return Collections.unmodifiableList(historico);
    }

    // -------------------------------------------------------------------------
    // Métodos privados de apoio
    // -------------------------------------------------------------------------

    /**
     * Credita o valor recebido via transferência, registrando a transação
     * como TRANSFERENCIA_RECEBIDA. Método de acesso restrito ao pacote
     * para manter o encapsulamento do fluxo de transferência.
     *
     * @param valor  montante recebido
     * @param origem conta que originou a transferência
     */
    void creditarTransferencia(double valor, ContaBancaria origem) {
        saldo += valor;
        String descDestino = String.format("Transferência recebida de conta %s (%s)", origem.getNumeroConta(), origem.getTitular());
        historico.add(new Transacao(TipoTransacao.TRANSFERENCIA_RECEBIDA, valor, descDestino));
    }

    private void validarValorPositivo(double valor, String operacao) {
        if (valor <= 0) {
            throw new IllegalArgumentException(
                    String.format("O valor de %s deve ser positivo. Valor informado: R$ %.2f", operacao, valor));
        }
    }

    private void validarSaldoSuficiente(double valor) {
        if (valor > saldo) {
            throw new IllegalArgumentException(
                    String.format("Saldo insuficiente. Saldo disponível: R$ %.2f | Valor solicitado: R$ %.2f", saldo, valor));
        }
    }
}
