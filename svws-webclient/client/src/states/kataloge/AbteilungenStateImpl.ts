import type { WatchHandle } from "vue";
import { watch } from "vue";

import type { Abteilung } from "@core/core/data/schule/Abteilung";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { useAbschnittState } from "@ui/states/AbschnittState";
import type { AbteilungenState } from "@ui/states/kataloge/AbteilungenState";
import type { KatalogState } from "@ui/states/kataloge/KatalogState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface AbteilungenReactiveState {
	abteilungen: List<Abteilung>;
	abteilungenById: Map<number, Abteilung>;
	abteilungsleitungen: Set<number>;
}

const KATALOG_LABEL = "Abteilungen";

/** Implementierung des States für den Abteilungenkatalog */
export class AbteilungenStateImpl extends StateManager<AbteilungenReactiveState> implements AbteilungenState {

	private readonly _abteilungen: KatalogState<Abteilung>;
	private _abschnittWatcher: WatchHandle | null = null;

	public constructor() {
		super({
			abteilungen: new ArrayList(),
			abteilungenById: new Map(),
			abteilungsleitungen: new Set(),
		});
		this._abteilungen = createKatalogState({
			katalogLabel: KATALOG_LABEL,
			getList: () => this.state.abteilungen,
			getById: () => this.state.abteilungenById,
			updateState: (list, byId) => this.setPatchedState({ abteilungen: list, abteilungenById: byId, abteilungsleitungen: this.berechneAbteilungsleitungen(list) }),
			updateDefaultState: (list, byId) => this.setPatchedDefaultState({ abteilungen: list, abteilungenById: byId, abteilungsleitungen: this.berechneAbteilungsleitungen(list) }),
			apiGet: () => api.server.getAbteilungenByIdJahresAbschnitt(api.schema, useAbschnittState().auswahl.id),
			apiAdd: (data: Partial<Abteilung>) => api.server.addAbteilung(data, api.schema, data.idSchuljahresabschnitt ?? -1),
			apiPatch: (id, data) => api.server.patchAbteilung(data, api.schema, id),
			apiDelete: async (id) => {
				const ids = new ArrayList<number>();
				ids.add(id);
				await api.server.deleteAbteilungen(ids, api.schema);
			},
		});
	}

	override reset() {
		super.reset();
		this._abschnittWatcher?.();
	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.abteilungen.update(true);
			this._abschnittWatcher ??= watch(() => useAbschnittState().auswahl.id, () => this.abteilungen.update(false));
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get abteilungen(): KatalogState<Abteilung> {
		return this._abteilungen;
	}

	/** Die IDs der Lehrkräfte, die Abteilungsleitung einer der Abteilungen des gewählten Schuljahresabschnitts sind */
	public get abteilungsleitungen(): Set<number> {
		return this.state.abteilungsleitungen;
	}

	/**
	 * Bestimmt die IDs aller Abteilungsleitungen der übergebenen Abteilungen. Es wird jeweils eine neue Set erzeugt,
	 * damit der State per Patch (neue Referenz) aktualisiert wird und die Abteilungsleitungen zur Liste passen.
	 *
	 * @param abteilungen   die Abteilungen
	 *
	 * @returns die IDs der Abteilungsleitungen
	 */
	private berechneAbteilungsleitungen(abteilungen: List<Abteilung>): Set<number> {
		const abteilungsleitungen = new Set<number>();
		for (const eintrag of abteilungen) {
			if (eintrag.idAbteilungsleiter !== null) {
				abteilungsleitungen.add(eintrag.idAbteilungsleiter);
			}
		}
		return abteilungsleitungen;
	}

}

export const abteilungenStateImpl = new AbteilungenStateImpl();

