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
 * @author Marlon Arr�liga
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

    @NotNull(message="La entidad administrativa de notificaci�n es requerida")
    @ManyToOne(targetEntity=EntidadAdtva.class)
	@JoinColumn(name="ENTIDAD_NOTIFICACION",nullable=false,updatable=true,referencedColumnName="CODIGO")
	private EntidadAdtva entidadNotificacion;

    @NotNull(message="La unidad de salud que notifica es requerida")
    @ManyToOne(targetEntity=Unidad.class)
	@JoinColumn(name="UNIDAD_NOTIFICACION",nullable=false,updatable=true,referencedColumnName="CODIGO")
	private Unidad unidadNotificacion;

    @NotNull(message="El municipio de notificaci�n es requerido")
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

	@NotNull(message="El tipo de b�squeda es requerida")
	@DecimalMin(value="0",message="Tipo de b�squeda no v�lido")
	@DecimalMax(value="1",message="Tipo de b�squeda no v�lido")
	@Column(name="TIPO_BUSQUEDA",nullable=false)
	private Integer tipoBusqueda;
	
	@NotNull(message="La fecha en la cual se realiz� la toma de la gota gruesa es requerida")
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_TOMA",nullable=false)
	private Date fechaToma;
    
	@NotNull(message="El a�o epidemiol�gico es requerido")
	@Digits(integer=4,fraction=0,message="Declaraci�n de a�o epidemiol�gico no v�lida")
	@DecimalMin(value="1995",message="El a�o epidemiol�gico no puede ser inferior a 1995")
	@Column(name="A�O_EPIDEMIOLOGICO",nullable=false,updatable=true)
    private Integer a�oEpidemiologico;
    
    @NotNull(message="La semana epidemiol�gica es requerida")
	@DecimalMin(value="1",message="La semana epidemiol�gica debe ser igual o mayor que 1")
	@DecimalMax(value="52",message="La semana epidemiol�gica debe ser igual o menor que 52")
	@Column(name="SEMANA_EPIDEMIOLOGICA",nullable=false,updatable=true)
	private Integer semanaEpidemiologica;
	
	@NotNull(message="La declaraci�n de la persona a la cual se le realiza el muestreo hem�tico, es requerida")
    @ManyToOne(targetEntity=SisPersona.class)
	@JoinColumn(name="PERSONA",nullable=false,updatable=false)
	private SisPersona sisPersona;

	@NotNull(message="La fecha de nacimiento es requerida")
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_NACIMIENTO",nullable=false,updatable=false)
	private Date fechaNacimiento;

	@NotNull(message="La declaraci�n del sexo de la persona es requerida")
	@ManyToOne
	@JoinColumn(name="SEXO",referencedColumnName="CODIGO")
	private Sexo sexo;

	@ManyToOne
	@JoinColumn(name="ETNIA",referencedColumnName="CODIGO")
	private Etnia etnia;
	
	@DecimalMin(value="0",message="El valor del indicador de embarazo no es v�lido")
	@DecimalMax(value="1",message="El valor del indicador de embarazo no es v�lido")
	@Digits(integer=1,fraction=0,message="El valor del indicador de embarazo no es v�lido")
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
	
	@NotNull(message="La direcci�n de residencia de la persona es requerida")
	@Size(min=1,max=100,message="La direcci�n de residencia debe ser declarada")
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
	
    @Size(min=1,max=50,message="El total de caracteres para indicar el n�mero de tel�fono de la persona referente no debe superar los 50 caracteres")
    @Column(name="TELEFONO_REFERENTE",nullable=true,length=50)
	private String telefonoReferente;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="INICIO_SINTOMAS",nullable=true,updatable=true)
	private Date inicioSintomas;

	@NotNull(message="El indicador del manejo cl�nico es requerido")
	@DecimalMin(value="0",message="El valor del indicador del manejo cl�nico no es v�lido")
	@DecimalMax(value="1",message="El valor del indicador del manejo cl�nico no es v�lido")
	@Digits(integer=1,fraction=0,message="El valor del indicador del manejo cl�nico no es v�lido")
	@Column(name="MANEJO_CLINICO",nullable=true,precision=1,scale=0)
	private BigDecimal manejoClinico;

	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="INICIO_TRATAMIENTO",nullable=true,updatable=true)
	private Date inicioTratamiento;

	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="FIN_TRATAMIENTO",nullable=true,updatable=true)
	private Date finTratamiento;

	@DecimalMin(value="0",message="El n�mero de d�as de tratamiento administrado bajo supervisi�n debe ser mayor o igual que cero")
	@DecimalMax(value="30",message="El n�mero de d�as de tratamiento administrado bajo supervisi�n debe ser menor o igual que 30")
	@Digits(integer=2,fraction=0,message="El n�mero de d�as de tratamiento en boca debe ser entero")
	@Column(name="TRATAMIENTO_ENBOCA",nullable=true,updatable=true)
	private BigDecimal tratamientoEnBoca;

	@DecimalMin(value="0",message="El n�mero de d�as de tratamiento administrado sin supervisi�n debe ser mayor o igual que cero")
	@DecimalMax(value="30",message="El n�mero de d�as de tratamiento administrado sin supervisi�n debe ser menor o igual que 30")
	@Digits(integer=2,fraction=0,message="El n�mero de d�as de tratamiento remanente debe ser entero")
	@Column(name="TRATAMIENTO_REMANENTE",nullable=true,updatable=true)
	private BigDecimal tratamientoRemanente;
	
	@DecimalMin(value="0",message="El n�mero total de tabletas de cloroquina, en boca y remanente, debe ser mayor o igual que cero")
	@DecimalMax(value="99",message="El n�mero total de tabletas de cloroquina, en boca y remanente, debe ser menor o igual que 99")
	@Digits(integer=2,fraction=0,message="El total de tabletas de cloroquina, en boca y remanente, debe ser entero")
	@Column(name="CLOROQUINA",nullable=true,updatable=true)
	private BigDecimal cloroquina;

	@DecimalMin(value="0",message="El n�mero total de tabletas de primaquina de 5mg, en boca y remanente, debe ser mayor o igual que cero")
	@DecimalMax(value="99",message="El n�mero total de tabletas de primaquina de 5mg, en boca y remanente, debe ser menor o igual que 99")
	@Digits(integer=2,fraction=0,message="El total de tabletas de primaquina de 5mg, en boca y remanente, debe ser entero")
	@Column(name="PRIMAQUINA_5MG",nullable=true,updatable=true)
	private BigDecimal primaquina5mg;

	@DecimalMin(value="0",message="El n�mero total de tabletas de primaquina de 15mg, en boca y remanente, debe ser mayor o igual que cero")
	@DecimalMax(value="99",message="El n�mero total de tabletas de primaquina de 15mg, en boca y remanente, debe ser menor o igual que 99")
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
	 * @return Identificador del registro de muestreo hem�tico
	 */
	public long getMuestreoHematicoId() {
		return muestreoHematicoId;
	}

	/**
	 * Establece el identificador del registro del muestreo hem�tico
	 *
	 * @param muestreoHematicoId Identificador del muestreo hem�tico
	 */
	public void setMuestreoHematicoId(long muestreoHematicoId) {
		this.muestreoHematicoId = muestreoHematicoId;
	}

	/**
	 * Obtiene la entidad administrativa asociada a la notificaci�n del muestreo hem�tico
	 *
	 * @return {@link EntidadAdtva} 
	 */
	public EntidadAdtva getEntidadNotificacion() {
		return entidadNotificacion;
	}

	/**
	 * Establece la entidad administrativa asociada a la notificaci�n del
	 * muestreo hem�tico
	 *
	 * @param entidadNotificacion {@link EntidadAdtva}
	 */
	public void setEntidadNotificacion(EntidadAdtva entidadNotificacion) {
		this.entidadNotificacion = entidadNotificacion;
	}

	/**
	 * Obtiene la unidad de salud que realiz� la notificaci�n del muestreo hem�tico
	 *
	 * @return {@link Unidad}
	 */
	public Unidad getUnidadNotificacion() {
		return unidadNotificacion;
	}

	/**
	 * Establece la unidad de salud que realiza la notificaci�n del muestreo hem�tico
	 *
	 * @param unidadNotificacion {@link Unidad}
	 */
	public void setUnidadNotificacion(Unidad unidadNotificacion) {
		this.unidadNotificacion = unidadNotificacion;
	}

	/**
	 * Establece el municipio en el cual se encuentra ubicada la unidad de salud
	 * que realiz� la modificaci�n.<p>  
	 * Puesto que es posible que se presente una reestructuraci�n de la
	 * divisi�n pol�tica del pa�s, a fin de mantener el dato hist�rico del municipio
	 * de notificaci�n, este dato es almacenado y su referencia debe efectuarse
	 * a trav�s de esta propiedad y no mediante la ubicaci�n vigente de la unidad
	 * de salud.
	 *
	 * @return {@link DivisionPolitica}
	 */
	public DivisionPolitica getMunicipioNotificacion() {
		return municipioNotificacion;
	}

	/**
	 * Establece el municipio en el cual se encuentra ubicada geogr�ficamente
	 * la unidad de salud que realiza la notificaci�n del muestreo hem�tico.
	 *
	 * @param municipioNotificacion {@link DivisionPolitica}
	 */
	public void setMunicipioNotificacion(DivisionPolitica municipioNotificacion) {
		this.municipioNotificacion = municipioNotificacion;
	}

	/**
	 * Obtiene el n�mero de l�mina que identifica a la muestra de la gota gruesa.  Para
	 * los puestos de notificaci�n corresponde al n�mero consecutivo asignado al formato
	 * de muestreo hem�tico.
	 * 
	 * @return N�mero de la l�mina
	 */
	public BigDecimal getNumeroLamina() {
		return numeroLamina;
	}

	/**
	 * Establece el n�mero de l�mina que identifica a la muestra de gota gruesa.  Para los
	 * puestos de notificaci�n corresponde al n�mero consecutivo asignado al formato.
	 *
	 * @param numeroLamina N�mero de l�mina
	 */
	public void setNumeroLamina(BigDecimal numeroLamina) {
		this.numeroLamina = numeroLamina;
	}

	/**
	 * Retorna el tipo de b�squeda asociada al muestreo hem�tico realizado.
	 * @return 0 (Pasiva), 1 (Activa).
	 */
	public Integer getTipoBusqueda() {
		return tipoBusqueda;
	}

	/**
	 * Establece el tipo de b�squeda asociada al muestreo hem�tico.
	 * @param tipoBusqueda 0 (Pasiva), 1 (Activa)
	 */
	public void setTipoBusqueda(Integer tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	/**
	 * @return Fecha en que se realiz� la toma de la muestra de gota gruesa
	 */
	public Date getFechaToma() {
		return fechaToma;
	}

	/**
	 * Establece la fecha en que se realiz� la toma de la muestra de gota gruesa
	 * 
	 * @param fechaToma
	 */
	public void setFechaToma(Date fechaToma) {
		this.fechaToma = fechaToma;
	}

	/**
	 * A�o al cual pertenece la semana epidemiol�gica, tomando como base la fecha de toma de 
	 * la muestra de gota gruesa.
	 * 
	 * @return Entero de 4 d�gitos superior a 1995 
	 */
	public Integer getA�oEpidemiologico() {
		return a�oEpidemiologico;
	}

	/**
	 * Establece el a�o al cual pertenece la semana epidemiol�gica.  
	 * 
	 * @param a�oEpidemiologico Entero de 4 d�gitos mayor o igual a 1995
	 */
	public void setA�oEpidemiologico(Integer a�oEpidemiologico) {
		this.a�oEpidemiologico = a�oEpidemiologico;
	}

	/**
	 * Obtiene la semana epidemiol�gica tomando como base la fecha de toma
	 * de la muestra de gota gruesa
	 * 
	 * @return Entero del 1 al 52 que representa la semana epidemiol�gica.
	 */
	public Integer getSemanaEpidemiologica() {
		return semanaEpidemiologica;
	}

	/**
	 * Establece la semana epidemiol�gica tomando como base la fecha de
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
	 * muestreo hem�tico.
	 * 
	 * @return Fecha de nacimiento
	 */
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * Establece la fecha de nacimiento de la persona; dicha fecha debe corresponder
	 * a la fecha de nacimiento al momento de efectuar el registro del muestreo hem�tico
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
	 * o si la edad es inferior o superior a las edades establecidas en los par�metros
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
	 * rango de edades establecidos como de edad f�rtil (EDAD_MIN_EMBARAZO y EDAD_MAX_EMBARAZO)
	 * 
	 * @param embarazada <code>1</code> (Embarazada), <code>0</code> (No embarazada), <code>null</code> (No aplica) 
	 */
	public void setEmbarazada(BigDecimal embarazada) {
		this.embarazada = embarazada;
	}

	/**
	 * Obtiene el municipio de residencia de la persona al momento de efectuarse
	 * el muestreo hem�tico
	 * 
	 * @return {@link DivisionPolitica}
	 */
	public DivisionPolitica getMunicipioResidencia() {
		return municipioResidencia;
	}

	/**
	 * Establece el municipio de residencia de la persona sujeta al muestreo hem�tico.  Dicho
	 * municipio debe corresponder al municipio donde se encuentra el domicilio de la persona
	 * al momento de efectuarse el muestreo hem�tico.
	 * 
	 * @param municipioResidencia {@link DivisionPolitica}
	 */
	public void setMunicipioResidencia(DivisionPolitica municipioResidencia) {
		this.municipioResidencia = municipioResidencia;
	}

	/**
	 * Obtiene la localidad, comunidad o barrio de residencia de la persona al momento de efectuarse
	 * el muestreo hem�tico
	 * 
	 * @return {@link Comunidad}
	 */
	public Comunidad getComunidadResidencia() {
		return comunidadResidencia;
	}

	/**
	 * Establece la comunidad de residencia de la persona sujeta al muestreo hem�tico.  Dicha
	 * comunidad debe corresponder a la comunidad donde se encuentra el domicilio de la persona
	 * al momento de efectuarse el muestreo hem�tico.
	 * 
	 * @param comunidadResidencia {@link Comunidad}
	 */
	public void setComunidadResidencia(Comunidad comunidadResidencia) {
		this.comunidadResidencia = comunidadResidencia;
	}

	/**
	 * Obtiene la manzana en la cual reside la persona al momento de efectuarse
	 * el muestreo hem�tico
	 * 
	 * @return {@link Manzana}
	 */
	public Manzana getManzana() {
		return manzana;
	}

	/**
	 * Establece la manzana en la cual se encuentra ubicada la vivienda donde
	 * reside la persona al momento de efectuarse el muestreo hem�tico
	 * 
	 * @param manzana {@link Manzana}
	 */
	public void setManzana(Manzana manzana) {
		this.manzana = manzana;
	}

	/**
	 * Obtiene la viviedad donde reside habitualmente la persona al momento
	 * de efectuarse el muestreo hem�tico
	 * 
	 * @return {@link Vivienda}
	 */
	public Vivienda getVivienda() {
		return vivienda;
	}

	/**
	 * Establece la vivienda donde habitualmente reside la persona al momento
	 * de efectuar el muestreo hem�tico
	 * 
	 * @param vivienda {@link Vivienda}
	 */
	public void setVivienda(Vivienda vivienda) {
		this.vivienda = vivienda;
	}

	/**
	 * Obtiene el nombre de la persona de referencia o de contacto para la 
	 * localizaci�n de la persona a quien se le realiz� el muestreo hem�tico
	 * @return Persona de referencia
	 */
	public String getPersonaReferente() {
		return personaReferente;
	}

	/**
	 * Establece el nombre de la persona de referencia o que sirve de contacto
	 * para la localizaci�n de la persona a la cual se le realiz� el muestreo
	 * hem�tico.
	 * @param personaReferente Nombre y apellidos de la persona referente
	 */
	public void setPersonaReferente(String personaReferente) {
		this.personaReferente = (personaReferente==null || personaReferente.trim().isEmpty())?null:personaReferente.trim();
	}

	/**
	 * Obtiene el n�mero de tel�fon de la persona de referencia o de contacto para la 
	 * localizaci�n de la persona a quien se le realiz� el muestreo hem�tico
	 * @return N�mero de tel�fono
	 */
	public String getTelefonoReferente() {
		return telefonoReferente;
	}

	/**
	 * Establece el n�mero de tel�fono de la persona de referencia o que sirve de contacto
	 * para la localizaci�n de la persona a la cual se le realiz� el muestreo hem�tico.
	 * @param telefonoReferente N�mero de tel�fono
	 */
	public void setTelefonoReferente(String telefonoReferente) {
		this.telefonoReferente = (telefonoReferente ==null || telefonoReferente.trim().isEmpty())?null:telefonoReferente.trim();
	}

	/**
	 * Obtiene la fecha en la cual se iniciaron los s�ntomas asociados a la malaria.
	 * 
	 * @return Fecha de inicio de s�ntomas o <code>null</code> si la persona es asintom�tica
	 */
	public Date getInicioSintomas() {
		return inicioSintomas;
	}

	/**
	 * Establece la fecha en la cual se iniciaron los s�ntomas, si la persona es sintom�tica.
	 * @param inicioSintomas Fecha de inicio de los s�ntomas
	 */
	public void setInicioSintomas(Date inicioSintomas) {
		this.inicioSintomas = inicioSintomas;
	}

	/**
	 * Obtiene el valor que corresponde al manejo cl�nico utilizado con la persona a quien se le realiz� 
	 * muestreo hem�tico. 
	 * @return 1 (Hospitalario) � 0 (Ambulatorio)
	 */
	public BigDecimal getManejoClinico() {
		return manejoClinico;
	}

	/**
	 * Establece el valor que corresponde al manejo cl�nico utilizado con la persona a quien
	 * se le efectu� muestreo hem�tico y que es sospechoso de presentar malaria.
	 * 
	 * @param manejoClinico 1 (Hospitalario) � 0 (Ambulatorio).
	 */
	public void setManejoClinico(BigDecimal manejoClinico) {
		this.manejoClinico = manejoClinico;
	}

	/**
	 * @return Fecha de inicio del tratamiento antimal�rico.
	 */
	public Date getInicioTratamiento() {
		return inicioTratamiento;
	}

	/**
	 * Establece la fecha en la cual se di� inicio al tratamiento antimal�rico
	 * @param inicioTratamiento Fecha de inicio de tratamiento
	 */
	public void setInicioTratamiento(Date inicioTratamiento) {
		this.inicioTratamiento = inicioTratamiento;
	}

	/**
	 * @return Fecha en la cual se finaliz� el tratamiento antimal�rico
	 */
	public Date getFinTratamiento() {
		return finTratamiento;
	}

	/**
	 * Establece la fecha en la cual se finaliz� el tratamiento antimal�rico
	 * @param finTratamiento Fecha de fin de tratamiento
	 */
	public void setFinTratamiento(Date finTratamiento) {
		this.finTratamiento = finTratamiento;
	}

	/**
	 * Obtiene el n�mero de d�as de tratamiento administrado bajo supervisi�n
	 * del medicador
	 * @return N�mero de d�as
	 */
	public BigDecimal getTratamientoEnBoca() {
		return tratamientoEnBoca;
	}

	/**
	 * Establece el n�mero de d�as de tratamiento administrado bajo supervisi�n
	 * del medicador
	 * @param tratamientoEnBoca N�mero de d�as
	 */
	public void setTratamientoEnBoca(BigDecimal tratamientoEnBoca) {
		this.tratamientoEnBoca = tratamientoEnBoca;
	}

	/**
	 * Obtiene el n�mero de d�as de tratamiento administrado sin supervisi�n
	 * del medicador
	 * @return N�mero de d�as
	 */
	public BigDecimal getTratamientoRemanente() {
		return tratamientoRemanente;
	}

	/**
	 * Establece el n�mero de d�as de tratamiento administrado sin supervisi�n
	 * del medicador
	 * @param tratamientoRemanente N�mero de d�as
	 */
	public void setTratamientoRemanente(BigDecimal tratamientoRemanente) {
		this.tratamientoRemanente = tratamientoRemanente;
	}

	/**
	 * Obtiene el n�mero de total de tabletas de cloroquina utilizadas para
	 * el tratamiento antimal�rico, ya sea en boca o remanente. 
	 * @return N�mero de tabletas
	 */
	public BigDecimal getCloroquina() {
		return cloroquina;
	}

	/**
	 * Establece el n�mero de total de tabletas de cloroquina utilizadas para
	 * el tratamiento antimal�rico, ya sea en boca o remanente. 
	 * @param cloroquina N�mero de tabletas
	 */
	public void setCloroquina(BigDecimal cloroquina) {
		this.cloroquina = cloroquina;
	}

	/**
	 * Obtiene el n�mero de total de tabletas de primaquina de 5mg utilizadas para
	 * el tratamiento antimal�rico, ya sea en boca o remanente. 
	 * @return N�mero de tabletas
	 */
	public BigDecimal getPrimaquina5mg() {
		return primaquina5mg;
	}

	/**
	 * Establece el n�mero de total de tabletas de primaquina de 5mg utilizadas para
	 * el tratamiento antimal�rico, ya sea en boca o remanente. 
	 * @param primaquina5mg N�mero de tabletas
	 */
	public void setPrimaquina5mg(BigDecimal primaquina5mg) {
		this.primaquina5mg = primaquina5mg;
	}

	/**
	 * Obtiene el n�mero de total de tabletas de primaquina de 5mg utilizadas para
	 * el tratamiento antimal�rico, ya sea en boca o remanente. 
	 * @return N�mero de tabletas
	 */
	public BigDecimal getPrimaquina15mg() {
		return primaquina15mg;
	}

	/**
	 * Establece el n�mero de total de tabletas de primaquina de 15mg utilizadas para
	 * el tratamiento antimal�rico, ya sea en boca o remanente. 
	 * @param primaquina15mg N�mero de tabletas
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