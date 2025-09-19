import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { List, SimpleOperationResponse, Merkmal } from "@core";
import { MerkmaleListeManager } from "@ui";
import { ArrayList } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeMerkmaleDaten } from "~/router/apps/schule/schulbezogen/merkmale/RouteMerkmaleDaten";
import { routeMerkmaleGruppenprozesse } from "~/router/apps/schule/schulbezogen/merkmale/RouteMerkmaleGruppenprozesse";
import { routeMerkmaleNeu } from "~/router/apps/schule/schulbezogen/merkmale/RouteMerkmaleNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new MerkmaleListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeMerkmaleDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
}

export class RouteDataMerkmale extends RouteDataAuswahl<MerkmaleListeManager, RouteStateAuswahlInterface<MerkmaleListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeMerkmaleGruppenprozesse, hinzufuegen: routeMerkmaleNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<MerkmaleListeManager>>> {
		const merkmale = await api.server.getMerkmale(api.schema);
		const manager = new MerkmaleListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, merkmale);
		return { manager }
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: any): Promise<Merkmal> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchMerkmal(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteMerkmale(ids, api.schema);
	}

	protected deleteMessage(id: number, merkmal: Merkmal | null): string {
		return `Merkmal ${merkmal?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	addMerkmal = async (data: Partial<Merkmal>) : Promise<void> => {
		const result = await api.server.addMerkmal(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	}

}

