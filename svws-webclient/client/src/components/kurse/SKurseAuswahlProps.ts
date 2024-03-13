import type { JahrgangsDaten, KursDaten, LehrerListeEintrag, Schuljahresabschnitt } from "@core";

export interface KurseAuswahlProps {
	auswahl: () => KursDaten | undefined;
	mapKatalogeintraege: () => Map<number, KursDaten>;
	mapJahrgaenge: Map<number, JahrgangsDaten>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	gotoEintrag: (eintrag: JahrgangsDaten) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
}