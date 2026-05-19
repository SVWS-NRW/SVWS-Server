import type { RouteParamsRawGeneric } from "vue-router";
import type { Teilleistungsart, List, SimpleOperationResponse } from "@core";
import { ArrayList, BenutzerKompetenz } from "@core";
import { ViewType } from "@ui";
import { TeilleistungsartenListeManager } from "~/components/schule/kataloge/teilleistungsarten/manager/TeilleistungsartenListeManager";
import { api } from "~/router/Api";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeTeilleistungsartenGruppenprozesse } from "./RouteTeilleistungsartenGruppenprozesse";
import { routeTeilleistungsartenNeu } from "./RouteTeilleistungsartenNeu";
import { routeTeilleistungsartenDaten } from "./RouteTeilleistungsartenDaten";
import { abschnittState } from "~/states/AbschnittStateImpl";
import { schuleState } from "~/states/SchuleStateImpl";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeTeilleistungsartenDaten,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataTeilleistungsarten extends RouteDataAuswahl<TeilleistungsartenListeManager, RouteStateAuswahlInterface<TeilleistungsartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeTeilleistungsartenGruppenprozesse, hinzufuegen: routeTeilleistungsartenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateAuswahlInterface<TeilleistungsartenListeManager>>> {
		const teilleistungsarten = await api.server.getTeilleistungsarten(api.schema);
		const manager = new TeilleistungsartenListeManager(idSchuljahresabschnitt, schuleState.abschnitt.id, abschnittState.alle,
			schuleState.schulform, teilleistungsarten);
		return { manager };
	}

	async ladeDaten(auswahl: Teilleistungsart | null): Promise<Teilleistungsart | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Teilleistungsart>, id: number): Promise<boolean> {
		await api.server.patchTeilleistungsart(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteTeilleistungsarten(ids, api.schema);
	}

	add = async (partial: Partial<Teilleistungsart>): Promise<void> => {
		const teilleistungsart = await api.server.addTeilleistungsart(partial, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(teilleistungsart.id);
	};

	protected deleteMessage(id: number, teilleistung: Teilleistungsart | null): string {
		return `Teilleistungsart ${teilleistung?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {

		const errorLog = new ArrayList<string>();
		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Teilleistungsarten vor.');
		}
		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurden keine Teilleistungsarten zum Löschen ausgewählt.');
		}
		if (!this.manager.idsOfReferencedTeilleistungsarten.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedTeilleistungsarten());
		}
		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedTeilleistungsarten(): string {
		let errorMessage = 'Die folgenden Teilleistungsarten sind an anderer Stelle referenziert:\n\n';
		for (const id of this.manager.idsOfReferencedTeilleistungsarten) {
			const teilleistungsart = this.manager.liste.get(id);
			if (teilleistungsart) {
				errorMessage += `- ${teilleistungsart.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

}
