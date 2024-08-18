import type { Schueler, GostBlockungsergebnisManager, GostFaecherManager, GostBlockungsdatenManager, GostBlockungRegelUpdate } from "@core";
import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";

export interface KursplanungSchuelerAuswahlProps {
	hatBlockung: boolean,
	hatErgebnis: boolean,
	setSchueler: (schueler: Schueler) => Promise<void>;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	getDatenmanager: () => GostBlockungsdatenManager;
	schueler: Schueler | undefined;
	schuelerFilter: () => GostKursplanungSchuelerFilter;
	faecherManager: GostFaecherManager;
	regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
	isSchuelerFilterOpen: () => boolean;
	setIsSchuelerFilterOpen: (value: boolean) => Promise<void>;
	showGeschlecht: () => boolean;
	setShowGeschlecht: (value: boolean) => Promise<void>;
}