import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashMap } from "@core/java/util/HashMap";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";
import type { MultiModelProxyConfiguration } from "@ui/model/MultiModelProxy";
import { MultiModelProxy } from "@ui/model/MultiModelProxy";
import { StateManager } from "@ui/ui/StateManager";

import type { GenericAuswahlGruppenprozessHost } from "./GenericAuswahlState";
import { GruppenprozessBereichPatchSperren } from "./GruppenprozessBereichPatchSperren";

/** Der reaktive State eines Bereiches der Gruppenprozesse */
export interface GruppenprozessBereichReactiveState<TDaten> {
	mapDaten: JavaMap<number, TDaten>;
}

/**
 * Der typsichere Schlüssel, unter dem ein Bereich bei den Gruppenprozessen angemeldet ist und abgefragt wird. Die
 * Identität bestimmt das Schlüsselobjekt, jeder Bereich legt dazu genau einen Schlüssel an (static readonly).
 *
 * @typeParam TBereich   der Typ des Bereiches
 */
export class GruppenprozessBereichSchluessel<TBereich extends GruppenprozessBereich<any, any>> {

	/** Dient nur der Typinferenz, der Wert wird nicht gesetzt */
	public readonly bereichTyp?: TBereich;

	/**
	 * der sprechende Name des Bereiches, z.B. "Gruppenprozesse der Schüler: Stammdaten". Er dient nur der
	 * Diagnose, z.B. in Fehlermeldungen, und nicht der Unterscheidung der Schlüssel
	 * */
	public readonly name: string;

	/**
	 * Erzeugt einen neuen Schlüssel.
	 *
	 * @param name   der name des Schlüssels
	 */
	public constructor(name: string) {
		this.name = name;
	}

}

/**
 * Die abstrakte Basisklasse für einen Bereich der Gruppenprozesse zu einer Auswahlliste, z.B. die Stammdaten oder die
 * Personaldaten. Ein Bereich wird dynamisch bei den Gruppenprozessen angemeldet (z.B. beim Betreten des Tabs, der ihn
 * nutzt). Er hält dann die zugehörigen Daten der ausgewählten Einträge, lädt diese nach, wenn der Orchestrator ihn über
 * eine geänderte Mehrfachauswahl informiert, und hält in seinem {@link MultiModelProxy} die ausstehenden Änderungen,
 * die auf mehreren Datensätzen gepatcht werden.
 *
 * @typeParam TEintrag   der Typ der Einträge der Auswahlliste
 * @typeParam TDaten     der Typ der Daten des Bereiches
 */
export abstract class GruppenprozessBereich<TEintrag extends { id: number }, TDaten extends object>
	extends StateManager<GruppenprozessBereichReactiveState<TDaten>> {

	/** Die Funktion, welche die ID zu Daten liefert */
	private readonly _getId: (daten: TDaten) => number;

	/** Der Name des ID-Attributes der Daten */
	private readonly _idField: keyof TDaten;

	/** Der Auswahl-State, an dem der Bereich angemeldet ist, oder null */
	private _auswahl: GenericAuswahlGruppenprozessHost<TEintrag> | null = null;

	/** Der Proxy mit den ausstehenden Änderungen, wird bei Bedarf erzeugt */
	private _model: MultiModelProxy<TDaten> | null = null;

	/** Die Sperren des Speicherns, z.B. wegen ungültiger Eingaben */
	private readonly _patchSperren = new GruppenprozessBereichPatchSperren();


	/**
	 * Erzeugt einen neuen Bereich.
	 *
	 * @param getId   die Funktion, welche die ID zu Daten liefert
	 * @param idField       der Name des ID-Attributes der Daten (Default: "id")
	 */
	protected constructor(getId: (daten: TDaten) => number, idField: keyof TDaten = "id" as keyof TDaten) {
		super({ mapDaten: new HashMap<number, TDaten>() });
		this._getId = getId;
		this._idField = idField;
	}


	/**
	 * Gibt den Schlüssel des Bereiches zurück.
	 *
	 * @returns der Schlüssel
	 */
	public abstract get schluessel(): GruppenprozessBereichSchluessel<any>;

	/**
	 * Gibt die Bezeichnung des Bereiches für die Anzeige zurück, z.B. im Hinweis auf ungespeicherte Änderungen.
	 *
	 * @returns die Bezeichnung
	 */
	public abstract readonly bezeichnung: string;

	/**
	 * Gibt zurück, ob der aktuelle Benutzer den Bereich nutzen darf. Die Route des Tabs prüft dies, bevor sie den Bereich
	 * anmeldet. Ein Bereich ohne Berechtigung wird nicht angemeldet.
	 *
	 * @returns true, wenn der Bereich genutzt werden darf
	 */
	public istBerechtigt(): boolean {
		return true;
	}

	/**
	 * Diese Methode muss überschrieben werden und lädt die Daten zu mehreren Einträgen der Auswahlliste.
	 *
	 * @param ids   die IDs der Einträge, zu denen die Daten geladen werden sollen
	 *
	 * @returns die geladenen Daten oder null
	 */
	protected abstract ladeDatenMultiple(ids: List<number>): Promise<Iterable<TDaten> | null>;

	/**
	 * Diese Methode kann überschrieben werden und sendet die Teilobjekte zum Patchen mehrerer Datensätze an den Server.
	 * Ohne Überschreiben wird das Patchen nicht unterstützt.
	 *
	 * @param partials   die Teilobjekte, je Datensatz eines
	 */
	protected async doPatch(partials: List<Partial<TDaten>>): Promise<void> {
		throw new DeveloperNotificationException("Das Patchen mehrerer Datensätze wird nicht unterstützt.");
	}

	/**
	 * Diese Methode kann überschrieben werden, um einen spezialisierten Proxy zu erzeugen (z.B. mit Anzeige-Mappern und
	 * Bindungen für die UI).
	 *
	 * @param config   die Konfiguration des Proxies
	 *
	 * @returns der Proxy
	 */
	protected createModel(config: MultiModelProxyConfiguration<TDaten>): MultiModelProxy<TDaten> {
		return new MultiModelProxy<TDaten>(config);
	}

	/**
	 * Gibt den Eintrag der Auswahlliste zu der ID zurück.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @returns der Eintrag
	 *
	 * @throws DeveloperNotificationException   wenn der Bereich nicht angemeldet oder der Eintrag nicht in der Auswahlliste enthalten ist
	 */
	protected getEintragOrException(id: number): TEintrag {
		const eintrag = this.auswahlOrException.getEntry(id);
		if (eintrag === null) {
			throw new DeveloperNotificationException(`Der Eintrag mit der ID ${id.toString()} ist nicht in der Auswahlliste enthalten.`);
		}
		return eintrag;
	}

	/**
	 * Gibt den Auswahl-State zurück, an dem der Bereich angemeldet ist.
	 *
	 * @throws DeveloperNotificationException   wenn der Bereich nicht angemeldet ist
	 */
	protected get auswahlOrException(): GenericAuswahlGruppenprozessHost<TEintrag> {
		if (this._auswahl === null) {
			throw new DeveloperNotificationException(`Der Bereich '${this.schluessel.name}' ist nicht angemeldet.`);
		}
		return this._auswahl;
	}


	/**
	 * Gibt den Proxy mit den ausstehenden Änderungen zurück.
	 *
	 * @returns der Proxy
	 */
	public get model(): MultiModelProxy<TDaten> {
		this._model ??= this.createModel({
			data: () => this.mapDaten.values(),
			getId: this._getId,
			idField: this._idField,
			patchMultiple: async partials => await this.patchAndMerge(partials),
		});
		return this._model;
	}

	/**
	 * Gibt die Daten der Mehrfachauswahl zugeordnet zu deren ID zurück.
	 *
	 * @returns die Daten
	 */
	public get mapDaten(): JavaMap<number, TDaten> {
		return this._state.value.mapDaten;
	}

	/**
	 * Sperrt das Speichern der Änderungen des Bereiches, z.B. weil eine Eingabe ungültig ist. Besteht unter dem Schlüssel
	 * bereits eine Sperre, so wird nur deren Hinweis aktualisiert.
	 *
	 * @param schluessel   der Schlüssel der Sperre, z.B. der Name des betroffenen Feldes
	 * @param hinweis      der Hinweis für den Benutzer
	 */
	public denyPatch(schluessel: string, hinweis: string): void {
		this._patchSperren.deny(schluessel, hinweis);
	}

	/**
	 * Hebt die Sperre des Speicherns zum Schlüssel auf. Ohne Sperre unter dem Schlüssel passiert nichts.
	 *
	 * @param schluessel   der Schlüssel der Sperre
	 */
	public allowPatch(schluessel: string): void {
		this._patchSperren.allow(schluessel);
	}

	/**
	 * Gibt zurück, ob die ausstehenden Änderungen des Bereiches gespeichert werden dürfen, d.h. ob keine Sperre vorliegt.
	 *
	 * @returns true, wenn das Speichern möglich ist
	 */
	public get isPatchAllowed(): boolean {
		return this._patchSperren.isPatchAllowed;
	}

	/**
	 * Gibt die Hinweise der Sperren des Speicherns zurück, um dem Benutzer zu zeigen, warum das Speichern nicht möglich ist.
	 *
	 * @returns die Hinweise, leer wenn das Speichern möglich ist
	 */
	public get patchHinweise(): string[] {
		return this._patchSperren.hinweise;
	}

	/**
	 * Gibt zurück, ob im Bereich ausstehende Änderungen vorliegen.
	 *
	 * @returns true, wenn es ausstehende Änderungen gibt
	 */
	public get hasPending(): boolean {
		return this.model.hasPending;
	}

	/**
	 * Patcht die ausstehenden Änderungen des Bereiches für alle Datensätze der Mehrfachauswahl. Bei Erfolg werden sie
	 * verworfen, bei einem Fehler bleiben sie erhalten und der Fehler wird weitergegeben.
	 */
	public async patchMultiple(): Promise<void> {
		await this.model.patchMultiple();
	}

	/**
	 * Verwirft die ausstehenden Änderungen des Bereiches und hebt dessen Sperren des Speicherns auf.
	 */
	public clearPending(): void {
		this._model?.clearPending();
		this._patchSperren.allowAll();
	}

	/**
	 * Gibt zurück, ob der Bereich an einem Auswahl-State angemeldet ist.
	 *
	 * @returns true, wenn er angemeldet ist
	 */
	public get istAngemeldet(): boolean {
		return this._auswahl !== null;
	}

	/**
	 * Meldet den Bereich am Auswahl-State an: Die Daten der aktuellen Mehrfachauswahl werden geladen. Ab dann informiert
	 * der Orchestrator den Bereich über Änderungen der Mehrfachauswahl.
	 *
	 * @param auswahl       der Auswahl-State, aus dem der Bereich Einträge der Auswahlliste liest
	 * @param idsAuswahl    die IDs der aktuellen Mehrfachauswahl
	 */
	public async anmelden(auswahl: GenericAuswahlGruppenprozessHost<TEintrag>, idsAuswahl: List<number>): Promise<void> {
		this._auswahl = auswahl;
		const apply = await this.loadForMultiSelection(idsAuswahl);
		apply();
	}

	/**
	 * Meldet den Bereich vom Auswahl-State ab und verwirft die Daten, die ausstehenden Änderungen und die Sperren.
	 */
	public abmelden(): void {
		this._auswahl = null;
		this.clearPending();
		this.setPatchedDefaultState({ mapDaten: new HashMap<number, TDaten>() });
	}

	/**
	 * Lädt die Daten der übergebenen Mehrfachauswahl, ohne sie zu übernehmen. Es werden nur die Daten nachgeladen, die
	 * noch nicht vorliegen. Daten von Einträgen, die nicht mehr ausgewählt sind, werden bei der Übernahme entfernt. Ändert
	 * sich dadurch die Menge der ausgewählten Einträge, so werden die ausstehenden Änderungen und die Sperren verworfen.
	 *
	 * @param idsAuswahl   die IDs der Einträge der Mehrfachauswahl
	 *
	 * @returns eine Promise mit der Funktion, welche die geladenen Daten übernimmt
	 */
	public async loadForMultiSelection(idsAuswahl: List<number>): Promise<() => void> {
		const currentSelection = this._state.value.mapDaten;
		const newSelection = new HashMap<number, TDaten>();
		const delta = new ArrayList<number>();
		for (const id of idsAuswahl) {
			const daten = currentSelection.get(id);
			if (daten === null) {
				delta.add(id);
			} else {
				newSelection.put(id, daten);
			}
		}

		let deltaDaten: Iterable<TDaten> = [];
		if (!delta.isEmpty()) {
			const geladen = await this.ladeDatenMultiple(delta);
			if (geladen === null) {
				throw new DeveloperNotificationException("Fehler beim Laden der Daten. Es konnten keine Daten zu den ausgewählten Einträgen geladen werden.");
			}
			deltaDaten = geladen;
		}

		return () => {
			for (const daten of deltaDaten) {
				newSelection.put(this._getId(daten), daten);
			}
			// Die ausstehenden Änderungen gelten nur für die Auswahl, für die sie gesetzt wurden
			if (!GruppenprozessBereich.hasSameKeys(this._state.value.mapDaten, newSelection)) {
				this.clearPending();
			}
			this.setPatchedState({ mapDaten: newSelection });
		};
	}

	/**
	 * Vergleicht, ob zwei Maps dieselben Schlüssel haben.
	 *
	 * @param a   die erste Map
	 * @param b   die zweite Map
	 *
	 * @returns true, wenn beide Maps dieselben Schlüssel haben
	 */
	private static hasSameKeys(a: JavaMap<number, unknown>, b: JavaMap<number, unknown>): boolean {
		if (a.size() !== b.size()) {
			return false;
		}
		for (const id of a.keySet()) {
			if (!b.containsKey(id)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Sendet die Teilobjekte an den Server und übernimmt die geänderten Werte in die Daten der Mehrfachauswahl, damit
	 * nicht alle Daten neu geladen werden müssen.
	 *
	 * @param partials   die Teilobjekte, je Datensatz eines
	 */
	private async patchAndMerge(partials: List<Partial<TDaten>>): Promise<void> {
		await this.doPatch(partials);
		for (const partial of partials) {
			const id = partial[this._idField] as number;
			const aktuell = this.mapDaten.get(id);
			if (aktuell !== null) {
				this.mapDaten.put(id, { ...aktuell, ...partial });
			}
		}
		this.commit();
	}

}
