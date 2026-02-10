import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { Floskel, List, SimpleOperationResponse } from "@core";
import { ArrayList } from "@core";
import { ViewType, FloskelnListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeFloskelnDaten } from "~/router/apps/schule/kataloge/floskeln/RouteFloskelnDaten";
import { routeFloskelnGruppenprozesse } from "~/router/apps/schule/kataloge/floskeln/RouteFloskelnGruppenprozesse";
import { routeFloskelnNeu } from "~/router/apps/schule/kataloge/floskeln/RouteFloskelnNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new FloskelnListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList()),
	view: routeFloskelnDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataFloskeln extends RouteDataAuswahl<FloskelnListeManager, RouteStateAuswahlInterface<FloskelnListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeFloskelnGruppenprozesse, hinzufuegen: routeFloskelnNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<FloskelnListeManager>>> {
		const floskeln = await api.server.getFloskeln(api.schema);
		const floskelgruppen = await api.server.getFloskelgruppen(api.schema);
		const manager = new FloskelnListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, floskeln, floskelgruppen);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: any): Promise<Floskel> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchFloskeln(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return api.server.deleteFloskeln(ids, api.schema);
	}

	protected deleteMessage(id: number, floskel: Floskel | null): string {
		return `Die Floskel ${floskel?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	add = async (data: Partial<Floskel>): Promise<void> => {
		const result = await api.server.addFloskel(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

}
