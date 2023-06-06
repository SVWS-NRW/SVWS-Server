import type { JahrgangsListeEintrag, KursListeEintrag, LehrerListeEintrag, Schuljahresabschnitt } from "@core";

export interface KurseAuswahlProps {
	auswahl: KursListeEintrag | undefined;
	mapKatalogeintraege: Map<number, KursListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	gotoEintrag: (eintrag: JahrgangsListeEintrag) => Promise<void>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
}