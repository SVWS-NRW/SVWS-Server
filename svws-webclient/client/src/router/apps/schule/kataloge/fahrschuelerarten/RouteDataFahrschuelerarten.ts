import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { Fahrschuelerart, List, SimpleOperationResponse } from "@core";
import { BenutzerKompetenz, ArrayList } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ViewType, FahrschuelerartenListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeFahrschuelerartenGruppenprozesse } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerartenGruppenprozesse";
import { routeFahrschuelerartenNeu } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerartenNeu";
import { routeFahrschuelerartenDaten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerartenDaten";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new FahrschuelerartenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeFahrschuelerartenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataFahrschuelerarten extends RouteDataAuswahl<FahrschuelerartenListeManager, RouteStateAuswahlInterface<FahrschuelerartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeFahrschuelerartenGruppenprozesse, hinzufuegen: routeFahrschuelerartenNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<FahrschuelerartenListeManager>>> {
		const fahrschuelerarten = await api.server.getFahrschuelerarten(api.schema);
		const manager = new FahrschuelerartenListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, fahrschuelerarten);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: Promise<Fahrschuelerart>): Promise<Fahrschuelerart> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Fahrschuelerart>, id: number): Promise<boolean> {
		await api.server.patchFahrschuelerart(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteFahrschuelerarten(ids, api.schema);
	}

	protected deleteMessage(id: number, fahrschuelerart: Fahrschuelerart | null): string {
		return `Fahrschülerart ${fahrschuelerart?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	add = async (data: Partial<Fahrschuelerart>): Promise<void> => {
		const result = await api.server.addFahrschuelerart(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Fahrschülerarten vor.');
		}

		if (!this.manager.idsReferencedFahrschuelerarten.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedFahrschuelerarten());
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedFahrschuelerarten(): string {
		let errorMessage = 'Die folgenden Fahrschülerarten sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of this.manager.idsReferencedFahrschuelerarten) {
			const fahrschuelerart = this.manager.liste.get(id);
			if (fahrschuelerart) {
				errorMessage += `- ${fahrschuelerart.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}
}
