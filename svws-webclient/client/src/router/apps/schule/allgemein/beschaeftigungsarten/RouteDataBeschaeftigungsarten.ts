import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { Beschaeftigungsart, List, SimpleOperationResponse } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ArrayList } from "@core";
import { ViewType, BeschaeftigungsartenListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeBeschaeftigungsartenDaten } from "~/router/apps/schule/allgemein/beschaeftigungsarten/RouteBeschaeftigungsartenDaten";
import { routeBeschaeftigungsartenGruppenprozesse } from "~/router/apps/schule/allgemein/beschaeftigungsarten/RouteBeschaeftigungsartenGruppenprozesse";
import { routeBeschaeftigungsartenNeu } from "~/router/apps/schule/allgemein/beschaeftigungsarten/RouteBeschaeftigungsartenNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new BeschaeftigungsartenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeBeschaeftigungsartenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
}

export class RouteDataBeschaeftigungsarten extends RouteDataAuswahl<BeschaeftigungsartenListeManager, RouteStateAuswahlInterface<BeschaeftigungsartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeBeschaeftigungsartenGruppenprozesse, hinzufuegen: routeBeschaeftigungsartenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<BeschaeftigungsartenListeManager>>> {
		const beschaeftigungsarten = await api.server.getBeschaeftigungsarten(api.schema);
		const manager = new BeschaeftigungsartenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, beschaeftigungsarten);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: any): Promise<Beschaeftigungsart> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchBeschaeftigungsart(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteBeschaeftigungsarten(ids, api.schema);
	}

	protected deleteMessage(id: number, beschaeftigungsart: Beschaeftigungsart | null): string {
		return `Beschäftigungsart ${beschaeftigungsart?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	addBeschaeftigungsart = async (data: Partial<Beschaeftigungsart>) : Promise<void> => {
		const result = await api.server.addBeschaeftigungsart(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	}
}
