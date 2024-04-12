import type { ApiFile } from "../../../core/src/api/BaseApi";
import type { AbiturdatenManager } from "../../../core/src/core/abschluss/gost/AbiturdatenManager";
import type { GostBelegpruefungErgebnis } from "../../../core/src/core/abschluss/gost/GostBelegpruefungErgebnis";
import type { GostJahrgangsdaten } from "../../../core/src/core/data/gost/GostJahrgangsdaten";
import type { GostLaufbahnplanungDaten } from "../../../core/src/core/data/gost/GostLaufbahnplanungDaten";
import type { GostSchuelerFachwahl } from "../../../core/src/core/data/gost/GostSchuelerFachwahl";
import type { SchuelerListeEintrag } from "../../../core/src/core/data/schueler/SchuelerListeEintrag";

export interface LaufbahnplanungOberstufeProps {
	setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
	setGostBelegpruefungsArt: (value: 'ef1'|'gesamt'|'auto') => Promise<void>;
	exportLaufbahnplanung: () => Promise<ApiFile>;
	importLaufbahnplanung: (data: FormData) => Promise<void>;
	schueler: SchuelerListeEintrag,
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostBelegpruefungsArt: () => 'ef1'|'gesamt'|'auto';
	gostBelegpruefungErgebnis: () => GostBelegpruefungErgebnis;
	abiturdatenManager: () => AbiturdatenManager;
	id?: number;
	zwischenspeicher?: GostLaufbahnplanungDaten;
	saveLaufbahnplanung: () => Promise<void>;
	restoreLaufbahnplanung: () => Promise<void>;
	resetFachwahlen: () => Promise<void>;
	modus: 'manuell'|'normal'|'hochschreiben';
	setModus: (modus: 'manuell'|'normal'|'hochschreiben') => Promise<void>;
}
