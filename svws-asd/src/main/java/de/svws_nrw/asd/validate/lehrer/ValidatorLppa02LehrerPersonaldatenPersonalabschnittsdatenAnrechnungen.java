package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob der gewählte Anrechnungsgrund im Kontext des aktuellen Schuljahres gültig ist.
 */
public final class ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Anrechnungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen;

	/**
	 * Erstellt einen neuen Validator für die zeitliche Gültigkeit der Anrechnungsgründe.
	 *
	 * @param anrechnungen die Liste der Anrechnungsstunden
	 * @param kontext      der Kontext des Validators
	 */
	public ValidatorLppa02LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.anrechnungen = anrechnungen;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> liste = this.anrechnungen.get();
		if (liste == null)
			return true;

		final int aktuellesSchuljahr = this.kontext().getSchuljahr();

		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : liste) {
			// Falls keine ID vorhanden ist, wird dies in LPPA00/01 geprüft.
			if (eintrag.idGrund == null)
				continue;

			final LehrerAnrechnungsgrund grund = LehrerAnrechnungsgrund.data().getWertByIDOrNull(eintrag.idGrund);

			// Ein Grund ist ungültig, wenn er nicht im Katalog existiert oder
			// für das spezifische Schuljahr keine gültigen Historien-Daten (gueltigVon/Bis) vorliegen.
			if ((grund == null) || (grund.daten(aktuellesSchuljahr) == null)) {
				addFehler(0, "Der eingetragene Wert für das Feld 'Anrechnungsgründe' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
				return false;
			}
		}
		return true;
	}
}
