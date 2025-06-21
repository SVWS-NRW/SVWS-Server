import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeLeistungen } from "~/router/apps/RouteLeistungen";
import { api } from "~/router/Api";
import type { ENMLeistung } from "@core/core/data/enm/ENMLeistung";
import type { ENMLeistungBemerkungen } from "@core/core/data/enm/ENMLeistungBemerkungen";
import type { ENMLernabschnitt } from "@core/core/data/enm/ENMLernabschnitt";
import type { ENMTeilleistung } from "@core/core/data/enm/ENMTeilleistung";
import type { PairNN } from "@core/asd/adt/PairNN";
import type { ENMSchueler } from "@core/core/data/enm/ENMSchueler";
import type { ENMKlasse } from "@core/index";
import { EnmAuswahlManager, type EnmLerngruppenAuswahlEintrag } from "@ui/components/enm/EnmManager";


const defaultState = <RouteStateInterface>{
	view: routeLeistungen,
};

export class RouteDataApp extends RouteData<RouteStateInterface> {

	protected	_auswahlmanagerLeistungen: EnmAuswahlManager<EnmLerngruppenAuswahlEintrag[], PairNN<ENMLeistung, ENMSchueler>>|undefined = undefined;
	protected	_auswahlmanagerTeilleistungen: EnmAuswahlManager<EnmLerngruppenAuswahlEintrag, PairNN<ENMLeistung, ENMSchueler>>|undefined = undefined;
	protected	_auswahlmanagerKlassenleitung: EnmAuswahlManager<ENMKlasse, ENMSchueler>|undefined = undefined;

	public constructor() {
		super(defaultState);
	}

	/** Einstellungen für die Tabellen und der Anzeige der Spalten */

	get klassenleitungColumnsVisible(): Map<string, boolean|null> {
		return new Map<string, boolean|null>(JSON.parse(api.config.getValue("klassenleitung.table.columns")));
	}

	setKlassenleitungColumnsVisible = async (value: Map<string, boolean|null>) => {
		await api.config.setValue('klassenleitung.table.columns', JSON.stringify([...value]));
	}

	get leistungenColumnsVisible(): Map<string, boolean|null> {
		return new Map<string, boolean|null>(JSON.parse(api.config.getValue("leistungen.table.columns")));
	}

	setLeistungenColumnsVisible = async (value: Map<string, boolean|null>) => {
		await api.config.setValue('leistungen.table.columns', JSON.stringify([...value]));
	}

	get teilleistungenColumnsVisible(): Map<string, boolean|null> {
		return new Map<string, boolean|null>(JSON.parse(api.config.getValue("teilleistungen.table.columns")));
	}

	setTeilleistungenColumnsVisible = async (value: Map<string, boolean|null>) => {
		await api.config.setValue('teilleistungen.table.columns', JSON.stringify([...value]));
	}

	get floskelEditorVisible(): boolean {
		return (api.config.getValue("floskelEditorVisible") === 'true');
	}

	setFloskelEditorVisible = async (value: boolean) => {
		await api.config.setValue('floskelEditorVisible', value ? 'true' : 'false');
	}

	get auswahlmanagerLeistungen() {
		if (this._auswahlmanagerLeistungen === undefined)
			this._auswahlmanagerLeistungen = new EnmAuswahlManager<EnmLerngruppenAuswahlEintrag[], PairNN<ENMLeistung, ENMSchueler>>([], api.manager.mapLerngruppeLeistungen);
		return this._auswahlmanagerLeistungen;
	}

	get auswahlmanagerTeilleistungen() {
		if (this._auswahlmanagerTeilleistungen === undefined)
			this._auswahlmanagerTeilleistungen = new EnmAuswahlManager<EnmLerngruppenAuswahlEintrag, PairNN<ENMLeistung, ENMSchueler>>(api.manager.listLerngruppenAuswahlliste.getFirst(), api.manager.mapLerngruppeLeistungen);
		return this._auswahlmanagerTeilleistungen;
	}

	get auswahlmanagerKlassenleitung() {
		if (this._auswahlmanagerKlassenleitung === undefined)
			this._auswahlmanagerKlassenleitung = new EnmAuswahlManager<ENMKlasse, ENMSchueler>(api.manager.listKlassenKlassenlehrer.getFirst(), api.manager.mapKlassenSchueler);
		return this._auswahlmanagerKlassenleitung;
	}

	/**
	 * Patch-Methode für ENM-Leistungsdaten.
	 *
	 * @param patch   die Daten des Patches mit gültiger ID
	 *
	 * @returns true, falls der Patch erfolgreich war, und ansonsten false
	 */
	public patchLeistung = async (patch: Partial<ENMLeistung>): Promise<boolean> => {
		await api.server.patchENMLeistung(patch);
		return true;
	}

	/**
	 * Patch-Methode für ENM-Teilleistungen.
	 *
	 * @param patch   die Daten des Patches mit gültiger ID
	 *
	 * @returns true, falls der Patch erfolgreich war, und ansonsten false
	 */
	public patchTeilleistung = async (patch: Partial<ENMTeilleistung>): Promise<boolean> => {
		await api.server.patchENMTeilleistung(patch);
		return true;
	}

	/**
	 * Patch-Methode für ENM-Bemerkungen.
	 *
	 * @param id      die gültige Schüler-ID
	 * @param patch   die Daten des Patches mit gültiger ID
	 *
	 * @returns true, falls der Patch erfolgreich war, und ansonsten false
	 */
	public patchBemerkungen = async (id: number, patch: Partial<ENMLeistungBemerkungen>): Promise<boolean> => {
		await api.server.patchENMSchuelerBemerkungen(id, patch);
		return true;
	}

	/**
	 * Patch-Methode für ENM-Lernabschnitt.
	 *
	 * @param patch   die Daten des Patches mit gültiger ID
	 *
	 * @returns true, falls der Patch erfolgreich war, und ansonsten false
	 */
	public patchLernabschnitt = async (patch: Partial<ENMLernabschnitt>): Promise<boolean> => {
		await api.server.patchENMSchuelerLernabschnitt(patch);
		return true;
	}

}

