import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { List } from "@core/java/util/List";

import { GruppenprozessBereich, GruppenprozessBereichSchluessel } from "../GruppenprozessBereich";
import { api } from "~/router/Api";

/**
 * Der Bereich der Gruppenprozesse für die Daten der Klassen der Mehrfachauswahl.
 */
export class KlassenDatenBereich extends GruppenprozessBereich<KlassenListeEintrag, KlassenDaten> {

	/** Der Schlüssel, unter dem der Bereich bei den Gruppenprozessen angemeldet ist */
	public static readonly SCHLUESSEL = new GruppenprozessBereichSchluessel<KlassenDatenBereich>("Gruppenprozesse der Klassen: Daten");

	/**
	 * Erzeugt einen neuen Bereich für die Daten der Klassen der Mehrfachauswahl.
	 */
	public constructor() {
		super(daten => daten.id);
	}

	public get schluessel(): GruppenprozessBereichSchluessel<KlassenDatenBereich> {
		return KlassenDatenBereich.SCHLUESSEL;
	}

	public readonly bezeichnung = "Daten der Klassen";

	protected async ladeDatenMultiple(ids: List<number>): Promise<Iterable<KlassenDaten> | null> {
		if (ids.isEmpty()) {
			return null;
		}
		return await api.server.getKlassenMultiple(ids, api.schema);
	}

}
