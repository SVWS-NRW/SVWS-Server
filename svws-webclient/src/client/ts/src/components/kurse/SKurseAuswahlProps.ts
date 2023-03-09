import { JahrgangsListeEintrag, KursListeEintrag, LehrerListeEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core";

export interface KurseAuswahlProps {
	auswahl: KursListeEintrag | undefined;
	listKurse: List<KursListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	setKurs: (value: KursListeEintrag) => Promise<void>;
}