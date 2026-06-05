/**
 * Aqui é onde inicia a primeira tela do sistema
 */

package lanchonete.view;

import javax.swing.*;
import lanchonete.controller.PedidoController;
import lanchonete.model.Pedido;

public class JanelaPrincipal {

    public static void main(String[] args) {
        // SwingUtilities.invokeLater garante que a janela seja criada na thread certa do Swing
        SwingUtilities.invokeLater(() -> {
            PedidoController controller = new PedidoController();

            //Cria a janela principal
            JFrame janela = new JFrame("Lanchonete");
            janela.setSize(800, 600);
            janela.setResizable(false);
            //Ele sai do programa
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //Cria as abas e as janelas de cada uma
            JTabbedPane abas = new JTabbedPane();
            JPanel painelCardapio = new JPanel();
            JPanel painelPedido = new JPanel();
            JPanel painelResumo = new JPanel();

            //Heitor, vai ser aqui que você vai preencher as paginas com a sua parte, você vai substituir essas treis linhas pelo o conteudo das paginas, isso aqui é só temporario e para vc saber aonde é para colocar a sua parte
            painelCardapio.add(new JLabel("Cardápio aqui"));
            painelPedido.add(new JLabel("Pedido aqui"));
            painelResumo.add(new JLabel("Resumo aqui"));

            //Coloca nomes em cada uma
            abas.addTab("Cardápio", painelCardapio);
            abas.addTab("Fazer Pedido", painelPedido);
            abas.addTab("Resumo", painelResumo);

            //Bloqueia a aba "resumos" até ter algum resumo
            abas.setEnabledAt(2, false); // Heitor aqui é outra parte importante, você vai fazer o sistema de quando estiver sem pedidos, ela se mantem cinza, a unica coisa que precisa fazer é chamar abas.setEnabledAt(2, true) para liberar quando tiver pedidos

            //Cria uma barra de menu e adiciona "Novo pedido" nela
            JMenuBar barraMenu = new JMenuBar();
            JMenu menuNovoPedido = new JMenu("Novo Pedido");
            JMenuItem itemNovoPedido = new JMenuItem("Iniciar novo pedido");

            //Cria a função de clique e confirmação da opção "Novo pedido"
            itemNovoPedido.addActionListener(e -> {
                int r = JOptionPane.showConfirmDialog(janela,
                        "Isso vai apagar o pedido atual. Continuar?",
                        "Novo Pedido", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) {
                    controller.novoPedido();
                    abas.setEnabledAt(2, false);
                    abas.setSelectedIndex(1);
                }
            });
            menuNovoPedido.add(itemNovoPedido);

            //Cria opção historico
            JMenu menuHistorico = new JMenu("Historico");
            JMenuItem itemHistorico = new JMenuItem("Ver pedidos finalizados");

            //Verifica se existe algum pedido e se não tiver, coloca mensagem de erro
            itemHistorico.addActionListener(e -> {
                if (controller.getHistorico().isEmpty()) {
                    JOptionPane.showMessageDialog(janela,
                            "Nenhum pedido finalizado ainda.",
                            "Histórico", JOptionPane.INFORMATION_MESSAGE);
                //Se tiver historico conta os numeros dos pedidos
                } else {
                    StringBuilder sb = new StringBuilder();
                    int num = 1;
                    for (Pedido pedido : controller.getHistorico()) {
                        sb.append("Pedido ").append(num++).append(":\n");
                        sb.append(pedido.toString()).append("\n");
                    //Faz barra de escrolagem e area de texto
                    }
                    JTextArea areaTexto = new JTextArea(sb.toString());
                    areaTexto.setEditable(false);
                    JOptionPane.showMessageDialog(janela,
                            new JScrollPane(areaTexto),
                            "Histórico de Pedidos", JOptionPane.PLAIN_MESSAGE);
                }
            });
            menuHistorico.add(itemHistorico);

            //Faz o menu de sair do programa, e da o aviso de confirmação
            JMenu menuSair = new JMenu("Sair");
            JMenuItem itemSair = new JMenuItem("Fechar programa");
            itemSair.addActionListener(e -> {
                int r = JOptionPane.showConfirmDialog(janela, "Confirmar fechamento?",
                        "Sair", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) System.exit(0);
            });
            menuSair.add(itemSair);

            //Adiciona os itens a barra
            barraMenu.add(menuNovoPedido);
            barraMenu.add(menuHistorico);
            barraMenu.add(menuSair);

            janela.setJMenuBar(barraMenu);
            janela.add(abas);
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
        });
    }
}
