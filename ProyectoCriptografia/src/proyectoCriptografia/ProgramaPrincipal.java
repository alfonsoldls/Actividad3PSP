package proyectoCriptografia;

import proyectoCriptografia.Usuario;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class ProgramaPrincipal {

	public static void main(String[] args) {
		
		inicioSesion();
		
	}
	
	// Método para inicializar y retornar una lista de usuarios con sus nombres y contraseñas (en hash SHA-512).
	public static List<Usuario> inicializarUsuarios() {
	    List<Usuario> lista = new ArrayList<>();
	    // Agregar usuarios a la lista
	    lista.add(new Usuario("Alfonso", "d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db"));
	    lista.add(new Usuario("Azahara", "33275a8aa48ea918bd53a9181aa975f15ab0d0645398f5918a006d08675c1cb27d5c645dbd084eee56e675e25ba4019f2ecea37ca9e2995b49fcb12c096a032e"));
	    lista.add(new Usuario("Alejandro", "7e2feac95dcd7d1df803345e197369af4b156e4e7a95fcb2955bdbbb3a11afd8bb9d35931bf15511370b18143e38b01b903f55c5ecbded4af99934602fcdf38c"));
	    return lista;
	    
	    //Usuario -> Alfonso
	    //Contraseña -> 1234

	    //Usuario -> Azahara
	    //Contraseña -> 1111
	    
	    //Usuario -> Alejandro
	    //Contraseña -> 4321
	}

	// Método para solicitar al usuario que ingrese su nombre de usuario.
	public static String solicitarNombreUsuario(Scanner scanner) {
	    System.out.println("Introduzca su usuario:");
	    return scanner.nextLine();
	}

	// Método para buscar un usuario en la lista por su nombre. Retorna el usuario si lo encuentra, de lo contrario retorna null.
	public static Usuario buscarUsuario(List<Usuario> lista, String nameuser) {
	    for (Usuario usuario : lista) {
	        if (usuario.getName().equals(nameuser)) {
	            return usuario;
	        }
	    }
	    return null;
	}

	// Método para verificar si la contraseña ingresada es correcta. Retorna true si coincide, false en caso contrario.
	public static boolean verificarContraseña(Usuario usuario, String password) {
	    try {
	        // Crear una instancia de MessageDigest para SHA-512
	        MessageDigest md = MessageDigest.getInstance("SHA-512");
	        // Aplicar hash SHA-512 a la contraseña ingresada
	        byte[] hashedBytes = md.digest(password.getBytes());
	        StringBuilder sb = new StringBuilder();
	        for (byte b : hashedBytes) {
	            sb.append(String.format("%02x", b));
	        }
	        // Comparar la contraseña ingresada (en hash) con la almacenada
	        return sb.toString().equals(usuario.getPassword());
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	// Método principal para iniciar sesión. Permite hasta 3 intentos de inicio de sesión.
	public static void inicioSesion() {
	    // Inicializar la lista de usuarios
	    List<Usuario> lista = inicializarUsuarios();
	    Scanner scanner = new Scanner(System.in);
	    int intentos = 0;

	    while (intentos < 3) {
	        // Solicitar nombre de usuario
	        String nameuser = solicitarNombreUsuario(scanner);
	        // Buscar al usuario en la lista
	        Usuario usuarioEncontrado = buscarUsuario(lista, nameuser);

	        if (usuarioEncontrado != null) {
	            System.out.println("Introduzca su contraseña:");
	            // Solicitar contraseña
	            String password = scanner.nextLine();
	            // Verificar si la contraseña es correcta
	            if (verificarContraseña(usuarioEncontrado, password)) {
	            	System.out.println("Bienvenido " + usuarioEncontrado.getName());
	            	program();
	            } else {
	                System.out.println("Contraseña incorrecta");
	            }
	        } else {
	            System.out.println("No se ha encontrado ningún nombre");
	        }
	        intentos++;
	    }
	    System.out.println("Ha consumido todos los intentos"); // Mensaje tras 3 intentos fallidos
	}

	public static void program() {
	    Scanner scanner = new Scanner(System.in);

	    // Variables para almacenar la opción del usuario y la frase
	    int opcion = 0;
	    String frase = "";
	    byte[] bytesFraseCifrada = null;
	    SecretKey clave = null;

	    try {
	        // Generar una clave secreta utilizando el algoritmo AES
	        KeyGenerator generador = KeyGenerator.getInstance("AES");
	        clave = generador.generateKey();
	    } catch (GeneralSecurityException e) {
	        // Capturar y mostrar cualquier excepción de seguridad general
	        e.printStackTrace();
	        return;
	    }

	    while (opcion != 3) {
	        // Mostrar opciones al usuario
	        System.out.println("\n1. Encriptar frase\n2. Desencriptar frase\n3. Salir\nElige una opción: ");
	        opcion = scanner.nextInt();
	        scanner.nextLine();

	        switch (opcion) {
	            case 1:
	                // Opción para encriptar una frase
	                System.out.print("Escriba una frase:");
	                frase = scanner.nextLine();

	                try {
	                    // Configurar un cifrador con el modo de encriptación y la clave
	                    Cipher cifrador = Cipher.getInstance("AES");
	                    cifrador.init(Cipher.ENCRYPT_MODE, clave);
	                    bytesFraseCifrada = cifrador.doFinal(frase.getBytes());
	                    String fraseCifrada = new String(bytesFraseCifrada);
	                    System.out.println("Mensaje Original: " + frase);
	                    System.out.println("Mensaje Cifrado: " + fraseCifrada);
	                } catch (GeneralSecurityException gse) {
	                    // Capturar y mostrar cualquier error durante la encriptación
	                    System.out.println("Error en la encriptación: " + gse.getMessage());
	                    gse.printStackTrace();
	                }
	                break;

	            case 2:
	                // Opción para desencriptar la frase
	                if (frase.equals("") || bytesFraseCifrada == null) {
	                    System.out.print("No hay ninguna frase guardada");
	                } else {
	                    try {
	                        // Configurar un descifrador con el modo de desencriptación y la clave
	                        Cipher descifrador = Cipher.getInstance("AES");
	                        descifrador.init(Cipher.DECRYPT_MODE, clave);
	                        byte[] bytesFraseDescifrada = descifrador.doFinal(bytesFraseCifrada);
	                        System.out.println("Mensaje Descifrado: " + new String(bytesFraseDescifrada));
	                    } catch (GeneralSecurityException gse) {
	                        // Capturar y mostrar cualquier error durante la desencriptación
	                        System.out.println("Error en la desencriptación: " + gse.getMessage());
	                        gse.printStackTrace();
	                    }
	                }
	                break;

	            case 3:
	                // Opción para salir del programa
	                System.out.print("Saliendo...");
	                System.exit(0);
	                break;

	            default:
	                // Manejar cualquier opción no válida
	                System.out.print("Opción no válida");
	        }
	    }
	}

	

}
