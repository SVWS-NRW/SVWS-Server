import type { List, SimpleOperationResponse , Erzieherart } from "@core";
import { ArrayList } from "@core";
import { api } from "~/router/Api";
import { ViewType, ErzieherartListeManager } from "@ui";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeKatalogErzieherartenDaten } from "~/router/apps/schule/allgemein/erzieherarten/RouteKatalogErzieherartenDaten";
import { routeKatalogErzieherartenGruppenprozesse } from "~/router/apps/schule/allgemein/erzieherarten/RouteKatalogErzieherartenGruppenprozesse";
import { routeKatalogErzieherartenNeu } from "~/router/apps/schule/allgemein/erzieherarten/RouteKatalogErzieherartenNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new ErzieherartListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeKatalogErzieherartenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataKatalogErzieherarten extends RouteDataAuswahl<ErzieherartListeManager, RouteStateAuswahlInterface<ErzieherartListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeKatalogErzieherartenGruppenprozesse, hinzufuegen: routeKatalogErzieherartenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_ : number) : Promise<Partial<RouteStateAuswahlInterface<ErzieherartListeManager>>> {
		const erzieherarten = await api.server.getErzieherArten(api.schema);
		const manager = new ErzieherartListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, erzieherarten);
		return { manager };
	}

	async ladeDaten(auswahl: Erzieherart | null): Promise<Erzieherart | null> {
		if (auswahl === null)
			return auswahl;
		const erzieherart = await api.server.getErzieherart(api.schema, auswahl.id);
		this.manager.getIdByEintrag(erzieherart);
		return auswahl;
	}

	protected async doPatch(data : Partial<Erzieherart>, id: number) : Promise<void> {
		await api.server.patchErzieherart(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteErzieherarten(ids, api.schema);
	}

	add = async (data: Partial<Erzieherart>): Promise<void> => {
		const res = await api.server.addErzieherart(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(res.id);
	}

	protected deleteMessage(id: number, erzieherart: Erzieherart | null) : string {
		return `Erzieherart ${erzieherart?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}
}
