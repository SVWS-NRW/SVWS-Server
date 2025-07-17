import type { Config } from "../../../utils/Config"
import type { ApiFile } from "../../../../../core/src/api/BaseApi";
import type { Schulform } from "../../../../../core/src/asd/types/schule/Schulform";
import type { AbiturdatenManager } from "../../../../../core/src/core/abschluss/gost/AbiturdatenManager";
import type { GostBelegpruefungErgebnis } from "../../../../../core/src/core/abschluss/gost/GostBelegpruefungErgebnis";
import type { GostJahrgangsdaten } from "../../../../../core/src/core/data/gost/GostJahrgangsdaten";
import type { GostLaufbahnplanungBeratungsdaten } from "../../../../../core/src/core/data/gost/GostLaufbahnplanungBeratungsdaten";
import type { GostLaufbahnplanungDaten } from "../../../../../core/src/core/data/gost/GostLaufbahnplanungDaten";
import type { GostSchuelerFachwahl } from "../../../../../core/src/core/data/gost/GostSchuelerFachwahl";
import type { LehrerListeEintrag } from "../../../../../core/src/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "../../../../../core/src/core/data/schueler/SchuelerListeEintrag";
import type { BenutzerKompetenz } from "../../../../../core/src/core/types/benutzer/BenutzerKompetenz";
import type { GostHalbjahr } from "../../../../../core/src/core/types/gost/GostHalbjahr";
import type { ServerMode } from "../../../../../core/src/core/types/ServerMode";
import type { ArrayList } from "../../../../../core/src/java/util/ArrayList";

export interface SchuelerLaufbahnplanungProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen?: Set<BenutzerKompetenz>,
	benutzerKompetenzenAbiturjahrgaenge?: Set<number>;
	config: () => Config;
	setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
	setGostBelegpruefungsArt: (value: 'ef1'|'gesamt'|'auto') => Promise<void>;
	getPdfWahlbogen: (title: string) => Promise<ApiFile>;
	exportLaufbahnplanung: () => Promise<ApiFile>;
	importLaufbahnplanung: (data: FormData) => Promise<void>;
	schueler: SchuelerListeEintrag,
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostLaufbahnBeratungsdaten: () => GostLaufbahnplanungBeratungsdaten;
	patchBeratungsdaten: (data : Partial<GostLaufbahnplanungBeratungsdaten>) => Promise<void>;
	gostBelegpruefungsArt: () => 'ef1'|'gesamt'|'auto';
	gostBelegpruefungErgebnis: () => GostBelegpruefungErgebnis;
	abiturdatenManager: () => AbiturdatenManager;
	listLehrer: ArrayList<LehrerListeEintrag>;
	id?: number;
	zwischenspeicher?: GostLaufbahnplanungDaten;
	saveLaufbahnplanung: () => Promise<void>;
	restoreLaufbahnplanung: () => Promise<void>;
	resetFachwahlen: (forceDelete: boolean) => Promise<void>;
	gotoKursblockung: (halbjahr: GostHalbjahr) => Promise<void>;
}