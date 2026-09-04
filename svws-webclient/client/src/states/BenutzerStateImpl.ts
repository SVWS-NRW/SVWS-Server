import type { BenutzerDaten } from "@core/core/data/benutzer/BenutzerDaten";
import type { BenutzerEMailDaten } from "@core/core/data/benutzer/BenutzerEMailDaten";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { BenutzerTyp } from "@core/core/types/benutzer/BenutzerTyp";
import type { BenutzerState } from "@ui/states/BenutzerState";
import { StateManager } from "@ui/ui/StateManager";
import { AES } from "@ui/utils/crypto/aes";
import { AESAlgo } from "@ui/utils/crypto/aesAlgo";
import { api } from "~/router/Api";

interface BenutzerReactiveState {
	// Gibt an, ob der Client beim Server authentifiziert ist
	authenticated: boolean;

	// Die Benutzerdaten des angemeldeten Benutzers
	benutzerdaten: BenutzerDaten | undefined;

	// Das benutzerspezifische AES-Objekt zur Verschlüsselung
	aes: AES | undefined;

	// Gibt an, ob der Benutzer Administrator-Rechte hat oder nicht (direkt oder indirekt über eine Gruppen-Zugehörigkeit)
	istAdmin: boolean;

	// Gibt die Kompetenzen des Benutzers zurück, die der Benutzer direkt oder indirekt über eine Gruppen-Zugehörigkeit besitzt
	kompetenzen: Set<BenutzerKompetenz>;

	// Enthält die Klassen-IDs, auf denen der Benutzer aufgrund einer Klassen- oder Abteilungsleitung funktionsbezogene Kompetenzen hat
	kompetenzenKlasse: Set<number>;

	// Enthält die Abiturjahrgänge, bei denen der Benutzer als Beratungslehrer funktionsbezogene Kompetenzen hat
	kompetenzenAbiturjahrgaenge: Set<number>;

	// Die Maildaten des Benutzers
	benutzerEMailDaten: BenutzerEMailDaten | undefined;

	// Das Initialkennwort für den WeNoM
	wenomInitialkennwort: string | null;

}

/**
 * Die Schnittstelle für den Zustand des Benutzers
 */
export class BenutzerStateImpl extends StateManager<BenutzerReactiveState> implements BenutzerState {

	public constructor() {
		super({
			authenticated: false,
			benutzerdaten: undefined,
			aes: undefined,
			istAdmin: false,
			kompetenzen: new Set(),
			kompetenzenKlasse: new Set(),
			kompetenzenAbiturjahrgaenge: new Set(),
			benutzerEMailDaten: undefined,
			wenomInitialkennwort: null,
		});
	}

	public async init(username: string, password: string): Promise<void> {
		const benutzerdaten = await api.server.getBenutzerDatenEigene(api.schema);
		const istAdmin = this.getIstAdmin(benutzerdaten);
		const kompetenzen = this.getKompetenzen(benutzerdaten);
		const kompetenzenKlasse = this.getKompetenzenKlasse(benutzerdaten);
		const kompetenzenAbiturjahrgaenge = this.getKompetenzenAbiturjahrgaenge(benutzerdaten);
		const aesKey = await AES.getKey256(password, username);
		const aes = new AES(AESAlgo.CBC, aesKey);
		const benutzerEMailDaten = await api.server.getBenutzerEmailDaten(api.schema);

		this.setPatchedDefaultState({ benutzerdaten, authenticated: true, istAdmin, kompetenzen, kompetenzenKlasse, kompetenzenAbiturjahrgaenge, aes, benutzerEMailDaten });
	}

	// Gibt den Status zurück, ob der Benutzer authentifiziert wurde
	get authenticated(): boolean {
		return this.state.authenticated;
	}

	// Gibt die Benutzerdaten des angemeldeten Benutzers zurück, sofern ein Login stattgefunden hat
	get benutzerdaten(): BenutzerDaten {
		if (this.state.benutzerdaten === undefined) {
			throw new DeveloperNotificationException("Ein Benutzer muss angemeldet sein, damit dessen Daten geladen sein können.");
		}
		return this.state.benutzerdaten;
	}

	// Gibt die Benutzerdaten des angemeldeten Benutzers zurück, sofern ein Login stattgefunden hat
	get benutzerEMailDaten(): BenutzerEMailDaten {
		if (this.state.benutzerEMailDaten === undefined) {
			throw new DeveloperNotificationException("Ein Benutzer muss angemeldet sein, damit dessen Daten geladen sein können.");
		}
		return this.state.benutzerEMailDaten;
	}
	// Gibt ein Promise zurück mit einem AES-Schlüssel
	get aes(): AES {
		if (this.state.aes === undefined) {
			throw new DeveloperNotificationException("Das AES-Objekt ist nicht definiert");
		}
		return this.state.aes;
	}

	// Gibt an, sofern ein Login stattgefunden hat, ob es sich bei dem angemeldeten Benutzer um einen Administrator handelt oder nicht
	get istAdmin(): boolean {
		return this.state.istAdmin;
	}

	// Die Kompetenzen des angemeldeten Benutzers, sofern ein Login stattgefunden hat
	get kompetenzen(): Set<BenutzerKompetenz> {
		return this.state.kompetenzen;
	}

	// Die Klassen-IDs, auf denen der angemeldete Benutzer aufgrund einer Klassen- oder Abteilungsleitung funktionsbezogene Kompetenzen hat
	get kompetenzenKlasse(): Set<number> {
		return this.state.kompetenzenKlasse;
	}

	// Die Abiturjahrgänge, auf denen der angemeldete Benutzer als Beratungslehrer funktionsbezogene Kompetenzen hat
	get kompetenzenAbiturjahrgaenge(): Set<number> {
		return this.state.kompetenzenAbiturjahrgaenge;
	}

	/**
	 * Ermittelt, ob der Benutzer mit den angebenen Daten ein administrativer
	 * Benutzer ist oder nicht.
	 *
	 * @param daten   die Daten des Benutzers
	 *
	 * @returns true, falls der benutzer Administrator-Rechte hat, und ansonsten false
	 */
	protected getIstAdmin(daten: BenutzerDaten): boolean {
		if (daten.istAdmin) {
			return true;
		}
		for (const gruppe of daten.gruppen) {
			if (gruppe.istAdmin) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Ermittelt, die Menge an Kompetenzen, die der Benutzer mit den angebenen Daten
	 * entweder direkt oder indirekt über eine Gruppe hat.
	 *
	 * @param daten   die Daten des Benutzers
	 *
	 * @returns die Menge an Kompetenzen
	 */
	protected getKompetenzen(daten: BenutzerDaten): Set<BenutzerKompetenz> {
		const result: Set<BenutzerKompetenz> = new Set();
		// Jeder Benutzer hat die Kompetenz auf Teile der Applikation zuzugreifen, die keine Kompetenz benötigen
		result.add(BenutzerKompetenz.KEINE);
		// Ein Admin-Benutzer hat alle Kompetenzen...
		const istAdmin = this.getIstAdmin(daten);
		if (istAdmin) {
			result.add(BenutzerKompetenz.ADMIN);
			for (const k of BenutzerKompetenz.values()) {
				result.add(k);
			}
			return result;
		}
		// Lese die Kompetenzen ein, die der Benutzer direkt hat
		for (const kid of daten.kompetenzen) {
			const k = BenutzerKompetenz.getByID(kid);
			if (k !== null) {
				result.add(k);
			}
		}
		// Lese die Kompetenzen ein, die der Benutzer indirekt über eine Gruppe hat
		for (const gruppe of daten.gruppen) {
			for (const kid of gruppe.kompetenzen) {
				const k = BenutzerKompetenz.getByID(kid);
				if (k !== null) {
					result.add(k);
				}
			}
		}
		return result;
	}


	/**
	 * Ermittelt, die Menge an Klassen-IDs, auf denen der Benutzer aufgrund einer Klassen- oder Abteilungsleitung
	 * funktionsbezogene Kompetenzen hat.
	 *
	 * @param daten   die Daten des Benutzers
	 *
	 * @returns die Menge an Klassen-IDs
	 */
	protected getKompetenzenKlasse(daten: BenutzerDaten): Set<number> {
		const result = new Set<number>();
		for (const id of daten.kompetenzenKlassen) {
			result.add(id);
		}
		return result;
	}


	/**
	 * Ermittelt, die Menge an Abiturjahrgängen, bei denen der Benutzer als Beratungslehrer
	 * funktionsbezogene Kompetenzen hat.
	 *
	 * @param daten   die Daten des Benutzers
	 *
	 * @returns die Menge an Abiturjahrgängen
	 */
	protected getKompetenzenAbiturjahrgaenge(daten: BenutzerDaten): Set<number> {
		const result = new Set<number>();
		for (const id of daten.kompetenzenAbiturjahrgaenge) {
			result.add(id);
		}
		return result;
	}

	public benutzerHatKompetenz(kompetenz: BenutzerKompetenz): boolean {
		return this.kompetenzen.has(kompetenz);
	}

	public benutzerHatEineKompetenz(kompetenzen: Iterable<BenutzerKompetenz>): boolean {
		for (const kompetenz of kompetenzen) {
			if (this.kompetenzen.has(kompetenz)) {
				return true;
			}
		}
		return false;
	}

	public get benutzertyp(): BenutzerTyp {
		const typ = BenutzerTyp.getByID(this.benutzerdaten.typ);
		if (typ === null) {
			throw new DeveloperNotificationException("Der Typ des Benutzers ist ungültig.");
		}
		return typ;
	}

	public get benutzerIDLehrer(): number {
		if (this.benutzertyp !== BenutzerTyp.LEHRER) {
			throw new DeveloperNotificationException("Der Benutzer ist kein Lehrer, weshalb keine Lehrer-ID ermittelt werden kann.");
		}
		return this.benutzerdaten.typID;
	}

	public async getWenomInitialkennwort() {
		const wenomInitialkennwort = await api.server.getENMLehrerInitialKennwort(api.schema, this.benutzerIDLehrer);
		this.setPatchedState({ wenomInitialkennwort });
	};

	public get wenomInitialkennwort(): string | null {
		return this.state.wenomInitialkennwort;
	}

	public async patchBenutzerdaten(data: Partial<BenutzerDaten>) {
		// console.log("TODO: Benutzerdaten patchen");
	}

	public async patchBenutzerpasswort(eins: string, zwei: string): Promise<boolean> {
		if (eins !== zwei) {
			return false;
		}
		const password = eins.length > 0 ? eins : null;
		try {
			await api.server.setPassword(password, api.schema, this.benutzerdaten.id);
			return true;
		} catch {
			return false;
		}
	};

	public async passwordResetWenom() {
		try {
			if (this.benutzertyp !== BenutzerTyp.LEHRER) {
				return false;
			}
			await api.server.resetENMLehrerPasswordToInitial(api.schema, this.benutzerIDLehrer);
			return true;
		} catch {
			return false;
		}
	};

	public async patchBenutzerEMailDaten(data: Partial<BenutzerEMailDaten>) {
		await api.server.patchBenutzerEmailDaten(data, api.schema);
	};

}

export const benutzerStateImpl = new BenutzerStateImpl();
