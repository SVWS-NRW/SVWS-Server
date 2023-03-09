import { KursListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core";

export interface KurseAppProps {
		auswahl: KursListeEintrag | undefined;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}