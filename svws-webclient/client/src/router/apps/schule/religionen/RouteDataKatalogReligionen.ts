import type { List, ReligionEintrag, SimpleOperationResponse } from "@core";
import { BenutzerKompetenz, ReligionListeManager, ArrayList } from "@core";
import { api } from "~/router/Api";
import { routeKatalogReligionDaten } from "./RouteKatalogReligionDaten";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeKatalogReligionGruppenprozesse } from "~/router/apps/schule/religionen/RouteKatalogReligionGruppenprozesse";
import { routeKatalogReligionNeu } from "~/router/apps/schule/religionen/RouteKatalogReligionNeu";

type RouteStateKatalogeReligionen = RouteStateAuswahlInterface<ReligionListeManager>;

const defaultState: RouteStateKatalogeReligionen = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeKatalogReligionDaten,
};

export class RouteDataKatalogReligionen extends RouteDataAuswahl<ReligionListeManager, RouteStateKatalogeReligionen> {

	public constructor() {
		super(defaultState, routeKatalogReligionGruppenprozesse, routeKatalogReligionNeu);
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt : number) : Promise<Partial<RouteStateKatalogeReligionen>> {
		const listeReligionen = await api.server.getReligionen(api.schema);
		const manager = new ReligionListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, listeReligionen);
		manager.setFilterAuswahlPermitted(true);
		return { manager };
	}

	public async ladeDaten(auswahl: ReligionEintrag | null) : Promise<ReligionEintrag | null> {
		return auswahl;
	}

	protected async doPatch(data : Partial<ReligionEintrag>, id: number) : Promise<void> {
		await api.server.patchReligion(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		const deletedReligionen = await api.server.deleteReligionEintraege(ids, api.schema);
		// TODO: folgende Konvertierung ausbauen sobald Backend auf DataManagerRevised umgestellt wurde
		const simpleOperationResponses = new ArrayList<SimpleOperationResponse>();
		for (const religion of deletedReligionen) {
			simpleOperationResponses.add({ id: religion.id, success: true, log: new ArrayList() } as unknown as SimpleOperationResponse)
		}
		return simpleOperationResponses;
	}

	add = async (partialReligion: Partial<ReligionEintrag>): Promise<void> => {
		delete partialReligion.id;
		const religion = await api.server.createReligion(partialReligion, api.schema)
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(religion.id)
	}

	protected deleteMessage(id: number, religion: ReligionEintrag | null) : string {
		return `Religion ${religion?.kuerzel} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	public checkBeforeDelete = (): List<string> => {
		const errorLog = new ArrayList<string>();
		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN))
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Religionen vor.');

		if (!this.manager.liste.auswahlExists())
			errorLog.add('Es wurde keine Religion zum Löschen ausgewählt.');

		return errorLog;
	}


}
