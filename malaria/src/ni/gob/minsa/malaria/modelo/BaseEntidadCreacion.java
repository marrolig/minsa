package ni.gob.minsa.malaria.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@MappedSuperclass  
public class BaseEntidadCreacion  
{  
    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO",updatable=false,nullable=false)
	private Date fechaRegistro;

    @NotNull(message="No se encontró el usuario que realiza la transacción.  Es posible que la sesión de trabajo haya finalizado.")
	@Column(name="USUARIO_REGISTRO",updatable=false,nullable=false)
	private String usuarioRegistro;

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
