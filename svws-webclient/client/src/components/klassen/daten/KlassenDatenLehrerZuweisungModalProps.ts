import type { KlassenListeManager } from "~/states/klassen/KlassenListeManager";

export interface KlassenDatenLehrerZuweisungModalProps {
	manager: () => KlassenListeManager,
	addKlassenleitung: (idLehrer: number, idKlasse: number) => Promise<void>;
}
