import type { Ankreuzkompetenz, SimpleOperationResponse, List } from "@core";
import { AnkreuzkompetenzJahrgangszuordnung, ArrayList, BenutzerKompetenz } from "@core";
import { AnkreuzkompetenzenListeManager, ViewType } from "@ui";
import type { RouteParamsRawGeneric } from "vue-router";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import { api } from "~/router/Api";
import { routeAnkreuzkompetenzenDaten } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzenDaten";
import { routeAnkreuzkompetenzenGruppenprozesse } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzenGruppenprozesse";
import { routeAnkreuzkompetenzenNeu } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzenNeu";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";

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
			schuleStateImpl.abschnitt.id,
			abschnittStateImpl.alle,
			schuleStateImpl.schulform,
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

	addAnkreuzkompetenz = async (partial: Partial<Ankreuzkompetenz>, idsJahrgaenge: List<number>): Promise<Ankreuzkompetenz> => {
		const ankreuzkompetenz = await api.server.addAnkreuzkompetenz(partial, api.schema);

		if (!idsJahrgaenge.isEmpty()) {
			const zuordnungen = await this.internalAddJahrgaengezuordnungen(ankreuzkompetenz.id, idsJahrgaenge);
			ankreuzkompetenz.jahrgaengezuordnung.addAll(zuordnungen);
		}

		this.manager.liste.add(ankreuzkompetenz);
		this.manager.setDaten(ankreuzkompetenz);
		this.commit();

		return ankreuzkompetenz;
	};

	addJahrgaengezuordnungen = async (idAnkreuzkompetenz: number, idsJahrgaenge: List<number>): Promise<void> => {
		if (idsJahrgaenge.isEmpty()) {
			return;
		}

		const zuordnungen = await this.internalAddJahrgaengezuordnungen(idAnkreuzkompetenz, idsJahrgaenge);

		this.manager.addJahrgaengezuordnungen(zuordnungen);
		this.commit();
	};

	deleteJahrgaengezuordnungen = async (ids: List<number>): Promise<void> => {
		await api.server.deleteAnkreuzkompetenzJahrgangszuordnungen(ids, api.schema);
		this.manager.deleteJahrgaengezuordnungen(ids);
		this.manager.daten().referenziertInAnderenTabellen = !this.manager.daten().jahrgaengezuordnung.isEmpty();
		this.commit();
	};

	protected deleteMessage(id: number, ankreuzkompetenz: Ankreuzkompetenz | null): string {
		return `Ankreuzkompetenz ${ankreuzkompetenz?.floskelText ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	private createJahrgaengezuordnungen(idAnkreuzkompetenz: number, idsJahrgaenge: List<number>) {
		const jahrgaengezuordnungen = new ArrayList<AnkreuzkompetenzJahrgangszuordnung>();
		for (const idJahrgang of idsJahrgaenge) {
			const zuordnung = new AnkreuzkompetenzJahrgangszuordnung();
			zuordnung.idAnkreuzkompetenz = idAnkreuzkompetenz;
			zuordnung.idJahrgang = idJahrgang;
			const { id, ...partialData } = zuordnung;
			jahrgaengezuordnungen.add(partialData as AnkreuzkompetenzJahrgangszuordnung);
		}
		return jahrgaengezuordnungen;
	}

	private async internalAddJahrgaengezuordnungen(idAnkreuzkompetenz: number, idsJahrgaenge: List<number>): Promise<List<AnkreuzkompetenzJahrgangszuordnung>> {
		const zuordnungenToCreate = this.createJahrgaengezuordnungen(idAnkreuzkompetenz, idsJahrgaenge);
		return await api.server.addAnkreuzkompetenzJahrgangszuordnungMultiple(zuordnungenToCreate, api.schema);
	}

	deleteCheck = (): [boolean, List<string>] => {
		const errorLog = new ArrayList<string>();

		if (!benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Ankreuzkompetenzen vor.');
		}

		if (!this.manager.idsReferencedAnkreuzkompetenzen.isEmpty()) {
			errorLog.add(this.getErrorMessageForReferencedAnkreuzkompetenzen());
		}

		return [errorLog.isEmpty(), errorLog];
	};

	private getErrorMessageForReferencedAnkreuzkompetenzen(): string {
		let errorMessage = 'Die folgenden Ankreuzkompetenzen sind an anderer Stelle referenziert und können daher nicht gelöscht werden:\n\n';
		for (const id of this.manager.idsReferencedAnkreuzkompetenzen) {
			const ankreuzkompetenz = this.manager.liste.get(id);
			if (ankreuzkompetenz) {
				errorMessage += `- ${ankreuzkompetenz.floskelText} \n`;
			}
		}
		return errorMessage;
	}

}
