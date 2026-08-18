import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { List, SimpleOperationResponse, FoerderschwerpunktEintrag } from "@core";
import { BenutzerKompetenz, ArrayList } from "@core";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { ViewType, FoerderschwerpunkteListeManager } from "@ui";
import { api } from "~/router/Api";
import { routeFoerderschwerpunkteDaten } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkteDaten";
import { routeFoerderschwerpunkteGruppenprozesse } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkteGruppenprozesse";
import { routeFoerderschwerpunkteNeu } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkteNeu";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new FoerderschwerpunkteListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeFoerderschwerpunkteDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataFoerderschwerpunkte extends RouteDataAuswahl<FoerderschwerpunkteListeManager, RouteStateAuswahlInterface<FoerderschwerpunkteListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeFoerderschwerpunkteGruppenprozesse, hinzufuegen: routeFoerderschwerpunkteNeu });
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<FoerderschwerpunkteListeManager>>> {
		const foerderschwerpunkte = await api.server.getKatalogFoerderschwerpunkte(api.schema);
		const manager = new FoerderschwerpunkteListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, foerderschwerpunkte);
		return { manager };
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	public ladeDaten(auswahl: Promise<FoerderschwerpunktEintrag>): Promise<FoerderschwerpunktEintrag> {
		return auswahl;
	}

	protected async doPatch(data: Partial<FoerderschwerpunktEintrag>, id: number): Promise<boolean> {
		await api.server.patchKatalogFoerderschwerpunkt(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return api.server.deleteKatalogFoerderschwerpunkte(ids, api.schema);
	}

	protected deleteMessage(id: number, foerderschwerpunkt: FoerderschwerpunktEintrag | null): string {
		return `Förderschwerpunkt ${foerderschwerpunkt?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	addFoerderschwerpunkt = async (data: Partial<FoerderschwerpunktEintrag>): Promise<void> => {
		const result = await api.server.addKatalogFoerderschwerpunkt(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Förderschwerpunkten vor.');
		}

		if (!this.manager.getIdsReferencedFoerderschwerpunkte().isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedFoerderschwerpunkte());
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedFoerderschwerpunkte(): string {
		let errorMessage = 'Die folgenden Förderschwerpunkte sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of this.manager.getIdsReferencedFoerderschwerpunkte()) {
			const foerderschwerpunkt = this.manager.liste.get(id);
			if (foerderschwerpunkt) {
				errorMessage += `- ${foerderschwerpunkt.kuerzelStatistik}: ${foerderschwerpunkt.kuerzel} \n`;
			}
		}
		return errorMessage;
	}

}
