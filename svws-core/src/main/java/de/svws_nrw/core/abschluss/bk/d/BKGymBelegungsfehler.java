package de.svws_nrw.core.abschluss.bk.d;

import de.svws_nrw.core.types.bk.BKGymBelegungsfehlerArt;
import de.svws_nrw.core.types.bk.BKGymBelegungsfehlerTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse repräsentiert einen Belegungsfehler.
 */
public class BKGymBelegungsfehler {
	/** Der eindeutige Code des Belegungsfehlers */
	public final @NotNull String code;

	/** Die Art des Fehlers */
	public final @NotNull BKGymBelegungsfehlerArt art;

	/** Der Wert des Fehlers höhere Werte gleich schwerwiegenderer Fehler */
	public final @NotNull Integer wert;

	/** Der Text des Fehlers, der ausgegeben wird */
	public final @NotNull String text;


	/**
	 *  Erstellt einen Belegungsfehler mit den angegebenen Daten
	 *
	 * @param fehlertyp   der Belegungsfehler, der mit den args präzisiert wird
	 * @param args        die Parameter für den Belegungsfehler.
	 */
	public BKGymBelegungsfehler(final @NotNull BKGymBelegungsfehlerTyp fehlertyp, final Object... args) {
		this.code = fehlertyp.code;
		this.art = fehlertyp.art;
		this.wert = fehlertyp.wert;
		this.text = fehlertyp.format(args);
	}


	/**
	 * Reicht die Information, ob es ein Fehler ist, durch
	 *
	 * @return true, wenn Fehler, sonst false
	 */
	public boolean istFehler() {
		return (this.art != BKGymBelegungsfehlerArt.HINWEIS);
	}


	/**
	 * Reicht die Information, ob die Fehlerart HINWEIS vorliegt
	 *
	 * @return true, wenn Fehler, sonst false
	 */
	public boolean istInfo() {
		return (this.art == BKGymBelegungsfehlerArt.HINWEIS);
	}
}
