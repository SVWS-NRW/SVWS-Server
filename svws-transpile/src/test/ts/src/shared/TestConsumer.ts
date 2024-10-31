import type { Consumer } from "../../../../main/resources/typescript/java/util/function/Consumer";
import type { TestPerson } from "./TestPerson";

export class TestConsumer implements Consumer<string | number | TestPerson> {
	public value: (string | number | TestPerson)[] = [];
	public accept(e: string | number | TestPerson): void {
		this.value.push(e);
	}
}
