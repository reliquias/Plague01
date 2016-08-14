package br.com.inicial.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PRG_EMPRESA")
public class Empresa {

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "CNPJ", nullable = false, length=19, unique=true)
    private String cnpj;

    @Column(name = "CONFIG_FILE_NAME", nullable = false, length=50, unique=false)
    private String configFileName;
    
    @Lob
    @Column(name = "SQL_DIALECT")
    private String sqlDialect;
    
    @Lob
    @Column(name = "URL_CONEXAO")
    private String URLConexao;

    @Lob
    @Column(name = "DRIVER_CLASS")
    private String driverClass;

    @Lob
    @Column(name = "USER_NAME")
    private String username;
    
    @Lob
    @Column(name = "PASSWORD")
    private String password;
    @Transient
    private static String erroEmpresaCNPJ;

    public Empresa() {
    }

    @Override
    public boolean equals( Object obj ) {
        if( obj instanceof Empresa ) {
            Empresa that = (Empresa) obj;
            if( this.getCnpj().equals( that.getCnpj() ) ) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int hashCode() {
        int SEED = 17;
        SEED = 37 * SEED + ((getCnpj() == null) ? 0 :getCnpj().hashCode());
        return SEED;
    }


    @Override
    public String toString() {
        return "Id: " + id + " / CNPJ: " + cnpj + " / configFile: " + configFileName;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the cnpj
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * @param cnpj the cnpj to set
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * @return the configFileName
     */
    public String getConfigFileName() {
        return configFileName;
    }

    /**
     * @param configFileName the configFileName to set
     */
    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }

	public String getSqlDialect() {
		return sqlDialect;
	}

	public void setSqlDialect(String sqlDialect) {
		this.sqlDialect = sqlDialect;
	}

	public String getURLConexao() {
		return URLConexao;
	}

	public void setURLConexao(String uRLConexao) {
		URLConexao = uRLConexao;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static String getErroEmpresaCNPJ() {
		return erroEmpresaCNPJ;
	}

	public static void setErroEmpresaCNPJ(String erroEmpresaCNPJ) {
		Empresa.erroEmpresaCNPJ = erroEmpresaCNPJ;
	}
	
	
    

}