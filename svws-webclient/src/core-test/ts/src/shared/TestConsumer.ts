/* eslint-disable @typescript-eslint/ban-types */
import type { LehrerListeEintrag, Consumer } from "~/index";

export class TestConsumer implements Consumer<string | number | LehrerListeEintrag> {
	public value: (string | number | LehrerListeEintrag)[] = [];
	public accept(e: string | number | LehrerListeEintrag): void {
		this.value.push(e);
	}
}
