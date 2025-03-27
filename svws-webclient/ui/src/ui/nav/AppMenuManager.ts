import { DeveloperNotificationException } from "../../../../core/src";
import { AppMenuGroup } from "./AppMenuGroup";
import type { TabData } from "./TabData";
import type { TabManager } from "./TabManager";

/**
 * Diese Klasse dient der Verwaltung der Menu-Struktur einer Client-Applikation.
 * Sie wird von der Hauptroute her initialisiert und ui-Komponenten über props
 * zur Verfügung gestellt.
 */
export class AppMenuManager {

	/** Der Manager für das Hauptmenü */
	private _menu: TabManager;

	/** Die Manager für die Untermenüs */
	private _submenus: Map<string, TabManager>;

	/** Der aktuell ausgeweählte Menü- bzw. Untermenü-Eintrag */
	private _current: TabData;

	/** Eine Map, von dem vollständigen Namen eines Menü- bzw. Untermenü-Eintrages zu dem zugehörigen Hauptmenü-Eintrag */
	private _mapMainmenu: Map<string, TabData>;

	/**
	 * Erstellt einen neuen Menu-Manager mit den übergebenen Apps für das Hauptmenu
	 *
	 * @param menuManager      der Tab-Manager für das Hauptmenu
	 * @param submenuManager   ein Array mit Tab-Managern für die Untermenus
	 */
	public constructor(menuManager: TabManager, submenuManager: {name: string, manager: TabManager}[], current: TabData) {
		this._menu = menuManager;
		this._submenus = new Map<string, TabManager>();
		for (const m of submenuManager) {
			// Prüfe, ob das Hauptmenu einen Eintrag mit dem Namen hat. Wenn nicht, dann gibt es eine Exception
			this._menu.getTab(m.name);
			// Wenn ein Eintrag vorhanden ist, dann füge diesen hinzu
			this._submenus.set(m.name, m.manager);
		}
		this._current = current;
		// Erstelle eine Map von den Namen der Einträge zu den zugehörigen Hauptmenü-Einträgen
		this._mapMainmenu = new Map<string, TabData>();
		for (const t of this._menu.tabs)
			this._mapMainmenu.set(t.name, t);
		for (const s of submenuManager)
			for (const t of s.manager.tabs)
				this._mapMainmenu.set(t.name, this._menu.getTab(s.name));
	}

	/**
	 * Gibt den Menü-Eintrag für das Benutzerprofil zurück, sofern ein Eintrag definiert wurde.
	 *
	 * @returns der Menü-Eintrag oder null
	 */
	public get benutzerprofil() : TabData | null {
		const tabs = this._menu.getTabsOfGroup(AppMenuGroup.BENUTZERPROFIL);
		if (tabs.length > 1)
			throw new DeveloperNotificationException("Es wurde mehr als ein Eintrag für das Benutzerprofil definiert. Dies wird nicht unterstützt.");
		return (tabs.length === 1) ? tabs[0] : null;
	}

	/**
	 * Gibt die Menü-Einträge des Hauptmenüs zurück.
	 */
	public get main() : TabData[] {
		return this._menu.getTabsOfGroup(AppMenuGroup.MAIN);
	}

	/**
	 * Gibt den Menü-Eintrag für die Einstellungen zurück, sofern ein Eintrag definiert wurde.
	 *
	 * @returns der Menü-Eintrag oder null
	 */
	public get einstellungen() : TabData | null {
		const tabs = this._menu.getTabsOfGroup(AppMenuGroup.EINSTELLUNGEN);
		if (tabs.length > 1)
			throw new DeveloperNotificationException("Es wurde mehr als ein Eintrag für die Einstellungen definiert. Dies wird nicht unterstützt.");
		return (tabs.length === 1) ? tabs[0] : null;
	}

	/**
	 * Gibt den aktuellen ausgewählten Menü- bzw. Untermenü-Eintrag zurück.
	 *
	 * @returns der aktuell ausgewählte Menü- bzw. Untermenü-Eintrag
	 */
	public get current(): TabData {
		return this._current;
	}

	/**
	 * Gibt den aktuellen ausgewählten Hauptmenü-Eintrag zurück. Im Falle eines
	 * aktuell ausgewählten Untermenü-Eintrages wird dieser ermittelt.
	 *
	 * @returns der aktuell ausgewählte Hauptmenü-Eintrag
	 */
	public get mainEntry(): TabData {
		return this._mapMainmenu.get(this._current.name) ?? this._current;
	}

	/**
	 * Prüft, ob der aktuell ausgewählte Hauptmenü-Eintrag ein Untermenü besitzt
	 * oder nicht.
	 *
	 * @returns true, falls ein Untermenü vorhanden ist, und ansonsten false
	 */
	public get hasSubmenu(): boolean {
		return (this._submenus.get(this.mainEntry.name) !== undefined);
	}

	/**
	 * Prüft, ob der aktuell ausgewählte Menü-Eintrag eine Auswahlliste besitzt
	 * oder nicht.
	 *
	 * @returns true, falls eine Auswahlliste vorhanden ist, und ansonsten false
	 */
	public get hasAuswahlliste(): boolean {
		return !(this._current.hide ?? false);
	}

	/**
	 * Setzt den aktuellen Eintrag des Hauptmenus und wartet ggf. auf den asynchronen Callback
	 *
	 * @param entry   der zu setzende Menüeintrag
	 */
	public async setEintrag(entry : TabData) {
		await this._menu.setTab(entry);
	}


}