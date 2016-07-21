package br.com.jaque.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

import br.com.jaque.util.BaseEntity;

@Entity
public class Doacao implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Integer id;
	private String numeroNf;
	@Temporal(TemporalType.DATE)
	private Date data;
	@SuppressWarnings("deprecation")
	@ManyToOne
	@ForeignKey(name = "fk_doacao_instituicao")
	private Instituicao instituicao;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public Integer getIdi() {
		return new Integer(id);
	}
	public String getNumeroNf() {
		return numeroNf;
	}
	public void setNumeroNf(String numeroNf) {
		this.numeroNf = numeroNf;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Instituicao getInstituicao() {
		return instituicao;
	}
	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((instituicao == null) ? 0 : instituicao.hashCode());
		result = prime * result
				+ ((numeroNf == null) ? 0 : numeroNf.hashCode());
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
		Doacao other = (Doacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (instituicao == null) {
			if (other.instituicao != null)
				return false;
		} else if (!instituicao.equals(other.instituicao))
			return false;
		if (numeroNf == null) {
			if (other.numeroNf != null)
				return false;
		} else if (!numeroNf.equals(other.numeroNf))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Doacoes [numeroNf=" + numeroNf + ", instituicao=" + instituicao
				+ "]";
	}
}
