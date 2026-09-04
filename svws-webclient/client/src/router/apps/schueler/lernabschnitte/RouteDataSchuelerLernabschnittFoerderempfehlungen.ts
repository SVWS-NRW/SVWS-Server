import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeSchuelerLernabschnitte } from "./RouteSchuelerLernabschnitte";
import type { SchuelerFoerderempfehlung } from "@core/asd/data/schueler/SchuelerFoerderempfehlung";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";


interface RouteStateDataSchuelerLernabschnittFoerderempfehlungen extends RouteStateInterface {
	listFoerderempfehlungen: List<SchuelerFoerderempfehlung>;
}

const defaultState = <RouteStateDataSchuelerLernabschnittFoerderempfehlungen> {
	listFoerderempfehlungen: new ArrayList<SchuelerFoerderempfehlung>(),
};

export class RouteDataSchuelerLernabschnittFoerderempfehlungen extends RouteData<RouteStateDataSchuelerLernabschnittFoerderempfehlungen> {

	public constructor() {
		super(defaultState);
	}

	get listFoerderempfehlungen(): List<SchuelerFoerderempfehlung> {
		return this._state.value.listFoerderempfehlungen;
	}

	/**
	 * Lädt die Förderempfehlungen für den aktuellen Lernabschnitt
	 */
	public async ladeFoerderempfehlungen(): Promise<void> {
		if (!routeSchuelerLernabschnitte.data.hatAuswahl) {
			return;
		}
		const lernabschnittsDaten = routeSchuelerLernabschnitte.data.daten;
		const foerderempfehlungen = await api.server.getFoerderempfehlungenByLernabschnittsdatenID(api.schema, lernabschnittsDaten.id);
		this.setPatchedDefaultState({ listFoerderempfehlungen: foerderempfehlungen });
	}

	addFoerderempfehlung = async (payload: Partial<SchuelerFoerderempfehlung>): Promise<void> => {
		payload.idLernabschnitt = routeSchuelerLernabschnitte.data.daten.id;
		const created = await api.server.addFoerderempfehlung(payload, api.schema);
		this._state.value.listFoerderempfehlungen.add(created);
		this.commit();
	};

	patchFoerderempfehlung = async (data: Partial<SchuelerFoerderempfehlung>, guid: string): Promise<boolean> => {
		await api.server.patchFoerderempfehlung(data, api.schema, guid);
		for (const empfehlung of this._state.value.listFoerderempfehlungen) {
			if (empfehlung.guid === guid) {
				Object.assign(empfehlung, data);
				break;
			}
		}
		this.commit();
		return true;
	};

	deleteFoerderempfehlungen = async (guIDs: List<string>) => {
		for (const guid of guIDs) {
			await api.server.deleteFoerderempfehlung(api.schema, guid);
		};
		const tempListFoerderempfehlungen = new ArrayList<SchuelerFoerderempfehlung>();
		for (const empfehlung of this._state.value.listFoerderempfehlungen) {
			if (!guIDs.contains(empfehlung.guid)) {
				tempListFoerderempfehlungen.add(empfehlung);
			}
		}
		this.setPatchedDefaultState({ listFoerderempfehlungen: tempListFoerderempfehlungen });
	};
}
