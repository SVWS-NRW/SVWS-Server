
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { api } from "~/router/Api";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeLeitungsfunktionenDaten } from "~/router/apps/schule/kataloge/leitungsfunktionen/RouteLeitungsfunktionenDaten";
import { routeLeitungsfunktionenGruppenprozesse } from "~/router/apps/schule/kataloge/leitungsfunktionen/RouteLeitungsfunktionenGruppenprozesse";
import { routeLeitungsfunktionenNeu } from "~/router/apps/schule/kataloge/leitungsfunktionen/RouteLeitungsfunktionenNeu";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { Leitungsfunktion } from "@core/core/data/schule/Leitungsfunktion";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { LeitungsfunktionenListeManager } from "@ui/ui/manager/kataloge/LeitungsfunktionenListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeLeitungsfunktionenDaten,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataLeitungsfunktionen extends RouteDataAuswahl<LeitungsfunktionenListeManager, RouteStateAuswahlInterface<LeitungsfunktionenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeLeitungsfunktionenGruppenprozesse, hinzufuegen: routeLeitungsfunktionenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<LeitungsfunktionenListeManager>>> {
		const leitungsfunktionen = await api.server.getLeitungsfunktionen(api.schema);
		const manager = new LeitungsfunktionenListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, leitungsfunktionen);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: Leitungsfunktion | null): Promise<Leitungsfunktion | null> {
		return Promise.resolve(auswahl);
	}

	protected async doPatch(data: Partial<Leitungsfunktion>, id: number): Promise<boolean> {
		await api.server.patchLeitungsfunktion(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteLeitungsfunktionen(ids, api.schema);
	}

	add = async (data: Partial<Leitungsfunktion>): Promise<void> => {
		const result = await api.server.addLeitungsfunktion(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	protected deleteMessage(id: number, leitungsfunktion: Leitungsfunktion | null): string {
		return `Leitungsfunktion ${leitungsfunktion?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Leitungsfunktionen vor.');
		}

		if (!this.manager.idsReferencedLeitungsfunktionen.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedLeitungsfunktionen());
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedLeitungsfunktionen(): string {
		let errorMessage = 'Die folgenden Leitungsfunktionen sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of this.manager.idsReferencedLeitungsfunktionen) {
			const leitungsfunktion: Leitungsfunktion | null = this.manager.liste.get(id);
			if (leitungsfunktion) {
				errorMessage += `- ${leitungsfunktion.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}
}

