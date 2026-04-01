package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die durchschnittlichen Schülerzahlen der Klinikschulen (K89).
 */
@XmlRootElement
@Schema(description = "die durchschnittlichen Schülerzahlen der Klinikschulen (K89).")
@TranspilerDTO
public class KlinikschuleStatistikExport {

	/** Schüler allgemeinbildend insgesamt. */
	@Schema(description = "schüler allgemeinbildend insgesamt", example = "12.3")
	public double allgemeinbildendInsgesamt = 0.0;

	/** Schüler allgemeinbildend mit Schwerstbehinderung. */
	@Schema(description = "schüler allgemeinbildend mit Schwerstbehinderung", example = "3.3")
	public double allgemeinbildendSchwerstbehindert = 0.0;

	/** Schüler berufsbildend Teilzeit insgesamt. */
	@Schema(description = "schüler berufsbildend Teilzeit insgesamt", example = "8.1")
	public double berufsbildendTeilzeitInsgesamt = 0.0;

	/** Schüler berufsbildend Teilzeit mit Schwerstbehinderung. */
	@Schema(description = "schüler berufsbildend Teilzeit mit Schwerstbehinderung", example = "2.7")
	public double berufsbildendTeilzeitSchwerstbehindert = 0.0;

	/** Schüler berufsbildend Vollzeit insgesamt. */
	@Schema(description = "schüler berufsbildend Vollzeit insgesamt", example = "9.1")
	public double berufsbildendVollzeitInsgesamt = 0.0;

	/** Schüler berufsbildend Vollzeit mit Schwerstbehinderung. */
	@Schema(description = "schüler berufsbildend Vollzeit mit Schwerstbehinderung", example = "3.7")
	public double berufsbildendVollzeitSchwerstbehindert = 0.0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlinikschuleStatistikExport() {
		// leer
	}

}
