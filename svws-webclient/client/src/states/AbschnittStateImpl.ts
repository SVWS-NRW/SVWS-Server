import type { List, SchuleStammdaten, Schuljahresabschnitt } from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";
import type { AbschnittState } from "@ui";
import { StateManager } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { schuleStateImpl } from "./SchuleStateImpl";

interface AbschnittReactiveState {
	/** Eine Map mit den Schuljahresabschnitten zugeordnet zu deren IDs */
	mapAbschnitte: Map<number, Schuljahresabschnitt>;

	/** Eine Liste der Schuljahresabschnitte */
	listAbschnitte: List<Schuljahresabschnitt>;

	/** Der aktuell ausgewählte Schuljahresabschnitts */
	auswahl: Schuljahresabschnitt | null;

}

/**
 * Die Schnittstelle für den Zustand des aktuell ausgewählten Abschnitts und der Liste der möglichen Schuljahresabschnitte
 */
export class AbschnittStateImpl extends StateManager<AbschnittReactiveState> implements AbschnittState {

	public constructor() {
		super({
			mapAbschnitte: new Map(),
			listAbschnitte: new ArrayList(),
			auswahl: null,
		});
	}

	public init(stammdaten: SchuleStammdaten, resetAuswahl: boolean = true): void {
		const mapAbschnitte = new Map();
		for (const a of stammdaten.abschnitte) {
			mapAbschnitte.set(a.id, a);
		}
		const abschnitt = mapAbschnitte.get(stammdaten.idSchuljahresabschnitt);
		if (abschnitt === undefined) {
			throw new DeveloperNotificationException("Der aktuelle Schuljahresabschnitt der Schule existiert nicht in der Liste der Schuljahresabschnitte.");
		}
		this.setPatchedDefaultState({
			listAbschnitte: stammdaten.abschnitte,
			mapAbschnitte,
			auswahl: ((this.state.auswahl !== null) || resetAuswahl) ? abschnitt : this.state.auswahl,
		});
	}

	public async setAuswahl(id: number): Promise<void> {
		const auswahl = this.get(id);
		this.setPatchedState({ auswahl });
		await RouteManager.instance.setAbschnitt(id);
	}

	public getOrNull(id: number): Schuljahresabschnitt | null {
		return this.state.mapAbschnitte.get(id) ?? null;
	}

	public get(id: number): Schuljahresabschnitt {
		const abschnitt = this.getOrNull(id);
		if (abschnitt === null) {
			throw new DeveloperNotificationException(`Der Schuljahresabschnitt mit der ID ${id} wurde nicht gefunden`);
		}
		return abschnitt;
	}

	public get auswahl(): Schuljahresabschnitt {
		if (this.state.auswahl === null) {
			throw new DeveloperNotificationException("Die Abschnittauswahl wurde noch nicht initialisiert, es besteht keine Verbindung zum Server");
		}
		return this.state.auswahl;
	}

	public get alle(): List<Schuljahresabschnitt> {
		return this.state.listAbschnitte;
	}

	public getBySchuljahrUndHalbjahr(schuljahr: number, halbjahr: number): Schuljahresabschnitt | null {
		let result: Schuljahresabschnitt | null = null;
		for (const a of this.state.mapAbschnitte.values()) {
			if ((a.schuljahr === schuljahr) && (a.abschnitt === halbjahr)) {
				result = a;
				break;
			}
		}
		return result;
	}

	public istSchuljahresabschnittAktuell(): boolean {
		return (this.auswahl.schuljahr === schuleStateImpl.abschnitt.schuljahr) && (this.auswahl === schuleStateImpl.abschnitt);
	}

	public istSchuljahresabschnittPlanung(): boolean {
		return (this.auswahl.schuljahr > schuleStateImpl.abschnitt.schuljahr)
				|| ((this.auswahl.schuljahr === schuleStateImpl.abschnitt.schuljahr) && (this.auswahl > schuleStateImpl.abschnitt));
	}

	public istSchuljahresabschnittVergangenheit(): boolean {
		return (this.auswahl.schuljahr < schuleStateImpl.abschnitt.schuljahr)
				|| ((this.auswahl.schuljahr === schuleStateImpl.abschnitt.schuljahr) && (this.auswahl.abschnitt < schuleStateImpl.abschnitt.abschnitt));
	}

}

export const abschnittStateImpl = new AbschnittStateImpl();
