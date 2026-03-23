import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { StringBuilder } from '../../../java/lang/StringBuilder';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class Base45 extends JavaObject {

	/**
	 * Das Base-45-Alphabet
	 */
	private static readonly ALPHABET: string = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ $%*+-./:";

	/**
	 *  Eine vordefinierte Tabelle für die Dekodierung. Hier werden
	 *  nur die ASCII-Codes erlaubt, da Base45 eine Teilmenge davon ist.
	 */
	private static readonly DECODE_TABLE: Array<number> = [-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, 37, 38, -1, -1, -1, -1, 39, 40, -1, 41, 42, 43, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 44, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35];


	private constructor() {
		super();
		throw new IllegalStateException("Utility-Klasse")
	}

	/**
	 * Dekodiert das übergebene Byte-Array mit einer Base45-Kodierung.
	 *
	 * @param data   das zu kodierende Byte-Array
	 *
	 * @return der String mit der Base45-Kodierung
	 */
	public static encode(data: Array<number> | null): string {
		if ((data === null) || (data.length === 0)) {
			return "";
		}
		const sb: StringBuilder = new StringBuilder();
		const len: number = data.length;
		for (let i: number = 0; i < len - 1; i += 2) {
			const val: number = ((data[i] & 255) << 8) | (data[i + 1] & 255);
			sb.append(Base45.ALPHABET.charAt(val % 45));
			sb.append(Base45.ALPHABET.charAt((Math.trunc(val / 45)) % 45));
			sb.append(Base45.ALPHABET.charAt((Math.trunc(val / 2025)) % 45));
		}
		if ((len % 2) !== 0) {
			const val: number = data[len - 1] & 255;
			sb.append(Base45.ALPHABET.charAt(val % 45));
			sb.append(Base45.ALPHABET.charAt((Math.trunc(val / 45)) % 45));
		}
		return sb.toString();
	}

	/**
	 * Dekodiert den übergebenen Base45-String in ein Byte-Array.
	 *
	 * @param base45   der zu dekodierende Base45-String
	 *
	 * @return das Byte-Array mit den dekodierten Daten
	 *
	 * @throws IllegalArgumentException wenn der Base45-String ungültige Zeichen enthält
	 */
	public static decode(base45: string | null): Array<number> {
		if ((base45 === null) || (JavaString.isEmpty(base45))) {
			return Array(0).fill(0);
		}
		const bytes: Array<number> = JavaString.getBytes(base45);
		const len: number = bytes.length;
		const resultLen: number = ((Math.trunc(len / 3)) * 2) + ((len % 3 === 2) ? 1 : 0);
		const result: Array<number> = Array(resultLen).fill(0);
		let index: number = 0;
		for (let i: number = 0; i < len - 2; i += 3) {
			const v1: number = Base45.getVal(bytes[i]);
			const v2: number = Base45.getVal(bytes[i + 1]);
			const v3: number = Base45.getVal(bytes[i + 2]);
			const val: number = v1 + (v2 * 45) + (v3 * 2025);
			if (val > 65535) {
				throw new IllegalArgumentException("Base45-Wert außerhalb des gültigen Bereichs.")
			}
			result[index++] = ((val >> 8) & 255) as number;
			result[index++] = (val & 255) as number;
		}
		if (len % 3 === 2) {
			const v1: number = Base45.getVal(bytes[len - 2]);
			const v2: number = Base45.getVal(bytes[len - 1]);
			const val: number = v1 + (v2 * 45);
			result[index] = (val & 255) as number;
		} else
			if (len % 3 === 1) {
				throw new IllegalArgumentException("Ungültige Base45-String-Länge.")
			}
		return result;
	}

	/**
	 * Wandelt das übergebene Zeichen mit Hilfe der DECODE_TABLE in den Wert 0...44 um.
	 *
	 * @param b   der Byte-Wert des ASCII-Zeichens
	 *
	 * @return der Wert (0...44)
	 */
	private static getVal(b: number): number {
		const c: number = b & 255;
		if (c >= Base45.DECODE_TABLE.length || Base45.DECODE_TABLE[c] === -1) {
			throw new IllegalArgumentException("Ungültiges Zeichen im Base45-String: " + c)
		}
		return Base45.DECODE_TABLE[c];
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.encoding.Base45';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.encoding.Base45'].includes(name);
	}

	public static readonly class = new Class<Base45>('de.svws_nrw.core.utils.encoding.Base45');

}

export function cast_de_svws_nrw_core_utils_encoding_Base45(obj: unknown): Base45 {
	return obj as Base45;
}
