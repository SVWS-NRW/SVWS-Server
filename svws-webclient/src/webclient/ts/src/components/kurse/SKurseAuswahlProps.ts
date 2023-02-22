import { JahrgangsListeEintrag, KursListeEintrag, LehrerListeEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

export interface KurseAuswahlProps {
	auswahl: KursListeEintrag | undefined;
	listKurse: List<KursListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	setKurs: (value: KursListeEintrag) => Promise<void>;
}