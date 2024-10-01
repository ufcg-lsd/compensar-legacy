package springboot.model;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Conteudo {
    @Id
    private final String nome;

    public Conteudo(@JsonProperty("nome") String nome) {
        this.nome = nome;
    }

    @JsonProperty("nome")
    public String getNome() {
        return nome;
    }
}
