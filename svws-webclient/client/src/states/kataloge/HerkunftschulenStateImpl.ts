import { Schulform } from "@core/asd/types/schule/Schulform";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { HerkunftSchuleKatalogState, HerkunftschulenState } from "@ui/states/kataloge/HerkunftschulenState";
import { createKatalogState } from "@ui/states/kataloge/katalogStateUtils";
import { useSchuleState } from "@ui/states/SchuleState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface HerkunftschulenReactiveState {
	schulen: List<SchulEintrag>;
	schulenById: Map<number, SchulEintrag>;
	bySchulnummerStatistikFilteredByEigeneSchulform: Map<string, SchulEintrag>,
}

const KATALOG_LABEL = "Schulen";

/** Implementierung des States für den Schulenkatalog */
export class HerkunftschulenStateImpl extends StateManager<HerkunftschulenReactiveState> implements HerkunftschulenState {

	private readonly _herkunftschulen: HerkunftSchuleKatalogState;

	public constructor() {
		super({
			schulen: new ArrayList(),
			schulenById: new Map(),
			// Die Map enthält nur Schulen der eigenen Schulform
			bySchulnummerStatistikFilteredByEigeneSchulform: new Map(),
		});

		this._herkunftschulen = {
			...createKatalogState({
				katalogLabel: KATALOG_LABEL,
				getList: () => this.state.schulen,
				getById: () => this.state.schulenById,
				updateState: (list, byId) => this.setPatchedState({ schulen: list, schulenById: byId }),
				updateDefaultState: (list, byId) => this.setPatchedDefaultState({ schulen: list, schulenById: byId }),
				apiGet: () => api.server.getSchulen(api.schema),
				apiAdd: (data) => api.server.addSchuleZuKatalog(data, api.schema),
				apiPatch: (id, data) => api.server.patchSchuleAusKatalog(data, api.schema, id),
				apiDelete: async (id) => {
					const ids = new ArrayList<number>();
					ids.add(id);
					await api.server.deleteSchulenVonKatalog(ids, api.schema);
				},
			}),
			bySchulnummerStatistikFilteredByEigeneSchulform: this.state.bySchulnummerStatistikFilteredByEigeneSchulform,
		};

	}

	/** Initialisiert den State und lädt Daten der Kataloge */
	public async init(): Promise<void> {
		try {
			await this.herkunftschulen.update(true);
			this.initBySchulnummerStatikstikFilteredByEigeneSchuleSchulform();
		} catch {
			this.reset();
			throw new DeveloperNotificationException(
				`Das Laden des Katalogs '${KATALOG_LABEL}' ist fehlgeschlagen.`
			);
		}
	}

	public get herkunftschulen(): HerkunftSchuleKatalogState {
		return this._herkunftschulen;
	}

	private initBySchulnummerStatikstikFilteredByEigeneSchuleSchulform() {
		this.state.bySchulnummerStatistikFilteredByEigeneSchulform.clear();
		const schuleState = useSchuleState();

		for (const schule of this.state.schulen) {
			if (schule.schulnummerStatistik === null) {
				continue;
			}

			const sfEintrag =
				schule.idSchulform === null
					? null
					: Schulform.data().getEintragByID(schule.idSchulform);

			const sf =
				sfEintrag === null
					? null
					: Schulform.data().getWertBySchluessel(sfEintrag.schluessel);
			if (sf === schuleState.schulform) {
				this.state.bySchulnummerStatistikFilteredByEigeneSchulform.set(schule.schulnummerStatistik, schule);
			}
		}

	}
}

export const herkunftschulenStateImpl = new HerkunftschulenStateImpl();
