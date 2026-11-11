
import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { SchuelerNeu } from "@core/asd/data/schueler/SchuelerNeu";
import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { GostAbiturjahrUtils } from "@core/core/utils/gost/GostAbiturjahrUtils";
import { KlassenUtils } from "@core/core/utils/klassen/KlassenUtils";
import { KursUtils } from "@core/core/utils/kurse/KursUtils";
import { LehrerUtils } from "@core/core/utils/lehrer/LehrerUtils";
import { SchuelerUtils } from "@core/core/utils/schueler/SchuelerUtils";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { useAbschnittState } from "@ui/states/AbschnittState";
import { useBenutzerState } from "@ui/states/BenutzerState";
import { JahrgaengeListeManager } from "@ui/ui/manager/kataloge/JahrgaengeListeManager";
import { ListeMitAuswahl } from "@ui/ui/manager/ListeMitAuswahl";

import { abschnittStateImpl } from "../AbschnittStateImpl";
import { AuswahlKatalog } from "../AuswahlKatalog";
import type { GenericAuswahlReactiveState } from "../GenericAuswahlStateImpl";
import { GenericAuswahlStateImpl } from "../GenericAuswahlStateImpl";
import { StundenplaeneListeState } from "../StundenplaeneListeState";
import { api } from "~/router/Api";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

import { SchuelerAuswahlFilter } from "./SchuelerAuswahlFilter";
import type { SchuelerAuswahlState } from "./SchuelerAuswahlState";
import { schuelerAuswahlStateRoutingAdapter } from "./SchuelerAuswahlStateRoutingAdapter";
import { SchuelerGruppenprozessState } from "./SchuelerGruppenprozessState";
import { SchuelerStammdatenState } from "./SchuelerStammdatenState";

type SchuelerAuswahlReactiveState = GenericAuswahlReactiveState<SchuelerListeEintrag>;

/**
 * Der State für die Auswahlliste der Schüler
 */
export class SchuelerAuswahlStateImpl extends GenericAuswahlStateImpl<SchuelerListeEintrag, SchuelerStammdaten, SchuelerAuswahlReactiveState> implements SchuelerAuswahlState {

	/** Der Katalog der Jahrgänge */
	private readonly _jahrgaenge = new AuswahlKatalog<JahrgangsDaten>(j => j.id, JahrgaengeListeManager.comparator);

	/** Der Katalog der Klassen des Schuljahresabschnittes */
	private readonly _klassen = new AuswahlKatalog<KlassenDaten>(k => k.id, KlassenUtils.comparator);

	/** Der Katalog aller Klassen der Schülerliste, auch der Klassen anderer Schuljahresabschnitte, in der Reihenfolge der Server-Antwort */
	private readonly _klassenAlle = new AuswahlKatalog<KlassenDaten>(k => k.id, { compare: () => 0 });

	/** Der Katalog der Kurse */
	private readonly _kurse = new AuswahlKatalog<KursDaten>(k => k.id, KursUtils.comparator);

	/** Der Katalog der Abiturjahrgänge des Schuljahresabschnittes */
	private readonly _abiturjahrgaenge = new AuswahlKatalog<GostJahrgang>(a => a.abiturjahr, GostAbiturjahrUtils.comparator);

	/** Der Teil-State für die Stundenpläne des Schuljahresabschnittes */
	private readonly _stundenplaene = new StundenplaeneListeState();

	/** Der State für die Lehrer */
	private readonly _lehrer = new AuswahlKatalog<LehrerListeEintrag>(l => l.id, LehrerUtils.comparator);

	/** Der State für die Stammdaten */
	private readonly _stammdaten = new SchuelerStammdatenState(this);

	/** Der State für die Stammdaten der Mehrfachauswahl */
	private readonly _gruppenprozesse = new SchuelerGruppenprozessState(this);

	/** Der Filter der Auswahlliste */
	private readonly _filter = new SchuelerAuswahlFilter(
		() => this._state.value.liste?.list() ?? new ArrayList<SchuelerListeEintrag>(),
		() => this.idSelected,
		{ schule: schuleStateImpl, klassen: this._klassen, klassenAlle: this._klassenAlle, jahrgaenge: this._jahrgaenge, kurse: this._kurse, idSchuljahresabschnitt: () => this.idSchuljahresabschnitt }
	);

	public constructor() {
		super({
			idSchuljahresabschnitt: -1,
			liste: undefined,
			idSelected: null,
		}, schuelerAuswahlStateRoutingAdapter);
		this._filter.onChanged = () => void this.setFilter();
	}

	/**
	 * Gibt den Filter der Auswahlliste zurück.
	 *
	 * @returns der Filter
	 */
	public get filter(): SchuelerAuswahlFilter {
		return this._filter;
	}

	public get schuljahresabschnitt(): Schuljahresabschnitt {
		return useAbschnittState().get(this.selected.idSchuljahresabschnitt);
	}

	public get schuljahresabschnittSchueler(): Schuljahresabschnitt {
		return useAbschnittState().get(this.selected.idSchuljahresabschnittSchueler);
	}

	public get stammdaten(): SchuelerStammdatenState {
		return this._stammdaten;
	}

	public get gruppenprozesse(): SchuelerGruppenprozessState {
		return this._gruppenprozesse;
	}

	public schuelerIstImSchuljahresabschnitt(idSchueler: number): boolean {
		const schueler = this.liste.get(idSchueler);
		return (schueler !== null) && (schueler.idSchuljahresabschnittSchueler === this.idSchuljahresabschnitt);
	}

	public schuelerSchuljahresabschnittAsString(idSchueler: number): string {
		const schueler = this.liste.get(idSchueler);
		const schuljahresabschnitt = (schueler === null) ? null : useAbschnittState().getOrNull(schueler.idSchuljahresabschnittSchueler);
		return (schuljahresabschnitt === null) ? "----.-" : schuljahresabschnitt.schuljahr + "." + schuljahresabschnitt.abschnitt;
	}

	public get jahrgaenge(): AuswahlKatalog<JahrgangsDaten> {
		return this._jahrgaenge;
	}

	public get klassen(): AuswahlKatalog<KlassenDaten> {
		return this._klassen;
	}

	public get abiturjahrgaenge(): AuswahlKatalog<GostJahrgang> {
		return this._abiturjahrgaenge;
	}

	public klasseGetOrNull(idKlasse: number): KlassenDaten | null {
		return this._klassenAlle.get(idKlasse);
	}

	public get lehrer(): AuswahlKatalog<LehrerListeEintrag> {
		return this._lehrer;
	}

	public commit(): void {
		super.commit();
		this._filter.refresh();
	}

	protected getListFiltered(): List<SchuelerListeEintrag> {
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
		this._klassen.reset();
		this._klassenAlle.reset();
		this._kurse.reset();
		this._abiturjahrgaenge.reset();
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
	 * @returns Eine Promise mit den Anpassungen für die Auswahlliste und deren Daten im reaktiven State
	 */
	protected async createListe(idSchuljahresabschnitt: number): Promise<Partial<SchuelerAuswahlReactiveState>> {
		const schuljahresabschnitt = abschnittStateImpl.getOrNull(idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		}

		// Lade die Daten von der API
		const [schuelerListe, lehrer] = await Promise.all([
			api.server.getSchuelerAuswahllisteFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getLehrer(api.schema),
		]);

		this._lehrer.set(lehrer);
		this._jahrgaenge.set(schuelerListe.jahrgaenge);
		this._klassenAlle.set(schuelerListe.klassen);
		this._klassen.set([...schuelerListe.klassen].filter(klasse => klasse.idSchuljahresabschnitt === schuelerListe.idSchuljahresabschnitt));
		this._kurse.set(schuelerListe.kurse);
		this._abiturjahrgaenge.set(schuelerListe.jahrgaengeGost);

		// Erstelle die Auswahlliste
		const liste = new ListeMitAuswahl(schuelerListe.schueler, e => e.id, SchuelerUtils.comparator, null);

		// Der Filter bleibt beim Wechsel des Schuljahresabschnittes erhalten, beim ersten Laden werden aktive und externe Schüler gefiltert
		this._filter.pruneValues();
		if (this._state.value.liste === undefined) {
			this._filter.setValueSilent("schuelerstatus", this._filter.defaultStatus);
		}
		this._filter.setValueSilent("nurMitLernabschnitt", true);

		// Die Stundenpläne gehören zum Schuljahresabschnitt und werden bei dessen Wechsel verworfen
		this._stundenplaene.reset();
		return { liste };
	}

	/**
	 * Führt eine LöscheOperation auf dem Server für die Schüler mit den übergebenen IDs aus.
	 *
	 * @param ids   die IDs der Schüler
	 *
	 * @returns eine Promise mit dem Ergebis der Lösch-Operation
	 */
	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.softDeleteSchuelerStammdatenMultiple(ids, api.schema);
	}

	protected deleteMessage(id: number, schueler: SchuelerListeEintrag | null): string {
		return `Der Löschvermerk des Schülers ${(schueler?.vorname ?? '???') + ' ' + (schueler?.nachname ?? '???')} (ID: ${id.toString()}) wurde erfolgreich gesetzt.`;
	}

	public deleteCheck(): [boolean, List<string>] {
		const errorLog = new ArrayList<string>();
		const benutzerState = useBenutzerState();
		if (!benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LOESCHEN)) {
			errorLog.add('Es liegt keine Berechtigung zum Löschen von Schülern vor.');
		}
		return [errorLog.isEmpty(), errorLog];
	};



	public async updateMapStundenplaene(): Promise<void> {
		await this._stundenplaene.update(this.idSchuljahresabschnitt);
	}

	public async add(data: Partial<SchuelerNeu>): Promise<SchuelerStammdaten> {
		const result = await api.server.addSchueler(data, api.schema);
		await this.setSchuljahresabschnitt(data.idSchuljahresabschnitt ?? this.idSchuljahresabschnitt, true);
		await this.setDaten(this.liste.get(result.id));
		this.commit();
		return result;
	};

}

export const schuelerAuswahlStateImpl = new SchuelerAuswahlStateImpl();
