
import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { KursUtils } from "@core/core/utils/kurse/KursUtils";
import { LehrerUtils } from "@core/core/utils/lehrer/LehrerUtils";
import { SchuelerUtils } from "@core/core/utils/schueler/SchuelerUtils";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import { ArrayList } from "@core/java/util/ArrayList";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { List } from "@core/java/util/List";
import { useAbschnittState } from "@ui/states/AbschnittState";
import { useConfigState } from "@ui/states/ConfigState";
import { JahrgaengeListeManager } from "@ui/ui/manager/kataloge/JahrgaengeListeManager";
import { ListeMitAuswahl } from "@ui/ui/manager/ListeMitAuswahl";

import { AuswahlKatalog } from "../AuswahlKatalog";
import type { GenericAuswahlReactiveState } from "../GenericAuswahlStateImpl";
import { GenericAuswahlStateImpl } from "../GenericAuswahlStateImpl";
import { SchuelerStatusFilter } from "../SchuelerStatusFilter";
import { api } from "~/router/Api";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteManager } from "~/router/RouteManager";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

import { KurseAuswahlFilter } from "./KurseAuswahlFilter";
import type { KurseAuswahlState } from "./KurseAuswahlState";
import { kurseAuswahlStateRoutingAdapter } from "./KurseAuswahlStateRoutingAdapter";
import { KurseGruppenprozessState } from "./KurseGruppenprozessState";
import { KurseStammdatenState } from "./KurseStammdatenState";

/** Der Comparator für den Vergleich von Fächern in Fächerlisten */
const comparatorFaecher: Comparator<FachDaten> = { compare: (a: FachDaten, b: FachDaten) => {
	const cmp = a.sortierung - b.sortierung;
	if (cmp !== 0) {
		return cmp;
	}
	const cmpKuerzel = JavaString.compareTo(a.kuerzel, b.kuerzel);
	return (cmpKuerzel === 0) ? JavaLong.compare(a.id, b.id) : cmpKuerzel;
} };

interface KurseAuswahlReactiveState extends GenericAuswahlReactiveState<KursDaten> {
}

/**
 * Der State für die Auswahlliste der Kurse
 */
export class KurseAuswahlStateImpl extends GenericAuswahlStateImpl<KursDaten, KursDaten, KurseAuswahlReactiveState> implements KurseAuswahlState {

	/** Der Katalog der Jahrgänge */
	private readonly _jahrgaenge = new AuswahlKatalog<JahrgangsDaten>(j => j.id, JahrgaengeListeManager.comparator);

	/** Der Katalog der Fächer, die sichtbar oder in einem Kurs des Schuljahresabschnittes verwendet sind */
	private readonly _faecher = new AuswahlKatalog<FachDaten>(f => f.id, comparatorFaecher);

	/** Der Katalog der Schüler des Schuljahresabschnittes */
	private readonly _schueler = new AuswahlKatalog<SchuelerListeEintrag>(s => s.id, SchuelerUtils.comparator);

	/** Der Filter nach dem Schüler-Status für die Schülerliste in den Daten */
	private readonly _schuelerstatus = new SchuelerStatusFilter([SchuelerStatus.AKTIV, SchuelerStatus.EXTERN]);

	/** Der State für die Lehrer des Schuljahresabschnittes */
	private readonly _lehrer = new AuswahlKatalog<LehrerListeEintrag>(l => l.id, LehrerUtils.comparatorKuerzel);

	/** Der State für die Stammdaten */
	private readonly _stammdaten = new KurseStammdatenState(this);

	/** Der State für die Stammdaten der Mehrfachauswahl */
	private readonly _gruppenprozesse = new KurseGruppenprozessState(this);

	/** Der Filter der Auswahlliste */
	private readonly _filter = new KurseAuswahlFilter(
		() => this._state.value.liste?.list() ?? new ArrayList<KursDaten>(),
		() => this.idSelected,
		{ schule: schuleStateImpl, jahrgaenge: this._jahrgaenge, lehrer: this._lehrer, faecher: this._faecher, schueler: this._schueler }
	);

	public constructor() {
		super({
			idSchuljahresabschnitt: -1,
			liste: undefined,
			idSelected: null,
		}, kurseAuswahlStateRoutingAdapter);
		this._filter.onChanged = () => {
			// Der Schalter "Nur Sichtbare" wird in der Konfiguration des Benutzers gespeichert
			if (this._filter.values.nurSichtbar !== this.filterNurSichtbar) {
				void this.setFilterNurSichtbar(this._filter.values.nurSichtbar);
			}
			void this.setFilter();
		};
	}

	/**
	 * Gibt den Filter der Auswahlliste zurück.
	 *
	 * @returns der Filter
	 */
	public get filter(): KurseAuswahlFilter {
		return this._filter;
	}

	public get schuelerstatus(): SchuelerStatusFilter {
		return this._schuelerstatus;
	}

	public get lehrer(): AuswahlKatalog<LehrerListeEintrag> {
		return this._lehrer;
	}

	public get schuljahresabschnitt(): Schuljahresabschnitt {
		return useAbschnittState().get(this.selected.idSchuljahresabschnitt);
	}

	public get jahrgaenge(): AuswahlKatalog<JahrgangsDaten> {
		return this._jahrgaenge;
	}

	public get faecher(): AuswahlKatalog<FachDaten> {
		return this._faecher;
	}

	public get stammdaten(): KurseStammdatenState {
		return this._stammdaten;
	}

	public get gruppenprozesse(): KurseGruppenprozessState {
		return this._gruppenprozesse;
	}

	public commit(): void {
		super.commit();
		this._filter.refresh();
	}

	protected getListFiltered(): List<KursDaten> {
		return this._filter.filtered;
	}

	/**
	 * Setzt den State und den Filter in den Default-State zurück.
	 */
	public reset(): void {
		super.reset();
		this._stammdaten.reset();
		this._gruppenprozesse.reset();
		this._lehrer.reset();
		this._jahrgaenge.reset();
		this._faecher.reset();
		this._schueler.reset();
		this._schuelerstatus.reset();
		this._filter.reset();
	}

	public get filterNurSichtbar(): boolean {
		const configState = useConfigState();
		return configState.config.getValue("kurse.auswahl.filterNurSichtbar") === 'true';
	}

	public async setFilterNurSichtbar(value: boolean): Promise<void> {
		const configState = useConfigState();
		await configState.config.setValue('kurse.auswahl.filterNurSichtbar', value ? "true" : "false");
	};

	/**
	 * Erstellt die Auswahlliste für den angegebenen Schuljahresabschnitt. Die Daten dafür werden
	 * über die API abgefragt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @returns Eine Promise mit den Anpassungen für die Auswahlliste und deren Daten im KurseAuswahlState
	 */
	protected async createListe(idSchuljahresabschnitt: number): Promise<Partial<KurseAuswahlReactiveState>> {
		const abschnittState = useAbschnittState();
		const schuljahresabschnitt = abschnittState.getOrNull(idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		}

		// Lade die Kataloge und erstelle die Auswahlliste
		const [listKurse, listSchueler, listJahrgaenge, listLehrer, listFaecher] = await Promise.all([
			api.server.getKurseFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getSchuelerFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getJahrgaenge(api.schema),
			api.server.getLehrerFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getFaecher(api.schema),
		]);

		const liste = new ListeMitAuswahl(listKurse, e => e.id, KursUtils.comparator, null);
		// Beim ersten Laden wird der Schalter "Nur Sichtbare" aus der Konfiguration übernommen, ansonsten bleibt der Filter erhalten
		if (this._state.value.liste === undefined) {
			this._filter.setValueSilent("nurSichtbar", this.filterNurSichtbar);
		}
		this._lehrer.set(listLehrer);
		this._jahrgaenge.set(listJahrgaenge);
		this._schueler.set(listSchueler);
		const idsFaecherInKursen = new HashSet<number>();
		for (const kurs of listKurse) {
			idsFaecherInKursen.add(kurs.idFach);
		}
		this._faecher.set([...listFaecher].filter(fach => fach.istSichtbar || idsFaecherInKursen.contains(fach.id)));
		this._filter.pruneValues();
		return { liste };
	}

	/**
	 * Führt eine Lösch-Operation auf dem Server für die Kurse mit den übergebenen IDs aus.
	 *
	 * @param ids   die IDs der Kurse
	 *
	 * @returns eine Promise mit dem Ergebis der Lösch-Operation
	 */
	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteKurse(ids, api.schema);
	}

	protected deleteMessage(id: number, kurs: KursDaten | null): string {
		return `Kurs ${kurs?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public async add(partialKurs: Partial<KursDaten>): Promise<void> {
		const abschnittState = useAbschnittState();
		const neuerKurs = await api.server.addKurs({ ...partialKurs, idSchuljahresabschnitt: abschnittState.auswahl.id }, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.navigator.select(neuerKurs.id);
	};

	public async gotoSchueler(eintrag: Schueler): Promise<void> {
		await RouteManager.doRoute(routeSchueler.getRoute({ id: eintrag.id }));
	};

}

export const kurseAuswahlStateImpl = new KurseAuswahlStateImpl();
