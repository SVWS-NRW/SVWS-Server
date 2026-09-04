import type { RouteParamsRawGeneric } from "vue-router";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeSchwerpunkteGruppenprozesse } from "./RouteSchwerpunkteGruppenprozesse";
import { routeSchwerpunkteNeu } from "./RouteSchwerpunkteNeu";
import { routeSchwerpunkteDaten } from "./RouteSchwerpunkteDaten";
import { api } from "~/router/Api";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { SchuelerSchwerpunkt } from "@core/core/data/kataloge/SchuelerSchwerpunkt";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { SchwerpunkteListeManager } from "@ui/ui/manager/kataloge/SchwerpunkteListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new SchwerpunkteListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
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
		const manager = new SchwerpunkteListeManager(idSchuljahresabschnitt, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, schwerpunkte);
		return { manager };
	}

	async ladeDaten(auswahl: SchuelerSchwerpunkt | null): Promise<SchuelerSchwerpunkt | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<SchuelerSchwerpunkt>, id: number): Promise<boolean> {
		await api.server.patchSchuelerSchwerpunkt(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteSchuelerSchwerpunkte(ids, api.schema);
	}

	add = async (partial: Partial<SchuelerSchwerpunkt>): Promise<void> => {
		const schwerpunkt = await api.server.addSchuelerSchwerpunkt(partial, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(schwerpunkt.id);
	};

	protected deleteMessage(id: number, schwerpunkt: SchuelerSchwerpunkt | null): string {
		return `Schwerpunkt ${schwerpunkt?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {

		const errorLog = new ArrayList<string>();
		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Schwerpunkte vor.');
		}
		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurden keine Schwerpunkte zum Löschen ausgewählt.');
		}
		if (!this.manager.idsOfReferencedSchwerpunkte.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedSchwerpunkte());
		}
		return { success: errorLog.isEmpty(), logs: errorLog };
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
