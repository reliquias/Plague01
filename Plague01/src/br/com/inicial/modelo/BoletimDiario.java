package br.com.inicial.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import br.com.inicial.interfaces.BaseEntity;

@Entity
public class BoletimDiario implements BaseEntity, Serializable {

	private static final long serialVersionUID = 2471765773806278661L;
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String idFirebase;
	
	private String horimetro;
	private String maquinaBase;
	private String matriculaResponsavel;
	private String nomeResponsavel;
	private String odometro;
	private String projeto;
	private String turno;
	private String dataHoraInicio;
	
	@ManyToOne
	private Fazenda fazenda;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "CHECK_LIST_ID")
	private BoletimChecklist boletimCheckList;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="boletimDiario", fetch=FetchType.LAZY)
	private List<BoletimApontamento> apontamentoList = new ArrayList<BoletimApontamento>();

	
	@Transient
	private Integer idFazenda;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHorimetro() {
		return horimetro;
	}

	public void setHorimetro(String horimetro) {
		this.horimetro = horimetro;
	}

	public String getMaquinaBase() {
		return maquinaBase;
	}

	public void setMaquinaBase(String maquinaBase) {
		this.maquinaBase = maquinaBase;
	}

	public String getMatriculaResponsavel() {
		return matriculaResponsavel;
	}

	public void setMatriculaResponsavel(String matriculaResponsavel) {
		this.matriculaResponsavel = matriculaResponsavel;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getOdometro() {
		return odometro;
	}

	public void setOdometro(String odometro) {
		this.odometro = odometro;
	}

	public String getProjeto() {
		return projeto;
	}

	public void setProjeto(String projeto) {
		this.projeto = projeto;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public Fazenda getFazenda() {
		return fazenda;
	}

	public void setFazenda(Fazenda fazenda) {
		this.fazenda = fazenda;
	}

	public BoletimChecklist getBoletimCheckList() {
		return boletimCheckList;
	}

	public void setBoletimCheckList(BoletimChecklist boletimCheckList) {
		this.boletimCheckList = boletimCheckList;
	}

	public Integer getIdFazenda() {
		return idFazenda;
	}

	public void setIdFazenda(Integer idFazenda) {
		this.idFazenda = idFazenda;
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
		BoletimDiario other = (BoletimDiario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public List<BoletimApontamento> getApontamentoList() {
		return apontamentoList;
	}

	public void setApontamentoList(List<BoletimApontamento> apontamentoList) {
		this.apontamentoList = apontamentoList;
	}

	public String getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(String dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}

	public String getIdFirebase() {
		return idFirebase;
	}

	public void setIdFirebase(String idFirebase) {
		this.idFirebase = idFirebase;
	}

	@Override
    public Integer getIdi() {
        return new Integer(id);
    }
	
	
	
}
