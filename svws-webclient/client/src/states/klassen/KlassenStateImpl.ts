import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KlassenDatenMinimal } from "@core/asd/data/klassen/KlassenDatenMinimal";
import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import { Arrays } from "@core/java/util/Arrays";
import type { List } from "@core/java/util/List";
import { ViewType } from "@ui/ui/nav/ViewType";

import { abschnittStateImpl } from "../AbschnittStateImpl";
import { benutzerStateImpl } from "../BenutzerStateImpl";
import type { GenericAuswahlReactiveState } from "../GenericAuswahlStateImpl";
import { GenericAuswahlStateImpl } from "../GenericAuswahlStateImpl";
import { schuleStateImpl } from "../SchuleStateImpl";
import { api } from "~/router/Api";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteManager } from "~/router/RouteManager";

import { KlassenListeManager } from "./KlassenListeManager";
import type { KlassenState } from "./KlassenState";
import { klassenStateRoutingAdapter } from "./KlassenStateRoutingAdapter";

interface KlassenReactiveState extends GenericAuswahlReactiveState<KlassenListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
}

/**
 * Der State für die Auswahlliste der Klassen
 */
export class KlassenStateImpl extends GenericAuswahlStateImpl<KlassenListeManager, KlassenReactiveState> implements KlassenState {

	public constructor() {
		super({
			idSchuljahresabschnitt: -1,
			manager: undefined,
			mapStundenplaene: new Map(),
			activeViewType: ViewType.DEFAULT,
		}, klassenStateRoutingAdapter);
	}

	/**
	 * Initialisiert den State für den ausgewählten Schuljahresabschnitt. Es werden die Daten für diesen Abschnitt geladen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param force                    gibt an, ob das Laden der Daten auch erzwungen werden soll, wenn die ID des
	 *                                 Schuljahresabschnittes bereits gesetzt ist (z.B. beim Betreten einer Route)
	 *
	 * @returns ein Promise mit der ID der Auswahl oder null
	 */
	public async init(idSchuljahresabschnitt: number, force: boolean): Promise<number | null> {
		return await super.setSchuljahresabschnitt(idSchuljahresabschnitt, force);
	}

	/**
	 * Gibt die ID des aktuell gewählten Schuljahresabschnittes zurück
	 *
	 * @returns die ID des aktuell gewählten Schuljahresabschnittes
	 */
	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	/**
	 * Gibt eine Map mit den Stundenplänen des Schuljahresabschnittes zugeordnet zu deren ID zurück.
	 *
	 * @returns die Map mit den Stundenplänen
	 */
	public get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	/**
	 * Erstellt einen neunen Auswahl-Manager für den angegebenen Schuljahresabschnitt. Die Daten dafür werden
	 * über die API abgefragt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 *
	 * @returns Eine Promise mit den Anpassungen für den Manager und dessen Daten im KlassenState
	 */
	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<KlassenState>> {
		const schuljahresabschnitt = abschnittStateImpl.getOrNull(idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		}
		// Lade die Kataloge und erstelle den Manager

		const [klassen, klassenVorabschnitt, klassenFolgeabschnitt, schueler, lehrer, jahrgaenge] = await Promise.all([
			api.server.getListKlassenListeEintragBySchuljahresabschnitt(api.schema, idSchuljahresabschnitt),
			this.getKlassenVorabschnitt(schuljahresabschnitt),
			this.getKlassenFolgeabschnitt(schuljahresabschnitt),
			api.server.getSchuelerFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getLehrerFuerAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getJahrgaenge(api.schema),
		])
		;
		const manager = new KlassenListeManager(
			idSchuljahresabschnitt,
			schuleStateImpl.abschnitt.id,
			schuleStateImpl.schulform,
			{
				schuljahresabschnitte: abschnittStateImpl.alle,
				klassenAktAbschnitt: klassen,
				klassenVorabschnitt: klassenVorabschnitt,
				klassenFolgeabschnitt: klassenFolgeabschnitt,
				schueler: schueler,
				jahrgaenge: jahrgaenge,
				lehrer: lehrer,
			});
		if (this._state.value.manager === undefined) {
			manager.setFilterAuswahlPermitted(true);
		} else {
			manager.useFilter(this._state.value.manager);
		}
		return { manager };
	}

	/**
	 * Ermittelt die Klassen für den vorigen Schuljahresabschnitt
	 *
	 * @param schuljahresabschnitt   der aktuelle Schuljahresabschnitt, von dem ausgegangen wird
	 *
	 * @returns eine Promise mit der Liste der Klassen
	 */
	private getKlassenVorabschnitt(schuljahresabschnitt: Schuljahresabschnitt) {
		return (schuljahresabschnitt.idVorigerAbschnitt === null)
			? Promise.resolve(new ArrayList<KlassenDatenMinimal>())
			: api.server.getKlassenDatenMinimalBySchuljahresabschnitt(api.schema, schuljahresabschnitt.idVorigerAbschnitt);
	}

	/**
	 * Ermittelt die Klassen für den folgenden Schuljahresabschnitt
	 *
	 * @param schuljahresabschnitt   der folgende Schuljahresabschnitt, von dem ausgegangen wird
	 *
	 * @returns eine Promise mit der Liste der Klassen
	 */
	private getKlassenFolgeabschnitt(schuljahresabschnitt: Schuljahresabschnitt) {
		return (schuljahresabschnitt.idFolgeAbschnitt === null)
			? Promise.resolve(new ArrayList<KlassenDatenMinimal>())
			: api.server.getKlassenDatenMinimalBySchuljahresabschnitt(api.schema, schuljahresabschnitt.idFolgeAbschnitt);
	}

	/**
	 * Lädt die Daten für den übergebenen Listeneintrag einer Klasse
	 *
	 * @param auswahl   der ausgewählte Listeneintrag einer Klasse
	 *
	 * @returns eine Promise mit den Daten zu der Klasse
	 */
	public async ladeDaten(auswahl: KlassenListeEintrag | null): Promise<KlassenDaten | null> {
		if (auswahl === null) {
			return null;
		}
		return await api.server.getKlasse(api.schema, auswahl.id);
	}

	public async ladeDatenMultiple(auswahlList: List<KlassenListeEintrag>, state: Partial<KlassenReactiveState>): Promise<List<KlassenDaten> | null> {
		if (auswahlList.isEmpty()) {
			return null;
		}
		const promises = [];
		for (const auswahl of auswahlList) {
			promises.push(api.server.getKlasse(api.schema, auswahl.id));
		}
		const res = await Promise.all(promises);
		return Arrays.asList(res);
	}

	/**
	 * Führt einen Patch auf den Klassendaten über die API aus.
	 *
	 * @param data   die Daten des Patches
	 * @param id     die der zu patchenden Klasse
	 *
	 * @returns eine Promise, die bei Erfolg true zurückgibt
	 */
	protected async doPatch(data: Partial<KlassenDaten>, id: number): Promise<boolean> {
		await api.server.patchKlasse(data, api.schema, id);
		return true;
	}

	/**
	 * Führt eine LöscheOperation auf dem Server für die Klassen mit den übergebenen IDs aus.
	 *
	 * @param ids   die IDs der Klassen
	 *
	 * @returns eine Promise mit dem Ergebis der Lösch-Operation
	 */
	protected async doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteKlassen(ids, api.schema);
	}

	protected filterOnDelete(ids: List<number>): List<number> {
		const neueIDs = new ArrayList<number>();
		const set = this.manager.getKlassenIDsMitSchuelern();
		for (const id of ids) {
			if (!set.contains(id)) {
				neueIDs.add(id);
			}
		}
		return neueIDs;
	}

	protected deleteMessage(id: number, klasse: KlassenDaten | null): string {
		return `Klasse ${klasse?.kuerzel ?? '???'} (ID: ${id}) wurde erfolgreich gelöscht.`;
	}

	public async updateMapStundenplaene(): Promise<void> {
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		if (benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
			const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, this.idSchuljahresabschnitt);
			for (const l of listStundenplaene) {
				mapStundenplaene.set(l.id, l);
			}
		}
		this.setPatchedState({ mapStundenplaene });
	}

	public async addKlassenleitung(idLehrer: number, idKlasse: number): Promise<void> {
		// Prüfe zunächst, ob die Lehrer-ID bereits in der Liste der Klassenleitungen vorkommt
		if (this.manager.daten().klassenLeitungen.contains(idLehrer)) {
			throw new DeveloperNotificationException("Die Klassenleitung mit der Lehrer-ID " + idLehrer + " kommt bereits in der Klasse mit der ID " + idKlasse + "vor.");
		}

		// Erstelle die neue Klassenliste durch anhängen der neuen Lehrer-ID
		const listKlassenleitungenNeu = new ArrayList<number>(this.manager.daten().klassenLeitungen);
		listKlassenleitungenNeu.add(idLehrer);

		// Führe den API-Aufruf durch
		const requestBody: Partial<KlassenDaten> = { klassenLeitungen: listKlassenleitungenNeu };
		await api.server.patchKlasse(requestBody, api.schema, idKlasse);

		// Aktualisiere die Liste der Klassenleitungen im Erfolgsfall
		this.manager.daten().klassenLeitungen.add(idLehrer);
		this.commit();
	};

	public async removeKlassenleitung(eintrag: LehrerListeEintrag): Promise<void> {
		// Bestimme die Position der Klassenleitung in der zugehörigen Liste
		const listKlassenleitungenNeu = new ArrayList<number>(this.manager.daten().klassenLeitungen);
		const lehrerIndex: number = listKlassenleitungenNeu.indexOf(eintrag.id);
		listKlassenleitungenNeu.removeElementAt(lehrerIndex);

		// Führe den API-Aufruf durch
		const requestBody: Partial<KlassenDaten> = { klassenLeitungen: listKlassenleitungenNeu };
		const klassenId: number | null = this.manager.auswahlID();
		if (klassenId === null) {
			throw new DeveloperNotificationException("Keine Klasse ausgewählt, Klassenleitung kann nicht entfernt werden");
		}
		await api.server.patchKlasse(requestBody, api.schema, klassenId);

		// Aktualisiere die Liste der Klassenleitungen im Erfolgsfall
		this.manager.daten().klassenLeitungen = listKlassenleitungenNeu;
		this.commit();
	};

	public async updateReihenfolgeKlassenleitung(idLehrer: number, erhoehe: boolean): Promise<void> {
		const idKlasse: number | null = this.manager.auswahlID();
		if (idKlasse === null) {
			throw new DeveloperNotificationException("Für das Anpassen der Reihenfolge von Klassenlehrern muss eine Klasse ausgewählt sein.");
		}

		// Erstelle eine Kopie der Liste der Klassenleitungen und führe an dieser die Änderungen durch
		const listKlassenleitungenNeu = new ArrayList<number>(this.manager.daten().klassenLeitungen);
		if (!KlassenListeManager.updateReihenfolgeKlassenleitung(listKlassenleitungenNeu, idLehrer, erhoehe)) {
			return;
		}

		// Führe den API-Aufruf durch
		const requestBody: Partial<KlassenDaten> = { klassenLeitungen: listKlassenleitungenNeu };
		await api.server.patchKlasse(requestBody, api.schema, idKlasse);

		// Aktualisiere die Liste der Klassenleitungen im Erfolgsfall
		this.manager.daten().klassenLeitungen = listKlassenleitungenNeu;
		this.commit();
	};

	public async setzeDefaultSortierung(): Promise<void> {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahl_id = this.manager.auswahl().id;
		await api.server.setKlassenSortierungFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		await this.setSchuljahresabschnitt(idSchuljahresabschnitt, true);
		await this.setDaten(this.manager.liste.get(auswahl_id));
	};

	public async add(partialKlasse: Partial<KlassenDaten>): Promise<void> {
		const neueKlasse = await api.server.addKlasse({ ...partialKlasse, idSchuljahresabschnitt: abschnittStateImpl.auswahl.id }, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(neueKlasse.id);
	};

	public async gotoSchueler(eintrag: Schueler): Promise<void> {
		await RouteManager.doRoute(routeSchueler.getRoute({ id: eintrag.id }));
	};

	public async gotoLehrer(eintrag: LehrerListeEintrag): Promise<void> {
		await RouteManager.doRoute(routeLehrer.getRoute({ id: eintrag.id }));
	};

}

export const klassenStateImpl = new KlassenStateImpl();
