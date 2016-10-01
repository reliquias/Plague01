package br.com.inicial.modelo;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import br.com.inicial.interfaces.BaseEntity;

@Entity
public class Vistoria implements BaseEntity, Serializable {

	private static final long serialVersionUID = 2471765773806278661L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String armazenamento;
	private String coordenadas;
	private Calendar dataVistoria;
	private String status;
	private String responsavel;
	@Lob
    @Column(name = "IMAGEM_1")
    private byte[] imagem1;
	
	@ManyToOne
	private TipoDoenca praga;
	@ManyToOne
	private PlantaTalhao plantaTalhao;
	
    @Override
    public Integer getIdi() {
        return new Integer(id);
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getArmazenamento() {
		return armazenamento;
	}

	public void setArmazenamento(String armazenamento) {
		this.armazenamento = armazenamento;
	}

	public String getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(String coordenadas) {
		this.coordenadas = coordenadas;
	}

	public Calendar getDataVistoria() {
		return dataVistoria;
	}

	public void setDataVistoria(Calendar dataVistoria) {
		this.dataVistoria = dataVistoria;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TipoDoenca getPraga() {
		return praga;
	}

	public void setPraga(TipoDoenca praga) {
		this.praga = praga;
	}

	public PlantaTalhao getPlantaTalhao() {
		return plantaTalhao;
	}

	public void setPlantaTalhao(PlantaTalhao plantaTalhao) {
		this.plantaTalhao = plantaTalhao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((plantaTalhao == null) ? 0 : plantaTalhao.hashCode());
		result = prime * result + ((praga == null) ? 0 : praga.hashCode());
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
		Vistoria other = (Vistoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (plantaTalhao == null) {
			if (other.plantaTalhao != null)
				return false;
		} else if (!plantaTalhao.equals(other.plantaTalhao))
			return false;
		if (praga == null) {
			if (other.praga != null)
				return false;
		} else if (!praga.equals(other.praga))
			return false;
		return true;
	}

	public byte[] getImagem1() {
		return imagem1;
	}

	public void setImagem1(byte[] imagem1) {
		this.imagem1 = imagem1;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	@Override
	public String toString() {
		return armazenamento + ", "+ status + ", " + responsavel;
	}
	
	
}
