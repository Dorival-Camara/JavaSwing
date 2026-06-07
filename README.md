# Sistema de Pedidos — Lanchonete

Sistema de pedidos para lanchonete desenvolvido em Java com interface gráfica JavaSwing, seguindo o padrão de arquitetura MVC.

---

## Tecnologias

- Java (JDK 11 ou superior)
- JavaSwing (interface gráfica)
- Padrão MVC

---

## Estrutura do Projeto

```
JavaSwing/
└── lanchonete/
    ├── Main.java
    ├── model/
    │   ├── ItemPedido.java
    │   └── Pedido.java
    ├── controller/
    │   └── PedidoController.java
    └── view/
        └── JanelaPrincipal.java
```

---

## Como Executar

1. Certifique-se de ter o JDK instalado
2. Compile todos os arquivos a partir da pasta raiz do projeto:

```bash
javac lanchonete/**/*.java lanchonete/Main.java
```

3. Execute:

```bash
java lanchonete.Main
```

---

## Funcionalidades

- Visualizar cardápio completo com preços por tamanho
- Montar pedido escolhendo item, tamanho e adicionais opcionais
- Adicionar observações por item
- Remover itens do pedido antes de finalizar
- Visualizar total do pedido em tempo real
- Finalizar pedido e ver resumo completo
- Iniciar novo pedido a partir da tela de resumo

---

## Cardápio

| Item                | Pequeno  | Médio    | Grande   |
|---------------------|----------|----------|----------|
| Hambúrguer Clássico | R$ 15,00 | R$ 22,00 | R$ 30,00 |
| X-Bacon             | R$ 18,00 | R$ 25,00 | R$ 33,00 |
| X-Salada            | R$ 16,00 | R$ 23,00 | R$ 31,00 |
| Batata Frita        | R$ 12,00 | R$ 18,00 | R$ 25,00 |
| Refrigerante        | R$ 8,00  | R$ 10,00 | R$ 12,00 |
| Suco Natural        | R$ 10,00 | R$ 14,00 | R$ 18,00 |
| Milkshake           | R$ 14,00 | R$ 18,00 | R$ 22,00 |

### Adicionais

| Adicional      | Valor   |
|----------------|---------|
| Queijo Extra   | R$ 3,00 |
| Bacon          | R$ 4,00 |
| Molho Especial | R$ 2,50 |
| Duplo          | R$ 8,00 |
| Sem Cebola     | Grátis  |

---

## Arquitetura MVC

O projeto segue o padrão **MVC (Model - View - Controller)**, que separa o código em três camadas com responsabilidades bem definidas.

| Camada     | Arquivo               | Responsabilidade                                    |
|------------|-----------------------|-----------------------------------------------------|
| Model      | ItemPedido.java       | Representa um produto: nome, tamanho, preço         |
| Model      | Pedido.java           | Representa o carrinho: lista de itens e total       |
| Controller | PedidoController.java | Gerencia a lógica: adicionar, remover, finalizar    |
| View       | JanelaPrincipal.java  | Interface gráfica: exibe dados e reage ao usuário   |

A regra principal do MVC: **a View nunca mexe nos dados diretamente**. Ela sempre pede ao Controller, que usa os Models para processar e guardar as informações.

---

## Fluxo de Uso

```
1. App abre → aba Resumo bloqueada, pedido vazio criado

2. Usuário vai em "Fazer Pedido"
   → escolhe item, tamanho, adicionais → clica "Adicionar ao Pedido"
   → item aparece na lista com o preço calculado

3. Usuário clica "Finalizar Pedido"
   → pedido é salvo, app vai automaticamente para aba "Resumo"
   → botão "Novo Pedido" aparece

4. Usuário clica "Novo Pedido"
   → pedido é limpo, app volta para "Fazer Pedido"
   → aba Resumo é bloqueada novamente
```

---

## Detalhes por Arquivo

### Main.java
Ponto de entrada do programa. Única responsabilidade: chamar `JanelaPrincipal.main()`.

### ItemPedido.java
Representa um produto adicionado ao pedido. Calcula o preço com base no item e tamanho escolhidos (`getValorBase()`) mais os adicionais selecionados (`getValorAdicionais()`). O método `toString()` formata o item para exibição na lista.

### Pedido.java
Representa o carrinho completo. Mantém uma lista de `ItemPedido` e calcula o total somando o valor de cada item. O método `toString()` formata o pedido completo para exibição no resumo.

### PedidoController.java
Gerencia dois estados: o `pedidoAtual` (sendo montado) e a lista `pedidos` (histórico dos finalizados). Ao finalizar, salva o pedido atual no histórico e cria um novo pedido vazio para o próximo atendimento.

### JanelaPrincipal.java
Janela principal que herda de `JFrame`. Contém três abas montadas por métodos privados (`criarPainelCardapio`, `criarPainelPedido`, `criarPainelResumo`). Os componentes são campos da classe para que os métodos de ação (`adicionarItem`, `removerItem`, `finalizarPedido`) e os métodos de atualização (`atualizarListaPedido`, `atualizarResumo`) possam acessá-los.
