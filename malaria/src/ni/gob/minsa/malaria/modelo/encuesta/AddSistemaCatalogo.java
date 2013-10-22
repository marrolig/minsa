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

public class AddSistemaCatalogo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SISTCATS_ID_GENERATOR", sequenceName="GENERAL.SISTCAT_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SISTCATS_ID_GENERATOR")
	@Column(name="SISTEMACATALOGO_ID",updatable=false)
	private long sistemaCatalogoId;

	@Column(name="SISTEMA",nullable=false,updatable=false)
	private String sistema;

	@Column(name="CATALOGO",nullable=false,updatable=false)
	private String catalogo;
	
    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO",updatable=false,nullable=false)
	private Date fechaRegistro;

    @NotNull(message="No se encontró el usuario que realiza la transacción.  Es posible que la sesión de trabajo haya finalizado.")
	@Column(name="USUARIO_REGISTRO",updatable=false,nullable=false)
	private String usuarioRegistro;	

    public AddSistemaCatalogo() {
    }

	public void setSistemaCatalogoId(long sistemaCatalogoId) {
		this.sistemaCatalogoId = sistemaCatalogoId;
	}

	public long getSistemaCatalogoId() {
		return sistemaCatalogoId;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(String catalogo) {
		this.catalogo = catalogo;
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
