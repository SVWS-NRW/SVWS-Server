import { ArrayList, JahrgangListeManager, type JahrgangsDaten, type List, type SimpleOperationResponse} from "@core";

import { routeSchuleJahrgaengeDaten } from "./RouteSchuleJahrgaengeDaten";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeSchuleJahrgangNeu } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgangNeu";
import { routeSchuleJahrgangGruppenprozesse } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgangGruppenprozesse";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type {RouteParamsRawGeneric} from "vue-router";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new JahrgangListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeSchuleJahrgaengeDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};


export class RouteDataSchuleJahrgaenge extends RouteDataAuswahl<JahrgangListeManager, RouteStateAuswahlInterface<JahrgangListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeSchuleJahrgangGruppenprozesse, hinzufuegen: routeSchuleJahrgangNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_ : number) : Promise<Partial<RouteStateAuswahlInterface<JahrgangListeManager>>> {
		const jahrgaenge = await api.server.getJahrgaenge(api.schema);
		const manager = new JahrgangListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, jahrgaenge);

		return { manager };
	}

	ladeDaten(auswahl: any): Promise<any> {
		return auswahl;
	}

	protected async doPatch(data : Partial<JahrgangsDaten>, id: number) : Promise<void> {
		await api.server.patchJahrgang(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		//  TODO: anpassen auf SimpleOperationResponse
		// return await api.server.deleteJahrgaenge(ids, api.schema);
		return new ArrayList();
	}

	add = async (data: Partial<JahrgangsDaten>): Promise<void> => {
		// Muss implementiert werden
	}

	protected deleteMessage(id: number, jahrgang: JahrgangsDaten | null) : string {
		return `Jahrgang ${jahrgang?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

}
