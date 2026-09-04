import { api } from "~/router/Api";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeLernplattformenDaten } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformenDaten";
import { routeLernplattformenGruppenprozesse } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformenGruppenprozesse";
import { routeLernplattformenNeu } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformenNeu";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { Lernplattform } from "@core/core/data/schule/Lernplattform";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { LernplattformListeManager } from "@ui/ui/manager/kataloge/LernplattformListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

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
		const manager = new LernplattformListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle, schuleStateImpl.schulform, lernplattformen);
		return { manager };
	}

	async ladeDaten(auswahl: Lernplattform): Promise<Lernplattform> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Lernplattform>, id: number): Promise<boolean> {
		await api.server.patchLernplattform(data, api.schema, id);
		return true;
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

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
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
