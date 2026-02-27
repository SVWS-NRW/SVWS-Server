package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob bei allen Anrechnungsstunden ein Grund angegeben wurde (Pflichtfeldprüfung).
 */
public final class ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Anrechnungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen;

	/**
	 * Erstellt einen neuen Validator für die Pflichtfeldprüfung der Anrechnungsgründe.
	 *
	 * @param anrechnungen  die Liste der Anrechnungsstunden
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorLppa00LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.anrechnungen = anrechnungen;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> liste = this.anrechnungen.get();

		if (liste == null)
			return false;

		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : liste)
			// Das Feld 'Anrechnungsgründe' (idGrund) muss besetzt sein.
			if (eintrag.idGrund == null) {
				this.addFehler(0, "Das Feld 'Anrechnungsgründe' muss besetzt sein.");
				return false;
			}

		return true;
	}
}
