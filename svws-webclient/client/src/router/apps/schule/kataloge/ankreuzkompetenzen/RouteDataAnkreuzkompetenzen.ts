import type { Ankreuzkompetenz, SimpleOperationResponse, List, AnkreuzkompetenzJahrgangszuordnung } from "@core";
import { AnkreuzkompetenzenListeManager, ViewType } from "@ui";
import type { RouteParamsRawGeneric } from "vue-router";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { api } from "~/router/Api";
import { routeAnkreuzkompetenzenDaten } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzenDaten";
import { routeAnkreuzkompetenzenGruppenprozesse } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzenGruppenprozesse";
import { routeAnkreuzkompetenzenNeu } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzenNeu";

const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	view: routeAnkreuzkompetenzenDaten,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataAnkreuzkompetenzen extends RouteDataAuswahl<AnkreuzkompetenzenListeManager, RouteStateAuswahlInterface<AnkreuzkompetenzenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeAnkreuzkompetenzenGruppenprozesse, hinzufuegen: routeAnkreuzkompetenzenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateAuswahlInterface<AnkreuzkompetenzenListeManager>>> {
		const [ankreuzkompetenzen, faecher, jahrgaenge] = await Promise.all([
			api.server.getAnkreuzkompetenzen(api.schema),
			api.server.getFaecher(api.schema),
			api.server.getJahrgaenge(api.schema),
		]);

		const manager = new AnkreuzkompetenzenListeManager(
			idSchuljahresabschnitt,
			api.schuleStammdaten.idSchuljahresabschnitt,
			api.schuleStammdaten.abschnitte,
			api.schulform,
			ankreuzkompetenzen,
			faecher,
			jahrgaenge);

		return { manager };
	}

	async ladeDaten(auswahl: Ankreuzkompetenz | null): Promise<Ankreuzkompetenz | null> {
		return auswahl;
	}

	protected async doPatch(data: Partial<Ankreuzkompetenz>, id: number): Promise<boolean> {
		await api.server.patchAnkreuzkompetenz(data, api.schema, id);
		return true;
	}

	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteAnkreuzkompetenzen(ids, api.schema);
	}

	addAnkreuzkompetenz = async (partial: Partial<Ankreuzkompetenz>): Promise<Ankreuzkompetenz> => {
		const ankreuzkompetenz = await api.server.addAnkreuzkompetenz(partial, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		this.manager.liste.add(ankreuzkompetenz);
		return ankreuzkompetenz;
	};

	addJahrgaengezuordnungen = async (data: List<Partial<AnkreuzkompetenzJahrgangszuordnung>>, idAnkreuzkompetenz: number): Promise<void> => {
		const result = await api.server.addAnkreuzkompetenzJahrgangszuordnung(data, api.schema);
		this.manager.addJahrgaengeToAuswahl(result);
		this.commit();
		await this.gotoDefaultView(idAnkreuzkompetenz);
	};

	deleteJahrgaengezuordnungen = async (ids: List<number>): Promise<void> => {
		await api.server.deleteAnkreuzkompetenzJahrgangszuordnungen(ids, api.schema);
		this.manager.deleteJahrgaengezuordnungen(ids);
		this.commit();
	};

	protected deleteMessage(id: number, ankreuzkompetenz: Ankreuzkompetenz | null): string {
		return `Ankreuzkompetenz ${ankreuzkompetenz?.floskelText ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}
}
