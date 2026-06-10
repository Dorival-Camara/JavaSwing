/**
 * Representa um item individual dentro de um pedido.
 * Funciona como a "ficha" de um produto: guarda o nome, tamanho,
 * adicionais escolhidos e observações, e sabe calcular o próprio preço.
 */
package lanchonete.model;

import java.util.List; // necessário para guardar a lista de adicionais escolhidos

public class ItemPedido {

    // dados do item escolhido pelo cliente
    private String item;              // nome do produto (ex: "X-Bacon")
    private String tamanho;           // tamanho escolhido: Pequeno, Médio ou Grande
    private List<String> adicionais;  // lista de adicionais marcados (ex: ["Bacon", "Queijo Extra"])
    private String observacoes;       // texto livre digitado pelo cliente (ex: "sem sal")

    // construtor: recebe todos os dados e os guarda nos campos acima
    public ItemPedido(String item, String tamanho, List<String> adicionais, String observacoes) {
        this.item = item;
        this.tamanho = tamanho;
        this.adicionais = adicionais;
        this.observacoes = observacoes;
    }

    // métodos get: servem para que outras classes possam ler os dados deste item
    public String getItem() { return item; }
    public String getTamanho() { return tamanho; }
    public List<String> getAdicionais() { return adicionais; }
    public String getObservacoes() { return observacoes; }

    /**
     * Consulta o preço base do item de acordo com o nome e o tamanho escolhido.
     * Cada combinação de produto + tamanho tem um valor fixo definido aqui.
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
            default: // item desconhecido usa os valores do Hambúrguer Clássico
                switch (tamanho) {
                    case "Pequeno": return 15.00;
                    case "Grande":  return 30.00;
                    default:        return 22.00; // Médio
                }
        }
    }

    /**
     * Percorre a lista de adicionais e soma o valor de cada um.
     * "Sem Cebola" é grátis, por isso não aparece no switch.
     */
    public double getValorAdicionais() {
        double valor = 0;
        if (adicionais != null) { // evita erro caso a lista seja nula
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
     * Retorna o preço total do item: valor base + soma dos adicionais.
     */
    public double getValorTotal() {
        return getValorBase() + getValorAdicionais();
    }

    /**
     * Formata o item em texto para exibir na tela e no resumo do pedido.
     * Exemplo de saída: "X-Bacon (Médio) + Queijo Extra [Obs: sem sal] - R$ 28,00"
     */
    @Override
    public String toString() {
        String texto = item + " (" + tamanho + ")";

        // só adiciona os adicionais se algum foi marcado
        if (adicionais != null && !adicionais.isEmpty()) {
            texto = texto + " + " + String.join(", ", adicionais);
        }

        // só adiciona observação se o cliente digitou alguma coisa
        if (observacoes != null && !observacoes.isEmpty()) {
            texto = texto + " [Obs: " + observacoes + "]";
        }

        texto = texto + " - R$ " + String.format("%.2f", getValorTotal());

        return texto;
    }
}
