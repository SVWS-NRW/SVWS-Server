import type { SchuelerSchwerpunkt as Schwerpunkt } from "@core";
import { ArrayList, BenutzerKompetenz, type List, type SimpleOperationResponse } from "@core";
import { SchwerpunkteListeManager } from "@ui";
import { ViewType } from "@ui";
import type { RouteParamsRawGeneric } from "vue-router";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeSchwerpunkteGruppenprozesse } from "./RouteSchwerpunkteGruppenprozesse";
import { routeSchwerpunkteNeu } from "./RouteSchwerpunkteNeu";
import { routeSchwerpunkteDaten } from "./RouteSchwerpunkteDaten";
import { api } from "~/router/Api";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeSchwerpunkteDaten,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataSchwerpunkte extends RouteDataAuswahl<SchwerpunkteListeManager, RouteStateAuswahlInterface<SchwerpunkteListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeSchwerpunkteGruppenprozesse, hinzufuegen: routeSchwerpunkteNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateAuswahlInterface<SchwerpunkteListeManager>>> {
		const schwerpunkte = await api.server.getSchuelerSchwerpunkte(api.schema);
		const manager = new SchwerpunkteListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, schwerpunkte);
		return { manager };
	}

	async ladeDaten(auswahl: Schwerpunkt | null): Promise<Schwerpunkt | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Schwerpunkt>, id: number): Promise<void> {
		await api.server.patchSchuelerSchwerpunkt(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteSchuelerSchwerpunkte(ids, api.schema);
	}

	add = async (partial: Partial<Schwerpunkt>): Promise<void> => {
		const schwerpunkt = await api.server.addSchuelerSchwerpunkt(partial, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(schwerpunkt.id);
	};

	protected deleteMessage(id: number, schwerpunkt: Schwerpunkt | null): string {
		return `Schwerpunkt ${schwerpunkt?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public deleteCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();
		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Schwerpunkte vor.');
		}
		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurden keine Schwerpunkte zum Löschen ausgewählt.');
		}
		if (!this.manager.idsOfReferencedSchwerpunkte.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedSchwerpunkte());
		}
		return [errorLog.isEmpty(), errorLog];
	};

	private getErrorMessageForReferencedSchwerpunkte(): string {
		let errorMessage = 'Die folgenden Schwerpunkte sind an anderer Stelle referenziert:\n\n';
		for (const id of this.manager.idsOfReferencedSchwerpunkte) {
			const schwerpunkt = this.manager.liste.get(id);
			if (schwerpunkt) {
				errorMessage += `- ${schwerpunkt.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

}
