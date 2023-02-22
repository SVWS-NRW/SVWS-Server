import { List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

export interface KatalogeAuswahlProps {
	abschnitte: List<Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
}