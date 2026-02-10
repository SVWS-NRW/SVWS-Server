import type { List, SimpleOperationResponse, Einwilligungsart } from "@core";
import { BenutzerKompetenz, ArrayList } from "@core";
import { api } from "~/router/Api";
import { routeEinwilligungsartenDaten } from "./RouteEinwilligungsartenDaten";
import { ViewType, EinwilligungsartenListeManager } from "@ui";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeEinwilligungsartenGruppenprozesse } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsartenGruppenprozesse";
import { routeEinwilligungsartenNeu } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsartenNeu";
import type { RouteParamsRawGeneric } from "vue-router";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new EinwilligungsartenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeEinwilligungsartenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataEinwilligungsarten extends RouteDataAuswahl<EinwilligungsartenListeManager, RouteStateAuswahlInterface<EinwilligungsartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeEinwilligungsartenGruppenprozesse, hinzufuegen: routeEinwilligungsartenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<EinwilligungsartenListeManager>>> {
		const einwilligungsarten = await api.server.getEinwilligungsarten(api.schema);
		const manager = new EinwilligungsartenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, einwilligungsarten);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	async ladeDaten(auswahl: Einwilligungsart): Promise<Einwilligungsart> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Einwilligungsart>, id: number): Promise<void> {
		await api.server.patchEinwilligungsart(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteEinwilligungsarten(ids, api.schema);
	}

	add = async (data: Partial<Einwilligungsart>): Promise<void> => {
		const res = await api.server.createEinwilligungsart(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(res.id);
	};

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Einwilligungsarten vor.');
		}

		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurden keine Einwilligungsarten zum Löschen ausgewählt.');
		}

		if (!this.manager.getIdsReferencedEinwilligungsarten().isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedEinwilligungsarten());
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedEinwilligungsarten(): string {
		let errorMessage = 'Die folgenden Einwilligungsarten sind an anderer Stelle referenziert:\n\n';
		for (const id of this.manager.getIdsReferencedEinwilligungsarten()) {
			const einwilligungsart = this.manager.liste.get(id);
			if (einwilligungsart) {
				errorMessage += `- ${einwilligungsart.bezeichnung}: ${einwilligungsart.schluessel} \n`;
			}
		}
		return errorMessage;
	}

	protected deleteMessage(id: number, einwilligungsart: Einwilligungsart | null): string {
		return `Einwilligungsart ${einwilligungsart?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}
}
