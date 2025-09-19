import type { List, SimpleOperationResponse , Lernplattform} from "@core";
import { ArrayList,  } from "@core";

import { api } from "~/router/Api";

import { ViewType, LernplattformListeManager } from "@ui";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeKatalogLernplattformenDaten } from "~/router/apps/schule/schulbezogen/lernplattformen/RouteKatalogLernplattformenDaten";
import { routeKatalogLernplattformenGruppenprozesse } from "~/router/apps/schule/schulbezogen/lernplattformen/RouteKatalogLernplattformenGruppenprozesse";
import { routeKatalogLernplattformenNeu } from "~/router/apps/schule/schulbezogen/lernplattformen/RouteKatalogLernplattformenNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new LernplattformListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeKatalogLernplattformenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataKatalogLernplattformen extends RouteDataAuswahl<LernplattformListeManager, RouteStateAuswahlInterface<LernplattformListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeKatalogLernplattformenGruppenprozesse, hinzufuegen: routeKatalogLernplattformenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_ : number) : Promise<Partial<RouteStateAuswahlInterface<LernplattformListeManager>>> {
		const lernplattformen = await api.server.getLernplattformen(api.schema);
		const manager = new LernplattformListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, lernplattformen);
		return { manager };
	}

	async ladeDaten(auswahl: Lernplattform | null): Promise<Lernplattform | null> {
		if (auswahl === null)
			return auswahl;
		const lernplattform = await api.server.getLernplattform(api.schema, auswahl.id);
		this.manager.getIdByEintrag(lernplattform);
		return auswahl;
	}

	protected async doPatch(data : Partial<Lernplattform>, id: number) : Promise<void> {
		await api.server.patchLernplattform(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteLernplattformen(ids, api.schema);
	}

	add = async (data: Partial<Lernplattform>): Promise<void> => {
		const res = await api.server.addLernplattform(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(res.id);
	}

	protected deleteMessage(id: number, lernplattform: Lernplattform | null) : string {
		return `Lernplattform ${lernplattform?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}
}
