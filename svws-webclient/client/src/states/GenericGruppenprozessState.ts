import type { ShallowRef } from "vue";
import { shallowRef } from "vue";

import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { List } from "@core/java/util/List";
import { NavigationGuardQuery } from "@ui/ui/modal/NavigationGuardQuery";

import type { GenericAuswahlGruppenprozessHost } from "./GenericAuswahlState";
import type { GruppenprozessBereich, GruppenprozessBereichSchluessel } from "./GruppenprozessBereich";

/**
 * Der Orchestrator der Gruppenprozesse eines Auswahl-States. Er hält keine Fachdaten, sondern die dynamisch
 * angemeldeten Bereiche (z.B. Stammdaten, Personaldaten) und bietet die Sicht über alle Bereiche: ausstehende
 * Änderungen, Speichern und Verwerfen. Der Auswahl-State besitzt den Orchestrator, die Tab-Routen melden die Bereiche
 * beim Betreten an.
 *
 * @typeParam TAuswahl   der Typ der Einträge der Auswahlliste
 */
export class GenericGruppenprozessState<TAuswahl extends { id: number }> {

	/** Die angemeldeten Bereiche, zugeordnet zu ihrem Schlüssel */
	private readonly _bereiche: ShallowRef<Map<GruppenprozessBereichSchluessel<any>, GruppenprozessBereich<TAuswahl, any>>>
		= shallowRef(new Map<GruppenprozessBereichSchluessel<any>, GruppenprozessBereich<TAuswahl, any>>());

	/** Die Abfrage, mit der der Navigation-Guard beim Verlassen mit ausstehenden Änderungen den Benutzer fragt */
	public readonly query = new NavigationGuardQuery();

	/** Die Nummer des letzten Ladens der Mehrfachauswahl, damit nur die Daten der neuesten Mehrfachauswahl übernommen werden */
	private _ladeNummer = 0;

	/** Die laufenden Anmeldungen, zugeordnet zum Schlüssel des Bereiches, damit ein gleichzeitiges zweites Anmelden denselben Bereich liefert */
	private readonly _anmeldungen = new Map<GruppenprozessBereichSchluessel<any>, Promise<GruppenprozessBereich<TAuswahl, any>>>();

	/** Der Auswahl-State, an dem die Bereiche angemeldet werden */
	private readonly _auswahl: GenericAuswahlGruppenprozessHost<TAuswahl>;


	/**
	 * Erzeugt einen neuen Orchestrator ohne angemeldete Bereiche.
	 *
	 * @param auswahl   der Auswahl-State, an dem die Bereiche angemeldet werden
	 */
	public constructor(auswahl: GenericAuswahlGruppenprozessHost<TAuswahl>) {
		this._auswahl = auswahl;
	}


	/**
	 * Meldet einen Bereich an. Ist unter dem Schlüssel bereits ein Bereich angemeldet, so wird dieser zurückgegeben.
	 *
	 * @param schluessel      der Schlüssel des Bereiches
	 * @param createBereich   erzeugt den Bereich, falls er noch nicht angemeldet ist
	 *
	 * @returns der angemeldete Bereich
	 *
	 * @throws DeveloperNotificationException   wenn der Benutzer den Bereich nicht nutzen darf
	 */
	public async register<TBereich extends GruppenprozessBereich<TAuswahl, any>>(schluessel: GruppenprozessBereichSchluessel<TBereich>,
		createBereich: () => TBereich): Promise<TBereich> {
		// Die Prüfung erfolgt synchron bis zum Vermerken der Anmeldung, damit ein gleichzeitiges Anmelden denselben Bereich liefert
		const vorhanden = this._bereiche.value.get(schluessel);
		if (vorhanden !== undefined) {
			return vorhanden as TBereich;
		}
		const laufend = this._anmeldungen.get(schluessel);
		if (laufend !== undefined) {
			return await laufend as TBereich;
		}
		const bereich = createBereich();
		if (!bereich.istBerechtigt()) {
			throw new DeveloperNotificationException("Für den Bereich '" + schluessel.name + "' der Gruppenprozesse fehlt die Berechtigung, er darf nicht angemeldet werden.");
		}
		// Der Bereich wird mit dem Abschluss des Ladens eingetragen, damit er für eine wartende Änderung der Mehrfachauswahl bereits angemeldet ist
		const anmeldung = bereich.anmelden(this._auswahl, this._auswahl.getMultiSelection()).then(() => {
			this._bereiche.value = new Map(this._bereiche.value).set(schluessel, bereich);
			return bereich;
		});
		this._anmeldungen.set(schluessel, anmeldung);
		try {
			await anmeldung;
		} finally {
			this._anmeldungen.delete(schluessel);
		}
		return bereich;
	}

	/**
	 * Gibt den angemeldeten Bereich zum Schlüssel zurück.
	 *
	 * @param schluessel   der Schlüssel des Bereiches
	 *
	 * @returns der Bereich oder null, wenn er nicht angemeldet ist
	 */
	public bereich<TBereich extends GruppenprozessBereich<TAuswahl, any>>(schluessel: GruppenprozessBereichSchluessel<TBereich>): TBereich | null {
		return (this._bereiche.value.get(schluessel) as TBereich | undefined) ?? null;
	}

	/**
	 * Gibt den angemeldeten Bereich zum Schlüssel zurück.
	 *
	 * @param schluessel   der Schlüssel des Bereiches
	 *
	 * @returns der Bereich
	 *
	 * @throws DeveloperNotificationException   wenn der Bereich nicht angemeldet ist
	 */
	public bereichOrException<TBereich extends GruppenprozessBereich<TAuswahl, any>>(schluessel: GruppenprozessBereichSchluessel<TBereich>): TBereich {
		const bereich = this.bereich(schluessel);
		if (bereich === null) {
			throw new DeveloperNotificationException(`Der Bereich '${schluessel.name}' ist bei den Gruppenprozessen nicht angemeldet.`);
		}
		return bereich;
	}

	/**
	 * Gibt zurück, ob in einem der angemeldeten Bereiche ausstehende Änderungen vorliegen.
	 *
	 * @returns true, wenn es ausstehende Änderungen gibt
	 */
	public get hasPending(): boolean {
		for (const bereich of this._bereiche.value.values()) {
			if (bereich.hasPending) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gibt die Bezeichnungen der Bereiche zurück, in denen ausstehende Änderungen vorliegen.
	 *
	 * @returns die Bezeichnungen, leer wenn es keine ausstehenden Änderungen gibt
	 */
	public get bereicheMitPending(): string[] {
		const bezeichnungen: string[] = [];
		for (const bereich of this._bereiche.value.values()) {
			if (bereich.hasPending) {
				bezeichnungen.push(bereich.bezeichnung);
			}
		}
		return bezeichnungen;
	}

	/**
	 * Gibt zurück, ob die ausstehenden Änderungen gespeichert werden dürfen, d.h. ob kein Bereich das Speichern sperrt.
	 *
	 * @returns true, wenn das Speichern möglich ist
	 */
	public get isPatchAllowed(): boolean {
		for (const bereich of this._bereiche.value.values()) {
			if (!bereich.isPatchAllowed) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gibt die Hinweise zu den Sperren des Speicherns aller Bereiche zurück.
	 *
	 * @returns die Hinweise, leer wenn das Speichern möglich ist
	 */
	public get patchHinweise(): string[] {
		const hinweise: string[] = [];
		for (const bereich of this._bereiche.value.values()) {
			hinweise.push(...bereich.patchHinweise);
		}
		return hinweise;
	}

	/**
	 * Patcht die ausstehenden Änderungen aller Bereiche parallel. Bei einem Fehler in einem Bereich behält dieser seine
	 * ausstehenden Änderungen, die übrigen Bereiche werden trotzdem gepatcht. Anschließend werden alle aufgetretenen
	 * Fehler gemeldet: ein einzelner Fehler unverändert, mehrere als AggregateError.
	 *
	 * @throws Error            wenn das Patchen in genau einem Bereich fehlschlägt
	 * @throws AggregateError   wenn das Patchen in mehreren Bereichen fehlschlägt, die Fehler stehen in errors
	 */
	public async patchMultiple(): Promise<void> {
		const ergebnisse = await Promise.allSettled([...this._bereiche.value.values()].map(async bereich => await bereich.patchMultiple()));
		const fehler: unknown[] = [];
		for (const ergebnis of ergebnisse) {
			if (ergebnis.status === "rejected") {
				fehler.push(ergebnis.reason);
			}
		}
		if (fehler.length === 1) {
			throw fehler[0];
		}
		if (fehler.length > 1) {
			throw new AggregateError(fehler, fehler.map(e => (e instanceof Error) ? e.message : String(e)).join("; "));
		}
	}

	/**
	 * Lädt die Daten zu der Mehrfachauswahl in allen angemeldeten Bereichen parallel, ohne sie zu übernehmen. Der
	 * Auswahl-State ruft dies bei jeder Änderung der Mehrfachauswahl auf (auch beim Wechsel des Schuljahresabschnittes,
	 * bei dem die Mehrfachauswahl verworfen wird). Die Daten werden erst übernommen, wenn der Auswahl-State die
	 * zurückgegebene Funktion aufruft, damit er alle States gemeinsam aktualisieren kann. Bereiche, die gerade noch
	 * angemeldet werden, werden zuvor abgewartet, damit sie die Änderung der Mehrfachauswahl nicht verpassen. Wird
	 * während des Ladens eine neuere Mehrfachauswahl geladen, übernimmt die zurückgegebene Funktion nichts mehr.
	 *
	 * @param idsAuswahl   die IDs der Einträge der Mehrfachauswahl, die Liste ist leer, wenn keine Mehrfachauswahl besteht
	 *
	 * @returns eine Promise mit der Funktion, welche die geladenen Daten aller Bereiche übernimmt
	 */
	public async loadForMultiSelection(idsAuswahl: List<number>): Promise<() => void> {
		const nummer = ++this._ladeNummer;
		await Promise.allSettled([...this._anmeldungen.values()]);
		const applies = await Promise.all([...this._bereiche.value.values()].map(async bereich => await bereich.loadForMultiSelection(idsAuswahl)));
		// Wurde inzwischen eine neuere Mehrfachauswahl geladen, liefert diese den aktuellen Stand
		return () => {
			if (nummer === this._ladeNummer) {
				applies.forEach(apply => apply());
			}
		};
	}

	/**
	 * Verwirft die ausstehenden Änderungen aller Bereiche und hebt deren Sperren des Speicherns auf. Die Bereiche bleiben angemeldet.
	 */
	public clearPending(): void {
		for (const bereich of this._bereiche.value.values()) {
			bereich.clearPending();
		}
	}

	/**
	 * Meldet alle Bereiche ab und verwirft deren Daten und ausstehende Änderungen.
	 */
	public reset(): void {
		for (const bereich of this._bereiche.value.values()) {
			bereich.abmelden();
		}
		this._bereiche.value = new Map<GruppenprozessBereichSchluessel<any>, GruppenprozessBereich<TAuswahl, any>>();
	}

}
