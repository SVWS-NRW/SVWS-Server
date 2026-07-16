import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { StringBuilder } from '../../../java/lang/StringBuilder';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class Base32 extends JavaObject {

	/**
	 * Das Base-32-Alphabet (A-Z und 2-7)
	 */
	private static readonly ALPHABET: string = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

	/**
	 *  Eine vordefinierte Tabelle für die Dekodierung. Hier werden
	 *  nur die ASCII-Codes erlaubt, da Base32 eine Teilmenge davon ist.
	 */
	private static readonly DECODE_TABLE: Array<number> = [-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1];


	/**
	 * Privater Konstruktor mit Exception, da diese Klasse nicht instatiiert werden soll
	 */
	private constructor() {
		super();
		throw new IllegalStateException("Utility-Klasse");
	}

	/**
	 * Kodiert das übergeben Byte-Array mit einer Base32-Kodierung.
	 *
	 * @param data   das zu kodierende Byte-Array
	 *
	 * @return der String mit der Base32-Kodierung
	 */
	public static encode(data: Array<number> | null): string {
		if ((data === null) || (data.length === 0)) {
			return "";
		}
		const sb: StringBuilder = new StringBuilder();
		let buffer: number = 0;
		let bitsLeft: number = 0;
		for (const b of data) {
			buffer = (buffer << 8) | (b & 255);
			bitsLeft += 8;
			while (bitsLeft >= 5) {
				sb.append(Base32.ALPHABET.charAt((buffer >> (bitsLeft - 5)) & 31));
				bitsLeft -= 5;
			}
		}
		if (bitsLeft > 0) {
			sb.append(Base32.ALPHABET.charAt((buffer << (5 - bitsLeft)) & 31));
		}
		while ((sb.length() % 8) !== 0) {
			sb.append('=');
		}
		return sb.toString();
	}

	/**
	 * Dekodiert den übergebenen Base32-String in ein Byte-Array.
	 *
	 * @param base32   der zu dekodierende Base32-String
	 *
	 * @return das Byte-Array mit den dekodierten Daten
	 *
	 * @throws IllegalArgumentException wenn der Base32-String ungültige Zeichen enthält
	 */
	public static decode(base32: string | null): Array<number> {
		if ((base32 === null) || (JavaString.isEmpty(base32))) {
			return Array(0).fill(0);
		}
		const bytes: Array<number> = JavaString.getBytes(JavaString.replace(base32, "=", "").toUpperCase());
		const len: number = bytes.length;
		const anzahlBytes: number = Math.trunc((len * 5) / 8);
		const result: Array<number> = Array(anzahlBytes).fill(0);
		let buffer: number = 0;
		let bitsLeft: number = 0;
		let index: number = 0;
		for (let i: number = 0; i < len; i++) {
			const c: number = bytes[i] & 255;
			if ((c >= Base32.DECODE_TABLE.length) || (Base32.DECODE_TABLE[c] === -1)) {
				throw new IllegalArgumentException("Ungültiges Zeichen im Base32-String: " + c);
			}
			buffer = (buffer << 5) | Base32.DECODE_TABLE[c];
			bitsLeft += 5;
			if (bitsLeft >= 8) {
				if (index < result.length) {
					result[index++] = ((buffer >> (bitsLeft - 8)) & 255) as number;
				}
				bitsLeft -= 8;
			}
		}
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.encoding.Base32';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.encoding.Base32'].includes(name);
	}

	public static readonly class = new Class<Base32>('de.svws_nrw.core.utils.encoding.Base32');

}

export function cast_de_svws_nrw_core_utils_encoding_Base32(obj: unknown): Base32 {
	return obj as Base32;
}
