import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import type { Abteilung, KlassenListeEintrag, List, Schuljahresabschnitt, SimpleOperationResponse } from "@core";
import { AbteilungKlassenzuordnung, ArrayList, Arrays } from "@core";
import { AbteilungenListeManager, ViewType } from "@ui";
import { routeAbteilungenGruppenprozesse } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungenGruppenprozesse";
import { routeAbteilungenDaten } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungenDaten";
import { routeAbteilungenNeu } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungenNeu";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeApp } from "~/router/apps/RouteApp";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: new AbteilungenListeManager(-1, -1, new ArrayList(), null, new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()),
	view: routeAbteilungenDaten,
	activeViewType: ViewType.DEFAULT,
	oldView: undefined,
};

export class RouteDataAbteilungen extends RouteDataAuswahl<AbteilungenListeManager, RouteStateAuswahlInterface<AbteilungenListeManager>> {

	public constructor() {
		super(defaultState, { gruppenprozesse: routeAbteilungenGruppenprozesse, hinzufuegen: routeAbteilungenNeu });
	}

	public addID(param: RouteParamsRawGeneric, idAbteilung: number) {
		param.id = idAbteilung;
	}

	protected async createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteStateAuswahlInterface<AbteilungenListeManager>>> {
		const abteilungen = await api.server.getAbteilungenByIdJahresAbschnitt(api.schema, idSchuljahresabschnitt);
		const lehrer = await api.server.getLehrer(api.schema);
		const klassenAktAbschnitt = await api.server.getListKlassenListeEintragBySchuljahresabschnitt(api.schema, idSchuljahresabschnitt);

		const abteilungenFolgeAbschnitt = new ArrayList<Abteilung>();
		const klassenFolgeAbschnitt = new ArrayList<KlassenListeEintrag>();
		const schuljahresabschnitt = api.mapAbschnitte.value.get(idSchuljahresabschnitt);
		if (this.istSchuleAbschnittUndHatFolgeAbschnitt(schuljahresabschnitt)) {
			abteilungenFolgeAbschnitt.addAll(await api.server.getAbteilungenByIdJahresAbschnitt(api.schema, schuljahresabschnitt.idFolgeAbschnitt!));
			klassenFolgeAbschnitt.addAll(await api.server.getListKlassenListeEintragBySchuljahresabschnitt(api.schema, schuljahresabschnitt.idFolgeAbschnitt!));
		}

		const manager = new AbteilungenListeManager(idSchuljahresabschnitt, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,
			api.schulform, abteilungen, abteilungenFolgeAbschnitt, lehrer, klassenAktAbschnitt, klassenFolgeAbschnitt);
		return { manager };
	}

	public async ladeDaten(auswahl: Abteilung): Promise<Abteilung> {
		return auswahl;
	}

	protected async doPatch(abteilungPatch: Partial<Abteilung>, idAbteilung: number): Promise<boolean> {
		await api.server.patchAbteilung(abteilungPatch, api.schema, idAbteilung);

		const abteilungFolgeAbschnitt = this.manager.abteilungenFolgeAbschnittByBezeichnung.get(this.manager.daten().bezeichnung);
		if (abteilungFolgeAbschnitt !== null) {
			await api.server.patchAbteilung(abteilungPatch, api.schema, abteilungFolgeAbschnitt.id);
		}
		return true;
	}

	protected async doDelete(idsAbteilungen: List<number>): Promise<List<SimpleOperationResponse>> {
		const idsAbteilungenToDelete = new ArrayList<number>();
		if (this.manager.deleteAbteilungenInFolgeAbschnitt) {
			for (const idAbteilung of idsAbteilungen) {
				idsAbteilungenToDelete.add(idAbteilung);
				const abteilung = this.manager.liste.get(idAbteilung);
				const abteilungFolgeAbschnitt = this.manager.abteilungenFolgeAbschnittByBezeichnung.get(abteilung?.bezeichnung);
				if (abteilungFolgeAbschnitt !== null) {
					idsAbteilungenToDelete.add(abteilungFolgeAbschnitt.id);
				}
			}
		} else {
			idsAbteilungenToDelete.addAll(idsAbteilungen);
		}

		return await api.server.deleteAbteilungen(idsAbteilungenToDelete, api.schema);
	}

	protected deleteMessage(idAbteilung: number, _: Abteilung | null): string {
		const abteilung = this.manager.liste.get(idAbteilung) ?? this.manager.abteilungenFolgeAbschnittById.get(idAbteilung);
		const schuljahresabschnitt = api.mapAbschnitte.value.get(abteilung?.idSchuljahresabschnitt ?? -1);
		const schuljahresabschnittText = (schuljahresabschnitt === undefined) ?
			'???' : `${schuljahresabschnitt.schuljahr}/${(schuljahresabschnitt.schuljahr + 1) % 100}.${schuljahresabschnitt.abschnitt}`;
		return `Abteilung ${abteilung?.bezeichnung ?? '???'} (Abschnitt: ${schuljahresabschnittText}, ID: ${idAbteilung}) wurde erfolgreich gelöscht.`;
	}

	add = async (abteilung: Partial<Abteilung>, assignedKlassenIds: List<number>, addAbteilungInFolgeAbschnitt: boolean): Promise<number> => {
		const idAktAbschnitt = routeApp.data.aktAbschnitt.value.id;
		const idFolgeAbschnitt = routeApp.data.aktAbschnitt.value.idFolgeAbschnitt;

		const abteilungAktAbschnitt = await this.addAbteilungWithKlassenIds(abteilung, idAktAbschnitt, assignedKlassenIds);
		this.manager.liste.add(abteilungAktAbschnitt);
		this.manager.setDaten(abteilungAktAbschnitt);

		const bezeichnungNotExistsInFolgeAbschnitt = !this.manager.abteilungenFolgeAbschnittByBezeichnung.containsKey(abteilungAktAbschnitt.bezeichnung);
		if (addAbteilungInFolgeAbschnitt && (idFolgeAbschnitt !== null) && bezeichnungNotExistsInFolgeAbschnitt) {
			const assignedKlassenIdsFolgeAbschnitt = this.manager.getKlassenIdsFuerFolgeAbschnitt(assignedKlassenIds);
			const abteilungFolgeAbschnitt = await this.addAbteilungWithKlassenIds(abteilung, idFolgeAbschnitt, assignedKlassenIdsFolgeAbschnitt);
			this.manager.addAbteilungFolgeAbschnitt(abteilungFolgeAbschnitt);
		}

		this.commit();
		return abteilungAktAbschnitt.id;
	};

	addKlassenzuordnungen = async (idAbteilung: number, idsKlassen: List<number>): Promise<void> => {
		const klassenzuordnungenAktAbschnittToCreate = this.createKlassenzuordnungObjects(idAbteilung, idsKlassen);
		const klassenzuordnungenAktAbschnitt = await api.server.addAbteilungKlassenzuordnung(klassenzuordnungenAktAbschnittToCreate, api.schema);
		this.manager.addKlassenzuordnungen(klassenzuordnungenAktAbschnitt);

		const abteilungFolgeAbschnitt = this.manager.abteilungenFolgeAbschnittByBezeichnung.get(this.manager.daten().bezeichnung);
		if (abteilungFolgeAbschnitt !== null) {
			const idsKlassenFolgeAbschnitt = this.manager.getKlassenIdsFuerFolgeAbschnitt(idsKlassen);
			const klassenzuordnungenFolgeAbschnittToCreate = this.createKlassenzuordnungObjects(abteilungFolgeAbschnitt.id, idsKlassenFolgeAbschnitt);
			const klassenZuordnungenFolgeAbschnitt = await api.server.addAbteilungKlassenzuordnung(klassenzuordnungenFolgeAbschnittToCreate, api.schema);
			abteilungFolgeAbschnitt.klassenzuordnungen.addAll(klassenZuordnungenFolgeAbschnitt);
		}

		this.commit();
	};

	deleteKlassenzuordnungen = async (klassenzuordnungen: List<AbteilungKlassenzuordnung>): Promise<void> => {
		const idsKlassenzuordnungen = Arrays.asList([...klassenzuordnungen].map(zuordnung => zuordnung.id));
		await api.server.deleteAbteilungKlassenzuordnung(idsKlassenzuordnungen, api.schema);
		this.manager.deleteKlassenzuordnungen(klassenzuordnungen);

		const abteilungFolgeAbschnitt = this.manager.abteilungenFolgeAbschnittByBezeichnung.get(this.manager.daten().bezeichnung);
		if (abteilungFolgeAbschnitt !== null) {
			const idsKlassenzuordnungenFolgeAbschnitt = this.manager.getKlassenzuordnungenIdsFolgeAbschnitt(abteilungFolgeAbschnitt, klassenzuordnungen);
			await api.server.deleteAbteilungKlassenzuordnung(idsKlassenzuordnungenFolgeAbschnitt, api.schema);
		}

		this.commit();
	};

	goToLehrer = async (idLehrer: number) => {
		await RouteManager.doRoute(routeLehrer.getRoute({ id: idLehrer }));
	};

	private istSchuleAbschnittUndHatFolgeAbschnitt(schuljahresabschnitt: Schuljahresabschnitt | undefined): schuljahresabschnitt is Schuljahresabschnitt {
		return (schuljahresabschnitt !== undefined) && (api.schuleStammdaten.idSchuljahresabschnitt === schuljahresabschnitt.id)
				&& (schuljahresabschnitt.idFolgeAbschnitt !== null);
	}

	private async addAbteilungWithKlassenIds(abteilung: Partial<Abteilung>, idAktAbschnitt: number, assignedKlassenIds: List<number>) {
		const abteilungAktAbschnitt = await api.server.addAbteilung(abteilung, api.schema, idAktAbschnitt);
		const klassenZuordnungenAktAbschnittToCreate = this.createKlassenzuordnungObjects(abteilungAktAbschnitt.id, assignedKlassenIds);
		const klassenZuordnungenAktAbschnitt = await api.server.addAbteilungKlassenzuordnung(klassenZuordnungenAktAbschnittToCreate, api.schema);
		abteilungAktAbschnitt.klassenzuordnungen.addAll(klassenZuordnungenAktAbschnitt);
		return abteilungAktAbschnitt;
	}

	private createKlassenzuordnungObjects(idAbteilung: number, idsKlassen: List<number>) {
		const klassenzuordnungen = new ArrayList<AbteilungKlassenzuordnung>();
		for (const idKlasse of idsKlassen) {
			const zuordnung = new AbteilungKlassenzuordnung();
			zuordnung.idAbteilung = idAbteilung;
			zuordnung.idKlasse = idKlasse;
			const { id, ...partialData } = zuordnung;
			klassenzuordnungen.add(partialData as AbteilungKlassenzuordnung);
		}
		return klassenzuordnungen;
	}

	get isReadonly() {
		return (routeApp.data.aktAbschnitt.value.id !== api.abschnitt.id) && (routeApp.data.aktAbschnitt.value.id !== api.abschnitt.idFolgeAbschnitt);
	}

	get isAbteilungImZukuenftigenAbschnitt() {
		return routeApp.data.aktAbschnitt.value.id === api.abschnitt.idFolgeAbschnitt;
	}

}
