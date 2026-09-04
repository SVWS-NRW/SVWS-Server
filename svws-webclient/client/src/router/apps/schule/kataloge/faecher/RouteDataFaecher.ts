import { api } from "~/router/Api";
import type { RouteParamsRawGeneric } from "vue-router";
import { RouteDataAuswahl, type RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { routeFaecherDaten } from "./RouteFaecherDaten";
import { routeFaecherGruppenprozesse } from "./RouteFaecherGruppenprozesse";
import { routeFaecherNeu } from "./RouteFaecherNeu";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { FaecherListeManager } from "@ui/ui/manager/kataloge/FaecherListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new FaecherListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList()),
	view: routeFaecherDaten,
	oldView: undefined,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataFaecher extends RouteDataAuswahl<FaecherListeManager, RouteStateAuswahlInterface<FaecherListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeFaecherGruppenprozesse, hinzufuegen: routeFaecherNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateAuswahlInterface<FaecherListeManager>>> {
		const [faecher, stundenplaene] = await Promise.all([
			api.server.getFaecher(api.schema),
			api.server.getStundenplanlisteFuerAbschnitt(api.schema, idSchuljahresabschnitt),
		]);
		const manager = new FaecherListeManager(idSchuljahresabschnitt, schuleStateImpl.abschnitt.id, abschnittStateImpl.alle,
			schuleStateImpl.schulform, faecher, stundenplaene);
		return { manager };
	}

	public async ladeDaten(auswahl: FachDaten | null): Promise<FachDaten | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<FachDaten>, id: number): Promise<boolean> {
		await api.server.patchFach(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteFaecher(ids, api.schema);
	}

	protected deleteMessage(id: number, fach: FachDaten | null): string {
		return `Fach ${fach?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	deleteCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Fächern vor.');
		}

		const idsOfReferencedFaecher = this.manager.idsReferencedFaecher;
		if (!idsOfReferencedFaecher.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedFaecher(idsOfReferencedFaecher));
		}

		return [errorLog.isEmpty(), errorLog];
	};

	private getErrorMessageForReferencedFaecher(idsOfReferencedFaecher: JavaSet<number>): string {
		let errorMessage = 'Die folgenden Fächer sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of idsOfReferencedFaecher) {
			const fach = this.manager.liste.get(id);
			if (fach) {
				errorMessage += `- ${fach.bezeichnung} \n`;
			}
		}
		return errorMessage;
	}

	add = async (data: Partial<FachDaten>): Promise<void> => {
		const fach = await api.server.addFach(data, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(fach.id);
	};

	sortFaecher = async () => {
		if (this.manager.liste.list().isEmpty()) {
			return;
		}
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		await api.server.setFaecherSortierungSekII(api.schema);
		await this.setSchuljahresabschnitt(idSchuljahresabschnitt, true);
	};

}
