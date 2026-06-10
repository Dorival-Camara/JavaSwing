/**
 * Janela principal da lanchonete.
 * É a tela que o usuário vê e usa — monta as três abas (Cardápio, Fazer Pedido, Resumo)
 * e reage aos cliques dos botões chamando o Controller para processar os dados.
 */
package lanchonete.view;

import java.util.ArrayList; // ArrayList é uma lista que cresce conforme adicionamos itens, sem tamanho fixo
import java.util.List;      // necessário para declarar o tipo da lista de adicionais
import javax.swing.*;       // importa todos os componentes visuais do Java Swing (JFrame, JButton, JLabel, etc.)
import lanchonete.controller.PedidoController; // o controller que processa os pedidos
import lanchonete.model.ItemPedido;            // modelo de cada item do pedido
import lanchonete.model.Pedido;                // modelo do pedido completo

public class JanelaPrincipal extends JFrame { // JFrame é a janela em si

    // controller que vai processar tudo que o usuário fizer na tela
    private PedidoController controller;

    // componente que cria as abas (Cardápio, Fazer Pedido, Resumo)
    private JTabbedPane abas;

    // --- campos da aba "Fazer Pedido" ---
    private JComboBox<String> comboItem;                          // lista suspensa com os itens do cardápio
    private JRadioButton rbPequeno, rbMedio, rbGrande;            // botões de tamanho (só um pode ser marcado por vez)
    private JCheckBox cbQueijo, cbBacon, cbMolho, cbDuplo, cbSemCebola; // caixas de adicionais (podem marcar vários)
    private JTextField txtObs;    // campo de texto para observações
    private JTextArea areaItens;  // área que exibe os itens adicionados ao pedido atual
    private JLabel lblTotal;      // label que mostra o total em tempo real

    // --- campos da aba "Resumo" ---
    private JTextArea areaResumo;    // área que exibe o resumo do pedido finalizado
    private JLabel lblTotalResumo;   // label que mostra o total do pedido finalizado
    private JButton btnNovoPedido;   // botão para começar um novo pedido

    // lista fixa com os nomes dos itens disponíveis no cardápio
    private static final String[] ITENS = {
        "Hambúrguer Clássico", "X-Bacon", "X-Salada",
        "Batata Frita", "Refrigerante", "Suco Natural", "Milkshake"
    };

    public static void main(String[] args) {
        // SwingUtilities.invokeLater garante que a janela seja criada na thread certa do Swing
        SwingUtilities.invokeLater(() -> new JanelaPrincipal());
    }

    public JanelaPrincipal() {
        controller = new PedidoController();

        setTitle("Lanchonete");
        setSize(800, 600);
        setResizable(false); // impede o usuário de redimensionar a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fecha o programa ao fechar a janela

        // cria as abas e adiciona cada painel
        abas = new JTabbedPane();
        abas.addTab("Cardápio",     criarPainelCardapio());
        abas.addTab("Fazer Pedido", criarPainelPedido());
        abas.addTab("Resumo",       criarPainelResumo());

        // bloqueia a aba Resumo até que um pedido seja finalizado
        abas.setEnabledAt(2, false);

        setJMenuBar(criarMenuBar()); // adiciona o menu superior
        add(abas);                   // adiciona as abas na janela
        setLocationRelativeTo(null); // centraliza a janela na tela
        setVisible(true);            // exibe a janela
    }

    // =========================================================================
    // Menu superior
    // =========================================================================

    /**
     * Cria o menu superior com as opções Novo Pedido e Sair.
     */
    private JMenuBar criarMenuBar() {
        JMenuBar barraMenu = new JMenuBar();

        // --- menu Pedido ---
        JMenu menuPedido = new JMenu("Pedido");
        JMenuItem itemNovoPedido = new JMenuItem("Novo Pedido");

        // ao clicar em "Novo Pedido", pede confirmação e reinicia o pedido
        itemNovoPedido.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this, "Deseja iniciar um novo pedido?","Novo Pedido", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                controller.novoPedido();
                atualizarListaPedido();
                abas.setEnabledAt(2, false); // bloqueia a aba Resumo
                btnNovoPedido.setVisible(false);
                abas.setSelectedIndex(1);    // vai para a aba "Fazer Pedido"
            }
        });

        menuPedido.add(itemNovoPedido);
        barraMenu.add(menuPedido);

        // --- menu Sair ---
        JMenu menuSair = new JMenu("Sair");
        JMenuItem itemSair = new JMenuItem("Fechar programa");

        // ao clicar em "Fechar programa", abre uma caixa de confirmação
        itemSair.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this, "Confirmar fechamento?","Sair", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) System.exit(0); // encerra o programa
        });

        menuSair.add(itemSair);
        barraMenu.add(menuSair);
        return barraMenu;
    }

    // =========================================================================
    // Aba: Cardápio
    // =========================================================================

    /**
     * Cria o painel da aba Cardápio com a lista de itens e seus preços.
     */
    private JPanel criarPainelCardapio() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS)); // empilha os componentes de cima para baixo
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // margem interna

        JLabel titulo = new JLabel("Cardápio");
        titulo.setAlignmentX(0.5f); // centraliza o título
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(10)); // espaço entre o título e os itens

        // tabela interna com os preços de cada item por tamanho
        String[][] cardapio = {
            {"Hambúrguer Clássico", "R$ 15,00", "R$ 22,00", "R$ 30,00"},
            {"X-Bacon",             "R$ 18,00", "R$ 25,00", "R$ 33,00"},
            {"X-Salada",            "R$ 16,00", "R$ 23,00", "R$ 31,00"},
            {"Batata Frita",        "R$ 12,00", "R$ 18,00", "R$ 25,00"},
            {"Refrigerante",        "R$ 8,00",  "R$ 10,00", "R$ 12,00"},
            {"Suco Natural",        "R$ 10,00", "R$ 14,00", "R$ 18,00"},
            {"Milkshake",           "R$ 14,00", "R$ 18,00", "R$ 22,00"},
        };

        // para cada item do cardápio, cria uma linha com nome e preços
        for (String[] linha : cardapio) {
            JLabel label = new JLabel(linha[0] + " — P: " + linha[1] + " | M: " + linha[2] + " | G: " + linha[3]);
            label.setAlignmentX(0.0f);
            painel.add(label);
            painel.add(Box.createVerticalStrut(6)); // espaço entre cada item
        }

        painel.add(Box.createVerticalStrut(10));

        // painel com a lista de adicionais disponíveis
        JPanel adicionais = new JPanel();
        adicionais.setLayout(new BoxLayout(adicionais, BoxLayout.Y_AXIS));
        adicionais.setBorder(BorderFactory.createTitledBorder("Adicionais disponíveis"));
        adicionais.add(new JLabel("Queijo Extra: +R$ 3,00"));
        adicionais.add(new JLabel("Bacon: +R$ 4,00"));
        adicionais.add(new JLabel("Molho Especial: +R$ 2,50"));
        adicionais.add(new JLabel("Duplo: +R$ 8,00"));
        adicionais.add(new JLabel("Sem Cebola: Grátis"));
        painel.add(adicionais);

        return painel;
    }

    // =========================================================================
    // Aba: Fazer Pedido
    // =========================================================================

    /**
     * Cria o painel da aba Fazer Pedido, dividido em dois lados:
     * esquerda (formulário de seleção) e direita (pedido atual).
     */
    private JPanel criarPainelPedido() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Fazer Pedido");
        titulo.setAlignmentX(0.5f);
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(8));

        // painel horizontal que coloca o formulário e o pedido atual lado a lado
        JPanel linha = new JPanel();
        linha.setLayout(new BoxLayout(linha, BoxLayout.X_AXIS)); // empilha da esquerda para a direita
        linha.add(criarFormulario());
        linha.add(Box.createHorizontalStrut(10)); // espaço entre os dois lados
        linha.add(criarPainelAtual());
        painel.add(linha);

        return painel;
    }

    /**
     * Cria o formulário onde o cliente escolhe item, tamanho, adicionais e observações.
     */
    private JPanel criarFormulario() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createTitledBorder("Escolha o item"));

        // lista suspensa com os itens do cardápio
        comboItem = new JComboBox<>(ITENS);
        form.add(new JLabel("Item:"));
        form.add(comboItem);
        form.add(Box.createVerticalStrut(8));

        // botões de tamanho — ButtonGroup garante que só um pode ser marcado por vez
        rbPequeno = new JRadioButton("Pequeno");
        rbMedio   = new JRadioButton("Médio", true); // "true" deixa este marcado por padrão
        rbGrande  = new JRadioButton("Grande");
        ButtonGroup grupoTamanho = new ButtonGroup();
        grupoTamanho.add(rbPequeno);
        grupoTamanho.add(rbMedio);
        grupoTamanho.add(rbGrande);
        JPanel painelTamanho = new JPanel();
        painelTamanho.setLayout(new BoxLayout(painelTamanho, BoxLayout.X_AXIS)); // botões lado a lado
        painelTamanho.add(rbPequeno);
        painelTamanho.add(rbMedio);
        painelTamanho.add(rbGrande);
        form.add(new JLabel("Tamanho:"));
        form.add(painelTamanho);
        form.add(Box.createVerticalStrut(8));

        // checkboxes de adicionais — diferente do RadioButton, podem ser marcados vários ao mesmo tempo
        cbQueijo    = new JCheckBox("Queijo Extra (+R$ 3,00)");
        cbBacon     = new JCheckBox("Bacon (+R$ 4,00)");
        cbMolho     = new JCheckBox("Molho Especial (+R$ 2,50)");
        cbDuplo     = new JCheckBox("Duplo (+R$ 8,00)");
        cbSemCebola = new JCheckBox("Sem Cebola (grátis)");
        form.add(new JLabel("Adicionais:"));
        form.add(cbQueijo);
        form.add(cbBacon);
        form.add(cbMolho);
        form.add(cbDuplo);
        form.add(cbSemCebola);
        form.add(Box.createVerticalStrut(8));

        // campo de texto livre para o cliente digitar observações
        txtObs = new JTextField(20);
        form.add(new JLabel("Observações:"));
        form.add(txtObs);
        form.add(Box.createVerticalStrut(8));

        // botão que chama o método adicionarItem() ao ser clicado
        JButton btnAdicionar = new JButton("Adicionar ao Pedido");
        btnAdicionar.addActionListener(e -> adicionarItem());
        form.add(btnAdicionar);

        return form;
    }

    /**
     * Cria o painel que exibe os itens do pedido atual e os botões de ação.
     */
    private JPanel criarPainelAtual() {
        JPanel painelAtual = new JPanel();
        painelAtual.setLayout(new BoxLayout(painelAtual, BoxLayout.Y_AXIS));
        painelAtual.setBorder(BorderFactory.createTitledBorder("Pedido Atual"));

        // área de texto onde os itens adicionados aparecem (somente leitura)
        areaItens = new JTextArea();
        areaItens.setEditable(false);
        painelAtual.add(new JScrollPane(areaItens)); // JScrollPane adiciona barra de rolagem se necessário
        painelAtual.add(Box.createVerticalStrut(5));

        lblTotal = new JLabel("Total: R$ 0,00");
        JButton btnRemover   = new JButton("Remover Último");
        JButton btnFinalizar = new JButton("Finalizar Pedido");
        btnRemover.addActionListener(e -> removerItem());     // remove o último item da lista
        btnFinalizar.addActionListener(e -> finalizarPedido()); // finaliza e vai para o resumo
        painelAtual.add(lblTotal);
        painelAtual.add(btnRemover);
        painelAtual.add(btnFinalizar);

        return painelAtual;
    }

    // =========================================================================
    // Aba: Resumo
    // =========================================================================

    /**
     * Cria o painel da aba Resumo, que exibe o último pedido finalizado.
     */
    private JPanel criarPainelResumo() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Resumo do Último Pedido");
        titulo.setAlignmentX(0.5f);
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(10));

        // área de texto que exibe o conteúdo do pedido finalizado (somente leitura)
        areaResumo = new JTextArea();
        areaResumo.setEditable(false);
        painel.add(new JScrollPane(areaResumo));
        painel.add(Box.createVerticalStrut(5));

        lblTotalResumo = new JLabel("");
        btnNovoPedido = new JButton("Novo Pedido");
        btnNovoPedido.setVisible(false); // fica escondido até um pedido ser finalizado

        // ao clicar em "Novo Pedido": limpa o pedido, bloqueia o resumo e volta para a aba de pedido
        btnNovoPedido.addActionListener(e -> {
            controller.novoPedido();
            atualizarListaPedido();
            abas.setEnabledAt(2, false); // bloqueia a aba Resumo novamente
            btnNovoPedido.setVisible(false);
            abas.setSelectedIndex(1);    // vai para a aba "Fazer Pedido"
        });
        painel.add(lblTotalResumo);
        painel.add(btnNovoPedido);

        return painel;
    }

    // =========================================================================
    // Ações dos botões
    // =========================================================================

    /**
     * Lê o que o usuário escolheu no formulário e envia para o controller adicionar.
     */
    private void adicionarItem() {
        String item    = (String) comboItem.getSelectedItem(); // pega o item selecionado no combo
        // descobre o tamanho verificando qual RadioButton está marcado
        String tamanho = rbPequeno.isSelected() ? "Pequeno" : rbGrande.isSelected() ? "Grande" : "Médio";

        // monta a lista de adicionais verificando quais checkboxes estão marcados
        List<String> adicionais = new ArrayList<>();
        if (cbQueijo.isSelected())    adicionais.add("Queijo Extra");
        if (cbBacon.isSelected())     adicionais.add("Bacon");
        if (cbMolho.isSelected())     adicionais.add("Molho Especial");
        if (cbDuplo.isSelected())     adicionais.add("Duplo");
        if (cbSemCebola.isSelected()) adicionais.add("Sem Cebola");

        controller.adicionarItem(item, tamanho, adicionais, txtObs.getText().trim());
        atualizarListaPedido(); // atualiza a área de itens e o total na tela
    }

    /**
     * Remove o último item adicionado ao pedido.
     */
    private void removerItem() {
        List<ItemPedido> itens = controller.getItensAtuais();
        if (!itens.isEmpty()) { // só remove se houver itens
            controller.removerItem(itens.size() - 1); // remove o último da lista
            atualizarListaPedido();
        }
    }

    /**
     * Finaliza o pedido: valida, chama o controller, exibe mensagem e vai para o Resumo.
     */
    private void finalizarPedido() {
        // impede finalizar um pedido vazio
        if (controller.getItensAtuais().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Adicione ao menos um item antes de finalizar.",
                    "Pedido Vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double total = controller.finalizarPedido(); // envia para o controller finalizar e recebe o total

        // exibe mensagem de confirmação com o total (substitui ponto por vírgula no valor)
        JOptionPane.showMessageDialog(this,
                String.format("Pedido finalizado! Total: R$ %.2f", total).replace(".", ","),
                "Pedido Finalizado", JOptionPane.INFORMATION_MESSAGE);

        atualizarListaPedido();      // limpa a área de itens
        atualizarResumo();           // preenche a aba de resumo
        btnNovoPedido.setVisible(true);
        abas.setEnabledAt(2, true);  // desbloqueia a aba Resumo
        abas.setSelectedIndex(2);    // vai automaticamente para a aba Resumo
    }

    /**
     * Atualiza a área de itens e o label de total com o estado atual do pedido.
     * É chamado sempre que um item é adicionado ou removido.
     */
    private void atualizarListaPedido() {
        StringBuilder sb = new StringBuilder();
        for (ItemPedido item : controller.getItensAtuais()) {
            sb.append(item.toString()).append("\n"); // cada item em uma linha
        }
        areaItens.setText(sb.toString());
        lblTotal.setText(
            String.format("Total: R$ %.2f", controller.calcularTotalAtual()).replace(".", ",")
        );
    }

    /**
     * Preenche a aba Resumo com os dados do último pedido finalizado.
     */
    private void atualizarResumo() {
        List<Pedido> historico = controller.getHistorico();
        if (historico.isEmpty()) return; // segurança: não faz nada se não houver pedidos

        // pega o último pedido do histórico (o que acabou de ser finalizado)
        Pedido ultimo = historico.get(historico.size() - 1);
        areaResumo.setText(ultimo.toString());
        lblTotalResumo.setText(
            String.format("Total: R$ %.2f", ultimo.calcularTotal()).replace(".", ",")
        );
    }
}
