package br.com.inicial.modelo;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import br.com.inicial.interfaces.BaseEntity;
import br.com.inicial.util.Utils;

@Entity
public class Vistoria implements BaseEntity, Serializable {

	private static final long serialVersionUID = 2471765773806278661L;
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String armazenamento;
	private String coordenadas;
	private Calendar dataVistoria;
	private String status;
	private String responsavel;
	@Lob
    @Column(name = "IMAGEM_1")
    private byte[] imagem1;
	@Lob
	@Column(name = "IMAGEM_2")
	private byte[] imagem2;
	@Lob
	@Column(name = "IMAGEM_3")
	private byte[] imagem3;
	@Lob
	@Column(name = "IMAGEM_4")
	private byte[] imagem4;
	@Lob
	@Column(name = "IMAGEM_5")
	private byte[] imagem5;
	
	@Lob
	private String imagens;
	
	@ManyToOne
	private TipoDoenca praga;
	@ManyToOne
	private PlantaTalhao plantaTalhao;
	
	@Transient
	private Integer idFazenda;
	@Transient
	private Integer idPlanta;
	@Transient
	private Integer idDoenca;
	@Transient
	private Integer idTalhao;

	@Transient
	private List<String> urlImage;

	@Transient
	private String dataVistoriaFirebase;
	
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

	public Integer getIdFazenda() {
		return idFazenda;
	}

	public void setIdFazenda(Integer idFazenda) {
		this.idFazenda = idFazenda;
	}

	public Integer getIdPlanta() {
		return idPlanta;
	}

	public void setIdPlanta(Integer idPlanta) {
		this.idPlanta = idPlanta;
	}

	public Integer getIdDoenca() {
		return idDoenca;
	}

	public void setIdDoenca(Integer idDoenca) {
		this.idDoenca = idDoenca;
	}

	public Integer getIdTalhao() {
		return idTalhao;
	}

	public void setIdTalhao(Integer idTalhao) {
		this.idTalhao = idTalhao;
	}

	public String getDataVistoriaFirebase() {
		return dataVistoriaFirebase;
	}

	public void setDataVistoriaFirebase(String dataVistoriaFirebase) {
		this.dataVistoriaFirebase = dataVistoriaFirebase;
	}

	public byte[] getImagem2() {
		return imagem2;
	}

	public void setImagem2(byte[] imagem2) {
		this.imagem2 = imagem2;
	}

	public byte[] getImagem3() {
		return imagem3;
	}

	public void setImagem3(byte[] imagem3) {
		this.imagem3 = imagem3;
	}

	public byte[] getImagem4() {
		return imagem4;
	}

	public void setImagem4(byte[] imagem4) {
		this.imagem4 = imagem4;
	}

	public byte[] getImagem5() {
		return imagem5;
	}

	public void setImagem5(byte[] imagem5) {
		this.imagem5 = imagem5;
	}

	public List<String> getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(List<String> urlImage) {
		int x = 0;
		String urls = "";
		try {
			for (String image : urlImage) {
				x++;
				urls = urls+image+";";
				if(x == 1){
					setImagem1(Utils.urlToByte(image).toByteArray());
				}
				if(x == 2){
					setImagem2(Utils.urlToByte(image).toByteArray());
				}
				if(x == 3){
					setImagem3(Utils.urlToByte(image).toByteArray());
				}
				if(x == 4){
					setImagem4(Utils.urlToByte(image).toByteArray());
				}
				if(x == 5){
					setImagem5(Utils.urlToByte(image).toByteArray());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setImagens(urls);
		this.urlImage = urlImage;
	}

	public String getImagens() {
		return imagens;
	}

	public void setImagens(String imagens) {
		this.imagens = imagens;
	}
}
