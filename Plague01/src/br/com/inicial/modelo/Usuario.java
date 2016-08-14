package br.com.inicial.modelo;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import br.com.inicial.util.Utils;

@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 2471765773806278661L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	private String login;
	private String senha;
	/*private String email;
	
	private Date nascimento;
	private String celular;
	private String idioma;*/
	private boolean ativo;
	@ElementCollection(targetClass=String.class)
	@JoinTable(name="usuario_permissao",uniqueConstraints={@UniqueConstraint(columnNames={"usuario", "permissao"})},joinColumns=@JoinColumn(name="usuario"))
	@Column(name="permissao", length=50)
	private Set<String> permissao = new HashSet<String>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Set<String> getPermissao() {
		return permissao;
	}

	public void setPermissao(Set<String> permissao) {
		this.permissao = permissao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ativo ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((permissao == null) ? 0 : permissao.hashCode());
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
		if (ativo != other.ativo)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (permissao == null) {
			if (other.permissao != null)
				return false;
		} else if (!permissao.equals(other.permissao))
			return false;
		return true;
	}

	public String getLogin() {
		return Utils.substituiNuloPorString(login);
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return Utils.substituiNuloPorString(senha);
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
