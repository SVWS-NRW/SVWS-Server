package de.svws_nrw.asd.validate.lehrer;

import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum im Kontext des Rechtsverhältnisses
 * der Abschnittsdaten eines Lehrers einer Schule aus.
 */
public final class ValidatorLpprb10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart extends Validator {

	/** Das Rechtsverhältnis */
	private final @NotNull Supplier<LehrerRechtsverhaeltnis> _rechtsverhaeltnis;

	/** Die Beschäftigungs */
	private final @NotNull Supplier<LehrerBeschaeftigungsart> _beschaeftigungsart;

	private static final @NotNull Set<LehrerBeschaeftigungsart> setBeschaeftigungsart = Set.of(LehrerBeschaeftigungsart.V, LehrerBeschaeftigungsart.T, LehrerBeschaeftigungsart.TS, LehrerBeschaeftigungsart.SB);

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerRechtsverhaeltnis    das Rechtsverhältnis
	 * @param lehrerBeschaeftigungsart   die Beschäftigungsart
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorLpprb10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnisBeschaeftigungsart(
			final @NotNull Supplier<@NotNull LehrerRechtsverhaeltnis> lehrerRechtsverhaeltnis,
			final @NotNull Supplier<@NotNull LehrerBeschaeftigungsart> lehrerBeschaeftigungsart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_rechtsverhaeltnis = lehrerRechtsverhaeltnis;
		_beschaeftigungsart = lehrerBeschaeftigungsart;
	}

	@Override
	protected boolean pruefe() {

		if (_rechtsverhaeltnis.get().equals(LehrerRechtsverhaeltnis.B) && !setBeschaeftigungsart.contains(_beschaeftigungsart.get())) {

			addFehler(0, "Für das Rechtsverhältnis 'Angestellte, befristet (TVL-Vertrag)' sind die Beschätigungsarten 'Vollzeit', 'Teilzeit', 'Teilzeitbeschäftigung im Blockmodell' und 'Angestellte, nebenberuflich' zulässig.");
			return false;
		}
		return true;
	}

}
