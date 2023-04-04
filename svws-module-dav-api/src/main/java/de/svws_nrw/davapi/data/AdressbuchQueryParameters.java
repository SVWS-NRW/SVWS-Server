package de.svws_nrw.davapi.data;
/**
 * Diese Klasse abstrahiert verschiedene Parameter für die Suche nach Adressbuechern aus einem Repository
 *
 */
public class AdressbuchQueryParameters {

	/**
	 * Parameter bestimmt, ob die angefragten Adressbuecher die Adressbucheinträge enthalten sollen.
	 */
	public boolean includeAdressbuchEintraege;

	/**
	 * Parameter bestimmt, ob die angefragten Adressbucheinträge nur IDs enthalten sollen, also keine weiteren
	 * Informationen wie Adressdaten, Namen, etc.
	 */
	public boolean includeAdressbuchEintragIDsOnly;

}
