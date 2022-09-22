<template>
	<svws-ui-content-card title="Lehramtsangaben">
		<div class="input-wrapper">
			<svws-ui-multi-select
				v-model="lehramt"
				title="Lehramt"
				:items="lehraemter_liste"
				:item-text="(i: LehrerLehramt) => i.daten.text"
				required
			/>
			<svws-ui-multi-select
				v-model="lehramt_anerkennung"
				title="Anerkennung Lehramt"
				:items="lehramt_anerkennungen_liste"
				:item-text="(i: LehrerLehramtAnerkennung) => i.daten.text"
				required
			/>
			<svws-ui-multi-select
				v-model="lehrbefaehigung"
				title="Lehrbefähigung"
				:items="lehrbefaehigungen_liste"
				:item-text="(i: LehrerLehrbefaehigung) => i.daten.text"
				required
			/>
			<svws-ui-multi-select
				v-model="lehrbefaehigung_anerkennung"
				title="Anerkennung Lehrbefähigung"
				:items="lehrbefaehigung_anerkennungen_liste"
				:item-text="(i: LehrerLehrbefaehigungAnerkennung) => i.daten.text"
				required
			/>
			<svws-ui-multi-select
				v-model="fachrichtung"
				title="Fachrichtung"
				:items="fachrichtungen_liste"
				:item-text="(i: LehrerFachrichtung) => i.daten.text"
				required
			/>
			<svws-ui-multi-select
				v-model="fachrichtung_anerkennung"
				title="Anerkennung Fachrichtung"
				:items="fachrichtung_anerkennungen_liste"
				:item-text="(i: LehrerFachrichtungAnerkennung) => i.daten.text"
				required
			/>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import {
		LehrerFachrichtung,
		LehrerFachrichtungAnerkennung,
		LehrerLehramt,
		LehrerLehramtAnerkennung,
		LehrerLehrbefaehigung,
		LehrerLehrbefaehigungAnerkennung
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.lehrer;

	const lehraemter_liste: ComputedRef<LehrerLehramt[]> = computed(() => {
		return LehrerLehramt.values();
	});

	const lehramt_anerkennungen_liste: ComputedRef<LehrerLehramtAnerkennung[]> =
		computed(() => {
			return LehrerLehramtAnerkennung.values();
		});

	const lehrbefaehigungen_liste: ComputedRef<LehrerLehrbefaehigung[]> =
		computed(() => {
			return LehrerLehrbefaehigung.values();
		});

	const lehrbefaehigung_anerkennungen_liste: ComputedRef<
		LehrerLehrbefaehigungAnerkennung[]
	> = computed(() => {
		return LehrerLehrbefaehigungAnerkennung.values();
	});

	const fachrichtungen_liste: ComputedRef<LehrerFachrichtung[]> = computed(
		() => {
			return LehrerFachrichtung.values();
		}
	);

	const fachrichtung_anerkennungen_liste: ComputedRef<
		LehrerFachrichtungAnerkennung[]
	> = computed(() => {
		return LehrerFachrichtungAnerkennung.values();
	});

	const lehramt: WritableComputedRef<LehrerLehramt | undefined> = computed({
		get(): LehrerLehramt | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return lehraemter_liste.value.find(
				(e: LehrerLehramt) => e.daten.kuerzel === kuerzel
			);
		},
		set(val: LehrerLehramt | undefined) {
			// TODO
			// this.app.personaldaten.patch({
			//	lehramt: val?.kuerzel
			// });
		}
	});

	const lehramt_anerkennung: WritableComputedRef<
		LehrerLehramtAnerkennung | undefined
	> = computed({
		get(): LehrerLehramtAnerkennung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return lehramt_anerkennungen_liste.value.find(
				e => e.daten.kuerzel === kuerzel
			);
		},
		set(val: LehrerLehramtAnerkennung | undefined) {
			// TODO
			// this.app.personaldaten.patch({
			//	lehramtAnerkennung: val?.kuerzel
			// });
		}
	});

	const lehrbefaehigung: WritableComputedRef<
		LehrerLehrbefaehigung | undefined
	> = computed({
		get(): LehrerLehrbefaehigung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return lehrbefaehigungen_liste.value.find(
				e => e.daten.kuerzel === kuerzel
			);
		},
		set(val: LehrerLehrbefaehigung | undefined) {
			// TODO
			// this.app.personaldaten.patch({
			//	lehrbefaehigung: val?.kuerzel
			// });
		}
	});

	const lehrbefaehigung_anerkennung: WritableComputedRef<
		LehrerLehrbefaehigungAnerkennung | undefined
	> = computed({
		get(): LehrerLehrbefaehigungAnerkennung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return lehrbefaehigung_anerkennungen_liste.value.find(
				e => e.daten.kuerzel === kuerzel
			);
		},
		set(val: LehrerLehrbefaehigungAnerkennung | undefined) {
			// TODO
			// this.app.personaldaten.patch({
			//	lehrbefaehigungAnerkennung: val?.kuerzel
			// });
		}
	});

	const fachrichtung: WritableComputedRef<LehrerFachrichtung | undefined> =
		computed({
			get(): LehrerFachrichtung | undefined {
				// TODO aus Personaldaten bestimmten
				const kuerzel = undefined;
				return fachrichtungen_liste.value.find(
					e => e.daten.kuerzel === kuerzel
				);
			},
			set(val: LehrerFachrichtung | undefined) {
				// TODO
				// this.app.personaldaten.patch({
				//	fachrichtung: val?.kuerzel
				// });
			}
		});

	const fachrichtung_anerkennung: WritableComputedRef<
		LehrerFachrichtungAnerkennung | undefined
	> = computed({
		get(): LehrerFachrichtungAnerkennung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return fachrichtung_anerkennungen_liste.value.find(
				e => e.daten.kuerzel === kuerzel
			);
		},
		set(val: LehrerFachrichtungAnerkennung | undefined) {
			// TODO
			// this.app.personaldaten.patch({
			//	fachrichtungAnerkennung: val?.kuerzel
			// });
		}
	});
</script>
