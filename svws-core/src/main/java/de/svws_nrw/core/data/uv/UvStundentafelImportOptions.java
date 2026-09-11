package de.svws_nrw.core.data.uv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/** Die Angaben zum Erstellen einer Stundentafel durch Import aus Schild-Leistungsdaten. */
@Schema(description = "die Informationen zum Import einer UV-Stundentafel aus Schüler-Leistungsdaten.")
@TranspilerDTO
@XmlRootElement
public class UvStundentafelImportOptions {
	/** Die ID des Jahrgangs. */
	@NotNull(message = "Die ID des Jahrgangs muss gesetzt werden.")
	public Long idJahrgang = 0L;
	/** Die Bezeichnung der Stundentafel. */
	@NotNull(message = "Die Bezeichnung muss gesetzt werden.")
	public String bezeichnung = "";
	/** Das Datum, ab dem die Stundentafel gültig ist. */
	@NotNull(message = "Das Datum gueltigVon muss gesetzt werden.")
	public String gueltigVon = "";
	/** Das Datum, bis zu dem die Stundentafel gültig ist. */
	public String gueltigBis;
	/** Eine optionale Beschreibung. */
	public String beschreibung;
	/** Das Schuljahr mit den Quelldaten. */
	@NotNull(message = "Das Schuljahr muss gesetzt werden.")
	public Integer schuljahr = 0;
	/** Die ID der Schild-Klasse mit den Quelldaten. */
	@NotNull(message = "Die ID der Klasse muss gesetzt werden.")
	public Long idKlasse = 0L;
	/** Gibt an, ob für fehlende Schild-Fächer neue UV-Fächer angelegt werden sollen. */
	public boolean fehlendeUvFaecherAnlegen = true;

	/**
	 * Prüft, ob die angegebenen Gültigkeitsdaten gültig und in der richtigen Reihenfolge sind.
	 *
	 * @return true, wenn die Gültigkeitsdaten gültig sind
	 */
	@JsonIgnore
	@Schema(hidden = true)
	@AssertTrue(message = "Die Gültigkeitsdaten sind ungültig.")
	public boolean isGueltigkeitszeitraumGueltig() {
		if (gueltigVon == null) {
			return true;
		}
		if (!DateUtils.isValidDate(gueltigVon)) {
			return false;
		}
		if (gueltigBis == null) {
			return true;
		}
		return DateUtils.isValidDate(gueltigBis) && (gueltigVon.compareTo(gueltigBis) <= 0);
	}

	/** Leerer Standardkonstruktor. */
	public UvStundentafelImportOptions() {
		// leer
	}
}
