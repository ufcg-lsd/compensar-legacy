package springboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import springboot.enums.PermissaoType;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "usuario")
public class Usuario {

	@NotBlank(message = "Nome não pode ser vazio")
	private String nome;

	@NotNull(message = "Idade não pode ser nula")
	private int idade;

	@NotBlank(message = "Nome da instituição não pode ser vazio")
	private String nomeInstituicao;

	@NotBlank(message = "Cargo não pode ser vazio")
	private String cargo;

	@NotBlank(message = "Cidade não pode ser vazia")
	private String cidade;

	@Id
	@Email(message = "E-mail deve ser válido")
	@NotBlank(message = "E-mail não pode ser vazio")
	private String email;

	private boolean ativo;

	private List<PermissaoType> permissoes = new ArrayList<>();

	private List<ModuloCurso> cursoAvaliacao = new ArrayList<>();

	private List<ModuloCurso> cursoCriacao = new ArrayList<>();

	public Usuario(String nome, int idade, String nomeInstituicao, String cargo, String cidade, String email,
			boolean ativo) {
		this.nome = nome;
		this.idade = idade;
		this.nomeInstituicao = nomeInstituicao;
		this.cargo = cargo;
		this.cidade = cidade;
		this.email = email;
		this.ativo = ativo;
		adicionarPermissoesPadrao(email);
	}

	public Usuario() {

	}

	public Usuario(boolean ativo, String cargo, String cidade, String email, int idade, String nome,
			String nomeInstituicao) {
		this.ativo = ativo;
		this.cargo = cargo;
		this.cidade = cidade;
		this.email = email;
		this.idade = idade;
		this.nome = nome;
		this.nomeInstituicao = nomeInstituicao;
	}

	private void adicionarPermissoesPadrao(String email) {
		if (isEmailAdmin(email)) {
			permissoes.add(PermissaoType.ADMIN);
			permissoes.add(PermissaoType.JUDGE);
		}
	}

	private boolean isEmailAdmin(String email) {
		return "joao.luciano@ccc.ufcg.edu.br".equals(email) || "aepc.lacina@gmail.com".equals(email);
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

	public List<ModuloCurso> getCursoCriacao() {
		return cursoCriacao;
	}

	public void setCursoCriacao(List<ModuloCurso> cursoCriacao) {
		this.cursoCriacao = cursoCriacao;
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
		return "Usuario [nome=".concat(nome).concat(", nomeInstituicao=").concat(nomeInstituicao).concat(", email=")
				.concat(email).concat("]");
	}
}
