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

- Montar pedido escolhendo item, tamanho e adicionais
- Adicionar e remover itens do pedido
- Visualizar resumo do pedido com total em tempo real
- Finalizar pedido com confirmação
- Histórico de todos os pedidos finalizados na sessão
- Iniciar novo pedido a qualquer momento pelo menu

---

## Cardápio

| Item                | Pequeno   | Médio     | Grande    |
|---------------------|-----------|-----------|-----------|
| Hambúrguer Clássico | R$ 15,00  | R$ 22,00  | R$ 30,00  |
| X-Bacon             | R$ 18,00  | R$ 25,00  | R$ 33,00  |
| X-Salada            | R$ 16,00  | R$ 23,00  | R$ 31,00  |
| Batata Frita        | R$ 12,00  | R$ 18,00  | R$ 25,00  |
| Refrigerante        | R$ 8,00   | R$ 10,00  | R$ 12,00  |
| Suco Natural        | R$ 10,00  | R$ 14,00  | R$ 18,00  |
| Milkshake           | R$ 14,00  | R$ 18,00  | R$ 22,00  |

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

O projeto segue o padrão **MVC (Model - View - Controller)**, que separa o código em três camadas com responsabilidades bem definidas. Isso torna o sistema mais organizado, fácil de entender e fácil de expandir no futuro.

| Camada     | Arquivo               | Responsabilidade                         |
|------------|-----------------------|------------------------------------------|
| Model      | ItemPedido.java       | Representa um item individual do pedido  |
| Model      | Pedido.java           | Representa o pedido completo (carrinho)  |
| Controller | PedidoController.java | Gerencia a lógica e conecta Model e View |
| View       | JanelaPrincipal.java  | Interface gráfica com o usuário          |

A regra principal do MVC é: **a View nunca mexe nos dados diretamente**. Ela sempre pede ao Controller para fazer isso, e o Controller usa os Models para processar e guardar as informações.

---

## Como o Código Foi Feito

### Main.java — O Ponto de Entrada

Todo programa Java começa pelo método `main`. Aqui ele tem uma única responsabilidade: abrir a janela principal.

```java
public static void main(String[] args) {
    new JanelaPrincipal();
}
```

Quando `new JanelaPrincipal()` é chamado, o construtor da janela roda automaticamente e o sistema inteiro se inicializa a partir daí.

---

### ItemPedido.java — Como um Item é Representado

A classe `ItemPedido` representa **um único produto** dentro de um pedido. Cada vez que o usuário adiciona algo, um objeto desta classe é criado com os dados daquele produto.

Os campos são `private` para que só a própria classe possa acessá-los diretamente. O acesso externo é feito pelos métodos `get`:

```java
private String item;
private String tamanho;
private List<String> adicionais;
private String observacoes;
```

O construtor recebe todos os dados de uma vez e os atribui aos campos:

```java
public ItemPedido(String item, String tamanho, List<String> adicionais, String observacoes) {
    this.item = item;
    this.tamanho = tamanho;
    this.adicionais = adicionais;
    this.observacoes = observacoes;
}
```

A palavra `this` diferencia o campo da classe do parâmetro do método, já que os dois têm o mesmo nome.

#### Como o preço é calculado

O cálculo do preço foi dividido em dois métodos para ficar mais organizado:

**`getValorBase()`** usa dois `switch` aninhados — um para o nome do item e outro para o tamanho — para retornar o preço correto de cada combinação:

```java
public double getValorBase() {
    switch (item) {
        case "X-Bacon":
            switch (tamanho) {
                case "Pequeno": return 18.00;
                case "Grande":  return 33.00;
                default:        return 25.00; // Médio
            }
        // outros itens...
    }
}
```

**`getValorAdicionais()`** percorre a lista de adicionais com um `for-each` e soma o valor de cada um. "Sem Cebola" é grátis, por isso não aparece no switch:

```java
public double getValorAdicionais() {
    double valor = 0;
    if (adicionais != null) {
        for (String adicional : adicionais) {
            switch (adicional) {
                case "Queijo Extra":   valor += 3.00; break;
                case "Bacon":          valor += 4.00; break;
                case "Molho Especial": valor += 2.50; break;
                case "Duplo":          valor += 8.00; break;
                // "Sem Cebola" é grátis, não soma nada
            }
        }
    }
    return valor;
}
```

**`getValorTotal()`** simplesmente soma os dois:

```java
public double getValorTotal() {
    return getValorBase() + getValorAdicionais();
}
```

#### Como o item é exibido na tela

O método `toString()` foi sobrescrito com `@Override` para definir como o objeto aparece quando convertido para texto. Ele monta a linha concatenando os dados com `+`:

```java
@Override
public String toString() {
    String texto = item + " (" + tamanho + ")";

    if (adicionais != null && !adicionais.isEmpty()) {
        texto = texto + " + " + String.join(", ", adicionais);
    }

    if (observacoes != null && !observacoes.isEmpty()) {
        texto = texto + " [Obs: " + observacoes + "]";
    }

    texto = texto + " - R$ " + String.format("%.2f", getValorTotal());

    return texto;
}
```

`String.join(", ", adicionais)` junta todos os adicionais separados por vírgula: `"Bacon, Queijo Extra"`.

`String.format("%.2f", valor)` formata o número com sempre duas casas decimais: `25.00`.

---

### Pedido.java — Como o Carrinho foi Feito

A classe `Pedido` representa o carrinho completo. Ela guarda uma lista de `ItemPedido`s usando `ArrayList`, que cresce automaticamente conforme itens são adicionados:

```java
private List<ItemPedido> itens;

public Pedido() {
    this.itens = new ArrayList<>();
}
```

**Adicionar e remover itens:**

Adicionar insere o item no final da lista:

```java
public void adicionarItem(ItemPedido item) {
    itens.add(item);
}
```

Remover valida o índice antes de agir, para evitar erros caso o índice seja inválido:

```java
public boolean removerItem(int indice) {
    if (indice >= 0 && indice < itens.size()) {
        itens.remove(indice);
        return true;
    }
    return false;
}
```

**Calcular o total:**

Percorre todos os itens com um `for-each` e soma o valor de cada um:

```java
public double calcularTotal() {
    double total = 0;
    for (ItemPedido item : itens) {
        total += item.getValorTotal();
    }
    return total;
}
```

**Como o pedido é exibido no histórico:**

O `toString()` usa o `for` tradicional (com `i`) porque precisa numerar cada item:

```java
@Override
public String toString() {
    String texto = "=== PEDIDO ===\n";

    for (int i = 0; i < itens.size(); i++) {
        texto = texto + (i + 1) + ". " + itens.get(i).toString() + "\n";
    }

    texto = texto + "TOTAL: R$ " + String.format("%.2f", calcularTotal()) + "\n";

    return texto;
}
```

---

### PedidoController.java — Como a Lógica foi Organizada

O Controller mantém dois campos principais:

```java
// lista que guarda todos os pedidos finalizados durante o uso do programa
private List<Pedido> pedidos = new ArrayList<>();

// pedido atual que está sendo montado
private Pedido pedidoAtual = new Pedido();
```

Quando o programa inicia, `pedidoAtual` já começa como um `Pedido` vazio, pronto para receber itens.

**Como `adicionarItem` funciona:**

Primeiro valida se os dados obrigatórios foram preenchidos. Se algum estiver vazio, retorna `false` para que a View mostre um aviso:

```java
public boolean adicionarItem(String item, String tamanho, List<String> adicionais, String observacoes) {
    if (item == null || item.isEmpty() || tamanho == null || tamanho.isEmpty()) {
        return false;
    }
    ItemPedido itemPedido = new ItemPedido(item, tamanho, adicionais, observacoes);
    pedidoAtual.adicionarItem(itemPedido);
    return true;
}
```

**Como `finalizarPedido` funciona:**

Verifica se o pedido tem pelo menos um item. Se tiver, salva no histórico, cria um novo pedido vazio e retorna o total para a View exibir:

```java
public double finalizarPedido() {
    if (pedidoAtual.getItens().isEmpty()) {
        return -1; // sinal de erro: pedido vazio
    }
    double total = pedidoAtual.calcularTotal();
    pedidos.add(pedidoAtual);   // salva no histórico
    pedidoAtual = new Pedido(); // começa um novo pedido vazio
    return total;
}
```

---

### JanelaPrincipal.java — Como a Tela foi Construída

A classe herda de `JFrame`, que é a janela do sistema operacional:

```java
public class JanelaPrincipal extends JFrame {
```

Isso significa que `JanelaPrincipal` **é** uma janela e herda todos os comportamentos de `JFrame` automaticamente.

#### Por que os componentes são campos da classe

Os componentes visuais (como `comboItem`, `checkBacon`, etc.) são declarados no topo da classe, fora de qualquer método. Isso é necessário porque vários métodos diferentes precisam acessá-los:

- `criarPainelEsquerdo()` os cria e os coloca na tela
- `adicionarItem()` os lê para saber o que o usuário escolheu
- `limparCamposItem()` os reseta após adicionar um item

Se fossem declarados dentro de um método, os outros não conseguiriam enxergá-los.

#### Como o layout foi montado

O JavaSwing usa **LayoutManagers** para definir como os componentes são organizados dentro de um painel.

O `BoxLayout.Y_AXIS` empilha os componentes de **cima para baixo**:

```java
painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
```

O `BoxLayout.X_AXIS` coloca os componentes **lado a lado**:

```java
painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.X_AXIS));
```

`Box.createVerticalStrut(10)` cria um espaço vazio de 10 pixels entre componentes, funcionando como uma margem.

#### Como os botões de tamanho funcionam

`JRadioButton` permite selecionar apenas uma opção de um grupo. Para garantir que apenas um tamanho seja selecionado por vez, todos os radio buttons são adicionados a um `ButtonGroup`:

```java
grupoTamanho = new ButtonGroup();
grupoTamanho.add(radioPequeno);
grupoTamanho.add(radioMedio);
grupoTamanho.add(radioGrande);
```

O `ButtonGroup` não aparece na tela — ele apenas controla a lógica de seleção exclusiva.

#### Como os eventos dos botões foram configurados

Cada botão usa uma **expressão lambda** como `ActionListener`. Isso significa: "quando este botão for clicado, execute este código":

```java
JButton botaoAdicionar = new JButton("Adicionar Item");
botaoAdicionar.addActionListener(e -> adicionarItem());
```

O `e` representa o evento de clique, mas neste caso não é usado — só nos importa que o clique aconteceu.

#### Como a lista de itens é atualizada

A `JList` não é atualizada diretamente. Ela exibe os dados de um `DefaultListModel`, que funciona como a fonte de dados da lista. Para atualizar o que aparece na tela, basta atualizar o modelo:

```java
private void atualizarListaItens() {
    modeloListaItens.clear(); // limpa o modelo
    for (ItemPedido item : controller.getItensAtuais()) {
        modeloListaItens.addElement(item.toString()); // adiciona cada item como texto
    }
    labelTotal.setText("Total: R$ " + String.format("%.2f", controller.calcularTotalAtual()));
}
```

A `JList` detecta automaticamente as mudanças no modelo e se redesenha.

#### Como o método adicionarItem da View funciona

Este método mostra bem a separação de responsabilidades do MVC. A View lê os campos, monta os dados e passa para o Controller. Ela não calcula nada:

```java
private void adicionarItem() {
    // 1. lê o item selecionado no combo
    String item = (String) comboItem.getSelectedItem();

    // 2. descobre qual radio button está marcado
    String tamanho = "";
    if (radioPequeno.isSelected()) tamanho = "Pequeno";
    else if (radioMedio.isSelected()) tamanho = "Médio";
    else if (radioGrande.isSelected()) tamanho = "Grande";

    // 3. monta a lista de adicionais marcados
    List<String> adicionais = new ArrayList<>();
    if (checkQueijoExtra.isSelected()) adicionais.add("Queijo Extra");
    if (checkBacon.isSelected()) adicionais.add("Bacon");
    // ...

    // 4. lê as observações
    String observacoes = areaObservacoes.getText();

    // 5. passa tudo para o Controller processar
    boolean adicionou = controller.adicionarItem(item, tamanho, adicionais, observacoes);

    // 6. se deu certo, atualiza a tela; se não, mostra aviso
    if (adicionou) {
        atualizarListaItens();
        atualizarResumo();
        limparCamposItem();
    } else {
        JOptionPane.showMessageDialog(this, "Selecione um item e um tamanho!");
    }
}
```

---

## Fluxo Completo de um Pedido

```
1. Main.java executa → cria JanelaPrincipal
2. JanelaPrincipal cria PedidoController
3. PedidoController cria um Pedido vazio (pedidoAtual)

4. Usuário escolhe item, tamanho e adicionais → clica "Adicionar"
   → adicionarItem() lê os campos
   → controller.adicionarItem() cria um ItemPedido e adiciona ao pedidoAtual
   → atualizarListaItens() redesenha a lista e o total

5. Usuário clica "Finalizar Pedido"
   → controller.finalizarPedido() salva o pedido no histórico
   → um novo Pedido vazio vira o pedidoAtual
   → a tela é limpa

6. Usuário acessa "Histórico" no menu
   → controller.getHistorico() retorna todos os pedidos finalizados
   → mostrarHistorico() exibe em uma janela de diálogo
```
