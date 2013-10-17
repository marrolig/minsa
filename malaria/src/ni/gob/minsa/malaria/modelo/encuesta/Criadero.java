package ni.gob.minsa.malaria.modelo.encuesta;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import ni.gob.minsa.malaria.modelo.general.Catalogo;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the CRIADEROS database table.
 * 
 */
@Entity
@Table(name="CRIADEROS")
public class Criadero implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CRIADEROS_CRIADEROID_GENERATOR",sequenceName="SIVE.SEQ_CRIADEROID",allocationSize=1 )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CRIADEROS_CRIADEROID_GENERATOR")
	@Column(name="CRIADERO_ID")
	private long criaderoId;

	@Column(name="CODIGO")
	private String codigo;

	@NotNull(message="Nombre del criadero es requerido")
	@Column(name="NOMBRE",nullable=false)
	private String nombre;	
	
	@NotNull(message="Comunidad donde es identificado el criadero es requerido")
	@ManyToOne(targetEntity=Comunidad.class,fetch=FetchType.LAZY)
	@JoinColumn(name="COMUNIDAD",referencedColumnName="CODIGO",nullable=false)
	private Comunidad comunidad;

	@Column(name="DIRECCION")
	private String direccion;	

	@Column(name="LONGITUD")
	private BigDecimal longitud;	
	
	@Column(name="LATITUD")
	private BigDecimal latitud;
	
	@NotNull(message="Identificacion si el criadero es temporal o no requerido")
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="CLASIFICACION",referencedColumnName="CODIGO",nullable=false)	
	@Column(name="TIPO")
	private TiposCriaderos tipoCriadero;
	
	@NotNull(message="Identificacion de la clasificacion del criadero requerida")
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="CLASIFICACION",referencedColumnName="CODIGO",nullable=false)
	private ClasesCriaderos claseCriadero;
	
	@Column(name="DISTANCIA_CASA")
	private BigDecimal distanciaCasa;
	
	@Column(name="AREA_ACTUAL")
	private BigDecimal areaActual;

	@Column(name="AREA_MIN")
	private BigDecimal areaMin;	
	
	@Column(name="AREA_MAX")
	private BigDecimal areaMax;

	@NotNull(message="Identificion de abundancia relativa de vegetacion vertical requerido")
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="VEG_VERTICAL",referencedColumnName="CODIGO",nullable=false)
	private AbundanciaVegetacion vegVertical;	

	@NotNull(message="Identificion de abundancia relativa de vegetacion emergente requerido")
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="VEG_EMERGENTE",referencedColumnName="CODIGO",nullable=false)
	private AbundanciaVegetacion vegEmergente;

	@NotNull(message="Identificion de abundancia relativa de vegetacion flotante requerido")
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="VEG_FLOTANTE",referencedColumnName="CODIGO",nullable=false)
	private String vegFlotante;

	@NotNull(message="Identificion de abundancia relativa de vegetacion sumergida requerido")
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="VEG_SUMERGIDA",referencedColumnName="CODIGO",nullable=false)	
	private String vegSumergida;

	@NotNull(message="Identificion de abundancia relativa de insectos requerido")
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="FAUNA_INSECTO",referencedColumnName="CODIGO",nullable=false)		
	private AbundanciaFauna faunaInsecto;
	
	@NotNull(message="Identificion de abundancia relativa de pesces requerido")	
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="FAUNA_PECES",referencedColumnName="CODIGO",nullable=false)		
	private AbundanciaFauna faunaPeces;

	@NotNull(message="Identificacion de abundancia relativa de anfibios requerido")	
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="FAUNA_ANFIBIOS",referencedColumnName="CODIGO",nullable=false)			
	private AbundanciaFauna faunaAnfibios;

	@NotNull(message="Nivel de turbidez del agua requerido")
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="TURBIDEZ_AGUA",referencedColumnName="CODIGO",nullable=false)		
	private TurbidezAgua turbidezAgua;

	@NotNull(message="Movimiento de agua asociada al criadero requerido")
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="MOVIMIENTO_AGUA",referencedColumnName="CODIGO",nullable=false)		
	private MovimientoAgua movimientoAgua;
	
	@NotNull(message="Identificacion del nivel de exposicion al sol requerido")
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="MOVIMIENTO_AGUA",referencedColumnName="CODIGO",nullable=false)
	private ExposicionSol exposicionSol;	
	
	@Column(name="PH")
	private BigDecimal ph;

	@Column(name="TEMPERATURA")
	private BigDecimal temperatura;

	@Column(name="CLORO")
	private BigDecimal cloro;

	@Column(name="OBSERVACIONES")
	private String observaciones;

	@NotNull(message="Fecha de toma de datos requerido")
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_DATOS",nullable=false)
	private Date fechaDatos;

	@NotNull(message="Fecha de registro de datos requerido")
    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO",nullable=false)
	private Date fechaRegistro;
	
	@NotNull(message="Datos de usuario que registra datos requerido")
	@Column(name="USUARIO_REGISTRO",nullable=false)
	private String usuarioRegistro;


    public Criadero() {
    }


	public long getCriaderoId() {
		return criaderoId;
	}


	public void setCriaderoId(long criaderoId) {
		this.criaderoId = criaderoId;
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public Comunidad getComunidad() {
		return comunidad;
	}


	public void setComunidad(Comunidad comunidad) {
		this.comunidad = comunidad;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public BigDecimal getLongitud() {
		return longitud;
	}


	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}


	public BigDecimal getLatitud() {
		return latitud;
	}


	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}


	public TiposCriaderos getTipoCriadero() {
		return tipoCriadero;
	}


	public void setTipoCriadero(TiposCriaderos tipoCriadero) {
		this.tipoCriadero = tipoCriadero;
	}


	public ClasesCriaderos getClaseCriadero() {
		return claseCriadero;
	}


	public void setClaseCriadero(ClasesCriaderos claseCriadero) {
		this.claseCriadero = claseCriadero;
	}


	public BigDecimal getDistanciaCasa() {
		return distanciaCasa;
	}


	public void setDistanciaCasa(BigDecimal distanciaCasa) {
		this.distanciaCasa = distanciaCasa;
	}


	public BigDecimal getAreaActual() {
		return areaActual;
	}


	public void setAreaActual(BigDecimal areaActual) {
		this.areaActual = areaActual;
	}


	public BigDecimal getAreaMin() {
		return areaMin;
	}


	public void setAreaMin(BigDecimal areaMin) {
		this.areaMin = areaMin;
	}


	public BigDecimal getAreaMax() {
		return areaMax;
	}


	public void setAreaMax(BigDecimal areaMax) {
		this.areaMax = areaMax;
	}


	public AbundanciaVegetacion getVegVertical() {
		return vegVertical;
	}


	public void setVegVertical(AbundanciaVegetacion vegVertical) {
		this.vegVertical = vegVertical;
	}


	public AbundanciaVegetacion getVegEmergente() {
		return vegEmergente;
	}


	public void setVegEmergente(AbundanciaVegetacion vegEmergente) {
		this.vegEmergente = vegEmergente;
	}


	public String getVegFlotante() {
		return vegFlotante;
	}


	public void setVegFlotante(String vegFlotante) {
		this.vegFlotante = vegFlotante;
	}


	public String getVegSumergida() {
		return vegSumergida;
	}


	public void setVegSumergida(String vegSumergida) {
		this.vegSumergida = vegSumergida;
	}


	public AbundanciaFauna getFaunaInsecto() {
		return faunaInsecto;
	}


	public void setFaunaInsecto(AbundanciaFauna faunaInsecto) {
		this.faunaInsecto = faunaInsecto;
	}


	public AbundanciaFauna getFaunaPeces() {
		return faunaPeces;
	}


	public void setFaunaPeces(AbundanciaFauna faunaPeces) {
		this.faunaPeces = faunaPeces;
	}


	public AbundanciaFauna getFaunaAnfibios() {
		return faunaAnfibios;
	}


	public void setFaunaAnfibios(AbundanciaFauna faunaAnfibios) {
		this.faunaAnfibios = faunaAnfibios;
	}


	public TurbidezAgua getTurbidezAgua() {
		return turbidezAgua;
	}


	public void setTurbidezAgua(TurbidezAgua turbidezAgua) {
		this.turbidezAgua = turbidezAgua;
	}


	public MovimientoAgua getMovimientoAgua() {
		return movimientoAgua;
	}


	public void setMovimientoAgua(MovimientoAgua movimientoAgua) {
		this.movimientoAgua = movimientoAgua;
	}


	public ExposicionSol getExposicionSol() {
		return exposicionSol;
	}


	public void setExposicionSol(ExposicionSol exposicionSol) {
		this.exposicionSol = exposicionSol;
	}


	public BigDecimal getPh() {
		return ph;
	}


	public void setPh(BigDecimal ph) {
		this.ph = ph;
	}


	public BigDecimal getTemperatura() {
		return temperatura;
	}


	public void setTemperatura(BigDecimal temperatura) {
		this.temperatura = temperatura;
	}


	public BigDecimal getCloro() {
		return cloro;
	}


	public void setCloro(BigDecimal cloro) {
		this.cloro = cloro;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public Date getFechaDatos() {
		return fechaDatos;
	}


	public void setFechaDatos(Date fechaDatos) {
		this.fechaDatos = fechaDatos;
	}


	public Date getFechaRegistro() {
		return fechaRegistro;
	}


	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}


	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}


	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
    
    

}