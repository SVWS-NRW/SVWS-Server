
import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KlassenDatenMinimal } from "@core/asd/data/klassen/KlassenDatenMinimal";
import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { LehrerUtils } from "@core/core/utils/lehrer/LehrerUtils";
import { SchuelerUtils } from "@core/core/utils/schueler/SchuelerUtils";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { useAbschnittState } from "@ui/states/AbschnittState";
import { JahrgaengeListeManager } from "@ui/ui/manager/kataloge/JahrgaengeListeManager";
import { ListeMitAuswahl } from "@ui/ui/manager/ListeMitAuswahl";

import { abschnittStateImpl } from "../AbschnittStateImpl";
import { AuswahlKatalog } from "../AuswahlKatalog";
import type { GenericAuswahlReactiveState } from "../GenericAuswahlStateImpl";
import { GenericAuswahlStateImpl } from "../GenericAuswahlStateImpl";
import { SchuelerStatusFilter } from "../SchuelerStatusFilter";
import { schuleStateImpl } from "../SchuleStateImpl";
import { StundenplaeneListeState } from "../StundenplaeneListeState";
import { api } from "~/router/Api";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteManager } from "~/router/RouteManager";

import { comparatorKlassenListeEintrag, KlassenAuswahlFilter } from "./KlassenAuswahlFilter";
import type { KlassenAuswahlState } from "./KlassenAuswahlState";
import { klassenAuswahlStateRoutingAdapter } from "./KlassenAuswahlStateRoutingAdapter";
import { KlassenGruppenprozessState } from "./KlassenGruppenprozessState";
import { KlassenStammdatenState } from "./KlassenStammdatenState";

type KlassenAuswahlReactiveState = GenericAuswahlReactiveState<KlassenListeEintrag>;

/**
 * Der State für die Auswahlliste der Klassen
 */
export class KlassenAuswahlStateImpl extends GenericAuswahlStateImpl<KlassenListeEintrag, KlassenDaten, KlassenAuswahlReactiveState> implements KlassenAuswahlState {

	/** Der Katalog der Jahrgänge */
	private readonly _jahrgaenge = new AuswahlKatalog<JahrgangsDaten>(j => j.id, JahrgaengeListeManager.comparator);

	/** Der Katalog der Schüler des Schuljahresabschnittes */
	private readonly _schueler = new AuswahlKatalog<SchuelerListeEintrag>(s => s.id, SchuelerUtils.comparator);

	/** Der Katalog der Klassen des vorigen Schuljahresabschnittes in der Reihenfolge der Server-Antwort */
	private readonly _klassenByIdVorabschnitt = new AuswahlKatalog<KlassenDatenMinimal>(k => k.id, { compare: () => 0 });

	/** Der Katalog der Klassen des folgenden Schuljahresabschnittes in der Reihenfolge der Server-Antwort */
	private readonly _klassenByIdFolgeAbschnitt = new AuswahlKatalog<KlassenDatenMinimal>(k => k.id, { compare: () => 0 });

	/** Der Teil-State für die Stundenpläne des Schuljahresabschnittes */
	private readonly _stundenplaene = new StundenplaeneListeState();

	/** Der Filter nach dem Schüler-Status für die Schülerliste in den Daten */
	private readonly _schuelerstatus = new SchuelerStatusFilter([SchuelerStatus.AKTIV, SchuelerStatus.EXTERN, SchuelerStatus.NEUAUFNAHME, SchuelerStatus.WARTELISTE]);

	/** Der State für die Lehrer des Schuljahresabschnittes */
	private readonly _lehrer = new AuswahlKatalog<LehrerListeEintrag>(l => l.id, LehrerUtils.comparator);

	/** Der State für die Stammdaten */
	private readonly _stammdaten = new KlassenStammdatenState(this);

	/** Der State für die Stammdaten der Mehrfachauswahl */
	private readonly _gruppenprozesse = new KlassenGruppenprozessState(this);

	/** Der Filter der Auswahlliste */
	private readonly _filter = new KlassenAuswahlFilter(
		() => this._state.value.liste?.list() ?? new ArrayList<KlassenListeEintrag>(),
		() => this.idSelected,
		{ schule: schuleStateImpl, jahrgaenge: this._jahrgaenge, lehrer: this._lehrer }
	);

	public constructor() {
		super({
			idSchuljahresabschnitt: -1,
			liste: undefined,
			idSelected: null,
		}, klassenAuswahlStateRoutingAdapter);
		this._filter.onChanged = () => void this.setFilter();
	}

	/**
	 * Gibt den Filter der Auswahlliste zurück.
	 *
	 * @returns der Filter
	 */
	public get filter(): KlassenAuswahlFilter {
		return this._filter;
	}

	public get schuljahresabschnitt(): Schuljahresabschnitt {
		return useAbschnittState().get(this.selected.idSchuljahresabschnitt);
	}

	public get jahrgaenge(): AuswahlKatalog<JahrgangsDaten> {
		return this._jahrgaenge;
	}

	public get klassenByIdVorabschnitt(): AuswahlKatalog<KlassenDatenMinimal> {
		return this._klassenByIdVorabschnitt;
	}

	public get klassenByIdFolgeAbschnitt(): AuswahlKatalog<KlassenDatenMinimal> {
		return this._klassenByIdFolgeAbschnitt;
	}

	public getKlassenIDsMitSchuelern(): JavaSet<number> {
		const result = new HashSet<number>();
		for (const klasse of this.liste.auswahl()) {
			if (klasse.anzahlZugeordneteSchueler !== 0) {
				result.add(klasse.id);
			}
		}
		return result;
	}

	public getSchuelerIDsDerAuswahl(): List<number> {
		const result = new ArrayList<number>();
		for (const s of this._schueler.list()) {
			if (this.liste.has(s.idKlasse) && this.liste.auswahlHasKey(s.idKlasse)) {
				result.add(s.id);
			}
		}
		return result;
	}

	public get stammdaten(): KlassenStammdatenState {
		return this._stammdaten;
	}

	public get gruppenprozesse(): KlassenGruppenprozessState {
		return this._gruppenprozesse;
	}

	public get schuelerstatus(): SchuelerStatusFilter {
		return this._schuelerstatus;
	}

	public get lehrer(): AuswahlKatalog<LehrerListeEintrag> {
		return this._lehrer;
	}

	public commit(): void {
		super.commit();
		this._filter.refresh();
	}

	protected getListFiltered(): List<KlassenListeEintrag> {
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
		this._lehrer.reset();
		this._jahrgaenge.reset();
		this._schueler.reset();
		this._klassenByIdVorabschnitt.reset();
		this._klassenByIdFolgeAbschnitt.reset();
		this._schuelerstatus.reset();
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

	/**
	 * Erstellt die Auswahlliste für den angegebenen Schuljahresabschnitt. Die Daten dafür werden
	 * über die API abgefragt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @returns Eine Promise mit den Anpassungen für die Auswahlliste und deren Daten im KlassenState
	 */
	protected async createListe(idSchuljahresabschnitt: number): Promise<Partial<KlassenAuswahlReactiveState>> {
		const schuljahresabschnitt = abschnittStateImpl.getOrNull(idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		}
		// Lade die Kataloge und erstelle die Auswahlliste

		const [klassen, klassenVorabschnitt, klassenFolgeabschnitt, schueler, lehrer, jahrgaenge] = await Promise.all([
			api.server.getListKlassenListeEintragBySchuljahresabschnitt(api.schema, idSchuljahresabschnitt),
			this.getKlassenVorabschnitt(schuljahresabschnitt),
			this.getKlassenFolgeabschnitt(schuljahresabschnitt),
			api.server.getSchuelerFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getLehrerFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getJahrgaenge(api.schema),
		])
		;
		const liste = new ListeMitAuswahl(klassen, e => e.id, comparatorKlassenListeEintrag, null);
		this._lehrer.set(lehrer);
		this._jahrgaenge.set(jahrgaenge);
		this._schueler.set(schueler);
		this._klassenByIdVorabschnitt.set(klassenVorabschnitt);
		this._klassenByIdFolgeAbschnitt.set(klassenFolgeabschnitt);
		this._filter.pruneValues();
		// Die Stundenpläne gehören zum Schuljahresabschnitt und werden bei dessen Wechsel verworfen
		this._stundenplaene.reset();
		return { liste };
	}

	/**
	 * Ermittelt die Klassen für den vorigen Schuljahresabschnitt
	 *
	 * @param schuljahresabschnitt   der aktuelle Schuljahresabschnitt, von dem ausgegangen wird
	 *
	 * @returns eine Promise mit der Liste der Klassen
	 */
	private getKlassenVorabschnitt(schuljahresabschnitt: Schuljahresabschnitt) {
		return (schuljahresabschnitt.idVorigerAbschnitt === null)
			? Promise.resolve(new ArrayList<KlassenDatenMinimal>())
			: api.server.getKlassenDatenMinimalBySchuljahresabschnitt(api.schema, schuljahresabschnitt.idVorigerAbschnitt);
	}

	/**
	 * Ermittelt die Klassen für den folgenden Schuljahresabschnitt
	 *
	 * @param schuljahresabschnitt   der folgende Schuljahresabschnitt, von dem ausgegangen wird
	 *
	 * @returns eine Promise mit der Liste der Klassen
	 */
	private getKlassenFolgeabschnitt(schuljahresabschnitt: Schuljahresabschnitt) {
		return (schuljahresabschnitt.idFolgeAbschnitt === null)
			? Promise.resolve(new ArrayList<KlassenDatenMinimal>())
			: api.server.getKlassenDatenMinimalBySchuljahresabschnitt(api.schema, schuljahresabschnitt.idFolgeAbschnitt);
	}

	/**
	 * Führt eine LöscheOperation auf dem Server für die Klassen mit den übergebenen IDs aus.
	 *
	 * @param ids   die IDs der Klassen
	 *
	 * @returns eine Promise mit dem Ergebis der Lösch-Operation
	 */
	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteKlassen(ids, api.schema);
	}

	protected filterOnDelete(ids: List<number>): List<number> {
		const neueIDs = new ArrayList<number>();
		const set = this.getKlassenIDsMitSchuelern();
		for (const id of ids) {
			if (!set.contains(id)) {
				neueIDs.add(id);
			}
		}
		return neueIDs;
	}

	protected deleteMessage(id: number, klasse: KlassenListeEintrag | null): string {
		return `Klasse ${klasse?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public async updateMapStundenplaene(): Promise<void> {
		await this._stundenplaene.update(this.idSchuljahresabschnitt);
	}

	public async setzeDefaultSortierung(): Promise<void> {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahl_id = this.selected.id;
		await api.server.setKlassenSortierungFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		await this.setSchuljahresabschnitt(idSchuljahresabschnitt, true);
		await this.setDaten(this.liste.get(auswahl_id));
	};

	public async add(partialKlasse: Partial<KlassenDaten>): Promise<void> {
		const neueKlasse = await api.server.addKlasse({ ...partialKlasse, idSchuljahresabschnitt: abschnittStateImpl.auswahl.id }, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.navigator.select(neueKlasse.id);
	};

	public async gotoSchueler(eintrag: Schueler): Promise<void> {
		await RouteManager.doRoute(routeSchueler.getRoute({ id: eintrag.id }));
	};

	public async gotoLehrer(eintrag: LehrerListeEintrag): Promise<void> {
		await RouteManager.doRoute(routeLehrer.getRoute({ id: eintrag.id }));
	};

}

export const klassenAuswahlStateImpl = new KlassenAuswahlStateImpl();
