package de.svws_nrw.schuldatei.v1.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.schuldatei.v1.data.SchuldateiKatalogeintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dient der Handhabung von Katalogen zur Schuldatei.
 */
public class SchuldateiKatalogManager {

	/** Der Name des Katalogs */
	private final @NotNull String _name;

	/** Die Liste aller Katalog-Einträge dieses Katalogs */
	private final @NotNull List<@NotNull SchuldateiKatalogeintrag> _eintraege = new ArrayList<>();

	/** Eine Map von dem Wert der Katalog-Einträge auf diese */
	private final @NotNull Map<@NotNull String, @NotNull SchuldateiKatalogeintrag> _mapEintragByWert = new HashMap<>();


	/**
	 * Erstellt einen neuen Katalog-Manager.
	 *
	 * @param name   der Name des Katalogs
	 */
	public SchuldateiKatalogManager(final @NotNull String name) {
		this._name = name;
	}


	/**
	 * Fügt einen neuen Eintrag zum Manager hinzu.
	 *
	 * @param eintrag   der Eintrag
	 */
	void addEintrag(final @NotNull SchuldateiKatalogeintrag eintrag) {
		this._eintraege.add(eintrag);
		if (this._mapEintragByWert.containsKey(eintrag.wert))
			throw new IllegalArgumentException("Katalog " + this._name + ": Es existiert bereits ein anderer Katalog-Eintrag mit dem angegebenen Wert " + eintrag.wert + ".");
		this._mapEintragByWert.put(eintrag.wert, eintrag);
	}


	/**
	 * Gibt den Namen des Katalogs zurück.
	 *
	 * @return der Name des Katalogs
	 */
	public @NotNull String getName() {
		return _name;
	}


	/**
	 * Gibt den Katalog-Eintrag zu dem Wert zurück, sofern der Wert gültig ist.
	 *
	 * @param wert   der Wert des gesuchten Katalog-Eintrags
	 *
	 * @return der Katalog-Eintrag oder null, wenn es keinen für den Wert gibt.
	 */
	public SchuldateiKatalogeintrag getEintrag(final String wert) {
		return this._mapEintragByWert.get(wert);
	}


	/**
	 * Gibt die Bezeichnung des Katalog-Eintrag zu dem Wert zurück, sofern
	 * der Wert gültig ist.
	 *
	 * @param wert   der Wert des Katalog-Eintrags
	 *
	 * @return die Bezeichnung
	 *
	 * @throws IllegalArgumentException   falls der Wert ungültig ist
	 */
	public @NotNull String getBezeichnung(final String wert) throws IllegalArgumentException {
		final SchuldateiKatalogeintrag eintrag = getEintrag(wert);
		if (eintrag == null)
			throw new IllegalArgumentException("Es konnte kein Katalog-Eintrag für den Wert " + wert + " gefunden werden.");
		return eintrag.bezeichnung;
	}

}