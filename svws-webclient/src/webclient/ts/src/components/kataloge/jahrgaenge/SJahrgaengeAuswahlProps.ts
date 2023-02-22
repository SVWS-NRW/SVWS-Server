import { JahrgangsListeEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

export interface JahrgaengeAuswahlProps {
	auswahl: JahrgangsListeEintrag | undefined;
	listJahrgaenge: List<JahrgangsListeEintrag>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	setJahrgang: (jahrgang: JahrgangsListeEintrag) => Promise<void>;
}