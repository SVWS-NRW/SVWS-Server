import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { useAbschnittState } from "@ui/states/AbschnittState";

import type { GenericAuswahlHost } from "../GenericAuswahlState";
import { GenericDatenStateImpl } from "../GenericDatenStateImpl";
import { api } from "~/router/Api";

import { SchuelerTelefonState } from "./SchuelerTelefonState";

/**
 * Der State für die Stammdaten der Schüler
 */
export class SchuelerStammdatenState extends GenericDatenStateImpl<SchuelerListeEintrag, SchuelerStammdaten> {

	/** Der State für die Telefoneinträge des Schülers */
	public readonly telefon = new SchuelerTelefonState();

	/**
	 * Erzeugt einen neuen State für die Stammdaten der Schüler.
	 *
	 * @param auswahl   der Auswahl-State, an den sich der State anhängt
	 */
	public constructor(auswahl: GenericAuswahlHost<SchuelerListeEintrag>) {
		super(daten => daten.id, auswahl);
		this.registerChild(this.telefon);
	}

	/**
	 * Gibt den Schuljahresabschnitt aus den Stammdaten des Schülers der Einzelauswahl zurück.
	 *
	 * @returns der Schuljahresabschnitt oder null, falls in den Stammdaten keiner gesetzt ist
	 *
	 * @throws DeveloperNotificationException   wenn keine Daten der Einzelauswahl vorliegen oder der Schuljahresabschnitt fehlt
	 */
	public get schuljahresabschnitt(): Schuljahresabschnitt | null {
		const id = this.datenOrException().idSchuljahresabschnitt;
		return (id === null) ? null : useAbschnittState().get(id);
	}

	public async ladeDaten(id: number): Promise<SchuelerStammdaten> {
		return await api.server.getSchuelerStammdaten(api.schema, id);
	}

	protected async doPatch(data: Partial<SchuelerStammdaten>, id: number): Promise<boolean> {
		await api.server.patchSchuelerStammdaten(data, api.schema, id);
		return true;
	}

	protected override uebernehmeInEintrag(eintrag: SchuelerListeEintrag, daten: SchuelerStammdaten): void {
		if (eintrag.vorname !== daten.vorname) {
			eintrag.vorname = daten.vorname;
		}
		if (eintrag.nachname !== daten.nachname) {
			eintrag.nachname = daten.nachname;
		}
		if (eintrag.status !== daten.status) {
			eintrag.status = daten.status;
		}
	}

}
