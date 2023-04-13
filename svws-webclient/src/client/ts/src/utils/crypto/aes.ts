import { ArrayIndexOutOfBoundsException } from "@svws-nrw/svws-core";
import type { AESAlgo } from "./aesAlgo";
import { AESException } from "./aesException";

/**
 * Diese Klasse stellt Methoden zum Ver- und Entschlüsseln
 * von Daten mithilfe des AES-Standards zur Verfügung.
 * Der Initialisierungsvektor (IV) wird dabei den verschlüsselten
 * Daten vorangestellt.
 */
export class AES {

	/** Ein Zufallszahlen-Generator für kryptographisch starke Zufallszahlen (RNG). */
	//private static random = crypto.getRandomValues(new Uint8Array(16));

	/** Das zu verwendende AES-Verfahren */
	private readonly algo: AESAlgo;

	/** Der zu verwendende AES-Schlüssel */
	private readonly key: CryptoKey;


	/**
	 * Erstellt ein neues AES-Objekt zum Ver- und entschlüsseln von Daten.
	 *
	 * @param algo   das zu verwendende AES-Verfahren
	 * @param key    der zu verwendende AES-Schlüssel
	 */
	public constructor(algo: AESAlgo, key: CryptoKey) {
		this.algo = algo;
		this.key = key;
	}

	/**
	 * Verschlüsselt das übergebene Byte-Array. Der Initialisieruns-Vektor (IV) wird dabei den Daten vorangestellt.
	 *
	 * @param input   das zu verschlüsselnde Byte-Array
	 *
	 * @return das verschlüsselte Byte-Array
	 *
	 * @throws AESException   eine Exception, falls ein Fehler beim Verschlüsseln auftritt
	 */
	public async encrypt(input: BufferSource): Promise<Uint8Array> {
		try {
			const iv = crypto.getRandomValues(new Uint8Array(16));
			const encrypted = await crypto.subtle.encrypt({name: this.algo.value, iv}, this.key, input);
			const len = encrypted.byteLength + 16;
			const output = new Uint8Array(len);
			output.set(iv, 0);
			output.set(new Uint8Array(encrypted), 16);
			return output;
		} catch (e: unknown) {
			throw new AESException("Fehler beim Verschlüsseln der Daten.", e as Error);
		}
	}

	/**
	 * Entschlüsselt das übergebene Byte Array. Dabei wird davon
	 * ausgegangen, dass dieses Byte-Array den Initialisierungsvektor (IV)
	 * am Anfang des Arrays von den eigentlich verschlüsselten Daten enthält.
	 *
	 * @param input   die verschlüsselten Daten mit dem IV
	 *
	 * @return die entschlüsselten Daten
	 *
	 * @throws AESException   eine Exception, falls ein Fehler beim Entschlüsseln auftritt
	 */

	public async decrypt(input: Uint8Array): Promise<Uint8Array> {
		try {
			if (input.length < 16)
				throw new ArrayIndexOutOfBoundsException("Das übegebene Array ist zu klein und kann noch nicht einmal einen Initialisierungsvektor enthalten.");
			const iv = input.slice(0, 16);
			const data = input.slice(16);
			const decryptedData = await crypto.subtle.decrypt({name: this.algo.value, iv}, this.key, data);
			return new Uint8Array(decryptedData);
		} catch (e: unknown) {
			console.log(e)
			throw new AESException("Fehler beim Entschlüsseln der Daten.", e as Error);
		}
	}

	/**
	 * Verschlüsselt die übergebenen Daten und erstellt einen Base64-kodierten
	 * String aus dem Ergebnis. Der Initialisieruns-Vektor (IV) wird dabei den Daten vorangestellt.
	 *
	 * @param input   die unverschlüsselten Daten
	 *
	 * @return die verschlüsselten Daten als Base64-String.
	 *
	 * @throws AESException   eine Exception, falls ein Fehler beim Verschlüsseln auftritt
	 */
	public async encryptBase64(input: Uint8Array): Promise<string> {
		const buf = await this.encrypt(input);
		const res = await this.arraybufferToBase64(buf);
		return res;
		// return window.btoa(decoder.decode(buf));
	}


	/**
	 * Entschlüsselt den übergebenen Base64-String. Dabei wird davon
	 * ausgegangen, dass das Base64-kodierte Byte-Array den Initialisierungsvektor (IV)
	 * am Anfang des Arrays von den eigentlich verschlüsselten Daten enthält.
	 *
	 * @param input   die verschlüsselten Base64-kodierten Daten mit dem IV
	 *
	 * @return die entschlüsselten Daten
	 *
	 * @throws AESException   eine Exception, falls ein Fehler beim Entschlüsseln auftritt
	 */
	public async decryptBase64(input: string): Promise<Uint8Array> {
		const buf = await this.base64ToBufferAsync(input);
		return this.decrypt(buf);
	}

	/**
	 * Konvertiert einen Buffer in einen Base64-String
	 *
	 * @param data ein Buffer
	 * @return ein Base64-String
	 */
	private async arraybufferToBase64(data: BufferSource) {
		const base64url: string | ArrayBuffer | null = await new Promise((r) => {
			const reader = new FileReader();
			reader.onload = () => r(reader.result);
			if (data instanceof File)
				reader.readAsDataURL(data);
			else
				reader.readAsDataURL(new Blob([data]));
		})
		if (typeof base64url === 'string')
			return base64url.split(",", 2)[1];
		else
			throw new Error("Keine gültigen Daten zum Umwandeln in Base64");
	}

	/**
	 * Konvertiert einen Base64-String zu einem Buffer
	 *
	 * @param base64 Ein Base64-String
	 * @return ein Promise mit einem Buffer
	 */
	private async base64ToBufferAsync(base64: string): Promise<Uint8Array> {
		const dataUrl = "data:application/octet-binary;base64," + base64;
		const res = await fetch(dataUrl);
		const buffer = await res.arrayBuffer();
		return new Uint8Array(buffer);
	}

	/**
	 * Erzeugt einen neuen zufälligen AES-Schlüssel mit der Schlüssellänge 256.
	 *
	 * @return der AES-Schlüssel
	 *
	 * @throws AESException   falls kein AES-Schlüssel generiert werden kann
	 */
	public static async getRandomKey256(): Promise<CryptoKey> {
		try {
			return await crypto.subtle.generateKey({name: "AEA-CBC", length: 256}, true, ["encrypt", "decrypt"]);
		} catch (e: unknown) {
			throw new AESException("Fehler beim Erstellen eines zufälligen AES-Schlüssels.", e as Error);
		}
	}

	/**
	 * Erstellt aus dem angegebenen Kennwort und dem angegebenen Salt einen AES-Schlüssel.
	 * Das bei wird die Password-Based Key Derivation Function 2 (PBDKF2) verwendet.
	 * Dabei kommt Keyed-Hashing for Message Authentication Code (HMAC) in Kombination
	 * mit dem Secure Hash Algorithm (SHA256) zur Verwendung.
	 *
	 * @param password   das Kennwort
	 * @param salt       der Salt
	 *
	 * @return der AES-Schlüssel
	 *
	 * @throws AESException   falls kein AES-Schlüssel generiert werden kann
	 */
	public static async getKey256(password: string, _salt: string): Promise<CryptoKey> {
		try {
			const encoder = new TextEncoder();
			const salt = encoder.encode(_salt);
			const keySpec: Pbkdf2Params = { name: "PBKDF2", salt: salt, iterations: 65536, hash: "SHA-256" }
			const passwordKey = await crypto.subtle.importKey("raw", encoder.encode(password), "PBKDF2", false, ["deriveBits", "deriveKey"]);
			const derivedKeyType = { name: "AES-CBC", length: 256}
			const key = await crypto.subtle.deriveKey(keySpec, passwordKey, derivedKeyType, true, ["encrypt", "decrypt"]);
			return key;
		} catch (e: unknown) {
			console.log(e)
			throw new AESException("Fehler beim Erstellen des AES-Schlüssels aus dem Kennwort und dem Salt.", e as Error);
		}
	}

	/**
	 * Erzeugt einen zufälligen Initialisierungsvektor (IV) der Länge 16 Byte
	 *
	 * @return der IV der Länge 16 Byte
	 */
	public static getRandomIV(): Uint8Array {
		const result = new Uint8Array(16);
		return crypto.getRandomValues(result);
	}

}
