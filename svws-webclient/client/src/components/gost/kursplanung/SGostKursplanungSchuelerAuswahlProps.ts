import type { SchuelerListeEintrag, GostBlockungsergebnisManager, GostFaecherManager, GostBlockungsdatenManager, GostBlockungRegel, GostBlockungsergebnisKursSchuelerZuordnung, List } from "@core";
import type { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";
import type { Config } from "~/components/Config";

export interface KursplanungSchuelerAuswahlProps {
	hatBlockung: boolean,
	hatErgebnis: boolean,
	setSchueler: (schueler: SchuelerListeEintrag) => Promise<void>;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	getDatenmanager: () => GostBlockungsdatenManager;
	schueler: SchuelerListeEintrag | undefined;
	schuelerFilter: () => GostKursplanungSchuelerFilter;
	faecherManager: GostFaecherManager;
	config: Config;
	addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
	removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
	addRegeln: (listRegeln: List<GostBlockungRegel>) => Promise<void>;
	removeRegeln: (listRegeln: List<GostBlockungRegel>) => Promise<void>;
	updateKursSchuelerZuordnung: (idSchueler: number, idKursNeu: number, idKursAlt: number | undefined) => Promise<boolean>;
	removeKursSchuelerZuordnung: (zuordnungen: Iterable<GostBlockungsergebnisKursSchuelerZuordnung>) => Promise<boolean>;
}