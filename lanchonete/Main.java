/**
 * Ponto de entrada do programa.
 * Toda aplicação Java precisa de um método main() para começar a rodar.
 * Aqui a única função é chamar a JanelaPrincipal para abrir a tela.
 */
package lanchonete;

import lanchonete.view.JanelaPrincipal; // importa a tela principal para poder abri-la

public class Main {
    public static void main(String[] args) {
        JanelaPrincipal.main(args); // inicia a janela principal do programa
    }
}
