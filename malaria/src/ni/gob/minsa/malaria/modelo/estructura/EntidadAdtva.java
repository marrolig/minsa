package ni.gob.minsa.malaria.modelo.estructura;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;

import org.eclipse.persistence.annotations.Cache;


import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the ENTIDADES_ADTVAS database table.
 * 
 */
@Entity
@Table(name="ENTIDADES_ADTVAS", schema="GENERAL")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="entidadesAdtvasActivas",
			query="select tea from EntidadAdtva tea " +
					"where tea.pasivo='0' " +
					"order by tea.nombre")
})
public class EntidadAdtva implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Comparator<EntidadAdtva> ORDEN_NOMBRE =
			new Comparator<EntidadAdtva>() {
				public int compare(EntidadAdtva e1, EntidadAdtva e2) {
					return e1.nombre.compareTo(e2.nombre);
				}
	};

	@Id
	@SequenceGenerator(name="ENTIDADES_ADTVAS_ENTIDADADTVAID_GENERATOR", sequenceName="ENTIDADES_ADTVAS_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ENTIDADES_ADTVAS_ENTIDADADTVAID_GENERATOR")
	@Column(name="ENTIDAD_ADTVA_ID", updatable=false)
	private long entidadAdtvaId;

	private long codigo;

	private String email;

	private String fax;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_REGISTRO", updatable=false)
	private Date fechaRegistro;

	private BigDecimal latitud;

	private BigDecimal longitud;

	// asociaci�n uni-directional muchos a uno con Divisionpolitica
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MUNICIPIO",referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipio;

	private String nombre;

	private String pasivo;

	private String telefono;

	@Column(name="USUARIO_REGISTRO", updatable=false)
	private String usuarioRegistro;

	//asociaci�n de autoreferencia bi-direccional muchos a uno
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DEPENDENCIA")
	private EntidadAdtva entidadSuperior;

	//asociaci�n de autoreferencia bi-direccional muchos a uno
	@OneToMany(mappedBy="entidadSuperior",targetEntity=ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva.class)
	private Set<EntidadAdtva> entidadesDependientes;

	//asociaci�n bidireccional uno a muchos con Unidad
	@OneToMany(mappedBy="entidadAdtva",targetEntity=ni.gob.minsa.malaria.modelo.estructura.Unidad.class,fetch=FetchType.LAZY)
	private Set<Unidad> unidades;
	
    public EntidadAdtva() {
    }

    /**
     * Obtiene el identificador la Entidad Administrativa
     * 
     * @return Identificador �nico de la Entidad Administrativa
     */
	public long getEntidadAdtvaId() {
		return this.entidadAdtvaId;
	}

	/**
	 * Establece el identificador de la Entidad Administrativa
	 * 
	 * @param entidadAdtvaId Entero largo con el identificador de la Entidad Administrativa
	 */
	public void setEntidadAdtvaId(long entidadAdtvaId) {
		this.entidadAdtvaId = entidadAdtvaId;
	}

	/**
	 * Obtiene el c�digo institucional asignado a la
	 * entidad administrativa
	 * 
	 * @return Entero largo con el c�digo institucional de la entidad
	 */
	public long getCodigo() {
		return this.codigo;
	}
	/**
	 * Establece el c�digo institucional asignado a la 
	 * entidad administrativa, el cual debe ser �nico y no nulo
	 * 
	 * @param codigo Entero largo con el c�digo institucional de la entidad administrativa
	 */
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	/**
	 * Obtiene el correo electr�nico de la entidad administrativa. 
	 * Retorna nulo en caso de no tenerlo declarado.
	 * 
	 * @return Cadena de caracteres con el correo electr�nico
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Establece el correo electr�nico de la entidad administrativa
	 * y utilizada por �sta para comunicaci�n institucional.
	 * 
	 * @param email Cadena de caracteres con el correo electr�nico
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Obtiene el n�mero de fax de la entidad administrativa. En caso de
	 * existir mas de un n�mero de fax, se retorna una cadena de caracteres
	 * con los n�meros de fax separados por comas.  Los n�meros de fax no
	 * utilizar�n ningun caracter de separaci�n o patr�n, tal como el gui�n
	 * o el punto.
	 * 
	 * @return Cadena de caracteres con uno o m�s n�meros de fax
	 */
	public String getFax() {
		return this.fax;
	}

	/**
	 * Establece el n�mero de fax de la entidad administrativa.  En caso
	 * de existir mas de un n�mero de fax, estos deber�n ser registrados
	 * utilizando una coma como caracter de separaci�n.
	 * 
	 * @param fax Cadena de caracteres con uno o m�s n�meros de fax
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * Obtiene la fecha en la cual se registr� la entidad administravia
	 * 
	 * @return Fecha de Registro de la Entidad
	 */
	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	/** 
	 * Establece la fecha en la cual se registr� la entidad administrativa.
	 * La fecha es establecida a nivel de base de datos de forma autom�tica
	 * utilizando la fecha del sistema y por tanto, la fecha que aqu� se
	 * establezca no ser� utilizada.
	 * 
	 * @param fechaRegistro Fecha de Registro
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
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

	public DivisionPolitica getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(DivisionPolitica municipio) {
		this.municipio = municipio;
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

	/**
	 * Obtiene la entidad administrativa de la cual depende la entidad 
	 * instanciada, es decir, la entidad administrativa superior o nodo
	 * padre.
	 * 
	 * @return Objeto con la entidad administrativa superior o nodo padre
	 */
	public EntidadAdtva getEntidadSuperior() {
		return this.entidadSuperior;
	}

	/**
	 * Establece la entidad administrativa de la cual depende la entidad
	 * instanciada, es decir, la entidad administrativa superior o nodo
	 * padre.
	 * <p>
	 * <b>Importante:</b>Esta propiedad no es utilizada en esta versi�n
	 * del aplicativo y por tanto todas la entidades administrativas se
	 * corresponden con los SILAIS existentes.
	 * 
	 * @param entidadSuperior Objeto con la entidad adminstrativa padre
	 */
	public void setEntidadSuperior(EntidadAdtva entidadSuperior) {
		this.entidadSuperior = entidadSuperior;
	}
	
	/**
	 * Obtiene un conjunto de objetos de todas las entidades administrativas
	 * que dependen de la entidad instanciada.
	 * 
	 * @return Conjunto de objetos del tipo {@link EntidadAdtva}
	 */
	public Set<EntidadAdtva> getEntidadesDependientes() {
		return this.entidadesDependientes;
	}

	public void setEntidadesDependientes(Set<EntidadAdtva> entidadesDependientes) {
		this.entidadesDependientes = entidadesDependientes;
	}
	
	// esta asignaci�n es importante
	@Override
	public String toString() {
		return nombre;
	}
	/**
	 * Obtiene el conjunto de objetos {@link Unidad} que responden 
	 * administrativamente a la entidad instanciada.
	 * 
	 * @param unidades Conjunto de Objetos {@link Unidad}
	 */
	public void setUnidades(Set<Unidad> unidades) {
		this.unidades = unidades;
	}
	/**
	 * Establece el conjunto de objetos {@link Unidad} que responden 
	 * administrativamente a la entidad instanciada.
	 * 
	 * @return Conjunto de Objetos {@link Unidad}
	 */
	public Set<Unidad> getUnidades() {
		return unidades;
	}
	
}