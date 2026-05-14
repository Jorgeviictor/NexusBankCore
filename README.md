# NexusBank Core

Sistema de backend bancário simplificado desenvolvido em **Java 17**, criado como projeto de portfólio para demonstrar maturidade técnica em orientação a objetos, validação de regras de negócio e rastreabilidade de dados financeiros.

---

## Funcionalidades

| Operação              | Descrição                                                              |
|-----------------------|------------------------------------------------------------------------|
| **Depósito**          | Crédito validado (não aceita valores negativos ou zerados)             |
| **Saque**             | Débito com verificação de saldo suficiente                             |
| **Transferência**     | Operação atômica entre duas contas com registro em ambos os históricos |
| **Extrato formatado** | Listagem cronológica de todas as transações com tipo, descrição e valor|

---

## Estrutura do Projeto

```
src/
└── main/
    └── java/
        └── com/nexusbank/core/
            ├── TipoTransacao.java        # Enum com os 4 tipos de transação
            ├── Transacao.java            # Registro imutável de cada operação
            ├── ContaBancaria.java        # Entidade principal com regras de negócio
            └── NexusBankApplication.java # Main com simulação de fluxo real
```

---

## Conceitos Demonstrados

- **Encapsulamento rigoroso** — atributos `private final`, acesso apenas por métodos controlados
- **Validação de regras de negócio** — exceções claras e descritivas para cada violação
- **Imutabilidade** — objetos `Transacao` não podem ser alterados após a criação
- **Tipagem forte** — `enum TipoTransacao` no lugar de Strings livres
- **Rastreabilidade** — histórico completo e auditável de todas as operações
- **API `java.time`** — uso de `LocalDateTime` para timestamps precisos
- **JavaDoc** — documentação de todos os métodos e classes públicas

---

## Como Executar

**Pré-requisitos:** Java 17+ e Maven 3.8+

```bash
# Compilar
mvn compile

# Executar a simulação
mvn exec:java

# Gerar o JAR executável
mvn package
java -jar target/nexusbank-core-1.0.0.jar
```

---

## Saída Esperada

```
╔══════════════════════════════════════════╗
║        NexusBank Core — Simulação        ║
╚══════════════════════════════════════════╝

>> Abrindo contas...
>> Realizando operações...
  > Depósito de R$ 500,00 realizado na conta 001-1.
  > Saque de R$ 200,00 realizado na conta 001-1.
  > Transferência de R$ 700,00 de [Ana Lima] para [Carlos Souza] realizada com sucesso.
  ...

>> Testando validações...
  [BLOQUEADO] Saque acima do saldo: Saldo insuficiente. Saldo disponível: R$ ...

================================================================================
  EXTRATO BANCÁRIO — NexusBank Core
  Titular: Ana Lima                        | Conta: 001-1
================================================================================
  [dd/MM/yyyy HH:mm:ss] Depósito de abertura     | ...  | +R$ 1500,00
  ...
--------------------------------------------------------------------------------
  Saldo atual: R$ 1100,00
================================================================================
```

---

## Próximos Passos (Roadmap)

- [ ] Persistência com Spring Data JPA + H2/PostgreSQL
- [ ] Exposição via REST API com Spring Boot
- [ ] Autenticação e autorização com Spring Security + JWT
- [ ] Testes unitários com JUnit 5 e Mockito
- [ ] Containerização com Docker

---

## Princípios Aplicados

Este projeto aplica os princípios **SOLID** de forma prática:

- **S**ingle Responsibility — cada classe tem uma única responsabilidade
- **O**pen/Closed — novos tipos de transação são adicionados no enum sem alterar a lógica existente
- **L**iskov Substitution — estrutura preparada para herança segura
- **I**nterface Segregation — interfaces pequenas e coesas (próximo passo)
- **D**ependency Inversion — desacoplamento entre camadas (próximo passo)
