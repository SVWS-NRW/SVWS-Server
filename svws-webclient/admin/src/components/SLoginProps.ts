
export interface LoginProps {
	authenticated: boolean;
	login: (username: string, password: string) => Promise<void>;
	connectTo: () => Promise<boolean>;
	schemaPrevious: string | null;
}
