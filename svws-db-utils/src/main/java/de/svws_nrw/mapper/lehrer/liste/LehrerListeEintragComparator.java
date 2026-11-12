package de.svws_nrw.mapper.lehrer.liste;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;

/**
 * Vergleicht und sortiert {@link LehrerListeEintrag}-Objekte nach dem Kürzel, dem Nachnamen und dem Vornamen.
 * Die Werte werden mit dem deutschen {@link Collator} verglichen. Ein Wert {@code null} wird dabei vor
 * jedem anderen Wert einsortiert.
 */
public final class LehrerListeEintragComparator implements Comparator<LehrerListeEintrag> {

	/** Instanz des Comparators */
	public static final LehrerListeEintragComparator INSTANCE = new LehrerListeEintragComparator();

	/** Vergleicht Texte mit dem deutschen Collator, null zuerst. Der Collator synchronisiert seine Vergleiche und darf daher geteilt werden. */
	private static final Comparator<String> TEXTE = Comparator.nullsFirst(Collator.getInstance(Locale.GERMAN)::compare);

	/** Der eigentliche Vergleich: erst nach dem Kürzel, dann nach dem Nachnamen und zuletzt nach dem Vornamen. */
	private static final Comparator<LehrerListeEintrag> VERGLEICH = Comparator
			.comparing((final LehrerListeEintrag e) -> e.kuerzel, TEXTE)
			.thenComparing(e -> e.nachname, TEXTE)
			.thenComparing(e -> e.vorname, TEXTE);

	/**
	 * Privater Konstruktor, damit nur die Instanz {@link #INSTANCE} verwendet wird.
	 */
	private LehrerListeEintragComparator() {
		// leer
	}

	@Override
	public int compare(final LehrerListeEintrag a, final LehrerListeEintrag b) {
		return VERGLEICH.compare(a, b);
	}

}
