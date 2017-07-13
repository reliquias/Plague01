package br.com.inicial.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import br.com.inicial.interfaces.BaseEntity;

@Entity
public class BoletimChecklist implements BaseEntity, Serializable {

	private static final long serialVersionUID = 2471765773806278661L;
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	private long idFirebase;
	
	private boolean nivelOleoMotor;
    private boolean nivelLiquidoRefrigerante;
    private boolean nivelOleoHidraulico;
    private boolean protecaoFrontalRadiador;
    private boolean limpezaRadiador;
    private boolean vazamentoMangueirasECilindros;
    private boolean faroisTrabalho;
    private boolean sinalizacaoReEMovimento;
    private boolean presencaGeralTrincasMaquinaBase;
    private boolean alarmesPainelInstrumentos;
    private boolean limpezaInternaCabine;
    private boolean janelasLaterais;
    private boolean parabrisaFrontal;
    private boolean tensaoEsteiras;
    private boolean roletesSuperiores;
    private boolean roletesInferiores;
    private boolean limpezaEsteiras;
    private boolean sapatasEsteira;
    private boolean freioServicos;
    private boolean condicoesPneus;

    private boolean conjuntoCorteHARVESTER;
    private boolean pinosBielasFacasRolosECilindros;
    private boolean presencaGeralTrincasAcessorio;
    private boolean lubrificacaoGeral;
    private boolean rotator;
    private boolean discoEDentesCorteFELLER;

    private boolean tampasProtecao;
    private boolean freioEmergencia;
    private boolean chaveGeral;
    private boolean kitContencaoVazamentos;
    private boolean inspecaoExtintores;
    private boolean adesivosSeguranca;
    private boolean travaCabineFORWARDER;
    private boolean assentoOperacao;
    private boolean radioComunicacao;
    private boolean escadasOuEstribosAcesso;

    private boolean paradaImediata;
    private boolean paradaCombinada;
	
	@OneToOne(mappedBy = "boletimCheckList", fetch=FetchType.LAZY)
	private BoletimDiario boletimDiario;
	
	public long getIdFirebase() {
		return idFirebase;
	}

	public void setIdFirebase(long idFirebase) {
		this.idFirebase = idFirebase;
	}

	public boolean isNivelOleoMotor() {
		return nivelOleoMotor;
	}

	public void setNivelOleoMotor(boolean nivelOleoMotor) {
		this.nivelOleoMotor = nivelOleoMotor;
	}

	public boolean isNivelLiquidoRefrigerante() {
		return nivelLiquidoRefrigerante;
	}

	public void setNivelLiquidoRefrigerante(boolean nivelLiquidoRefrigerante) {
		this.nivelLiquidoRefrigerante = nivelLiquidoRefrigerante;
	}

	public boolean isNivelOleoHidraulico() {
		return nivelOleoHidraulico;
	}

	public void setNivelOleoHidraulico(boolean nivelOleoHidraulico) {
		this.nivelOleoHidraulico = nivelOleoHidraulico;
	}

	public boolean isProtecaoFrontalRadiador() {
		return protecaoFrontalRadiador;
	}

	public void setProtecaoFrontalRadiador(boolean protecaoFrontalRadiador) {
		this.protecaoFrontalRadiador = protecaoFrontalRadiador;
	}

	public boolean isLimpezaRadiador() {
		return limpezaRadiador;
	}

	public void setLimpezaRadiador(boolean limpezaRadiador) {
		this.limpezaRadiador = limpezaRadiador;
	}

	public boolean isVazamentoMangueirasECilindros() {
		return vazamentoMangueirasECilindros;
	}

	public void setVazamentoMangueirasECilindros(
			boolean vazamentoMangueirasECilindros) {
		this.vazamentoMangueirasECilindros = vazamentoMangueirasECilindros;
	}

	public boolean isFaroisTrabalho() {
		return faroisTrabalho;
	}

	public void setFaroisTrabalho(boolean faroisTrabalho) {
		this.faroisTrabalho = faroisTrabalho;
	}

	public boolean isSinalizacaoReEMovimento() {
		return sinalizacaoReEMovimento;
	}

	public void setSinalizacaoReEMovimento(boolean sinalizacaoReEMovimento) {
		this.sinalizacaoReEMovimento = sinalizacaoReEMovimento;
	}

	public boolean isPresencaGeralTrincasMaquinaBase() {
		return presencaGeralTrincasMaquinaBase;
	}

	public void setPresencaGeralTrincasMaquinaBase(
			boolean presencaGeralTrincasMaquinaBase) {
		this.presencaGeralTrincasMaquinaBase = presencaGeralTrincasMaquinaBase;
	}

	public boolean isAlarmesPainelInstrumentos() {
		return alarmesPainelInstrumentos;
	}

	public void setAlarmesPainelInstrumentos(boolean alarmesPainelInstrumentos) {
		this.alarmesPainelInstrumentos = alarmesPainelInstrumentos;
	}

	public boolean isLimpezaInternaCabine() {
		return limpezaInternaCabine;
	}

	public void setLimpezaInternaCabine(boolean limpezaInternaCabine) {
		this.limpezaInternaCabine = limpezaInternaCabine;
	}

	public boolean isJanelasLaterais() {
		return janelasLaterais;
	}

	public void setJanelasLaterais(boolean janelasLaterais) {
		this.janelasLaterais = janelasLaterais;
	}

	public boolean isParabrisaFrontal() {
		return parabrisaFrontal;
	}

	public void setParabrisaFrontal(boolean parabrisaFrontal) {
		this.parabrisaFrontal = parabrisaFrontal;
	}

	public boolean isTensaoEsteiras() {
		return tensaoEsteiras;
	}

	public void setTensaoEsteiras(boolean tensaoEsteiras) {
		this.tensaoEsteiras = tensaoEsteiras;
	}

	public boolean isRoletesSuperiores() {
		return roletesSuperiores;
	}

	public void setRoletesSuperiores(boolean roletesSuperiores) {
		this.roletesSuperiores = roletesSuperiores;
	}

	public boolean isRoletesInferiores() {
		return roletesInferiores;
	}

	public void setRoletesInferiores(boolean roletesInferiores) {
		this.roletesInferiores = roletesInferiores;
	}

	public boolean isLimpezaEsteiras() {
		return limpezaEsteiras;
	}

	public void setLimpezaEsteiras(boolean limpezaEsteiras) {
		this.limpezaEsteiras = limpezaEsteiras;
	}

	public boolean isSapatasEsteira() {
		return sapatasEsteira;
	}

	public void setSapatasEsteira(boolean sapatasEsteira) {
		this.sapatasEsteira = sapatasEsteira;
	}

	public boolean isFreioServicos() {
		return freioServicos;
	}

	public void setFreioServicos(boolean freioServicos) {
		this.freioServicos = freioServicos;
	}

	public boolean isCondicoesPneus() {
		return condicoesPneus;
	}

	public void setCondicoesPneus(boolean condicoesPneus) {
		this.condicoesPneus = condicoesPneus;
	}

	public boolean isConjuntoCorteHARVESTER() {
		return conjuntoCorteHARVESTER;
	}

	public void setConjuntoCorteHARVESTER(boolean conjuntoCorteHARVESTER) {
		this.conjuntoCorteHARVESTER = conjuntoCorteHARVESTER;
	}

	public boolean isPresencaGeralTrincasAcessorio() {
		return presencaGeralTrincasAcessorio;
	}

	public void setPresencaGeralTrincasAcessorio(
			boolean presencaGeralTrincasAcessorio) {
		this.presencaGeralTrincasAcessorio = presencaGeralTrincasAcessorio;
	}

	public boolean isLubrificacaoGeral() {
		return lubrificacaoGeral;
	}

	public void setLubrificacaoGeral(boolean lubrificacaoGeral) {
		this.lubrificacaoGeral = lubrificacaoGeral;
	}

	public boolean isRotator() {
		return rotator;
	}

	public void setRotator(boolean rotator) {
		this.rotator = rotator;
	}

	public boolean isDiscoEDentesCorteFELLER() {
		return discoEDentesCorteFELLER;
	}

	public void setDiscoEDentesCorteFELLER(boolean discoEDentesCorteFELLER) {
		this.discoEDentesCorteFELLER = discoEDentesCorteFELLER;
	}

	public boolean isTampasProtecao() {
		return tampasProtecao;
	}

	public void setTampasProtecao(boolean tampasProtecao) {
		this.tampasProtecao = tampasProtecao;
	}

	public boolean isFreioEmergencia() {
		return freioEmergencia;
	}

	public void setFreioEmergencia(boolean freioEmergencia) {
		this.freioEmergencia = freioEmergencia;
	}

	public boolean isChaveGeral() {
		return chaveGeral;
	}

	public void setChaveGeral(boolean chaveGeral) {
		this.chaveGeral = chaveGeral;
	}

	public boolean isKitContencaoVazamentos() {
		return kitContencaoVazamentos;
	}

	public void setKitContencaoVazamentos(boolean kitContencaoVazamentos) {
		this.kitContencaoVazamentos = kitContencaoVazamentos;
	}

	public boolean isInspecaoExtintores() {
		return inspecaoExtintores;
	}

	public void setInspecaoExtintores(boolean inspecaoExtintores) {
		this.inspecaoExtintores = inspecaoExtintores;
	}

	public boolean isAdesivosSeguranca() {
		return adesivosSeguranca;
	}

	public void setAdesivosSeguranca(boolean adesivosSeguranca) {
		this.adesivosSeguranca = adesivosSeguranca;
	}

	public boolean isTravaCabineFORWARDER() {
		return travaCabineFORWARDER;
	}

	public void setTravaCabineFORWARDER(boolean travaCabineFORWARDER) {
		this.travaCabineFORWARDER = travaCabineFORWARDER;
	}

	public boolean isAssentoOperacao() {
		return assentoOperacao;
	}

	public void setAssentoOperacao(boolean assentoOperacao) {
		this.assentoOperacao = assentoOperacao;
	}

	public boolean isRadioComunicacao() {
		return radioComunicacao;
	}

	public void setRadioComunicacao(boolean radioComunicacao) {
		this.radioComunicacao = radioComunicacao;
	}

	public boolean isEscadasOuEstribosAcesso() {
		return escadasOuEstribosAcesso;
	}

	public void setEscadasOuEstribosAcesso(boolean escadasOuEstribosAcesso) {
		this.escadasOuEstribosAcesso = escadasOuEstribosAcesso;
	}

	public boolean isParadaImediata() {
		return paradaImediata;
	}

	public void setParadaImediata(boolean paradaImediata) {
		this.paradaImediata = paradaImediata;
	}

	public boolean isParadaCombinada() {
		return paradaCombinada;
	}

	public void setParadaCombinada(boolean paradaCombinada) {
		this.paradaCombinada = paradaCombinada;
	}

	public BoletimDiario getBoletimDiario() {
		return boletimDiario;
	}

	public void setBoletimDiario(BoletimDiario boletimDiario) {
		this.boletimDiario = boletimDiario;
	}

	public boolean isPinosBielasFacasRolosECilindros() {
		return pinosBielasFacasRolosECilindros;
	}

	public void setPinosBielasFacasRolosECilindros(
			boolean pinosBielasFacasRolosECilindros) {
		this.pinosBielasFacasRolosECilindros = pinosBielasFacasRolosECilindros;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	@Override
    public Integer getIdi() {
        return new Integer(id);
    }
	
}
