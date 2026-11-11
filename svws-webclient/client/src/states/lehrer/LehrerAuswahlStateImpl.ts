import type { LehrerPersonalabschnittsdaten } from "@core/asd/data/lehrer/LehrerPersonalabschnittsdaten";
import type { LehrerStammdaten } from "@core/asd/data/lehrer/LehrerStammdaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { LehrerUtils } from "@core/core/utils/lehrer/LehrerUtils";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { useAbschnittState } from "@ui/states/AbschnittState";
import { useBenutzerState } from "@ui/states/BenutzerState";
import { useConfigState } from "@ui/states/ConfigState";
import { ListeMitAuswahl } from "@ui/ui/manager/ListeMitAuswahl";

import type { GenericAuswahlReactiveState } from "../GenericAuswahlStateImpl";
import { GenericAuswahlStateImpl } from "../GenericAuswahlStateImpl";
import { StundenplaeneListeState } from "../StundenplaeneListeState";
import { api } from "~/router/Api";

import { LehrerAuswahlFilter } from "./LehrerAuswahlFilter";
import type { LehrerAuswahlState } from "./LehrerAuswahlState";
import { lehrerAuswahlStateRoutingAdapter } from "./LehrerAuswahlStateRoutingAdapter";
import { LehrerGruppenprozessState } from "./LehrerGruppenprozessState";
import { LehrerPersonaldatenState } from "./LehrerPersonaldatenState";
import { LehrerStammdatenState } from "./LehrerStammdatenState";

interface LehrerAuswahlReactiveState extends GenericAuswahlReactiveState<LehrerListeEintrag> {
}

/**
 * Der State für die Auswahlliste der Lehrer
 */
export class LehrerAuswahlStateImpl extends GenericAuswahlStateImpl<LehrerListeEintrag, LehrerStammdaten, LehrerAuswahlReactiveState> implements LehrerAuswahlState {

	/** Der Teil-State für die Stundenpläne des Schuljahresabschnittes */
	private readonly _stundenplaene = new StundenplaeneListeState();

	/** Der State für die Stammdaten */
	private readonly _stammdaten = new LehrerStammdatenState(this);

	/** Der State für die Stammdaten der Mehrfachauswahl */
	private readonly _gruppenprozesse = new LehrerGruppenprozessState(this);

	/** Der State für die Personaldaten */
	private readonly _personaldaten = new LehrerPersonaldatenState(this, () => this.idSchuljahresabschnitt);

	/** Der Filter der Auswahlliste */
	private readonly _filter = new LehrerAuswahlFilter(
		() => this._state.value.liste?.list() ?? new ArrayList<LehrerListeEintrag>(),
		() => this.idSelected
	);

	public constructor() {
		super({
			idSchuljahresabschnitt: -1,
			liste: undefined,
			idSelected: null,
		}, lehrerAuswahlStateRoutingAdapter);
		this._filter.onChanged = () => {
			// Die Schalter "Nur Sichtbare" und "Nur Statistik-Relevante" werden in der Konfiguration des Benutzers gespeichert
			if (this._filter.values.nurSichtbar !== this.filterNurSichtbar) {
				void this.setFilterNurSichtbar(this._filter.values.nurSichtbar);
			}
			if (this._filter.values.nurStatistikrelevant !== this.filterNurStatistikrelevant) {
				void this.setFilterNurStatistikrelevant(this._filter.values.nurStatistikrelevant);
			}
			void this.setFilter();
		};
	}

	/**
	 * Gibt den Filter der Auswahlliste zurück.
	 *
	 * @returns der Filter
	 */
	public get filter(): LehrerAuswahlFilter {
		return this._filter;
	}

	public get stammdaten(): LehrerStammdatenState {
		return this._stammdaten;
	}

	public get gruppenprozesse(): LehrerGruppenprozessState {
		return this._gruppenprozesse;
	}

	public get personaldaten(): LehrerPersonaldatenState {
		return this._personaldaten;
	}

	public commit(): void {
		super.commit();
		this._filter.refresh();
	}

	protected getListFiltered(): List<LehrerListeEintrag> {
		return this._filter.filtered;
	}

	/**
	 * Setzt den State und den Teil-State für die Stundenpläne in den Default-State zurück.
	 */
	public reset(): void {
		super.reset();
		this._stundenplaene.reset();
		this._stammdaten.reset();
		this._gruppenprozesse.reset();
		this._personaldaten.reset();
		this._filter.reset();
	}

	/**
	 * Gibt eine Map mit den Stundenplänen des Schuljahresabschnittes zugeordnet zu deren ID zurück.
	 *
	 * @returns die Map mit den Stundenplänen
	 */
	public get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._stundenplaene.mapStundenplaene;
	}

	public get filterNurSichtbar(): boolean {
		const configState = useConfigState();
		return configState.config.getValue("lehrer.auswahl.filterNurSichtbar") === 'true';
	}

	public async setFilterNurSichtbar(value: boolean): Promise<void> {
		const configState = useConfigState();
		await configState.config.setValue('lehrer.auswahl.filterNurSichtbar', value ? "true" : "false");
	};

	public get filterNurStatistikrelevant(): boolean {
		const configState = useConfigState();
		return configState.config.getValue("lehrer.auswahl.filterNurStatistikrelevant") === 'true';
	}

	public async setFilterNurStatistikrelevant(value: boolean): Promise<void> {
		const configState = useConfigState();
		await configState.config.setValue('lehrer.auswahl.filterNurStatistikrelevant', value ? "true" : "false");
	};

	/**
	 * Erstellt die Auswahlliste für den angegebenen Schuljahresabschnitt. Die Daten dafür werden
	 * über die API abgefragt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @returns Eine Promise mit den Anpassungen für die Auswahlliste und deren Daten im reaktiven State
	 */
	protected async createListe(idSchuljahresabschnitt: number): Promise<Partial<LehrerAuswahlReactiveState>> {
		const abschnittState = useAbschnittState();
		const schuljahresabschnitt = abschnittState.getOrNull(idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		}

		// Lade die Daten von der API
		const listLehrer = await api.server.getLehrer(api.schema);

		// Erstelle die Auswahlliste
		const liste = new ListeMitAuswahl(listLehrer, e => e.id, LehrerUtils.comparator, null);

		// Beim ersten Laden werden die Schalter aus der Konfiguration übernommen, ansonsten bleibt der Filter erhalten
		if (this._state.value.liste === undefined) {
			this._filter.setValueSilent("nurSichtbar", this.filterNurSichtbar);
			this._filter.setValueSilent("nurStatistikrelevant", this.filterNurStatistikrelevant);
		}
		// Die Stundenpläne gehören zum Schuljahresabschnitt und werden bei dessen Wechsel verworfen
		this._stundenplaene.reset();
		return { liste };
	}

	/**
	 * Gibt die IDs der Lehrer der Mehrfachauswahl zurück, die in anderen Datenbanktabellen referenziert werden.
	 *
	 * @returns die Menge der IDs der referenzierten Lehrer
	 */
	private getIdsReferenzierterLehrer(): JavaSet<number> {
		const result = new HashSet<number>();
		for (const lehrer of this.liste.auswahl()) {
			if (lehrer.referenziertInAnderenTabellen === true) {
				result.add(lehrer.id);
			}
		}
		return result;
	}

	protected filterOnDelete(ids: List<number>): List<number> {
		const list = new ArrayList<number>();
		const idsReferenziert = this.getIdsReferenzierterLehrer();
		for (const id of ids) {
			if (!idsReferenziert.contains(id)) {
				list.add(id);
			}
		}
		return list;
	}

	/**
	 * Führt eine Lösch-Operation auf dem Server für die Lehrer mit den übergebenen IDs aus.
	 *
	 * @param ids   die IDs der Lehrer
	 *
	 * @returns eine Promise mit dem Ergebis der Lösch-Operation
	 */
	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteLehrer(ids, api.schema);
	}

	protected deleteMessage(id: number, lehrer: LehrerListeEintrag | null): string {
		return `Lehrer ${(lehrer?.vorname ?? '???') + ' ' + (lehrer?.nachname ?? '???')} (ID: ${id.toString()}) wurde erfolgreich gelöscht.`;
	}

	public deleteCheck(): [boolean, List<string>] {
		const errorLog = new ArrayList<string>();
		const benutzerState = useBenutzerState();
		if (!benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Lehrern vor.');
		}

		for (const id of this.getIdsReferenzierterLehrer()) {
			const lehrer = this.liste.get(id);
			if (lehrer) {
				errorLog.add(`Die Lehrkraft ${lehrer.vorname} ${lehrer.nachname} ist an anderer Stelle referenziert und kann daher nicht gelöscht werden.`);
			}
		}

		return [errorLog.isEmpty(), errorLog];
	};

	public async updateMapStundenplaene(): Promise<void> {
		await this._stundenplaene.update(this.idSchuljahresabschnitt);
	}

	private async createPersonalabschnittsdaten(idLehrer: number): Promise<LehrerPersonalabschnittsdaten> {
		const idSchuljahresabschnitt = this.idSchuljahresabschnitt;
		return await api.server.createLehrerPersonalabschnittsdaten({ idLehrer, idSchuljahresabschnitt }, api.schema);
	}

	public async add(data: Partial<LehrerStammdaten>): Promise<LehrerStammdaten> {
		const result = await api.server.addLehrerStammdaten(data, api.schema);
		await this.createPersonalabschnittsdaten(result.id);
		await this.setSchuljahresabschnitt(this.idSchuljahresabschnitt, true);
		await this.navigator.select(result.id);
		return result;
	};



	public async loadPersonaldaten(): Promise<void> {
		if (!this.stammdaten.hasDaten) {
			return;
		}
		const id = this.selected.id;
		await this._personaldaten.activate(id);
	}

	public async unloadPersonaldaten(): Promise<void> {
		this._personaldaten.deactivate();
		this.commit();
	}

}

export const lehrerAuswahlStateImpl = new LehrerAuswahlStateImpl();
