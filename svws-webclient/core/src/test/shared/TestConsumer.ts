import type { Consumer } from "../../java/util/function/Consumer";

export class TestConsumer<T> implements Consumer<T> {
	public value: (T)[] = [];
	public accept(e: T): void {
		this.value.push(e);
	}
}
