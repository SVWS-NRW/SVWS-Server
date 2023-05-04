export interface InitProps {
	goto: (route: string) => Promise<void>;
}