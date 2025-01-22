import type { TabData } from "./TabData";

/**
 * Diese Klasse dient der Verwaltung der Tabs. Sie wird von Applikationsseite initialisiert
 * und ui-Komponenten über props zur Verfügung gestellt. Die setTab-Funktion dient dabei
 * als Callback.
 */
export class TabManager {

	// Der aktuell ausgewählte Tab
	private _tab : TabData;

	// Das Array mit den verwalteten Tabs (siehe Konstruktor)
	private _tabs : TabData[];

	// Die Callback-Funktion, falls das Tab neu gesetzt wird.
	private _setTab: (value: TabData) => Promise<void>;

	// Eine Map für den schnellen Zugriff auf die Tabs
	private _mapName = new Map<string, TabData>();

	// Eine Map für den schnellen Zugriff auf den Index des Arrays anhand des Tabnamens
	private _mapNameToIndex = new Map<string, number>();

	/** Eine Map mit den Tab-Groups für die Darstellung */
	private _tabgroups = new Map<string, Array<TabData>>();

	/**
	 * Erstellt einen neuen Manager.
	 *
	 * @param tabs     die zu verwaltenden Tabs
	 * @param tab      das zu Beginn ausgewählte Tab
	 * @param setTab   der Callback, falls ein neues aktives Tab gesetzt wird
	 * @param hidden   ein optionales Array, mit dem das attribute hide bei den Tabs gesetzt werden kann
	 */
	public constructor(tabs : TabData[], tab : TabData, setTab: (value: TabData) => Promise<void>, hidden?: boolean[]) {
		this._tabs = tabs;
		this._tab = tab;
		this._setTab = setTab;
		for (let i = 0; (i < this._tabs.length); i++) {
			if ((hidden !== undefined) && (i < hidden.length))
				this._tabs[i].hide = hidden[i];
			this._mapName.set(this._tabs[i].name, this._tabs[i]);
			this._mapNameToIndex.set(this._tabs[i].name, i);
			const groupname = this.tabs[i].tabgroup ?? "";
			let group = this._tabgroups.get(groupname);
			if (group === undefined) {
				group = new Array<TabData>();
				this._tabgroups.set(groupname, group);
			}
			group.push(this._tabs[i]);
		}
	}

	/**
	 * Gibt die Liste der Tabs zurück.
	 */
	public get tabs() : Array<TabData> {
		return this._tabs;
	}

	/**
	 * Gibt das aktuell ausgewählte Tab zurück.
	 */
	public get tab() : TabData {
		return this._tab;
	}

	/**
	 * Gibt ein Array mit den Tab-Groups zurück.
	 */
	public get tabgroups() : string[] {
		return [ ... this._tabgroups.keys()];
	}

	/**
	 * Gibt das nächste sichtbare Tab ausgehend vom aktuellen Tab zurück. Ist dies nicht verfügbar,
	 * so wird das erste sichtbare Tab zurückgegeben. In dem Sonderfall, dass es kein anderes sichtbares
	 * Tab gibt wird das aktuelle Tab zurückgegeben.
	 */
	public get nextTab() : TabData {
		let i = this._mapNameToIndex.get(this._tab.name) ?? -1;
		do {
			i++;
		} while ((i < this._tabs.length) && (this._tabs[i].hide === true));
		if (i === this._tabs.length)
			i = 0;
		while ((i < this._tabs.length) && (this._tabs[i].hide === true))
			i++;
		return (i < this._tabs.length) ? this._tabs[i] : this._tab;
	}

	/**
	 * Gibt das vorige sichtbare Tab ausgehend vom aktuellen Tab zurück. Ist dies nicht verfügbar,
	 * so wird das letzte sichtbare Tab zurückgegeben. In dem Sonderfall, dass es kein anderes sichtbares
	 * Tab gibt wird das aktuelle Tab zurückgegeben.
	 */
	public get prevTab() : TabData {
		let i = this._mapNameToIndex.get(this._tab.name) ?? -1;
		do {
			i--;
		} while ((i > -1) && (this._tabs[i].hide === true));
		if (i === this._tabs.length)
			i = this._tabs.length - 1;
		while ((i > -1) && (this._tabs[i].hide === true))
			i++;
		return (i > -1) ? this._tabs[i] : this._tab;
	}

	/**
	 * Setzt das aktuelle Tab und wartet ggf. auf damit den asynchronen Callback
	 *
	 * @param value   das neu zu setzende Tab
	 */
	public async setTab(value : TabData) {
		await this._setTab(value);
		this._tab = value;
	}

	/**
	 * Aktualisiert die Hidden-Attribute bei den Tabs anhand des übergebenen boolean-Arrays
	 *
	 * @param hidden   ein Array, mit dem das attribute hide bei den Tabs gesetzt werden kann
	 */
	public updateHidden(hidden: boolean[]) : void {
		for (let i = 0; (i < this._tabs.length) && i < (hidden.length); i++)
			this._tabs[i].hide = hidden[i];
	}

	/**
	 * Gibt die Tab-Daten für den übergebenen Tab-Namen zurück.
	 *
	 * @param name   der Name des Tabs
	 *
	 * @returns die Tab-Daten
	 */
	public getTab(name: string) : TabData {
		const result = this._mapName.get(name);
		if (result === undefined)
			throw new Error("Der Name des Tabs " + name + " ist nicht bekannt.");
		return result;
	}

	/**
	 * Gibt zurück, ob das Tab mit dem übergenenen Namen versteckt ist oder nicht.
	 *
	 * @param name   der Name des Tabs
	 */
	public isHidden(name: string) : boolean {
		return this.getTab(name).hide ?? false;
	}

	/**
	 * Gibt zurück, ob das Tab mit dem übergenenen Namen sichtbar ist oder nicht.
	 *
	 * @param name   der Name des Tabs
	 */
	public isVisible(name: string) : boolean {
		return !(this.getTab(name).hide ?? false);
	}

	/**
	 * Gibt das Bild als String zurück, welches mit den Tab-Daten des angegebenen Tabs
	 * verknüpft ist.
	 *
	 * @param name   der Name des Tabs
	 *
	 * @returns das Bild
	 */
	public getImage(name: string) : string | null {
		return this.getTab(name).image ?? null;
	}


	/**
	 * Gibt alle Tabs zurück, die der angegebenen Tab-Group zugeordnet sind.
	 *
	 * @param groupname   der Name der Tab-Group
	 *
	 * @returns das Array mit den Tabs
	 */
	public getTabsOfGroup(groupname : string) : Array<TabData> {
		return this._tabgroups.get(groupname) ?? new Array<TabData>();
	}

}
