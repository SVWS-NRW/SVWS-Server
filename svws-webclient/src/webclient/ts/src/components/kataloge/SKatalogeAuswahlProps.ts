import { Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

export interface KatalogeAuswahlProps {
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
}