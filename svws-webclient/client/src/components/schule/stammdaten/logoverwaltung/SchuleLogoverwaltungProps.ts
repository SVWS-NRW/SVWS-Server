import type { List, Logo } from "@core";

export interface SchuleLogoverwaltungProps {
	logos: () => List<Logo>;
	patchLogo: (logo: Partial<Logo>, id: number) => Promise<void>;
	addLogo: (logo: Logo) => Promise<Logo>;
	deleteLogo: (logos: Logo[]) => Promise<void>;
}
