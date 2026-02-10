import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { VermerkartEintrag, List, SimpleOperationResponse } from "@core";
import { ArrayList, BenutzerKompetenz } from "@core";
import { api } from "~/router/Api";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeVermerkartenDaten } from "./RouteVermerkartenDaten";
import { ViewType, VermerkartenListeManager } from "@ui";
import { routeVermerkartenGruppenprozesse } from "./RouteVermerkartenGruppenprozesse";
import { routeVermerkartenNeu } from "./RouteVermerkartenNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new VermerkartenListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList()),
	view: routeVermerkartenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataVermerkarten extends RouteDataAuswahl<VermerkartenListeManager, RouteStateAuswahlInterface<VermerkartenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeVermerkartenGruppenprozesse, hinzufuegen: routeVermerkartenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<VermerkartenListeManager>>> {
		const vermerkarten = await api.server.getVermerkarten(api.schema);
		const manager = new VermerkartenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, vermerkarten, new ArrayList());
		return { manager };
	}

	async ladeDaten(auswahl: VermerkartEintrag | null): Promise<VermerkartEintrag | null> {
		if (auswahl === null) {
			return null;
		}
		const schueler = await api.server.getSchuelerByVermerkartID(api.schema, auswahl.id);
		this.manager.schuelerVermerkartZusammenfassungen = schueler;
		return auswahl;
	}

	protected async doPatch(data: Partial<VermerkartEintrag>, id: number): Promise<void> {
		await api.server.patchVermerkart(data, api.schema, id);
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteVermerkartEintraege(ids, api.schema);
	}

	add = async (data: Partial<VermerkartEintrag>): Promise<void> => {
		const res = await api.server.createVermerkart(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(res.id);
	};

	protected deleteMessage(id: number, vermerkart: VermerkartEintrag | null): string {
		return `Vermerkart ${vermerkart?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Vermerkarten vor.');
		}

		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurde keine Vermerkart zum Löschen ausgewählt.');
		}

		if (!this.manager.idsReferencedEinwilligungsarten.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedVermerkarten());
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedVermerkarten(): string {
		let errorMessage = 'Die folgenden Vermerkarten sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of this.manager.idsReferencedEinwilligungsarten) {
			const jahrgang = this.manager.liste.get(id);
			if (jahrgang) {
				errorMessage += `- ${jahrgang.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

}
