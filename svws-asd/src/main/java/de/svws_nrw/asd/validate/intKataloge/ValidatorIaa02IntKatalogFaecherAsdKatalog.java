package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.fach.FachKatalogEintrag;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IAA: Prüft das Feld 'Fach ASD-Kürzel'.
 */
public final class ValidatorIaa02IntKatalogFaecherAsdKatalog extends Validator {

	private final @NotNull Supplier<@NotNull String> _kuerzelStatistik;

	/**
	 * @param kuerzelStatistik    Fach ASD-Kürzel
	 * @param kontext	          Kontext
	 */
	public ValidatorIaa02IntKatalogFaecherAsdKatalog(
			final @NotNull Supplier<@NotNull String> kuerzelStatistik,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._kuerzelStatistik = kuerzelStatistik;
	}

	@Override
	protected boolean pruefe() {

		// Prüfen, ob für das aktuelle Schuljahr ein gültiger Historieneintrag existiert.

		final @AllowNull FachKatalogEintrag fachKatalogEintrag = Fach.data().getEintragBySchuljahrUndSchluessel(kontext().getSchuljahr(), _kuerzelStatistik.get());

		// Prüft auf fehlende Werte, inaktive Schuljahre und falsche Historien-IDs
		if ((fachKatalogEintrag == null)) {
			addFehler(0,
					"Fach: Der eingetragene Wert für das Feld 'Fach ASD-Kürzel' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
