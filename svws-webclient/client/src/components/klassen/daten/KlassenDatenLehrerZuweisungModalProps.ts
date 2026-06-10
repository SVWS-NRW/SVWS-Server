import type { KlassenListeManager } from "@ui";

export interface KlassenDatenLehrerZuweisungModalProps {
	manager: () => KlassenListeManager,
	addKlassenleitung: (idLehrer: number, idKlasse: number) => Promise<void>;
}
