// -----------------------------------------------
// Unidad.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.estructura;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.general.CategoriaUnidad;
import ni.gob.minsa.malaria.modelo.general.Regimen;
import ni.gob.minsa.malaria.modelo.general.TipoUnidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;

import org.eclipse.persistence.annotations.Cache;


import java.math.BigDecimal;
import java.util.Date;


/**
 * La clase persistente para la tabla UNIDADES de la base de datos
 * 
 */
@Entity
@Table(name="UNIDADES", schema="GENERAL")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="unidadesActivas",
			query="select tu from Unidad tu " +
					"where tu.pasivo='0' " +
					"order by tu.nombre"),
	@NamedQuery(							
			name="unidadesActivasPorEntidadAdtva",
			query="select tu from Unidad tu " +
					"where tu.pasivo='0' and " +
					"tu.entidadAdtva.entidadAdtvaId=:pEntidadId " +
					"order by tu.codigo"),
	@NamedQuery(							
			name="unidadesActivasPorMunicipio",
			query="select tu from Unidad tu " +
					"where tu.pasivo='0' AND " +
					"      tu.municipio.divisionPoliticaId=:pMunicipioId AND " +
					"      (:pTipoUnidadId=0 OR tu.tipoUnidad.tipoUnidadId=:pTipoUnidadId) " +
					"order by tu.nombre"),
	@NamedQuery(							
			name="unidadesActivasPorMunicipioYNombre",
			query="select tu from Unidad tu " +
					"where tu.pasivo='0' AND " +
					"      tu.municipio.divisionPoliticaId=:pMunicipioId AND " +
					"      (:pTipoUnidadId=0 OR tu.tipoUnidad.tipoUnidadId=:pTipoUnidadId) AND " +
					"      UPPER(tu.nombre) LIKE :pNombre " +
					"order by tu.nombre"),
	@NamedQuery(							
			name="unidadesActivasPorEntidadYTipo",
			query="select tu from Unidad tu " +
					"where tu.pasivo='0' and " +
					"tu.entidadAdtva.entidadAdtvaId=:pEntidadId and " +
					"tu.tipoUnidad.tipoUnidadId=:pTipoUnidadId " +
					"order by tu.codigo"),
	@NamedQuery(
			name="unidadesActivasPorEntidadYCategoria",
			query="select tu from Unidad tu " +
			"where tu.pasivo='0' and " +
			"tu.entidadAdtva.entidadAdtvaId=:pEntidadId and " +
			"tu.categoriaUnidad.codigo=:pCategoriaUnidad " +
			"order by tu.codigo"),
	@NamedQuery(
			name="UnidadesActivasPorPropiedad",
			query="select tu from Unidad tu where tu.unidadId in " +
					" (select distinct pu.unidad.unidadId from PropiedadUnidad pu " +
            		               "where pu.propiedad=:pPropiedad)"
	),
	@NamedQuery(
			name="unidadesPorNombre",
			query="select tu from Unidad tu " +
					"where (:pEntidadAdtvaId=0 or tu.entidadAdtva.entidadAdtvaId=:pEntidadAdtvaId) and " +
						"(:pMunicipioId=0 or tu.municipio.divisionPoliticaId=:pMunicipioId) and " +
						"(:pPasivo IS NULL or tu.pasivo=:pPasivo) and " +
						"UPPER(tu.nombre) LIKE :pNombre " +
					"order by tu.nombre")
})
public class Unidad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="UNIDADES_UNIDADID_GENERATOR", sequenceName="UNIDADES_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="UNIDADES_UNIDADID_GENERATOR")
	@Column(name="UNIDAD_ID", updatable=false)
	private long unidadId;

	@Column(name="CODIGO",nullable=false, insertable=true,unique=true)
	private long codigo;

	private String conectividad;

	@Column(name="DECLARA_SECTOR")
	private String declaraSector;

	@Column(name="RAZON_SOCIAL")
	private String razonSocial;
	
	private String direccion;

	private String email;

	private String fax;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO", updatable=false)
	private Date fechaRegistro;

	@Column(name="GRUPO_ECONOMICO",length=1)
	private String grupoEconomico;

	private BigDecimal latitud;

	private BigDecimal longitud;

	@Column(name="NOMBRE",nullable=false,unique=true,updatable=true,insertable=true,length=100)
	private String nombre;

	@Column(name="PASIVO",nullable=false,unique=false,updatable=true,insertable=true,length=1)
	private String pasivo;

	private String telefono;

	@Column(name="USUARIO_REGISTRO")
	private String usuarioRegistro;

	//bi-directional many-to-one association to Divisionpolitica
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MUNICIPIO",referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipio;

	//bi-directional many-to-one association to Tipounidade
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TIPO_UNIDAD",referencedColumnName="CODIGO")
	private TipoUnidad tipoUnidad;

	//bi-directional many-to-one association to Unidade
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ENTIDAD_ADTVA", referencedColumnName="CODIGO")
	private EntidadAdtva entidadAdtva;

	//asociación unidireccional muchos a uno con Regimen
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REGIMEN",referencedColumnName="CODIGO")
	private Regimen regimen;

	//asociación unidireccional muchos a uno con CategoriaUnidad
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CATEGORIA",referencedColumnName="CODIGO")
	private CategoriaUnidad categoriaUnidad;
    
	@OneToOne(mappedBy="unidad",targetEntity=UnidadAcceso.class,fetch=FetchType.LAZY,optional=true)
	private UnidadAcceso unidadAcceso;

    public Unidad() {
    }

    /**
     * Obtiene el identificador del objeto Unidad
     * 
     * @return Identificador del objeto Unidad
     */
	public long getUnidadId() {
		return this.unidadId;
	}

	/**
	 * Establece el identificador del objeto Unidad
	 * 
	 * @param unidadId Entero largo con el identifador del objeto
	 */
	public void setUnidadId(long unidadId) {
		this.unidadId = unidadId;
	}

	/**
	 * Obtiene el Código institucional para la unidad y que sirve de identificador
	 * para la relación con otros objetos
	 * 
	 * @return Código institucional de la unidad
	 */
	public long getCodigo() {
		return this.codigo;
	}

	/**
	 * Establece el Código institucional para la unidad y que sirve de identificador
	 * para la relación con otros objetos
	 * 
	 * @param codigo Entero largo que identifica a la unidad institucionalmente
	 */
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	/**
	 * Obtiene el valor que indica si la unidad tiene conexión con
	 * el sistema.
	 * 
	 * @return Cadena de caracteres con valor 0 ó 1
	 */
	public String getConectividad() {
		return this.conectividad;
	}

	/**
	 * Establece el valor que indica si la unidad tiene 
	 * conectividad con el sistema, independientemente si dispone 
	 * de conexión a internet en la unidad de forma directa o utiliza
	 * otros medios para su conexión. 
	 * <p>
	 * Valor: 1=Si; 0=No
	 * 
	 * @param conectividad Cadena de caracteres con 0 ó 1
	 */
	public void setConectividad(String conectividad) {
		this.conectividad = conectividad;
	}

	/**
	 * Obtiene el caracter 0 (No) o 1 (Si) que indica si
	 * la unidad tiene que declarar el sector al momento de
	 * registrar la actividad y metas de población.
	 * 
	 * @return Cadena de caracteres con 0 ó 1
	 */
	public String getDeclaraSector() {
		return this.declaraSector;
	}

	/**
	 * Establece si la unidad tiene que declarar el sector al momento 
	 * de registrar la actividad y metas de población.  Por ejemplo, 
	 * para aquellas unidades, tal como los hospitales que operativamente 
	 * no pueden declarar el sector al cual pertenece la población 
	 * atendida, deben configurarse con Declara Sector=No (0).
	 * <p>
	 * Valor 0: No; valor 1, Si.
	 * 
	 * @param declaraSector Cadena de caracteres con 0 ó 1
	 */
	public void setDeclaraSector(String declaraSector) {
		this.declaraSector = declaraSector;
	}

	/**
	 * Obtiene la dirección en la cual se encuentran ubicadas las
	 * instalaciones centrales de la unidad de salud.
	 * 
	 * @return Cadena de caracteres con la dirección de la unidad
	 */
	public String getDireccion() {
		return this.direccion;
	}

	/**
	 * Establece la dirección en la cual se encuentran ubicadas las
	 * instalaciones centrales de la unidad de salud.
	 * 
	 * @param direccion Cadena de caracteres con la dirección de la unidad
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * Obtiene el correo electrónico o email utilizado por
	 * la unidad de salud para comunicación institucional
	 * 
	 * @return Cadena de caracteres con el correo electrónico
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Establece el correo electrónico o email utilizado por la
	 * unidad para comunicación institucional.
	 * 
	 * @param email Cadena de caracteres con el correo electrónico
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Obtiene el número de fax utilizado por la unidad de
	 * salud para comunicación institucional
	 * 
	 * @return Cadena de caracteres con uno o mas números de fax
	 */
	public String getFax() {
		return this.fax;
	}

	/**
	 * Establece el número de fax que es utilizado por la unidad
	 * de salud para comunicación institucional.  Si existe más de
	 * un número de fax estos deben ser separados por una coma y
	 * un espacio.
	 * 
	 * @param fax Cadena de caracteres con el número de fax
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * Obtiene la fecha en la cual se registró la unidad
	 * 
	 * @return Fecha de Registro de la Unidad de Salud
	 */
	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	/** 
	 * Establece la fecha en la cual se registró la unidad.
	 * La fecha es establecida a nivel de base de datos de forma automática
	 * utilizando la fecha del sistema y por tanto, la fecha que aquí se
	 * establezca no será utilizada.
	 * 
	 * @param fechaRegistro Fecha de Registro
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getGrupoEconomico() {
		return this.grupoEconomico;
	}

	public void setGrupoEconomico(String grupoEconomico) {
		this.grupoEconomico = grupoEconomico;
	}

	public BigDecimal getLatitud() {
		return this.latitud;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public BigDecimal getLongitud() {
		return this.longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPasivo() {
		return this.pasivo;
	}

	public void setPasivo(String pasivo) {
		this.pasivo = pasivo;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public DivisionPolitica getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(DivisionPolitica municipio) {
		this.municipio = municipio;
	}
	
	public TipoUnidad getTipoUnidad() {
		return this.tipoUnidad;
	}

	public void setTipoUnidad(TipoUnidad tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}
		
	// esta asignación es importante para la creación el arbol
	@Override
	public String toString() {
		return nombre;
	}

	public void setEntidadAdtva(EntidadAdtva entidadAdtva) {
		this.entidadAdtva = entidadAdtva;
	}

	public EntidadAdtva getEntidadAdtva() {
		return entidadAdtva;
	}

	public void setRegimen(Regimen regimen) {
		this.regimen = regimen;
	}

	public Regimen getRegimen() {
		return regimen;
	}

	public void setCategoriaUnidad(CategoriaUnidad categoriaUnidad) {
		this.categoriaUnidad = categoriaUnidad;
	}

	public CategoriaUnidad getCategoriaUnidad() {
		return categoriaUnidad;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setUnidadAcceso(UnidadAcceso unidadAcceso) {
		this.unidadAcceso = unidadAcceso;
	}

	public UnidadAcceso getUnidadAcceso() {
		return unidadAcceso;
	}
	
}