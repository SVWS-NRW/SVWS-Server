import type { List, SchulEintrag, SimpleOperationResponse } from "@core";
import type { RouteParamsRawGeneric } from "vue-router";
import { ArrayList } from "@core";
import { api } from "~/router/Api";
import { routeKatalogSchuleDaten } from "./RouteKatalogSchuleDaten";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { ViewType, KatalogSchuleListeManager } from "@ui";
import { routeKatalogSchuleGruppenprozesse } from "~/router/apps/schule/allgemein/schulen/RouteKatalogSchuleGruppenprozesse";
import { routeKatalogSchuleNeu } from "~/router/apps/schule/allgemein/schulen/RouteKatalogSchuleNeu";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new KatalogSchuleListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList()),
	view: routeKatalogSchuleDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataKatalogSchulen extends RouteDataAuswahl<KatalogSchuleListeManager, RouteStateAuswahlInterface<KatalogSchuleListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeKatalogSchuleGruppenprozesse, hinzufuegen: routeKatalogSchuleNeu });
	}


	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_ : number) : Promise<Partial<RouteStateAuswahlInterface<KatalogSchuleListeManager>>> {
		const schulen = await api.server.getSchulen(api.schema);
		const katalogSchulen = await api.server.getKatalogSchulen(api.schema);
		const manager = new KatalogSchuleListeManager(
			api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, schulen, katalogSchulen);
		return { manager };
	}

	public ladeDaten(auswahl: any): Promise<any> {
		return auswahl;
	}

	protected async doPatch(data : Partial<SchulEintrag>, id: number) : Promise<void> {
		await api.server.patchSchuleAusKatalog(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		//  TODO: anpassen auf SimpleOperationResponse
		// return await api.server.deleteJahrgaenge(ids, api.schema);
		return new ArrayList();
	}

	add = async (data: Partial<SchulEintrag>): Promise<void> => {
		const schule = await api.server.addSchuleZuKatalog(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(schule.id);
	}

	protected deleteMessage(id: number, schule: SchulEintrag | null) : string {
		return `Schule ${schule?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}
}

