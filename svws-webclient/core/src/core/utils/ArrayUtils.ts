import { JavaObject } from '../../java/lang/JavaObject';
import { Random } from '../../java/util/Random';
import { Class } from '../../java/lang/Class';
import { System } from '../../java/lang/System';

export class ArrayUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Liefert das Array mit den Werten 0 bis size-1 in permutierter Reihenfolge.
	 * <br> Beispiel für size = 5 --> {2, 3, 0, 1, 4}
	 *
	 * @param size    Die Länge des Arrays.
	 * @param random  Ein {@link Random}-Objekt zum Permutieren der Elemente des Arrays.
	 *
	 * @return das Array mit den Werten 0 bis size-1 in permutierter Reihenfolge.
	 */
	public static getIndexPermutation(size : number, random : Random) : Array<number> {
		const perm : Array<number> | null = Array(size).fill(0);
		for (let i : number = 0; i < perm.length; i++)
			perm[i] = i;
		for (let i1 : number = 0; i1 < perm.length; i1++) {
			const i2 : number = random.nextInt(perm.length);
			const save1 : number = perm[i1];
			const save2 : number = perm[i2];
			perm[i1] = save2;
			perm[i2] = save1;
		}
		return perm;
	}

	/**
	 * Liefert eine Kopie des übergebenen Arrays ergänzt um eine übergebene Zahl.
	 *
	 * @param a      Das übergebene Array.
	 * @param value  Die Zahl, welche ergänzt wird.
	 *
	 * @return eine Kopie des übergebenen Arrays ergänzt um eine übergebene Zahl.
	 */
	public static erweitern(a : Array<number>, value : number) : Array<number> {
		const temp : Array<number> | null = Array(a.length + 1).fill(0);
		System.arraycopy(a, 0, temp, 0, a.length);
		temp[a.length] = value;
		return temp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.ArrayUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.ArrayUtils'].includes(name);
	}

	public static class = new Class<ArrayUtils>('de.svws_nrw.core.utils.ArrayUtils');

}

export function cast_de_svws_nrw_core_utils_ArrayUtils(obj : unknown) : ArrayUtils {
	return obj as ArrayUtils;
}
