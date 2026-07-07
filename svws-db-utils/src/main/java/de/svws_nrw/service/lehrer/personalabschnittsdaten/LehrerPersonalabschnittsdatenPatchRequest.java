package de.svws_nrw.service.lehrer.personalabschnittsdaten;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public class LehrerPersonalabschnittsdatenPatchRequest implements LehrerPersonalabschnittsdatenPatchable {

	/** Pflichtstundensoll des Lehrers. */
	@Schema(description = "Pflichtstundensoll des Lehrers.", example = "18.5")
	public JsonNullable<@Digits(integer = 4, fraction = 2) Double> pflichtstundensoll = JsonNullable.undefined();

	/** ID des Rechtsverhältnisses des Lehrers (z.B. Beamter auf Lebenszeit). */
	@Schema(description = "ID des Rechtsverhältnisses des Lehrers (z.B. Beamter auf Lebenszeit).", example = "L")
	public JsonNullable<Long> idRechtsverhaeltnis = JsonNullable.undefined();

	/** ID der Beschäftigungsart (z.B. Vollzeit, Teilzeit). */
	@Schema(description = "ID der Beschäftigungsart (z.B. Vollzeit, Teilzeit).", example = "T")
	public JsonNullable<Long> idBeschaeftigungsart = JsonNullable.undefined();

	/** ID des Einsatzstatus (z.B. Stammschule, nur hier tätig). */
	@Schema(description = "ID des Einsatzstatus (z.B. Stammschule, nur hier tätig).", example = "A")
	public JsonNullable<Long> idEinsatzstatus = JsonNullable.undefined();

	/** Schulnummer der Stammschule, falls abweichend. */
	@Schema(description = "Schulnummer der Stammschule, falls abweichend.", example = "168890")
	public JsonNullable<@Size(max = 6) @NotEmpty String> stammschulnummer = JsonNullable.undefined();

	/**
	 * @return pflichtstundensoll
	 */
	@Override
	public JsonNullable<Double> getPflichtstundensoll() {
		return pflichtstundensoll;
	}

	/**
	 * @return idRechtsverhaeltnis
	 */
	@Override
	public JsonNullable<Long> getIdRechtsverhaeltnis() {
		return idRechtsverhaeltnis;
	}

	/**
	 * @return idBeschaeftigungsart
	 */
	@Override
	public JsonNullable<Long> getIdBeschaeftigungsart() {
		return idBeschaeftigungsart;
	}

	/**
	 * @return idEinsatzstatus
	 */
	@Override
	public JsonNullable<Long> getIdEinsatzstatus() {
		return idEinsatzstatus;
	}

	/**
	 * @return stammschulnummer
	 */
	@Override
	public JsonNullable<String> getStammschulnummer() {
		return stammschulnummer;
	}
}
