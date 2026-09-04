import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { routeJahrgaengeDaten } from "./RouteJahrgaengeDaten";
import { routeJahrgaengeNeu } from "~/router/apps/schule/kataloge/jahrgaenge/RouteJahrgaengeNeu";
import { routeJahrgaengeGruppenprozesse } from "~/router/apps/schule/kataloge/jahrgaenge/RouteJahrgaengeGruppenprozesse";
import { api } from "~/router/Api";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { JahrgaengeListeManager } from "@ui/ui/manager/kataloge/JahrgaengeListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new JahrgaengeListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeJahrgaengeDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};


export class RouteDataJahrgaenge extends RouteDataAuswahl<JahrgaengeListeManager, RouteStateAuswahlInterface<JahrgaengeListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeJahrgaengeGruppenprozesse, hinzufuegen: routeJahrgaengeNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(_: number): Promise<Partial<RouteStateAuswahlInterface<JahrgaengeListeManager>>> {
		const jahrgaenge = await api.server.getJahrgangsdaten(api.schema);
		const manager = new JahrgaengeListeManager(schuleStateImpl.abschnitt.id, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle, schuleStateImpl.schulform, jahrgaenge);

		return { manager };
	}

	ladeDaten(auswahl: Promise<JahrgangsDaten>): Promise<JahrgangsDaten> {
		return auswahl;
	}

	protected async doPatch(data: Partial<JahrgangsDaten>, id: number): Promise<boolean> {
		await api.server.patchJahrgang(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteJahrgaenge(ids, api.schema);
	}

	add = async (data: Partial<JahrgangsDaten>): Promise<void> => {
		const result = await api.server.addJahrgang(data, api.schema);
		this.manager.liste.add(result);
		this.commit();
		await this.gotoDefaultView(result.id);
	};

	protected deleteMessage(id: number, jahrgang: JahrgangsDaten | null): string {
		return `Jahrgang ${jahrgang?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): { success: boolean, logs: Iterable<string> } => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Jahrgängen vor.');
		}

		if (!this.manager.getIdsReferencedJahrgaenge().isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedJahrgaenge());
		}

		return { success: errorLog.isEmpty(), logs: errorLog };
	};

	private getErrorMessageForReferencedJahrgaenge(): string {
		let errorMessage = 'Die folgenden Jahrgänge sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of this.manager.getIdsReferencedJahrgaenge()) {
			const jahrgang = this.manager.liste.get(id);
			if (jahrgang) {
				errorMessage += `- ${jahrgang.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

}
