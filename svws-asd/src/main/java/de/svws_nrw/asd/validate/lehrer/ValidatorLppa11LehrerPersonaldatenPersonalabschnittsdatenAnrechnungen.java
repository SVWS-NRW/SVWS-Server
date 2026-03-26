package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob bei Lehrkräften mit dem Lehramt 'Schulverwaltungsassistent/-in' (ID 70)
 * die Summe der Anrechnungsstunden für den Grund '935 - Schulverwaltungsassistenz'
 * exakt dem Pflichtstundensoll entspricht.
 */
public final class ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Anrechnungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen;

	/** Die Liste der Lehrämter der Lehrkraft. */
	private final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter;

	/** Das Pflichtstundensoll der Lehrkraft. */
	private final @NotNull Supplier<@AllowNull Double> pflichtstundensoll;

	/**
	 * Erstellt einen neuen Validator für den Pflichtstundensoll-Abgleich bei Schulverwaltungsassistenten.
	 *
	 * @param anrechnungen       die Liste der Anrechnungsstunden
	 * @param lehraemter         die Liste der Lehrämter
	 * @param pflichtstundensoll das Pflichtstundensoll
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorLppa11LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen,
			final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.anrechnungen = anrechnungen;
		this.lehraemter = lehraemter;
		this.pflichtstundensoll = pflichtstundensoll;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerLehramtEintrag> listeLehraemter = this.lehraemter.get();
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> listeAnrechnungen = this.anrechnungen.get();
		final Double soll = this.pflichtstundensoll.get();

		// Wenn wichtige Datenquellen fehlen, kann die Prüfung nicht durchgeführt werden.
		if (listeLehraemter == null || listeAnrechnungen == null || soll == null) {
			return true;
		}

		// 1. Prüfe Vorbedingung: Besitzt die Lehrkraft das Lehramt 'ID_70' (Schulverwaltungsassistent/-in)?
		boolean hatLehramt70 = false;
		for (final LehrerLehramtEintrag lehramtEintrag : listeLehraemter) {
			final long idKatalog = lehramtEintrag.idKatalogLehramt;
			if (LehrerLehramt.ID_70 == LehrerLehramt.data().getWertByIDOrNull(idKatalog)) {
				hatLehramt70 = true;
				break;
			}
		}

		// Falls die Vorbedingung nicht erfüllt ist, ist dieser Validator nicht relevant.
		if (!hatLehramt70) {
			return true;
		}

		// 2. Berechne die Summe der Anrechnungsstunden für den Grund '935 - Schulverwaltungsassistenz'.
		final LehrerAnrechnungsgrund grund935 = LehrerAnrechnungsgrund.data().getWertByBezeichner("ID_935");
		double summe935 = 0;
		boolean hatAnrechnung935 = false;

		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden anrechnung : listeAnrechnungen) {
			if (anrechnung.idGrund != null) {
				final LehrerAnrechnungsgrund grund = LehrerAnrechnungsgrund.data().getWertByIDOrNull(anrechnung.idGrund);
				if (grund == grund935) {
					hatAnrechnung935 = true;
					summe935 += anrechnung.anzahl;
				}
			}
		}

		// 3. Prüfung der Bedingung: Die Summe der Stunden muss (nahezu) identisch zum Pflichtstundensoll sein.
		// Wir nutzen eine Toleranz von 0.001 für den Vergleich der Double-Werte.
		if (hatAnrechnung935 && (Math.abs(summe935 - soll) > 0.001)) {
			this.addFehler(0,
					"Für das Lehramt 'Schulverwaltungsassistent/-in' muss die Anzahl der Anrechungsstunden bei dem Anrechnungsgrund '935 - Schulverwaltungsassistenz' dem Pflichtstundensoll entsprechen.");
			return false;
		}

		return true;
	}
}
