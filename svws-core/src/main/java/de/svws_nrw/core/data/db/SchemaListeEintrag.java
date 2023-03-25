package de.svws_nrw.core.data.db;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beinhaltet den Namen und die Versionsinformationen eines SVWS-Datenbank-Schemas.  
 */
@XmlRootElement
@Schema(description="ein Eintrag eines SVWS-Schemas in der SVWS-Schema-Liste.")
@TranspilerDTO
public class SchemaListeEintrag {

	/** Der Name des Schemas. */
	@Schema(required = true, description = "der Name des Schemas", example="svwsdb")
	public String name;

	/** Die Revisionsnummer des Schemas. */
	@Schema(required = true, description = "die Revisionsnummer des Schemas", example="3")
	public long revision;

	/** Gibt an, ob das Schema als "verdorben" markiert wurde und deswegen nicht mehr für den produktiven Einsatz in der Schule genutzt werden sollte. */
	@Schema(required = true, description = "gibt an, ob das Schema als \"verdorben\" markiert wurde und deswegen nicht mehr für den produktiven Einsatz in der Schule genutzt werden sollte", example="false")
	public boolean isTainted;

}
