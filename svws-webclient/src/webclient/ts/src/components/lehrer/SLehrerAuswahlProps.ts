import { LehrerListeEintrag, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

export interface LehrerAuswahlProps {
		auswahl: LehrerListeEintrag | undefined;
		mapLehrer: Map<number, LehrerListeEintrag>;
		gotoLehrer: (value: LehrerListeEintrag | undefined) => Promise<void>;
		abschnitte: Map<number, Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		aktSchulabschnitt: number;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	}