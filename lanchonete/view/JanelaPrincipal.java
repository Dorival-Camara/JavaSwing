/**
 * Janela principal da lanchonete.
 * Contém as três abas (Cardápio, Fazer Pedido, Resumo) e o menu superior.
 */
package lanchonete.view;

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
    private JTextArea areaItens;
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
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("<html><b><font size='5'>Cardápio</font></b></html>");
        titulo.setAlignmentX(0.5f);
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(10));

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
            JLabel label = new JLabel(String.format(
                "<html><b>%s</b> &nbsp;—&nbsp; P: %s | M: %s | G: %s</html>",
                linha[0], linha[1], linha[2], linha[3]
            ));
            label.setAlignmentX(0.0f);
            painel.add(label);
            painel.add(Box.createVerticalStrut(6));
        }

        painel.add(Box.createVerticalStrut(10));

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
    private JPanel criarPainelPedido() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("<html><b><font size='4'>Fazer Pedido</font></b></html>");
        titulo.setAlignmentX(0.5f);
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(8));

        // Painel horizontal: formulário (esquerda) e pedido atual (direita)
        JPanel linha = new JPanel();
        linha.setLayout(new BoxLayout(linha, BoxLayout.X_AXIS));
        linha.add(criarFormulario());
        linha.add(Box.createHorizontalStrut(10));
        linha.add(criarPainelAtual());
        painel.add(linha);

        return painel;
    }

    private JPanel criarFormulario() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createTitledBorder("Escolha o item"));

        comboItem = new JComboBox<>(ITENS);
        form.add(new JLabel("Item:"));
        form.add(comboItem);
        form.add(Box.createVerticalStrut(8));

        rbPequeno = new JRadioButton("Pequeno");
        rbMedio   = new JRadioButton("Médio", true); // padrão
        rbGrande  = new JRadioButton("Grande");
        ButtonGroup grupoTamanho = new ButtonGroup();
        grupoTamanho.add(rbPequeno);
        grupoTamanho.add(rbMedio);
        grupoTamanho.add(rbGrande);
        JPanel painelTamanho = new JPanel();
        painelTamanho.setLayout(new BoxLayout(painelTamanho, BoxLayout.X_AXIS));
        painelTamanho.add(rbPequeno);
        painelTamanho.add(rbMedio);
        painelTamanho.add(rbGrande);
        form.add(new JLabel("Tamanho:"));
        form.add(painelTamanho);
        form.add(Box.createVerticalStrut(8));

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

        txtObs = new JTextField(20);
        form.add(new JLabel("Observações:"));
        form.add(txtObs);
        form.add(Box.createVerticalStrut(8));

        JButton btnAdicionar = new JButton("Adicionar ao Pedido");
        btnAdicionar.addActionListener(e -> adicionarItem());
        form.add(btnAdicionar);

        return form;
    }

    private JPanel criarPainelAtual() {
        JPanel painelAtual = new JPanel();
        painelAtual.setLayout(new BoxLayout(painelAtual, BoxLayout.Y_AXIS));
        painelAtual.setBorder(BorderFactory.createTitledBorder("Pedido Atual"));

        areaItens = new JTextArea();
        areaItens.setEditable(false);
        painelAtual.add(new JScrollPane(areaItens));
        painelAtual.add(Box.createVerticalStrut(5));

        lblTotal = new JLabel("Total: R$ 0,00");
        JButton btnRemover   = new JButton("Remover Último");
        JButton btnFinalizar = new JButton("Finalizar Pedido");
        btnRemover.addActionListener(e -> removerItem());
        btnFinalizar.addActionListener(e -> finalizarPedido());
        painelAtual.add(lblTotal);
        painelAtual.add(btnRemover);
        painelAtual.add(btnFinalizar);

        return painelAtual;
    }

    // =========================================================================
    // Aba: Resumo
    // =========================================================================
    private JPanel criarPainelResumo() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("<html><b><font size='4'>Resumo do Último Pedido</font></b></html>");
        titulo.setAlignmentX(0.5f);
        painel.add(titulo);
        painel.add(Box.createVerticalStrut(10));

        areaResumo = new JTextArea();
        areaResumo.setEditable(false);
        painel.add(new JScrollPane(areaResumo));
        painel.add(Box.createVerticalStrut(5));

        lblTotalResumo = new JLabel("");
        btnNovoPedido = new JButton("Novo Pedido");
        btnNovoPedido.setVisible(false); // aparece só após finalizar
        btnNovoPedido.addActionListener(e -> {
            controller.novoPedido();
            atualizarListaPedido();
            abas.setEnabledAt(2, false);
            btnNovoPedido.setVisible(false);
            abas.setSelectedIndex(1);
        });
        painel.add(lblTotalResumo);
        painel.add(btnNovoPedido);

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
        List<ItemPedido> itens = controller.getItensAtuais();
        if (!itens.isEmpty()) {
            controller.removerItem(itens.size() - 1);
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
        StringBuilder sb = new StringBuilder();
        for (ItemPedido item : controller.getItensAtuais()) {
            sb.append(item.toString()).append("\n");
        }
        areaItens.setText(sb.toString());
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
            String.format("<html><b>Total: R$ %.2f</b></html>", ultimo.calcularTotal()).replace(".", ",")
        );
    }
}
