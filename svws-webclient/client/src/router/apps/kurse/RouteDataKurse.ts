import type { KursDaten, List, Schueler } from "@core";
import { ArrayList, DeveloperNotificationException, KursListeManager, SimpleOperationResponse } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";

import type { RouteParamsRawGeneric } from "vue-router";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeKurseGruppenprozesse } from "./RouteKurseGruppenprozesse";
import { routeKurseNeu } from "./RouteKurseNeu";
import { routeApp } from "../RouteApp";

type RouteStateKurse = RouteStateAuswahlInterface<KursListeManager>;

const defaultState: RouteStateKurse = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeKursDaten,
};

export class RouteDataKurse extends RouteDataAuswahl<KursListeManager, RouteStateKurse> {

	public constructor() {
		super(defaultState, routeKurseGruppenprozesse, routeKurseNeu);
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt : number) : Promise<Partial<RouteStateKurse>> {
		const schuljahresabschnitt = api.mapAbschnitte.value.get(idSchuljahresabschnitt);
		if (schuljahresabschnitt === undefined)
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		// Lade die Kataloge und erstelle den Manager
		const listKurse = await api.server.getKurseFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listSchueler = await api.server.getSchuelerFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const listLehrer = await api.server.getLehrer(api.schema);
		const listFaecher = await api.server.getFaecher(api.schema);
		const manager = new KursListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, listKurse, listSchueler, listJahrgaenge, listLehrer, listFaecher);
		if (this._state.value.manager === undefined) {
			manager.setFilterAuswahlPermitted(true);
			manager.setFilterNurSichtbar(false);
		}
		return { manager };
	}

	public async ladeDaten(auswahl: KursDaten | null) : Promise<KursDaten | null> {
		// Die Daten sind vollständig in der Liste enthalten, kein Aufruf der API notwendig
		return auswahl;
	}

	protected async doPatch(data : Partial<KursDaten>, id: number) : Promise<void> {
		await api.server.patchKurs(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteKurse(ids, api.schema);
	}

	protected deleteMessage(id: number, kurs: KursDaten | null) : string {
		return `Kurs ${kurs?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute({ id: eintrag.id }));
	}

	add = async (partialKurs: Partial<KursDaten>): Promise<void> => {
		const neuerKurs = await api.server.addKurs({ ...partialKurs, idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(neuerKurs.id);
	}

	/* TODO
	setzeDefaultSortierung = async () => {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahl_id = this.manager.auswahl().id;
		await api.server.setKursSortierungFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		await this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
		await this.setEintrag(this.manager.liste.get(auswahl_id));
	}
	*/

}
