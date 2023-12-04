package modelo.javabeans;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class Usuario {
	
	String name;
	byte[] password;
	
	public Usuario(String name, String password) {
		this.name = name;
		this.password = hashedPass(password);
		
	}
	
	/**
	 * Funcion que hashea una contraseña pasada en String como parametro.
	 * @param password Contraseña en formato de String
	 * @return Devuelve la contraseña hasheada en un Array de bytes.
	 */
	private byte[] hashedPass(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] bytePassword = password.getBytes();
			return md.digest(bytePassword);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	@Override
	public String toString() {
		return "Usuario [name=" + name + ", password=" + Arrays.toString(password) + "]";
	}

	
	// DOS USUARIOS SON IGUALES CUANDO TIENEN MISMO NAME Y CONTRASEÑA (hashed)
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(password);
		result = prime * result + Objects.hash(name);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(name, other.name) && Arrays.equals(password, other.password);
	}
	
}
