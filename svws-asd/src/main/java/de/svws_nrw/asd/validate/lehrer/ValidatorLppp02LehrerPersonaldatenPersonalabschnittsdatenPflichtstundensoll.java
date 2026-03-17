package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Pflichtstundensoll der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/** Das Pflichtstundensoll */
	private final @NotNull Supplier<@AllowNull Double> _pflichtstundensoll;

	/** Der Einsatzstatus */
	private final @NotNull Supplier<@AllowNull Long> _idEinsatzstatus;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll    das Pflichtstundensoll
	 * @param idEinsatzstatus       der Einsatzstatus
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._pflichtstundensoll = pflichtstundensoll;
		this._idEinsatzstatus = idEinsatzstatus;
	}


	@Override
	protected boolean pruefe() {
		final Double pflichtstundensoll = this._pflichtstundensoll.get();
		final Long idEinsatzstatus = this._idEinsatzstatus.get();
		final LehrerEinsatzstatus einsatzstatus = (idEinsatzstatus == null) ? null : LehrerEinsatzstatus.data().getWertByID(idEinsatzstatus);

		if ((einsatzstatus == LehrerEinsatzstatus.B) && (pflichtstundensoll == 0.0)) {
			this.addFehler(2,
					"Bei Lehrkräften, die von einer anderen Schule abgeordnet wurden (Einsatzstatus = 'B'), darf das Pflichtstundensoll nicht 0,00 betragen.");
			return false;
		}

		return true;
	}

}
