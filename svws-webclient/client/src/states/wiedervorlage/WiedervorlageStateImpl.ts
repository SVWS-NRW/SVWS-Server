import type { WiedervorlageState } from "@ui";
import { StateManager } from "@ui";
import type { List, BenutzergruppeListeEintrag, WiedervorlageEintrag } from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";
import { benutzerStateImpl } from "../BenutzerStateImpl";

interface BenutzerGruppen {
	data: List<BenutzergruppeListeEintrag>
}

interface Wiedervorlagen {
	data: List<WiedervorlageEintrag>;
}

interface WiedervorlageReactiveState {
	benutzerGruppen: BenutzerGruppen;
	wiedervorlagenListe: Wiedervorlagen;
}

/**
 * Die Schnittstelle für die Anzeige der Wiedervorlagenliste und ihrer API-Methoden
 */
export class WiedervorlageStateImpl extends StateManager<WiedervorlageReactiveState> implements WiedervorlageState {

	public constructor() {
		super({
			benutzerGruppen: { data: new ArrayList<BenutzergruppeListeEintrag>() },
			wiedervorlagenListe: { data: new ArrayList<WiedervorlageEintrag>() },
		});
	}

	public get benutzerGruppen(): List<BenutzergruppeListeEintrag> {
		return this.state.benutzerGruppen.data;
	}

	/** Getter für die Wiedervorlage-Liste */
	public get wiedervorlagenListe(): List<WiedervorlageEintrag> {
		return this.state.wiedervorlagenListe.data;
	}

	/** Initialisierung des States - lädt alle Daten */
	public async init() {
		await Promise.all([
			this.setBenutzergruppen(),
			// TODO lade wiedervorlagen anzahl
		]);
	}

	/** Lädt die kompletten Daten für Wiedervorlagen */
	public async ladeWiedervorlagen(): Promise<void> {
		let wiedervorlagenListe: List<WiedervorlageEintrag>;
		try {
			wiedervorlagenListe = await api.server.getWiedervorlageListe(api.schema);
		} catch {
			throw new DeveloperNotificationException("Das Laden der Wiedervorlagen ist fehlgeschlagen.");
		}
		this.setPatchedState({ wiedervorlagenListe: { data: wiedervorlagenListe } });

	}

	/** Erstellt eine Wiedervorlage */
	public async addWiedervorlage(data: Partial<WiedervorlageEintrag>): Promise<WiedervorlageEintrag> {
		let response: WiedervorlageEintrag;
		try {
			response = await api.server.addWiedervorlageEintrag(data, api.schema);
		} catch {
			throw new DeveloperNotificationException("Das Anlegen der Wiedervorlage ist fehlgeschlagen.");
		}
		await this.ladeWiedervorlagen();
		return response;
	}

	/** Patcht eine Wiedervorlage */
	public async patchWiedervorlage(data: Partial<WiedervorlageEintrag>, id: number): Promise<void> {
		try {
			await api.server.patchWiedervorlageEintrag(data, api.schema, id);
		} catch {
			throw new DeveloperNotificationException("Das Bearbeiten der Wiedervorlage ist fehlgeschlagen.");
		}
		await this.ladeWiedervorlagen();
	}

	/** Wechselt den Erledigungsstatus einer Wiedervorlage */
	public async toggleWiedervorlageErledigung(data: WiedervorlageEintrag): Promise<boolean> {
		const isErledigt = data.tsErledigt !== null;
		const { id } = data;

		try {
			await api.server.patchWiedervorlageEintragErledigung({ erledigt: !isErledigt }, api.schema, id);
		} catch {
			throw new DeveloperNotificationException("Das Setzen des Status der Wiedervorlage ist fehlgeschlagen.");
		}
		await this.ladeWiedervorlagen();
		return !isErledigt;
	}

	/** Setzt die Benutzergruppen */
	public async setBenutzergruppen(): Promise<void> {
		let data: List<BenutzergruppeListeEintrag>;
		try {
			if (benutzerStateImpl.istAdmin) {
				// für "admin" alle Benutzergruppen zurückgeben
				data = await api.server.getBenutzergruppenliste(api.schema);
			} else {
				// sonst nur eigene Benutzergruppen zurückgeben
				const benutzerdaten = benutzerStateImpl.benutzerdaten;
				data = benutzerdaten.gruppen;
			}
		} catch {
			throw new DeveloperNotificationException("Das Laden der Benutzergruppen ist fehlgeschlagen.");
		}
		this.setPatchedState({ benutzerGruppen: { data } });
	}
}

export const wiedervorlageStateImpl = new WiedervorlageStateImpl();
