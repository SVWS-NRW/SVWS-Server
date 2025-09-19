import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { List, SimpleOperationResponse , FoerderschwerpunktEintrag } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ArrayList } from "@core";
import { ViewType, FoerderschwerpunkteListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeFoerderschwerpunkteDaten } from "~/router/apps/schule/schulbezogen/foerderschwerpunkte/RouteFoerderschwerpunkteDaten";
import { routeFoerderschwerpunkteGruppenprozesse } from "~/router/apps/schule/schulbezogen/foerderschwerpunkte/RouteFoerderschwerpunkteGruppenprozesse";
import { routeFoerderschwerpunkteNeu } from "~/router/apps/schule/schulbezogen/foerderschwerpunkte/RouteFoerderschwerpunkteNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new FoerderschwerpunkteListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeFoerderschwerpunkteDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
}

export class RouteDataFoerderschwerpunkte extends RouteDataAuswahl<FoerderschwerpunkteListeManager, RouteStateAuswahlInterface<FoerderschwerpunkteListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeFoerderschwerpunkteGruppenprozesse, hinzufuegen: routeFoerderschwerpunkteNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<FoerderschwerpunkteListeManager>>> {
		const foerderschwerpunkte = await api.server.getKatalogFoerderschwerpunkte(api.schema);
		const manager = new FoerderschwerpunkteListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, foerderschwerpunkte);
		return { manager }
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: any): Promise<FoerderschwerpunktEintrag> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchKatalogFoerderschwerpunkt(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return api.server.deleteKatalogFoerderschwerpunkte(ids, api.schema);
	}

	protected deleteMessage(id: number, foerderschwerpunkt: FoerderschwerpunktEintrag | null): string {
		return `Förderschwerpunkt ${foerderschwerpunkt?.text ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	addFoerderschwerpunkt = async (data: Partial<FoerderschwerpunktEintrag>) : Promise<void> => {
		const result = await api.server.addKatalogFoerderschwerpunkt(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	}

}
