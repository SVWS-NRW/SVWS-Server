package de.svws_nrw.core.data.adressbuch;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Vector;

/**
 * Diese Klasse kapselt verschiedene Adressbuchkontakte in einer Liste, bspw. um
 * Gruppen von Adressdaten/Kontakten entsprechend des vCard v4 Standards zu
 * definieren.
 *
 */
public class AdressbuchKontaktListe extends AdressbuchEintrag {
	/** Die Kategorien dieses Kontakts */
	@ArraySchema(schema = @Schema(description = "Die Kategorien dieses Kontakts", example = "..."))
	public List<AdressbuchKontakt> kontakte = new Vector<>();
}
