package mx.com.qtx.ejmSpSec.seguridad.jwt;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import mx.com.qtx.ejmSpSec.seguridad.servicios.IGeneradorTokensJWT;

public class GeneradorTokensJWT_JJWT0_12_6_SHA512 implements IGeneradorTokensJWT{
	private SecretKey llave;
	private long duracionTokens;
	
	public GeneradorTokensJWT_JJWT0_12_6_SHA512() {
		super();
		this.llave = generarLlave(AlgoritmoCifradoLlaveSimetrico.HmacSHA512);
		// 10 horas por defecto
		this.duracionTokens = 1000 * 60 * 60 * 10;
	}

	private static SecretKey generarLlave(AlgoritmoCifradoLlaveSimetrico algoritmo) {
		switch (algoritmo){
			case HmacSHA256: return Jwts.SIG.HS256.key().build();
			case HmacSHA384: return Jwts.SIG.HS384.key().build();
			case HmacSHA512: return Jwts.SIG.HS512.key().build();
			default: return null;
		}
		
	}
	
	private static KeyPair generarParLlaves(AlgoritmoCifradoParLlaves algoritmo) {
		switch (algoritmo){
			case EdDSA: return Jwts.SIG.EdDSA.keyPair().build();
			case ES256: return Jwts.SIG.ES256.keyPair().build();
			case ES384: return Jwts.SIG.ES384.keyPair().build();
			case ES512: return Jwts.SIG.ES512.keyPair().build();
			case PS256: return Jwts.SIG.PS256.keyPair().build();
			case PS384: return Jwts.SIG.PS384.keyPair().build();
			case PS512: return Jwts.SIG.PS512.keyPair().build();
			case RS256: return Jwts.SIG.RS256.keyPair().build();
			case RS384: return Jwts.SIG.RS384.keyPair().build();
			case RS512: return Jwts.SIG.RS512.keyPair().build();
			default: return null;
		}
	}
	
	@Override
	public void setPeriodoExpiracionHoras(int horas) {
		this.duracionTokens = 1000 * 60 * 60 * horas;
	}

	@Override
	public void setPeriodoExpiracionMinutos(int min) {
		this.duracionTokens = 1000 * 60 * min;
	}

	@Override
	public String generarToken(String nombreUsuario) {
		return this.generarToken(nombreUsuario, this.llave);
	}

	private String generarToken(String nombreUsuario, SecretKey llave) {
		String id = UUID.randomUUID()
				        .toString()
				        .replace("-", "");
		Date ahora = new Date();
		Date expiracion = new Date(System.currentTimeMillis() + this.duracionTokens);
		
		String tokenJwt = Jwts.builder().id(id)
	                          .issuedAt(ahora)
			                  .subject(nombreUsuario)
			                  .expiration(expiracion)
			                  .signWith(llave)
			                  .compact();
		
		return tokenJwt;
	}
	
	@Override
	public String generarToken(String nombreUsuario, Map<String, Object> mapClaims) {
		return this.generarToken(nombreUsuario, mapClaims, this.llave);
	}
	
	private String generarToken(String nombreUsuario, Map<String,Object> mapClaims, 
			                          SecretKey llave) {
		return this.generarToken(nombreUsuario,mapClaims,llave,this.duracionTokens);
	}
	
	@Override
	public String generarToken(String nombreUsuario, Map<String,Object> mapClaims, 
            long milisDuracion) {
		return this.generarToken(nombreUsuario, mapClaims, this.llave, milisDuracion);
	}
	
	private String generarToken(String nombreUsuario, Map<String,Object> mapClaims, 
			                          SecretKey llave, long milisDuracion) {
		Date ahora = new Date();
		Date expiracion = new Date(System.currentTimeMillis() + milisDuracion);
		String id = UUID.randomUUID()
				        .toString()
				        .replace("-", "");
		
		Claims claims = Jwts.claims().add(mapClaims).build();
		
		return Jwts.builder()
					  .id(id)
					  .issuedAt(ahora)
					  .expiration(expiracion)
					  .subject(nombreUsuario)
					  .claims(claims)
		              .signWith(llave)
		              .compact();	
	}

	@Override
	public String extraerUsuario(String token) {
		return this.extraerUsuarioTokenFirmado(token, this.llave);
	}

	//Lanzara excepcion si el token esta vencido o incorrecto
	private String extraerUsuarioTokenFirmado(String token, SecretKey skLlave) {
		Jws<Claims> contenido = extraerJwsClaimsTokenFirmado(token, skLlave);
		return contenido.getPayload()
				        .getSubject();
	}
	
	private Jws<Claims> extraerJwsClaimsTokenFirmado(String tokenFirmado, SecretKey llave){
		 JwtParser parser = Jwts.parser()
				                .verifyWith(llave)
			                    .build();
		
		 Jws<Claims> jwsClaims = parser.parseSignedClaims(tokenFirmado);

		return jwsClaims;
	}

		
	@Override
	public Date extraerExpiracion(String token) {
		return this.extraerExpiracionTokenFirmado(token, this.llave);
	}
	
	private Date extraerExpiracionTokenFirmado(String token, SecretKey skLlave) {
		Jws<Claims> contenido = this.extraerJwsClaimsTokenFirmado(token, skLlave);
		return contenido.getPayload()
				        .getExpiration();
	}

	@Override
	public boolean tokenExpirado(String token) {
		return this.tokenFirmadoExpirado(token, this.llave);
	}
	
	private boolean tokenFirmadoExpirado(String token, SecretKey skLlave) {
		try {
			this.extraerJwsClaimsTokenFirmado(token, skLlave);
		}
		catch(ExpiredJwtException ex) {
			return true;
		}
		return false;
	}

	@Override
	public boolean tokenValido(String tokenFirmado, String nombreUsuario) {
		return this.tokenValido(tokenFirmado, this.llave);
	}

	private boolean tokenValido(String tokenFirmado, SecretKey skLlave) {
		try {
			extraerJwsClaimsTokenFirmado(tokenFirmado, skLlave);
		}
		catch(Exception ex) {
			return false;
		}
		return true;
	}
	
	@Override
	public String extraerContenidoJwtTokenSinFirmarStr(String tokenSinFirma) {
		Jwt<Header,Claims> jwtHeaderClaims = this.extraerJwtTokenSinFirmar(tokenSinFirma);
		return jwtHeaderClaims.getHeader() + " " + jwtHeaderClaims.getPayload();	
	}
	
	private Jwt<Header,Claims> extraerJwtTokenSinFirmar(String tokenSinFirma){
		// Solamente se debe usar con tokens SIN firmar
		Jwt<Header,Claims> jwtHeaderClaims = Jwts.parser()
											    .build().parseUnsecuredClaims(tokenSinFirma);
		return jwtHeaderClaims;
	}

	@Override
	public String extraerContenidoTokenFirmadoStr(String tokenFirmado) {
		return this.extraerContenidoTokenFirmadoStr(tokenFirmado, this.llave);
	}

	private String extraerContenidoTokenFirmadoStr(String tokenFirmado, SecretKey llave) {
		
		Jws<Claims> contenidoToken = this.extraerJwsClaimsTokenFirmado(tokenFirmado, llave);
		
		JwsHeader header = contenidoToken.getHeader();
		String strHeader = "Header:["
				      + "Algorithm:" + header.getAlgorithm() + ", "
				      + "CompressionAlgorithm:" + header.getCompressionAlgorithm() + ", "
				      + "ContentType:" + header.getContentType() + ", "
				      + "KeyId(protected header):" + header.getKeyId() + ", "
				      + "Type:" + header.getType() 
				      + "] ";
		
		 Claims payload = contenidoToken.getPayload();
		 String strPayload = "Payload:["
				           + "Id:" + payload.getId( )+ ", "
				           + "Issuer:" + payload.getIssuer() + ", "
                           + "Subject:" + payload.getSubject() + ", "
                           + "Expiration:" + payload.getExpiration() + ", "
                           + "IssuedAt:" + payload.getIssuedAt() + ", "
                           + "NotBefore:" + payload.getNotBefore()
     				       + "] ";
		 
		 byte[] digest = contenidoToken.getDigest();
		 int codifIntLlave[] = new int[digest.length];
		 for(int i=0;i<digest.length;i++) {
			 codifIntLlave[i] = Byte.toUnsignedInt(digest[i]);
		 }
		 String strDigest = "Digest(Signature):" + Arrays.toString(codifIntLlave);
				 
		 return strHeader + "\n" + strPayload + "\n" + strDigest;
	}
	
	@Override
	public <R> R extraerCampo(String tokenFirmado, Class<R> tipoJavaCampo, String campo) {
		return this.extraerCampo(tokenFirmado, tipoJavaCampo, campo, this.llave);
	}
	
	//Devuelve el valor del campo, del tipo que sea
	private <R> R extraerCampo(String tokenFirmado, Function<Claims, R> getterCampo, SecretKey skLlave) {
		Jws<Claims> contenido = this.extraerJwsClaimsTokenFirmado(tokenFirmado, skLlave);
		Claims claims = contenido.getPayload();
		return getterCampo.apply(claims);
	}

	//Devuelve el valor del campo, del tipo que sea
	private <R> R extraerCampo(String tokenFirmado, Class<R> tipoJavaCampo, String campo, SecretKey skLlave) {
		@SuppressWarnings("unchecked")
		Function<Claims, R> getterCampo = (claims) -> (R) claims.get(campo);
		return this.extraerCampo(tokenFirmado, getterCampo, skLlave);
	}
	
	@Override
	public String getLlaveBase64() {
		return this.getLlaveBase64(this.llave);
	}
	
	private String getLlaveBase64(SecretKey llave) {
		return Encoders.BASE64.encode(llave.getEncoded());
	}


}
