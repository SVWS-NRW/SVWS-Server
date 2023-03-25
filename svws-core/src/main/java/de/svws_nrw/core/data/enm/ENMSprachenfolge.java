package de.svws_nrw.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zur Sprachenfolge eines 
 * Schülers für das Externe-Noten-Modul ENM. 
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zur Sprachenfolge eines Schülers für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMSprachenfolge {

	/** Das Kürzel der Sprache, bereinigt von dem Jahrgang, in dem die Sprache eingesetzt hat */
	@Schema(required = true, description = "Das Kürzel der Sprache, bereinigt von dem Jahrgang, in dem die Sprache eingesetzt hat", example="F")
	public String sprache;
	
	/** Die ID des Faches */
	@Schema(required = true, description = "Die ID des Faches", example="17")
	public long fachID;
	
	/** Das Kürzel des Faches */
	@Schema(required = true, description = "Das Kürzel des Faches", example="GE")
	public String fachKuerzel;

	/** Die Reihenfolge des Faches in der Sprachenfolge (Beispiel 1) */
	@Schema(required = true, description = "Die Reihenfolge des Faches in der Sprachenfolge", example="1")
	public int reihenfolge; 

	/** Die Information, ab welchem Jahrgang die Sprache belegt wurde (Beispiel 5) */
	@Schema(required = false, description = "Die Information, ab welchem Jahrgang die Sprache belegt wurde", example="5")
	public int belegungVonJahrgang;
	
	/** Die Information, ab welchem Abschnitt in dem Jahrgang die Sprache belegt wurde (Beispiel 1) */
	@Schema(required = false, description = "Die Information, ab welchem Abschnitt in dem Jahrgang die "
			+ "Sprache belegt wurde", example="1")
	public int belegungVonAbschnitt; 
	
	/** Die Information, bis zu welchem Jahrgang die Sprache belegt wurde (Beispiel 12), sofern die Sprache bereits abgeschlossen ist */
	@Schema(required = false, description = "Die Information, bis zu welchem Jahrgang die Sprache belegt wurde (Beispiel 12), "
			+ "sofern die Sprache bereits abgeschlossen ist.", example="12")
	public Integer belegungBisJahrgang;
	
	/** Die Information, bis zu welchem Abschnitt in dem Jahrgang die Sprache belegt wurde (Beispiel 2), sofern die Sprache bereits abgeschlossen ist */
	@Schema(required = false, description = "Die Information, bis zu welchem Abschnitt in dem Jahrgang die "
			+ "Sprache belegt wurde, sofern die Sprache bereits abgeschlossen ist.", example="2")
	public Integer belegungBisAbschnitt; 

	/** Die Bezeichnung des Sprachreferenzniveaus, welches bisher erreicht wurde (z.B. B2/C1) */
	@Schema(required = false, description = "Die Bezeichnung des Sprachreferenzniveaus, welches bisher erreicht wurde", example="B2/C1")
	public String referenzniveau;  
	
	/** Die Mindest-Dauer der Belegung in der Sekundarstufe I gemäß den Stufen im Core-Type SprachBelegungSekI (z.B. "0, 2, 4, 6") */
	@Schema(required = false, description = "Die Mindest-Dauer der Belegung in der Sekundarstufe I gemäß den Stufen im Core-Type "
			+ "SprachBelegungSekI (0, 2, 4, 6)", example="4")
	public Integer belegungSekI;
	
}
