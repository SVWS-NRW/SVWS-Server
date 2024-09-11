import type { Consumer } from "@transpiled";
import type { TestPerson } from "./TestPerson";

export class TestConsumer implements Consumer<string | number | TestPerson> {
	public value: (string | number | TestPerson)[] = [];
	public accept(e: string | number | TestPerson): void {
		this.value.push(e);
	}
}
