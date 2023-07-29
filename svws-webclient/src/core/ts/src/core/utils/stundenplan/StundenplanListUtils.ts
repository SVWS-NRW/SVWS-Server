import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanListeEintrag } from '../../../core/data/stundenplan/StundenplanListeEintrag';
import type { List } from '../../../java/util/List';
import { JavaString } from '../../../java/lang/JavaString';

export class StundenplanListUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Sucht aus der übergebenen Liste den Eintrag heraus, für den das übergebene
	 * Datum eine Gültigkeit liefert oder den Plan, der als letztes aktiv war, falls
	 * alle Stundenpläne abgelaufen sind.
	 *
	 * @param eintraege die Liste der Stundenplanlisten-Einträge
	 * @param datum     das Datum, für das der gültige Plan gesucht wird
	 *
	 * @return den StundenplanListeEintrag, der für das angegebene Datum gültig
	 *         ist oder der letzte gültige Plan, falls die Gültigkeit aller Pläne
	 *         abgelaufen ist.
	 */
	public static get(eintraege : List<StundenplanListeEintrag>, datum : string) : StundenplanListeEintrag | null {
		let last : StundenplanListeEintrag | null = null;
		for (const eintrag of eintraege) {
			if (JavaString.compareTo(eintrag.gueltigAb, datum) <= 0 && JavaString.compareTo(eintrag.gueltigBis, datum) >= 0)
				return eintrag;
			if (last === null || JavaString.compareTo(eintrag.gueltigAb, last.gueltigAb) > 0)
				last = eintrag;
		}
		return last;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplan.StundenplanListUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanListUtils(obj : unknown) : StundenplanListUtils {
	return obj as StundenplanListUtils;
}
