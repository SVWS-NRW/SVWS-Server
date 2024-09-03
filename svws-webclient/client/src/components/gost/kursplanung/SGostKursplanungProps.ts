import type { WritableComputedRef } from "vue";
import type { GostBlockungsdatenManager, GostBlockungsergebnisManager, GostBlockungSchiene, GostBlockungKurs, GostBlockungKursLehrer, GostFaecherManager,
	GostHalbjahr, LehrerListeEintrag, GostStatistikFachwahl, GostBlockungsergebnisKurs, GostJahrgangsdaten, ApiFile,
	GostBlockungsergebnisKursSchuelerZuordnungUpdate, GostBlockungRegelUpdate, JavaSet, BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";
import type { ApiStatus } from "~/components/ApiStatus";
import type { DownloadPDFTypen } from "./DownloadPDFTypen";

export interface GostKursplanungProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerKompetenzenAbiturjahrgaenge: Set<number>;
	getDatenmanager: () => GostBlockungsdatenManager;
	getKursauswahl: () => JavaSet<number>,
	getErgebnismanager: () => GostBlockungsergebnisManager;
	addBlockung: () => Promise<void>;
	restoreBlockung: () => Promise<void>;
	regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
	updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
	patchSchiene: (data: Partial<GostBlockungSchiene>, id : number) => Promise<void>;
	addSchiene: () => Promise<GostBlockungSchiene | undefined>;
	removeSchiene: (s: GostBlockungSchiene) => Promise<GostBlockungSchiene | undefined>;
	patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
	addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
	removeKurse: (ids: Iterable<number>) => Promise<void>;
	combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs | undefined | null) => Promise<void>;
	splitKurs: (kurs: GostBlockungKurs) => Promise<void>;
	addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
	removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
	addSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
	removeSchieneKurs: (kurs: GostBlockungKurs) => Promise<void>;
	ergebnisAbleiten: () => Promise<void>;
	ergebnisHochschreiben: () => Promise<void>;
	ergebnisAktivieren: () => Promise<boolean>;
	ergebnisSynchronisieren: () => Promise<void>;
	getPDF: (title: DownloadPDFTypen) => Promise<ApiFile>;
	jahrgangsdaten: () => GostJahrgangsdaten;
	kurssortierung: WritableComputedRef<'fach' | 'kursart'>;
	existiertSchuljahresabschnitt: boolean;
	hatBlockung: boolean;
	hatErgebnis: boolean;
	schuelerFilter: () => GostKursplanungSchuelerFilter;
	faecherManager: GostFaecherManager;
	halbjahr: GostHalbjahr;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapFachwahlStatistik: () => Map<number, GostStatistikFachwahl>;
	apiStatus: ApiStatus;
	updateKursSchuelerZuordnungen: (update: GostBlockungsergebnisKursSchuelerZuordnungUpdate) => Promise<boolean>;
	// Config
	zeigeSchienenbezeichnungen: () => boolean;
	setZeigeSchienenbezeichnungen: (value: boolean) => Promise<void>;
	blockungstabelleHidden: () => boolean;
	setBlockungstabelleHidden: (value: boolean) => Promise<void>;
	fixierteVerschieben: () => boolean;
	setFixierteVerschieben: (value: boolean) => Promise<void>;
	inZielkursFixieren: () => boolean;
	setInZielkursFixieren: (value: boolean) => Promise<void>;
}