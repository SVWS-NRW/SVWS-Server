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
import de.svws_nrw.core.types.Wochentag;
import jakarta.validation.constraints.NotNull;

public final class StundenplanUnterrichtUtils {
	private StundenplanUnterrichtUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/** Ein Default-Comparator für den Vergleich von Unterrichten in Unterrichtelisten. */
	public static final @NotNull Comparator<@NotNull StundenplanUnterricht> comparator = (
			final @NotNull StundenplanUnterricht a, final @NotNull StundenplanUnterricht b) -> {
		return Long.compare(a.id, b.id);
	};

	/** Ein Default-Comparator für den Vergleich von Fächern in Listen. */
	public static final @NotNull Comparator<@NotNull StundenplanFach> comparatorFaecher = (final @NotNull StundenplanFach a, final @NotNull StundenplanFach b) -> {
		int cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

	/** Ein Default-Comparator für den Vergleich von Kurs in Listen. */
	public static final @NotNull Comparator<@NotNull StundenplanKurs> comparatorKurse = (final @NotNull StundenplanKurs a, final @NotNull StundenplanKurs b) -> {
		int cmp = Long.compare(a.id, b.id);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

	/** Ein Default-Comparator für den Vergleich von Räumen in Listen. */
	public static final @NotNull Comparator<@NotNull StundenplanRaum> comparatorRaeume = (final @NotNull StundenplanRaum a, final @NotNull StundenplanRaum b) -> {
		int cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

	/** Ein Default-Comparator für den Vergleich von Räumen in Listen. */
	public static final @NotNull Comparator<@NotNull StundenplanSchiene> comparatorSchienen = (final @NotNull StundenplanSchiene a, final @NotNull StundenplanSchiene b) -> {
		return Long.compare(a.nummer, b.nummer);
	};

	/** Ein Default-Comparator für den Vergleich von Wochentagen in Listen. */
	public static final @NotNull Comparator<@NotNull Wochentag> comparatorWochentag = (final @NotNull Wochentag a, final @NotNull Wochentag b) -> {
		return Long.compare(a.id, b.id);
	};

	/** Ein Default-Comparator für den Vergleich von Stunden in Listen. */
	public static final @NotNull Comparator<@NotNull Integer> comparatorStunden = (final @NotNull Integer a, final @NotNull Integer b) -> {
		return Long.compare(a, b);
	};

	/** Ein Default-Comparator für den Vergleich von Klassen in Listen. */
	public static final @NotNull Comparator<@NotNull StundenplanKlasse> comparatorKlassen = (final @NotNull StundenplanKlasse a, final @NotNull StundenplanKlasse b) -> {
		int cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

	/** Ein Default-Comparator für den Vergleich von Lehrern in Listen. */
	public static final @NotNull Comparator<@NotNull StundenplanLehrer> comparatorLehrer = (final @NotNull StundenplanLehrer a, final @NotNull StundenplanLehrer b) -> {
		int cmp = a.kuerzel.compareTo(b.kuerzel);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

	/** Ein Default-Comparator für den Vergleich von Schülern in Listen. */
	public static final @NotNull Comparator<@NotNull StundenplanSchueler> comparatorSchueler = (final @NotNull StundenplanSchueler a, final @NotNull StundenplanSchueler b) -> {
		int cmp = a.nachname.compareTo(b.nachname);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

}
