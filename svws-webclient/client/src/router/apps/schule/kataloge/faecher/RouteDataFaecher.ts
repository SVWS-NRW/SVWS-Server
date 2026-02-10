import type { ApiFile, FachDaten, JavaSet, List, ReportingParameter, SimpleOperationResponse } from "@core";
import { ArrayList, BenutzerKompetenz, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";

import { routeFaecherDaten } from "./RouteFaecherDaten";
import { routeFaecherGruppenprozesse } from "./RouteFaecherGruppenprozesse";
import { routeFaecherNeu } from "./RouteFaecherNeu";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { ViewType, FaecherListeManager } from "@ui";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new FaecherListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList()),
	view: routeFaecherDaten,
	oldView: undefined,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataFaecher extends RouteDataAuswahl<FaecherListeManager, RouteStateAuswahlInterface<FaecherListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeFaecherGruppenprozesse, hinzufuegen: routeFaecherNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateAuswahlInterface<FaecherListeManager>>> {
		const [faecher, stundenplaene] = await Promise.all([
			api.server.getFaecher(api.schema),
			api.server.getStundenplanlisteFuerAbschnitt(api.schema, idSchuljahresabschnitt),
		]);
		const manager = new FaecherListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, faecher, stundenplaene);
		return { manager };
	}

	public async ladeDaten(auswahl: FachDaten | null): Promise<FachDaten | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<FachDaten>, id: number): Promise<void> {
		await api.server.patchFach(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteFaecher(ids, api.schema);
	}

	protected deleteMessage(id: number, fach: FachDaten | null): string {
		return `Fach ${fach?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();

		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Fächern vor.');
		}

		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurde kein Fach zum Löschen ausgewählt.');
		}

		const idsOfReferencedFaecher = this.manager.idsReferencedFaecher;
		if (!idsOfReferencedFaecher.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedFaecher(idsOfReferencedFaecher));
		}

		return [errorLog.isEmpty(), errorLog];
	};

	private getErrorMessageForReferencedFaecher(idsOfReferencedFaecher: JavaSet<number>): string {
		let errorMessage = 'Die folgenden Fächer sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of idsOfReferencedFaecher) {
			const fach = this.manager.liste.get(id);
			if (fach) {
				errorMessage += `- ${fach.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

	add = async (data: Partial<FachDaten>): Promise<void> => {
		const fach = await api.server.addFach(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(fach.id);
	};

	getPDF = api.call(async (reportingParameter: ReportingParameter, idStundenplan: number): Promise<ApiFile> => {
		if (!this.manager.liste.auswahlExists()) {
			throw new DeveloperNotificationException("Dieser Stundenplan kann nur gedruckt werden, wenn mindestens ein Fach ausgewählt ist.");
		}
		reportingParameter.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
		reportingParameter.idsHauptdaten.add(idStundenplan);
		for (const l of this.manager.liste.auswahl()) {
			reportingParameter.idsDetaildaten.add(l.id);
		}
		return await api.server.pdfReport(reportingParameter, api.schema);
	});

	sortFaecher = async () => {
		if (this.manager.liste.list().isEmpty()) {
			return;
		}
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		await api.server.setFaecherSortierungSekII(api.schema);
		await this.setSchuljahresabschnitt(idSchuljahresabschnitt, true);
	};

}
