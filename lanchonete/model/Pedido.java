/**
 * Representa um pedido completo — funciona como o "carrinho" da lanchonete.
 * Guarda todos os itens escolhidos pelo cliente e sabe calcular o total.
 */
package lanchonete.model;

import java.util.ArrayList; // ArrayList é uma lista que cresce conforme adicionamos itens, sem tamanho fixo
import java.util.List;      // necessário para declarar o tipo da lista de itens

public class Pedido {

    // lista que guarda todos os itens adicionados neste pedido
    private List<ItemPedido> itens;

    // construtor: cria o pedido já com a lista vazia, pronta para receber itens
    public Pedido() {
        this.itens = new ArrayList<>();
    }

    /**
     * Adiciona um item ao final da lista do pedido.
     */
    public void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    /**
     * Remove um item pelo índice (posição na lista).
     * Retorna true se removeu com sucesso, ou false se o índice não existia.
     */
    public boolean removerItem(int indice) {
        // verifica se o índice está dentro dos limites da lista antes de remover
        if (indice >= 0 && indice < itens.size()) {
            itens.remove(indice);
            return true;
        }
        return false;
    }

    /**
     * Soma o valor total de todos os itens do pedido.
     */
    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getValorTotal(); // pede o preço para cada item e vai somando
        }
        return total;
    }

    // retorna a lista de itens para que outras classes possam lê-la
    public List<ItemPedido> getItens() {
        return itens;
    }

    /**
     * Formata o pedido completo em texto para exibir no resumo.
     * Exemplo de saída:
     *   === PEDIDO ===
     *   1. X-Bacon (Médio) - R$ 25,00
     *   TOTAL: R$ 25,00
     */
    @Override
    public String toString() {
        String texto = "=== PEDIDO ===\n";

        // percorre a lista numerando cada item (i+1 porque o índice começa em 0)
        for (int i = 0; i < itens.size(); i++) {
            texto = texto + (i + 1) + ". " + itens.get(i).toString() + "\n";
        }

        texto = texto + "TOTAL: R$ " + String.format("%.2f", calcularTotal()) + "\n";

        return texto;
    }
}
