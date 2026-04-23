package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten eines DQR-Niveaus.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag im Katalog eines DQR-Niveaus.")
@TranspilerDTO
public class DQRNiveauKatalogEintrag extends CoreTypeData {
}
