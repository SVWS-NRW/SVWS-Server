import type { SchuelerTelefon } from "@core/core/data/schueler/SchuelerTelefon";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { useBenutzerState } from "@ui/states/BenutzerState";

import { GenericDetailListStateImpl } from "../GenericDetailListStateImpl";
import { api } from "~/router/Api";

/**
 * Der State für die Telefoneinträge eines Schülers
 */
export class SchuelerTelefonState extends GenericDetailListStateImpl<SchuelerTelefon> {

	public constructor() {
		super(telefon => telefon.id);
	}

	/**
	 * Lädt die Telefoneinträge, sofern eine Kompetenz zum Ansehen der Individualdaten vorliegt.
	 *
	 * @param idAuswahl   die ID des Schülers
	 *
	 * @returns die Telefoneinträge
	 */
	protected async loadEntries(idAuswahl: number): Promise<List<SchuelerTelefon>> {
		return useBenutzerState().benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN)
			? await api.server.getSchuelerTelefone(api.schema, idAuswahl)
			: new ArrayList<SchuelerTelefon>();
	}

	/**
	 * Fügt einen neuen Telefoneintrag zu dem Schüler hinzu.
	 *
	 * @param data        die Daten des Telefoneintrags
	 * @param idSchueler  die ID des Schülers
	 */
	public async add(data: Partial<SchuelerTelefon>, idSchueler: number): Promise<void> {
		this.applyAdd(await api.server.addSchuelerTelefon(data, api.schema, idSchueler));
	}

	/**
	 * Passt einen Telefoneintrag an.
	 *
	 * @param data        die Änderungen
	 * @param idEintrag   die ID des Telefoneintrags
	 *
	 * @returns true, wenn die Änderung durchgeführt wurde
	 */
	public async patch(data: Partial<SchuelerTelefon>, idEintrag: number): Promise<boolean> {
		await api.server.patchSchuelerTelefon(data, api.schema, idEintrag);
		this.applyPatch(idEintrag, data);
		return true;
	}

	/**
	 * Entfernt die Telefoneinträge.
	 *
	 * @param idsEintraege   die IDs der Telefoneinträge
	 */
	public async remove(idsEintraege: List<number>): Promise<void> {
		await api.server.deleteSchuelerTelefone(idsEintraege, api.schema);
		this.applyRemove(idsEintraege);
	}

}
