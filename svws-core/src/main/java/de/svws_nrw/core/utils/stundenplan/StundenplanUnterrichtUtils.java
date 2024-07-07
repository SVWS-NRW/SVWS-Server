package de.svws_nrw.core.utils.stundenplan;

import java.util.Comparator;

import de.svws_nrw.core.data.stundenplan.StundenplanFach;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanKurs;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.data.stundenplan.StundenplanSchueler;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.core.types.Wochentag;
import jakarta.validation.constraints.NotNull;

/**
 * Eine Klasse mit Hilfsmethoden zu den Unterrichten von Stundenplänen
 */
public final class StundenplanUnterrichtUtils {

	private StundenplanUnterrichtUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/** Ein Default-Comparator für den Vergleich von Unterrichten in Unterrichtelisten. */
	public static final @NotNull Comparator<StundenplanUnterricht> comparator =
			(final @NotNull StundenplanUnterricht a, final @NotNull StundenplanUnterricht b) -> Long.compare(a.id, b.id);

	/** Ein Default-Comparator für den Vergleich von Fächern in Listen. */
	public static final @NotNull Comparator<StundenplanFach> comparatorFaecher =
			(final @NotNull StundenplanFach a, final @NotNull StundenplanFach b) -> {
				final int cmp = a.kuerzel.compareTo(b.kuerzel);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

	/** Ein Default-Comparator für den Vergleich von Kurs in Listen. */
	public static final @NotNull Comparator<StundenplanKurs> comparatorKurse =
			(final @NotNull StundenplanKurs a, final @NotNull StundenplanKurs b) -> {
				final int cmp = Long.compare(a.id, b.id);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

	/** Ein Default-Comparator für den Vergleich von Räumen in Listen. */
	public static final @NotNull Comparator<StundenplanRaum> comparatorRaeume =
			(final @NotNull StundenplanRaum a, final @NotNull StundenplanRaum b) -> {
				final int cmp = a.kuerzel.compareTo(b.kuerzel);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

	/** Ein Default-Comparator für den Vergleich von Räumen in Listen. */
	public static final @NotNull Comparator<StundenplanSchiene> comparatorSchienen =
			(final @NotNull StundenplanSchiene a, final @NotNull StundenplanSchiene b) -> Long.compare(a.nummer, b.nummer);

	/** Ein Default-Comparator für den Vergleich von Wochentagen in Listen. */
	public static final @NotNull Comparator<Wochentag> comparatorWochentage =
			(final @NotNull Wochentag a, final @NotNull Wochentag b) -> Long.compare(a.id, b.id);

	/** Ein Default-Comparator für den Vergleich von Zeitrastern in Listen. */
	public static final @NotNull Comparator<StundenplanZeitraster> comparatorZeitraster =
			(final @NotNull StundenplanZeitraster a, final @NotNull StundenplanZeitraster b) -> Long.compare(a.id, b.id);

	/** Ein Default-Comparator für den Vergleich von Stunden in Listen. */
	public static final @NotNull Comparator<Integer> comparatorStunden =
			(final @NotNull Integer a, final @NotNull Integer b) -> Long.compare(a, b);

	/** Ein Default-Comparator für den Vergleich von Wochentypen in Listen. */
	public static final @NotNull Comparator<Integer> comparatorWochentypen =
			(final @NotNull Integer a, final @NotNull Integer b) -> Long.compare(a, b);

	/** Ein Default-Comparator für den Vergleich von Klassen in Listen. */
	public static final @NotNull Comparator<StundenplanKlasse> comparatorKlassen =
			(final @NotNull StundenplanKlasse a, final @NotNull StundenplanKlasse b) -> {
				final int cmp = a.kuerzel.compareTo(b.kuerzel);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

	/** Ein Default-Comparator für den Vergleich von Lehrern in Listen. */
	public static final @NotNull Comparator<StundenplanLehrer> comparatorLehrer =
			(final @NotNull StundenplanLehrer a, final @NotNull StundenplanLehrer b) -> {
				final int cmp = a.kuerzel.compareTo(b.kuerzel);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

	/** Ein Default-Comparator für den Vergleich von Schülern in Listen. */
	public static final @NotNull Comparator<StundenplanSchueler> comparatorSchueler =
			(final @NotNull StundenplanSchueler a, final @NotNull StundenplanSchueler b) -> {
				final int cmp = a.nachname.compareTo(b.nachname);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

}
