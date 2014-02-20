/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pretextexecutor;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class Parametro {
    private String nome;
    private String valor;

    public Parametro(String nome, String valor) {
        this.nome = nome;
        this.valor = valor;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    
    
}
