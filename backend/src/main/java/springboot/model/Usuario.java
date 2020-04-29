package springboot.model;


import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import springboot.enums.PermissaoType;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "usuario")
public class Usuario {

    @NotNull
    @TextIndexed
	private String nome;
    
    @NotNull
    @TextIndexed
	private int idade;

    @NotNull
    @TextIndexed
	private String nomeInstituicao;
    
    @NotNull
    @TextIndexed
	private String cargo;
    
    @NotNull
    @TextIndexed
	private String cidade;

    @NotNull
    @TextIndexed
	@Id	
	private String email;

    @NotNull
    @TextIndexed
	private boolean ativo;

	@NotNull
	@TextIndexed
	private List<PermissaoType> permissoes;

	@NotNull
	private List<ModuloCurso> cursoAvaliacao;

	public Usuario(String nome, int idade, String nomeInstituicao, String cargo, String cidade, String email, boolean ativo) {
		this.nome = nome;
		this.idade = idade;
		this.nomeInstituicao = nomeInstituicao;
		this.cargo = cargo;
		this.cidade = cidade;
		this.email = email;
		this.ativo = ativo;
		this.permissoes = new ArrayList<>();
		if (email.equals("joao.medeiros@ccc.ufcg.edu.br") || email.equals("joao.medeiros@ccc.ufcg.edu.br")) {
			permissoes.add(PermissaoType.ADMIN);
			permissoes.add(PermissaoType.JUDGE);
		}
		this.cursoAvaliacao = new ArrayList<>();
	}

	public Usuario() {

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
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

	public String getNomeInstituicao() {
		return nomeInstituicao;
	}

	public void setNomeInstituicao(String nomeInstituicao) {
		this.nomeInstituicao = nomeInstituicao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<PermissaoType> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<PermissaoType> permissoes) {
		this.permissoes = permissoes;
	}

	public List<ModuloCurso> getCursoAvaliacao() {
		return cursoAvaliacao;
	}

	public void setCursoAvaliacao(List<ModuloCurso> cursoAvaliacao) {
		this.cursoAvaliacao = cursoAvaliacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Usuario [nomeCompleto=" + nome + ", nomeInstituicao=" + nomeInstituicao + ", email=" + email + "]";
	}

}
