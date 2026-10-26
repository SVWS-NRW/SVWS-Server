package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Beschäftigungsart der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idSchuljahresabschnitt;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param idBeschaeftigungsart     die Beschäftigungsart
	 * @param idEinsatzstatus     	   der Einsatzstatus
	 * @param pflichtstundensoll       das Pflichtstundensoll
	 * @param kontext   			   der Kontext des Validators
	 */
	public ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idBeschaeftigungsart,
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_idSchuljahresabschnitt = idSchuljahresabschnitt;

		final @NotNull Supplier<@NotNull Integer> schuljahr = () ->  kontext.getSchuljahrBySchuljahresabschnitt(getNotNullSupplierLong(idSchuljahresabschnitt));

		final @NotNull Supplier<@AllowNull LehrerEinsatzstatus> einsatzstatus = () -> LehrerEinsatzstatus.data().getWertByIDOrNull(idEinsatzstatus.get());

		_validatoren.add(new ValidatorLppb00LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(schuljahr, idBeschaeftigungsart, pflichtstundensoll, einsatzstatus,
				kontext));
	}


	@Override
	protected boolean pruefe() {
		return true;
	}

}
