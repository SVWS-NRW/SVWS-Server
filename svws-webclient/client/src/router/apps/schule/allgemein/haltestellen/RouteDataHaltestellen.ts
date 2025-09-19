import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { Haltestelle, List, SimpleOperationResponse } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ArrayList } from "@core";
import { ViewType, HaltestellenListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeHaltestellenDaten } from "~/router/apps/schule/allgemein/haltestellen/RouteHaltestellenDaten";
import { routeHaltestellenGruppenprozesse } from "~/router/apps/schule/allgemein/haltestellen/RouteHaltestellenGruppenprozesse";
import { routeHaltestellenNeu } from "~/router/apps/schule/allgemein/haltestellen/RouteHaltestellenNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new HaltestellenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeHaltestellenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
}

export class RouteDataHaltestellen extends RouteDataAuswahl<HaltestellenListeManager, RouteStateAuswahlInterface<HaltestellenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeHaltestellenGruppenprozesse, hinzufuegen: routeHaltestellenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<HaltestellenListeManager>>> {
		const haltestellen = await api.server.getHaltestellen(api.schema);
		const manager = new HaltestellenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, haltestellen);
		return { manager }
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: any): Promise<Haltestelle> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchHaltestelle(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteHaltestellen(ids, api.schema);
	}

	protected deleteMessage(id: number, haltestelle: Haltestelle | null): string {
		return `Haltestelle ${haltestelle?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	addHaltestelle = async (data: Partial<Haltestelle>) : Promise<void> => {
		const result = await api.server.addHaltestelle(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	}
}

