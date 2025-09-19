import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { List, SimpleOperationResponse, Kindergarten } from "@core";
import { ArrayList } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ViewType, KindergaertenListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeKindergaertenGruppenprozesse } from "~/router/apps/schule/allgemein/kindergaerten/RouteKindergaertenGruppenprozesse";
import { routeKindergaertenNeu } from "~/router/apps/schule/allgemein/kindergaerten/RouteKindergaertenNeu";
import { routeKindergaertenDaten } from "~/router/apps/schule/allgemein/kindergaerten/RouteKindergaertenDaten";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new KindergaertenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeKindergaertenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
}

export class RouteDataKindergaerten extends RouteDataAuswahl<KindergaertenListeManager, RouteStateAuswahlInterface<KindergaertenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeKindergaertenGruppenprozesse, hinzufuegen: routeKindergaertenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<KindergaertenListeManager>>> {
		const kindergaerten = await api.server.getKindergaerten(api.schema);
		const manager = new KindergaertenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, kindergaerten);
		return { manager }
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: any): Promise<Kindergarten> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchKindergarten(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteKindergaerten(ids, api.schema);
	}

	protected deleteMessage(id: number, kindergarten: Kindergarten | null): string {
		return `Kindergarten ${kindergarten?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	addKindergarten = async (data: Partial<Kindergarten>) : Promise<void> => {
		const result = await api.server.addKindergarten(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	}

}
