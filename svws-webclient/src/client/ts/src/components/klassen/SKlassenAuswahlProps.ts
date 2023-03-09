import { KlassenListeEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core";

export interface KlassenAuswahlProps {
		auswahl: KlassenListeEintrag | undefined,
		listKlassen: List<KlassenListeEintrag>;
		abschnitte: Map<number, Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		aktSchulabschnitt: number;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
		setKlasse: (value: KlassenListeEintrag) =>  Promise<void>;
	}