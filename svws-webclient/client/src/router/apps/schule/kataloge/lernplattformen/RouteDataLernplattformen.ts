import type { List, SimpleOperationResponse, Lernplattform } from "@core";
import { BenutzerKompetenz, ArrayList } from "@core";

import { api } from "~/router/Api";

import { ViewType, LernplattformListeManager } from "@ui";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeLernplattformenDaten } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformenDaten";
import { routeLernplattformenGruppenprozesse } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformenGruppenprozesse";
import { routeLernplattformenNeu } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformenNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new LernplattformListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeLernplattformenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataLernplattformen extends RouteDataAuswahl<LernplattformListeManager, RouteStateAuswahlInterface<LernplattformListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeLernplattformenGruppenprozesse, hinzufuegen: routeLernplattformenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<LernplattformListeManager>>> {
		const lernplattformen = await api.server.getLernplattformen(api.schema);
		const manager = new LernplattformListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, lernplattformen);
		return { manager };
	}

	async ladeDaten(auswahl: Lernplattform): Promise<Lernplattform> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Lernplattform>, id: number): Promise<void> {
		await api.server.patchLernplattform(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteLernplattformen(ids, api.schema);
	}

	add = async (data: Partial<Lernplattform>): Promise<void> => {
		const res = await api.server.addLernplattform(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(res.id);
	};

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Lernplattformen vor.');
		}

		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurden keine Lernplattformen zum Löschen ausgewählt.');
		}

		if (!this.manager.getIdsReferencedLernplattformen().isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedLernplattformen());
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedLernplattformen(): string {
		let errorMessage = 'Die folgenden Lernplattformen sind an anderer Stelle referenziert:\n\n';
		for (const id of this.manager.getIdsReferencedLernplattformen()) {
			const lernplattform = this.manager.liste.get(id);
			if (lernplattform) {
				errorMessage += `- ${lernplattform.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

	protected deleteMessage(id: number, lernplattform: Lernplattform | null): string {
		return `Lernplattform ${lernplattform?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}
}
