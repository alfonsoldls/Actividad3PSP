package Main;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import modelo.javabeans.Usuario;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Main {

	static Scanner sc = new Scanner(System.in);
	static SecretKey key;
	static List<Usuario> miDataBase;
	
	public static void main(String[] args) {
	
		String opcion,frase,fraseDesencriptada;
		crearDataBase();
		byte[] fraseEncriptada = null;
		boolean continuar = true;
		int intentos = 0;
		
		//Intenta logear hasta 3 veces si no se ha podido autentificar.
		
		while(intentos<3) {
			System.out.println("Tienes " + (3-intentos) +" intentos");
			System.out.println("Introduzca el Nombre de usuario:");
			String name = sc.nextLine();
			System.out.println("Introduzca la contraseña de usuario:");
			String pass = sc.nextLine();
			Usuario user = new Usuario(name, pass);
			
			//Se comprueba si hay un usuario igual al que hemos pedido dentro de nuestra dataBase
			//Dos usuarios son iguales, si su nombre y contraseña(hasheada) es la misma.
			if(miDataBase.contains(user)) {
				while(continuar) {
				mostrarMenu();
				opcion = sc.nextLine();
					switch(opcion){
						case "1":
							System.out.println("Introduzca la frase a encriptar.");
							frase = sc.nextLine();
							fraseEncriptada = encriptar(frase);
							break;
							
						case "2":
							if(fraseEncriptada != null) {
								fraseDesencriptada =  desencriptar(fraseEncriptada);
								System.out.println(fraseDesencriptada);
								
							} else 
								System.out.println("No ha introducido ninguna frase a encriptar anteriormente");
							break;
							
						case "3":
							continuar = false;
							intentos=3;
							break;
							
						default: 
							System.out.println("Caracter introducido incorrecto, vuelva a introducir:");
							
					}
				}	
			} else {
				intentos++;
				System.out.println("Usuario no valido");
			}	
			
		}
		
		
		System.out.println("Cerrando el Programa...");

	}
	/**
	 * Funcion que simula una base de datos cuando el programa comienza.
	 * Se guardan en ella los usuarios con su nombre y contraseña.
	 */
	private static void crearDataBase() {
		miDataBase = new ArrayList<>();
		//Al crear un usuario, al llamar al constructor hashea su contraseña automaticamente pasada como String.
		Usuario user1 = new Usuario("Pablo", "12345");
		Usuario user2 = new Usuario("Antonio", "12345");
		Usuario user3 = new Usuario("Alfonso", "12345");
		miDataBase.add(user1);
		miDataBase.add(user2);
		miDataBase.add(user3);
	}
	
	/**
	 * Función que muestra el menu de opciones.
	 */
	private static void mostrarMenu() {
			System.out.println("\n------------------------------");
			System.out.println("1. Encriptar frase");
			System.out.println("2. Desencriptar frase");
			System.out.println("3. Salir");	
			System.out.println("------------------------------");
	}
	
	/**
	 * Función que encripta una frase pasada por parametro.
	 * @param frase Frase que queremos encriptar
	 * @return Devuelve la frase encriptada en un Array de bytes.
	 */
	private static byte[] encriptar(String frase){
		System.out.println("Encriptando clave...");
		
		try {
			//Se crea el generador de claves
			KeyGenerator generador = KeyGenerator.getInstance("DES");
			//Se genera la clave
			key = generador.generateKey();
			//Se crea el cifrador
			Cipher cifrador = Cipher.getInstance("DES");
			//Le decimos el cifrador que va a cifrar y con la clave generada
			cifrador.init(Cipher.ENCRYPT_MODE, key);
			//Tratamos el mensaje pasandolo a bytes
			byte[] fraseEnBytes = frase.getBytes();
			//Encriptamos
			byte[] fraseEncriptada = cifrador.doFinal(fraseEnBytes);
			//Se imprime la clave encriptada en forma de String
			System.out.println("La clave encriptada es: " + new String(fraseEncriptada));
			return fraseEncriptada;
			
		//Se catchean las excepciones 	
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			System.out.println("Error al crear y configurar el descifrador");
			System.out.println(e.getMessage());
			return null;
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Error al cifrar el mensaje");
			System.out.println(e.getMessage());
			return null;
		}	
	}
	
	/**
	 * Funcion que desencripta la frase encriptada en bytes.
	 * @param fraseEncriptada fraseEncriptada en Bytes
	 * @return Devuelve un String de la frase desencriptada
	 */
	private static String desencriptar(byte[] fraseEncriptada) {
		try {
			//Se crea el objeto descifrador
			Cipher descifrador = Cipher.getInstance("DES");
			//Configuramos el objeto para que descifre y usamos la key generada enteriormente
			descifrador.init(Cipher.DECRYPT_MODE, key);
			//Pasamos la frase desenciptada a string
			String fraseDesencriptada = new String(descifrador.doFinal(fraseEncriptada));
			return fraseDesencriptada;
			
		//Se catchean las excepciones
		} catch (NoSuchAlgorithmException  | NoSuchPaddingException |  InvalidKeyException e) {
			System.out.println("Error al crear y configurar el descifrador");
			System.out.println(e.getMessage());
			return null;
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Error al cifrar el mensaje");
			System.out.println(e.getMessage());
			return null;
		}	
		
		
	}
	
}
