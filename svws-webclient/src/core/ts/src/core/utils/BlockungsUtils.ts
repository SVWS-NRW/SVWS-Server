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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.BlockungsUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_BlockungsUtils(obj : unknown) : BlockungsUtils {
	return obj as BlockungsUtils;
}
