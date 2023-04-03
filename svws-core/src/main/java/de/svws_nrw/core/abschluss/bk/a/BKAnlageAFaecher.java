package de.svws_nrw.core.abschluss.bk.a;

import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * TODO
 */
@XmlRootElement(name = "BKAbschlussFaecher")
@Schema(name = "BKAbschlussFaecher", description = "Die Fachinformationen für eine Abschlussberechnung.")
public class BKAnlageAFaecher {

	/** Die Fachinformationen. */
	@Schema(description = "Die Fachinformationen.")
	public List<@NotNull BKAnlageAFach> faecher;

	/** Information zur praktischen Teil der Berufsabschlussprüfung (IHK). */
	@Schema(description = "Information zur praktischen Teil der Berufsabschlussprüfung (IHK).")
	public Boolean hatBestandenBerufsAbschlussPruefung;

	/** Die Bezeichnung des Sprachreferenzniveaus in Englisch nach dem GeR. */
	@Schema(description = "die Bezeichnung des Sprachreferenzniveaus in Englisch nach dem GeR")
	public String englischGeR;

}

