/**
 * Representa um pedido completo na lanchonete
 */

package lanchonete.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private List<ItemPedido> itens;

    public Pedido() {
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    public boolean removerItem(int indice) {
        if (indice >= 0 && indice < itens.size()) {
            itens.remove(indice);
            return true;
        }
        return false;
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getValorTotal();
        }
        return total;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    @Override
    public String toString() {
        String texto = "=== PEDIDO ===\n";

        for (int i = 0; i < itens.size(); i++) {
            texto = texto + (i + 1) + ". " + itens.get(i).toString() + "\n";
        }

        texto = texto + "TOTAL: R$ " + String.format("%.2f", calcularTotal()) + "\n";

        return texto;
    }
}
