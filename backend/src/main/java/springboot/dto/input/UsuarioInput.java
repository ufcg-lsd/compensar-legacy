package springboot.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioInput {

    @JsonProperty("cargo")
    String cargo;

    @JsonProperty("cidade")
    String cidade;

    @JsonProperty("idade")
    int idade;

    @JsonProperty("nomeInstituicao")
    String nomeInstituicao;

    public UsuarioInput( String cargo, String cidade, int idade, String nomeInstituicao) {
        this.cargo = cargo;
        this.cidade = cidade;
        this.idade = idade;
        this.nomeInstituicao = nomeInstituicao;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getNomeInstituicao() {
        return nomeInstituicao;
    }

    public void setNomeInstituicao(String nomeInstituicao) {
        this.nomeInstituicao = nomeInstituicao;
    }
}
