import { JavaObject } from '../../java/lang/JavaObject';
import { StringBuilder } from '../../java/lang/StringBuilder';
import type { Collection } from '../../java/util/Collection';

export class StringUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Liefert einen durch Komma getrennten String aller Inhalte der übergebenen {@link Collection}.
	 *
	 * @param collection  Die zu verbindenden Inhalte.
	 *
	 * @return einen durch Komma getrennten String aller Inhalte der übergebenen {@link Collection}.
	 */
	public static collectionToCommaSeparatedString(collection : Collection<string>) : string {
		const sb : StringBuilder = new StringBuilder();
		for (const s of collection)
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
