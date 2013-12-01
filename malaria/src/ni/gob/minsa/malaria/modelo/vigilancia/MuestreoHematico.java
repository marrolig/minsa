// -----------------------------------------------
// MuestreoHematico.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.vigilancia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Manzana;
import ni.gob.minsa.malaria.modelo.poblacion.Vivienda;
import ni.gob.minsa.malaria.modelo.sis.Etnia;
import ni.gob.minsa.malaria.modelo.sis.Sexo;
import ni.gob.minsa.malaria.modelo.sis.SisPersona;
import ni.gob.minsa.malaria.soporte.TipoBusqueda;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla MUESTREOS_HEMATICOS 
 * en el esquema SIVE.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 29/10/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="MUESTREOS_HEMATICOS",schema="SIVE")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="MuestreoHematico.encontrarPorLamina",
			query="select mh from MuestreoHematico mh " +
				    "where mh.entidadNotificacion.entidadAdtvaId=:pEntidadId AND mh.clave=:pClave AND " +
				    "      mh.numeroLamina=:pNumeroLamina "),
	@NamedQuery(
			name="MuestreoHematico.listarPorPersona",
			query="select mh from MuestreoHematico mh " +
					"where mh.sisPersona.personaId=:pPersonaId")
})				 
public class MuestreoHematico extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MSTRS_HMS_ID_GENERATOR", sequenceName="SIVE.MSTRS_HMS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MSTRS_HMS_ID_GENERATOR")
	@Column(name="MUESTREO_HEMATICO_ID",updatable=false,nullable=false)
	private long muestreoHematicoId;

    @NotNull(message="La entidad administrativa de notificación es requerida")
    @ManyToOne(targetEntity=EntidadAdtva.class)
	@JoinColumn(name="ENTIDAD_NOTIFICACION",nullable=false,updatable=true,referencedColumnName="CODIGO")
	private EntidadAdtva entidadNotificacion;

    @NotNull(message="La unidad de salud que notifica es requerida")
    @ManyToOne(targetEntity=Unidad.class)
	@JoinColumn(name="UNIDAD_NOTIFICACION",nullable=false,updatable=true,referencedColumnName="CODIGO")
	private Unidad unidadNotificacion;

    @NotNull(message="El municipio de notificación es requerido")
    @ManyToOne(targetEntity=DivisionPolitica.class)
	@JoinColumn(name="MUNICIPIO_NOTIFICACION",nullable=false,updatable=true,referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipioNotificacion;

    @ManyToOne(targetEntity=PuestoNotificacion.class)
	@JoinColumn(name="PUESTO_NOTIFICACION",nullable=true,updatable=true)
	private PuestoNotificacion puestoNotificacion;

    @Size(min=1,max=5,message="La clave debe tener de 1 a 5 caracteres")
    @Column(name="CLAVE",nullable=true,length=5)
    private String clave;
    
	@Digits(integer=10,fraction=0)
	@Column(name="NUMERO_LAMINA",nullable=true,precision=10,scale=0)
	private BigDecimal numeroLamina;

	@NotNull(message="El tipo de búsqueda es requerida")
	@DecimalMin(value="0",message="Tipo de búsqueda no válido")
	@DecimalMax(value="1",message="Tipo de búsqueda no válido")
	@Column(name="TIPO_BUSQUEDA",nullable=false)
	private Integer tipoBusqueda;
	
	@NotNull(message="La fecha en la cual se realizó la toma de la gota gruesa es requerida")
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_TOMA",nullable=false)
	private Date fechaToma;
    
	@NotNull(message="El año epidemiológico es requerido")
	@Digits(integer=4,fraction=0,message="Declaración de año epidemiológico no válida")
	@DecimalMin(value="1995",message="El año epidemiológico no puede ser inferior a 1995")
	@Column(name="AÑO_EPIDEMIOLOGICO",nullable=false,updatable=true)
    private Integer añoEpidemiologico;
    
    @NotNull(message="La semana epidemiológica es requerida")
	@DecimalMin(value="1",message="La semana epidemiológica debe ser igual o mayor que 1")
	@DecimalMax(value="52",message="La semana epidemiológica debe ser igual o menor que 52")
	@Column(name="SEMANA_EPIDEMIOLOGICA",nullable=false,updatable=true)
	private Integer semanaEpidemiologica;
	
	@NotNull(message="La declaración de la persona a la cual se le realiza el muestreo hemático, es requerida")
    @ManyToOne(targetEntity=SisPersona.class)
	@JoinColumn(name="PERSONA",nullable=false,updatable=false)
	private SisPersona sisPersona;

	@NotNull(message="La fecha de nacimiento es requerida")
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_NACIMIENTO",nullable=false,updatable=false)
	private Date fechaNacimiento;

	@NotNull(message="La declaración del sexo de la persona es requerida")
	@ManyToOne
	@JoinColumn(name="SEXO",referencedColumnName="CODIGO")
	private Sexo sexo;

	@ManyToOne
	@JoinColumn(name="ETNIA",referencedColumnName="CODIGO")
	private Etnia etnia;
	
	@DecimalMin(value="0",message="El valor del indicador de embarazo no es válido")
	@DecimalMax(value="1",message="El valor del indicador de embarazo no es válido")
	@Digits(integer=1,fraction=0,message="El valor del indicador de embarazo no es válido")
	@Column(nullable=true,precision=1,scale=0)
	private BigDecimal embarazada;

	@NotNull(message="El municipio de residencia es requerido")
	@ManyToOne
	@JoinColumn(name="MUNICIPIO_RESIDENCIA",referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipioResidencia;

	@NotNull(message="La comunidad, localidad o barrio en la cual reside la persona es requerida")
	@ManyToOne
	@JoinColumn(name="COMUNIDAD_RESIDENCIA",referencedColumnName="CODIGO")
	private Comunidad comunidadResidencia;
	
	@NotNull(message="La dirección de residencia de la persona es requerida")
	@Size(min=1,max=100,message="La dirección de residencia debe ser declarada")
	@Column(name="DIRECCION_RESIDENCIA",nullable=false)
	private String direccionResidencia;

	@ManyToOne
	@JoinColumn(name="MANZANA",referencedColumnName="CODIGO")
	private Manzana manzana;
	
	@ManyToOne
	@JoinColumn(name="VIVIENDA",referencedColumnName="CODIGO")
	private Vivienda vivienda;

    @Size(min=0,max=100,message="El total de caracteres para el empleador o lugar de trabajo no debe superar los 100 caracteres")
    @Column(name="EMPLEADOR",nullable=true,length=100)
	private String empleador;
	
    @Size(min=1,max=100,message="El total de caracteres para la persona referente no debe superar los 100 caracteres")
    @Column(name="PERSONA_REFERENTE",nullable=true,length=100)
	private String personaReferente;
	
    @Size(min=1,max=50,message="El total de caracteres para indicar el número de teléfono de la persona referente no debe superar los 50 caracteres")
    @Column(name="TELEFONO_REFERENTE",nullable=true,length=50)
	private String telefonoReferente;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="INICIO_SINTOMAS",nullable=true,updatable=true)
	private Date inicioSintomas;

	@NotNull(message="El indicador del manejo clínico es requerido")
	@DecimalMin(value="0",message="El valor del indicador del manejo clínico no es válido")
	@DecimalMax(value="1",message="El valor del indicador del manejo clínico no es válido")
	@Digits(integer=1,fraction=0,message="El valor del indicador del manejo clínico no es válido")
	@Column(name="MANEJO_CLINICO",nullable=true,precision=1,scale=0)
	private BigDecimal manejoClinico;

	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="INICIO_TRATAMIENTO",nullable=true,updatable=true)
	private Date inicioTratamiento;

	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="FIN_TRATAMIENTO",nullable=true,updatable=true)
	private Date finTratamiento;

	@DecimalMin(value="0",message="El número de días de tratamiento administrado bajo supervisión debe ser mayor o igual que cero")
	@DecimalMax(value="30",message="El número de días de tratamiento administrado bajo supervisión debe ser menor o igual que 30")
	@Digits(integer=2,fraction=0,message="El número de días de tratamiento en boca debe ser entero")
	@Column(name="TRATAMIENTO_ENBOCA",nullable=true,updatable=true)
	private BigDecimal tratamientoEnBoca;

	@DecimalMin(value="0",message="El número de días de tratamiento administrado sin supervisión debe ser mayor o igual que cero")
	@DecimalMax(value="30",message="El número de días de tratamiento administrado sin supervisión debe ser menor o igual que 30")
	@Digits(integer=2,fraction=0,message="El número de días de tratamiento remanente debe ser entero")
	@Column(name="TRATAMIENTO_REMANENTE",nullable=true,updatable=true)
	private BigDecimal tratamientoRemanente;
	
	@DecimalMin(value="0",message="El número total de tabletas de cloroquina, en boca y remanente, debe ser mayor o igual que cero")
	@DecimalMax(value="99",message="El número total de tabletas de cloroquina, en boca y remanente, debe ser menor o igual que 99")
	@Digits(integer=2,fraction=0,message="El total de tabletas de cloroquina, en boca y remanente, debe ser entero")
	@Column(name="CLOROQUINA",nullable=true,updatable=true)
	private BigDecimal cloroquina;

	@DecimalMin(value="0",message="El número total de tabletas de primaquina de 5mg, en boca y remanente, debe ser mayor o igual que cero")
	@DecimalMax(value="99",message="El número total de tabletas de primaquina de 5mg, en boca y remanente, debe ser menor o igual que 99")
	@Digits(integer=2,fraction=0,message="El total de tabletas de primaquina de 5mg, en boca y remanente, debe ser entero")
	@Column(name="PRIMAQUINA_5MG",nullable=true,updatable=true)
	private BigDecimal primaquina5mg;

	@DecimalMin(value="0",message="El número total de tabletas de primaquina de 15mg, en boca y remanente, debe ser mayor o igual que cero")
	@DecimalMax(value="99",message="El número total de tabletas de primaquina de 15mg, en boca y remanente, debe ser menor o igual que 99")
	@Digits(integer=2,fraction=0,message="El total de tabletas de primaquina de 15mg, en boca y remanente, debe ser entero")
	@Column(name="PRIMAQUINA_15MG",nullable=true,updatable=true)
	private BigDecimal primaquina15mg;

	@OneToOne(mappedBy="muestreoHematico",targetEntity=MuestreoPruebaRapida.class,fetch=FetchType.LAZY,optional=true, cascade=CascadeType.ALL)
	private MuestreoPruebaRapida pruebaRapida;

	@OneToOne(mappedBy="muestreoHematico",targetEntity=MuestreoDiagnostico.class,fetch=FetchType.LAZY,optional=true,cascade=CascadeType.ALL)
	private MuestreoDiagnostico diagnostico;
	
    public MuestreoHematico() {
    	tipoBusqueda=Integer.valueOf(TipoBusqueda.PASIVA.getCodigo());
    	manejoClinico=BigDecimal.ZERO;
    }

	public void setSisPersona(SisPersona sisPersona) {
		this.sisPersona = sisPersona;
	}

	public SisPersona getSisPersona() {
		return sisPersona;
	}

	public void setPuestoNotificacion(PuestoNotificacion puestoNotificacion) {
		this.puestoNotificacion = puestoNotificacion;
	}

	public PuestoNotificacion getPuestoNotificacion() {
		return puestoNotificacion;
	}

	/**
	 * @return Identificador del registro de muestreo hemático
	 */
	public long getMuestreoHematicoId() {
		return muestreoHematicoId;
	}

	/**
	 * Establece el identificador del registro del muestreo hemático
	 *
	 * @param muestreoHematicoId Identificador del muestreo hemático
	 */
	public void setMuestreoHematicoId(long muestreoHematicoId) {
		this.muestreoHematicoId = muestreoHematicoId;
	}

	/**
	 * Obtiene la entidad administrativa asociada a la notificación del muestreo hemático
	 *
	 * @return {@link EntidadAdtva} 
	 */
	public EntidadAdtva getEntidadNotificacion() {
		return entidadNotificacion;
	}

	/**
	 * Establece la entidad administrativa asociada a la notificación del
	 * muestreo hemático
	 *
	 * @param entidadNotificacion {@link EntidadAdtva}
	 */
	public void setEntidadNotificacion(EntidadAdtva entidadNotificacion) {
		this.entidadNotificacion = entidadNotificacion;
	}

	/**
	 * Obtiene la unidad de salud que realizó la notificación del muestreo hemático
	 *
	 * @return {@link Unidad}
	 */
	public Unidad getUnidadNotificacion() {
		return unidadNotificacion;
	}

	/**
	 * Establece la unidad de salud que realiza la notificación del muestreo hemático
	 *
	 * @param unidadNotificacion {@link Unidad}
	 */
	public void setUnidadNotificacion(Unidad unidadNotificacion) {
		this.unidadNotificacion = unidadNotificacion;
	}

	/**
	 * Establece el municipio en el cual se encuentra ubicada la unidad de salud
	 * que realizó la modificación.<p>  
	 * Puesto que es posible que se presente una reestructuración de la
	 * división política del país, a fin de mantener el dato histórico del municipio
	 * de notificación, este dato es almacenado y su referencia debe efectuarse
	 * a través de esta propiedad y no mediante la ubicación vigente de la unidad
	 * de salud.
	 *
	 * @return {@link DivisionPolitica}
	 */
	public DivisionPolitica getMunicipioNotificacion() {
		return municipioNotificacion;
	}

	/**
	 * Establece el municipio en el cual se encuentra ubicada geográficamente
	 * la unidad de salud que realiza la notificación del muestreo hemático.
	 *
	 * @param municipioNotificacion {@link DivisionPolitica}
	 */
	public void setMunicipioNotificacion(DivisionPolitica municipioNotificacion) {
		this.municipioNotificacion = municipioNotificacion;
	}

	/**
	 * Obtiene el número de lámina que identifica a la muestra de la gota gruesa.  Para
	 * los puestos de notificación corresponde al número consecutivo asignado al formato
	 * de muestreo hemático.
	 * 
	 * @return Número de la lámina
	 */
	public BigDecimal getNumeroLamina() {
		return numeroLamina;
	}

	/**
	 * Establece el número de lámina que identifica a la muestra de gota gruesa.  Para los
	 * puestos de notificación corresponde al número consecutivo asignado al formato.
	 *
	 * @param numeroLamina Número de lámina
	 */
	public void setNumeroLamina(BigDecimal numeroLamina) {
		this.numeroLamina = numeroLamina;
	}

	/**
	 * Retorna el tipo de búsqueda asociada al muestreo hemático realizado.
	 * @return 0 (Pasiva), 1 (Activa).
	 */
	public Integer getTipoBusqueda() {
		return tipoBusqueda;
	}

	/**
	 * Establece el tipo de búsqueda asociada al muestreo hemático.
	 * @param tipoBusqueda 0 (Pasiva), 1 (Activa)
	 */
	public void setTipoBusqueda(Integer tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	/**
	 * @return Fecha en que se realizó la toma de la muestra de gota gruesa
	 */
	public Date getFechaToma() {
		return fechaToma;
	}

	/**
	 * Establece la fecha en que se realizó la toma de la muestra de gota gruesa
	 * 
	 * @param fechaToma
	 */
	public void setFechaToma(Date fechaToma) {
		this.fechaToma = fechaToma;
	}

	/**
	 * Año al cual pertenece la semana epidemiológica, tomando como base la fecha de toma de 
	 * la muestra de gota gruesa.
	 * 
	 * @return Entero de 4 dígitos superior a 1995 
	 */
	public Integer getAñoEpidemiologico() {
		return añoEpidemiologico;
	}

	/**
	 * Establece el año al cual pertenece la semana epidemiológica.  
	 * 
	 * @param añoEpidemiologico Entero de 4 dígitos mayor o igual a 1995
	 */
	public void setAñoEpidemiologico(Integer añoEpidemiologico) {
		this.añoEpidemiologico = añoEpidemiologico;
	}

	/**
	 * Obtiene la semana epidemiológica tomando como base la fecha de toma
	 * de la muestra de gota gruesa
	 * 
	 * @return Entero del 1 al 52 que representa la semana epidemiológica.
	 */
	public Integer getSemanaEpidemiologica() {
		return semanaEpidemiologica;
	}

	/**
	 * Establece la semana epidemiológica tomando como base la fecha de
	 * toma de la muestra de gota gruesa
	 * 
	 * @param semanaEpidemiologica Entero del 1 al 52.
	 */
	public void setSemanaEpidemiologica(Integer semanaEpidemiologica) {
		this.semanaEpidemiologica = semanaEpidemiologica;
	}

	/**
	 * Obtiene la fecha de nacimiento de la persona, cuyo valor corresponde a la fecha
	 * de nacimiento declarada para la persona al momento de efectuar el registro del
	 * muestreo hemático.
	 * 
	 * @return Fecha de nacimiento
	 */
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * Establece la fecha de nacimiento de la persona; dicha fecha debe corresponder
	 * a la fecha de nacimiento al momento de efectuar el registro del muestreo hemático
	 * 
	 * @param fechaNacimiento Fecha de nacimiento
	 */
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * Obtiene el sexo de la persona
	 * 
	 * @return {@link Sexo}
	 */
	public Sexo getSexo() {
		return sexo;
	}

	/**
	 * Establece el sexo de la persona
	 * 
	 * @param sexo {@link Sexo}
	 */
	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	/**
	 * Obtiene la etnia de la persona
	 * 
	 * @return {@link Etnia}
	 */
	public Etnia getEtnia() {
		return etnia;
	}

	/**
	 * Establece la etnia de la persona
	 * 
	 * @param etnia {@link Etnia}
	 */
	public void setEtnia(Etnia etnia) {
		this.etnia = etnia;
	}

	/**
	 * Obtiene el valor que indica si la persona (en caso de sexo femenino) se encuentra
	 * embarazada (1) o no (0).  El valor es nulo cuando la persona no es del sexo femenino
	 * o si la edad es inferior o superior a las edades establecidas en los parámetros
	 * EDAD_MIN_EMBARAZO y EDAD_MAX_EMBARAZO.
	 * 
	 * @return <code>1</code> (Embarazada), <code>0</code> (No Embarazada), <code>null</code> (No aplica)
	 */
	public BigDecimal getEmbarazada() {
		return embarazada;
	}

	/**
	 * Establece el valor que indica si la persona se encuentra embarazada (1) o no
	 * embarazada (0) en caso de que la persona sea del sexo femenino y se encuentra en el
	 * rango de edades establecidos como de edad fértil (EDAD_MIN_EMBARAZO y EDAD_MAX_EMBARAZO)
	 * 
	 * @param embarazada <code>1</code> (Embarazada), <code>0</code> (No embarazada), <code>null</code> (No aplica) 
	 */
	public void setEmbarazada(BigDecimal embarazada) {
		this.embarazada = embarazada;
	}

	/**
	 * Obtiene el municipio de residencia de la persona al momento de efectuarse
	 * el muestreo hemático
	 * 
	 * @return {@link DivisionPolitica}
	 */
	public DivisionPolitica getMunicipioResidencia() {
		return municipioResidencia;
	}

	/**
	 * Establece el municipio de residencia de la persona sujeta al muestreo hemático.  Dicho
	 * municipio debe corresponder al municipio donde se encuentra el domicilio de la persona
	 * al momento de efectuarse el muestreo hemático.
	 * 
	 * @param municipioResidencia {@link DivisionPolitica}
	 */
	public void setMunicipioResidencia(DivisionPolitica municipioResidencia) {
		this.municipioResidencia = municipioResidencia;
	}

	/**
	 * Obtiene la localidad, comunidad o barrio de residencia de la persona al momento de efectuarse
	 * el muestreo hemático
	 * 
	 * @return {@link Comunidad}
	 */
	public Comunidad getComunidadResidencia() {
		return comunidadResidencia;
	}

	/**
	 * Establece la comunidad de residencia de la persona sujeta al muestreo hemático.  Dicha
	 * comunidad debe corresponder a la comunidad donde se encuentra el domicilio de la persona
	 * al momento de efectuarse el muestreo hemático.
	 * 
	 * @param comunidadResidencia {@link Comunidad}
	 */
	public void setComunidadResidencia(Comunidad comunidadResidencia) {
		this.comunidadResidencia = comunidadResidencia;
	}

	/**
	 * Obtiene la manzana en la cual reside la persona al momento de efectuarse
	 * el muestreo hemático
	 * 
	 * @return {@link Manzana}
	 */
	public Manzana getManzana() {
		return manzana;
	}

	/**
	 * Establece la manzana en la cual se encuentra ubicada la vivienda donde
	 * reside la persona al momento de efectuarse el muestreo hemático
	 * 
	 * @param manzana {@link Manzana}
	 */
	public void setManzana(Manzana manzana) {
		this.manzana = manzana;
	}

	/**
	 * Obtiene la viviedad donde reside habitualmente la persona al momento
	 * de efectuarse el muestreo hemático
	 * 
	 * @return {@link Vivienda}
	 */
	public Vivienda getVivienda() {
		return vivienda;
	}

	/**
	 * Establece la vivienda donde habitualmente reside la persona al momento
	 * de efectuar el muestreo hemático
	 * 
	 * @param vivienda {@link Vivienda}
	 */
	public void setVivienda(Vivienda vivienda) {
		this.vivienda = vivienda;
	}

	/**
	 * Obtiene el nombre de la persona de referencia o de contacto para la 
	 * localización de la persona a quien se le realizó el muestreo hemático
	 * @return Persona de referencia
	 */
	public String getPersonaReferente() {
		return personaReferente;
	}

	/**
	 * Establece el nombre de la persona de referencia o que sirve de contacto
	 * para la localización de la persona a la cual se le realizó el muestreo
	 * hemático.
	 * @param personaReferente Nombre y apellidos de la persona referente
	 */
	public void setPersonaReferente(String personaReferente) {
		this.personaReferente = (personaReferente==null || personaReferente.trim().isEmpty())?null:personaReferente.trim();
	}

	/**
	 * Obtiene el número de teléfon de la persona de referencia o de contacto para la 
	 * localización de la persona a quien se le realizó el muestreo hemático
	 * @return Número de teléfono
	 */
	public String getTelefonoReferente() {
		return telefonoReferente;
	}

	/**
	 * Establece el número de teléfono de la persona de referencia o que sirve de contacto
	 * para la localización de la persona a la cual se le realizó el muestreo hemático.
	 * @param telefonoReferente Número de teléfono
	 */
	public void setTelefonoReferente(String telefonoReferente) {
		this.telefonoReferente = (telefonoReferente ==null || telefonoReferente.trim().isEmpty())?null:telefonoReferente.trim();
	}

	/**
	 * Obtiene la fecha en la cual se iniciaron los síntomas asociados a la malaria.
	 * 
	 * @return Fecha de inicio de síntomas o <code>null</code> si la persona es asintomática
	 */
	public Date getInicioSintomas() {
		return inicioSintomas;
	}

	/**
	 * Establece la fecha en la cual se iniciaron los síntomas, si la persona es sintomática.
	 * @param inicioSintomas Fecha de inicio de los síntomas
	 */
	public void setInicioSintomas(Date inicioSintomas) {
		this.inicioSintomas = inicioSintomas;
	}

	/**
	 * Obtiene el valor que corresponde al manejo clínico utilizado con la persona a quien se le realizó 
	 * muestreo hemático. 
	 * @return 1 (Hospitalario) ó 0 (Ambulatorio)
	 */
	public BigDecimal getManejoClinico() {
		return manejoClinico;
	}

	/**
	 * Establece el valor que corresponde al manejo clínico utilizado con la persona a quien
	 * se le efectuó muestreo hemático y que es sospechoso de presentar malaria.
	 * 
	 * @param manejoClinico 1 (Hospitalario) ó 0 (Ambulatorio).
	 */
	public void setManejoClinico(BigDecimal manejoClinico) {
		this.manejoClinico = manejoClinico;
	}

	/**
	 * @return Fecha de inicio del tratamiento antimalárico.
	 */
	public Date getInicioTratamiento() {
		return inicioTratamiento;
	}

	/**
	 * Establece la fecha en la cual se dió inicio al tratamiento antimalárico
	 * @param inicioTratamiento Fecha de inicio de tratamiento
	 */
	public void setInicioTratamiento(Date inicioTratamiento) {
		this.inicioTratamiento = inicioTratamiento;
	}

	/**
	 * @return Fecha en la cual se finalizó el tratamiento antimalárico
	 */
	public Date getFinTratamiento() {
		return finTratamiento;
	}

	/**
	 * Establece la fecha en la cual se finalizó el tratamiento antimalárico
	 * @param finTratamiento Fecha de fin de tratamiento
	 */
	public void setFinTratamiento(Date finTratamiento) {
		this.finTratamiento = finTratamiento;
	}

	/**
	 * Obtiene el número de días de tratamiento administrado bajo supervisión
	 * del medicador
	 * @return Número de días
	 */
	public BigDecimal getTratamientoEnBoca() {
		return tratamientoEnBoca;
	}

	/**
	 * Establece el número de días de tratamiento administrado bajo supervisión
	 * del medicador
	 * @param tratamientoEnBoca Número de días
	 */
	public void setTratamientoEnBoca(BigDecimal tratamientoEnBoca) {
		this.tratamientoEnBoca = tratamientoEnBoca;
	}

	/**
	 * Obtiene el número de días de tratamiento administrado sin supervisión
	 * del medicador
	 * @return Número de días
	 */
	public BigDecimal getTratamientoRemanente() {
		return tratamientoRemanente;
	}

	/**
	 * Establece el número de días de tratamiento administrado sin supervisión
	 * del medicador
	 * @param tratamientoRemanente Número de días
	 */
	public void setTratamientoRemanente(BigDecimal tratamientoRemanente) {
		this.tratamientoRemanente = tratamientoRemanente;
	}

	/**
	 * Obtiene el número de total de tabletas de cloroquina utilizadas para
	 * el tratamiento antimalárico, ya sea en boca o remanente. 
	 * @return Número de tabletas
	 */
	public BigDecimal getCloroquina() {
		return cloroquina;
	}

	/**
	 * Establece el número de total de tabletas de cloroquina utilizadas para
	 * el tratamiento antimalárico, ya sea en boca o remanente. 
	 * @param cloroquina Número de tabletas
	 */
	public void setCloroquina(BigDecimal cloroquina) {
		this.cloroquina = cloroquina;
	}

	/**
	 * Obtiene el número de total de tabletas de primaquina de 5mg utilizadas para
	 * el tratamiento antimalárico, ya sea en boca o remanente. 
	 * @return Número de tabletas
	 */
	public BigDecimal getPrimaquina5mg() {
		return primaquina5mg;
	}

	/**
	 * Establece el número de total de tabletas de primaquina de 5mg utilizadas para
	 * el tratamiento antimalárico, ya sea en boca o remanente. 
	 * @param primaquina5mg Número de tabletas
	 */
	public void setPrimaquina5mg(BigDecimal primaquina5mg) {
		this.primaquina5mg = primaquina5mg;
	}

	/**
	 * Obtiene el número de total de tabletas de primaquina de 5mg utilizadas para
	 * el tratamiento antimalárico, ya sea en boca o remanente. 
	 * @return Número de tabletas
	 */
	public BigDecimal getPrimaquina15mg() {
		return primaquina15mg;
	}

	/**
	 * Establece el número de total de tabletas de primaquina de 15mg utilizadas para
	 * el tratamiento antimalárico, ya sea en boca o remanente. 
	 * @param primaquina15mg Número de tabletas
	 */
	public void setPrimaquina15mg(BigDecimal primaquina15mg) {
		this.primaquina15mg = primaquina15mg;
	}

	public void setPruebaRapida(MuestreoPruebaRapida pruebaRapida) {
		this.pruebaRapida = pruebaRapida;
	}

	public MuestreoPruebaRapida getPruebaRapida() {
		return pruebaRapida;
	}

	public void setDiagnostico(MuestreoDiagnostico diagnostico) {
		this.diagnostico = diagnostico;
	}

	public MuestreoDiagnostico getDiagnostico() {
		return diagnostico;
	}

	public void setClave(String clave) {
		this.clave = (clave==null || clave.trim().isEmpty())?null:clave.trim();
	}

	public String getClave() {
		return clave;
	}

	public void setDireccionResidencia(String direccionResidencia) {
		this.direccionResidencia = (direccionResidencia==null || direccionResidencia.trim().isEmpty())?null:direccionResidencia.trim();
	}

	public String getDireccionResidencia() {
		return direccionResidencia;
	}

	public void setEmpleador(String empleador) {
		this.empleador = (empleador==null || empleador.trim().isEmpty())?null:empleador.trim();
	}

	public String getEmpleador() {
		return empleador;
	}

}