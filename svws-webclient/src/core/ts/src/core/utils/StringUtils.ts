import { JavaObject } from '../../java/lang/JavaObject';
import type { JavaSet } from '../../java/util/JavaSet';
import { StringBuilder } from '../../java/lang/StringBuilder';

export class StringUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Liefert einen durch Komma getrennten String aller Inhalte des übergebenen Sets.
	 *
	 * @param set  Das Set der zu verbindenden Inhalte.
	 *
	 * @return einen durch Komma getrennten String aller Inhalte des übergebenen Sets.
	 */
	public static toKommaSeperatedString(set : JavaSet<string>) : string {
		const sb : StringBuilder = new StringBuilder();
		for (const s of set)
			if (sb.isEmpty())
				sb.append(s);
			else
				sb.append(", " + s!);
		return sb.toString();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.StringUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_StringUtils(obj : unknown) : StringUtils {
	return obj as StringUtils;
}
