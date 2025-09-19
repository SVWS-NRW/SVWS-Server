import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { List, SimpleOperationResponse, Sportbefreiung } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ArrayList } from "@core";
import { ViewType, SportbefreiungenListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeSportbefreiungenGruppenprozesse } from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteSportbefreiungenGruppenprozesse";
import { routeSportbefreiungenDaten } from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteSportbefreiungenDaten";
import { routeSportbefreiungenNeu } from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteSportbefreiungenNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new SportbefreiungenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeSportbefreiungenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
}

export class RouteDataSportbefreiungen extends RouteDataAuswahl<SportbefreiungenListeManager, RouteStateAuswahlInterface<SportbefreiungenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeSportbefreiungenGruppenprozesse, hinzufuegen: routeSportbefreiungenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<SportbefreiungenListeManager>>> {
		const sportbefreiungen = await api.server.getSportbefreiungen(api.schema);
		const manager = new SportbefreiungenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, sportbefreiungen);
		return { manager }
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: any): Promise<Sportbefreiung> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchSportbefreiung(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteSportbefreiungen(ids, api.schema);
	}

	protected deleteMessage(id: number, sportbefreiung: Sportbefreiung | null): string {
		return `Sportbefreiung ${sportbefreiung?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	addSportbefreiung = async (data: Partial<Sportbefreiung>) : Promise<void> => {
		const result = await api.server.addSportbefreiung(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	}
}
