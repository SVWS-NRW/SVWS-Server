import type { BenutzergruppeListeEintrag} from "@svws-nrw/svws-core";
export interface BenutzergruppeAuswahlProps {
    auswahl: () => BenutzergruppeListeEintrag |undefined;
    mapBenutzergruppe: Map<number, BenutzergruppeListeEintrag>;
	gotoBenutzergruppe: (benutzer: BenutzergruppeListeEintrag) => Promise<void>;
	createBenutzergruppe : (bezeichnung: string, istAdmin: boolean) => Promise<void>;
	deleteBenutzergruppen : (selectedItems: BenutzergruppeListeEintrag[]) => Promise<void>;
	gotoSchule: () => Promise<void>;
}