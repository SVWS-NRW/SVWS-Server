import type { ApiFile, FachDaten, List, ReportingParameter, SimpleOperationResponse, StundenplanListeEintrag } from "@core";
import { ArrayList, BenutzerKompetenz, DeveloperNotificationException, FachListeManager } from "@core";

import { api } from "~/router/Api";

import { routeSchuleFachDaten } from "./RouteSchuleFachDaten";
import { routeSchuleFachGruppenprozesse } from "./RouteSchuleFachGruppenprozesse";
import { routeSchuleFachNeu } from "./RouteSchuleFachNeu";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { ViewType } from "@ui";
import type { RouteNode } from "~/router/RouteNode";

interface RouteStateFaecher extends RouteStateAuswahlInterface<FachListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	oldView?: RouteNode<any, any>;
}

const defaultState: RouteStateFaecher = {
	idSchuljahresabschnitt: -1,
	mapStundenplaene: new Map(),
	manager: new FachListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeSchuleFachDaten,
	oldView: undefined,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataSchuleFaecher extends RouteDataAuswahl<FachListeManager, RouteStateFaecher> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeSchuleFachGruppenprozesse, hinzufuegen: routeSchuleFachNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	protected async createManager(idSchuljahresabschnitt : number) : Promise<Partial<RouteStateAuswahlInterface<FachListeManager>>> {
		const listKatalogeintraege = await api.server.getFaecher(api.schema);
		const manager = new FachListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,	api.schulform, listKatalogeintraege);
		if (this._state.value.manager === undefined) {
			manager.setFilterAuswahlPermitted(true);
			manager.setFilterNurSichtbar(false);
		} else {
			manager.useFilter(this._state.value.manager);
		}

		return { manager }
	}

	public async ladeDaten(auswahl: FachDaten | null) : Promise<FachDaten | null> {
		if (auswahl === null)
			return null;
		return await api.server.getFach(api.schema, auswahl.id);
	}

	public async updateMapStundenplaene() {
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, this.idSchuljahresabschnitt);
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		for (const l of listStundenplaene)
			mapStundenplaene.set(l.id, l);
		this.setPatchedState({ mapStundenplaene });
	}

	protected async doPatch(data : Partial<FachDaten>, id: number) : Promise<void> {
		await api.server.patchFach(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteFaecher(ids, api.schema);
	}

	protected deleteMessage(id: number, fach: FachDaten | null) : string {
		return `Fach ${fach?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteFaecherCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();
		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN))
			errorLog.add('Es kiegt keine Berechtigung zum Löschen von Fächern vor.');
		if (!this.manager.liste.auswahlExists())
			errorLog.add('Es wurde kein Fach zum Löschen ausgewählt.');
		for (const id of this.manager.getIdsReferenzierterFaecher()) {
			const fach = this.manager.liste.get(id);
			if (fach)
				errorLog.add(`Das Fach ${fach.bezeichnung} mit dem Kürzel ${fach.kuerzel} ist an anderer Stelle referenziert und kann daher nicht gelöscht werden.`);
		}
		return [errorLog.isEmpty(), errorLog];
	};

	setzeDefaultSortierungSekII = async () => {
		if (this.manager.liste.list().isEmpty())
			return;
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahlId = this.manager.auswahl().id;
		await api.server.setFaecherSortierungSekII(api.schema);
		await this.setSchuljahresabschnitt(idSchuljahresabschnitt, true);
		await this.setDaten(this.manager.liste.get(auswahlId));
	};

	add = async (data: Partial<FachDaten>): Promise<void> => {
		const fachDaten = await api.server.addFach(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(fachDaten.id);
	};

	getPDF = api.call(async (reportingParameter: ReportingParameter, idStundenplan: number): Promise<ApiFile> => {
		if (!this.manager.liste.auswahlExists())
			throw new DeveloperNotificationException("Dieser Stundenplan kann nur gedruckt werden, wenn mindestens ein Fach ausgewählt ist.");
		reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
		reportingParameter.idsHauptdaten.add(idStundenplan);
		for (const l of this.manager.liste.auswahl())
			reportingParameter.idsDetaildaten.add(l.id);
		return await api.server.pdfReport(reportingParameter, api.schema);
	});

}
