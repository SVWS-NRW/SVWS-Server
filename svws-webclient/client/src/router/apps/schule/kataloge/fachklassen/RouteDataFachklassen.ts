import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { api } from "~/router/Api";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { routeFachklassenDaten } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassenDaten";
import { routeFachklassenGruppenprozesse } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassenGruppenprozesse";
import { routeFachklassenNeu } from "~/router/apps/schule/kataloge/fachklassen/RouteFachklassenNeu";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { FachklasseEintrag } from "@core/core/data/schule/FachklasseEintrag";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { FachklassenListeManager } from "@ui/ui/manager/kataloge/FachklassenListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeFachklassenDaten,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataFachklassen extends RouteDataAuswahl<FachklassenListeManager, RouteStateAuswahlInterface<FachklassenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeFachklassenGruppenprozesse, hinzufuegen: routeFachklassenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateAuswahlInterface<FachklassenListeManager>>> {
		const fachklassen = await api.server.getFachklassen(api.schema);
		const manager = new FachklassenListeManager(idSchuljahresabschnitt, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, fachklassen);
		return { manager };
	}

	async ladeDaten(auswahl: FachklasseEintrag | null): Promise<FachklasseEintrag | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<boolean> {
		await api.server.patchFachklasse(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteFachklassen(ids, api.schema);
	}

	add = async (partial: Partial<FachklasseEintrag>): Promise<void> => {
		const fachklasse = await api.server.addFachklasse(partial, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(fachklasse.id);
	};

	protected deleteMessage(id: number, fachklasse: FachklasseEintrag | null): string {
		return `Fachklasse ${fachklasse?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {

		const errorLog = new ArrayList<string>();
		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Fachklassen vor.');
		}
		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurden keine Fachklassen zum Löschen ausgewählt.');
		}
		if (!this.manager.idsOfReferencedFachklassen.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedFachklassen());
		}
		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedFachklassen(): string {
		let errorMessage = 'Die folgenden Fachklassen sind an anderer Stelle referenziert:\n\n';
		for (const id of this.manager.idsOfReferencedFachklassen) {
			const fachklasse = this.manager.liste.get(id);
			if (fachklasse) {
				errorMessage += `- ${fachklasse.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

}
