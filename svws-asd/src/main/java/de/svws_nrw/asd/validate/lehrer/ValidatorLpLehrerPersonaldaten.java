package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Personaldaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLpLehrerPersonaldaten extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerId                 die LehrerId
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 * @param idStaatsangehoerigkeit   die idStaatsangehoerigkeit des Lehrers
	 * @param idRechtsverhaeltnis      das Rechtsverhältnis
	 * @param pflichtstundensoll       der Pflichtstundensoll
	 * @param anrechnungen             die Anrechnungen
	 * @param idEinsatzstatus          der Einsatz-Status
	 * @param idBeschaeftigungsart     die Beschäftigungsart
	 * @param geburtsdatum             das Geburtsdatum des Lehrers
	 * @param lehraemter               die Liste der Lehrämter, die geprüft werden sollen
	 * @param mehrleistungen           die Liste mit den Einträgen zu Mehrleistungen
	 * @param minderleistungen         die Liste mit den Einträgen zu Minderleistungen
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLpLehrerPersonaldaten(
			final @NotNull Supplier<Long> lehrerId,
			final @NotNull Supplier<Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit,
			final @NotNull Supplier<@AllowNull Long> idRechtsverhaeltnis,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> anrechnungen,
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull Supplier<@AllowNull Long> idBeschaeftigungsart,
			final @NotNull Supplier<@AllowNull String> geburtsdatum,
			final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> mehrleistungen,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistungen,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(idSchuljahresabschnitt, idStaatsangehoerigkeit, idRechtsverhaeltnis, pflichtstundensoll,
				anrechnungen, idEinsatzstatus, idBeschaeftigungsart, geburtsdatum, lehraemter, mehrleistungen, minderleistungen, kontext));

		_validatoren.add(new ValidatorLplLehrerPersonaldatenLehramt(lehraemter, lehrerId, getDateManagerSupplier(geburtsdatum), kontext));
	}

	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
