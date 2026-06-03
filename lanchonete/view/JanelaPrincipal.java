/**
 * Aqui é onde inicia a primeira tela do sistema
 */

package lanchonete.view;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import lanchonete.controller.PedidoController;
import lanchonete.model.ItemPedido;

public class JanelaPrincipal extends JFrame {

    // controller que irá tratar os dados do pedido
    PedidoController controller = new PedidoController();

    // campos do pedido - declarados aqui fora para que qualquer método consiga ler os valores digitados
    JComboBox<String> comboItem;
    JRadioButton radioPequeno;
    JRadioButton radioMedio;
    JRadioButton radioGrande;
    ButtonGroup grupoTamanho;
    JCheckBox checkQueijoExtra;
    JCheckBox checkBacon;
    JCheckBox checkSemCebola;
    JCheckBox checkMolhoEspecial;
    JCheckBox checkDuplo;
    JTextArea areaObservacoes;
    JTabbedPane abas;

    // área de resumo do pedido
    JTextArea areaResumo;
    JLabel labelTotal;

    // lista de itens do pedido atual
    DefaultListModel<String> modeloListaItens;
    JList<String> listaItens;

    public JanelaPrincipal() {
        setTitle("Sistema de Pedidos - Lanchonete");
        setSize(850, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        criarMenu();

        abas = new JTabbedPane();
        abas.addTab("Cardápio", criarPainelCardapio());
        abas.addTab("Pedido", criarPainelPedido());
        abas.addTab("Resumo", criarPainelResumo());
        add(abas);
        setVisible(true);
    }

    private void criarMenu() {
        // 1. cria a barra
        JMenuBar barraMenu = new JMenuBar();

        // 2. cria o menu Sistema
        JMenu menuSistema = new JMenu("Sistema");

        JMenuItem itemNovo = new JMenuItem("Novo Pedido");
        itemNovo.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Deseja iniciar um novo pedido? O pedido atual será perdido.", 
                "Novo Pedido", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.novoPedido();
                limparCamposPedido();
                atualizarResumo();
            }
        });
        menuSistema.add(itemNovo);

        JMenuItem itemHistorico = new JMenuItem("Histórico");
        itemHistorico.addActionListener(e -> {
            mostrarHistorico();
        });
        menuSistema.add(itemHistorico);

        menuSistema.addSeparator();

        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente sair do sistema?", 
                "Sair", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        menuSistema.add(itemSair);

        barraMenu.add(menuSistema);
        setJMenuBar(barraMenu);
    }

    private JPanel criarPainelCardapio() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("CARDÁPIO");
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setFont(titulo.getFont().deriveFont(18f));
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(20));

        // Itens do cardápio
        String[] itens = {
            "Hambúrguer Clássico - R$ 15,00 / R$ 22,00 / R$ 30,00",
            "X-Bacon - R$ 18,00 / R$ 25,00 / R$ 33,00",
            "X-Salada - R$ 16,00 / R$ 23,00 / R$ 31,00",
            "Batata Frita - R$ 12,00 / R$ 18,00 / R$ 25,00",
            "Refrigerante - R$ 8,00 / R$ 10,00 / R$ 12,00",
            "Suco Natural - R$ 10,00 / R$ 14,00 / R$ 18,00",
            "Milkshake - R$ 14,00 / R$ 18,00 / R$ 22,00"
        };

        for (String item : itens) {
            JLabel label = new JLabel("• " + item);
            label.setAlignmentX(LEFT_ALIGNMENT);
            painel.add(label);
            painel.add(Box.createVerticalStrut(5));
        }

        painel.add(Box.createVerticalStrut(20));

        JLabel tituloAdicionais = new JLabel("ADICIONAIS:");
        tituloAdicionais.setFont(tituloAdicionais.getFont().deriveFont(14f));
        tituloAdicionais.setAlignmentX(LEFT_ALIGNMENT);
        painel.add(tituloAdicionais);
        painel.add(Box.createVerticalStrut(10));

        String[] adicionais = {
            "Queijo Extra - R$ 3,00",
            "Bacon - R$ 4,00",
            "Sem Cebola - Grátis",
            "Molho Especial - R$ 2,50",
            "Duplo - R$ 8,00"
        };

        for (String adicional : adicionais) {
            JLabel label = new JLabel("• " + adicional);
            label.setAlignmentX(LEFT_ALIGNMENT);
            painel.add(label);
            painel.add(Box.createVerticalStrut(5));
        }

        return painel;
    }

    private JPanel criarPainelPedido() {
        // 1. cria o painel principal (container que vai ter o container da esquerda e o da direita)
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.X_AXIS));

        // 2. chama o método que monta o painel esquerdo (dados do item)
        JPanel painelEsquerdo = criarPainelEsquerdo();

        // 3. chama o método que monta o painel direito (lista de itens + botões)
        JPanel painelDireito = criarPainelDireito();

        // 4. adiciona os dois painéis dentro do painel principal lado a lado
        painelPrincipal.add(painelEsquerdo);
        painelPrincipal.add(painelDireito);

        return painelPrincipal;
    }

    private JPanel criarPainelEsquerdo() {
        // opções que vão aparecer na lista suspensa de itens
        String[] itens = { "Hambúrguer Clássico", "X-Bacon", "X-Salada", "Batata Frita", "Refrigerante", "Suco Natural", "Milkshake" };

        // cria o painel e define que os componentes serão empilhados de cima pra baixo
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Montar Pedido");
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(15));

        JLabel labelItem = new JLabel("Item");
        labelItem.setAlignmentX(LEFT_ALIGNMENT);
        painel.add(labelItem);
        comboItem = new JComboBox<>(itens);
        comboItem.setAlignmentX(LEFT_ALIGNMENT);
        painel.add(comboItem);
        painel.add(Box.createVerticalStrut(10));

        JLabel labelTamanho = new JLabel("Tamanho");
        labelTamanho.setAlignmentX(LEFT_ALIGNMENT);
        painel.add(labelTamanho);

        // cria os três botões de seleção de tamanho
        radioPequeno = new JRadioButton("Pequeno");
        radioMedio = new JRadioButton("Médio", true); // selecionado por padrão
        radioGrande = new JRadioButton("Grande");
        radioPequeno.setAlignmentX(LEFT_ALIGNMENT);
        radioMedio.setAlignmentX(LEFT_ALIGNMENT);
        radioGrande.setAlignmentX(LEFT_ALIGNMENT);

        // junta os três no ButtonGroup pra que só um possa ser selecionado
        grupoTamanho = new ButtonGroup();
        grupoTamanho.add(radioPequeno);
        grupoTamanho.add(radioMedio);
        grupoTamanho.add(radioGrande);

        painel.add(radioPequeno);
        painel.add(radioMedio);
        painel.add(radioGrande);
        painel.add(Box.createVerticalStrut(10));

        JLabel labelAdicionais = new JLabel("Adicionais");
        labelAdicionais.setAlignmentX(LEFT_ALIGNMENT);
        painel.add(labelAdicionais);

        // cada checkbox é um adicional e como são JCheckBox, podem ser marcadas várias ao mesmo tempo
        checkQueijoExtra = new JCheckBox("Queijo Extra");
        checkBacon = new JCheckBox("Bacon");
        checkSemCebola = new JCheckBox("Sem Cebola");
        checkMolhoEspecial = new JCheckBox("Molho Especial");
        checkDuplo = new JCheckBox("Duplo");

        checkQueijoExtra.setAlignmentX(LEFT_ALIGNMENT);
        checkBacon.setAlignmentX(LEFT_ALIGNMENT);
        checkSemCebola.setAlignmentX(LEFT_ALIGNMENT);
        checkMolhoEspecial.setAlignmentX(LEFT_ALIGNMENT);
        checkDuplo.setAlignmentX(LEFT_ALIGNMENT);

        painel.add(checkQueijoExtra);
        painel.add(checkBacon);
        painel.add(checkSemCebola);
        painel.add(checkMolhoEspecial);
        painel.add(checkDuplo);
        painel.add(Box.createVerticalStrut(10));

        JLabel labelObs = new JLabel("Observações");
        labelObs.setAlignmentX(LEFT_ALIGNMENT);
        painel.add(labelObs);
        areaObservacoes = new JTextArea(3, 20);
        areaObservacoes.setAlignmentX(LEFT_ALIGNMENT);
        areaObservacoes.setLineWrap(true);
        areaObservacoes.setWrapStyleWord(true);
        JScrollPane scrollObs = new JScrollPane(areaObservacoes);
        scrollObs.setAlignmentX(LEFT_ALIGNMENT);
        painel.add(scrollObs);

        return painel;
    }

    private JPanel criarPainelDireito() {
        // cria o painel e define que os componentes serão empilhados de cima pra baixo
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Itens do Pedido");
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        titulo.setFont(titulo.getFont().deriveFont(16f));
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(10));

        // lista de itens do pedido
        modeloListaItens = new DefaultListModel<>();
        listaItens = new JList<>(modeloListaItens);
        JScrollPane scrollLista = new JScrollPane(listaItens);
        scrollLista.setAlignmentX(LEFT_ALIGNMENT);
        painel.add(scrollLista);
        painel.add(Box.createVerticalStrut(15));

        // parte dos botões adicionar, remover e finalizar
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new BoxLayout(painelBotoes, BoxLayout.Y_AXIS));

        JPanel painelBotoesLinha1 = new JPanel();
        JButton botaoAdicionar = new JButton("Adicionar Item");
        botaoAdicionar.addActionListener(e -> adicionarItem());
        painelBotoesLinha1.add(botaoAdicionar);

        JButton botaoRemover = new JButton("Remover Item");
        botaoRemover.addActionListener(e -> removerItem());
        painelBotoesLinha1.add(botaoRemover);
        painelBotoes.add(painelBotoesLinha1);

        painelBotoes.add(Box.createVerticalStrut(10));

        JPanel painelBotoesLinha2 = new JPanel();
        JButton botaoLimpar = new JButton("Limpar");
        botaoLimpar.addActionListener(e -> limparCamposPedido());
        painelBotoesLinha2.add(botaoLimpar);

        JButton botaoFinalizar = new JButton("Finalizar Pedido");
        botaoFinalizar.addActionListener(e -> finalizarPedido());
        painelBotoesLinha2.add(botaoFinalizar);
        painelBotoes.add(painelBotoesLinha2);

        painel.add(painelBotoes);

        // label do total
        labelTotal = new JLabel("Total: R$ 0,00");
        labelTotal.setAlignmentX(LEFT_ALIGNMENT);
        labelTotal.setFont(labelTotal.getFont().deriveFont(16f));
        painel.add(Box.createVerticalStrut(15));
        painel.add(labelTotal);

        return painel;
    }

    private JPanel criarPainelResumo() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Resumo do Pedido");
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setFont(titulo.getFont().deriveFont(18f));
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(20));

        areaResumo = new JTextArea(20, 50);
        areaResumo.setEditable(false);
        areaResumo.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaResumo);
        scroll.setAlignmentX(CENTER_ALIGNMENT);
        painel.add(scroll);

        return painel;
    }

    private void adicionarItem() {
        String item = (String) comboItem.getSelectedItem();
        String tamanho = "";
        if (radioPequeno.isSelected()) tamanho = "Pequeno";
        else if (radioMedio.isSelected()) tamanho = "Médio";
        else if (radioGrande.isSelected()) tamanho = "Grande";

        List<String> adicionais = new ArrayList<>();
        if (checkQueijoExtra.isSelected()) adicionais.add("Queijo Extra");
        if (checkBacon.isSelected()) adicionais.add("Bacon");
        if (checkSemCebola.isSelected()) adicionais.add("Sem Cebola");
        if (checkMolhoEspecial.isSelected()) adicionais.add("Molho Especial");
        if (checkDuplo.isSelected()) adicionais.add("Duplo");

        String observacoes = areaObservacoes.getText();

        boolean adicionou = controller.adicionarItem(item, tamanho, adicionais, observacoes);

        if (adicionou) {
            atualizarListaItens();
            atualizarResumo();
            limparCamposItem();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item e um tamanho!");
        }
    }

    private void removerItem() {
        int indice = listaItens.getSelectedIndex();
        if (indice >= 0) {
            controller.removerItem(indice);
            atualizarListaItens();
            atualizarResumo();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover!");
        }
    }

    private void finalizarPedido() {
        double total = controller.calcularTotalAtual();
        if (total <= 0) {
            JOptionPane.showMessageDialog(this, "Adicione itens ao pedido antes de finalizar!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Deseja finalizar o pedido?\nTotal: R$ " + String.format("%.2f", total),
            "Confirmar Pedido", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            double totalFinal = controller.finalizarPedido();
            JOptionPane.showMessageDialog(this,
                "Pedido finalizado com sucesso!\nTotal: R$ " + String.format("%.2f", totalFinal),
                "Pedido Finalizado", JOptionPane.INFORMATION_MESSAGE);
            limparCamposPedido();
            atualizarResumo();
        }
    }

    private void atualizarListaItens() {
        modeloListaItens.clear();
        for (ItemPedido item : controller.getItensAtuais()) {
            modeloListaItens.addElement(item.toString());
        }
        labelTotal.setText("Total: R$ " + String.format("%.2f", controller.calcularTotalAtual()));
    }

    private void atualizarResumo() {
        StringBuilder sb = new StringBuilder();
        List<ItemPedido> itens = controller.getItensAtuais();
        if (itens.isEmpty()) {
            sb.append("Nenhum item no pedido.");
        } else {
            sb.append("=== ITENS DO PEDIDO ===\n\n");
            for (int i = 0; i < itens.size(); i++) {
                sb.append(i + 1).append(". ").append(itens.get(i).toString()).append("\n\n");
            }
            sb.append("========================\n");
            sb.append("TOTAL: R$ ").append(String.format("%.2f", controller.calcularTotalAtual())).append("\n");
        }
        areaResumo.setText(sb.toString());
    }

    private void limparCamposItem() {
        comboItem.setSelectedIndex(0);
        radioMedio.setSelected(true);
        checkQueijoExtra.setSelected(false);
        checkBacon.setSelected(false);
        checkSemCebola.setSelected(false);
        checkMolhoEspecial.setSelected(false);
        checkDuplo.setSelected(false);
        areaObservacoes.setText("");
    }

    private void limparCamposPedido() {
        limparCamposItem();
        modeloListaItens.clear();
        labelTotal.setText("Total: R$ 0,00");
    }

    private void mostrarHistorico() {
        StringBuilder sb = new StringBuilder();
        var historico = controller.getHistorico();
        if (historico.isEmpty()) {
            sb.append("Nenhum pedido no histórico.");
        } else {
            sb.append("=== HISTÓRICO DE PEDIDOS ===\n\n");
            for (int i = 0; i < historico.size(); i++) {
                sb.append("PEDIDO #").append(i + 1).append("\n");
                sb.append(historico.get(i).toString()).append("\n");
            }
        }
        JTextArea area = new JTextArea(sb.toString(), 20, 50);
        area.setEditable(false);
        area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(this, scroll, "Histórico de Pedidos", JOptionPane.INFORMATION_MESSAGE);
    }
}