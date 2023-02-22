import { FaecherListeEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

export interface FaecherAuswahlProps {
	auswahl: FaecherListeEintrag | undefined;
	listFaecher: List<FaecherListeEintrag>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	setFach: (fach: FaecherListeEintrag) => Promise<void>;
}