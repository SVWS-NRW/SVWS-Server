package de.svws_nrw.mapper.schueler;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;

/**
 * Vergleicht und sortiert {@link SchuelerListeEintrag}-Objekte nach der ID der Klasse, dem Nachnamen und dem
 * Vornamen. Namen werden mit dem deutschen {@link Collator} verglichen. Ein Wert {@code null} wird dabei vor
 * jedem anderen Wert einsortiert.
 */
public final class SchuelerListeEintragComparator implements Comparator<SchuelerListeEintrag> {

	/** Instanz des Comparators */
	public static final SchuelerListeEintragComparator INSTANCE = new SchuelerListeEintragComparator();

	/** Vergleicht Namen mit dem deutschen Collator, null zuerst. Der Collator synchronisiert seine Vergleiche und darf daher geteilt werden. */
	private static final Comparator<String> NAMEN = Comparator.nullsFirst(Collator.getInstance(Locale.GERMAN)::compare);

	/** Der eigentliche Vergleich: erst nach der Klasse, dann nach dem Nachnamen und zuletzt nach dem Vornamen. */
	private static final Comparator<SchuelerListeEintrag> VERGLEICH = Comparator
			.comparing((final SchuelerListeEintrag e) -> e.idKlasse, Comparator.nullsFirst(Comparator.naturalOrder()))
			.thenComparing(e -> e.nachname, NAMEN)
			.thenComparing(e -> e.vorname, NAMEN);

	/**
	 * Privater Konstruktor, damit nur die Instanz {@link #INSTANCE} verwendet wird.
	 */
	private SchuelerListeEintragComparator() {
		// leer
	}

	@Override
	public int compare(final SchuelerListeEintrag a, final SchuelerListeEintrag b) {
		return VERGLEICH.compare(a, b);
	}

}
