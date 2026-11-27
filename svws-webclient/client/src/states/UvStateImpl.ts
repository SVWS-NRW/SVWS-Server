import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { UvBlockungRegel } from "@core/core/data/uv/regel/UvBlockungRegel";
import { UvPlanungsabschnitt } from "@core/core/data/uv/UvPlanungsabschnitt";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { UvManager } from "@core/core/utils/uv/UvManager";
import type { List } from "@core/java/util/List";
import type { UvState } from "@ui/states/UvState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";
import { abschnittStateImpl as abschnittState } from "~/states/AbschnittStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";

import { UvGrunddatenStateAktionen } from "./UvGrunddatenStateAktionen";
import { UvPlanungsStateAktionen } from "./UvPlanungsStateAktionen";
import { UvRegelStateAktionen } from "./UvRegelStateAktionen";

interface UvReactiveState {
	bearbeitungAktiv: boolean;
	uvManager: UvManager;
	planungsabschnitt: UvPlanungsabschnitt | null;
	regeln: UvBlockungRegel[];
}

const defaultState = <UvReactiveState> {
	bearbeitungAktiv: false,
	uvManager: new UvManager(),
	planungsabschnitt: null,
	regeln: [],
};

export interface UvRequestKontext {
	manager: UvManager;
	idPlanungsabschnitt: number | undefined;
	schema: string;
}

export interface UvStateAktionskontext {
	state: Pick<UvState, 'uvManager' | 'planungsabschnitt' | 'regelliste' | 'hatKompetenzAendern'>;
	commit: () => void;
	createPayload: <T extends { id?: number }>(data: T) => T;
	patchManagerDto: <T extends object>(existing: T, data: Partial<T>, dtoClass: new () => T, update: (patched: T) => void) => void;
}

export class UvStateImpl extends StateManager<UvReactiveState> implements UvState {

	// Ladestand und Warteschlange

	private schuljahrLoaded = -1;
	private geladenesSchema: string | null = null;
	private schuljahrLadeversion = 0;
	private planungsabschnittLadeversion = 0;
	private readonly objektAenderungen = new Map<string, Promise<void>>();
	// Aktionsgruppen am gemeinsamen UV-State

	private readonly aktionskontext: UvStateAktionskontext = {
		state: this,
		commit: () => this.commit(),
		patchManagerDto: (existing, data, dtoClass, update) => this.patchManagerDto(existing, data, dtoClass, update),
		createPayload: data => this.createPayload(data),
	};
	public readonly planung = new UvPlanungsStateAktionen({
		...this.aktionskontext,
		planungsabschnitt: () => this.planungsabschnittOrException,
		requestKontext: () => this.requestKontext(),
		istRequestKontextAktuell: (kontext, mitPlanungsabschnitt) => this.istRequestKontextAktuell(kontext, mitPlanungsabschnitt),
		objektAenderungEinreihen: (schluessel, aktion) => this.objektAenderungEinreihen(schluessel, aktion),
	});
	public readonly grunddaten = new UvGrunddatenStateAktionen(this.aktionskontext, this.planung.removeLehrerFromPlanungsabschnitt);
	public readonly regeln = new UvRegelStateAktionen({
		state: this,
		planungsabschnitt: () => this.planungsabschnittOrException,
		requestKontext: () => this.requestKontext(),
		istRequestKontextAktuell: kontext => this.istRequestKontextAktuell(kontext),
		regellisteSetzen: regeln => this.setPatchedState({ regeln }),
		ersetzeLehrkraftzuordnungen: ergebnis => this.planung.ersetzeLehrkraftzuordnungen(ergebnis),
	});

	public constructor() {
		super(defaultState);
	}

	// Zugriff auf Zustand und Berechtigungen

	public get bearbeitungAktiv(): boolean {
		return this._state.value.bearbeitungAktiv;
	}

	public setBearbeitungAktiv(aktiv: boolean): void {
		this.setPatchedState({ bearbeitungAktiv: aktiv });
	}

	public get uvManager(): UvManager {
		return this._state.value.uvManager;
	}

	public get planungsabschnitt(): UvPlanungsabschnitt | null {
		return this._state.value.planungsabschnitt;
	}

	public get regelliste(): UvBlockungRegel[] {
		return this._state.value.regeln;
	}

	public get hatKompetenzAllgemeinAendern(): boolean {
		return benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN);
	}

	public get hatKompetenzFunktionsbezogenAendern(): boolean {
		return benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_FUNKTIONSBEZOGEN_AENDERN);
	}

	public get hatKompetenzAendern(): boolean {
		return this.hatKompetenzAllgemeinAendern || this.hatKompetenzFunktionsbezogenAendern;
	}

	// Kontextprüfung und Reihenfolge laufender Änderungen

	private requestKontext(): UvRequestKontext {
		return { manager: this.uvManager, idPlanungsabschnitt: this.planungsabschnitt?.id, schema: api.schema };
	}

	/** Abschnittsübergreifende Daten im Manager dürfen auch nach einem Auswahlwechsel aktualisiert werden. */
	private istRequestKontextAktuell(kontext: UvRequestKontext, mitPlanungsabschnitt = true): boolean {
		return this.uvManager === kontext.manager && api.schema === kontext.schema
			&& (!mitPlanungsabschnitt || this.planungsabschnitt?.id === kontext.idPlanungsabschnitt);
	}

	/** Serialisiert Änderungen desselben Objekts und prüft den Kontext vor dem Speichern. */
	private objektAenderungEinreihen(schluessel: string, aktion: (kontext: UvRequestKontext) => Promise<void>): Promise<void> {
		const kontext = this.requestKontext();
		const warteschluessel = `${kontext.schema}:${schluessel}`;
		const vorher = this.objektAenderungen.get(warteschluessel) ?? Promise.resolve();
		const aenderung = vorher.catch(() => undefined).then(async () => {
			if (!this.istRequestKontextAktuell(kontext)) {
				throw new Error('Der UV-Kontext hat sich vor dem Speichern geändert.');
			}
			await aktion(kontext);
		}).finally(() => {
			if (this.objektAenderungen.get(warteschluessel) === aenderung) {
				this.objektAenderungen.delete(warteschluessel);
			}
		});
		this.objektAenderungen.set(warteschluessel, aenderung);
		return aenderung;
	}

	private get planungsabschnittOrException(): UvPlanungsabschnitt {
		const planungsabschnitt = this._state.value.planungsabschnitt;
		if ((planungsabschnitt === null) || (planungsabschnitt.id === -1)) {
			throw new DeveloperNotificationException("Kein UV-Planungsabschnitt gewaehlt.");
		}
		return planungsabschnitt;
	}

	// Laden und Zurücksetzen der UV-Daten

	public reset(): void {
		this.schuljahrLadeversion++;
		this.planungsabschnittLadeversion++;
		this.schuljahrLoaded = -1;
		this.geladenesSchema = null;
		this.setPatchedDefaultState({ uvManager: new UvManager(), regeln: [] });
	}

	/** Lädt die Grunddaten und setzt den neuen Manager erst nach vollständig erfolgreichem Laden. */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number, forceReload: boolean = false): Promise<List<UvPlanungsabschnitt> | null> {
		const schuljahresabschnitt = abschnittState.getOrNull(idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		}
		const kontext = this.requestKontext();
		if (this.schuljahrLoaded === schuljahresabschnitt.schuljahr && this.geladenesSchema === kontext.schema && !forceReload) {
			return null;
		}
		const version = ++this.schuljahrLadeversion;
		const [listPlanungsabschnitte, listJahrgaenge, fachdaten, data] = await Promise.all([
			api.server.getUvPlanungsabschnitte(kontext.schema, schuljahresabschnitt.schuljahr),
			api.server.getJahrgaenge(kontext.schema),
			api.server.getFaecher(kontext.schema),
			api.server.getUvGrunddaten(kontext.schema),
		]);
		if (version !== this.schuljahrLadeversion || !this.istRequestKontextAktuell(kontext, false)) {
			return null;
		}
		const uvManager = new UvManager(listJahrgaenge, fachdaten);
		uvManager.grunddatenBundleAdd(data);
		this.schuljahrLoaded = schuljahresabschnitt.schuljahr;
		this.geladenesSchema = kontext.schema;
		this.setPatchedState({ uvManager, planungsabschnitt: null, regeln: [] });
		return listPlanungsabschnitte;
	}

	/** Lädt die fehlenden Abschnittsdaten und veröffentlicht nur das Ergebnis der aktuellen Auswahl. */
	public async ladeDaten(auswahl: UvPlanungsabschnitt | null, planungsabschnitte: Iterable<UvPlanungsabschnitt>): Promise<UvPlanungsabschnitt | null> {
		const version = ++this.planungsabschnittLadeversion;
		if (auswahl === null) {
			this.setPatchedState({ planungsabschnitt: null, regeln: [] });
			return null;
		}
		const kontext = this.requestKontext();
		// Grunddaten benötigen die Zuordnungen aller Planungsabschnitte für die Verwendungsprüfung.
		const abschnitte = auswahl.id === -1 ? [...planungsabschnitte].filter(plan => plan.id !== -1) : [auswahl];
		const pakete = [];
		for (const plan of abschnitte) {
			if (!kontext.manager.planungsabschnittIsGeladen(plan)) {
				const daten = await api.server.getUvPlanungsabschnittsdaten(kontext.schema, plan.id);
				if (version !== this.planungsabschnittLadeversion || !this.istRequestKontextAktuell(kontext, false)) {
					throw new DeveloperNotificationException('Die Auswahl hat sich beim Laden des UV-Planungsabschnitts geändert.');
				}
				pakete.push({ plan, daten });
			}
		}
		const regeln = auswahl.id === -1 ? [] : [...await api.server.getUvRegeln(kontext.schema, auswahl.id)];
		if (version !== this.planungsabschnittLadeversion || !this.istRequestKontextAktuell(kontext, false)) {
			throw new DeveloperNotificationException('Die Auswahl hat sich beim Laden des UV-Planungsabschnitts geändert.');
		}
		for (const { plan, daten } of pakete) {
			if (!kontext.manager.planungsabschnittIsGeladen(plan)) {
				kontext.manager.planungsabschnittsdatenBundleAdd(daten);
			}
		}
		this.setPatchedState({ planungsabschnitt: auswahl, regeln });
		return auswahl;
	}

	/** Erstellt einen Request ohne die Identität des übergebenen Entwurfs zu verändern. */
	private createPayload<T extends { id?: number }>(data: T): T {
		const payload = { ...data };
		delete payload.id;
		return payload;
	}

	/**
	 * Übergibt ein neues DTO an den Manager, solange die alten Indexschlüssel noch verfügbar sind.
	 */
	private patchManagerDto<T extends object>(existing: T, data: Partial<T>, dtoClass: new () => T, update: (patched: T) => void): void {
		const patched = Object.assign(new dtoClass(), existing, data);
		if ('id' in existing) {
			Object.assign(patched, { id: existing.id });
		}
		update(patched);
	}

	// Planungsabschnitte verwalten

	public async patch(id: number, data: Partial<UvPlanungsabschnitt>): Promise<boolean> {
		const planungsabschnitt = this.planungsabschnitt;
		if (planungsabschnitt?.id !== id) {
			throw new DeveloperNotificationException("Beim Patchen des UV-Planungsabschnitts sind keine gültigen Daten geladen.");
		}
		await api.server.patchUvPlanungsabschnitt({ ...data, id }, api.schema);
		this.patchManagerDto(planungsabschnitt, data, UvPlanungsabschnitt, patched => this.uvManager.planungsabschnittPatchAttributes(patched));
		this.setPatchedState({ planungsabschnitt: this.uvManager.planungsabschnittGetByIdOrException(id) });
		this.commit();
		return true;
	}

	public async deletePlanungsabschnitte(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteUvPlanungsabschnitteAsListSimpleOperationResponse(ids, api.schema);
	}

	public async add(partial: Partial<UvPlanungsabschnitt>): Promise<UvPlanungsabschnitt> {
		const neu = await api.server.createUvPlanungsabschnitt({ ...partial, schuljahr: abschnittState.auswahl.schuljahr }, api.schema);
		return neu;
	}

	public async addAsCopy(partial: Partial<UvPlanungsabschnitt>, _idFromUvPlanungsabschnitt?: number): Promise<UvPlanungsabschnitt> {
		return await this.add(partial);
	}

}

export const uvStateImpl = new UvStateImpl();
