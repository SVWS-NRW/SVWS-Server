package de.svws_nrw.service.lehrer.personalabschnittsdaten;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public final class LehrerPersonalabschnittsdatenCreateRequest implements LehrerPersonalabschnittsdatenPatchable {

	/** ID des Lehrers. */
	@Schema(description = "ID des Lehrers.", example = "4711")
	@NotNull
	public Long idLehrer;

	/** ID des Schuljahresabschnitts zu diesen Abschnittsdaten. */
	@Schema(description = "ID des Schuljahresabschnitts zu diesen Abschnittsdaten.", example = "42")
	@NotNull
	public Long idSchuljahresabschnitt;

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
	public JsonNullable<@NotEmpty @Size(max = 6) String> stammschulnummer = JsonNullable.undefined();

	@Override
	public JsonNullable<Double> getPflichtstundensoll() {
		return pflichtstundensoll;
	}

	@Override
	public JsonNullable<Long> getIdRechtsverhaeltnis() {
		return idRechtsverhaeltnis;
	}

	@Override
	public JsonNullable<Long> getIdBeschaeftigungsart() {
		return idBeschaeftigungsart;
	}

	@Override
	public JsonNullable<Long> getIdEinsatzstatus() {
		return idEinsatzstatus;
	}

	@Override
	public JsonNullable<String> getStammschulnummer() {
		return stammschulnummer;
	}

}
