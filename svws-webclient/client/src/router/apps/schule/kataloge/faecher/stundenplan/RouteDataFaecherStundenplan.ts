import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { routeFaecherStundenplan } from "./RouteFaecherStundenplan";
import { configStateImpl } from "~/states/ConfigStateImpl";
import type { StundenplanKalenderwochenzuordnung } from "@core/core/data/stundenplan/StundenplanKalenderwochenzuordnung";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import type { StundenplanPausenaufsicht } from "@core/core/data/stundenplan/StundenplanPausenaufsicht";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
import { ArrayList } from "@core/java/util/ArrayList";


interface RouteStateFachDataStundenplan extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	idFach: number | undefined;
	auswahl: StundenplanListeEintrag | undefined;
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	manager: StundenplanManager | undefined;
	wochentyp: number;
	kalenderwoche: StundenplanKalenderwochenzuordnung | undefined,
}

const defaultState = <RouteStateFachDataStundenplan> {
	idSchuljahresabschnitt: -1,
	idFach: undefined,
	auswahl: undefined,
	mapStundenplaene: new Map(),
	manager: undefined,
	wochentyp: 0,
	kalenderwoche: undefined,
};


export class RouteDataFaecherStundenplan extends RouteData<RouteStateFachDataStundenplan> {

	public constructor() {
		super(defaultState);
	}

	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	get idFach(): number {
		if (this._state.value.idFach === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Fach-ID nicht festgelegt, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		}
		return this._state.value.idFach;
	}

	get wochentyp(): number {
		return this._state.value.wochentyp;
	}

	get kalenderwoche(): StundenplanKalenderwochenzuordnung | undefined {
		return this._state.value.kalenderwoche;
	}

	get hatAuswahl(): boolean {
		return this._state.value.auswahl !== undefined;
	}

	get auswahl(): StundenplanListeEintrag {
		if (this._state.value.auswahl === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Stundenplaneintrag nicht festgelegt, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		}
		return this._state.value.auswahl;
	}

	get hasManager(): boolean {
		return this._state.value.manager !== undefined;
	}

	get manager(): StundenplanManager {
		if (this._state.value.manager === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Stundenplan-Manager nicht vorhanden, es können keine Informationen zum Stundenplan abgerufen oder eingegeben werden.");
		}
		return this._state.value.manager;
	}

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	get ganzerStundenplan(): boolean {
		return configStateImpl.config.getValue("schule.faecher.stundenplan.ganzerStundenplan") === 'true';
	}

	setGanzerStundenplan = async (value: boolean) => {
		await configStateImpl.config.setValue("schule.faecher.stundenplan.ganzerStundenplan", value ? "true" : "false");
	};

	public async ladeListe(): Promise<boolean> {
		const idSchuljahresabschnitt = abschnittStateImpl.auswahl.id;
		if (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt) {
			return false;
		}
		const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		const auswahl = listStundenplaene.size() > 0 ? listStundenplaene.get(0) : undefined;
		for (const l of listStundenplaene) {
			mapStundenplaene.set(l.id, l);
		}
		this.setPatchedDefaultState({ idSchuljahresabschnitt, auswahl, mapStundenplaene });
		return true;
	}

	private getKalenderWoche(manager: StundenplanManager, wochentyp: number, kwjahr: number | undefined, kw: number | undefined): { wochentyp?: number, kalenderwoche?: StundenplanKalenderwochenzuordnung } {
		const result: { wochentyp?: number, kalenderwoche?: StundenplanKalenderwochenzuordnung } = {};
		try {
			result.kalenderwoche = manager.kalenderwochenzuordnungGetByJahrAndKWOrException(kwjahr === undefined ? -1 : kwjahr, kw === undefined ? -1 : kw);
			result.wochentyp = result.kalenderwoche.wochentyp;
		} catch (e) {
			result.kalenderwoche = undefined;
			result.wochentyp = wochentyp;
		}
		if ((result.wochentyp < 0) || (result.wochentyp > manager.getWochenTypModell())) {
			throw new DeveloperNotificationException("Der Wochentyp " + result.wochentyp.toString() + " passt nicht zum Wochentyp-Modell " + this.manager.getWochenTypModell().toString() + " des Stundenplans");
		}
		return result;
	}

	public async setEintrag(idFach: number, idStundenplan: number | undefined, wochentypNr: number, kwjahr: number | undefined, kw: number | undefined) {
		// Prüfe, ob die vorge Auswahl mit der neuen Auswahl übereinstimmt. In diesem Fall ist keine Aktion nötig
		const vorige_auswahl = this._state.value.auswahl;
		if ((vorige_auswahl !== undefined) && (this._state.value.idFach === idFach) && (vorige_auswahl.id === idStundenplan)) {
			if ((wochentypNr !== this._state.value.wochentyp) || (kwjahr !== this._state.value.kalenderwoche?.jahr)
				|| (kw !== this._state.value.kalenderwoche?.kw)) {
				const tmpKW = this.getKalenderWoche(this.manager, wochentypNr, kwjahr, kw);
				this.setPatchedState({ wochentyp: tmpKW.wochentyp, kalenderwoche: tmpKW.kalenderwoche });
				return;
			}
			return;
		}
		// Prüfe, ob der Stundenplan deselektiert wird. In diesem Fall muss der interne State zurückgesetzt werden.
		if (idStundenplan === undefined) {
			this.setPatchedState({ idFach: undefined, auswahl: undefined, manager: undefined, wochentyp: 0, kalenderwoche: undefined });
			return;
		}
		// Ermittle aus der Liste der Stundenpläne den Stundenplan
		const auswahl = this.mapStundenplaene.get(idStundenplan);
		if (auswahl === undefined) {
			this.setPatchedState({ idFach: undefined, auswahl: undefined, manager: undefined, wochentyp: 0, kalenderwoche: undefined });
			return;
		}
		// Lade den Fach-Stundenplan
		const daten = await api.server.getStundenplan(api.schema, auswahl.id);
		const unterrichtsdaten = await api.server.getStundenplanUnterrichte(api.schema, auswahl.id);
		// const pausenaufsichten = await api.server.getStundenplanPausenaufsichten(api.schema, auswahl.id);
		const unterrichtsverteilung = await api.server.getStundenplanUnterrichtsverteilung(api.schema, auswahl.id);
		const manager = new StundenplanManager(daten, unterrichtsdaten, new ArrayList<StundenplanPausenaufsicht>(), unterrichtsverteilung);

		// Wochentyp und Kalenderwoche prüfen...
		const { wochentyp, kalenderwoche } = this.getKalenderWoche(manager, wochentypNr, kwjahr, kw);
		// Und zuletzt den internen State anpassen, um die Reaktivität der vue-Komponenten zu triggern
		this.setPatchedState({ idFach, auswahl, manager, wochentyp, kalenderwoche });
	}

	public gotoStundenplan = async (value: StundenplanListeEintrag) => {
		await RouteManager.doRoute(routeFaecherStundenplan.getRoute({ idStundenplan: value.id, wochentyp: 0, kw: "" }));
	};

	public gotoWochentyp = async (wochentyp: number) => {
		await RouteManager.doRoute(routeFaecherStundenplan.getRoute({ wochentyp }));
	};

	public gotoKalenderwoche = async (value: StundenplanKalenderwochenzuordnung | undefined) => {
		const kw = (value === undefined) ? "" : value.jahr + "." + value.kw;
		const wochentyp = (value === undefined) ? "" : value.wochentyp;
		await RouteManager.doRoute(routeFaecherStundenplan.getRoute({ wochentyp, kw }));
	};

}
