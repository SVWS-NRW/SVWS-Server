import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { Floskelgruppe, List, SimpleOperationResponse } from "@core";
import { ArrayList } from "@core";
import { ViewType, FloskelgruppenListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeFloskelgruppenDaten } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppenDaten";
import { routeFloskelgruppenGruppenprozesse } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppenGruppenprozesse";
import { routeFloskelgruppenNeu } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppenNeu";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new FloskelgruppenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeFloskelgruppenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataFloskelgruppen extends RouteDataAuswahl<FloskelgruppenListeManager, RouteStateAuswahlInterface<FloskelgruppenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeFloskelgruppenGruppenprozesse, hinzufuegen: routeFloskelgruppenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<FloskelgruppenListeManager>>> {
		const floskelgruppen = await api.server.getFloskelgruppen(api.schema);
		const manager = new FloskelgruppenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, floskelgruppen);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: any): Promise<Floskelgruppe> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchFloskelgruppe(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return api.server.deleteFloskelgruppen(ids, api.schema);
	}

	protected deleteMessage(id: number, floskelgruppe: Floskelgruppe | null): string {
		return `Die Floskelgruppe ${floskelgruppe?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	add = async (data: Partial<Floskelgruppe>): Promise<void> => {
		const result = await api.server.addFloskelgruppe(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

}
