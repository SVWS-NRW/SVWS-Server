import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungsdatenManager, GostBlockungsergebnisKurs, GostBlockungsergebnisManager, GostBlockungsergebnisSchiene, GostFaecherManager, GostKursart, GostStatistikFachwahl, LehrerListeEintrag, List } from "@core";
import type { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";

export type SGostKursplanungKursansichtDragData = { kurs: GostBlockungKurs; schiene: GostBlockungsergebnisSchiene; fachId: number; } | undefined;

export interface SGostKursplanungKursansichtFachwahlProps {
	getDatenmanager: () => GostBlockungsdatenManager;
	getKursauswahl: () => Set<number>,
	getErgebnismanager: () => GostBlockungsergebnisManager;
	addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
	patchRegel: (data: GostBlockungRegel, id: number) => Promise<void>;
	removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
	updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
	patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
	addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
	removeKurse: (ids: Iterable<number>) => Promise<void>;
	combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs | undefined | null) => Promise<void>;
	splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
	addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
	removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
	addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
	removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
	hatErgebnis: boolean;
	schuelerFilter: () => GostKursplanungSchuelerFilter | undefined;
	fachwahlen: GostStatistikFachwahl;
	faecherManager: GostFaecherManager;
	fachwahlenAnzahl: number;
	kursart: GostKursart;
	mapLehrer: Map<number, LehrerListeEintrag>;
	allowRegeln: boolean;
	isSelectedKurse: GostBlockungKurs[];
	selectedDo: (action: 'kurse fixieren'| 'kurse lösen' | 'toggle kurse' | 'schienen sperren' | 'schienen entsperren' | 'toggle schienen' | 'schüler fixieren' | 'schüler lösen' | 'toggle schüler') => Promise<List<GostBlockungRegel>>;
	dragDataKursSchiene: () => SGostKursplanungKursansichtDragData;
	dropDataKursSchiene: () => SGostKursplanungKursansichtDragData;
	onDragKursSchiene: (data: SGostKursplanungKursansichtDragData) => void;
	onDropKursSchiene: (zone: SGostKursplanungKursansichtDragData) => Promise<void>;
}