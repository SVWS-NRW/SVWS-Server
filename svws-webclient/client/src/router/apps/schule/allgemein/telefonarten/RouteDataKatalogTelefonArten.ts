import type { List, SimpleOperationResponse, TelefonArt } from "@core";
import { ArrayList } from "@core";
import { api } from "~/router/Api";
import { ViewType, TelefonArtListeManager } from "@ui";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeKatalogTelefonArtenGruppenprozesse } from "~/router/apps/schule/allgemein/telefonarten/RouteKatalogTelefonArtenGruppenprozesse";
import { routeKatalogTelefonArtenNeu } from "~/router/apps/schule/allgemein/telefonarten/RouteKatalogTelefonArtenNeu";
import { routeKatalogTelefonArtenDaten } from "~/router/apps/schule/allgemein/telefonarten/RouteKatalogTelefonArtenDaten";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new TelefonArtListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeKatalogTelefonArtenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataKatalogTelefonArten extends RouteDataAuswahl<TelefonArtListeManager, RouteStateAuswahlInterface<TelefonArtListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeKatalogTelefonArtenGruppenprozesse, hinzufuegen: routeKatalogTelefonArtenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_ : number) : Promise<Partial<RouteStateAuswahlInterface<TelefonArtListeManager>>> {
		const telefonArten = await api.server.getTelefonarten(api.schema);
		const manager = new TelefonArtListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, telefonArten);
		return { manager };
	}

	async ladeDaten(auswahl: TelefonArt | null): Promise<TelefonArt | null> {
		if (auswahl === null)
			return auswahl;
		const TelefonArt = await api.server.getTelefonart(api.schema, auswahl.id);
		this.manager.getIdByEintrag(TelefonArt);
		return auswahl;
	}

	protected async doPatch(data : Partial<TelefonArt>, id: number) : Promise<void> {
		await api.server.patchTelefonart(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteTelefonarten(ids, api.schema);
	}

	add = async (data: Partial<TelefonArt>): Promise<void> => {
		const res = await api.server.addTelefonart(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(res.id);
	}

	protected deleteMessage(id: number, TelefonArt: TelefonArt | null) : string {
		return `Telefonart ${TelefonArt?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}
}
