import { BaseList } from "./BaseList";

export class ListNone extends BaseList<unknown> {

	protected _filter: undefined;

	public async update_list(): Promise<void> {
	}

	public async on_select(): Promise<void> {
	}

}
