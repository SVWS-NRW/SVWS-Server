import { ReligionEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

export interface ReligionenAuswahlProps {
		auswahl: ReligionEintrag | undefined;
		listReligionen: List<ReligionEintrag>;
		addReligion: (religion: ReligionEintrag) => Promise<void>;
		abschnitte: Map<number, Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		aktSchulabschnitt: number;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
		setReligion: (religion: ReligionEintrag) => Promise<void>;
	}