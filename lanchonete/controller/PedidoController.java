/**
 * Recebe os dados que o usuário escolheu na tela (View),
 * trata esses dados e cria os objetos do Model (ItemPedido, Pedido).
 * É o intermediário entre a tela e os dados — nenhum dos dois se fala diretamente.
 */
package lanchonete.controller;

import lanchonete.model.ItemPedido; // pegamos a "ficha modelo" de cada item do pedido
import lanchonete.model.Pedido;     // pegamos a "ficha modelo" do pedido completo
import java.util.ArrayList; // ArrayList é uma lista que cresce conforme adicionamos itens, sem tamanho fixo
import java.util.List;      // necessário para declarar o tipo das listas

public class PedidoController {

    // lista que guarda todos os pedidos finalizados durante o uso do programa
    // funciona como um histórico que vai crescendo a cada pedido concluído
    private List<Pedido> pedidos = new ArrayList<>();

    // pedido que está sendo montado no momento pelo cliente
    private Pedido pedidoAtual = new Pedido();

    /**
     * Recebe os dados escolhidos pelo cliente na tela, valida se estão preenchidos
     * e adiciona o item ao pedido atual.
     * Retorna true se adicionou com sucesso, ou false se algum dado estava vazio.
     */
    public boolean adicionarItem(String item, String tamanho, List<String> adicionais, String observacoes) {
        // verifica se os campos obrigatórios foram preenchidos antes de criar o item
        if (item == null || item.isEmpty() || tamanho == null || tamanho.isEmpty()) {
            return false;
        }

        // cria o objeto ItemPedido com os dados recebidos e adiciona ao pedido atual
        ItemPedido itemPedido = new ItemPedido(item, tamanho, adicionais, observacoes);
        pedidoAtual.adicionarItem(itemPedido);
        return true;
    }

    /**
     * Remove um item do pedido atual pelo índice (posição na lista).
     * Retorna true se removeu com sucesso, ou false se o índice não existia.
     */
    public boolean removerItem(int indice) {
        return pedidoAtual.removerItem(indice);
    }

    /**
     * Finaliza o pedido atual: salva no histórico e cria um novo pedido vazio.
     * Retorna o valor total do pedido finalizado.
     */
    public double finalizarPedido() {
        if (pedidoAtual.getItens().isEmpty()) {
            return -1; // sinal de erro: pedido vazio, não pode finalizar
        }
        double total = pedidoAtual.calcularTotal();
        pedidos.add(pedidoAtual);   // salva o pedido no histórico
        pedidoAtual = new Pedido(); // cria um novo pedido vazio para o próximo cliente
        return total;
    }

    /**
     * Calcula e retorna o total do pedido que está sendo montado agora.
     */
    public double calcularTotalAtual() {
        return pedidoAtual.calcularTotal();
    }

    /**
     * Retorna a lista de itens do pedido atual para a tela exibir.
     */
    public List<ItemPedido> getItensAtuais() {
        return pedidoAtual.getItens();
    }

    /**
     * Descarta o pedido atual e começa um novo do zero.
     * Usado quando o cliente clica em "Novo Pedido" na tela de resumo.
     */
    public void novoPedido() {
        pedidoAtual = new Pedido();
    }

    /**
     * Retorna o histórico com todos os pedidos finalizados.
     * Usado pela tela de resumo para exibir o último pedido.
     */
    public List<Pedido> getHistorico() {
        return pedidos;
    }
}
