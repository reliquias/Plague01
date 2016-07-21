package br.com.jaque.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.com.jaque.util.BaseEntity;
import br.com.jaque.util.OrigemProduto;

@Entity
public class ItemDoacao implements Serializable, BaseEntity {

	private static final long serialVersionUID = 2471765773806278661L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
	private Produto produto;
	@ManyToOne
	private Doacao doacao;
	@Enumerated(EnumType.STRING)
    @Column(name="ORIGEM_PRODUTO")
    private OrigemProduto origemProduto;
	@Column(name="PESO_UNITARIO")
	private BigDecimal pesoUnitario;
	private String observacao;
	

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

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public OrigemProduto getOrigemProduto() {
		return origemProduto;
	}

	public void setOrigemProduto(OrigemProduto origemProduto) {
		this.origemProduto = origemProduto;
	}

	public BigDecimal getPesoUnitario() {
		return pesoUnitario;
	}

	public void setPesoUnitario(BigDecimal pesoUnitario) {
		this.pesoUnitario = pesoUnitario;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Doacao getDoacao() {
		return doacao;
	}

	public void setDoacao(Doacao doacao) {
		this.doacao = doacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((doacao == null) ? 0 : doacao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
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
		ItemDoacao other = (ItemDoacao) obj;
		if (doacao == null) {
			if (other.doacao != null)
				return false;
		} else if (!doacao.equals(other.doacao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		return true;
	}
}
