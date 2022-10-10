package de.nrw.schule.svws.core.types.dav;

import jakarta.validation.constraints.NotNull;

/** Eine Klasse für die Arten von DavRessourceCollections */
public enum DavRessourceCollectionTyp {
	/** Eine Ressourcensammlung für Adressdaten */
	ADRESSE(0),
	/** Eine Ressourcensammlung für Kalenderdaten */
	KALENDER(1);

	/** die ID */
	public final int id;

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
				return ADRESSE;
			case 1:
				return KALENDER;
			default:
				throw new IllegalArgumentException("Die angegebene ID '" + id + "' ist ungültig.");
		}
	}
}
