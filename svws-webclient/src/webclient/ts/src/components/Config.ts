
/** Der type der asynchronen Callback-Funktionen zum Applikations-spezifischen Setzen von Konfigurationswerten */
export type ConfigSetter = (key: string, value: string) => Promise<void>;

/**
 * Diese Klasse beschreibt ein Konfigurationselement.
 */
export class ConfigElement {

	// Der Schlüssel des Konfigurationselementes
	private _key: string;

	// Der Typ des Konfigurationselementes (globale oder benutzer-spezifische Konfiguration)
	private _type: 'user' | 'global';

	// Ein Default-Wert für das Konfigurationselement, wenn kein Wert in der Konfiguration hinterlegt ist
	private _defaultValue: string;

	/**
	 * Erstellt ein neues Konfigurationselement.
	 *
	 * @param key            der Schlüssel des Konfigurationselements
	 * @param type           der Typ des Konfigurationselements (globale oder benutzer-spezifische Konfiguration)
	 * @param defaultValue   der Default-Wert des Konfigurationselements
	 */
	public constructor(key: string, type: 'user' | 'global', defaultValue: string) {
		this._key = key;
		this._defaultValue = defaultValue;
		this._type = type;
	}

	/** Der Schlüssel des Konfigurationselements */
	public get key(): string {
		return this._key;
	}

	/** Der Typ des Konfigurationselements (globale oder benutzer-spezifische Konfiguration) */
	public get type(): 'user' | 'global' {
		return this._type;
	}

	/** Der Default-Wert des Konfigurationselements */
	public get default(): string {
		return this._defaultValue;
	}

}

/**
 * Diese Klasse kümmert sich um die Verwaltung der Konfiguration für
 * Clients, welche die SVWS-Komponenten nutzen.
 */
export class Config {

	// Die globale Konfiguration
	private _mapGlobal: Map<string, string> = new Map();

	// Der Setter-Callback für die globale Konfiguration
	private _setGlobal: ConfigSetter

	// Die benutzerspezifische Konfiguration
	private _mapUser: Map<string, string> = new Map();

	// Der Setter-Callback für die benutzers-pezifische Konfiguration
	private _setUser: ConfigSetter

	// Alle bekannten Konfigurationselemente
	private _mapElements: Map<string, ConfigElement> = new Map();


	/**
	 * Erstellt eine neue Konfiguration mit der globalen und der benutzer-spezifischen
	 * Konfiguration.
	 *
	 * @param setGlobal   der Setter-Callback für die globale Konfiguration
	 * @param setUser     der Setter-Callback für die benutzers-pezifische Konfiguration
	 */
	public constructor(setGlobal: ConfigSetter, setUser: ConfigSetter) {
		this._setGlobal = setGlobal;
		this._setUser = setUser;
	}

	/**
	 * Setzt die Map für die globale Konfiguration. Dies dient nur zur Initialisierung
	 * und sollte ansonsten nicht aufgerufen werden.
	 *
	 * @param mapGlobal   die Map
	 */
	public set mapGlobal(mapGlobal: Map<string, string>) {
		this._mapGlobal = mapGlobal;
	}


	/**
	 * Setzt die Map für die benutzerspezifische Konfiguration. Dies dient nur zur
	 * Initialisierung und sollte ansonsten nicht aufgerufen werden.
	 *
	 * @param mapUser   die Map
	 */
	public set mapUser(mapUser: Map<string, string>) {
		this._mapUser = mapUser;
	}


	/**
	 * Fügt ein neues Konfigurationselement zu der Konfiguration hinzu.
	 *
	 * @param elem   das hinzuzufügende Konfigurationselement
	 */
	public addElement(elem: ConfigElement) {
		this._mapElements.set(elem.key, elem);
	}

	/**
	 * Fügt die Konfigurationselement zu der Konfiguration hinzu.
	 *
	 * @param elem   die hinzuzufügenden Konfigurationselemente
	 */
	public addElements(elements: Iterable<ConfigElement>) {
		for (const elem of elements)
			this.addElement(elem);
	}

	/**
	 * Gibt den Wert für das Konfigurationselement mit dem Schlüssel key zurück.
	 *
	 * @param key   der Schlüssel des Konfigurationselements
	 *
	 * @returns der Wert des Konfigurationselements als string
	 *
	 * @throws wenn das Konfigurationselement nicht existiert
	 */
	public getValue(key: string): string {
		// Prüfe, ob das Konfigurationselement bekannt ist oder nicht
		const elem: ConfigElement | undefined = this._mapElements.get(key);
		if (elem === undefined)
			throw Error("Es ist kein Konfigurations-Element mit dem Schlüssel '" + key + "' bekannt.");
		// Lese den Wert aus der entsprechenden Konfiguration (Benutzer oder Global) aus - falls vorhanden...
		const value: string | undefined = (elem.type === 'global') ? this._mapGlobal.get(key) : this._mapUser.get(key);
		// Ist ein Wert kein vorhanden, so gebe den Default-Wert zurück und im anderen Fall den gespeicherten Wert
		return (value === undefined) ? elem.default : value;
	}

	/**
	 * Setzt den Wert für das Konfigurationselement mit dem Schlüssel key auf den Default-Wert.
	 *
	 * @param key     der Schlüssel des Konfigurationselements
	 *
	 * @throws wenn das Konfigurationselement nicht existiert
	 */
	public async setDefaultValue(key: string): Promise<void> {
		// Prüfe, ob das Konfigurationselement bekannt ist oder nicht
		const elem: ConfigElement | undefined = this._mapElements.get(key);
		if (elem === undefined)
			throw Error("Es ist kein Konfigurations-Element mit dem Schlüssel '" + key + "' bekannt.");
		await this.setValue(key, elem.default);
	}

	/**
	 * Setzt den Wert für das Konfigurationselement mit dem Schlüssel key auf den Wert value.
	 *
	 * @param key     der Schlüssel des Konfigurationselements
	 * @param value   der Wert
	 *
	 * @throws wenn das Konfigurationselement nicht existiert
	 */
	public async setValue(key: string, value: string): Promise<void> {
		// Prüfe, ob das Konfigurationselement bekannt ist oder nicht
		const elem: ConfigElement | undefined = this._mapElements.get(key);
		if (elem === undefined)
			throw Error("Es ist kein Konfigurations-Element mit dem Schlüssel '" + key + "' bekannt.");
		if (elem.type === 'global') {
			await this._setGlobal(key, value);
			this._mapGlobal.set(key, value);
		} else if (elem.type === 'user') {
			await this._setUser(key, value);
			this._mapUser.set(key, value);
		}
	}

	/**
	 * Gibt den Wert für das Konfigurationselement mit dem Schlüssel key als number zurück.
	 *
	 * @param key   der Schlüssel des Konfigurationselements
	 *
	 * @returns der Wert des Konfigurationselements als number
	 *
	 * @throws wenn das Konfigurationselement nicht existiert
	 */
	public getNumberValue(key: string): number {
		// Prüfe, ob das Konfigurationselement bekannt ist oder nicht
		const elem: ConfigElement | undefined = this._mapElements.get(key);
		if (elem === undefined)
			throw Error("Es ist kein Konfigurations-Element mit dem Schlüssel '" + key + "' bekannt.");
		// Lese den Wert aus der entsprechenden Konfiguration (Benutzer oder Global) aus - falls vorhanden...
		const value: string | undefined = (elem.type === 'global') ? this._mapGlobal.get(key) : this._mapUser.get(key);
		// Ist ein Wert kein vorhanden, so gebe den Default-Wert zurück und im anderen Fall den gespeicherten Wert
		return Number((value === undefined) ? elem.default : value)
	}

	/**
	 * Setzt den Wert für das Konfigurationselement mit dem Schlüssel key auf den Wert value.
	 *
	 * @param key     der Schlüssel des Konfigurationselements
	 * @param value   der Wert
	 *
	 * @throws wenn das Konfigurationselement nicht existiert
	 */
	public async setNumberValue(key: string, value: number): Promise<void> {
		// Prüfe, ob das Konfigurationselement bekannt ist oder nicht
		const elem: ConfigElement | undefined = this._mapElements.get(key);
		if (elem === undefined)
			throw Error("Es ist kein Konfigurations-Element mit dem Schlüssel '" + key + "' bekannt.");
		if (elem.type === 'global') {
			await this._setGlobal(key, value.toString());
			this._mapGlobal.set(key, value.toString());
		} else if (elem.type === 'user') {
			await this._setUser(key, value.toString());
			this._mapUser.set(key, value.toString());
		}
	}

	/**
	 * Gibt den Wert für das Konfigurationselement mit dem Schlüssel key als boolean zurück.
	 *
	 * @param key   der Schlüssel des Konfigurationselements
	 *
	 * @returns der Wert des Konfigurationselements als boolean
	 *
	 * @throws wenn das Konfigurationselement nicht existiert
	 */
	public getBooleanValue(key: string): boolean {
		// Prüfe, ob das Konfigurationselement bekannt ist oder nicht
		const elem: ConfigElement | undefined = this._mapElements.get(key);
		if (elem === undefined)
			throw Error("Es ist kein Konfigurations-Element mit dem Schlüssel '" + key + "' bekannt.");
		// Lese den Wert aus der entsprechenden Konfiguration (Benutzer oder Global) aus - falls vorhanden...
		const value: string | undefined = (elem.type === 'global') ? this._mapGlobal.get(key) : this._mapUser.get(key);
		// Ist ein Wert kein vorhanden, so gebe den Default-Wert zurück und im anderen Fall den gespeicherten Wert
		return Boolean((value === undefined) ? elem.default : value)
	}

	/**
	 * Setzt den Wert für das Konfigurationselement mit dem Schlüssel key auf den Wert value.
	 *
	 * @param key     der Schlüssel des Konfigurationselements
	 * @param value   der Wert
	 *
	 * @throws wenn das Konfigurationselement nicht existiert
	 */
	public async setBooleanValue(key: string, value: boolean): Promise<void> {
		// Prüfe, ob das Konfigurationselement bekannt ist oder nicht
		const elem: ConfigElement | undefined = this._mapElements.get(key);
		if (elem === undefined)
			throw Error("Es ist kein Konfigurations-Element mit dem Schlüssel '" + key + "' bekannt.");
		if (elem.type === 'global') {
			await this._setGlobal(key, value.toString());
			this._mapGlobal.set(key, value.toString());
		} else if (elem.type === 'user') {
			await this._setUser(key, value.toString());
			this._mapUser.set(key, value.toString());
		}
	}


	/**
	 * Gibt den Wert für das Konfigurationselement mit dem Schlüssel key als Object zurück.
	 *
	 * @param key        der Schlüssel des Konfigurationselements
	 * @param fromJSON   eine Funktion zum Umwandeln eines Strings in das Objekt
	 *
	 * @returns der Wert des Konfigurationselements als Object
	 *
	 * @throws wenn das Konfigurationselement nicht existiert
	 */
	public getObjectValue<TObject>(key: string, fromJSON: (json : string) => TObject): TObject | null {
		// Prüfe, ob das Konfigurationselement bekannt ist oder nicht
		const elem: ConfigElement | undefined = this._mapElements.get(key);
		if (elem === undefined)
			throw Error("Es ist kein Konfigurations-Element mit dem Schlüssel '" + key + "' bekannt.");
		// Lese den Wert aus der entsprechenden Konfiguration (Benutzer oder Global) aus - falls vorhanden...
		let value: string | undefined = (elem.type === 'global') ? this._mapGlobal.get(key) : this._mapUser.get(key);
		// Ist ein Wert kein vorhanden, so gebe den Default-Wert zurück und im anderen Fall den gespeicherten Wert
		if (value === undefined)
			value = elem.default;
		// Bei einem leeren String gebe null zurück.
		if (value === "")
			return null;
		return fromJSON(value);
	}

	/**
	 * Setzt den Wert für das Konfigurationselement mit dem Schlüssel key auf den Wert value.
	 *
	 * @param key     der Schlüssel des Konfigurationselements
	 * @param value   der Wert
	 * @param toJSON  eine Funktion zum Wandeln des Objektes in einen JSON-String
	 *
	 * @throws wenn das Konfigurationselement nicht existiert
	 */
	public async setObjectValue<TObject>(key: string, value: TObject | null, toJSON: (obj : TObject) => string): Promise<void> {
		// Prüfe, ob das Konfigurationselement bekannt ist oder nicht
		const elem: ConfigElement | undefined = this._mapElements.get(key);
		if (elem === undefined)
			throw Error("Es ist kein Konfigurations-Element mit dem Schlüssel '" + key + "' bekannt.");
		const strValue = value === null ? "" : toJSON(value);
		if (elem.type === 'global') {
			await this._setGlobal(key, strValue);
			this._mapGlobal.set(key, strValue);
		} else if (elem.type === 'user') {
			await this._setUser(key, strValue);
			this._mapUser.set(key, strValue);
		}
	}

}