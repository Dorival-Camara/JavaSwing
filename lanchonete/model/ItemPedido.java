/**
 * Representa um item do pedido na lanchonete
 */

package lanchonete.model;

import java.util.List;

public class ItemPedido {
    private String item;
    private String tamanho;
    private List<String> adicionais;
    private String observacoes;

    public ItemPedido(String item, String tamanho, List<String> adicionais, String observacoes) {
        this.item = item;
        this.tamanho = tamanho;
        this.adicionais = adicionais;
        this.observacoes = observacoes;
    }

    public String getItem() { return item; }
    public String getTamanho() { return tamanho; }
    public List<String> getAdicionais() { return adicionais; }
    public String getObservacoes() { return observacoes; }

    /**
     * Calcula o valor base do item de acordo com o tamanho
     */
    public double getValorBase() {
        switch (item) {
            case "Hambúrguer Clássico":
                switch (tamanho) { case "Pequeno": return 15.00; case "Grande": return 30.00; default: return 22.00; }
            case "X-Bacon":
                switch (tamanho) { case "Pequeno": return 18.00; case "Grande": return 33.00; default: return 25.00; }
            case "X-Salada":
                switch (tamanho) { case "Pequeno": return 16.00; case "Grande": return 31.00; default: return 23.00; }
            case "Batata Frita":
                switch (tamanho) { case "Pequeno": return 12.00; case "Grande": return 25.00; default: return 18.00; }
            case "Refrigerante":
                switch (tamanho) { case "Pequeno": return 8.00; case "Grande": return 12.00; default: return 10.00; }
            case "Suco Natural":
                switch (tamanho) { case "Pequeno": return 10.00; case "Grande": return 18.00; default: return 14.00; }
            case "Milkshake":
                switch (tamanho) { case "Pequeno": return 14.00; case "Grande": return 22.00; default: return 18.00; }
            default:
                switch (tamanho) { case "Pequeno": return 15.00; case "Grande": return 30.00; default: return 22.00; }
        }
    }

    /**
     * Calcula o valor dos adicionais
     */
    public double getValorAdicionais() {
        double valor = 0;
        if (adicionais != null) {
            for (String adicional : adicionais) {
                switch (adicional) {
                    case "Queijo Extra": valor += 3.00; break;
                    case "Bacon": valor += 4.00; break;
                    case "Sem Cebola": valor += 0.00; break;
                    case "Molho Especial": valor += 2.50; break;
                    case "Duplo": valor += 8.00; break;
                }
            }
        }
        return valor;
    }

    /**
     * Retorna o valor total do item
     */
    public double getValorTotal() {
        return getValorBase() + getValorAdicionais();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(item).append(" (").append(tamanho).append(")");
        if (adicionais != null && !adicionais.isEmpty()) {
            sb.append(" + ").append(String.join(", ", adicionais));
        }
        if (observacoes != null && !observacoes.isEmpty()) {
            sb.append(" [Obs: ").append(observacoes).append("]");
        }
        sb.append(" - R$ ").append(String.format("%.2f", getValorTotal()));
        return sb.toString();
    }
}