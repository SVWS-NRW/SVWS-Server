import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

import { GruppenprozessBereich, GruppenprozessBereichSchluessel } from "../GruppenprozessBereich";

/**
 * Der Bereich der Gruppenprozesse für die Daten der Kurse der Mehrfachauswahl. Die Daten eines Kurses sind
 * vollständig in der Auswahlliste enthalten, d.h. der Eintrag der Liste und die Daten sind dasselbe Objekt.
 */
export class KurseDatenBereich extends GruppenprozessBereich<KursDaten, KursDaten> {

	/** Der Schlüssel, unter dem der Bereich bei den Gruppenprozessen angemeldet ist */
	public static readonly SCHLUESSEL = new GruppenprozessBereichSchluessel<KurseDatenBereich>("Gruppenprozesse der Kurse: Daten");

	/**
	 * Erzeugt einen neuen Bereich für die Daten der Kurse der Mehrfachauswahl.
	 */
	public constructor() {
		super(daten => daten.id);
	}

	public get schluessel(): GruppenprozessBereichSchluessel<KurseDatenBereich> {
		return KurseDatenBereich.SCHLUESSEL;
	}

	public readonly bezeichnung = "Daten der Kurse";

	protected async ladeDatenMultiple(ids: List<number>): Promise<Iterable<KursDaten> | null> {
		// Die Daten sind vollständig in der Liste enthalten, kein Aufruf der API notwendig
		const eintraege = new ArrayList<KursDaten>();
		for (const id of ids) {
			eintraege.add(this.getEintragOrException(id));
		}
		return eintraege;
	}

}
