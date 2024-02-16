import type { GostBlockungRegel, GostBlockungsdatenManager, GostBlockungsergebnisKursSchuelerZuordnung, GostBlockungsergebnisManager, List, SchuelerListeEintrag } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface GostUmwahlansichtProps {
	hatBlockung: boolean,
	hatErgebnis: boolean,
	addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
	removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
	addRegeln: (listRegeln: List<GostBlockungRegel>) => Promise<void>;
	removeRegeln: (listRegeln: List<GostBlockungRegel>) => Promise<void>;
	regelnDeleteAndAdd: (listDelete: List<GostBlockungRegel>, listAdd: List<GostBlockungRegel>) => Promise<void>;
	updateKursSchuelerZuordnung: (idSchueler: number, idKursNeu: number, idKursAlt: number | undefined) => Promise<boolean>;
	removeKursSchuelerZuordnung: (zuordnungen: Iterable<GostBlockungsergebnisKursSchuelerZuordnung>) => Promise<boolean>;
	autoKursSchuelerZuordnung: (idSchueler : number) => Promise<void>;
	gotoSchueler: (idSchueler: number) => Promise<void>;
	gotoLaufbahnplanung: (idSchueler: number) => Promise<void>;
	getDatenmanager: () => GostBlockungsdatenManager;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	schueler: SchuelerListeEintrag | undefined;
	apiStatus: ApiStatus;
}