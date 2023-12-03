

import java.security.GeneralSecurityException;


import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;

import java.util.Scanner;

import javax.crypto.Cipher;

import javax.crypto.KeyGenerator;

import javax.crypto.SecretKey;

public class Programa  {

	private static final int MAX_INTENTOS = 3;

	private static final String[] Usuarios = { "usuario1", "usuario2", "usuario3" };

	private static final String[] Contraseñas_haseadas = {

			"f26a32b5bbe9693b0646414bb61ad93ac17f52cca65ab25801f444d1723fc82a6ef81c0dc229534d599a6948e361c4d131524a592ef534d74cde74305ca1ab36a5af", // "2580"
																																				// hasheado
																																				// con
																																				// SHA-512

			"1083e7ebf9478dc7e646732fc3cff7dffe63149f202046a6a5e45a15d4c95bce1e1910ddf641120b1252d5c2a2e024206589a12bcc35ca4d2e5367c80b189f97", // "Abcd"
																																				// hasheado
																																				// con
																																				// SHA-512

			"ba3253876aed6bc22d4a6ff53d8406c6ad864195ed144ab5c87621b6c233b548baeae6956df346ec8c17f5ea10f35ee3cbc514797ed7ddd3145464e2a0bab413" // "123456"
																																				// hasheado
																																				// con
																																				// SHA-512

	};

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		boolean autenticado = false;
		int intentos = 0;
		
		String usuario = "";

		while (!autenticado && intentos < MAX_INTENTOS) {

			System.out.println("Ingrese su nombre de usuario:");
			usuario = scanner.nextLine();
			System.out.println("Ingrese su contraseña:");
			String contraseña = scanner.nextLine();
			autenticado = autenticar(usuario, contraseña);

			if (!autenticado) {
				intentos++;
				System.out.println("Usuario o contraseña incorrectos. Intentos restantes: " + (MAX_INTENTOS - intentos));
			}

		}

		if (autenticado) {
			System.out.println("¡Bienvenido, " + usuario + "!");
			menu();
		} else {
			System.out.println("Demasiados intentos incorrectos. Saliendo del programa.");
		}

	}

	private static boolean autenticar(String usuario, String contraseña) {

		int indice = indiceUsuario(usuario);

		if (indice != -1) {
			return Contraseñas_haseadas[indice].equals(hashSHA512(contraseña));
		}

		return false;

	}

	private static int indiceUsuario(String usuario) {

		for (int i = 0; i < Usuarios.length; i++) {

			if (Usuarios[i].equals(usuario)) {
				return i;
			}
		}
		return -1;

	}

	private static String hashSHA512(String contraseña) {

		try {

			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] messageDigest = md.digest(contraseña.getBytes());
			StringBuilder hexString = new StringBuilder();
			for (byte b : messageDigest) {
				String hex = Integer.toHexString(0xFF & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

	}

	private static void menu () {
	
		Scanner sc = new Scanner(System.in);		
		boolean exit = false;		
		int opcion;		
		byte[] mensajeCifradoEnBytes = null;		
		SecretKey clave = null;
		
		// Calculamos la clave al principio
		
		try {
		
			KeyGenerator generador = KeyGenerator.getInstance("AES");			
			clave = generador.generateKey();
		
		} catch (GeneralSecurityException gse) {
		
			System.out.println("Algo ha fallado.." + gse.getMessage());			
			gse.printStackTrace();
		
		}
		
		while (!exit){
		
			System.out.println();
			System.out.println("********* MENU *********");			
			System.out.println("------------------------");			
			System.out.println("1.Encriptar frase");			
			System.out.println("2.Desencriptar frase");			
			System.out.println("3.Salir del programa");			
			System.out.println("Introduce una opcion");
			
			opcion = sc.nextInt();
			
			switch (opcion) {
			
				case 1:
				
					mensajeCifradoEnBytes = encriptarFrase(clave);					
					break;
				
				case 2:
				
					if (mensajeCifradoEnBytes != null) {				
						desencriptarFrase(mensajeCifradoEnBytes, clave);				
					} else {				
						System.out.println("Debes encriptar primero alguna frase.");				
					}
					
					break;
				
				case 3:			
					System.out.println("Has elegido salir. Hasta pronto");				
					exit = true;
					
					break;
				default:				
					System.out.println("Numero introducido no valido");
			
			}
		
		}
	
	}
	
	public static byte[] encriptarFrase(SecretKey clave) {

		Scanner scanner = new Scanner(System.in);

		try {
			System.out.println("Introduce un mensaje");
			String mensajeOriginal = scanner.nextLine();
			
			Cipher cifrador = Cipher.getInstance("AES");
			cifrador.init(Cipher.ENCRYPT_MODE, clave);

			byte[] bytesMensajeOriginal = mensajeOriginal.getBytes();

			byte[] bytesMensajeCifrado = cifrador.doFinal(bytesMensajeOriginal);

			String mensajeCifrado = new String(bytesMensajeCifrado);
			System.out.println("Mensaje Original: " + mensajeOriginal);
			System.out.println("Cifrando mensaje....");
			System.out.println("Mensaje Cifrado: " + mensajeCifrado);
			return bytesMensajeCifrado;

		} catch (GeneralSecurityException gse) {
			System.out.println("Algo ha fallado.." + gse.getMessage());
			gse.printStackTrace();
			return null;
		}
		
	}

	public static void desencriptarFrase(byte[] bytesMensajeCifrado, SecretKey clave) {

		try {

			System.out.println("Descifrando mensaje....");
			
			Cipher descifrador = Cipher.getInstance("AES");
			descifrador.init(Cipher.DECRYPT_MODE, clave);
			byte[] bytesMensajeDescifrado = descifrador.doFinal(bytesMensajeCifrado);
			
			System.out.println("Mensaje Descifrado: " + new String(bytesMensajeDescifrado));
			
		} catch (GeneralSecurityException gse) {
			System.out.println("Algo ha fallado.." + gse.getMessage());
			gse.printStackTrace();
		}

	}
}		