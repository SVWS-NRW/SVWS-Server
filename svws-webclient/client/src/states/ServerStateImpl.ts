import { ServerMode } from "@core/core/types/ServerMode";
import type { ServerState } from "@ui/states/ServerState";
import { StateManager } from "@ui/ui/StateManager";
import { api } from "~/router/Api";

interface ServerReactiveState {
	/** Der ServerMode des Servers */
	mode: ServerMode;
}

/**
 * Die Schnittstelle für den Zustand des Servers
 */
export class ServerStateImpl extends StateManager<ServerReactiveState> implements ServerState {

	public constructor() {
		super({
			mode: ServerMode.STABLE,
		});
	}

	public async init(): Promise<void> {
		const serverModeString = await api.server.getServerModus();
		const mode = ServerMode.getByText(serverModeString);
		this.setPatchedDefaultState({ mode });
	}

	public get mode(): ServerMode {
		return this.state.mode;
	}

	public get hasDev(): boolean {
		return ServerMode.DEV.checkServerMode(this.mode);
	}

	public get hasAlpha(): boolean {
		return ServerMode.ALPHA.checkServerMode(this.mode);
	}

	public get hasBeta(): boolean {
		return ServerMode.BETA.checkServerMode(this.mode);
	}

}

export const serverStateImpl = new ServerStateImpl();
