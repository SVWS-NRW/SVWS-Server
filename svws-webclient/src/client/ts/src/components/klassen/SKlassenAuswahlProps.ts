import type { KlassenListeEintrag, Schuljahresabschnitt } from "@svws-nrw/svws-core";

export interface KlassenAuswahlProps {
		auswahl: KlassenListeEintrag | undefined,
		mapKatalogeintraege: Map<number, KlassenListeEintrag>;
		gotoEintrag: (eintrag: KlassenListeEintrag) => Promise<void>;
		abschnitte: Map<number, Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		aktSchulabschnitt: number;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	}