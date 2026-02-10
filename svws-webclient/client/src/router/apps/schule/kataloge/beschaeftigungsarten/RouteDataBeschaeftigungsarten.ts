import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { Beschaeftigungsart, List, SimpleOperationResponse } from "@core";
import { ArrayList, BenutzerKompetenz } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ViewType, BeschaeftigungsartenListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeBeschaeftigungsartenDaten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsartenDaten";
import { routeBeschaeftigungsartenGruppenprozesse } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsartenGruppenprozesse";
import { routeBeschaeftigungsartenNeu } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsartenNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new BeschaeftigungsartenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeBeschaeftigungsartenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataBeschaeftigungsarten extends RouteDataAuswahl<BeschaeftigungsartenListeManager, RouteStateAuswahlInterface<BeschaeftigungsartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeBeschaeftigungsartenGruppenprozesse, hinzufuegen: routeBeschaeftigungsartenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<BeschaeftigungsartenListeManager>>> {
		const beschaeftigungsarten = await api.server.getBeschaeftigungsarten(api.schema);
		const manager = new BeschaeftigungsartenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, beschaeftigungsarten);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: any): Promise<Beschaeftigungsart> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchBeschaeftigungsart(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteBeschaeftigungsarten(ids, api.schema);
	}

	add = async (data: Partial<Beschaeftigungsart>): Promise<void> => {
		const result = await api.server.addBeschaeftigungsart(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	protected deleteMessage(id: number, beschaeftigungsart: Beschaeftigungsart | null): string {
		return `Beschäftigungsart ${beschaeftigungsart?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();

		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Beschäftigungsarten vor.');
		}

		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurde kein Beschäftigungsart zum Löschen ausgewählt.');
		}

		if (!this.manager.idsReferencedBeschaeftigungsarten.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedBeschaeftigungsarten());
		}

		return [errorLog.isEmpty(), errorLog];
	};

	private getErrorMessageForReferencedBeschaeftigungsarten(): string {
		let errorMessage = 'Die folgenden Beschäftigungsarten sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of this.manager.idsReferencedBeschaeftigungsarten) {
			const beschaeftigungsart = this.manager.liste.get(id);
			if (beschaeftigungsart) {
				errorMessage += `- ${beschaeftigungsart.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}
}
