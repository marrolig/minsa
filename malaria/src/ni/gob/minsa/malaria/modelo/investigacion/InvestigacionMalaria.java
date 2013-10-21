package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoHematico;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the INVESTIGACIONES_MALARIA database table.
 * 
 */
@NamedQueries({
	@NamedQuery(name="InvestigacionMalaria.encontrarPorMuestreoHematico",
			query="select ti from InvestigacionMalaria ti " +
					"where ti.muestreoHematico.muestreoHematicoId = :pMuestreoHematicoId")
})
@Entity
@Table(name="INVESTIGACIONES_MALARIA")
public class InvestigacionMalaria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INVESTIGACIONES_MALARIA_INVESTIGACIONMALARIAID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INVESTIGACIONES_MALARIA_INVESTIGACIONMALARIAID_GENERATOR")
	@Column(name="INVESTIGACION_MALARIA_ID", unique=true, nullable=false, precision=10)
	private long investigacionMalariaId;

	@Column(nullable=false, precision=1)
	private BigDecimal automedicacion;

	@Column(name="CASO_CERRADO", nullable=false, precision=1)
	private BigDecimal casoCerrado;

	@ManyToOne
	@JoinColumn(name="CLASIFICACION_CASO", referencedColumnName="CODIGO",nullable=false)
	private ClasificacionCaso clasificacionCaso;

	@ManyToOne
	@JoinColumn(name="CLASIFICACION_CLINICA", referencedColumnName="CODIGO",nullable=false)
	private ClasificacionClinica clasificacionClinica;

	@Column(name="COLATERALES_TRATADOS", nullable=false, precision=3)
	private BigDecimal colateralesTratados;

	@Column(name="CONDICION_FINAL_VIVO", nullable=false, precision=1)
	private BigDecimal condicionFinalVivo;

	@ManyToOne
	@JoinColumn(name="CONFIRMACION_CNDR", referencedColumnName="CODIGO",nullable=false)
	private ConfirmacionDiagnostico confirmacionCndr;

	@ManyToOne
	@JoinColumn(name="CONFIRMACION_ENTIDAD", referencedColumnName="CODIGO",nullable=false)
	private ConfirmacionDiagnostico confirmacionEntidad;

	@Column(name="CONTROL_PARASITARIO", nullable=false, precision=1)
	private BigDecimal controlParasitario;

	@Column(name="CONVIVIENTES_TRATADOS", nullable=false, precision=2)
	private BigDecimal convivientesTratados;

	@Column(name="DIAS_POSTERIORES_CONTROL", precision=3)
	private BigDecimal diasPosterioresControl;

	@Column(nullable=false, length=100)
	private String epidemiologo;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_CIERRE_CASO")
	private Date fechaCierreCaso;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_DEFUNCION")
	private Date fechaDefuncion;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_INFECCION", nullable=false)
	private Date fechaInfeccion;

    @Temporal( TemporalType.DATE)
	@Column(name="FIN_TRATAMIENTO")
	private Date finTratamiento;

	@Column(name="INFECCION_RESIDENCIA", nullable=false, precision=1)
	private BigDecimal infeccionResidencia;

    @Temporal( TemporalType.DATE)
	@Column(name="INICIO_TRATAMIENTO")
	private Date inicioTratamiento;

	@Column(name="LATITUD_VIVIENDA", precision=10, scale=6)
	private BigDecimal latitudVivienda;

	@Column(name="LONGITUD_VIVIENDA", precision=10, scale=6)
	private BigDecimal longitudVivienda;

	@Column(name="MANEJO_CLINICO", nullable=false, precision=1)
	private BigDecimal manejoClinico;

	@Column(name="MEDICAMENTOS_AUTOMEDICACION", length=300)
	private String medicamentosAutomedicacion;

	@OneToOne(targetEntity=MuestreoHematico.class)
	@JoinColumn(name="MUESTREO_HEMATICO", nullable=false,updatable=false)
	private MuestreoHematico muestreoHematico;

	@Column(name="NIVEL_AUTORIZACION", nullable=false, precision=1)
	private BigDecimal nivelAutorizacion;

	@Column(name="NUMERO_CASO", nullable=false, length=8)
	private String numeroCaso;

	@Column(length=500)
	private String observaciones;

	@Column(nullable=false, length=100)
	private String responsable;

	@Column(name="RESULTADO_CONTROL_POSITIVO", precision=1)
	private BigDecimal resultadoControlPositivo;

	@Column(nullable=false, precision=1)
	private BigDecimal sintomatico;

	@ManyToOne
	@JoinColumn(name="TIPO_COMPLICACION", referencedColumnName="CODIGO",nullable=false)
	private TipoComplicacion tipoComplicacion;

	@ManyToOne
	@JoinColumn(name="TIPO_RECURRENCIA", referencedColumnName="CODIGO",nullable=false)
	private TipoRecurrencia tipoRecurrencia;

	@Column(nullable=false, precision=1)
	private BigDecimal transfusion;

	@Column(name="TRATAMIENTO_COMPLETO", nullable=false, precision=1)
	private BigDecimal tratamientoCompleto;

	@Column(name="TRATAMIENTO_SUPERVISADO", nullable=false, precision=1)
	private BigDecimal tratamientoSupervisado;

	@Column(name="USO_MOSQUITERO", precision=1)
	private BigDecimal usoMosquitero;

	@Column(name="VIAJES_ZONA_RIESGO", nullable=false, precision=1)
	private BigDecimal viajesZonaRiesgo;

	//bi-directional many-to-one association to InvestigacionesMedicamento
	@OneToMany(mappedBy="investigacionMalaria",targetEntity=InvestigacionMedicamento.class,fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private Set<InvestigacionMedicamento> investigacionMedicamentos;
	
	@OneToOne(mappedBy="investigacionMalaria",targetEntity=InvestigacionSintoma.class,optional=true,fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private InvestigacionSintoma investigacionSintomas;
	
	@OneToOne(mappedBy="investigacionMalaria",targetEntity=InvestigacionLugar.class,optional=true,fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private InvestigacionLugar investigacionLugares;
	
	@OneToOne(mappedBy="investigacionMalaria",targetEntity=InvestigacionTransfusion.class,optional=true,fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private InvestigacionTransfusion investigacionTransfusiones; 
	
	@OneToOne(mappedBy="investigacionMalaria",targetEntity=InvestigacionHospitalario.class,optional=true,fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private InvestigacionHospitalario investigacionHospitalarios;

    public InvestigacionMalaria() {
    }

	public long getInvestigacionMalariaId() {
		return this.investigacionMalariaId;
	}

	public void setInvestigacionMalariaId(long investigacionMalariaId) {
		this.investigacionMalariaId = investigacionMalariaId;
	}

	public BigDecimal getAutomedicacion() {
		return this.automedicacion;
	}

	public void setAutomedicacion(BigDecimal automedicacion) {
		this.automedicacion = automedicacion;
	}

	public BigDecimal getCasoCerrado() {
		return this.casoCerrado;
	}

	public void setCasoCerrado(BigDecimal casoCerrado) {
		this.casoCerrado = casoCerrado;
	}

	public ClasificacionCaso getClasificacionCaso() {
		return this.clasificacionCaso;
	}

	public void setClasificacionCaso(ClasificacionCaso clasificacionCaso) {
		this.clasificacionCaso = clasificacionCaso;
	}

	public ClasificacionClinica getClasificacionClinica() {
		return this.clasificacionClinica;
	}

	public void setClasificacionClinica(ClasificacionClinica clasificacionClinica) {
		this.clasificacionClinica = clasificacionClinica;
	}

	public BigDecimal getColateralesTratados() {
		return this.colateralesTratados;
	}

	public void setColateralesTratados(BigDecimal colateralesTratados) {
		this.colateralesTratados = colateralesTratados;
	}

	public BigDecimal getCondicionFinalVivo() {
		return this.condicionFinalVivo;
	}

	public void setCondicionFinalVivo(BigDecimal condicionFinalVivo) {
		this.condicionFinalVivo = condicionFinalVivo;
	}

	public ConfirmacionDiagnostico getConfirmacionCndr() {
		return this.confirmacionCndr;
	}

	public void setConfirmacionCndr(ConfirmacionDiagnostico confirmacionCndr) {
		this.confirmacionCndr = confirmacionCndr;
	}

	public ConfirmacionDiagnostico getConfirmacionEntidad() {
		return this.confirmacionEntidad;
	}

	public void setConfirmacionEntidad(ConfirmacionDiagnostico confirmacionEntidad) {
		this.confirmacionEntidad = confirmacionEntidad;
	}

	public BigDecimal getControlParasitario() {
		return this.controlParasitario;
	}

	public void setControlParasitario(BigDecimal controlParasitario) {
		this.controlParasitario = controlParasitario;
	}

	public BigDecimal getConvivientesTratados() {
		return this.convivientesTratados;
	}

	public void setConvivientesTratados(BigDecimal convivientesTratados) {
		this.convivientesTratados = convivientesTratados;
	}

	public BigDecimal getDiasPosterioresControl() {
		return this.diasPosterioresControl;
	}

	public void setDiasPosterioresControl(BigDecimal diasPosterioresControl) {
		this.diasPosterioresControl = diasPosterioresControl;
	}

	public String getEpidemiologo() {
		return this.epidemiologo;
	}

	public void setEpidemiologo(String epidemiologo) {
		this.epidemiologo = epidemiologo;
	}

	public Date getFechaCierreCaso() {
		return this.fechaCierreCaso;
	}

	public void setFechaCierreCaso(Date fechaCierreCaso) {
		this.fechaCierreCaso = fechaCierreCaso;
	}

	public Date getFechaDefuncion() {
		return this.fechaDefuncion;
	}

	public void setFechaDefuncion(Date fechaDefuncion) {
		this.fechaDefuncion = fechaDefuncion;
	}

	public Date getFechaInfeccion() {
		return this.fechaInfeccion;
	}

	public void setFechaInfeccion(Date fechaInfeccion) {
		this.fechaInfeccion = fechaInfeccion;
	}

	public Date getFinTratamiento() {
		return this.finTratamiento;
	}

	public void setFinTratamiento(Date finTratamiento) {
		this.finTratamiento = finTratamiento;
	}

	public BigDecimal getInfeccionResidencia() {
		return this.infeccionResidencia;
	}

	public void setInfeccionResidencia(BigDecimal infeccionResidencia) {
		this.infeccionResidencia = infeccionResidencia;
	}

	public Date getInicioTratamiento() {
		return this.inicioTratamiento;
	}

	public void setInicioTratamiento(Date inicioTratamiento) {
		this.inicioTratamiento = inicioTratamiento;
	}

	public BigDecimal getLatitudVivienda() {
		return this.latitudVivienda;
	}

	public void setLatitudVivienda(BigDecimal latitudVivienda) {
		this.latitudVivienda = latitudVivienda;
	}

	public BigDecimal getLongitudVivienda() {
		return this.longitudVivienda;
	}

	public void setLongitudVivienda(BigDecimal longitudVivienda) {
		this.longitudVivienda = longitudVivienda;
	}

	public BigDecimal getManejoClinico() {
		return this.manejoClinico;
	}

	public void setManejoClinico(BigDecimal manejoClinico) {
		this.manejoClinico = manejoClinico;
	}

	public String getMedicamentosAutomedicacion() {
		return this.medicamentosAutomedicacion;
	}

	public void setMedicamentosAutomedicacion(String medicamentosAutomedicacion) {
		this.medicamentosAutomedicacion = medicamentosAutomedicacion;
	}

	public MuestreoHematico getMuestreoHematico() {
		return this.muestreoHematico;
	}

	public void setMuestreoHematico(MuestreoHematico muestreoHematico) {
		this.muestreoHematico = muestreoHematico;
	}

	public BigDecimal getNivelAutorizacion() {
		return this.nivelAutorizacion;
	}

	public void setNivelAutorizacion(BigDecimal nivelAutorizacion) {
		this.nivelAutorizacion = nivelAutorizacion;
	}

	public String getNumeroCaso() {
		return this.numeroCaso;
	}

	public void setNumeroCaso(String numeroCaso) {
		this.numeroCaso = numeroCaso;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getResponsable() {
		return this.responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public BigDecimal getResultadoControlPositivo() {
		return this.resultadoControlPositivo;
	}

	public void setResultadoControlPositivo(BigDecimal resultadoControlPositivo) {
		this.resultadoControlPositivo = resultadoControlPositivo;
	}

	public BigDecimal getSintomatico() {
		return this.sintomatico;
	}

	public void setSintomatico(BigDecimal sintomatico) {
		this.sintomatico = sintomatico;
	}

	public TipoComplicacion getTipoComplicacion() {
		return this.tipoComplicacion;
	}

	public void setTipoComplicacion(TipoComplicacion tipoComplicacion) {
		this.tipoComplicacion = tipoComplicacion;
	}

	public TipoRecurrencia getTipoRecurrencia() {
		return this.tipoRecurrencia;
	}

	public void setTipoRecurrencia(TipoRecurrencia tipoRecurrencia) {
		this.tipoRecurrencia = tipoRecurrencia;
	}

	public BigDecimal getTransfusion() {
		return this.transfusion;
	}

	public void setTransfusion(BigDecimal transfusion) {
		this.transfusion = transfusion;
	}

	public BigDecimal getTratamientoCompleto() {
		return this.tratamientoCompleto;
	}

	public void setTratamientoCompleto(BigDecimal tratamientoCompleto) {
		this.tratamientoCompleto = tratamientoCompleto;
	}

	public BigDecimal getTratamientoSupervisado() {
		return this.tratamientoSupervisado;
	}

	public void setTratamientoSupervisado(BigDecimal tratamientoSupervisado) {
		this.tratamientoSupervisado = tratamientoSupervisado;
	}

	public BigDecimal getUsoMosquitero() {
		return this.usoMosquitero;
	}

	public void setUsoMosquitero(BigDecimal usoMosquitero) {
		this.usoMosquitero = usoMosquitero;
	}

	public BigDecimal getViajesZonaRiesgo() {
		return this.viajesZonaRiesgo;
	}

	public void setViajesZonaRiesgo(BigDecimal viajesZonaRiesgo) {
		this.viajesZonaRiesgo = viajesZonaRiesgo;
	}

	public Set<InvestigacionMedicamento> getInvestigacionesMedicamentos() {
		return this.investigacionMedicamentos;
	}

	public void setInvestigacionesMedicamentos(Set<InvestigacionMedicamento> investigacionMedicamentos) {
		this.investigacionMedicamentos = investigacionMedicamentos;
	}

	public Set<InvestigacionMedicamento> getInvestigacionMedicamentos() {
		return investigacionMedicamentos;
	}

	public void setInvestigacionMedicamentos(
			Set<InvestigacionMedicamento> investigacionMedicamentos) {
		this.investigacionMedicamentos = investigacionMedicamentos;
	}

	public InvestigacionSintoma getInvestigacionSintomas() {
		return investigacionSintomas;
	}

	public void setInvestigacionSintomas(InvestigacionSintoma investigacionSintomas) {
		this.investigacionSintomas = investigacionSintomas;
	}

	public InvestigacionLugar getInvestigacionLugares() {
		return investigacionLugares;
	}

	public void setInvestigacionLugares(InvestigacionLugar investigacionLugares) {
		this.investigacionLugares = investigacionLugares;
	}

	public InvestigacionTransfusion getInvestigacionTransfusiones() {
		return investigacionTransfusiones;
	}

	public void setInvestigacionTransfusiones(
			InvestigacionTransfusion investigacionTransfusiones) {
		this.investigacionTransfusiones = investigacionTransfusiones;
	}

	public InvestigacionHospitalario getInvestigacionHospitalarios() {
		return investigacionHospitalarios;
	}

	public void setInvestigacionHospitalarios(
			InvestigacionHospitalario investigacionHospitalarios) {
		this.investigacionHospitalarios = investigacionHospitalarios;
	}

}