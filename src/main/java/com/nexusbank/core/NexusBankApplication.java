package com.nexusbank.core;

/**
 * Ponto de entrada da aplicação NexusBank Core.
 *
 * <p>Simula um fluxo bancário real entre duas contas, demonstrando as
 * regras de negócio, o encapsulamento e a rastreabilidade de transações
 * implementados no sistema.</p>
 *
 * @author NexusBank Core
 * @version 1.0
 */
public class NexusBankApplication {

    public static void main(String[] args) {

        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║        NexusBank Core — Simulação        ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        // --- Criação das contas -----------------------------------------------
        System.out.println(">> Abrindo contas...");
        ContaBancaria contaAna = new ContaBancaria("001-1", "Ana Lima", 1500.00);
        ContaBancaria contaCarlos = new ContaBancaria("002-2", "Carlos Souza", 300.00);
        System.out.printf("   Conta de %s aberta com saldo inicial de R$ %.2f%n", contaAna.getTitular(), contaAna.getSaldo());
        System.out.printf("   Conta de %s aberta com saldo inicial de R$ %.2f%n", contaCarlos.getTitular(), contaCarlos.getSaldo());

        // --- Operações bancárias ----------------------------------------------
        System.out.println();
        System.out.println(">> Realizando operações...");

        contaAna.depositar(500.00);
        contaAna.sacar(200.00);
        contaAna.transferir(700.00, contaCarlos);
        contaCarlos.depositar(150.00);
        contaCarlos.sacar(80.00);

        // --- Tentativa de operações inválidas ---------------------------------
        System.out.println();
        System.out.println(">> Testando validações de regras de negócio...");

        tentarOperacaoInvalida("Saque acima do saldo em [Carlos]",
                () -> contaCarlos.sacar(9999.00));

        tentarOperacaoInvalida("Depósito com valor negativo em [Ana]",
                () -> contaAna.depositar(-100.00));

        tentarOperacaoInvalida("Transferência para conta nula",
                () -> contaAna.transferir(100.00, null));

        // --- Impressão dos extratos -------------------------------------------
        System.out.println();
        System.out.println(">> Gerando extratos...");

        contaAna.imprimirExtrato();
        contaCarlos.imprimirExtrato();
    }

    /**
     * Executa uma operação que se espera lançar uma exceção e exibe a mensagem
     * de validação sem interromper o fluxo da simulação.
     *
     * @param descricao descrição do cenário de teste
     * @param operacao  lambda com a operação inválida a ser testada
     */
    private static void tentarOperacaoInvalida(String descricao, Runnable operacao) {
        try {
            operacao.run();
        } catch (IllegalArgumentException e) {
            System.out.printf("  [BLOQUEADO] %s: %s%n", descricao, e.getMessage());
        }
    }
}
