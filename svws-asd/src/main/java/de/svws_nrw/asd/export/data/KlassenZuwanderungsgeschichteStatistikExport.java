package de.svws_nrw.asd.export.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Daten zur Zuwanderungsgeschichte der Schüler in der (Teil-) Klasse (X98)
 */
@XmlRootElement
@Schema(description = "die Daten zur Zuwanderungsgeschichte der Schüler in der (Teil-) Klasse (X98)")
@TranspilerDTO
public class KlassenZuwanderungsgeschichteStatistikExport {

	/** Die Schüler mit Zuwanderungsgeschichte insgesamt. */
	@Schema(description = "die Schüler mit Zuwanderungsgeschichte insgesamt.", example = "12")
	public int zuwanderungsgeschichteInsgesamt = 0;

	/** Die Schüler mit eigenem Zuzug. */
	@Schema(description = "die Schüler mit eigenem Zuzug.", example = "5")
	public int zuwanderungsgeschichteEigenerZuzug = 0;

	/** Die Schüler mit mindestens einem im Ausland geborenen Elternteil. */
	@Schema(description = "die Schüler mit mindestens einem im Ausland geborenen Elternteil.", example = "10")
	public int zuwanderungsgeschichteElternteilZugezogen = 0;

	/** Die Schüler mit nicht deutscher Verkehrssprache in der Familie. */
	@Schema(description = "die Schüler mit nicht deutscher Verkehrssprache in der Familie.", example = "8")
	public int zuwanderungsgeschichteNichtDeutscheVerkehrssprache = 0;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenZuwanderungsgeschichteStatistikExport() {
	}

}
