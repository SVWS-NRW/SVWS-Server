import type { FachDaten, List, SimpleOperationResponse } from "@core";
import { ArrayList, FachListeManager } from "@core";

import { api } from "~/router/Api";

import { routeSchuleFachDaten } from "./RouteSchuleFachDaten";
import { routeSchuleFachGruppenprozesse } from "./RouteSchuleFachGruppenprozesse";
import { routeSchuleFachNeu } from "./RouteSchuleFachNeu";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { ViewType } from "@ui";

const defaultState = <RouteStateAuswahlInterface<FachListeManager>> {
	idSchuljahresabschnitt: -1,
	manager: new FachListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeSchuleFachDaten,
	oldView: undefined,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataSchuleFaecher extends RouteDataAuswahl<FachListeManager, RouteStateAuswahlInterface<FachListeManager>> {

	public constructor() {
		super(defaultState, routeSchuleFachGruppenprozesse, routeSchuleFachNeu);
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt : number) : Promise<Partial<RouteStateAuswahlInterface<FachListeManager>>> {
		const listKatalogeintraege = await api.server.getFaecher(api.schema);
		const manager = new FachListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,	api.schulform, listKatalogeintraege);
		manager.setFilterAuswahlPermitted(true);

		return { manager }
	}

	public async ladeDaten(auswahl: FachDaten | null) : Promise<FachDaten | null> {
		return auswahl;
	}

	protected async doPatch(data : Partial<FachDaten>, id: number) : Promise<void> {
		await api.server.patchFach(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		// TODO: Implementierung der multipleDelete Methode für Fächer
		return new ArrayList();
	}

	protected deleteMessage(id: number, fach: FachDaten | null) : string {
		return `Fach ${fach?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	setzeDefaultSortierungSekII = async () => {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahlId = this.manager.auswahl().id;
		await api.server.setFaecherSortierungSekII(api.schema);
		await this.setSchuljahresabschnitt(idSchuljahresabschnitt, true);
		await this.setDaten(this.manager.liste.get(auswahlId));
	}

	add = async (data: Partial<FachDaten>): Promise<void> => {
		const fachDaten = await api.server.addFach(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(fachDaten.id);
	}
}
