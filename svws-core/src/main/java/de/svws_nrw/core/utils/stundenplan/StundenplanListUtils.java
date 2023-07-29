package de.svws_nrw.core.utils.stundenplan;

import java.util.List;

import de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet statische Methoden für den Typ {@link StundenplanListeEintrag}.
 */
public final class StundenplanListUtils {

	private StundenplanListUtils() {
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
	public static StundenplanListeEintrag get(final @NotNull List<@NotNull StundenplanListeEintrag> eintraege, final @NotNull String datum) {
		StundenplanListeEintrag last = null;
		for (final StundenplanListeEintrag eintrag : eintraege) {
			if (eintrag.gueltigAb.compareTo(datum) <= 0 && eintrag.gueltigBis.compareTo(datum) >= 0)
				return eintrag;
			if (last == null || eintrag.gueltigAb.compareTo(last.gueltigAb) > 0)
				last = eintrag;
		}
		return last;
	}

}
