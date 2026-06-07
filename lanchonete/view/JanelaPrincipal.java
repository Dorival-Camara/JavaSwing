/**
 * Janela principal da lanchonete.
 * Contém as três abas (Cardápio, Fazer Pedido, Resumo) e o menu superior.
 */
package lanchonete.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import lanchonete.controller.PedidoController;
import lanchonete.model.ItemPedido;
import lanchonete.model.Pedido;

public class JanelaPrincipal extends JFrame {

    private PedidoController controller;
    private JTabbedPane abas;

    // Campos do painel "Fazer Pedido"
    private JComboBox<String> comboItem;
    private JRadioButton rbPequeno, rbMedio, rbGrande;
    private JCheckBox cbQueijo, cbBacon, cbMolho, cbDuplo, cbSemCebola;
    private JTextField txtObs;
    private DefaultListModel<String> modeloLista;
    private JList<String> listaItens;
    private JLabel lblTotal;

    // Campos do painel "Resumo"
    private JTextArea areaResumo;
    private JLabel lblTotalResumo;
    private JButton btnNovoPedido;

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
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Cria as abas e preenche cada uma chamando os métodos abaixo
        abas = new JTabbedPane();
        abas.addTab("Cardápio",     criarPainelCardapio());
        abas.addTab("Fazer Pedido", criarPainelPedido());
        abas.addTab("Resumo",       criarPainelResumo());

        // Bloqueia a aba Resumo até ter um pedido finalizado
        abas.setEnabledAt(2, false);

        setJMenuBar(criarMenuBar());
        add(abas);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // =========================================================================
    // Menu superior
    // =========================================================================
    private JMenuBar criarMenuBar() {
        JMenuBar barraMenu = new JMenuBar();

        // Sair
        JMenu menuSair = new JMenu("Sair");
        JMenuItem itemSair = new JMenuItem("Fechar programa");
        itemSair.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this, "Confirmar fechamento?",
                    "Sair", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) System.exit(0);
        });
        menuSair.add(itemSair);

        barraMenu.add(menuSair);
        return barraMenu;
    }

    // =========================================================================
    // Aba: Cardápio
    // =========================================================================
    private JPanel criarPainelCardapio() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Cardápio", JLabel.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        painel.add(titulo, BorderLayout.NORTH);

        // Itens centralizados em labels
        JPanel itens = new JPanel();
        itens.setLayout(new BoxLayout(itens, BoxLayout.Y_AXIS));
        itens.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String[][] cardapio = {
            {"Hambúrguer Clássico", "R$ 15,00", "R$ 22,00", "R$ 30,00"},
            {"X-Bacon",             "R$ 18,00", "R$ 25,00", "R$ 33,00"},
            {"X-Salada",            "R$ 16,00", "R$ 23,00", "R$ 31,00"},
            {"Batata Frita",        "R$ 12,00", "R$ 18,00", "R$ 25,00"},
            {"Refrigerante",        "R$ 8,00",  "R$ 10,00", "R$ 12,00"},
            {"Suco Natural",        "R$ 10,00", "R$ 14,00", "R$ 18,00"},
            {"Milkshake",           "R$ 14,00", "R$ 18,00", "R$ 22,00"},
        };

        for (String[] linha : cardapio) {
            String texto = String.format(
                "<html><center><b>%s</b><br>P: %s &nbsp;|&nbsp; M: %s &nbsp;|&nbsp; G: %s</center></html>",
                linha[0], linha[1], linha[2], linha[3]
            );
            JLabel label = new JLabel(texto, JLabel.CENTER);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
            itens.add(label);
        }

        painel.add(itens, BorderLayout.CENTER);

        // Painel inferior com informações sobre adicionais
        JPanel painelAdicionais = new JPanel(new GridLayout(0, 1, 0, 4));
        painelAdicionais.setBorder(BorderFactory.createTitledBorder("Adicionais disponíveis"));
        painelAdicionais.add(new JLabel("Queijo Extra: +R$ 3,00", JLabel.CENTER));
        painelAdicionais.add(new JLabel("Bacon: +R$ 4,00", JLabel.CENTER));
        painelAdicionais.add(new JLabel("Molho Especial: +R$ 2,50", JLabel.CENTER));
        painelAdicionais.add(new JLabel("Duplo: +R$ 8,00", JLabel.CENTER));
        painelAdicionais.add(new JLabel("Sem Cebola: Grátis", JLabel.CENTER));
        painel.add(painelAdicionais, BorderLayout.SOUTH);

        return painel;
    }

    // =========================================================================
    // Aba: Fazer Pedido
    // =========================================================================
    private JPanel criarPainelPedido() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Fazer Pedido", JLabel.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        painel.add(titulo, BorderLayout.NORTH);

        // --- Formulário de seleção (lado esquerdo) ---
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createTitledBorder("Escolha o item"));

        comboItem = new JComboBox<>(ITENS);
        comboItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboItem.getPreferredSize().height));
        form.add(new JLabel("Item:"));
        form.add(comboItem);
        form.add(Box.createVerticalStrut(10));

        rbPequeno = new JRadioButton("Pequeno");
        rbMedio   = new JRadioButton("Médio", true); // padrão
        rbGrande  = new JRadioButton("Grande");
        ButtonGroup grupoTamanho = new ButtonGroup();
        grupoTamanho.add(rbPequeno);
        grupoTamanho.add(rbMedio);
        grupoTamanho.add(rbGrande);
        JPanel painelTamanho = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelTamanho.add(rbPequeno);
        painelTamanho.add(rbMedio);
        painelTamanho.add(rbGrande);
        form.add(new JLabel("Tamanho:"));
        form.add(painelTamanho);
        form.add(Box.createVerticalStrut(10));

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
        form.add(Box.createVerticalStrut(10));

        txtObs = new JTextField();
        txtObs.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtObs.getPreferredSize().height));
        form.add(new JLabel("Observações:"));
        form.add(txtObs);
        form.add(Box.createVerticalStrut(10));

        JButton btnAdicionar = new JButton("Adicionar ao Pedido");
        btnAdicionar.addActionListener(e -> adicionarItem());
        form.add(btnAdicionar);

        painel.add(form, BorderLayout.CENTER);

        // --- Pedido atual (lado direito) ---
        JPanel painelAtual = new JPanel(new BorderLayout(5, 5));
        painelAtual.setBorder(BorderFactory.createTitledBorder("Pedido Atual"));
        painelAtual.setPreferredSize(new Dimension(280, 0));

        modeloLista = new DefaultListModel<>();
        listaItens  = new JList<>(modeloLista);
        painelAtual.add(new JScrollPane(listaItens), BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new GridLayout(3, 1, 5, 5));
        lblTotal = new JLabel("Total: R$ 0,00");
        JButton btnRemover   = new JButton("Remover Selecionado");
        JButton btnFinalizar = new JButton("Finalizar Pedido");
        btnRemover.addActionListener(e -> removerItem());
        btnFinalizar.addActionListener(e -> finalizarPedido());
        painelBotoes.add(lblTotal);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnFinalizar);
        painelAtual.add(painelBotoes, BorderLayout.SOUTH);

        painel.add(painelAtual, BorderLayout.EAST);

        return painel;
    }

    // =========================================================================
    // Aba: Resumo
    // =========================================================================
    private JPanel criarPainelResumo() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Resumo do Último Pedido", JLabel.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        painel.add(titulo, BorderLayout.NORTH);

        areaResumo = new JTextArea();
        areaResumo.setEditable(false);
        areaResumo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        painel.add(new JScrollPane(areaResumo), BorderLayout.CENTER);

        JPanel painelSul = new JPanel(new GridLayout(2, 1, 5, 5));
        lblTotalResumo = new JLabel("", JLabel.RIGHT);
        lblTotalResumo.setFont(lblTotalResumo.getFont().deriveFont(Font.BOLD, 14f));
        btnNovoPedido = new JButton("Novo Pedido");
        btnNovoPedido.setVisible(false); // aparece só após finalizar
        btnNovoPedido.addActionListener(e -> {
            controller.novoPedido();
            atualizarListaPedido();
            abas.setEnabledAt(2, false);
            btnNovoPedido.setVisible(false);
            abas.setSelectedIndex(1);
        });
        painelSul.add(lblTotalResumo);
        painelSul.add(btnNovoPedido);
        painel.add(painelSul, BorderLayout.SOUTH);

        return painel;
    }

    // =========================================================================
    // Ações dos botões
    // =========================================================================
    private void adicionarItem() {
        String item    = (String) comboItem.getSelectedItem();
        String tamanho = rbPequeno.isSelected() ? "Pequeno" : rbGrande.isSelected() ? "Grande" : "Médio";

        List<String> adicionais = new ArrayList<>();
        if (cbQueijo.isSelected())    adicionais.add("Queijo Extra");
        if (cbBacon.isSelected())     adicionais.add("Bacon");
        if (cbMolho.isSelected())     adicionais.add("Molho Especial");
        if (cbDuplo.isSelected())     adicionais.add("Duplo");
        if (cbSemCebola.isSelected()) adicionais.add("Sem Cebola");

        controller.adicionarItem(item, tamanho, adicionais, txtObs.getText().trim());
        atualizarListaPedido();
    }

    private void removerItem() {
        int indice = listaItens.getSelectedIndex();
        if (indice >= 0) {
            controller.removerItem(indice);
            atualizarListaPedido();
        }
    }

    private void finalizarPedido() {
        if (controller.getItensAtuais().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Adicione ao menos um item antes de finalizar.",
                    "Pedido Vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double total = controller.finalizarPedido();
        JOptionPane.showMessageDialog(this,
                String.format("Pedido finalizado! Total: R$ %.2f", total).replace(".", ","),
                "Pedido Finalizado", JOptionPane.INFORMATION_MESSAGE);

        atualizarListaPedido();
        atualizarResumo();
        btnNovoPedido.setVisible(true);
        abas.setEnabledAt(2, true);
        abas.setSelectedIndex(2); // vai para a aba Resumo automaticamente
    }

    private void atualizarListaPedido() {
        modeloLista.clear();
        for (ItemPedido item : controller.getItensAtuais()) {
            modeloLista.addElement(item.toString());
        }
        lblTotal.setText(
            String.format("Total: R$ %.2f", controller.calcularTotalAtual()).replace(".", ",")
        );
    }

    private void atualizarResumo() {
        List<Pedido> historico = controller.getHistorico();
        if (historico.isEmpty()) return;

        Pedido ultimo = historico.get(historico.size() - 1);
        areaResumo.setText(ultimo.toString());
        lblTotalResumo.setText(
            String.format("Total: R$ %.2f", ultimo.calcularTotal()).replace(".", ",")
        );
    }
}
