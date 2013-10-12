// -----------------------------------------------
// SisPersona.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.sis;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.general.Ocupacion;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Pais;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla SIS_PERSONAS 
 * en el esquema SIS.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 20/06/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="SIS_PERSONAS",schema="SIS",
			uniqueConstraints=@UniqueConstraint(columnNames={"IDENTIFICACION_HSE"}))
@Cache(alwaysRefresh=true,disableHits=true)
public class SisPersona extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PERSONA_ID")
	private long personaId;

	@Column(name="IDENTIFICACION_HSE")
	private String historiaSalud;

	@Column(name="IDENTIFICACION")
	private String identificacion;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_NACIMIENTO")
	private Date fechaNacimiento;

	@Column(name="PRIMER_NOMBRE")
	private String primerNombre;

	@Column(name="SEGUNDO_NOMBRE")
	private String segundoNombre;

	@Column(name="PRIMER_APELLIDO")
	private String primerApellido;

	@Column(name="SEGUNDO_APELLIDO")
	private String segundoApellido;

	@Column(name="DIRECCION_RESIDENCIA")
	private String direccionResidencia;

	@Column(name="TELEFONO_RESIDENCIA")
	private String telefonoResidencia;

	@Column(name="TELEFONO_MOVIL")
	private String telefonoMovil;

	@Column(name="NUMERO_ASEGURADO")
	private String numeroAsegurado;

	@ManyToOne
	@JoinColumn(name="CODIGO_TIPOIDENTIFICACION",referencedColumnName="CODIGO")
	private TipoIdentificacion tipoIdentificacion;

	@ManyToOne
	@JoinColumn(name="CODIGO_ETNIA",referencedColumnName="CODIGO")
	private Etnia etnia;
	
	@ManyToOne
	@JoinColumn(name="CODIGO_ESCOLARIDAD",referencedColumnName="CODIGO")
	private Escolaridad escolaridad;
	
	@ManyToOne
	@JoinColumn(name="CODIGO_ESTADOCIVIL",referencedColumnName="CODIGO")
	private EstadoCivil estadoCivil;
	
	@ManyToOne
	@JoinColumn(name="CODIGO_TIPOASEGURADO",referencedColumnName="CODIGO")
	private TipoAsegurado tipoAsegurado;
	
	@ManyToOne
	@JoinColumn(name="CODIGO_SEXO",referencedColumnName="CODIGO")
	private Sexo sexo;

	@ManyToOne
	@JoinColumn(name="CODIGO_OCUPACION",referencedColumnName="CODIGO")
	private Ocupacion ocupacion;

	@ManyToOne
	@JoinColumn(name="CODIGO_MUNICIPIO_RESIDENCIA",referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipioResidencia;

	@ManyToOne
	@JoinColumn(name="CODIGO_COMUNIDAD_RESIDENCIA",referencedColumnName="CODIGO")
	private Comunidad comunidadResidencia;
	
	@ManyToOne
	@JoinColumn(name="CODIGO_MUNICIPIO_NACIMIENTO",referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipioNacimiento;

	@ManyToOne
	@JoinColumn(name="CODIGO_PAIS_NACIMIENTO",referencedColumnName="CODIGO_ALFADOS")
	private Pais paisNacimiento;
	
	@Column(name="CONFIRMADO", nullable=false, precision=1, scale=0)
	private boolean Confirmado;
	
	@Column(name="SND_NOMBRE",length=200)
	private String sndNombre;
	
	@SuppressWarnings("unused")
	@Transient
	private String nombreCompleto;

    public SisPersona() {
    }

	public long getPersonaId() {
		return personaId;
	}

	public void setPersonaId(long personaId) {
		this.personaId = personaId;
	}

	public String getHistoriaSalud() {
		return historiaSalud;
	}

	public void setHistoriaSalud(String historiaSalud) {
		this.historiaSalud = historiaSalud;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getDireccionResidencia() {
		return direccionResidencia;
	}

	public void setDireccionResidencia(String direccionResidencia) {
		this.direccionResidencia = direccionResidencia;
	}

	public String getTelefonoResidencia() {
		return telefonoResidencia;
	}

	public void setTelefonoResidencia(String telefonoResidencia) {
		this.telefonoResidencia = telefonoResidencia;
	}

	public String getTelefonoMovil() {
		return telefonoMovil;
	}

	public void setTelefonoMovil(String telefonoMovil) {
		this.telefonoMovil = telefonoMovil;
	}

	public String getNumeroAsegurado() {
		return numeroAsegurado;
	}

	public void setNumeroAsegurado(String numeroAsegurado) {
		this.numeroAsegurado = numeroAsegurado;
	}

	public TipoIdentificacion getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public Etnia getEtnia() {
		return etnia;
	}

	public void setEtnia(Etnia etnia) {
		this.etnia = etnia;
	}

	public Escolaridad getEscolaridad() {
		return escolaridad;
	}

	public void setEscolaridad(Escolaridad escolaridad) {
		this.escolaridad = escolaridad;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public TipoAsegurado getTipoAsegurado() {
		return tipoAsegurado;
	}

	public void setTipoAsegurado(TipoAsegurado tipoAsegurado) {
		this.tipoAsegurado = tipoAsegurado;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Ocupacion getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(Ocupacion ocupacion) {
		this.ocupacion = ocupacion;
	}

	public DivisionPolitica getMunicipioResidencia() {
		return municipioResidencia;
	}

	public void setMunicipioResidencia(DivisionPolitica municipioResidencia) {
		this.municipioResidencia = municipioResidencia;
	}

	public Comunidad getComunidadResidencia() {
		return comunidadResidencia;
	}

	public void setComunidadResidencia(Comunidad comunidadResidencia) {
		this.comunidadResidencia = comunidadResidencia;
	}

	public DivisionPolitica getMunicipioNacimiento() {
		return municipioNacimiento;
	}

	public void setMunicipioNacimiento(DivisionPolitica municipioNacimiento) {
		this.municipioNacimiento = municipioNacimiento;
	}

	public Pais getPaisNacimiento() {
		return paisNacimiento;
	}

	public void setPaisNacimiento(Pais paisNacimiento) {
		this.paisNacimiento = paisNacimiento;
	}

	public boolean isConfirmado() {
		return Confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		Confirmado = confirmado;
	}

	public String getSndNombre() {
		return sndNombre;
	}

	public void setSndNombre(String sndNombre) {
		this.sndNombre = sndNombre;
	}

	public String getNombreCompleto() {
		String iApellido2=(this.segundoApellido!=null && !this.segundoApellido.isEmpty())?" "+this.segundoApellido:"";
		String iNombre2=(this.segundoNombre!=null && !this.segundoNombre.isEmpty())?" "+this.segundoNombre:"";
		return this.primerApellido + iApellido2 + ", " + this.primerNombre + iNombre2; 
	}
	

}