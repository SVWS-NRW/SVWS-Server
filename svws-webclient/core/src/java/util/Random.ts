import { IllegalArgumentException } from '../../java/lang/IllegalArgumentException';
import { JavaObject } from '../../java/lang/JavaObject';

/**
 * Typescript-Implementierung, die das Verhalten der Java-Klasse {@link java.util.Random}
 * emuliert. Anders als bei {@link Math.random} wird hier ein deterministischer
 * Pseudo-Zufallszahlengenerator (Mulberry32) genutzt, damit die aus dem Java-Quellcode
 * transpilierten Algorithmen (z.B. Kursblockung, Stundenplanblockung) bei gleichem Seed
 * reproduzierbare Ergebnisse liefern.
 */
export class Random extends JavaObject {

	/** Der interne 32-Bit-Zustand des Mulberry32-Generators. */
	private _state: number;

	/**
	 * Erzeugt einen neuen Zufallszahlengenerator. Wird kein Seed übergeben, so wird
	 * ein zeitbasierter Seed verwendet (analog zu {@link java.util.Random#Random()}).
	 *
	 * @param seed   optionaler Seed für reproduzierbare Zufallsfolgen
	 */
	public constructor(seed?: number) {
		super();
		// Wenn kein Seed angegeben ist, wird ein zeitbasierter Seed genutzt.
		const s = (seed === undefined) ? (Date.now() ^ Math.floor(Math.random() * 0xFFFFFFFF)) : seed;
		// Der Zustand wird auf 32 Bit begrenzt (>>> 0 liefert einen vorzeichenlosen 32-Bit-Wert).
		this._state = (s | 0) >>> 0;
	}

	/**
	 * Liefert die nächste Pseudo-Zufallszahl auf Basis des Mulberry32-Algorithmus.
	 * Das Ergebnis liegt im Intervall [0, 1).
	 */
	private nextFloat(): number {
		// Mulberry32: einfacher, schneller und qualitativ ausreichender PRNG mit 32 Bit Zustand.
		this._state = (this._state + 0x6D2B79F5) >>> 0;
		let t = this._state;
		t = Math.imul(t ^ (t >>> 15), t | 1);
		t ^= t + Math.imul(t ^ (t >>> 7), t | 61);
		return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
	}

	/**
	 * Gibt eine pseudo-zufällige nicht-negative ganze Zahl im Bereich [0, bound) zurück.
	 *
	 * @param bound   die obere Schranke (exklusiv), muss positiv sein
	 *
	 * @throws IllegalArgumentException falls bound nicht positiv ist
	 */
	public nextInt(bound: number): number {
		// Gemäß Java-Kontrakt muss bound strikt positiv sein.
		if (bound <= 0)
			throw new IllegalArgumentException("bound must be positive");
		return Math.floor(this.nextFloat() * bound);
	}

	/**
	 * Gibt eine pseudo-zufällige ganze Zahl zurück. Wird keine Schranke angegeben, so liegt
	 * das Ergebnis im Bereich der gültigen sicheren Ganzzahlen in JavaScript.
	 *
	 * Hinweis: Abweichend von {@link java.util.Random#nextLong()} kann hier aufgrund der
	 * Begrenzung auf IEEE-754-Double kein vollständiger 64-Bit-Wertebereich abgebildet werden.
	 *
	 * @param bound   optionale obere Schranke (exklusiv), muss positiv sein
	 *
	 * @throws IllegalArgumentException falls bound angegeben und nicht positiv ist
	 */
	public nextLong(bound?: number): number {
		if (bound === undefined)
			return Math.floor(this.nextFloat() * 9007199254740991);
		if (bound <= 0)
			throw new IllegalArgumentException("bound must be positive");
		return Math.floor(this.nextFloat() * bound);
	}

	/** Gibt eine pseudo-zufällige Fließkommazahl im Bereich [0.0, 1.0) zurück. */
	public nextDouble(): number {
		return this.nextFloat();
	}

	/** Gibt einen pseudo-zufälligen booleschen Wert zurück. */
	public nextBoolean(): boolean {
		return this.nextFloat() < 0.5;
	}

	public transpilerCanonicalName(): string {
		return 'java.util.Random';
	}

	public isTranspiledInstanceOf(name: string): boolean {
		return [
			'java.util.Random',
			'java.lang.Object',
			'java.io.Serializable',
		].includes(name);
	}

}


export function cast_java_util_Random(obj: unknown): Random {
	return obj as Random;
}
