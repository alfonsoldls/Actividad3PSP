package proyectoCriptografia;

public class Usuario {

	private String name;
	private String password;
	
	public Usuario(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
	
	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}
	
}
