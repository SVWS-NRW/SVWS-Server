import { api } from "~/router/Api";
import { routeKonfessionenDaten } from "./RouteKonfessionenDaten";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeKonfessionenGruppenprozesse } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionenGruppenprozesse";
import { routeKonfessionenNeu } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionenNeu";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { KonfessionenListeManager } from "@ui/ui/manager/kataloge/KonfessionenListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

type RouteStateKonfessionen = RouteStateAuswahlInterface<KonfessionenListeManager>;

const defaultState: RouteStateKonfessionen = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeKonfessionenDaten,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataKonfessionen extends RouteDataAuswahl<KonfessionenListeManager, RouteStateKonfessionen> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeKonfessionenGruppenprozesse, hinzufuegen: routeKonfessionenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateKonfessionen>> {
		const konfessionen = await api.server.getReligionen(api.schema);
		const manager = new KonfessionenListeManager(idSchuljahresabschnitt, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, konfessionen);
		return { manager };
	}

	public async ladeDaten(auswahl: ReligionEintrag | null): Promise<ReligionEintrag | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<ReligionEintrag>, id: number): Promise<boolean> {
		await api.server.patchReligion(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteReligionen(ids, api.schema);
	}

	add = async (partial: Partial<ReligionEintrag>): Promise<void> => {
		delete partial.id;
		const konfession = await api.server.addReligion(partial, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(konfession.id);
	};

	protected deleteMessage(id: number, konfession: ReligionEintrag | null): string {
		return `Konfession ${konfession?.bezeichnung} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	public deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();
		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Konfessionen vor.');
		}

		if (!this.manager.idsReferencedKonfessionen.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedKonfessionen());
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedKonfessionen(): string {
		let errorMessage = 'Die folgenden Konfessionen sind an anderer Stelle referenziert:\n\n';
		for (const id of this.manager.idsReferencedKonfessionen) {
			const konfession = this.manager.liste.get(id);
			if (konfession) {
				errorMessage += `- ${konfession.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

}
