package de.svws_nrw.core.data.erzieher;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt eine Auswahl von Daten eines Erziehereintrags aus einer Liste.  
 */
@XmlRootElement
@Schema(description = "ein Eintrag einer Erziehers in der Erzieherliste.")
@TranspilerDTO
public class ErzieherListeEintrag {

	/** Die "Partner"-ID des Erziehers mit der Nummer des Erziehers in der DB angehangen (1 oder 2), welche diesem Erzieher im gleichen DB-Datensatz zugeordnet ist und die gleichen Addressdaten, etc. sich teil. */
	@Schema(description = "die ID des weiteren Erziehers mit der Nummer des Erziehers in der DB angehangen (1 oder 2), welcher diesem Erzieher im gleichen DB-Datensatz zugeordnet ist und die gleichen Addressdaten, etc. sich teil.", example = "4711")
	public long id;
	
	/** Die ID des Schülers, welchem der Erzieher zugeordnet ist. */
	@Schema(description = "die ID des Schülers, welchem der Erzieher zugeordnet ist", example = "4712")
	public long idSchueler;
	
	/** Die ID der Art des Erziehereintrages */
	@Schema(description = "Die ID der Art des Erziehereintrages", example = "56")
	public Long idErzieherArt;

	/** Die Anrede des Erziehers. */
	@Schema(description = "Anrede des Erziehers", example = "Herr")
	public String anrede;

	/** Der Name des Erziehers. */
	@Schema(description = "Der Name des Erziehers", example = "Müller")
	public String name;

	/** Der Vorname des Erziehers. */
	@Schema(description = "Der Vorname des Erziehers", example = "Max")
	public String vorname;

	/** Die E-Mailadresse des  Erziehers. */
	@Schema(description = "Die Email-Adresse des Erziehers", example = "max@test.de")
	public String email;
	
	
}
