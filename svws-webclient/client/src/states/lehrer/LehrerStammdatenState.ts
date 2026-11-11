import type { LehrerStammdaten } from "@core/asd/data/lehrer/LehrerStammdaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";

import type { GenericAuswahlHost } from "../GenericAuswahlState";
import { GenericDatenStateImpl } from "../GenericDatenStateImpl";
import { api } from "~/router/Api";

import { LehrerLeitungsfunktionenState } from "./LehrerLeitungsfunktionenState";

/**
 * Der State für die Stammdaten der Lehrer
 */
export class LehrerStammdatenState extends GenericDatenStateImpl<LehrerListeEintrag, LehrerStammdaten> {

	/** Der State für die Leitungsfunktionen des Lehrers */
	public readonly leitungsfunktionen = new LehrerLeitungsfunktionenState();

	/**
	 * Erzeugt einen neuen State für die Stammdaten der Lehrer.
	 *
	 * @param auswahl   der Auswahl-State, an den sich der State anhängt
	 */
	public constructor(auswahl: GenericAuswahlHost<LehrerListeEintrag>) {
		super(daten => daten.id, auswahl);
		this.registerChild(this.leitungsfunktionen);
	}

	public async ladeDaten(id: number): Promise<LehrerStammdaten> {
		return await api.server.getLehrerStammdaten(api.schema, id);
	}

	protected async doPatch(data: Partial<LehrerStammdaten>, id: number): Promise<boolean> {
		await api.server.patchLehrerStammdaten(data, api.schema, id);
		return true;
	}

	protected override uebernehmeInEintrag(eintrag: LehrerListeEintrag, daten: LehrerStammdaten): void {
		if (eintrag.kuerzel !== daten.kuerzel) {
			eintrag.kuerzel = daten.kuerzel;
		}
		if (eintrag.nachname !== daten.nachname) {
			eintrag.nachname = daten.nachname;
		}
		if (eintrag.vorname !== daten.vorname) {
			eintrag.vorname = daten.vorname;
		}
		if (eintrag.istSichtbar !== daten.istSichtbar) {
			eintrag.istSichtbar = daten.istSichtbar;
		}
		if (eintrag.istRelevantFuerStatistik !== daten.istRelevantFuerStatistik) {
			eintrag.istRelevantFuerStatistik = daten.istRelevantFuerStatistik;
		}
	}

}
