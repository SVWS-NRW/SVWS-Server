package de.nrw.schule.svws.core.types.dav;

import jakarta.validation.constraints.NotNull;

/** Eine Klasse für die Arten von DavRessourceCollections */
public enum DavRessourceCollectionTyp {
	/** Eine Ressourcensammlung für Adressdaten */
	ADRESSBUCH(0),
	/** Eine Ressourcensammlung für Kalenderdaten */
	KALENDER(1),
	/** Eine Ressourcensammlung, die den eigenen Kalender repräsentiert */
	EIGENER_KALENDER(2),
	/** Eine Ressourcensammlung, die das eigene Adressbuch repräsentiert */
	EIGENES_ADRESSBUCH(3);

	/** die ID */
	public final int id;

	/**
	 * Privater Konstruktor für das Enum mit der in der DB verwendeten ID des Typs
	 * 
	 * @param id die ID
	 */
	private DavRessourceCollectionTyp(int id) {
		this.id = id;
	}

	/**
	 * Gibt den RessourceCollectionTyp anhand der ID zurück.
	 * 
	 * @param id die ID des DavRessourceCollectionTyps
	 * @return den DavRessourceCollectionTyp oder null, wenn die ID fehlerhaft ist
	 */
	public static @NotNull DavRessourceCollectionTyp getByID(int id) {
		switch (id) {
		case 0:
			return ADRESSBUCH;
		case 1:
			return KALENDER;
		case 2:
			return EIGENER_KALENDER;
		case 3:
			return EIGENES_ADRESSBUCH;
		default:
			throw new IllegalArgumentException("Die angegebene ID '" + id + "' ist ungültig.");
		}
	}
}
