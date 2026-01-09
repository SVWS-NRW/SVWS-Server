import type { List, SimpleOperationResponse, Telefonart } from "@core";
import { ArrayList } from "@core";
import { api } from "~/router/Api";
import { ViewType, TelefonartenListeManager } from "@ui";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeTelefonartenGruppenprozesse } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonartenGruppenprozesse";
import { routeTelefonartenNeu } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonartenNeu";
import { routeTelefonartenDaten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonartenDaten";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new TelefonartenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeTelefonartenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataTelefonarten extends RouteDataAuswahl<TelefonartenListeManager, RouteStateAuswahlInterface<TelefonartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeTelefonartenGruppenprozesse, hinzufuegen: routeTelefonartenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<TelefonartenListeManager>>> {
		const telefonArten = await api.server.getTelefonarten(api.schema);
		const manager = new TelefonartenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, telefonArten);
		return { manager };
	}

	async ladeDaten(auswahl: Telefonart | null): Promise<Telefonart | null> {
		if (auswahl === null) {
			return auswahl;
		}
		const TelefonArt = await api.server.getTelefonart(api.schema, auswahl.id);
		this.manager.getIdByEintrag(TelefonArt);
		return auswahl;
	}

	protected async doPatch(data: Partial<Telefonart>, id: number): Promise<void> {
		await api.server.patchTelefonart(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteTelefonarten(ids, api.schema);
	}

	add = async (data: Partial<Telefonart>): Promise<void> => {
		const res = await api.server.addTelefonart(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(res.id);
	};

	protected deleteMessage(id: number, TelefonArt: Telefonart | null): string {
		return `Telefonart ${TelefonArt?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}
}
