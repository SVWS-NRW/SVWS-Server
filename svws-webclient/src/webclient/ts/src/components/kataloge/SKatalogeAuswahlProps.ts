import { Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

export interface KatalogeAuswahlProps {
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
}