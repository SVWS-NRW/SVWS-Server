import type { List, Logo } from "@core";

export interface SchuleLogoverwaltungProps {
	logos: () => List<Logo>;
	patchLogo: (logo: Partial<Logo>) => Promise<Logo>;
	addLogo: (logo: Logo) => Promise<Logo>;
	deleteLogo: (logos: Logo[]) => Promise<void>;
}
