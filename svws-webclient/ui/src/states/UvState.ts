import type { InjectionKey } from "vue";

import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { GostBlockungListeneintrag } from "@core/core/data/gost/GostBlockungListeneintrag";
import type { GostBlockungsdaten } from "@core/core/data/gost/GostBlockungsdaten";
import type { GostBlockungsergebnis } from "@core/core/data/gost/GostBlockungsergebnis";
import type { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import type { UvFach } from "@core/core/data/uv/UvFach";
import type { UvKlasse } from "@core/core/data/uv/UvKlasse";
import type { UvKlassenLehrer } from "@core/core/data/uv/UvKlassenLehrer";
import type { UvKurs } from "@core/core/data/uv/UvKurs";
import type { UvKursImportDaten } from "@core/core/data/uv/UvKursImportDaten";
import type { UvLehrer } from "@core/core/data/uv/UvLehrer";
import type { UvLehrerAnrechnungsstunden } from "@core/core/data/uv/UvLehrerAnrechnungsstunden";
import type { UvLehrerPflichtstundensoll } from "@core/core/data/uv/UvLehrerPflichtstundensoll";
import type { UvLerngruppe } from "@core/core/data/uv/UvLerngruppe";
import type { UvLerngruppenLehrer } from "@core/core/data/uv/UvLerngruppenLehrer";
import type { UvLerngruppenSchiene } from "@core/core/data/uv/UvLerngruppenSchiene";
import type { UvPlanungsabschnitt } from "@core/core/data/uv/UvPlanungsabschnitt";
import type { UvPlanungsabschnittSchueler } from "@core/core/data/uv/UvPlanungsabschnittSchueler";
import type { UvPlanungsabschnittZeitraster } from "@core/core/data/uv/UvPlanungsabschnittZeitraster";
import type { UvRaum } from "@core/core/data/uv/UvRaum";
import type { UvRaumgruppe } from "@core/core/data/uv/UvRaumgruppe";
import type { UvSchiene } from "@core/core/data/uv/UvSchiene";
import type { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
import type { UvStundentafel } from "@core/core/data/uv/UvStundentafel";
import type { UvStundentafelFach } from "@core/core/data/uv/UvStundentafelFach";
import type { UvUnterricht } from "@core/core/data/uv/UvUnterricht";
import type { UvUnterrichtLerngruppenlehrer } from "@core/core/data/uv/UvUnterrichtLerngruppenlehrer";
import type { UvUnterrichtRaum } from "@core/core/data/uv/UvUnterrichtRaum";
import type { UvZeitraster } from "@core/core/data/uv/UvZeitraster";
import type { UvZeitrasterEintrag } from "@core/core/data/uv/UvZeitrasterEintrag";
import type { UvManager } from "@core/core/utils/uv/UvManager";
import type { List } from "@core/java/util/List";

import { DeveloperNotificationException } from "../../../core/src/core/exceptions/DeveloperNotificationException";
import { AppContext } from "../AppContext";

export interface UvZeitrasterBlockOptions {
	idZeitraster: number;
	wochentagMin?: number;
	wochentagMax?: number;
	stundeMin?: number;
	stundeMax?: number;
	beginn?: number;
	dauer?: number;
	pause?: number;
}

export interface UvState {

	get uvManager(): UvManager;
	get planungsabschnitt(): UvPlanungsabschnitt | null;
	get hatKompetenzAllgemeinAendern(): boolean;
	get hatKompetenzFunktionsbezogenAendern(): boolean;
	get hatKompetenzAendern(): boolean;

	reset(): void;
	setSchuljahresabschnitt(idSchuljahresabschnitt: number, forceReload?: boolean): Promise<List<UvPlanungsabschnitt> | null>;
	ladeDaten(auswahl: UvPlanungsabschnitt | null, planungsabschnitte: Iterable<UvPlanungsabschnitt>): Promise<UvPlanungsabschnitt | null>;
	patch(id: number, data: Partial<UvPlanungsabschnitt>): Promise<boolean>;
	deletePlanungsabschnitte(ids: List<number>): Promise<List<SimpleOperationResponse>>;
	add(partial: Partial<UvPlanungsabschnitt>): Promise<UvPlanungsabschnitt>;
	addAsCopy(partial: Partial<UvPlanungsabschnitt>, idFromUvPlanungsabschnitt?: number): Promise<UvPlanungsabschnitt>;

	addZeitrasterZuordnung(idZeitraster: number): Promise<void>;
	patchZeitrasterZuordnung(id: UvPlanungsabschnittZeitraster, data: JahrgangsDaten[]): Promise<void>;
	addZeitrasterZuordnungen(idZeitrasterList: number[]): Promise<void>;
	removeZeitrasterZuordnung(idZeitraster: number): Promise<void>;
	removeZeitrasterZuordnungen(idZeitrasterList: number[]): Promise<void>;

	createKlasse(klasse: Partial<UvKlasse>, idSchuelergruppe: number | null, idsJahrgaenge: number[]): Promise<void>;
	delKlasse(klassen: UvKlasse[]): Promise<void>;
	patchKlasse(idPlanungsabschnitt: number, idKlasse: number, klasse: Partial<UvKlasse>): Promise<void>;
	addKlassenLehrer(data: Partial<UvKlassenLehrer>): Promise<void>;
	patchKlassenLehrer(id: number, data: Partial<UvKlassenLehrer>, zuordnung: UvKlassenLehrer): Promise<void>;
	delKlassenLehrer(lehrerList: UvKlassenLehrer[]): Promise<void>;

	createKurs(kurs: Partial<UvKurs>, idSchuelergruppe: number | null, idsJahrgaenge: number[]): Promise<UvKurs>;
	delKurs(kurse: UvKurs[]): Promise<void>;
	patchKurs(idPlanungsabschnitt: number, idKurs: number, kurs: Partial<UvKurs>): Promise<void>;
	importKursdaten(daten: UvKursImportDaten): Promise<void>;

	createSchiene(schiene: Partial<UvSchiene>): Promise<void>;
	delSchiene(schienen: UvSchiene[]): Promise<void>;
	patchSchiene(idSchiene: number, schiene: Partial<UvSchiene>): Promise<void>;

	createLerngruppe(lerngruppe: Partial<UvLerngruppe>): Promise<void>;
	createLerngruppenFromKlassen(): Promise<void>;
	delLerngruppe(lerngruppen: UvLerngruppe[]): Promise<void>;
	patchLerngruppe(idLerngruppe: number, lerngruppe: Partial<UvLerngruppe>): Promise<void>;
	erzeugeUnterrichteByLerngruppen(lerngruppen: UvLerngruppe[]): Promise<void>;
	createLerngruppenLehrerMultiple(lerngruppenLehrerList: List<Partial<UvLerngruppenLehrer>>): Promise<void>;
	addLerngruppenLehrer(data: Partial<UvLerngruppenLehrer>): Promise<void>;
	patchLerngruppenLehrer(id: number, data: Partial<UvLerngruppenLehrer>, zuordnung: UvLerngruppenLehrer): Promise<void>;
	delLerngruppenLehrer(lehrerList: UvLerngruppenLehrer[]): Promise<void>;
	addLerngruppenSchiene(data: Partial<UvLerngruppenSchiene>): Promise<void>;
	delLerngruppenSchiene(schienenList: UvLerngruppenSchiene[]): Promise<void>;

	delSchueler(schueler: UvPlanungsabschnittSchueler[]): Promise<void>;
	patchSchueler(idPlanungsabschnitt: number, idSchueler: number, schueler: Partial<UvPlanungsabschnittSchueler>): Promise<void>;
	importSchueler(options: { idSchuljahresabschnitt: number; folgejahrgang: boolean; klassenzuweisungenUebernehmen: boolean; versetzungsvermerkeBeruecksichtigen: boolean; createMissingKlassen: boolean }): Promise<void>;

	createSchuelergruppe(schuelergruppe: Partial<UvSchuelergruppe>): Promise<void>;
	delSchuelergruppe(schuelergruppen: UvSchuelergruppe[]): Promise<void>;
	patchSchuelergruppe(idSchuelergruppe: number, schuelergruppe: Partial<UvSchuelergruppe>): Promise<void>;
	addSchuelerToSchuelergruppe(idSchuelergruppe: number, idsSchueler: number[]): Promise<void>;
	removeSchuelerFromSchuelergruppe(idSchuelergruppe: number, idsSchueler: number[]): Promise<void>;

	delUnterricht(unterrichte: UvUnterricht[]): Promise<void>;
	patchUnterricht(idUnterricht: number, unterricht: Partial<UvUnterricht>): Promise<void>;
	addUnterrichtLerngruppenlehrer(data: Partial<UvUnterrichtLerngruppenlehrer>): Promise<void>;
	delUnterrichtLerngruppenlehrer(list: UvUnterrichtLerngruppenlehrer[]): Promise<void>;
	addUnterrichtRaum(data: Partial<UvUnterrichtRaum>): Promise<void>;
	delUnterrichtRaum(list: UvUnterrichtRaum[]): Promise<void>;

	delFaecher(faecher: UvFach[]): Promise<void>;
	patchFach(idFach: number, fach: Partial<UvFach>): Promise<void>;
	addFaecher(fachdaten: FachDaten[]): Promise<void>;

	delRaeume(raeume: UvRaum[]): Promise<void>;
	createRaum(raum: Partial<UvRaum>): Promise<void>;
	patchRaum(idRaum: number, raum: Partial<UvRaum>): Promise<void>;
	setUvRaeumeImportJSON(formData: FormData): Promise<void>;

	delRaumgruppen(raumgruppen: UvRaumgruppe[]): Promise<void>;
	createRaumgruppe(raumgruppe: Partial<UvRaumgruppe>): Promise<void>;
	patchRaumgruppe(idRaumgruppe: number, raumgruppe: Partial<UvRaumgruppe>): Promise<void>;

	createStundentafel(stundentafel: Partial<UvStundentafel>): Promise<void>;
	importStundentafel(options: Partial<UvStundentafel> & { schuljahr: number; idKlasse: number; fehlendeUvFaecherAnlegen: boolean }): Promise<void>;
	delStundentafel(stundentafeln: UvStundentafel[]): Promise<void>;
	patchStundentafel(idStundentafel: number, stundentafel: Partial<UvStundentafel>): Promise<void>;
	addStundentafelFach(fach: Partial<UvStundentafelFach>): Promise<void>;
	patchStundentafelFach(idStundentafelFach: number, fach: Partial<UvStundentafelFach>): Promise<void>;
	delStundentafelFach(faecher: UvStundentafelFach[]): Promise<void>;

	addZeitraster(data: Partial<UvZeitraster>): Promise<UvZeitraster>;
	patchZeitraster(idZeitraster: number, data: Partial<UvZeitraster>): Promise<void>;
	deleteZeitraster(ids: number[]): Promise<void>;
	addZeitrasterEintrag(data: Partial<UvZeitrasterEintrag>): Promise<UvZeitrasterEintrag>;
	addZeitrasterEintraege(eintraege: Partial<UvZeitrasterEintrag>[]): Promise<UvZeitrasterEintrag[]>;
	patchZeitrasterEintrag(idZeitraster: number, idEintrag: number, data: Partial<UvZeitrasterEintrag>): Promise<void>;
	deleteZeitrasterEintraege(idZeitraster: number, ids: number[]): Promise<void>;
	createZeitrasterBlock(options: UvZeitrasterBlockOptions): Promise<void>;

	addLehrerToPlanungsabschnitt(lehrer: UvLehrer[]): Promise<void>;
	delLehrer(lehrer: UvLehrer[]): Promise<void>;
	delPflichtstundensollBeiLehrern(lehrer: UvLehrer[]): Promise<void>;
	importPflichtstundensollBeiLehrern(lehrer: UvLehrer[]): Promise<void>;
	delAnrechnungsstundenBeiLehrern(lehrer: UvLehrer[]): Promise<void>;
	importAnrechnungsstundenBeiLehrern(lehrer: UvLehrer[]): Promise<void>;
	createLehrer(lehrer: Partial<UvLehrer>): Promise<void>;
	patchLehrer(idLehrer: number, lehrer: Partial<UvLehrer>): Promise<void>;
	addAnrechnungsstunden(anrechnung: Partial<UvLehrerAnrechnungsstunden>): Promise<void>;
	importAnrechnungsstundenFromPersonalabschnittsdaten(lehrer: UvLehrer): Promise<void>;
	patchAnrechnungsstunde(idAnrechnungsstunde: number, anrechnung: Partial<UvLehrerAnrechnungsstunden>): Promise<void>;
	delAnrechnungsstunden(anrechnungen: UvLehrerAnrechnungsstunden[]): Promise<void>;
	addPflichtstundensoll(pflichtstundensoll: Partial<UvLehrerPflichtstundensoll>): Promise<void>;
	importPflichtstundensollFromPersonalabschnittsdaten(lehrer: UvLehrer): Promise<void>;
	patchPflichtstundensoll(idPflichtstundensoll: number, pflichtstundensoll: Partial<UvLehrerPflichtstundensoll>): Promise<void>;
	delPflichtstundensoll(pflichtstundensoll: UvLehrerPflichtstundensoll[]): Promise<void>;
	addLehrerUnterrichtsfach(lehrer: UvLehrer, eintrag: Partial<LehrerUnterrichtsfach>): Promise<void>;
	patchLehrerUnterrichtsfach(eintrag: LehrerUnterrichtsfach, patch: Partial<LehrerUnterrichtsfach>): Promise<void>;
	removeLehrerUnterrichtsfach(eintrag: LehrerUnterrichtsfach): Promise<void>;

	// Übergangsschnittstelle für fachfremde Datenbereiche. Diese Methoden werden in passende States verschoben,
	// sobald States für den jeweiligen Bereich (z. B. GostKursblockung, Klassen oder Lehrer) verfügbar sind.
	getGostAbiturjahrgaenge(): Promise<GostJahrgang[]>;
	getGostBlockungen(abiturjahr: number, idHalbjahr: number): Promise<GostBlockungListeneintrag[]>;
	getGostBlockungsergebnisse(idBlockung: number): Promise<GostBlockungsergebnis[]>;
	getGostBlockung(idBlockung: number): Promise<GostBlockungsdaten>;
	getKurseFuerSchuljahresabschnitt(idSchuljahresabschnitt: number): Promise<List<KursDaten>>;
	getImportKlassen(idSchuljahresabschnitt: number, idJahrgang: number): Promise<KlassenListeEintrag[]>;
	getSchulLehrer(): Promise<LehrerListeEintrag[]>;
	importLehrerFromSchule(lehrer: LehrerListeEintrag[]): Promise<void>;

}

export const UvStateKey: InjectionKey<UvState> = Symbol('UvState');

export function useUvState(): UvState {
	const state = AppContext.instance.inject(UvStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des UvState über provide in der main.ts eingebunden");
	}
	return state;
}
