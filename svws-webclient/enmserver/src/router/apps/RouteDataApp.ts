import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeLeistungen } from "~/router/apps/RouteLeistungen";
import { api } from "~/router/Api";
import type { ENMLeistung } from "@core/core/data/enm/ENMLeistung";
import type { ENMLeistungBemerkungen } from "@core/core/data/enm/ENMLeistungBemerkungen";
import type { ENMLernabschnitt } from "@core/core/data/enm/ENMLernabschnitt";
import type { ENMTeilleistung } from "@core/core/data/enm/ENMTeilleistung";
import type { ENMKlasse} from "@core/index";
import { DeveloperNotificationException, ENMDaten, Schulform } from "@core/index";
import { EnmManager} from "@ui/components/enm/EnmManager";
import { type EnmLerngruppenAuswahlEintrag } from "@ui/components/enm/EnmManager";
import { shallowRef } from "vue";
import { Config, ConfigElement } from "@ui/utils/Config";


/**
 * Ein Interface, welches den allgemeinen reaktiven State der Anwendung beschreibt.
 */
interface RouteStateApp extends RouteStateInterface {

	// Die ENM-Daten, welche für den angemeldeten Lehrer-Benutzer über die API geladen werden
	daten: ENMDaten | null;

	// Der Manager für die ENM-Daten, welche für den angemeldeten Lehrer-Benutzer über die API geladen werden
	manager: EnmManager | null;

	// Die aktuelle Konfiguration aus der ENM-Server-Datenbank
	config: Config | null;

	// Die temporäre, nicht in der DB festgehaltene Konfiguration
	nonPersistentConfig: Config | null;

}



/**
 * Diese Klasse enthält den Zustand der Applikation in Bezug auf das Routing
 * und den Daten, die in diesem Zusammenhang ggf. mithilfe der API und der
 * Konfiguration verwaltet werden.
 */
export class RouteDataApp extends RouteData<RouteStateApp> {

	// Die aktuell ausgewählten Lerngruppen bei den Ansichten für Leistungen und Teilleistungen (bei Mehrfachauswahl)
	protected _auswahlLerngruppen = shallowRef<Array<EnmLerngruppenAuswahlEintrag>>([]);

	// Die aktuell ausgewählte Lerngruppe bei den Ansichten für Leistungen und Teilleistungen (bei Einzelauswahl)
	protected _auswahlLerngruppe = shallowRef<EnmLerngruppenAuswahlEintrag | null>(null);

	// Die aktuell ausgewählten Klassen bei der Ansicht für die Klassenleitung (bei Mehrfachauswahl)
	protected _auswahlKlassen = shallowRef<Array<ENMKlasse>>([]);

	// Die aktuell ausgewählte Klasse bei der Ansicht für die Klassenleitung (bei Einzelauswahl)
	protected _auswahlKlasse = shallowRef<ENMKlasse | null>(null);

	/**
	 * Erstellt die neue Data Klasse für den Zustand der Applikation.
	 * Dabei wird die Default-Ansicht auf die Leistungsansicht gesetzt.
	 */
	public constructor() {
		super(<RouteStateApp>{
			view: routeLeistungen,
			daten: null,
			manager: null,
			config: null,
			nonPersistentConfig: null,
		});
	}


	/**
	 * Lädt die ENM-Daten und erzeugt den zugehörigen ENM-Manager
	 */
	public async ladeDaten() {
		try {
			const newState = <Partial<RouteStateApp>>{};
			// Lade auch die ENM-Daten vom Server...
			const file = await api.server.getLehrerENMDaten();
			const blob = await new Response(file.data.stream().pipeThrough(new DecompressionStream("gzip"))).blob();
			newState.daten = ENMDaten.transpilerFromJSON(await blob.text());
			newState.manager = new EnmManager(newState.daten, newState.daten.lehrerID ?? -1);

			const lerngruppen = newState.manager.mapLerngruppenAuswahl.values();
			this._auswahlLerngruppe.value = lerngruppen.isEmpty() ? null : lerngruppen.iterator().next();
			this._auswahlLerngruppen.value = [];
			const klassen = newState.manager.listKlassenKlassenlehrer;
			this._auswahlKlasse.value = klassen.isEmpty() ? null : klassen.getFirst();
			this._auswahlKlassen.value = [];

			// Laden der Konfiguration
			const cfg = await api.server.getClientConfig();
			const mapUser = new Map<string, string>();
			for (const c of cfg.user)
				mapUser.set(c.key, c.value);
			const mapGlobal = new Map<string, string>();
			for (const c of cfg.global)
				mapGlobal.set(c.key, c.value);
			// Persistente Config mit den geladenen Daten anlegen
			newState.config = new Config(async (key: string, value: string): Promise<void> => {
				// Schreiben der globalen Konfiguration
				throw new DeveloperNotificationException("Die Anwendung unterstützt kein Schreiben der globalen Konfiguration.");
			}, async (key: string, value: string): Promise<void> => {
				// Schreiben der benutzerspezifischen Konfiguration
				await api.server.setClientConfigUserKey(value, key);
			})
			newState.config.mapGlobal = mapGlobal;
			newState.config.mapUser = mapUser;
			newState.config.addElements([
				new ConfigElement("floskelEditorVisible", "user", 'true'),
				new ConfigElement("leistungen.table.columns", "user", JSON.stringify([
					["Klasse", null],
					["Name", null],
					["Fach", null],
					["Kurs", true],
					["Kursart", true],
					["Lehrer", true],
					["Quartal", true],
					["Note", null],
					["Mahnung", true],
					["FS", true],
					["FSU", true],
					["Bemerkung", true],
				])),
				new ConfigElement("teilleistungen.table.columns", "user", JSON.stringify([
					["Klasse", null],
					["Name", null],
					["Fach", null],
					["Kurs", true],
					["Kursart", true],
					["Lehrer", true],
					["Teilleistung", null],
					["Quartal", true],
					["Note", null],
				])),
				new ConfigElement("klassenleitung.table.columns", "user", JSON.stringify([
					["Klasse", null],
					["Name", null],
					["FS", null],
					["FSU", null],
					["ASV", true],
					["AUE", true],
					["ZB", true],
				])),
			]);
			// Nicht-persistente Config leer anlegen
			newState.nonPersistentConfig = new Config(async (_,__) => {}, async (_,__) => { });
			newState.nonPersistentConfig.mapGlobal = new Map<string, string>();
			newState.nonPersistentConfig.mapUser = new Map<string, string>();
			this.setPatchedDefaultState(newState);
		} catch (e) {
			this.entferneDaten();
			// TODO Fehler beim Laden der Daten sollte benutzerfreundlicher gehandhabt werden...
			throw e;
		}
	}

	/**
	 * Entfernt die ENM-Daten und den zugehörigen ENM-Manager
	 */
	public entferneDaten() {
		this._auswahlKlassen.value = [];
		this._auswahlLerngruppen.value = [];
		this._auswahlKlasse.value = null;
		this._auswahlLerngruppe.value = null;
		this.setPatchedState({
			daten: null,
			manager: null,
		});
		// TODO leere Konfiguration
	}

	/**
	 * Gibt das ENM-Datenobjekt zurück.
	 *
	 * @returns die ENM-Daten
	 */
	get daten(): ENMDaten {
		if (this._state.value.daten === null)
			throw new DeveloperNotificationException("Es wurden noch keine ENM-Daten geladen - Ein Zugriff auf diese Methode darf daher zu diesem Zeitpunkt nicht erfolgen");
		return this._state.value.daten;
	}

	/**
	 * Gibt den ENM-Manager zurück.
	 *
	 * @returns der ENM-Manager
	 */
	get manager(): EnmManager {
		if (this._state.value.manager === null)
			throw new DeveloperNotificationException("Es wurden noch keine ENM-Daten geladen - Ein Zugriff auf diese Methode darf daher zu diesem Zeitpunkt nicht erfolgen");
		return this._state.value.manager;
	}

	/**
	 * Gibt die persistente Konfiguration für den angemeldeten Benutzer zurück
	 *
	 * @returns die persistente Konfiguration
	 */
	get config(): Config {
		if (this._state.value.config === null)
			throw new DeveloperNotificationException("Die persistente Konfiguration wurde noch nicht geladen - Ein Zugriff auf diese Methode darf daher zu diesem Zeitpunkt nicht erfolgen");
		return this._state.value.config;
	}

	/**
	 * Gibt die nicht persistente Konfiguration für den angemeldeten Benutzer zurück
	 *
	 * @returns die nicht persistente Konfiguration
	 */
	get nonPersistentConfig(): Config {
		if (this._state.value.nonPersistentConfig === null)
			throw new DeveloperNotificationException("Die nicht persistente Konfiguration wurde noch nicht erzeugt - Ein Zugriff auf diese Methode darf daher zu diesem Zeitpunkt nicht erfolgen");
		return this._state.value.nonPersistentConfig;
	}

	/**
	 * Gibt die Schulform der Schule zurück.
	 *
	 * @returns die Schulform
	 */
	public get schulform(): Schulform {
		if (this.daten.schulform === null)
			throw new DeveloperNotificationException("In den ENM-Daten ist keine Schulform eingetragen.");
		const schulform = Schulform.data().getWertByKuerzel(this.daten.schulform);
		if (schulform === null)
			throw new DeveloperNotificationException("In den ENM-Daten ist eine ungültige Schulform eingetragen.");
		return schulform;
	}


	/**
	 * Gibt die Map mit den sichtbaren Spalten in der Klassenleitungs-Ansicht
	 * aus der benutzerspezifischen Konfiguration zurück.
	 *
	 * @return eine Map, welche vom Spalten-Kürzel auf einen boolean-Wert oder null abbildet
	 */
	get klassenleitungColumnsVisible(): Map<string, boolean | null> {
		return new Map<string, boolean|null>(JSON.parse(this.config.getValue("klassenleitung.table.columns")));
	}

	/**
	 * Setzt in der benutzerspezifischen Konfiguration die Map mit den sichtbaren Spalten in der Klassenleitungs-Ansicht.
	 *
	 * @param value   die Map, welche vom Spalten-Kürzel auf einen boolean-Wert oder null abbildet
	 */
	setKlassenleitungColumnsVisible = async (value: Map<string, boolean | null>) => {
		await this.config.setValue('klassenleitung.table.columns', JSON.stringify([...value]));
		this.commit();
	}

	/**
	 * Gibt die Map mit den sichtbaren Spalten in der Leistungs-Ansicht
	 * aus der benutzerspezifischen Konfiguration zurück.
	 *
	 * @return eine Map, welche vom Spalten-Kürzel auf einen boolean-Wert oder null abbildet
	 */
	get leistungenColumnsVisible(): Map<string, boolean | null> {
		return new Map<string, boolean|null>(JSON.parse(this.config.getValue("leistungen.table.columns")));
	}

	/**
	 * Setzt in der benutzerspezifischen Konfiguration die Map mit den sichtbaren Spalten in der Leistungs-Ansicht.
	 *
	 * @param value   die Map, welche vom Spalten-Kürzel auf einen boolean-Wert oder null abbildet
	 */
	setLeistungenColumnsVisible = async (value: Map<string, boolean | null>) => {
		await this.config.setValue('leistungen.table.columns', JSON.stringify([...value]));
		this.commit();
	}

	/**
	 * Gibt die Map mit den sichtbaren Spalten in der Teilleistungs-Ansicht
	 * aus der benutzerspezifischen Konfiguration zurück.
	 *
	 * @return eine Map, welche vom Spalten-Kürzel auf einen boolean-Wert oder null abbildet
	 */
	get teilleistungenColumnsVisible(): Map<string, boolean | null> {
		return new Map<string, boolean|null>(JSON.parse(this.config.getValue("teilleistungen.table.columns")));
	}

	/**
	 * Setzt in der benutzerspezifischen Konfiguration die Map mit den sichtbaren Spalten in der Teilleistungs-Ansicht.
	 *
	 * @param value   die Map, welche vom Spalten-Kürzel auf einen boolean-Wert oder null abbildet
	 */
	setTeilleistungenColumnsVisible = async (value: Map<string, boolean | null>) => {
		await this.config.setValue('teilleistungen.table.columns', JSON.stringify([...value]));
		this.commit();
	}

	/**
	 * Gibt die Information aus der benutzerspezifischen Konfiguration zurück, ob
	 * der Floskel-Editor angezeigt werden soll oder nicht.
	 *
	 * @returns true, wenn der Floskel-Editor angezeigt werden soll, und ansonsten false
	 */
	get floskelEditorVisible(): boolean {
		return (this.config.getValue("floskelEditorVisible") === 'true');
	}

	/**
	 * Setzt die Information in der benutzerspezifischen Konfiguration, ob der Floskel-Editor
	 * angezeigt werden soll oder nicht.
	 *
	 * @param value   true, wenn der Floskel-Editor angezeigt werden soll, und ansonsten false
	 */
	setFloskelEditorVisible = async (value: boolean) => {
		await this.config.setValue('floskelEditorVisible', value ? 'true' : 'false');
		this.commit();
	}

	/**
	 * Gibt die aktuelle Lerngruppen-Auswahl für die Ansicht der Leistungen und Teilleistungen zurück. (die Einzelauswahl)
	 *
	 * @returns die Lerngruppen-Auswahl
	 */
	get auswahlLerngruppe(): EnmLerngruppenAuswahlEintrag | null {
		return this._auswahlLerngruppe.value;
	}

	/**
	 * Gibt die aktuelle Lerngruppen-Auswahl für die Ansichten der Leistungen und Teilleistungen zurück.
	 * (die Einzelauswahl oder die Mehrfachauswahl)
	 *
	 * @returns die Lerngruppen-Auswahl
	 */
	get auswahlLerngruppen(): Array<EnmLerngruppenAuswahlEintrag> {
		if (this._auswahlLerngruppe.value === null)
			return this._auswahlLerngruppen.value;
		return [ this._auswahlLerngruppe.value ];
	}

	/**
	 * Gibt die aktuelle Lerngruppen-Auswahl für die Ansichten der Leistungen und Teilleistungen zurück. (nur die Mehrfachauswahl)
	 *
	 * @returns die Lerngruppen-Auswahl
	 */
	get auswahlLerngruppenNurMehrfachauswahl(): Array<EnmLerngruppenAuswahlEintrag> {
		return this._auswahlLerngruppen.value;
	}

	/**
	 * Setzt die Auswahl der Lerngruppe (bei Einzelauswahl)
	 *
	 * @param value   die neue Auswahl
	 */
	public setAuswahlLerngruppe = (value: EnmLerngruppenAuswahlEintrag | null) => {
		this._auswahlLerngruppe.value = value;
	};

	/**
	 * Setzt die Auswahl der Lerngruppen (bei Mehrfachauswahl)
	 *
	 * @param value   die neue Auswahl
	 */
	public setAuswahlLerngruppen = (value: Array<EnmLerngruppenAuswahlEintrag>) => {
		this._auswahlLerngruppen.value = value;
	};

	/**
	 * Gibt die aktuelle Klassen-Auswahl für die Ansicht der Klassenleitungen zurück. (die Einzelauswahl)
	 *
	 * @returns die Klassen-Auswahl
	 */
	get auswahlKlasse(): ENMKlasse | null {
		return this._auswahlKlasse.value;
	}

	/**
	 * Gibt die aktuelle Klassen-Auswahl für die Ansicht der Klassenleitungen zurück.
	 * (die Einzelauswahl oder die Mehrfachauswahl)
	 *
	 * @returns die Klassen-Auswahl
	 */
	get auswahlKlassen(): Array<ENMKlasse> {
		if (this._auswahlKlasse.value === null)
			return this._auswahlKlassen.value;
		return [ this._auswahlKlasse.value ];
	}

	/**
	 * Gibt die aktuelle Klassen-Auswahl für die Ansicht der Klassenleitungen zurück. (nur die Mehrfachauswahl)
	 *
	 * @returns die Klassen-Auswahl
	 */
	get auswahlKlassenNurMehrfachauswahl() : Array<ENMKlasse> {
		return this._auswahlKlassen.value;
	}

	/**
	 * Setzt die Auswahl der Klasse (bei Einzelauswahl)
	 *
	 * @param value   die neue Auswahl
	 */
	public setAuswahlKlasse = (value: ENMKlasse | null) => {
		this._auswahlKlasse.value = value;
	};

	/**
	 * Setzt die Auswahl der Klassen (bei Mehrfachauswahl)
	 *
	 * @param value   die neue Auswahl
	 */
	public setAuswahlKlassen = (value: Array<ENMKlasse>) => {
		this._auswahlKlassen.value = value;
	};


	/**
	 * Patch-Methode für ENM-Leistungsdaten.
	 *
	 * @param data    die ursprünglichen Daten
	 * @param patch   die Daten des Patches
	 */
	public patchLeistung = async (data: ENMLeistung, patch: Partial<ENMLeistung>): Promise<void> => {
		patch.id = data.id;
		await api.server.patchENMLeistung(patch);
		Object.assign(data, patch);
		this.commit();
	}

	/**
	 * Patch-Methode für ENM-Teilleistungen.
	 *
	 * @param data    die ursprünglichen Daten
	 * @param patch   die Daten des Patches
	 */
	public patchTeilleistung = async (data: ENMTeilleistung, patch: Partial<ENMTeilleistung>): Promise<void> => {
		patch.id = data.id;
		await api.server.patchENMTeilleistung(patch);
		Object.assign(data, patch);
		this.commit();
	}

	/**
	 * Patch-Methode für ENM-Bemerkungen.
	 *
	 * @param id      die gültige Schüler-ID
	 * @param data    die ursprünglichen Daten
	 * @param patch   die Daten des Patches
	 */
	public patchBemerkungen = async (id: number, data: ENMLeistungBemerkungen, patch: Partial<ENMLeistungBemerkungen>): Promise<void> => {
		await api.server.patchENMSchuelerBemerkungen(id, patch);
		Object.assign(data, patch);
		this.commit();
	}

	/**
	 * Patch-Methode für ENM-Lernabschnitt.
	 *
	 * @param data    die ursprünglichen Daten
	 * @param patch   die Daten des Patches
	 */
	public patchLernabschnitt = async (data: ENMLernabschnitt, patch: Partial<ENMLernabschnitt>): Promise<void> => {
		patch.id = data.id;
		await api.server.patchENMSchuelerLernabschnitt(patch);
		Object.assign(data, patch);
		this.commit();
	}

}
