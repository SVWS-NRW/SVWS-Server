
import type { RouteParamsRawGeneric } from "vue-router";

import type { SchulinterneFunktion } from "@core/core/data/schule/SchulinterneFunktion";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { SchulinterneFunktionenListeManager } from "@ui/ui/manager/kataloge/SchulinterneFunktionenListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

import { api } from "~/router/Api";
import { routeSchulinterneFunktionenDaten } from "~/router/apps/schule/kataloge/schulinternefunktionen/RouteSchulinterneFunktionenDaten";
import { routeSchulinterneFunktionenGruppenprozesse } from "~/router/apps/schule/kataloge/schulinternefunktionen/RouteSchulinterneFunktionenGruppenprozesse";
import { routeSchulinterneFunktionenNeu } from "~/router/apps/schule/kataloge/schulinternefunktionen/RouteSchulinterneFunktionenNeu";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeSchulinterneFunktionenDaten,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataSchulinterneFunktionen extends RouteDataAuswahl<SchulinterneFunktionenListeManager, RouteStateAuswahlInterface<SchulinterneFunktionenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeSchulinterneFunktionenGruppenprozesse, hinzufuegen: routeSchulinterneFunktionenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<SchulinterneFunktionenListeManager>>> {
		const schulinterneFunktionen = await api.server.getSchulinterneFunktionen(api.schema);
		const manager = new SchulinterneFunktionenListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, schulinterneFunktionen);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: SchulinterneFunktion | null): Promise<SchulinterneFunktion | null> {
		return Promise.resolve(auswahl);
	}

	protected async doPatch(data: Partial<SchulinterneFunktion>, id: number): Promise<boolean> {
		await api.server.patchSchulinterneFunktion(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteSchulinterneFunktionen(ids, api.schema);
	}

	add = async (data: Partial<SchulinterneFunktion>): Promise<void> => {
		const result = await api.server.addSchulinterneFunktion(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	protected deleteMessage(id: number, schulinterneFunktion: SchulinterneFunktion | null): string {
		return `Schulinterne Funktion ${schulinterneFunktion?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von schulinternen Funktionen vor.');
		}

		if (!this.manager.idsReferencedSchulinterneFunktionen.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedSchulinterneFunktionen());
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedSchulinterneFunktionen(): string {
		let errorMessage = 'Die folgenden schulinternen Funktionen sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of this.manager.idsReferencedSchulinterneFunktionen) {
			const schulinterneFunktion: SchulinterneFunktion | null = this.manager.liste.get(id);
			if (schulinterneFunktion) {
				errorMessage += `- ${schulinterneFunktion.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}
}

