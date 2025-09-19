import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { VermerkartEintrag, List, SimpleOperationResponse } from "@core";
import { ArrayList } from "@core";
import { api } from "~/router/Api";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeKatalogVermerkartenDaten } from "./RouteKatalogVermerkartenDaten";
import { ViewType, VermerkartenListeManager } from "@ui";
import { routeKatalogVermerkartenGruppenprozesse } from "./RouteKatalogVermerkartenGruppenprozesse";
import { routeKatalogVermerkartenNeu } from "./RouteKatalogVermerkartenNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new VermerkartenListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList()),
	view: routeKatalogVermerkartenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataKatalogVermerkarten extends RouteDataAuswahl<VermerkartenListeManager, RouteStateAuswahlInterface<VermerkartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeKatalogVermerkartenGruppenprozesse, hinzufuegen: routeKatalogVermerkartenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_ : number) : Promise<Partial<RouteStateAuswahlInterface<VermerkartenListeManager>>> {
		const vermerkarten = await api.server.getVermerkarten(api.schema);
		const manager = new VermerkartenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, vermerkarten, new ArrayList());
		if (this._state.value.manager === undefined)
			manager.setFilterNurSichtbar(true);
		else
			manager.useFilter(this._state.value.manager);
		return { manager };
	}

	async ladeDaten(auswahl: VermerkartEintrag | null): Promise<VermerkartEintrag | null> {
		if (auswahl === null)
			return auswahl;
		const schueler = await api.server.getSchuelerByVermerkartID(api.schema, auswahl.id);
		this.manager.setListSchuelerVermerkartZusammenfassung(schueler);
		return auswahl;
	}

	protected async doPatch(data : Partial<VermerkartEintrag>, id: number) : Promise<void> {
		await api.server.patchVermerkart(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteVermerkartEintraege(ids, api.schema);
	}

	add = async (data: Partial<VermerkartEintrag>): Promise<void> => {
		const res = await api.server.createVermerkart(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(res.id);
	}

	protected deleteMessage(id: number, vermerkart: VermerkartEintrag | null) : string {
		return `Vermerkart ${vermerkart?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

}
