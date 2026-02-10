import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { Floskelgruppe, List, SimpleOperationResponse } from "@core";
import { BenutzerKompetenz, ArrayList } from "@core";
import { ViewType, FloskelgruppenListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeFloskelgruppenDaten } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppenDaten";
import { routeFloskelgruppenGruppenprozesse } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppenGruppenprozesse";
import { routeFloskelgruppenNeu } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppenNeu";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new FloskelgruppenListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeFloskelgruppenDaten,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataFloskelgruppen extends RouteDataAuswahl<FloskelgruppenListeManager, RouteStateAuswahlInterface<FloskelgruppenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeFloskelgruppenGruppenprozesse, hinzufuegen: routeFloskelgruppenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<FloskelgruppenListeManager>>> {
		const floskelgruppen = await api.server.getFloskelgruppen(api.schema);
		const manager = new FloskelgruppenListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, floskelgruppen);
		return { manager };
	}

	public ladeDaten(auswahl: any): Promise<Floskelgruppe> {
		return auswahl;
	}

	protected async doPatch(data: Partial<any>, id: number): Promise<void> {
		await api.server.patchFloskelgruppe(data, api.schema, id);
	}

	add = async (data: Partial<Floskelgruppe>): Promise<void> => {
		const result = await api.server.addFloskelgruppe(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return api.server.deleteFloskelgruppen(ids, api.schema);
	}

	protected deleteMessage(id: number, floskelgruppe: Floskelgruppe | null): string {
		return `Die Floskelgruppe ${floskelgruppe?.bezeichnung ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public deleteCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();

		if (!api.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Floskelgruppen vor.');
		}

		if (!this.manager.liste.auswahlExists()) {
			errorLog.add('Es wurde keine Floskelgruppe zum Löschen ausgewählt.');
		}

		if (!this.manager.idsOfReferencedFloskelgruppen.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedFloskelgruppen());
		}

		return [errorLog.isEmpty(), errorLog];
	};

	private getErrorMessageForReferencedFloskelgruppen(): string {
		let errorMessage = 'Die folgenden Floskelgruppen sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of this.manager.idsOfReferencedFloskelgruppen) {
			const fg = this.manager.liste.get(id);
			if (fg) {
				errorMessage += `- ${fg.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

}
