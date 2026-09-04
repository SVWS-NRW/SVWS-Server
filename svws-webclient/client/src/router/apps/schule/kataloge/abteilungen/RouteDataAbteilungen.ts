import type { RouteStateAuswahlInterface } from "~/router/RouteDataAuswahl";
import { RouteDataAuswahl } from "~/router/RouteDataAuswahl";
import type { RouteParamsRawGeneric } from "vue-router";
import { routeAbteilungenGruppenprozesse } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungenGruppenprozesse";
import { routeAbteilungenDaten } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungenDaten";
import { routeAbteilungenNeu } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungenNeu";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import type { KlassenDatenMinimal } from "@core/asd/data/klassen/KlassenDatenMinimal";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { Abteilung } from "@core/core/data/schule/Abteilung";
import { AbteilungKlassenzuordnung } from "@core/core/data/schule/AbteilungKlassenzuordnung";
import { ArrayList } from "@core/java/util/ArrayList";
import { Arrays } from "@core/java/util/Arrays";
import type { List } from "@core/java/util/List";
import { AbteilungenListeManager } from "@ui/ui/manager/kataloge/AbteilungenListeManager";
import { ViewType } from "@ui/ui/nav/ViewType";


const defaultState = {
	idSchuljahresabschnitt: -1,
	manager: undefined,
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
		const [abteilungenAktAbschnitt, lehrer, klassenAktAbschnitt] = await Promise.all([
			api.server.getAbteilungenByIdJahresAbschnitt(api.schema, idSchuljahresabschnitt),
			api.server.getLehrer(api.schema),
			api.server.getKlassenDatenMinimalBySchuljahresabschnitt(api.schema, idSchuljahresabschnitt),
		]);

		const abteilungenFolgeAbschnitt = new ArrayList<Abteilung>();
		const klassenFolgeAbschnitt = new ArrayList<KlassenDatenMinimal>();
		const schuljahresabschnitt = abschnittStateImpl.get(idSchuljahresabschnitt);
		if ((schuleStateImpl.abschnitt.id === schuljahresabschnitt.id) && (schuljahresabschnitt.idFolgeAbschnitt !== null)) {
			const [abteilungenByIdJahresabschnitt, klassenDatenMinimal] = await Promise.all([
				api.server.getAbteilungenByIdJahresAbschnitt(api.schema, schuljahresabschnitt.idFolgeAbschnitt),
				api.server.getKlassenDatenMinimalBySchuljahresabschnitt(api.schema, schuljahresabschnitt.idFolgeAbschnitt),
			]);
			abteilungenFolgeAbschnitt.addAll(abteilungenByIdJahresabschnitt);
			klassenFolgeAbschnitt.addAll(klassenDatenMinimal);
		}

		const manager = new AbteilungenListeManager(
			idSchuljahresabschnitt,
			schuleStateImpl.abschnitt.id,
			schuleStateImpl.schulform, {
				schuljahresabschnitte: abschnittStateImpl.alle,
				abteilungenAktAbschnitt,
				abteilungenFolgeAbschnitt,
				lehrer,
				klassenAktAbschnitt,
				klassenFolgeAbschnitt,
			}
		);
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
		const schuljahresabschnitt = abschnittStateImpl.getOrNull(abteilung?.idSchuljahresabschnitt ?? -1);
		const schuljahresabschnittText = (schuljahresabschnitt === null) ?
			'???' : `${schuljahresabschnitt.schuljahr}/${(schuljahresabschnitt.schuljahr + 1) % 100}.${schuljahresabschnitt.abschnitt}`;
		return `Abteilung ${abteilung?.bezeichnung ?? '???'} (Abschnitt: ${schuljahresabschnittText}, ID: ${idAbteilung}) wurde erfolgreich gelöscht.`;
	}

	add = async (abteilung: Partial<Abteilung>, assignedKlassenIds: List<number>, addAbteilungInFolgeAbschnitt: boolean): Promise<number> => {
		const idAktAbschnitt = abschnittStateImpl.auswahl.id;
		const idFolgeAbschnitt = abschnittStateImpl.auswahl.idFolgeAbschnitt;

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
		return (abschnittStateImpl.auswahl.id !== schuleStateImpl.abschnitt.id) && (abschnittStateImpl.auswahl.id !== schuleStateImpl.abschnitt.idFolgeAbschnitt);
	}

	get isAbteilungImZukuenftigenAbschnitt() {
		return abschnittStateImpl.auswahl.id === schuleStateImpl.abschnitt.idFolgeAbschnitt;
	}

}
