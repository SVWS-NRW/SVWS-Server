import { JavaObject } from '../../java/lang/JavaObject';
import { IllegalStateException } from '../../java/lang/IllegalStateException';
import { GostBlockungListeneintrag } from '../../core/data/gost/GostBlockungListeneintrag';
import { StringUtils } from '../../core/utils/StringUtils';
import { JavaLong } from '../../java/lang/JavaLong';
import type { List } from '../../java/util/List';
import { JavaString } from '../../java/lang/JavaString';
import type { Comparator } from '../../java/util/Comparator';

export class BlockungsUtils extends JavaObject {

	/**
	 * Ein Comparator zum Sortieren von {@link GostBlockungListeneintrag}, welcher beachtet, dass der Suffix Zahlen enthält.
	 */
	private static readonly _compGostBlockungListeneintrag : Comparator<GostBlockungListeneintrag> = { compare : (a: GostBlockungListeneintrag, b: GostBlockungListeneintrag) => {
		const splitA : Array<string> = BlockungsUtils.extractTrailingNumber(a.name.trim());
		const splitB : Array<string> = BlockungsUtils.extractTrailingNumber(b.name.trim());
		const praefixA : string = splitA[0].trim();
		const praefixB : string = splitB[0].trim();
		const cmpPrefix : number = JavaString.compareTo(praefixA, praefixB);
		if (cmpPrefix !== 0)
			return cmpPrefix;
		const sizeA : number = splitA[1].length;
		const sizeB : number = splitB[1].length;
		const suffixA : string = sizeA >= sizeB ? splitA[1] : StringUtils.fillWithLeadingZeros(splitA[1], sizeB);
		const suffixB : string = sizeB >= sizeA ? splitB[1] : StringUtils.fillWithLeadingZeros(splitB[1], sizeA);
		const cmpSuffix : number = JavaString.compareTo(suffixA, suffixB);
		if (cmpSuffix !== 0)
			return cmpSuffix;
		return JavaLong.compare(a.id, b.id);
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	/**
	 * Sortiert die Liste nach dem Namen der Blockung. Ist der Suffix eine Zahl, so wird dies bei der Sortierung beachtet.
	 *
	 * @param list Die zu sortierende Liste.
	 */
	public static sortGostBlockungListeneintrag(list : List<GostBlockungListeneintrag>) : void {
		list.sort(BlockungsUtils._compGostBlockungListeneintrag);
	}

	private static extractTrailingNumber(s : string) : Array<string> {
		const prefix : string | null = JavaString.replaceAll(s, "\\d*$", "");
		const suffix : string | null = s.substring(prefix.length);
		return [prefix, suffix];
	}

	/**
	 * Liefert das Minimum beider Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 *
	 * @param value1        Der 1. Zahlenwert.
	 * @param value2orNull  Der 2. Zahlenwert (oder NULL).
	 *
	 * @return das Minimum beider Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 */
	public static minVI(value1 : number, value2orNull : number | null) : number {
		if (value2orNull === null)
			return value1;
		const value2 : number = value2orNull.valueOf();
		return value1 <= value2 ? value1 : value2;
	}

	/**
	 * Liefert das Maximum beider Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 *
	 * @param value1        Der 1. Zahlenwert.
	 * @param value2orNull  Der 2. Zahlenwert (oder NULL).
	 *
	 * @return das Maximum beider Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 */
	public static maxVI(value1 : number, value2orNull : number | null) : number {
		if (value2orNull === null)
			return value1;
		const value2 : number = value2orNull.valueOf();
		return value1 >= value2 ? value1 : value2;
	}

	/**
	 * Liefert das Minimum zweier Integer-Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 *
	 * @param a  Der 1. Zahlenwert (oder NULL).
	 * @param b  Der 2. Zahlenwert (oder NULL).
	 *
	 * @return das Minimum zweier Integer-Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 */
	public static minII(a : number | null, b : number | null) : number | null {
		if (a === null)
			return b;
		if (b === null)
			return a;
		return a <= b ? a : b;
	}

	/**
	 * Liefert das Maximum zweier Integer-Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 *
	 * @param a  Der 1. Zahlenwert (oder NULL).
	 * @param b  Der 2. Zahlenwert (oder NULL).
	 *
	 * @return das Maximum zweier Integer-Zahlenwerte. Ein NULL-Wert wird ignoriert.
	 */
	public static maxII(a : number | null, b : number | null) : number | null {
		if (a === null)
			return b;
		if (b === null)
			return a;
		return a >= b ? a : b;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.BlockungsUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.BlockungsUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_BlockungsUtils(obj : unknown) : BlockungsUtils {
	return obj as BlockungsUtils;
}
