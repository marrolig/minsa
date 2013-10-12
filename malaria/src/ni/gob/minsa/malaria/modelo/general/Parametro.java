package ni.gob.minsa.malaria.modelo.general;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;


/**
 * La clase persistente para la tabla PARAMETROS de la base
 * de datos
 * <p>
 * @author Marlon Arróliga 
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 24/01/201
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="PARAMETROS",schema="SIVE")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="parametro.encontrarPorCodigo",
			query="select tp from Parametro tp " +
					"where tp.codigo=:pCodigo")
})
public class Parametro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PARAMETROS_PARAMETROID_GENERATOR", sequenceName="PARAMETROS_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PARAMETROS_PARAMETROID_GENERATOR")
	@Column(name="PARAMETRO_ID", updatable=false)
	private long parametroId;

	@Column(name="DESCRIPCION",nullable=true,updatable=true,insertable=true)
	private String descripcion;

	private String codigo;

	private String tipo;

	private String valor;

    public Parametro() {
    }

	public long getParametroId() {
		return this.parametroId;
	}

	public void setParametroId(long parametroId) {
		this.parametroId = parametroId;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}