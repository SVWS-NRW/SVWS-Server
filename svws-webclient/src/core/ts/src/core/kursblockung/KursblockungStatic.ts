import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { Random, cast_java_util_Random } from '../../java/util/Random';

export class KursblockungStatic extends JavaObject {


	public constructor() {
		super();
	}

	/**
	 *Erzeugt ein Array der Größe {@code n}, füllt es mit den Zahlen {@code 0 bis n-1} und permutiert das Array dann
	 * zufällig.
	 *
	 * @param  pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param  n       Die Größe des Arrays.
	 * @return         Eine Array-Referenz.
	 */
	public static gibPermutation(pRandom : Random, n : number) : Array<number> {
		let temp : Array<number> = Array(n).fill(0);
		for (let i : number = 0; i < n; i++) {
			temp[i] = i;
		}
		KursblockungStatic.aktionPermutiere(pRandom, temp);
		return temp;
	}

	/**
	 *Permutiert das Array {@code perm} zufällig.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param perm    Das zu permutierende Array.
	 */
	public static aktionPermutiere(pRandom : Random, perm : Array<number>) : void {
		let n : number = perm.length;
		for (let i1 : number = 0; i1 < n; i1++) {
			let i2 : number = pRandom.nextInt(n);
			let s1 : number = perm[i1];
			let s2 : number = perm[i2];
			perm[i1] = s2;
			perm[i2] = s1;
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungStatic'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungStatic(obj : unknown) : KursblockungStatic {
	return obj as KursblockungStatic;
}
