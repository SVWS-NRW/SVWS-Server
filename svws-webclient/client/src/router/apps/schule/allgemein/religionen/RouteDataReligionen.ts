import type { List, ReligionEintrag, SimpleOperationResponse } from "@core";
import { BenutzerKompetenz, ArrayList } from "@core";
import { ReligionenListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeReligionenDaten } from "./RouteReligionenDaten";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeReligionenGruppenprozesse } from "~/router/apps/schule/allgemein/religionen/RouteReligionenGruppenprozesse";
import { routeReligionenNeu } from "~/router/apps/schule/allgemein/religionen/RouteReligionenNeu";

type RouteStateReligionen = RouteStateAuswahlInterface<ReligionenListeManager>;

const defaultState: RouteStateReligionen = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeReligionenDaten,
};

export class RouteDataReligionen extends RouteDataAuswahl<ReligionenListeManager, RouteStateReligionen> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeReligionenGruppenprozesse, hinzufuegen: routeReligionenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateReligionen>> {
		const religionen = await api.server.getReligionen(api.schema);
		const manager = new ReligionenListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, religionen);
		if (this._state.value.manager === undefined) {
			manager.setFilterAuswahlPermitted(true);
			manager.setFilterNurSichtbar(false);
		} else {
			manager.useFilter(this._state.value.manager);
		}
		return { manager };
	}

	public async ladeDaten(auswahl: ReligionEintrag | null): Promise<ReligionEintrag | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<ReligionEintrag>, id: number): Promise<void> {
		await api.server.patchReligion(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteReligionEintraege(ids, api.schema);
	}

	add = async (partialReligion: Partial<ReligionEintrag>): Promise<void> => {
		delete partialReligion.id;
		const religion = await api.server.createReligion(partialReligion, api.schema)
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(religion.id)
	}

	protected deleteMessage(id: number, religion: ReligionEintrag | null): string {
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
