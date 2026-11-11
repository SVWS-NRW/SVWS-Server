import type { LehrerPersonalabschnittsdatenAnrechnungsstunden } from "@core/asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden";
import type { List } from "@core/java/util/List";

import { api } from "~/router/Api";

import type { LehrerPersonaldatenState } from "./LehrerPersonaldatenState";

type Anrechnungsstunden = LehrerPersonalabschnittsdatenAnrechnungsstunden;

/**
 * Die Basisklasse für die Operationen auf Anrechnungsstunden (Mehrleistung, Minderleistung und allgemeine
 * Anrechnungen) im Personalabschnitt des aktuellen Schuljahresabschnittes.
 */
abstract class LehrerAnrechnungsstundenOperationen {

	protected readonly _personaldaten: LehrerPersonaldatenState;

	/**
	 * Erzeugt die Operationen.
	 *
	 * @param personaldaten   der State mit den Personaldaten
	 */
	protected constructor(personaldaten: LehrerPersonaldatenState) {
		this._personaldaten = personaldaten;
	}

	/**
	 * Gibt die Liste der lokalen Anrechnungsstunden im Personalabschnitt des aktuellen Schuljahresabschnittes zurück.
	 *
	 * @returns die Liste
	 */
	protected abstract liste(): List<Anrechnungsstunden>;

	/**
	 * Übernimmt die gepatchten Werte in das lokale DTO mit der übergebenen ID, damit die Daten
	 * nach einem erfolgreichen Patch dem Stand auf dem Server entsprechen.
	 *
	 * @param data   die gepatchten Werte
	 * @param id     die ID des DTOs
	 */
	protected applyPatch(data: Partial<Anrechnungsstunden>, id: number): void {
		for (const eintrag of this.liste()) {
			if (eintrag.id === id) {
				Object.assign(eintrag, data);
				return;
			}
		}
	}

}

/**
 * Die Operationen für die Mehrleistung im Personalabschnitt des aktuellen Schuljahresabschnittes
 */
export class LehrerMehrleistungOperationen extends LehrerAnrechnungsstundenOperationen {

	public constructor(personaldaten: LehrerPersonaldatenState) {
		super(personaldaten);
	}

	protected liste(): List<Anrechnungsstunden> {
		return this._personaldaten.abschnittAktuellOrException().mehrleistung;
	}

	/**
	 * Fügt eine Mehrleistung hinzu.
	 *
	 * @param data   die Daten der Mehrleistung
	 */
	public async add(data: Partial<Anrechnungsstunden>): Promise<void> {
		const liste = this.liste();
		liste.add(await api.server.addLehrerPersonalabschnittsdatenMehrleistung(data, api.schema));
		this._personaldaten.commit();
	}

	/**
	 * Passt eine Mehrleistung an.
	 *
	 * @param data   die Änderungen
	 * @param id     die ID der Mehrleistung
	 *
	 * @returns true
	 */
	public async patch(data: Partial<Anrechnungsstunden>, id: number): Promise<boolean> {
		await api.server.patchLehrerPersonalabschnittsdatenMehrleistung(data, api.schema, id);
		this.applyPatch(data, id);
		this._personaldaten.commit();
		return true;
	}

	/**
	 * Entfernt eine Mehrleistung.
	 *
	 * @param data   die Mehrleistung
	 */
	public async remove(data: Anrechnungsstunden): Promise<void> {
		const liste = this.liste();
		await api.server.deleteLehrerPersonalabschnittsdatenMehrleistung(api.schema, data.id);
		liste.remove(data);
		this._personaldaten.commit();
	}

}

/**
 * Die Operationen für die Minderleistung im Personalabschnitt des aktuellen Schuljahresabschnittes
 */
export class LehrerMinderleistungOperationen extends LehrerAnrechnungsstundenOperationen {

	public constructor(personaldaten: LehrerPersonaldatenState) {
		super(personaldaten);
	}

	protected liste(): List<Anrechnungsstunden> {
		return this._personaldaten.abschnittAktuellOrException().minderleistung;
	}

	/**
	 * Fügt eine Minderleistung hinzu.
	 *
	 * @param data   die Daten der Minderleistung
	 */
	public async add(data: Partial<Anrechnungsstunden>): Promise<void> {
		const liste = this.liste();
		liste.add(await api.server.addLehrerPersonalabschnittsdatenMinderleistung(data, api.schema));
		this._personaldaten.commit();
	}

	/**
	 * Passt eine Minderleistung an.
	 *
	 * @param data   die Änderungen
	 * @param id     die ID der Minderleistung
	 *
	 * @returns true
	 */
	public async patch(data: Partial<Anrechnungsstunden>, id: number): Promise<boolean> {
		await api.server.patchLehrerPersonalabschnittsdatenMinderleistung(data, api.schema, id);
		this.applyPatch(data, id);
		this._personaldaten.commit();
		return true;
	}

	/**
	 * Entfernt eine Minderleistung.
	 *
	 * @param data   die Minderleistung
	 */
	public async remove(data: Anrechnungsstunden): Promise<void> {
		const liste = this.liste();
		await api.server.deleteLehrerPersonalabschnittsdatenMinderleistung(api.schema, data.id);
		liste.remove(data);
		this._personaldaten.commit();
	}

}

/**
 * Die Operationen für die allgemeinen Anrechnungen im Personalabschnitt des aktuellen Schuljahresabschnittes
 */
export class LehrerAnrechnungenOperationen extends LehrerAnrechnungsstundenOperationen {

	public constructor(personaldaten: LehrerPersonaldatenState) {
		super(personaldaten);
	}

	protected liste(): List<Anrechnungsstunden> {
		return this._personaldaten.abschnittAktuellOrException().anrechnungen;
	}

	/**
	 * Fügt eine Anrechnung hinzu.
	 *
	 * @param data   die Daten der Anrechnung
	 */
	public async add(data: Partial<Anrechnungsstunden>): Promise<void> {
		const liste = this.liste();
		liste.add(await api.server.addLehrerPersonalabschnittsdatenAllgemeineAnrechnung(data, api.schema));
		this._personaldaten.commit();
	}

	/**
	 * Passt mehrere Anrechnungen an.
	 *
	 * @param data   die Änderungen inklusive der IDs der Anrechnungen
	 *
	 * @returns true
	 */
	public async patch(data: List<Partial<Anrechnungsstunden>>): Promise<boolean> {
		await api.server.patchLehrerPersonalabschnittsdatenAllgemeineAnrechnungen(data, api.schema);
		for (const patch of data) {
			if (patch.id !== undefined) {
				this.applyPatch(patch, patch.id);
			}
		}
		this._personaldaten.commit();
		return true;
	}

	/**
	 * Entfernt eine Anrechnung.
	 *
	 * @param data   die Anrechnung
	 */
	public async remove(data: Anrechnungsstunden): Promise<void> {
		const liste = this.liste();
		await api.server.deleteLehrerPersonalabschnittsdatenAllgemeineAnrechnung(api.schema, data.id);
		liste.remove(data);
		this._personaldaten.commit();
	}

}
