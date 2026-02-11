package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten des schulspezifischen Betriebsansprechpartner übergeben werden.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag im Katalog der schulspezifischen Betriebsansprechpartner.")
@TranspilerDTO
public class BetriebeAnsprechpartner {

	/** Die ID des Ansprechpartners. */
	@Schema(description = "Die ID des Ansprechpartners.", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die ID des Betriebs. */
	@Schema(description = "Die ID des Betriebs.", example = "4711")
	public long idBetrieb;

	/** Das Anrede des Ansprechpartners. */
	@Schema(description = "Das Anrede des Ansprechpartners.", example = "Frau")
	public String anrede;

	/** Der Name des Ansprechpartners. */
	@Schema(description = "Der Name des Ansprechpartners.", example = "Igel")
	public String name;

	/** Der Rufname des Ansprechpartners. */
	@Schema(description = "Der Rufname des Ansprechpartners.", example = "Frosch")
	public String rufname;

	/** Die Telefonnummer des Ansprechpartners. */
	@Schema(description = "Die Telefonnummer des Ansprechpartners.", example = "1234")
	public String telefon;

	/** Die eMail des Ansprechpartners. */
	@Schema(description = "Die eMail des Ansprechpartners.", example = "1234@aol.com")
	public String eMail;

	/** Gibt an, ob der Ansprechpartner in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob der Ansprechpartner in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public boolean referenziertInAnderenTabellen;

}
