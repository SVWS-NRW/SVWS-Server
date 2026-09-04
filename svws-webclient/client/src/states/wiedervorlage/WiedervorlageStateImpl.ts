import { api } from "~/router/Api";
import { benutzerStateImpl } from "../BenutzerStateImpl";
import type { BenutzergruppeListeEintrag } from "@core/core/data/benutzer/BenutzergruppeListeEintrag";
import type { WiedervorlageEintrag } from "@core/core/data/schule/WiedervorlageEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { WiedervorlageState } from "@ui/states/WiedervorlageState";
import { StateManager } from "@ui/ui/StateManager";
import { notificationStateImpl } from "../NotificationsStateImpl";

interface BenutzerGruppen {
	data: List<BenutzergruppeListeEintrag>
}

interface Wiedervorlagen {
	data: List<WiedervorlageEintrag>;
}

interface WiedervorlageReactiveState {
	anzahlOffeneWiedervorlagen: number;
	wiedervorlagenListe: List<WiedervorlageEintrag>;
	benutzerGruppen: List<BenutzergruppeListeEintrag>;
}

/**
 * Die Schnittstelle für die Anzeige der Wiedervorlagenliste und ihrer API-Methoden
 */
export class WiedervorlageStateImpl extends StateManager<WiedervorlageReactiveState> implements WiedervorlageState {

	public constructor() {
		super({
			anzahlOffeneWiedervorlagen: 0,
			wiedervorlagenListe: new ArrayList<WiedervorlageEintrag>(),
			benutzerGruppen: new ArrayList<BenutzergruppeListeEintrag>(),
		});
	}

	/** Initialisierung des States - lädt alle Daten */
	public async init() {
		const [benutzerGruppen, anzahlOffeneWiedervorlagen] = await Promise.all([
			this.ladeBenutzergruppen(),
			this.ladeAnzahlOffenWiedervorlagen(),
		]);

		this.setPatchedDefaultState({ benutzerGruppen, anzahlOffeneWiedervorlagen });

		if (this.state.anzahlOffeneWiedervorlagen > 0) {
			const anzahl = this.state.anzahlOffeneWiedervorlagen;
			const text = anzahl === 1 ? `Es liegt 1 offene Wiedervorlage vor.` : `Es liegen ${anzahl} offene Wiedervorlagen vor.`;
			setTimeout(() => notificationStateImpl.warning("Hinweis", text, 5000), 3000);
		}
	}

	/** Getter für die möglichen Benutzergruppen bei der Erstellung einer Wiedervorlage */
	public get benutzerGruppen(): List<BenutzergruppeListeEintrag> {
		return this.state.benutzerGruppen;
	}

	/** Getter für die Wiedervorlage-Liste */
	public get wiedervorlagenListe(): List<WiedervorlageEintrag> {
		return this.state.wiedervorlagenListe;
	}

	public get anzahlOffeneWiedervorlagen(): number {
		return this.state.anzahlOffeneWiedervorlagen;
	}

	/** Lädt die kompletten Daten für Wiedervorlagen */
	private async ladeWiedervorlagen(): Promise<List<WiedervorlageEintrag>> {
		let wiedervorlagenListe: List<WiedervorlageEintrag>;
		try {
			wiedervorlagenListe = await api.server.getWiedervorlageListe(api.schema);
		} catch {
			throw new DeveloperNotificationException("Das Laden der Wiedervorlagen ist fehlgeschlagen.");
		}
		return wiedervorlagenListe;
	}

	private async ladeAnzahlOffenWiedervorlagen(): Promise<number> {
		let anzahl: number;
		try {
			anzahl = await api.server.getAnzahlOffeneWiedervorlagen(api.schema);
		} catch {
			throw new DeveloperNotificationException("Das Laden der Wiedervorlagen ist fehlgeschlagen.");
		}
		return anzahl;
	}

	public async updateWiedervorlagen(): Promise<void> {
		const [wiedervorlagenListe, anzahlOffeneWiedervorlagen] = await Promise.all([
			this.ladeWiedervorlagen(),
			this.ladeAnzahlOffenWiedervorlagen(),
		]);

		this.setPatchedState({ wiedervorlagenListe, anzahlOffeneWiedervorlagen });
	}

	public async updateAnzahlOffeneWiedervorlagen(): Promise<void> {
		const anzahlOffeneWiedervorlagen = await this.ladeAnzahlOffenWiedervorlagen();

		if (anzahlOffeneWiedervorlagen !== this.state.anzahlOffeneWiedervorlagen) {
			this.setPatchedState({ anzahlOffeneWiedervorlagen });

		}
	}

	/** Erstellt eine Wiedervorlage */
	public async addWiedervorlage(data: Partial<WiedervorlageEintrag>): Promise<WiedervorlageEintrag> {
		let response: WiedervorlageEintrag;
		try {
			response = await api.server.addWiedervorlageEintrag(data, api.schema);
		} catch {
			throw new DeveloperNotificationException("Das Anlegen der Wiedervorlage ist fehlgeschlagen.");
		}
		await this.updateWiedervorlagen();
		return response;
	}

	/** Patcht eine Wiedervorlage */
	public async patchWiedervorlage(data: Partial<WiedervorlageEintrag>, id: number): Promise<void> {
		try {
			await api.server.patchWiedervorlageEintrag(data, api.schema, id);
		} catch {
			throw new DeveloperNotificationException("Das Bearbeiten der Wiedervorlage ist fehlgeschlagen.");
		}
		await this.updateWiedervorlagen();
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
		await this.updateWiedervorlagen();
		return !isErledigt;
	}

	/** Lädt die Benutzergruppen für die Erstellung einer Wiedervorlage */
	private async ladeBenutzergruppen(): Promise<List<BenutzergruppeListeEintrag>> {
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

		return data;
	}
}

export const wiedervorlageStateImpl = new WiedervorlageStateImpl();
