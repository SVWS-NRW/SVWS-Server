import { ReligionEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

export interface ReligionenAuswahlProps {
		auswahl: ReligionEintrag | undefined;
		listReligionen: List<ReligionEintrag>;
		abschnitte: Map<number, Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
		setReligion: (religion: ReligionEintrag) => Promise<void>;
	}