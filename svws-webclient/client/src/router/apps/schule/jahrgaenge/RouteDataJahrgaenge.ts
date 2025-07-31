import { ArrayList, type JahrgangsDaten, type List, type SimpleOperationResponse} from "@core";
import { JahrgaengeListeManager } from "@ui";

import { routeJahrgaengeDaten } from "./RouteJahrgaengeDaten";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeJahrgaengeNeu } from "~/router/apps/schule/jahrgaenge/RouteJahrgaengeNeu";
import { routeJahrgaengeGruppenprozesse } from "~/router/apps/schule/jahrgaenge/RouteJahrgaengeGruppenprozesse";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type {RouteParamsRawGeneric} from "vue-router";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new JahrgaengeListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeJahrgaengeDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};


export class RouteDataJahrgaenge extends RouteDataAuswahl<JahrgaengeListeManager, RouteStateAuswahlInterface<JahrgaengeListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeJahrgaengeGruppenprozesse, hinzufuegen: routeJahrgaengeNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_ : number) : Promise<Partial<RouteStateAuswahlInterface<JahrgaengeListeManager>>> {
		const jahrgaenge = await api.server.getJahrgangsdaten(api.schema);
		const manager = new JahrgaengeListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, jahrgaenge);

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
