import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeKlasseGruppenprozesse } from "./RouteKlassenGruppenprozesse";
import type { RouteNode } from "~/router/RouteNode";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeKlassenNeu } from "~/router/apps/klassen/RouteKlassenNeu";
import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { KlassenListeManager } from "~/states/klassen/KlassenListeManager";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KlassenDatenMinimal } from "@core/asd/data/klassen/KlassenDatenMinimal";
import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { ViewType } from "@ui/ui/nav/ViewType";

interface RouteStateKlassen extends RouteStateAuswahlInterface<KlassenListeManager> {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
	oldView?: RouteNode<any, any>;
}

const defaultState: RouteStateKlassen = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
	mapStundenplaene: new Map(),
	view: routeKlassenDaten,
	oldView: undefined,
	activeViewType: ViewType.DEFAULT,
};

export class RouteDataKlassen extends RouteDataAuswahl<KlassenListeManager, RouteStateKlassen> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeKlasseGruppenprozesse, hinzufuegen: routeKlassenNeu });
	}

	public addID(param: RouteParamsRawGeneric, id: number): void {
		param.id = id;
	}

	get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateKlassen>> {
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

	private getKlassenVorabschnitt(schuljahresabschnitt: Schuljahresabschnitt) {
		return (schuljahresabschnitt.idVorigerAbschnitt === null)
			? Promise.resolve(new ArrayList<KlassenDatenMinimal>())
			: api.server.getKlassenDatenMinimalBySchuljahresabschnitt(api.schema, schuljahresabschnitt.idVorigerAbschnitt);
	}

	private getKlassenFolgeabschnitt(schuljahresabschnitt: Schuljahresabschnitt) {
		return (schuljahresabschnitt.idFolgeAbschnitt === null)
			? Promise.resolve(new ArrayList<KlassenDatenMinimal>())
			: api.server.getKlassenDatenMinimalBySchuljahresabschnitt(api.schema, schuljahresabschnitt.idFolgeAbschnitt);
	}

	public async ladeDaten(auswahl: KlassenListeEintrag | null): Promise<KlassenDaten | null> {
		if (auswahl === null) {
			return null;
		}
		return await api.server.getKlasse(api.schema, auswahl.id);
	}

	protected async doPatch(data: Partial<KlassenDaten>, id: number): Promise<boolean> {
		await api.server.patchKlasse(data, api.schema, id);
		return true;
	}

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

	public async updateMapStundenplaene() {
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		if (benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
			const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, this.idSchuljahresabschnitt);
			for (const l of listStundenplaene) {
				mapStundenplaene.set(l.id, l);
			}
		}
		this.setPatchedState({ mapStundenplaene });
	}

	addKlassenleitung = async (idLehrer: number, idKlasse: number): Promise<void> => {
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

	removeKlassenleitung = async (eintrag: LehrerListeEintrag) => {
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

	updateReihenfolgeKlassenleitung = async (idLehrer: number, erhoehe: boolean): Promise<void> => {
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

	setzeDefaultSortierung = async () => {
		const idSchuljahresabschnitt = this._state.value.idSchuljahresabschnitt;
		const auswahl_id = this.manager.auswahl().id;
		await api.server.setKlassenSortierungFuerAbschnitt(api.schema, idSchuljahresabschnitt);
		await this.setSchuljahresabschnitt(idSchuljahresabschnitt, true);
		await this.setDaten(this.manager.liste.get(auswahl_id));
	};

	add = async (partialKlasse: Partial<KlassenDaten>): Promise<void> => {
		const neueKlasse = await api.server.addKlasse({ ...partialKlasse, idSchuljahresabschnitt: abschnittStateImpl.auswahl.id }, api.schema);
		await this.setSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt, true);
		await this.gotoDefaultView(neueKlasse.id);
	};

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute({ id: eintrag.id }));
	};

	gotoLehrer = async (eintrag: LehrerListeEintrag) => {
		await RouteManager.doRoute(routeLehrer.getRoute({ id: eintrag.id }));
	};

}
