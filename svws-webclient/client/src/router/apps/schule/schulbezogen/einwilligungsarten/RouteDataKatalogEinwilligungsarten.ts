import type { List, SimpleOperationResponse } from "@core";
import { ArrayList, type Einwilligungsart } from "@core";

import { api } from "~/router/Api";

import { routeKatalogEinwilligungsartenDaten } from "./RouteKatalogEinwilligungsartenDaten";

import { ViewType, EinwilligungsartenListeManager } from "@ui";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeKatalogEinwilligungsartenGruppenprozesse } from "~/router/apps/schule/schulbezogen/einwilligungsarten/RouteKatalogEinwilligungsartenGruppenprozesse";
import { routeKatalogEinwilligungsartenNeu } from "~/router/apps/schule/schulbezogen/einwilligungsarten/RouteKatalogEinwilligungsartenNeu";
import type {RouteParamsRawGeneric } from "vue-router";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new EinwilligungsartenListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList()),
	view: routeKatalogEinwilligungsartenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataKatalogEinwilligungsarten extends RouteDataAuswahl<EinwilligungsartenListeManager, RouteStateAuswahlInterface<EinwilligungsartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeKatalogEinwilligungsartenGruppenprozesse, hinzufuegen: routeKatalogEinwilligungsartenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_ : number) : Promise<Partial<RouteStateAuswahlInterface<EinwilligungsartenListeManager>>> {
		const einwilligungsarten = await api.server.getEinwilligungsarten(api.schema);
		const manager = new EinwilligungsartenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, einwilligungsarten, new ArrayList());
		return { manager };
	}

	async ladeDaten(auswahl: Einwilligungsart | null): Promise<Einwilligungsart | null> {
		if (auswahl === null)
			return auswahl;
		const schueler = await api.server.getSchuelerByEinwilligungsartID(api.schema, auswahl.id);
		this.manager.setListSchuelerEinwilligungsartenZusammenfassung(schueler);
		return auswahl;
	}

	protected async doPatch(data : Partial<Einwilligungsart>, id: number) : Promise<void> {
		await api.server.patchEinwilligungsart(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteEinwilligungsarten(ids, api.schema);
	}

	add = async (data: Partial<Einwilligungsart>): Promise<void> => {
		const res = await api.server.createEinwilligungsart(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(res.id);
	}

	protected deleteMessage(id: number, einwilligungsart: Einwilligungsart | null) : string {
		return `Einwilligungsart ${einwilligungsart?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}
}
