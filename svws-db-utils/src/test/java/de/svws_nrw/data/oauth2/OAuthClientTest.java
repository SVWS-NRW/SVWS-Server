package de.svws_nrw.data.oauth2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.util.Base64;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Eine Testklasse für die Klasse {@link OAuth2Client}
 */
class OAuthClientTest {

	/** Access-Token String zum Testen des Parsers */
	private static final String TOKEN_STRING = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiMWIzNWNjNWUyMTUxZjE1MDg3ZjU2ZjBmOWU1ZjU5Njc3ZmExZWQ0NTMxNTYyMjEwMmVmODcyNDlhYzVhNjY5Y2YzOWZkN2I2MTQ4NDhiNzIiLCJpYXQiOjE3MDUzMjk2ODYuMTEzMjE1LCJuYmYiOjE3MDUzMjk2ODYuMTEzMjE3LCJleHAiOjE3MzY5NTIwODYuMTExNDc5LCJzdWIiOiIiLCJzY29wZXMiOltdfQ.G3KslcxjpBEYPiAX47mT2mu7lSDGQ5AQAPKpvNjSVV-6qvOL9fUV9J8RKLbvD4XmsSYXHEvAdKR2AMgYyPxvo_rvECxyM5iyDSenv0LixJ6G9aK2nw4t2i7leZygPshCnmTCXewicwKkM4eUBah7C27YsHkyHBBIAG4QZ9uX5prThZ0zZ9xNZUwFbdGmitsbcCdisioP3LFqSwfnamc5hjn41rzMV3SuTQZhptbEfJ7sT96RC9w19CjCRBXUwEt3g7HCwOJptXcqFDivNSijYnnUGvUFplil4IKUs20-RQHHGb5DicBObhUUMI7rPW8O1cgMZ0amMV4CJFdoemIjMRJjFhqOLqGoXcd9q6QMA1gavQUTwtJtBr4ph2DQMlh-oxTf-qIXzhEu-9kwT0WaWvTRTSGNN8bjqI4p_odaal1VP6aLxqp_o60nAqQDqH6Nh6lPBTI-nOx4H-p1GDME-EHIHiUWEmxgD1s2dP1bNcz0CAzSdwCEEeCSjSLOGZHSzMVCKhlGHRnmbvcFzYFPSv3Rzz7eWTyirCvPc7WqakyTyF4f58vC3qGG0xbnKD14pPNZldUDtxcdkOlY-L9s4KiVmi4QpASZgkTGqsrdJ8Zu4uadmFeodBthBi0ufkYNHelUxfsNdH7-ozeuwID5XS7Xedgf8cb7IwEP2OpKe1I";

	/** Token Json String zum Testen des Parsers */
	private static final String TOKEN_INPUT =
			"""
			{"token_type":"Bearer","expires_in":31622400,"access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiMWIzNWNjNWUyMTUxZjE1MDg3ZjU2ZjBmOWU1ZjU5Njc3ZmExZWQ0NTMxNTYyMjEwMmVmODcyNDlhYzVhNjY5Y2YzOWZkN2I2MTQ4NDhiNzIiLCJpYXQiOjE3MDUzMjk2ODYuMTEzMjE1LCJuYmYiOjE3MDUzMjk2ODYuMTEzMjE3LCJleHAiOjE3MzY5NTIwODYuMTExNDc5LCJzdWIiOiIiLCJzY29wZXMiOltdfQ.G3KslcxjpBEYPiAX47mT2mu7lSDGQ5AQAPKpvNjSVV-6qvOL9fUV9J8RKLbvD4XmsSYXHEvAdKR2AMgYyPxvo_rvECxyM5iyDSenv0LixJ6G9aK2nw4t2i7leZygPshCnmTCXewicwKkM4eUBah7C27YsHkyHBBIAG4QZ9uX5prThZ0zZ9xNZUwFbdGmitsbcCdisioP3LFqSwfnamc5hjn41rzMV3SuTQZhptbEfJ7sT96RC9w19CjCRBXUwEt3g7HCwOJptXcqFDivNSijYnnUGvUFplil4IKUs20-RQHHGb5DicBObhUUMI7rPW8O1cgMZ0amMV4CJFdoemIjMRJjFhqOLqGoXcd9q6QMA1gavQUTwtJtBr4ph2DQMlh-oxTf-qIXzhEu-9kwT0WaWvTRTSGNN8bjqI4p_odaal1VP6aLxqp_o60nAqQDqH6Nh6lPBTI-nOx4H-p1GDME-EHIHiUWEmxgD1s2dP1bNcz0CAzSdwCEEeCSjSLOGZHSzMVCKhlGHRnmbvcFzYFPSv3Rzz7eWTyirCvPc7WqakyTyF4f58vC3qGG0xbnKD14pPNZldUDtxcdkOlY-L9s4KiVmi4QpASZgkTGqsrdJ8Zu4uadmFeodBthBi0ufkYNHelUxfsNdH7-ozeuwID5XS7Xedgf8cb7IwEP2OpKe1I"}
			""";

	/**
	 * Testet den Tokenaustausch gegen den Testserver, daher disabled - zur Ausführung müssen client_id und client_secret
	 * mit dem Testserver übereinstimmen.
	 *
	 * @throws IOException            beim Parsen des Tokens i
	 * @throws InterruptedException   bei Verbindungsproblemen
	 */
	@Test
	@Disabled("Ein entsprechender Test-Server muss zur Verfügung und konfugiert sein. Dies ist bei einem Build nicht gewährleistet.")
	void testWenomConnection() throws IOException, InterruptedException {
		final String client_id = "1";
		final String client_secret = "1IbLT9WQr2JZrXuBcmB8K7bDvTkK2hcrZvfG5V9l";

		final String basicAuth = Base64.getEncoder()
				.encodeToString((client_id + ":" + client_secret).getBytes());
		final OAuth2Client client = OAuth2Client.getClient("https://wenom2.svws-nrw.de", basicAuth);
		assertNotNull(client.getToken());
		// testet, ob ein nochmals angeforderter Client die gleichen credentials
		// behält, da das Token noch gültig sein sollte
		final OAuth2Client client2 = OAuth2Client.getClient("https://wenom2.svws-nrw.de", basicAuth);
		assertSame(client, client2);
	}


	/**
	 * Testet das Parsen eines JSON Access-Tokens
	 *
	 * @throws IOException beim Parsen des Tokens i
	 */
	@Test
	void testOAuthTokenParse() throws IOException {
		final OAuth2Token parse = OAuth2Client.getTokenfromJson(TOKEN_INPUT);
		assertNotNull(parse);
		assertEquals("Bearer", parse.tokenType);
		assertEquals(31622400, parse.expiresIn);
		assertEquals(TOKEN_STRING, parse.accessToken);
	}

}
