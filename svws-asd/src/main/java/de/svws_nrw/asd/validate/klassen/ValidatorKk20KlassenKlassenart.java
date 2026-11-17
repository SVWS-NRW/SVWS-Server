package de.svws_nrw.asd.validate.klassen;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KK02: Prüft die angegebene Klassenart
 */
public final class ValidatorKk20KlassenKlassenart extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idKlassenart;

	private final @NotNull Supplier<@AllowNull List<Long>> _listIdKlassenartSchueler;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idKlassenart                KlassenartID
	 * @param listIdKlassenartSchueler    Liste der IDs der Klassenart der Schüler dieser Klasse
	 * @param kontext                     der Kontext des Validators
	 */
	public ValidatorKk20KlassenKlassenart(
			final @NotNull Supplier<@AllowNull Long> idKlassenart,
			final @NotNull Supplier<@AllowNull List<Long>> listIdKlassenartSchueler,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKlassenart = idKlassenart;
		this._listIdKlassenartSchueler = listIdKlassenartSchueler;
	}

	@Override
	protected boolean pruefe() {

		final Long idKlassenart = _idKlassenart.get();

		if (!Klassenart.PK.equals(Klassenart.data().getWertByIDOrNull(idKlassenart))) {

			List<Long> listIdKlassenartSchueler = _listIdKlassenartSchueler.get();

			if (listIdKlassenartSchueler != null) {

				for (Long idKlassenartSchueler : listIdKlassenartSchueler) {

					if (Klassenart.PK.equals(Klassenart.data().getWertByIDOrNull(idKlassenartSchueler))) {
						addFehler(0,
								"Klassenart der Klasse: In der vorliegenden Klasse ist mindestens ein(e) Schüler(in) mit der Klassenart 'Profilklasse' vorhanden. In den Daten zu dieser Klasse findet sich jedoch kein entsprechender Eintrag zur Profilklasse.");
						return false;
					}
				}
			}
		}

		return true;

	}
}
