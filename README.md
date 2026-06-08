# Sistema de Pedidos — Lanchonete

Sistema de pedidos para lanchonete desenvolvido em Java com interface gráfica Java Swing, seguindo o padrão de arquitetura MVC.

## Tecnologias

- Java (JDK 11 ou superior)
- Java Swing (interface gráfica)
- Padrão MVC

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

## Funcionalidades

- Visualizar cardápio completo com preços por tamanho
- Montar pedido escolhendo item, tamanho e adicionais opcionais
- Adicionar observações por item
- Remover o último item adicionado
- Visualizar total do pedido em tempo real
- Finalizar pedido e ver resumo completo
- Iniciar novo pedido a partir da tela de resumo

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

**Adicionais:** Queijo Extra +R$ 3,00 | Bacon +R$ 4,00 | Molho Especial +R$ 2,50 | Duplo +R$ 8,00 | Sem Cebola: Grátis

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

## Arquitetura MVC

| Camada     | Arquivo               | Responsabilidade                                  |
|------------|-----------------------|---------------------------------------------------|
| Model      | ItemPedido.java       | Representa um produto: nome, tamanho, preço       |
| Model      | Pedido.java           | Representa o carrinho: lista de itens e total     |
| Controller | PedidoController.java | Gerencia a lógica: adicionar, remover, finalizar  |
| View       | JanelaPrincipal.java  | Interface gráfica: exibe dados e reage ao usuário |

A View nunca mexe nos dados diretamente — ela sempre chama o Controller, que usa os Models para processar e guardar as informações.
