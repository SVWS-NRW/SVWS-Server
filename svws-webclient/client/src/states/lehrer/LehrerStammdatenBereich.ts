import type { LehrerStammdaten } from "@core/asd/data/lehrer/LehrerStammdaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";

import { GruppenprozessBereich, GruppenprozessBereichSchluessel } from "../GruppenprozessBereich";
import { api } from "~/router/Api";

/**
 * Der Bereich der Gruppenprozesse für die Stammdaten der Lehrer der Mehrfachauswahl.
 */
export class LehrerStammdatenBereich extends GruppenprozessBereich<LehrerListeEintrag, LehrerStammdaten> {

	/** Der Schlüssel, unter dem der Bereich bei den Gruppenprozessen angemeldet ist */
	public static readonly SCHLUESSEL = new GruppenprozessBereichSchluessel<LehrerStammdatenBereich>("Gruppenprozesse der Lehrer: Stammdaten");

	/**
	 * Erzeugt einen neuen Bereich für die Stammdaten der Lehrer der Mehrfachauswahl.
	 */
	public constructor() {
		super(daten => daten.id);
	}

	public get schluessel(): GruppenprozessBereichSchluessel<LehrerStammdatenBereich> {
		return LehrerStammdatenBereich.SCHLUESSEL;
	}

	public readonly bezeichnung = "Individualdaten";

	protected async ladeDatenMultiple(ids: List<number>): Promise<Iterable<LehrerStammdaten> | null> {
		if (ids.isEmpty()) {
			return null;
		}
		return await api.server.getLehrerStammdatenMultiple(ids, api.schema);
	}

	protected override async doPatch(partials: List<Partial<LehrerStammdaten>>): Promise<void> {
		// Der Server bietet derzeit keinen Endpunkt zum Patchen mehrerer Lehrer an. Daher wird kein Patch lokal übernommen,
		// der den Server nie erreicht.
		throw new DeveloperNotificationException("Das Patchen mehrerer Lehrer wird vom Server noch nicht unterstützt.");
	}

}
