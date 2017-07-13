package br.com.inicial.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.com.inicial.interfaces.BaseEntity;

@Entity
public class BoletimApontamento implements BaseEntity, Serializable {

	private static final long serialVersionUID = 2471765773806278661L;
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	private long idFirebase;
    private long checkListId;
    private long talhao;
    private long codigoAtividade;
    private long numeroLaudo;
    private long mecanicoId;
    private String nomeAtividade;
    private String dataHoraCadastro;
    private String horimetro;
    private String odometro;
    private String producao;
    private String cto;

    private String responsavel;

    private String dataHoraUpload;

	@ManyToOne
	private BoletimDiario boletimDiario;
	
	@Override
    public Integer getIdi() {
        return new Integer(id);
    }

	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public BoletimDiario getBoletimDiario() {
		return boletimDiario;
	}

	public void setBoletimDiario(BoletimDiario boletimDiario) {
		this.boletimDiario = boletimDiario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BoletimApontamento other = (BoletimApontamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public long getIdFirebase() {
		return idFirebase;
	}

	public void setIdFirebase(long idFirebase) {
		this.idFirebase = idFirebase;
	}

	public long getCheckListId() {
		return checkListId;
	}

	public void setCheckListId(long checkListId) {
		this.checkListId = checkListId;
	}

	public long getTalhao() {
		return talhao;
	}

	public void setTalhao(long talhao) {
		this.talhao = talhao;
	}

	public long getCodigoAtividade() {
		return codigoAtividade;
	}

	public void setCodigoAtividade(long codigoAtividade) {
		this.codigoAtividade = codigoAtividade;
	}

	public long getNumeroLaudo() {
		return numeroLaudo;
	}

	public void setNumeroLaudo(long numeroLaudo) {
		this.numeroLaudo = numeroLaudo;
	}

	public long getMecanicoId() {
		return mecanicoId;
	}

	public void setMecanicoId(long mecanicoId) {
		this.mecanicoId = mecanicoId;
	}

	public String getNomeAtividade() {
		return nomeAtividade;
	}

	public void setNomeAtividade(String nomeAtividade) {
		this.nomeAtividade = nomeAtividade;
	}

	public String getDataHoraCadastro() {
		return dataHoraCadastro;
	}

	public void setDataHoraCadastro(String dataHoraCadastro) {
		this.dataHoraCadastro = dataHoraCadastro;
	}

	public String getHorimetro() {
		return horimetro;
	}

	public void setHorimetro(String horimetro) {
		this.horimetro = horimetro;
	}

	public String getOdometro() {
		return odometro;
	}

	public void setOdometro(String odometro) {
		this.odometro = odometro;
	}

	public String getProducao() {
		return producao;
	}

	public void setProducao(String producao) {
		this.producao = producao;
	}

	public String getCto() {
		return cto;
	}

	public void setCto(String cto) {
		this.cto = cto;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getDataHoraUpload() {
		return dataHoraUpload;
	}

	public void setDataHoraUpload(String dataHoraUpload) {
		this.dataHoraUpload = dataHoraUpload;
	}
}
