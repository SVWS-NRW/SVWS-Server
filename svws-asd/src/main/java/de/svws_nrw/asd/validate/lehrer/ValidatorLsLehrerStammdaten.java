package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsLehrerStammdaten extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt
	 * @param nachname                 der Nachname
	 * @param vorname                  der Vorname
	 * @param geburtsdatum             das Geburtsdatum
	 * @param geschlecht               das Geschlecht
	 * @param kuerzel                  das Lehrerkürzel
	 * @param idStaatsangehoerigkeit   die Staatsangehoerigkeit
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLsLehrerStammdaten(
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull String> nachname,
			final @NotNull Supplier<@AllowNull String> vorname,
			final @NotNull Supplier<@AllowNull String> geburtsdatum,
			final @NotNull Supplier<@AllowNull Integer> geschlecht,
			final @NotNull Supplier<@AllowNull String> kuerzel,
			final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorLsnLehrerStammdatenNachname(nachname, kontext));
		_validatoren.add(new ValidatorLsvLehrerStammdatenVorname(vorname, kontext));
		_validatoren.add(new ValidatorLsdLehrerStammdatenGeburtsdatum(geburtsdatum, kontext));
		_validatoren.add(new ValidatorLsgLehrerStammdatenGeschlecht(geschlecht, kontext));
		_validatoren.add(new ValidatorLskLehrerStammdatenKuerzel(kuerzel, kontext));
		_validatoren.add(new ValidatorLssLehrerStammdatenStaatsangehoerigkeitID(idStaatsangehoerigkeit, idSchuljahresabschnitt, kontext));
	}

	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
