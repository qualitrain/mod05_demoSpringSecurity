package mx.com.qtx.ejmSpSec.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class TestEncriptado {

	public static void main(String[] args) {
//		test_DelegatingPasswordEncoder_default();
//		test_CifradoRepetido();
//		test_distintosAlgoritmosCifrado();
		proponerCifradoPasswordsApp();
	}

	private static void test_distintosAlgoritmosCifrado() {
		System.out.println("\ntest_distintosAlgoritmosCifrado ----------------------------");
		
		Argon2PasswordEncoder codificadorArgon2 = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
		Pbkdf2PasswordEncoder codificadorPbkdf2 = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
		BCryptPasswordEncoder codificadorBCrypt = new BCryptPasswordEncoder();
		
		String passwdCrudo = "conejilloRojo";
		
		//Fuente: https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/factory/PasswordEncoderFactories.html
		String idArgon2 = "argon2@SpringSecurity_v5_8";
		String idPbkdf2 = "pbkdf2@SpringSecurity_v5_8";
		String idBCrypt = "bcrypt";
		
		String pwdArgon2 = "{" + idArgon2 + "}" + codificadorArgon2.encode(passwdCrudo);
		String pwdPbkdf2 = "{" + idPbkdf2 + "}" + codificadorPbkdf2.encode(passwdCrudo);
		String pwdBCrypt = "{" + idBCrypt + "}" + codificadorBCrypt.encode(passwdCrudo);
		
		System.out.println("\n" + passwdCrudo + ":");
		System.out.println(pwdArgon2);
		System.out.println(pwdPbkdf2);
		System.out.println(pwdBCrypt);
		System.out.println();
		
		PasswordEncoder codificadorPasswordsMultiple = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		for(String hashI:List.of(pwdArgon2, pwdPbkdf2, pwdBCrypt)) {
			if(codificadorPasswordsMultiple.matches(passwdCrudo, hashI)) {
				System.out.println("\nLa cadena cifrada:\n" 
			                      + hashI + "\n"
			                      + "corresponde con [" + passwdCrudo + "]");
			}
			else {
				System.out.println("\nLa cadena cifrada:\n" 
			                      + hashI + "\n"
						          + " NO corresponde con [" + passwdCrudo + "]");
			}
		}
	}

	private static void test_DelegatingPasswordEncoder_default() {
		System.out.println("\ntest_DelegatingPasswordEncoder_default ----------------------------");
		PasswordEncoder codificadorPasswordsMultiple = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		
		String passwdCrudo = "conejilloRojo";
		String passwdCifrado = codificadorPasswordsMultiple.encode(passwdCrudo);
		
		System.out.println(passwdCrudo + " cifrado:[" + passwdCifrado+ "]");
	}
	
	private static void test_CifradoRepetido() {
		System.out.println("\ntest_CifradoRepetido ----------------------------");
		PasswordEncoder codificadorPasswordsMultiple = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		
		String passwdCrudo = "conejilloRojo";
		
		List<String> passwdsCodificados = new ArrayList<>();
		for(int i=0; i<10; i++) {
			passwdsCodificados.add(codificadorPasswordsMultiple.encode(passwdCrudo));
		}
		String passwdTest ="";
		for(String passwdCodifI : passwdsCodificados) {
			if(getAleatorio() % 2 == 0)
				passwdTest = passwdCrudo;
			else
				passwdTest = "conejitoRojo";
			
			if(codificadorPasswordsMultiple.matches(passwdTest, passwdCodifI)) {
				System.out.println("La cadena cifrada " + passwdCodifI + " corresponde con [" + passwdTest + "]");
			}
			else {
				System.out.println("La cadena cifrada " + passwdCodifI + " NO corresponde con [" + passwdTest + "]");
			}
		}
	}

	private static int getAleatorio() {
		return (int) (Math.random() * 10000);
	}

	private static void proponerCifradoPasswordsApp() {
		System.out.println("\nproponerCifradoPasswordsApp ----------------------------");
		
		System.out.println("\nUsuarios y passwords propuestos para la configuracion de esta aplicacion:\n");
		
		PasswordEncoder codificadorPasswordsMultiple = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		List<String> lstUsuarios = List.of("betote",          "david",    "tavo");
		List<String> lstPswds =    List.of("tlakoneteasesino","tekolutla","tlatelolko");
		
		for(int i=0; i<lstUsuarios.size(); i++) {
			String passwdCifrado = codificadorPasswordsMultiple.encode(lstPswds.get(i));
			System.out.println("Usuario:" + String.format("%-6s", lstUsuarios.get(i) )
					           + ", password:" + String.format("%-12s",lstPswds.get(i)) 
			                   + " codificar como:[" + passwdCifrado + "]");
		}
		
	}

}
