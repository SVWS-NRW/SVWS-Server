import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { JahrgangsDaten, List, SimpleOperationResponse } from "@core";
import { ArrayList, BenutzerKompetenz } from "@core";
import { JahrgaengeListeManager, ViewType } from "@ui";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeJahrgaengeDaten } from "./RouteJahrgaengeDaten";
import { routeJahrgaengeNeu } from "~/router/apps/schule/schulbezogen/jahrgaenge/RouteJahrgaengeNeu";
import { routeJahrgaengeGruppenprozesse } from "~/router/apps/schule/schulbezogen/jahrgaenge/RouteJahrgaengeGruppenprozesse";
import { api } from "~/router/Api";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new JahrgaengeListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeJahrgaengeDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};


export class RouteDataJahrgaenge extends RouteDataAuswahl<JahrgaengeListeManager, RouteStateAuswahlInterface<JahrgaengeListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeJahrgaengeGruppenprozesse, hinzufuegen: routeJahrgaengeNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_ : number) : Promise<Partial<RouteStateAuswahlInterface<JahrgaengeListeManager>>> {
		const jahrgaenge = await api.server.getJahrgangsdaten(api.schema);
		const manager = new JahrgaengeListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, jahrgaenge);

		return { manager };
	}

	ladeDaten(auswahl: any): Promise<any> {
		return auswahl;
	}

	protected async doPatch(data : Partial<JahrgangsDaten>, id: number) : Promise<void> {
		await api.server.patchJahrgang(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteJahrgaenge(ids, api.schema);
	}

	add = async (data: Partial<JahrgangsDaten>): Promise<void> => {
		const result = await api.server.addJahrgang(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	}

	protected deleteMessage(id: number, jahrgang: JahrgangsDaten | null) : string {
		return `Jahrgang ${jahrgang?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();
		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN))
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Jahrgängen vor.');

		if (!this.manager.liste.auswahlExists())
			errorLog.add('Es wurde kein Jahrgang zum Löschen ausgewählt.');

		for (const id of this.manager.getIdsReferencedJahrgaenge()) {
			const jahrgang = this.manager.liste.get(id);
			if (jahrgang)
				errorLog.add(`Der Jahrgang ${jahrgang.bezeichnung} (${jahrgang.kuerzelStatistik}) ist an anderer Stelle referenziert und kann daher nicht gelöscht werden.`);
		}

		return [ errorLog.isEmpty(), errorLog ];
	}

}
