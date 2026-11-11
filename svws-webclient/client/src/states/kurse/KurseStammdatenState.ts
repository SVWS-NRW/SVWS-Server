import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { KursLehrer } from "@core/asd/data/kurse/KursLehrer";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { List } from "@core/java/util/List";
import { useAbschnittState } from "@ui/states/AbschnittState";

import type { GenericAuswahlHost } from "../GenericAuswahlState";
import { GenericDatenStateImpl } from "../GenericDatenStateImpl";
import { api } from "~/router/Api";

/**
 * Der State für die Stammdaten der Kurse. Die Stammdaten eines Kurses sind vollständig in der Auswahlliste
 * enthalten, d.h. der Eintrag der Liste und die Stammdaten sind dasselbe Objekt.
 */
export class KurseStammdatenState extends GenericDatenStateImpl<KursDaten, KursDaten> {

	/**
	 * Erzeugt einen neuen State für die Stammdaten der Kurse.
	 *
	 * @param auswahl   der Auswahl-State, an den sich der State anhängt
	 */
	public constructor(auswahl: GenericAuswahlHost<KursDaten>) {
		super(daten => daten.id, auswahl);
	}

	/**
	 * Gibt den Schuljahresabschnitt des Kurses der Einzelauswahl zurück. Dieser kann vom Schuljahresabschnitt der Auswahlliste abweichen.
	 *
	 * @returns der Schuljahresabschnitt
	 *
	 * @throws DeveloperNotificationException   wenn keine Daten der Einzelauswahl vorliegen oder der Schuljahresabschnitt fehlt
	 */
	public get schuljahresabschnitt(): Schuljahresabschnitt {
		return useAbschnittState().get(this.datenOrException().idSchuljahresabschnitt);
	}

	public async ladeDaten(id: number): Promise<KursDaten> {
		// Die Daten sind vollständig in der Liste enthalten, kein Aufruf der API notwendig
		return this.getEintragOrException(id);
	}

	protected async doPatch(data: Partial<KursDaten>, id: number): Promise<boolean> {
		await api.server.patchKurs(data, api.schema, id);
		return true;
	}

	protected override uebernehmeInEintrag(eintrag: KursDaten, daten: KursDaten): void {
		if (eintrag.kuerzel !== daten.kuerzel) {
			eintrag.kuerzel = daten.kuerzel;
		}
	}


	/**
	 * Fügt dem Kurs einen weiteren Lehrer hinzu.
	 *
	 * @param data     die Daten des Lehrers beim Kurs
	 * @param idKurs   die ID des Kurses
	 */
	public async addLehrer(data: Partial<KursLehrer>, idKurs: number): Promise<void> {
		api.status.start();
		const result = await api.server.addKursLehrer(data, api.schema, idKurs);
		this.datenOrException().weitereLehrer.add(result);
		this.commit();
		api.status.stop();
	}

	/**
	 * Passt die Daten eines weiteren Lehrers beim Kurs an.
	 *
	 * @param data       die Änderungen
	 * @param idKurs     die ID des Kurses
	 * @param idLehrer   die ID des Lehrers
	 */
	public async patchLehrer(data: Partial<KursLehrer>, idKurs: number, idLehrer: number): Promise<void> {
		api.status.start();
		await api.server.patchKursLehrer(data, api.schema, idKurs, idLehrer);
		for (const k of this.datenOrException().weitereLehrer) {
			if (k.idLehrer === idLehrer) {
				Object.assign(k, data);
			}
		}
		this.commit();
		api.status.stop();
	}

	/**
	 * Entfernt weitere Lehrer vom Kurs.
	 *
	 * @param lehrerIds   die IDs der Lehrer
	 * @param idKurs      die ID des Kurses
	 */
	public async removeLehrer(lehrerIds: List<number>, idKurs: number): Promise<void> {
		await api.server.deleteKursLehrer(lehrerIds, api.schema, idKurs);
		const weitereLehrer = this.datenOrException().weitereLehrer;
		for (let i = weitereLehrer.size() - 1; i >= 0; i--) {
			if (lehrerIds.contains(weitereLehrer.get(i).idLehrer)) {
				weitereLehrer.removeElementAt(i);
			}
		}
		this.commit();
	}
}
