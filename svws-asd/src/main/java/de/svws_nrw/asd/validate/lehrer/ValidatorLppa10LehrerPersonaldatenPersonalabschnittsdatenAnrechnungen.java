package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob bei Lehrkräften mit dem Lehramt 'Schulverwaltungsassistent/-in' (ID 70)
 * ausschließlich der Anrechnungsgrund '935 - Schulverwaltungsassistenz' verwendet wird.
 */
public final class ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Anrechnungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen;

	/** Die Liste der Lehrämter der Lehrkraft. */
	private final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter;

	/**
	 * Erstellt einen neuen Validator für die Exklusivitätsprüfung des Anrechnungsgrundes 935.
	 *
	 * @param anrechnungen die Liste der Anrechnungsstunden
	 * @param lehraemter   die Liste der Lehrämter
	 * @param kontext      der Kontext des Validators
	 */
	public ValidatorLppa10LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen,
			final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.anrechnungen = anrechnungen;
		this.lehraemter = lehraemter;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerLehramtEintrag> listeLehraemter = this.lehraemter.get();
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> listeAnrechnungen = this.anrechnungen.get();

		if (listeLehraemter == null || listeAnrechnungen == null)
			return true;

		final LehrerAnrechnungsgrund grund935 = LehrerAnrechnungsgrund.data().getWertByBezeichner("ID_935");

		for (final LehrerLehramtEintrag lehramtEintrag : listeLehraemter)
			// Falls das Lehramt 'Schulverwaltungsassistent/-in' (ID 70) vorliegt...
			if (LehrerLehramt.ID_70 == LehrerLehramt.data().getWertByIDOrNull(lehramtEintrag.idKatalogLehramt))
				for (final LehrerPersonalabschnittsdatenAnrechnungsstunden anrechnung : listeAnrechnungen) {
					if (anrechnung.idGrund == null)
						continue;

					final LehrerAnrechnungsgrund grund = LehrerAnrechnungsgrund.data().getWertByIDOrNull(anrechnung.idGrund);

					// ...darf kein anderer Grund als '935' eingetragen sein.
					if (grund != grund935) {
						addFehler(0,
								"Für das Lehramt 'Schulverwaltungsassistent/-in' darf nur der Anrechnungsgrund '935 - Schulverwaltungsassistenz' eingetragen sein.");
						return false;
					}
				}
		return true;
	}
}
