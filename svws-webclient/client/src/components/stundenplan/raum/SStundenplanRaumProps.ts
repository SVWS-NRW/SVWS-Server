import type { StundenplanManager, BenutzerKompetenz, Schulform, ServerMode } from "@core";

export interface StundenplanRaumProps {
	// schulform: Schulform;
	// serverMode: ServerMode;
	// benutzerKompetenzen: Set<BenutzerKompetenz>,
	stundenplanManager: () => StundenplanManager;
	ganzerStundenplanRaeume: () => boolean;
	setGanzerStundenplanRaeume: (value: boolean) => Promise<void>;
}