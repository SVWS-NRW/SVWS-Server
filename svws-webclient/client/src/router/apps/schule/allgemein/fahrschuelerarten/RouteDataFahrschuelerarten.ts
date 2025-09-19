import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { Fahrschuelerart, List, SimpleOperationResponse } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ArrayList } from "@core";
import { ViewType, FahrschuelerartenListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeFahrschuelerartenGruppenprozesse } from "~/router/apps/schule/allgemein/fahrschuelerarten/RouteFahrschuelerartenGruppenprozesse";
import { routeFahrschuelerartenNeu } from "~/router/apps/schule/allgemein/fahrschuelerarten/RouteFahrschuelerartenNeu";
import { routeFahrschuelerartenDaten } from "~/router/apps/schule/allgemein/fahrschuelerarten/RouteFahrschuelerartenDaten";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new FahrschuelerartenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeFahrschuelerartenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
}

export class RouteDataFahrschuelerarten extends RouteDataAuswahl<FahrschuelerartenListeManager, RouteStateAuswahlInterface<FahrschuelerartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeFahrschuelerartenGruppenprozesse, hinzufuegen: routeFahrschuelerartenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<FahrschuelerartenListeManager>>> {
		const fahrschuelerarten = await api.server.getFahrschuelerarten(api.schema);
		const manager = new FahrschuelerartenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, fahrschuelerarten);
		return { manager }
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: any): Promise<Fahrschuelerart> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchFahrschuelerart(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteFahrschuelerarten(ids, api.schema);
	}

	protected deleteMessage(id: number, fahrschuelerart: Fahrschuelerart | null): string {
		return `Fahrschülerart ${fahrschuelerart?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	addFahrschuelerart = async (data: Partial<Fahrschuelerart>) : Promise<void> => {
		const result = await api.server.addFahrschuelerart(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	}
}
