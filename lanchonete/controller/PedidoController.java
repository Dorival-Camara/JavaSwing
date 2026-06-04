/**
 * Recebe os dados que o usuário digitou na View,
 * trata esses dados e cria os objetos do Model (ItemPedido, Pedido)
 */
package lanchonete.controller;

import lanchonete.model.ItemPedido;
import lanchonete.model.Pedido;
import java.util.ArrayList;
import java.util.List;

public class PedidoController {

    // lista que guarda todos os pedidos finalizados durante o uso do programa
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
            return -1; // sinal de erro: pedido vazio
        }
        double total = pedidoAtual.calcularTotal();
        pedidos.add(pedidoAtual);   // salva no histórico
        pedidoAtual = new Pedido(); // começa um novo pedido vazio
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
     * Retorna o histórico de todos os pedidos finalizados
     */
    public List<Pedido> getHistorico() {
        return pedidos;
    }

}
