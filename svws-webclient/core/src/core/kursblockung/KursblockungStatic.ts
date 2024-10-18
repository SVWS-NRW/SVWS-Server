import { JavaObject } from '../../java/lang/JavaObject';
import { IllegalStateException } from '../../java/lang/IllegalStateException';
import { Random } from '../../java/util/Random';
import { Class } from '../../java/lang/Class';

export class KursblockungStatic extends JavaObject {


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	/**
	 * Liefert ein neues Array der Größe {@code n} mit den Zahlen {@code 0 bis n-1} permutiert.
	 *
	 * @param  pRandom  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param  n        Die Größe des Arrays.
	 *
	 * @return ein neues Array der Größe {@code n} mit den Zahlen {@code 0 bis n-1} permutiert.
	 */
	public static gibPermutation(pRandom : Random, n : number) : Array<number> {
		const temp : Array<number> = Array(n).fill(0);
		for (let i : number = 0; i < n; i++) {
			temp[i] = i;
		}
		KursblockungStatic.aktionPermutiere(pRandom, temp);
		return temp;
	}

	/**
	 * Permutiert das Array {@code perm} zufällig.
	 *
	 * @param pRandom  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param perm     Das zu permutierende Array.
	 */
	public static aktionPermutiere(pRandom : Random, perm : Array<number>) : void {
		const n : number = perm.length;
		for (let i1 : number = 0; i1 < n; i1++) {
			const i2 : number = pRandom.nextInt(n);
			const s1 : number = perm[i1];
			const s2 : number = perm[i2];
			perm[i1] = s2;
			perm[i2] = s1;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungStatic';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungStatic'].includes(name);
	}

	public static class = new Class<KursblockungStatic>('de.svws_nrw.core.kursblockung.KursblockungStatic');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungStatic(obj : unknown) : KursblockungStatic {
	return obj as KursblockungStatic;
}
