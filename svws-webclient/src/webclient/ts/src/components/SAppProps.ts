import { SchuleStammdaten, Schulform } from "@svws-nrw/svws-core-ts";

export interface AppProps {
	schulform: Schulform;
	schuleStammdaten: SchuleStammdaten;
	username: string;
	logout: () => Promise<void>;
}