import { FoerderschwerpunktEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

export interface FoerderschwerpunkteAuswahlProps {
		auswahl: FoerderschwerpunktEintrag | undefined;
		listFoerderschwerpunkte: List<FoerderschwerpunktEintrag>;
		abschnitte: Map<number, Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		aktSchulabschnitt: number;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
		setFoerderschwerpunkt: (foerderschwerpunkt: FoerderschwerpunktEintrag) => Promise<void>;
	}