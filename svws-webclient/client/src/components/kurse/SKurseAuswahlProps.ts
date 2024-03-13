import type { JahrgangsListeEintrag, KursDaten, LehrerListeEintrag, Schuljahresabschnitt } from "@core";

export interface KurseAuswahlProps {
	auswahl: () => KursDaten | undefined;
	mapKatalogeintraege: () => Map<number, KursDaten>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	gotoEintrag: (eintrag: JahrgangsListeEintrag) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
}