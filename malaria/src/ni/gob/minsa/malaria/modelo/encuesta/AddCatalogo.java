package ni.gob.minsa.malaria.modelo.encuesta;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;


public class AddCatalogo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8818941134135901018L;

	@Id
	@Column(name="CATALOGO_ID", updatable=false)
	@SequenceGenerator(name="CATALOGO_CATALOGOID_GENERATOR",sequenceName="GENERAL.CATALOGOS_SEQ",allocationSize=1 )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATALOGO_CATALOGOID_GENERATOR")	
	private long catalogoId;

	@Column(name="CODIGO",nullable=true,length=50)
	private String codigo;
	
	@Column(name="VALOR",length=100,nullable=false)
	private String valor;
	
	@Column(name="PASIVO",precision=1,scale=0,nullable=false)
	private boolean pasivo;

	@Column(name="FINAL",precision=1,scale=0,nullable=false)
	private boolean eFinal;

	@Column(name="ORDEN",precision=3,scale=0,nullable=false)
    private Integer orden;
	
	@Column(name="DESCRIPCION",length=200,nullable=true)
    private String descripcion;
    
	@Column(name="DEPENDENCIA",updatable=false,nullable=true,insertable=true)
	private String dependencia;
	
    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO",updatable=false,nullable=false)
	private Date fechaRegistro;

    @NotNull(message="No se encontró el usuario que realiza la transacción.  Es posible que la sesión de trabajo haya finalizado.")
	@Column(name="USUARIO_REGISTRO",updatable=false,nullable=false)
	private String usuarioRegistro;	
	
	public AddCatalogo() {
		// TODO Auto-generated constructor stub
	}

	public long getCatalogoId() {
		return this.catalogoId;
	}

	public void setCatalogoId(long catalogoId) {
		this.catalogoId = catalogoId;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public boolean getPasivo() {
		return this.pasivo;
	}

	public void setPasivo(boolean pasivo) {
		this.pasivo = pasivo;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getDependencia() {
		return dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}

	public void seteFinal(boolean eFinal) {
		this.eFinal = eFinal;
	}

	public boolean iseFinal() {
		return eFinal;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}	

}
