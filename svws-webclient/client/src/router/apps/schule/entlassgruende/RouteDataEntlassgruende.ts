import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { List, SimpleOperationResponse, KatalogEntlassgrund } from "@core";
import { ArrayList, EntlassgruendeListeManager } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeEntlassgruendeDaten } from "~/router/apps/schule/entlassgruende/RouteEntlassgruendeDaten";
import { routeEntlassgruendeGruppenprozesse } from "~/router/apps/schule/entlassgruende/RouteEntlassgruendeGruppenprozesse";
import { routeEntlassgruendeNeu } from "~/router/apps/schule/entlassgruende/RouteEntlassgruendeNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new EntlassgruendeListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeEntlassgruendeDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
}

export class RouteDataEntlassgruende extends RouteDataAuswahl<EntlassgruendeListeManager, RouteStateAuswahlInterface<EntlassgruendeListeManager>> {

	public constructor() {
		super(defaultState, routeEntlassgruendeGruppenprozesse, routeEntlassgruendeNeu);
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<EntlassgruendeListeManager>>> {
		const entlassgruende = await api.server.getEntlassgruende(api.schema);
		const manager = new EntlassgruendeListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, entlassgruende);
		return { manager }
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: any): Promise<KatalogEntlassgrund> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchEntlassgrund(data, api.schema, id);
	}

	protected doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		// TODO: implement
		throw new Error("Method not implemented.");
	}

	protected deleteMessage(id: number, eintrag: any): string {
		// TODO: implement
		throw new Error("Method not implemented.");
	}

}

