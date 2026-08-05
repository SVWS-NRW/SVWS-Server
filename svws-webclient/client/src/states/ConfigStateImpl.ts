import { ref } from "vue";
import { DeveloperNotificationException } from "@core";
import { Config } from "@ui";
import type { ConfigState } from "@ui";
import { api } from "~/router/Api";

/**
 * Die Schnittstelle für den Zustand der Konfiguration
 */
class ConfigStateImpl implements ConfigState {

	// Die Konfiguration des angemeldeten Benutzers zurück, sofern diese geladen wurde
	protected _config;

	// Die nicht persistente Konfiguration des angemeldeten Benutzers
	protected _nonPersistentConfig;

	/**
	 * Erstellt einen neuen ConfigState
	 */
	public constructor() {
		this._config = ref<Config | undefined>(new Config(this.setConfigGlobal, this.setConfigUser));
		this._nonPersistentConfig = ref<Config>(new Config(async (_, __) => {}, async (_, __) => { }));
	}

	/**
	 * Initialisiert die Konfiguration, indem die Client- und die Benutzer-Konfiguration vom Server
	 * geladen wird. Die nicht-persistente Konfiguration wird dabei zurückgesetzt.
	 */
	public async init(): Promise<void> {
		const cfg = await api.server.getClientConfig(api.schema, 'SVWS-Client');
		const mapUser = new Map<string, string>();
		for (const c of cfg.user) {
			mapUser.set(c.key, c.value);
		}
		const mapGlobal = new Map<string, string>();
		for (const c of cfg.global) {
			mapGlobal.set(c.key, c.value);
		}
		this.config.mapGlobal = mapGlobal;
		this.config.mapUser = mapUser;
		// nicht-persistente Config ebenfalls anlegen
		this.nonPersistentConfig.mapGlobal = new Map<string, string>();
		this.nonPersistentConfig.mapUser = new Map<string, string>();
	}


	/**
	 * Leert die komplette Konfiguration
	 */
	public clear(): void {
		this.config.mapGlobal = new Map();
		this.config.mapUser = new Map();
		this.nonPersistentConfig.mapGlobal = new Map();
		this.nonPersistentConfig.mapUser = new Map();
	}


	/**
	 * Gibt die Konfiguration des angemeldeten Benutzers zurück, sofern diese geladen wurde
	 */
	public get config(): Config {
		if (this._config.value === undefined) {
			throw new DeveloperNotificationException("Eine Konfiguration ist nicht vorhanden.");
		}
		return this._config.value as Config;
	}

	/**
	 * Gibt die nicht persistente Konfiguration des angemeldeten Benutzers zurück
	 */
	public get nonPersistentConfig(): Config {
		return this._nonPersistentConfig.value as Config;
	}

	/**
	 * Setzt den Benutzer-spezifischen Konfigurationseintrag
	 *
	 * @param key    der Schlüssel des Konfigurationseintrags
	 * @param value  der Wert des Konfigurationseintrags
	 */
	private readonly setConfigUser = async (key: string, value: string): Promise<void> => {
		await api.server.setClientConfigUserKey(value, api.schema, 'SVWS-Client', key);
	};

	/**
	 * Setzt den globalen Konfigurationseintrag
	 *
	 * @param key    der Schlüssel des Konfigurationseintrags
	 * @param value  der Wert des Konfigurationseintrags
	 */
	private readonly setConfigGlobal = async (key: string, value: string): Promise<void> => {
		await api.server.setClientConfigGlobalKey(value, api.schema, 'SVWS-Client', key);
	};

}


export const configStateImpl = new ConfigStateImpl();
