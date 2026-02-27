package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob der angegebene Anrechnungsgrund im Katalog der Anrechnungsgründe existiert.
 */
public final class ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Anrechnungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen;

	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Anrechnungsgründe im Katalog.
	 *
	 * @param anrechnungen  die Liste der Anrechnungsstunden
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorLppa01LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
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

		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : liste)
			// Prüfung, ob die ID im Katalog vorhanden ist.
			// Hinweis: Die reine Pflichtfeldprüfung (idGrund == null) erfolgt bereits in LPPA00.
			if ((eintrag.idGrund == null) || (LehrerAnrechnungsgrund.data().getWertByIDOrNull(eintrag.idGrund) == null)) {
				addFehler(0, "Feld 'Anrechnungsgründe' muss besetzt sein.");
				return false;
			}

		return true;
	}
}
