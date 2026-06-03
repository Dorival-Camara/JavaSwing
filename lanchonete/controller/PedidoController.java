/**
 * Aqui ele receberá a ficha modelo do ItemPedido,
 * irá receber também os dados que o usuário digitou no view,
 * após isso ele irá tratar os dados e criar o objeto que será o item x
 */
package lanchonete.controller;

import lanchonete.model.ItemPedido;
import lanchonete.model.Pedido;
import java.util.ArrayList;
import java.util.List;

public class PedidoController {

    // lista que guarda todos os pedidos cadastrados durante o uso do programa
    // funciona como um arquivo que vai crescendo conforme novos pedidos são feitos
    private List<Pedido> pedidos = new ArrayList<>();

    // pedido atual que está sendo montado
    private Pedido pedidoAtual = new Pedido();

    /**
     * Adiciona um item ao pedido atual
     */
    public boolean adicionarItem(String item, String tamanho, List<String> adicionais, String observacoes) {
        if (item == null || item.isEmpty() || tamanho == null || tamanho.isEmpty()) {
            return false;
        }

        ItemPedido itemPedido = new ItemPedido(item, tamanho, adicionais, observacoes);
        pedidoAtual.adicionarItem(itemPedido);
        return true;
    }

    /**
     * Remove um item do pedido atual pelo índice
     */
    public boolean removerItem(int indice) {
        return pedidoAtual.removerItem(indice);
    }

    /**
     * Finaliza o pedido atual e guarda no histórico
     */
    public double finalizarPedido() {
        if (pedidoAtual.getItens().isEmpty()) {
            return -1;
        }
        double total = pedidoAtual.calcularTotal();
        pedidos.add(pedidoAtual);
        pedidoAtual = new Pedido();
        return total;
    }

    /**
     * Calcula o total do pedido atual
     */
    public double calcularTotalAtual() {
        return pedidoAtual.calcularTotal();
    }

    /**
     * Retorna os itens do pedido atual
     */
    public List<ItemPedido> getItensAtuais() {
        return pedidoAtual.getItens();
    }

    /**
     * Limpa o pedido atual (Novo Pedido)
     */
    public void novoPedido() {
        pedidoAtual = new Pedido();
    }

    /**
     * Retorna o histórico de todos os pedidos
     */
    public List<Pedido> getHistorico() {
        return pedidos;
    }

}