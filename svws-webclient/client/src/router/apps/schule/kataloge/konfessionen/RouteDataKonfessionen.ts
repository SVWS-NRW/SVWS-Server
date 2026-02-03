import type { List, ReligionEintrag, SimpleOperationResponse } from "@core";
import { BenutzerKompetenz, ArrayList } from "@core";
import { KonfessionenListeManager, ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeKonfessionenDaten } from "./RouteKonfessionenDaten";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeKonfessionenGruppenprozesse } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionenGruppenprozesse";
import { routeKonfessionenNeu } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionenNeu";

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
		const manager = new KonfessionenListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, konfessionen);
		return { manager };
	}

	public async ladeDaten(auswahl: ReligionEintrag | null): Promise<ReligionEintrag | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<ReligionEintrag>, id: number): Promise<void> {
		await api.server.patchReligion(data, api.schema, id);
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
		return `Konfession ${konfession?.kuerzel} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	public deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();
		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Konfessionen vor.');
		}
		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurde keine Konfession zum Löschen ausgewählt.');
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
