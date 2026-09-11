import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { GostBlockungListeneintrag } from "@core/core/data/gost/GostBlockungListeneintrag";
import type { GostBlockungsdaten } from "@core/core/data/gost/GostBlockungsdaten";
import type { GostBlockungsergebnis } from "@core/core/data/gost/GostBlockungsergebnis";
import type { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { UvFach } from "@core/core/data/uv/UvFach";
import { UvKlasse } from "@core/core/data/uv/UvKlasse";
import { UvKlassenLehrer } from "@core/core/data/uv/UvKlassenLehrer";
import { UvKurs } from "@core/core/data/uv/UvKurs";
import type { UvKursImportDaten } from "@core/core/data/uv/UvKursImportDaten";
import { UvLehrer } from "@core/core/data/uv/UvLehrer";
import { UvLehrerAnrechnungsstunden } from "@core/core/data/uv/UvLehrerAnrechnungsstunden";
import { UvLehrerPflichtstundensoll } from "@core/core/data/uv/UvLehrerPflichtstundensoll";
import { UvLerngruppe } from "@core/core/data/uv/UvLerngruppe";
import { UvLerngruppenLehrer } from "@core/core/data/uv/UvLerngruppenLehrer";
import type { UvLerngruppenSchiene } from "@core/core/data/uv/UvLerngruppenSchiene";
import { UvPlanungsabschnitt } from "@core/core/data/uv/UvPlanungsabschnitt";
import { UvPlanungsabschnittLehrer } from "@core/core/data/uv/UvPlanungsabschnittLehrer";
import { UvPlanungsabschnittSchueler } from "@core/core/data/uv/UvPlanungsabschnittSchueler";
import { UvPlanungsabschnittZeitraster } from "@core/core/data/uv/UvPlanungsabschnittZeitraster";
import { UvRaum } from "@core/core/data/uv/UvRaum";
import { UvRaumgruppe } from "@core/core/data/uv/UvRaumgruppe";
import { UvSchiene } from "@core/core/data/uv/UvSchiene";
import { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
import type { UvSchuelergruppeSchueler } from "@core/core/data/uv/UvSchuelergruppeSchueler";
import { UvSchuelerImportOptions } from "@core/core/data/uv/UvSchuelerImportOptions";
import { UvStundentafel } from "@core/core/data/uv/UvStundentafel";
import { UvStundentafelFach } from "@core/core/data/uv/UvStundentafelFach";
import { UvStundentafelImportOptions } from "@core/core/data/uv/UvStundentafelImportOptions";
import { UvUnterricht } from "@core/core/data/uv/UvUnterricht";
import type { UvUnterrichtLerngruppenlehrer } from "@core/core/data/uv/UvUnterrichtLerngruppenlehrer";
import type { UvUnterrichtRaum } from "@core/core/data/uv/UvUnterrichtRaum";
import { UvZeitraster } from "@core/core/data/uv/UvZeitraster";
import { UvZeitrasterEintrag } from "@core/core/data/uv/UvZeitrasterEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ListUtils } from "@core/core/utils/ListUtils";
import { UvManager } from "@core/core/utils/uv/UvManager";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { UvState, UvZeitrasterBlockOptions } from "@ui/states/UvState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";
import { abschnittStateImpl as abschnittState } from "~/states/AbschnittStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";

interface UvReactiveState {
	uvManager: UvManager;
	planungsabschnitt: UvPlanungsabschnitt | null;
}

const defaultState = <UvReactiveState> {
	uvManager: new UvManager(),
	planungsabschnitt: null,
};

export class UvStateImpl extends StateManager<UvReactiveState> implements UvState {

	private schuljahrLoaded = -1;

	public constructor() {
		super(defaultState);
	}

	public get uvManager(): UvManager {
		return this._state.value.uvManager;
	}

	public get planungsabschnitt(): UvPlanungsabschnitt | null {
		return this._state.value.planungsabschnitt;
	}

	public get hatKompetenzAllgemeinAendern(): boolean {
		return benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN);
	}

	public get hatKompetenzFunktionsbezogenAendern(): boolean {
		return benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_FUNKTIONSBEZOGEN_AENDERN);
	}

	public get hatKompetenzAendern(): boolean {
		return this.hatKompetenzAllgemeinAendern || this.hatKompetenzFunktionsbezogenAendern;
	}

	private get planungsabschnittOrException(): UvPlanungsabschnitt {
		const planungsabschnitt = this._state.value.planungsabschnitt;
		if ((planungsabschnitt === null) || (planungsabschnitt.id === -1)) {
			throw new DeveloperNotificationException("Kein UV-Planungsabschnitt gewaehlt.");
		}
		return planungsabschnitt;
	}

	private assertPlanungsabschnitt(): void {
		const planungsabschnitt = this._state.value.planungsabschnitt;
		if ((planungsabschnitt === null) || (planungsabschnitt.id === -1)) {
			throw new DeveloperNotificationException("Kein UV-Planungsabschnitt gewaehlt.");
		}
	}

	public reset(): void {
		this.schuljahrLoaded = -1;
		this.setPatchedDefaultState({ uvManager: new UvManager() });
	}

	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number, forceReload: boolean = false): Promise<List<UvPlanungsabschnitt> | null> {
		const schuljahresabschnitt = abschnittState.getOrNull(idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException('Es ist kein gültiger Schuljahresabschnitt ausgewählt');
		}
		if ((this.schuljahrLoaded === schuljahresabschnitt.schuljahr) && !forceReload) {
			return null;
		}
		this.schuljahrLoaded = schuljahresabschnitt.schuljahr;
		const listPlanungsabschnitte = await api.server.getUvPlanungsabschnitte(api.schema, schuljahresabschnitt.schuljahr);
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const fachdaten = await api.server.getFaecher(api.schema);
		const uvManager = new UvManager(listJahrgaenge, fachdaten);
		const data = await api.server.getUvGrunddaten(api.schema);
		uvManager.grunddatenBundleAdd(data);
		this.setPatchedState({ uvManager, planungsabschnitt: null });
		return listPlanungsabschnitte;
	}

	public async ladeDaten(auswahl: UvPlanungsabschnitt | null, planungsabschnitte: Iterable<UvPlanungsabschnitt>): Promise<UvPlanungsabschnitt | null> {
		if (auswahl === null) {
			this.setPatchedState({ planungsabschnitt: null });
			return null;
		}
		// Grunddaten benötigen die Zuordnungen aller Planungsabschnitte des Schuljahres für die Verwendungsprüfung.
		if (auswahl.id === -1) {
			for (const plan of planungsabschnitte) {
				if ((plan.id !== -1) && !this.uvManager.planungsabschnittIsGeladen(plan)) {
					const allData = await api.server.getUvPlanungsabschnittsdaten(api.schema, plan.id);
					this.uvManager.planungsabschnittsdatenBundleAdd(allData);
				}
			}
		}
		if ((auswahl.id !== -1) && (this.uvManager.planungsabschnittIsGeladen(auswahl) === false)) {
			const allData = await api.server.getUvPlanungsabschnittsdaten(api.schema, auswahl.id);
			this.uvManager.planungsabschnittsdatenBundleAdd(allData);
		}
		this.setPatchedState({ planungsabschnitt: auswahl });
		return auswahl;
	}

	/**
	 * Übergibt ein neues DTO an den Manager, solange die alten Indexschlüssel noch verfügbar sind.
	 */
	private patchManagerDto<T extends object>(existing: T, data: Partial<T>, dtoClass: new () => T, update: (patched: T) => void): void {
		const patched = Object.assign(new dtoClass(), existing, data);
		update(patched);
	}

	public async patch(id: number, data: Partial<UvPlanungsabschnitt>): Promise<boolean> {
		const planungsabschnitt = this.planungsabschnitt;
		if (planungsabschnitt?.id !== id) {
			throw new DeveloperNotificationException("Beim Patchen des UV-Planungsabschnitts sind keine gültigen Daten geladen.");
		}
		await api.server.patchUvPlanungsabschnitt({ id, ...data }, api.schema);
		this.patchManagerDto(planungsabschnitt, data, UvPlanungsabschnitt, patched => this.uvManager.planungsabschnittPatchAttributes(patched));
		this.setPatchedState({ planungsabschnitt: this.uvManager.planungsabschnittGetByIdOrException(id) });
		this.commit();
		return true;
	}

	public async deletePlanungsabschnitte(ids: List<number>): Promise<List<SimpleOperationResponse>> {
		return await api.server.deleteUvPlanungsabschnitteAsListSimpleOperationResponse(ids, api.schema);
	}

	public async add(partial: Partial<UvPlanungsabschnitt>): Promise<UvPlanungsabschnitt> {
		const neu = await api.server.createUvPlanungsabschnitt({ ...partial, schuljahr: abschnittState.auswahl.schuljahr }, api.schema);
		return neu;
	}

	public async addAsCopy(partial: Partial<UvPlanungsabschnitt>, _idFromUvPlanungsabschnitt?: number): Promise<UvPlanungsabschnitt> {
		return await this.add(partial);
	}

	public addZeitrasterZuordnung = async (idZeitraster: number): Promise<void> => {
		const planungsabschnitt = this._state.value.planungsabschnitt;
		if (planungsabschnitt === null) {
			return;
		}
		const zuordnung: Partial<UvPlanungsabschnittZeitraster> = {
			idPlanungsabschnitt: planungsabschnitt.id,
			idZeitraster: idZeitraster,
		};
		const neu = await api.server.createUvPlanungsabschnittZeitraster(zuordnung, api.schema);
		this.uvManager.planungsabschnittZeitrasterAdd(neu);
		this.commit();
	};

	public patchZeitrasterZuordnung = async (id: UvPlanungsabschnittZeitraster, data: JahrgangsDaten[]): Promise<void> => {
		const idsJahrgaenge = new ArrayList<number>();
		for (const d of data) {
			idsJahrgaenge.add(d.id);
		}
		const patch: Partial<UvPlanungsabschnittZeitraster> = { idPlanungsabschnitt: id.idPlanungsabschnitt, idZeitraster: id.idZeitraster, idsJahrgaenge };
		await api.server.patchUvPlanungsabschnittZeitraster(patch, api.schema);
		const existing = this.uvManager.planungsabschnittZeitrasterGetByIdOrException(id.idPlanungsabschnitt, id.idZeitraster);
		this.patchManagerDto(existing, patch, UvPlanungsabschnittZeitraster,
			patched => this.uvManager.planungsabschnittZeitrasterAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public addZeitrasterZuordnungen = async (idZeitrasterList: number[]): Promise<void> => {
		const planungsabschnitt = this._state.value.planungsabschnitt;
		if (planungsabschnitt === null) {
			return;
		}
		const zuordnungen = new ArrayList<Partial<UvPlanungsabschnittZeitraster>>();
		for (const idZeitraster of idZeitrasterList) {
			zuordnungen.add({
				idPlanungsabschnitt: planungsabschnitt.id,
				idZeitraster: idZeitraster,
			});
		}
		const neueZuordnungen = await api.server.createUvPlanungsabschnittZeitrasterMultiple(zuordnungen, api.schema);
		this.uvManager.planungsabschnittZeitrasterAddAll(neueZuordnungen);
		this.commit();
	};

	public removeZeitrasterZuordnung = async (idZeitraster: number): Promise<void> => {
		const planungsabschnitt = this._state.value.planungsabschnitt;
		if (planungsabschnitt === null) {
			return;
		}
		await api.server.deleteUvPlanungsabschnittZeitraster(api.schema, planungsabschnitt.id, idZeitraster);
		this.uvManager.planungsabschnittZeitrasterRemoveById(planungsabschnitt.id, idZeitraster);
		this.commit();
	};

	public removeZeitrasterZuordnungen = async (idZeitrasterList: number[]): Promise<void> => {
		const planungsabschnitt = this._state.value.planungsabschnitt;
		if (planungsabschnitt === null) {
			return;
		}
		const listIds = new ArrayList<number>();
		for (const id of idZeitrasterList) {
			listIds.add(id);
		}
		await api.server.deleteUvPlanungsabschnittZeitrasterMultiple(listIds, api.schema, planungsabschnitt.id);
		this.uvManager.planungsabschnittZeitrasterRemoveAllById(planungsabschnitt.id, listIds);
		this.commit();
	};

	public createKlasse = async (klasse: Partial<UvKlasse>, idSchuelergruppe: number | null, idsJahrgaenge: number[]): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		delete klasse.id;
		klasse.idPlanungsabschnitt = planungsabschnitt.id;
		if (idSchuelergruppe === null) {
			const idsJahrgaengeErlaubt = new ArrayList<number>();
			for (const id of idsJahrgaenge) {
				idsJahrgaengeErlaubt.add(id);
			}
			const neueSchuelergruppe: Partial<UvSchuelergruppe> = {
				idPlanungsabschnitt: planungsabschnitt.id,
				bezeichnung: `Klasse ${klasse.kuerzel ?? ''}`.trim(),
				idsJahrgaengeErlaubt,
			};
			const createdSchuelergruppe = await api.server.createUvSchuelergruppe(neueSchuelergruppe, api.schema);
			this.uvManager.schuelergruppeAdd(createdSchuelergruppe);
			klasse.idSchuelergruppe = createdSchuelergruppe.id;
			this.commit();
		} else {
			klasse.idSchuelergruppe = idSchuelergruppe;
		}
		const neu = await api.server.createUvKlasse(klasse, api.schema);
		this.uvManager.klasseAdd(neu);
		this.commit();
	};

	public delKlasse = async (klassen: UvKlasse[]): Promise<void> => {
		this.assertPlanungsabschnitt();
		const ids = new ArrayList<number>();
		const listKlassen = new ArrayList<UvKlasse>();
		for (const k of klassen) {
			ids.add(k.id);
			listKlassen.add(k);
		}
		await api.server.deleteUvKlassen(ids, api.schema);
		this.uvManager.klasseRemoveAll(listKlassen);
		klassen.length = 0;
		this.commit();
	};

	public patchKlasse = async (_idPlanungsabschnitt: number, idKlasse: number, klasse: Partial<UvKlasse>): Promise<void> => {
		await api.server.patchUvKlasse({ id: idKlasse, ...klasse }, api.schema);
		const existing = this.uvManager.klasseGetByIdOrException(idKlasse);
		this.patchManagerDto(existing, klasse, UvKlasse, patched => this.uvManager.klassePatchAttributes(patched));
		this.commit();
	};

	public addKlassenLehrer = async (data: Partial<UvKlassenLehrer>): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		delete data.id;
		data.idPlanungsabschnitt = planungsabschnitt.id;
		const neu = await api.server.createUvKlassenLehrer(data, api.schema);
		this.uvManager.klassenLehrerAdd(neu);
		this.commit();
	};

	public patchKlassenLehrer = async (id: number, data: Partial<UvKlassenLehrer>, _zuordnung: UvKlassenLehrer): Promise<void> => {
		await api.server.patchUvKlassenLehrer({ id, ...data }, api.schema);
		this.patchManagerDto(this.uvManager.klassenLehrerGetByZuordnungsIdOrException(id), data, UvKlassenLehrer,
			patched => this.uvManager.klassenLehrerAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public delKlassenLehrer = async (lehrerList: UvKlassenLehrer[]): Promise<void> => {
		const ids = new ArrayList<number>();
		const listZuordnungen = new ArrayList<UvKlassenLehrer>();
		for (const kl of lehrerList) {
			ids.add(kl.id);
			listZuordnungen.add(this.uvManager.klassenLehrerGetByZuordnungsIdOrException(kl.id));
		}
		await api.server.deleteUvKlassenLehrerMultiple(ids, api.schema);
		this.uvManager.klassenLehrerRemoveAll(listZuordnungen);
		lehrerList.length = 0;
		this.commit();
	};

	public createKurs = async (kurs: Partial<UvKurs>, idSchuelergruppe: number | null, idsJahrgaenge: number[]): Promise<UvKurs> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		delete kurs.id;
		kurs.idPlanungsabschnitt = planungsabschnitt.id;
		if (idSchuelergruppe === null) {
			const idsJahrgaengeErlaubt = new ArrayList<number>();
			for (const id of idsJahrgaenge) {
				idsJahrgaengeErlaubt.add(id);
			}
			const neueSchuelergruppe: Partial<UvSchuelergruppe> = {
				idPlanungsabschnitt: planungsabschnitt.id,
				bezeichnung: `Kurs ${kurs.kursart ?? ''} ${kurs.kursnummer ?? ''}`.trim(),
				idsJahrgaengeErlaubt,
			};
			const createdSchuelergruppe = await api.server.createUvSchuelergruppe(neueSchuelergruppe, api.schema);
			this.uvManager.schuelergruppeAdd(createdSchuelergruppe);
			kurs.idSchuelergruppe = createdSchuelergruppe.id;
			// Auch bei fehlgeschlagener Kursanlage muss die bereits angelegte Gruppe auswählbar bleiben.
			this.commit();
		} else {
			kurs.idSchuelergruppe = idSchuelergruppe;
		}
		const neu = await api.server.createUvKurs(kurs, api.schema);
		this.uvManager.kursAdd(neu);
		this.commit();
		return neu;
	};

	public delKurs = async (kurse: UvKurs[]): Promise<void> => {
		this.assertPlanungsabschnitt();
		const ids = new ArrayList<number>();
		const listKurse = new ArrayList<UvKurs>();
		for (const k of kurse) {
			ids.add(k.id);
			listKurse.add(k);
		}
		await api.server.deleteUvKurse(ids, api.schema);
		this.uvManager.kursRemoveAll(listKurse);
		kurse.length = 0;
		this.commit();
	};

	public patchKurs = async (_idPlanungsabschnitt: number, idKurs: number, kurs: Partial<UvKurs>): Promise<void> => {
		await api.server.patchUvKurs({ id: idKurs, ...kurs }, api.schema);
		const existing = this.uvManager.kursGetByIdOrException(idKurs);
		this.patchManagerDto(existing, kurs, UvKurs, patched => this.uvManager.kursPatchAttributes(patched));
		this.commit();
	};

	public importKursdaten = async (daten: UvKursImportDaten): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		const planungsabschnittsdaten = await api.server.importUvKurse(daten, api.schema, planungsabschnitt.id);
		this.uvManager.planungsabschnittsdatenBundleAdd(planungsabschnittsdaten);
		this.commit();
	};

	public createSchiene = async (schiene: Partial<UvSchiene>): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		delete schiene.id;
		schiene.idPlanungsabschnitt = planungsabschnitt.id;
		const neu = await api.server.createUvSchiene(schiene, api.schema);
		this.uvManager.schieneAdd(neu);
		this.commit();
	};

	public delSchiene = async (schienen: UvSchiene[]): Promise<void> => {
		this.assertPlanungsabschnitt();
		const ids = new ArrayList<number>();
		const listSchienen = new ArrayList<UvSchiene>();
		for (const s of schienen) {
			ids.add(s.id);
			listSchienen.add(this.uvManager.schieneGetByIdOrException(s.id));
		}
		await api.server.deleteUvSchienen(ids, api.schema);
		this.uvManager.schieneRemoveAll(listSchienen);
		schienen.length = 0;
		this.commit();
	};

	public patchSchiene = async (idSchiene: number, schiene: Partial<UvSchiene>): Promise<void> => {
		schiene.id = idSchiene;
		await api.server.patchUvSchiene(schiene, api.schema);
		const existing = this.uvManager.schieneGetByIdOrException(idSchiene);
		this.patchManagerDto(existing, schiene, UvSchiene, patched => this.uvManager.schienePatchAttributes(patched));
		this.commit();
	};

	public createLerngruppe = async (lerngruppe: Partial<UvLerngruppe>): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		UvManager.lerngruppeCheckZuordnung(Object.assign(new UvLerngruppe(), lerngruppe));
		delete lerngruppe.id;
		lerngruppe.idPlanungsabschnitt = planungsabschnitt.id;
		const neu = await api.server.createUvLerngruppe(lerngruppe, api.schema);
		this.uvManager.lerngruppeAdd(neu);
		this.commit();
	};

	public createLerngruppenFromKlassen = async (): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		const idsKlassen = new ArrayList<number>();
		for (const klasse of this.uvManager.klasseGetMengeByPlanungsabschnitt(planungsabschnitt)) {
			idsKlassen.add(klasse.id);
		}
		const lerngruppen = await api.server.createUvLerngruppenFromKlassen(idsKlassen, api.schema);
		this.uvManager.lerngruppeAddAll(lerngruppen);
		this.commit();
	};

	public delLerngruppe = async (lerngruppen: UvLerngruppe[]): Promise<void> => {
		this.assertPlanungsabschnitt();
		const ids = new ArrayList<number>();
		const listLerngruppen = new ArrayList<UvLerngruppe>();
		for (const lg of lerngruppen) {
			ids.add(lg.id);
			listLerngruppen.add(lg);
		}
		await api.server.deleteUvLerngruppen(ids, api.schema);
		this.uvManager.lerngruppeRemoveAll(listLerngruppen);
		lerngruppen.length = 0;
		this.commit();
	};

	public patchLerngruppe = async (id: number, lerngruppe: Partial<UvLerngruppe>): Promise<void> => {
		const result = Object.assign(new UvLerngruppe(), this.uvManager.lerngruppeGetByIdOrException(id), lerngruppe);
		UvManager.lerngruppeCheckZuordnung(result);
		await api.server.patchUvLerngruppe({ id, ...lerngruppe }, api.schema);
		const existing = this.uvManager.lerngruppeGetByIdOrException(id);
		this.patchManagerDto(existing, lerngruppe, UvLerngruppe, patched => this.uvManager.lerngruppePatchAttributes(patched));
		this.commit();
	};

	public erzeugeUnterrichteByLerngruppen = async (lerngruppen: UvLerngruppe[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const lg of lerngruppen) {
			ids.add(lg.id);
		}
		const unterrichte = await api.server.createUvUnterrichteByLerngruppen(ids, api.schema);
		this.uvManager.unterrichtAddAll(unterrichte);
		this.commit();
	};

	public createLerngruppenLehrerMultiple = async (lerngruppenLehrerList: List<Partial<UvLerngruppenLehrer>>): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		for (const lerngruppenLehrer of lerngruppenLehrerList) {
			delete lerngruppenLehrer.id;
		}
		const idsAlt = new ArrayList<number>();
		const listAlt = new ArrayList<UvLerngruppenLehrer>();
		for (const lerngruppenLehrer of this.uvManager.lerngruppenLehrerGetMengeByPlanungsabschnitt(planungsabschnitt)) {
			idsAlt.add(lerngruppenLehrer.id);
			listAlt.add(lerngruppenLehrer);
		}
		await api.server.deleteUvLerngruppenLehrerMultiple(idsAlt, api.schema);
		this.uvManager.lerngruppenLehrerRemoveAll(listAlt);
		const neu = await api.server.createUvLerngruppenLehrerMultiple(lerngruppenLehrerList, api.schema);
		this.uvManager.lerngruppenLehrerAddAll(neu);
		this.commit();
	};

	public addLerngruppenLehrer = async (data: Partial<UvLerngruppenLehrer>): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		delete data.id;
		data.idPlanungsabschnitt = planungsabschnitt.id;
		const neu = await api.server.createUvLerngruppenLehrer(data, api.schema);
		this.uvManager.lerngruppenLehrerAdd(neu);
		this.commit();
	};

	public patchLerngruppenLehrer = async (id: number, data: Partial<UvLerngruppenLehrer>, _zuordnung: UvLerngruppenLehrer): Promise<void> => {
		await api.server.patchUvLerngruppenLehrer({ id, ...data }, api.schema);
		this.patchManagerDto(this.uvManager.lerngruppenLehrerGetByZuordnungsIdOrException(id), data, UvLerngruppenLehrer,
			patched => this.uvManager.lerngruppenLehrerAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public delLerngruppenLehrer = async (lehrerList: UvLerngruppenLehrer[]): Promise<void> => {
		const ids = new ArrayList<number>();
		const listZuordnungen = new ArrayList<UvLerngruppenLehrer>();
		for (const lgl of lehrerList) {
			ids.add(lgl.id);
			listZuordnungen.add(this.uvManager.lerngruppenLehrerGetByZuordnungsIdOrException(lgl.id));
		}
		await api.server.deleteUvLerngruppenLehrerMultiple(ids, api.schema);
		this.uvManager.lerngruppenLehrerRemoveAll(listZuordnungen);
		lehrerList.length = 0;
		this.commit();
	};

	public addLerngruppenSchiene = async (data: Partial<UvLerngruppenSchiene>): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		if ((data.idLerngruppe === undefined) || (data.idSchiene === undefined)) {
			throw new DeveloperNotificationException("Ungültige Lerngruppe-Schiene-Zuordnung.");
		}
		data.idPlanungsabschnitt = planungsabschnitt.id;
		const neu = await api.server.createUvLerngruppenSchiene(data, api.schema);
		this.uvManager.lerngruppenSchieneAdd(neu);
		this.commit();
	};

	public delLerngruppenSchiene = async (schienenList: UvLerngruppenSchiene[]): Promise<void> => {
		const listZuordnungen = new ArrayList<UvLerngruppenSchiene>();
		for (const lgl of schienenList) {
			listZuordnungen.add(lgl);
		}
		await api.server.deleteUvLerngruppenSchienen(listZuordnungen, api.schema);
		this.uvManager.lerngruppenSchieneRemoveAll(listZuordnungen);
		schienenList.length = 0;
		this.commit();
	};

	public delSchueler = async (schueler: UvPlanungsabschnittSchueler[]): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		const ids = new ArrayList<number>();
		for (const s of schueler) {
			ids.add(s.idSchueler);
		}
		await api.server.deleteUvPlanungsabschnittSchuelerMultiple(ids, api.schema, planungsabschnitt.id);
		this.uvManager.planungsabschnittSchuelerRemoveAllById(planungsabschnitt.id, ids);
		schueler.length = 0;
		this.commit();
	};

	public patchSchueler = async (idPlanungsabschnitt: number, idSchueler: number, schueler: Partial<UvPlanungsabschnittSchueler>): Promise<void> => {
		await api.server.patchUvPlanungsabschnittSchueler({ idSchueler, idPlanungsabschnitt, ...schueler }, api.schema);
		const existing = this.uvManager.planungsabschnittSchuelerGetByIdOrException(idPlanungsabschnitt, idSchueler);
		this.patchManagerDto(existing, schueler, UvPlanungsabschnittSchueler,
			patched => this.uvManager.planungsabschnittSchuelerAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public importSchueler = async (options: { idSchuljahresabschnitt: number; folgejahrgang: boolean; klassenzuweisungenUebernehmen: boolean; versetzungsvermerkeBeruecksichtigen: boolean; createMissingKlassen: boolean }): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		const data = new UvSchuelerImportOptions();
		data.idSchuljahresabschnitt = options.idSchuljahresabschnitt;
		data.folgejahrgang = options.folgejahrgang;
		data.klassenzuweisungenUebernehmen = options.klassenzuweisungenUebernehmen;
		data.versetzungsvermerkeBeruecksichtigen = options.versetzungsvermerkeBeruecksichtigen;
		data.createMissingKlassen = options.createMissingKlassen;
		const planungsabschnittsdaten = await api.server.importUvPlanungsabschnittSchueler(data, api.schema, planungsabschnitt.id);
		this.uvManager.planungsabschnittsdatenBundleAdd(planungsabschnittsdaten);
		this.commit();
	};

	public createSchuelergruppe = async (schuelergruppe: Partial<UvSchuelergruppe>): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		delete schuelergruppe.id;
		schuelergruppe.idPlanungsabschnitt = planungsabschnitt.id;
		const neu = await api.server.createUvSchuelergruppe(schuelergruppe, api.schema);
		this.uvManager.schuelergruppeAdd(neu);
		this.commit();
	};

	public delSchuelergruppe = async (schuelergruppen: UvSchuelergruppe[]): Promise<void> => {
		this.assertPlanungsabschnitt();
		const ids = new ArrayList<number>();
		const listSchuelergruppen = new ArrayList<UvSchuelergruppe>();
		for (const sg of schuelergruppen) {
			ids.add(sg.id);
			listSchuelergruppen.add(sg);
		}
		await api.server.deleteUvSchuelergruppen(ids, api.schema);
		this.uvManager.schuelergruppeRemoveAll(listSchuelergruppen);
		schuelergruppen.length = 0;
		this.commit();
	};

	public addSchuelerToSchuelergruppe = async (idSchuelergruppe: number, idsSchueler: number[]): Promise<void> => {
		const manager = this.uvManager;
		const gruppe = manager.schuelergruppeGetByIdOrException(idSchuelergruppe);
		const vorhanden = new Set([...manager.planungsabschnittSchuelerGetMengeBySchuelergruppe(gruppe)].map(s => s.idSchueler));
		const daten = new ArrayList<Partial<UvSchuelergruppeSchueler>>();
		for (const idSchueler of new Set(idsSchueler)) {
			manager.planungsabschnittSchuelerGetByIdOrException(gruppe.idPlanungsabschnitt, idSchueler);
			if (!vorhanden.has(idSchueler)) {
				daten.add({ idPlanungsabschnitt: gruppe.idPlanungsabschnitt, idSchuelergruppe, idSchueler });
			}
		}
		if (daten.isEmpty()) {
			return;
		}
		const zuordnungen = await api.server.createUvSchuelergruppeSchuelerMultiple(daten, api.schema);
		manager.schuelergruppeSchuelerAddAll(zuordnungen);
		this.commit();
	};

	public removeSchuelerFromSchuelergruppe = async (idSchuelergruppe: number, idsSchueler: number[]): Promise<void> => {
		const manager = this.uvManager;
		const gruppe = manager.schuelergruppeGetByIdOrException(idSchuelergruppe);
		const ids = new Set(idsSchueler);
		const zuordnungen = new ArrayList<UvSchuelergruppeSchueler>();
		const zuLoeschen = new ArrayList<number>();
		for (const zuordnung of manager.schuelergruppeSchuelerGetMengeBySchuelergruppe(gruppe.idPlanungsabschnitt, idSchuelergruppe)) {
			if (ids.has(zuordnung.idSchueler)) {
				zuordnungen.add(zuordnung);
				zuLoeschen.add(zuordnung.idSchueler);
			}
		}
		if (zuLoeschen.isEmpty()) {
			return;
		}
		await api.server.deleteUvSchuelergruppeSchuelerMultiple(zuLoeschen, api.schema, idSchuelergruppe);
		manager.schuelergruppeSchuelerRemoveAll(zuordnungen);
		this.commit();
	};

	public patchSchuelergruppe = async (id: number, schuelergruppe: Partial<UvSchuelergruppe>): Promise<void> => {
		await api.server.patchUvSchuelergruppe({ id, ...schuelergruppe }, api.schema);
		const existing = this.uvManager.schuelergruppeGetByIdOrException(id);
		this.patchManagerDto(existing, schuelergruppe, UvSchuelergruppe, patched => this.uvManager.schuelergruppePatchAttributes(patched));
		this.commit();
	};

	public delUnterricht = async (unterrichte: UvUnterricht[]): Promise<void> => {
		this.assertPlanungsabschnitt();
		const ids = new ArrayList<number>();
		const listUnterrichte = new ArrayList<UvUnterricht>();
		for (const u of unterrichte) {
			ids.add(u.id);
			listUnterrichte.add(this.uvManager.unterrichtGetByIdOrException(u.id));
		}
		await api.server.deleteUvUnterrichte(ids, api.schema);
		this.uvManager.unterrichtRemoveAll(listUnterrichte);
		this.commit();
	};

	public patchUnterricht = async (id: number, unterricht: Partial<UvUnterricht>): Promise<void> => {
		await api.server.patchUvUnterricht({ id, ...unterricht }, api.schema);
		const existing = this.uvManager.unterrichtGetByIdOrException(id);
		this.patchManagerDto(existing, unterricht, UvUnterricht, patched => this.uvManager.unterrichtAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public addUnterrichtLerngruppenlehrer = async (data: Partial<UvUnterrichtLerngruppenlehrer>): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		data.idPlanungsabschnitt = planungsabschnitt.id;
		const neu = await api.server.createUvUnterrichtLerngruppenlehrer(data, api.schema);
		this.uvManager.unterrichtLerngruppenlehrerAdd(neu);
		this.commit();
	};

	public delUnterrichtLerngruppenlehrer = async (list: UvUnterrichtLerngruppenlehrer[]): Promise<void> => {
		const listZuordnungen = new ArrayList<UvUnterrichtLerngruppenlehrer>();
		for (const zuordnung of list) {
			listZuordnungen.add(zuordnung);
		}
		await api.server.deleteUvUnterrichtLerngruppenlehrerMultiple(listZuordnungen, api.schema);
		this.uvManager.unterrichtLerngruppenlehrerRemoveAll(listZuordnungen);
		list.length = 0;
		this.commit();
	};

	public addUnterrichtRaum = async (data: Partial<UvUnterrichtRaum>): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		data.idPlanungsabschnitt = planungsabschnitt.id;
		const neu = await api.server.createUvUnterrichtRaum(data, api.schema);
		this.uvManager.unterrichtRaumAdd(neu);
		this.commit();
	};

	public delUnterrichtRaum = async (list: UvUnterrichtRaum[]): Promise<void> => {
		const listZuordnungen = new ArrayList<UvUnterrichtRaum>();
		for (const zuordnung of list) {
			listZuordnungen.add(zuordnung);
		}
		await api.server.deleteUvUnterrichtRaeume(listZuordnungen, api.schema);
		this.uvManager.unterrichtRaumRemoveAll(listZuordnungen);
		list.length = 0;
		this.commit();
	};

	public delFaecher = async (faecher: UvFach[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const f of faecher) {
			ids.add(f.id);
		}
		await api.server.deleteUvFaecher(ids, api.schema);
		this.uvManager.fachRemoveAllById(ids);
		faecher.length = 0;
		this.commit();
	};

	public patchFach = async (id: number, fach: Partial<UvFach>): Promise<void> => {
		await api.server.patchUvFach({ id, ...fach }, api.schema);
		const fachAlt = this.uvManager.fachGetByIdOrException(id);
		this.patchManagerDto(fachAlt, fach, UvFach, patched => this.uvManager.fachPatchAttributes(patched));
		this.commit();
	};

	private todayISO(): string {
		return new Date().toISOString().slice(0, 10);
	}

	private addOneDayISO(dateISO: string): string {
		const d = new Date(dateISO);
		d.setDate(d.getDate() + 1);
		return d.toISOString().slice(0, 10);
	}

	public addFaecher = async (fachdaten: FachDaten[]): Promise<void> => {
		const faecherNeu = new ArrayList<Partial<UvFach>>();
		const heute = this.todayISO();
		for (const fachDaten of fachdaten) {
			const pFach: Partial<UvFach> = { idFach: fachDaten.id };
			const faecherExistierend = this.uvManager.fachGetMengeByFachdaten(fachDaten);
			if (faecherExistierend.isEmpty() === true) {
				pFach.gueltigVon = heute;
			} else {
				const gueltigBis = faecherExistierend.getLast().gueltigBis;
				pFach.gueltigVon = (gueltigBis === null || gueltigBis < heute) ? heute : this.addOneDayISO(gueltigBis);
			}
			faecherNeu.add(pFach);
		}
		const listFaecherNeu = await api.server.createUvFaecher(faecherNeu, api.schema);
		this.uvManager.fachAddAll(listFaecherNeu);
		this.commit();
	};

	public delRaeume = async (raeume: UvRaum[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const r of raeume) {
			ids.add(r.id);
		}
		await api.server.deleteUvRaeume(ids, api.schema);
		this.uvManager.raumRemoveAllById(ids);
		raeume.length = 0;
		this.commit();
	};

	public createRaum = async (raum: Partial<UvRaum>): Promise<void> => {
		delete raum.id;
		const neu = await api.server.createUvRaum(raum, api.schema);
		this.uvManager.raumAdd(neu);
		this.commit();
	};

	public patchRaum = async (id: number, raum: Partial<UvRaum>): Promise<void> => {
		await api.server.patchUvRaum({ id, ...raum }, api.schema);
		const raumAlt = this.uvManager.raumGetByIdOrException(id);
		this.patchManagerDto(raumAlt, raum, UvRaum, patched => this.uvManager.raumAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public setUvRaeumeImportJSON = async (formData: FormData): Promise<void> => {
		await api.call(async (data: FormData) => {
			const jsonFile = data.get("data");
			if (!(jsonFile instanceof File)) {
				return;
			}
			const json = await jsonFile.text();
			const raeume: Partial<UvRaum>[] = JSON.parse(json);
			const list = new ArrayList<Partial<UvRaum>>();
			for (const item of raeume) {
				if ((item.kuerzel !== undefined) && (this.uvManager.raumGetByKuerzelOrNull(item.kuerzel) === null)) {
					delete item.id;
					item.gueltigVon ??= this.todayISO();
					list.add(item);
				}
			}
			if (list.isEmpty()) {
				return;
			}
			const res = await api.server.createUvRaeume(list, api.schema);
			this.uvManager.raumAddAll(res);
			this.commit();
		})(formData);
	};

	public delRaumgruppen = async (raumgruppen: UvRaumgruppe[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const r of raumgruppen) {
			ids.add(r.id);
		}
		await api.server.deleteUvRaumgruppen(ids, api.schema);
		for (const r of raumgruppen) {
			this.uvManager.raumgruppeRemoveById(r.id);
		}
		raumgruppen.length = 0;
		this.commit();
	};

	public createRaumgruppe = async (raumgruppe: Partial<UvRaumgruppe>): Promise<void> => {
		delete raumgruppe.id;
		const neu = await api.server.createUvRaumgruppe(raumgruppe, api.schema);
		this.uvManager.raumgruppeAdd(neu);
		this.commit();
	};

	public patchRaumgruppe = async (id: number, raumgruppe: Partial<UvRaumgruppe>): Promise<void> => {
		await api.server.patchUvRaumgruppe({ id, ...raumgruppe }, api.schema);
		this.patchManagerDto(this.uvManager.raumgruppeGetByIdOrException(id), raumgruppe, UvRaumgruppe,
			patched => this.uvManager.raumgruppeAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public createStundentafel = async (stundentafel: Partial<UvStundentafel>): Promise<void> => {
		delete stundentafel.id;
		const neu = await api.server.createUvStundentafel(stundentafel, api.schema);
		this.uvManager.stundentafelAdd(neu);
		this.commit();
	};

	public importStundentafel = async (options: Partial<UvStundentafel> & { schuljahr: number; idKlasse: number; fehlendeUvFaecherAnlegen: boolean }): Promise<void> => {
		const request = Object.assign(new UvStundentafelImportOptions(), options);
		const daten = await api.server.importUvStundentafel(request, api.schema);
		this.uvManager.grunddatenBundleAdd(daten);
		this.commit();
	};

	public delStundentafel = async (stundentafeln: UvStundentafel[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const s of stundentafeln) {
			ids.add(s.id);
		}
		await api.server.deleteUvStundentafeln(ids, api.schema);
		this.uvManager.stundentafelRemoveAllById(ids);
		stundentafeln.length = 0;
		this.commit();
	};

	public patchStundentafel = async (id: number, stundentafel: Partial<UvStundentafel>): Promise<void> => {
		await api.server.patchUvStundentafel({ id, ...stundentafel }, api.schema);
		this.patchManagerDto(this.uvManager.stundentafelGetByIdOrException(id), stundentafel, UvStundentafel,
			patched => this.uvManager.stundentafelAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public addStundentafelFach = async (fach: Partial<UvStundentafelFach>): Promise<void> => {
		delete fach.id;
		const neu = await api.server.createUvStundentafelFach(fach, api.schema);
		this.uvManager.stundentafelFachAdd(neu);
		this.commit();
	};

	public patchStundentafelFach = async (id: number, fach: Partial<UvStundentafelFach>): Promise<void> => {
		await api.server.patchUvStundentafelFach({ id, ...fach }, api.schema);
		this.patchManagerDto(this.uvManager.stundentafelFachGetByIdOrException(id), fach, UvStundentafelFach,
			patched => this.uvManager.stundentafelFachAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public delStundentafelFach = async (faecher: UvStundentafelFach[]): Promise<void> => {
		const ids = new ArrayList<number>();
		const listFaecher = new ArrayList<UvStundentafelFach>();
		for (const f of faecher) {
			ids.add(f.id);
			listFaecher.add(this.uvManager.stundentafelFachGetByIdOrException(f.id));
		}
		await api.server.deleteUvStundentafelFaecher(ids, api.schema);
		this.uvManager.stundentafelFachRemoveAll(listFaecher);
		faecher.length = 0;
		this.commit();
	};

	public addZeitraster = async (data: Partial<UvZeitraster>): Promise<UvZeitraster> => {
		const neu = await api.server.createUvZeitraster(data, api.schema);
		this.uvManager.zeitrasterAdd(neu);
		this.commit();
		return neu;
	};

	public patchZeitraster = async (id: number, data: Partial<UvZeitraster>): Promise<void> => {
		await api.server.patchUvZeitraster({ id, ...data }, api.schema);
		const zeitraster = this.uvManager.zeitrasterGetByIdOrNull(id);
		if (zeitraster !== null) {
			this.patchManagerDto(zeitraster, data, UvZeitraster, patched => this.uvManager.zeitrasterAllPatchAttributes(ListUtils.create1(patched)));
		}
		this.commit();
	};

	public deleteZeitraster = async (ids: number[]): Promise<void> => {
		const listIds = new ArrayList<number>();
		for (const id of ids) {
			listIds.add(id);
		}
		await api.server.deleteUvZeitrasterMultiple(listIds, api.schema);
		this.uvManager.zeitrasterRemoveAllById(listIds);
		this.commit();
	};

	public addZeitrasterEintrag = async (data: Partial<UvZeitrasterEintrag>): Promise<UvZeitrasterEintrag> => {
		const neu = await api.server.createUvZeitrasterEintrag(data, api.schema);
		this.uvManager.zeitrasterEintragAdd(neu);
		this.commit();
		return neu;
	};

	public addZeitrasterEintraege = async (eintraege: Partial<UvZeitrasterEintrag>[]): Promise<UvZeitrasterEintrag[]> => {
		const listData = new ArrayList<Partial<UvZeitrasterEintrag>>();
		for (const e of eintraege) {
			listData.add(e);
		}
		const neueEintraege = await api.server.createUvZeitrasterEintraege(listData, api.schema);
		this.uvManager.zeitrasterEintragAddAll(neueEintraege);
		this.commit();
		return [...neueEintraege];
	};

	public patchZeitrasterEintrag = async (idZeitraster: number, id: number, data: Partial<UvZeitrasterEintrag>): Promise<void> => {
		await api.server.patchUvZeitrasterEintrag({ id, ...data }, api.schema);
		const eintrag = this.uvManager.zeitrasterEintragGetByIdOrException(idZeitraster, id);
		this.patchManagerDto(eintrag, data, UvZeitrasterEintrag, patched => this.uvManager.zeitrasterEintragAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public deleteZeitrasterEintraege = async (idZeitraster: number, ids: number[]): Promise<void> => {
		const listIds = new ArrayList<number>();
		for (const id of ids) {
			listIds.add(id);
		}
		await api.server.deleteUvZeitrasterEintraege(listIds, api.schema);
		for (const id of ids) {
			const eintrag = this.uvManager.zeitrasterEintragGetByIdOrException(idZeitraster, id);
			this.uvManager.zeitrasterEintragRemove(eintrag);
		}
		this.commit();
	};

	public createZeitrasterBlock = async ({
		idZeitraster,
		wochentagMin = 1,
		wochentagMax = 5,
		stundeMin = 1,
		stundeMax = 6,
		beginn = 480,
		dauer = 45,
		pause = 5,
	}: UvZeitrasterBlockOptions): Promise<void> => {
		const eintraege: Partial<UvZeitrasterEintrag>[] = [];
		for (let tag = wochentagMin; tag <= wochentagMax; tag++) {
			for (let stunde = stundeMin; stunde <= stundeMax; stunde++) {
				const stundenBeginn = beginn + ((stunde - stundeMin) * (dauer + pause));
				eintraege.push({ idZeitraster, wochentag: tag, stunde, beginn: stundenBeginn, ende: stundenBeginn + dauer });
			}
		}
		await this.addZeitrasterEintraege(eintraege);
	};

	public addLehrerToPlanungsabschnitt = async (lehrer: UvLehrer[]): Promise<void> => {
		const planungsabschnitt = this.planungsabschnittOrException;
		const lehrerPlanungsabschnitt = new ArrayList<UvPlanungsabschnittLehrer>();
		for (const l of lehrer) {
			const pal = new UvPlanungsabschnittLehrer();
			pal.idPlanungsabschnitt = planungsabschnitt.id;
			pal.idLehrer = l.id;
			lehrerPlanungsabschnitt.add(pal);
		}
		const listPalNeu = await api.server.createUvPlanungsabschnittLehrerMultiple(lehrerPlanungsabschnitt, api.schema);
		this.uvManager.planungsabschnittLehrerAddAll(listPalNeu);
		this.commit();
	};

	public delLehrer = async (lehrer: UvLehrer[]): Promise<void> => {
		const plan = this.planungsabschnitt;
		const verwendete = new Set([...((plan !== null && plan.id !== -1)
			? this.uvManager.lehrerGetMengeVerwendetInPlanungsabschnittByLehrerMenge(plan, this.uvManager.lehrerGetMengeAsList())
			: this.uvManager.lehrerGetMengeVerwendetByLehrerMenge(this.uvManager.lehrerGetMengeAsList()))].map(item => item.id));
		if (lehrer.some(item => verwendete.has(item.id))) {
			throw new DeveloperNotificationException('Die Auswahl enthält verwendete Lehrkräfte.');
		}
		const ids = new ArrayList<number>();
		for (const l of lehrer) {
			ids.add(l.id);
		}
		const planungsabschnitt = this.planungsabschnitt;
		if ((planungsabschnitt !== null) && (planungsabschnitt.id !== -1)) {
			await api.server.deleteUvPlanungsabschnittLehrerMultiple(ids, api.schema, planungsabschnitt.id);
			this.uvManager.planungsabschnittLehrerRemoveAllById(planungsabschnitt.id, ids);
		} else {
			const deleted = await api.server.deleteUvLehrerMultiple(ids, api.schema);
			this.uvManager.lehrerRemoveAll(deleted);
		}
		lehrer.length = 0;
		this.commit();
	};

	public delPflichtstundensollBeiLehrern = async (lehrer: UvLehrer[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const l of lehrer) {
			for (const p of this.uvManager.lehrerPflichtstundensollGetMengeByLehrer(l)) {
				ids.add(p.id);
			}
		}
		if (ids.isEmpty()) {
			return;
		}
		const deleted = await api.server.deleteUvLehrerPflichtstundensollMultiple(ids, api.schema);
		this.uvManager.lehrerPflichtstundensollRemoveAll(deleted);
		this.commit();
	};

	public importPflichtstundensollBeiLehrern = async (lehrer: UvLehrer[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const l of lehrer) {
			if ((l.idKLehrer !== null) && (this.uvManager.lehrerPflichtstundensollGetMengeByLehrer(l).isEmpty() === true)) {
				ids.add(l.id);
			}
		}
		if (ids.isEmpty()) {
			return;
		}
		const neu = await api.server.importUvLehrerPflichtstundensollFromPersonalabschnittsdaten(ids, api.schema);
		this.uvManager.lehrerPflichtstundensollAddAll(neu);
		this.commit();
	};

	public delAnrechnungsstundenBeiLehrern = async (lehrer: UvLehrer[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const l of lehrer) {
			for (const a of this.uvManager.lehrerAnrechnungsstundenGetMengeByLehrer(l)) {
				ids.add(a.id);
			}
		}
		if (ids.isEmpty()) {
			return;
		}
		const deleted = await api.server.deleteUvLehrerAnrechnungsstunden(ids, api.schema);
		this.uvManager.lehrerAnrechnungsstundenRemoveAll(deleted);
		this.commit();
	};

	public importAnrechnungsstundenBeiLehrern = async (lehrer: UvLehrer[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const l of lehrer) {
			if ((l.idKLehrer !== null) && (this.uvManager.lehrerAnrechnungsstundenGetMengeByLehrer(l).isEmpty() === true)) {
				ids.add(l.id);
			}
		}
		if (ids.isEmpty()) {
			return;
		}
		const neu = await api.server.importUvLehrerAnrechnungsstundenFromPersonalabschnittsdaten(ids, api.schema);
		this.uvManager.lehrerAnrechnungsstundenAddAll(neu);
		this.commit();
	};

	public createLehrer = async (lehrer: Partial<UvLehrer>): Promise<void> => {
		delete lehrer.id;
		const neu = await api.server.createUvLehrer(lehrer, api.schema);
		this.uvManager.lehrerAdd(neu);
		const planungsabschnitt = this.planungsabschnitt;
		if ((planungsabschnitt !== null) && (planungsabschnitt.id !== -1)) {
			const pal = new UvPlanungsabschnittLehrer();
			pal.idPlanungsabschnitt = planungsabschnitt.id;
			pal.idLehrer = neu.id;
			const palNeu = await api.server.createUvPlanungsabschnittLehrer(pal, api.schema);
			this.uvManager.planungsabschnittLehrerAdd(palNeu);
		}
		this.commit();
	};

	public patchLehrer = async (id: number, lehrer: Partial<UvLehrer>): Promise<void> => {
		await api.server.patchUvLehrer({ id, ...lehrer }, api.schema);
		this.patchManagerDto(this.uvManager.lehrerGetByIdOrException(id), lehrer, UvLehrer,
			patched => this.uvManager.lehrerAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public addAnrechnungsstunden = async (anrechnung: Partial<UvLehrerAnrechnungsstunden>): Promise<void> => {
		delete anrechnung.id;
		if (anrechnung.gueltigBis?.trim().length === 0) {
			anrechnung.gueltigBis = null;
		}
		const neu = await api.server.createUvLehrerAnrechnungsstunde(anrechnung, api.schema);
		this.uvManager.lehrerAnrechnungsstundenAdd(neu);
		this.commit();
	};

	public importAnrechnungsstundenFromPersonalabschnittsdaten = async (lehrer: UvLehrer): Promise<void> => {
		const neu = await api.server.importUvLehrerAnrechnungsstundenFromPersonalabschnittsdaten(ListUtils.create1(lehrer.id), api.schema);
		this.uvManager.lehrerAnrechnungsstundenAddAll(neu);
		this.commit();
	};

	public patchAnrechnungsstunde = async (id: number, anrechnung: Partial<UvLehrerAnrechnungsstunden>): Promise<void> => {
		await api.server.patchUvLehrerAnrechnungsstunde({ id, ...anrechnung }, api.schema);
		this.patchManagerDto(this.uvManager.lehrerAnrechnungsstundenGetByIdOrException(id), anrechnung, UvLehrerAnrechnungsstunden,
			patched => this.uvManager.lehrerAnrechnungsstundenAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public delAnrechnungsstunden = async (anrechnungen: UvLehrerAnrechnungsstunden[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const a of anrechnungen) {
			ids.add(a.id);
		}
		const deleted = await api.server.deleteUvLehrerAnrechnungsstunden(ids, api.schema);
		this.uvManager.lehrerAnrechnungsstundenRemoveAll(deleted);
		anrechnungen.length = 0;
		this.commit();
	};

	public addPflichtstundensoll = async (pflichtstundensoll: Partial<UvLehrerPflichtstundensoll>): Promise<void> => {
		delete pflichtstundensoll.id;
		if (pflichtstundensoll.gueltigBis?.trim().length === 0) {
			pflichtstundensoll.gueltigBis = null;
		}
		const neu = await api.server.createUvLehrerPflichtstundensoll(pflichtstundensoll, api.schema);
		this.uvManager.lehrerPflichtstundensollAdd(neu);
		this.commit();
	};

	public importPflichtstundensollFromPersonalabschnittsdaten = async (lehrer: UvLehrer): Promise<void> => {
		const neu = await api.server.importUvLehrerPflichtstundensollFromPersonalabschnittsdaten(ListUtils.create1(lehrer.id), api.schema);
		this.uvManager.lehrerPflichtstundensollAddAll(neu);
		this.commit();
	};

	public patchPflichtstundensoll = async (id: number, pflichtstundensoll: Partial<UvLehrerPflichtstundensoll>): Promise<void> => {
		await api.server.patchUvLehrerPflichtstundensoll({ id, ...pflichtstundensoll }, api.schema);
		this.patchManagerDto(this.uvManager.lehrerPflichtstundensollGetByIdOrException(id), pflichtstundensoll, UvLehrerPflichtstundensoll,
			patched => this.uvManager.lehrerPflichtstundensollAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public delPflichtstundensoll = async (pflichtstundensoll: UvLehrerPflichtstundensoll[]): Promise<void> => {
		const ids = new ArrayList<number>();
		for (const p of pflichtstundensoll) {
			ids.add(p.id);
		}
		const deleted = await api.server.deleteUvLehrerPflichtstundensollMultiple(ids, api.schema);
		this.uvManager.lehrerPflichtstundensollRemoveAll(deleted);
		pflichtstundensoll.length = 0;
		this.commit();
	};

	public addLehrerUnterrichtsfach = async (lehrer: UvLehrer, eintrag: Partial<LehrerUnterrichtsfach>): Promise<void> => {
		const payload = { ...eintrag, idLehrer: lehrer.id };
		delete payload.id;
		const fachNeu = lehrer.idKLehrer === null ? await api.server.createUvLehrerUnterrichtsfach(payload, api.schema) : await api.server.addLehrerUnterrichtsfach(payload, api.schema);
		this.uvManager.lehrerUnterrichtsfachAdd(fachNeu);
		this.commit();
	};

	public patchLehrerUnterrichtsfach = async (eintrag: LehrerUnterrichtsfach, patch: Partial<LehrerUnterrichtsfach>): Promise<void> => {
		if (eintrag.istKLehrer) {
			await api.server.patchLehrerUnterrichtsfach(patch, api.schema, eintrag.id);
		} else {
			await api.server.patchUvLehrerUnterrichtsfach({ id: eintrag.id, ...patch }, api.schema);
		}
		const existing = this.uvManager.lehrerUnterrichtsfachGetByIdOrException(eintrag.istKLehrer, eintrag.id);
		this.patchManagerDto(existing, patch, LehrerUnterrichtsfach,
			patched => this.uvManager.lehrerUnterrichtsfachAllPatchAttributes(ListUtils.create1(patched)));
		this.commit();
	};

	public removeLehrerUnterrichtsfach = async (eintrag: LehrerUnterrichtsfach): Promise<void> => {
		if (eintrag.istKLehrer) {
			await api.server.deleteLehrerUnterrichtsfach(api.schema, eintrag.id);
		} else {
			await api.server.deleteUvLehrerUnterrichtsfach(api.schema, eintrag.id);
		}
		this.uvManager.lehrerUnterrichtsfachRemove(eintrag);
		this.commit();
	};

	// Übergangsschnittstelle für fachfremde Datenbereiche. Diese Methoden werden in passende States verschoben,
	// sobald States für den jeweiligen Bereich (z. B. GostKursblockung, Klassen oder Lehrer) verfügbar sind.
	public getGostAbiturjahrgaenge = async (): Promise<GostJahrgang[]> => {
		const jahrgaenge = await api.server.getGostAbiturjahrgaenge(api.schema);
		return [...jahrgaenge].filter(jahrgang => jahrgang.abiturjahr > 0);
	};

	public getGostBlockungen = async (abiturjahr: number, idHalbjahr: number): Promise<GostBlockungListeneintrag[]> => {
		return [...await api.server.getGostAbiturjahrgangBlockungsliste(api.schema, abiturjahr, idHalbjahr)];
	};

	public getGostBlockungsergebnisse = async (idBlockung: number): Promise<GostBlockungsergebnis[]> => {
		const blockung = await api.server.getGostBlockung(api.schema, idBlockung);
		return [...blockung.ergebnisse];
	};

	public getGostBlockung = async (idBlockung: number): Promise<GostBlockungsdaten> => {
		return await api.server.getGostBlockung(api.schema, idBlockung);
	};

	public getKurseFuerSchuljahresabschnitt = async (idSchuljahresabschnitt: number): Promise<List<KursDaten>> => {
		return await api.server.getKurseFuerAbschnitt(api.schema, idSchuljahresabschnitt);
	};

	public getImportKlassen = async (idSchuljahresabschnitt: number, idJahrgang: number): Promise<KlassenListeEintrag[]> => {
		const klassen = await api.server.getListKlassenListeEintragBySchuljahresabschnitt(api.schema, idSchuljahresabschnitt);
		return [...klassen].filter(k => (k.idJahrgang === idJahrgang) && (k.anzahlZugeordneteSchueler > 0));
	};

	public getSchulLehrer = async (): Promise<LehrerListeEintrag[]> => {
		const listLehrer = await api.server.getLehrer(api.schema);
		const bereitsImportiert = new Set(this.uvManager.lehrerGetMengeAsList().toArray(new Array<UvLehrer>()).map(l => l.idKLehrer));
		return listLehrer.toArray(new Array<LehrerListeEintrag>()).filter(l => !bereitsImportiert.has(l.id));
	};

	public importLehrerFromSchule = async (lehrer: LehrerListeEintrag[]): Promise<void> => {
		const list = new ArrayList<Partial<UvLehrer>>();
		for (const l of lehrer) {
			list.add({ idKLehrer: l.id });
		}
		const created = await api.server.createUvLehrerMultiple(list, api.schema);
		this.uvManager.lehrerAddAll(created);
		this.commit();
	};

}

export const uvStateImpl = new UvStateImpl();
