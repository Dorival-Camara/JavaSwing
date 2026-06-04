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
                switch (tamanho) {
                    case "Pequeno": return 15.00;
                    case "Grande":  return 30.00;
                    default:        return 22.00; // Médio
                }
            case "X-Bacon":
                switch (tamanho) {
                    case "Pequeno": return 18.00;
                    case "Grande":  return 33.00;
                    default:        return 25.00; // Médio
                }
            case "X-Salada":
                switch (tamanho) {
                    case "Pequeno": return 16.00;
                    case "Grande":  return 31.00;
                    default:        return 23.00; // Médio
                }
            case "Batata Frita":
                switch (tamanho) {
                    case "Pequeno": return 12.00;
                    case "Grande":  return 25.00;
                    default:        return 18.00; // Médio
                }
            case "Refrigerante":
                switch (tamanho) {
                    case "Pequeno": return 8.00;
                    case "Grande":  return 12.00;
                    default:        return 10.00; // Médio
                }
            case "Suco Natural":
                switch (tamanho) {
                    case "Pequeno": return 10.00;
                    case "Grande":  return 18.00;
                    default:        return 14.00; // Médio
                }
            case "Milkshake":
                switch (tamanho) {
                    case "Pequeno": return 14.00;
                    case "Grande":  return 22.00;
                    default:        return 18.00; // Médio
                }
            default:
                switch (tamanho) {
                    case "Pequeno": return 15.00;
                    case "Grande":  return 30.00;
                    default:        return 22.00; // Médio
                }
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
                    case "Queijo Extra":   valor += 3.00; break;
                    case "Bacon":          valor += 4.00; break;
                    case "Molho Especial": valor += 2.50; break;
                    case "Duplo":          valor += 8.00; break;
                    // "Sem Cebola" é grátis, não soma nada
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
        String texto = item + " (" + tamanho + ")";

        if (adicionais != null && !adicionais.isEmpty()) {
            texto = texto + " + " + String.join(", ", adicionais);
        }

        if (observacoes != null && !observacoes.isEmpty()) {
            texto = texto + " [Obs: " + observacoes + "]";
        }

        texto = texto + " - R$ " + String.format("%.2f", getValorTotal());

        return texto;
    }
}
