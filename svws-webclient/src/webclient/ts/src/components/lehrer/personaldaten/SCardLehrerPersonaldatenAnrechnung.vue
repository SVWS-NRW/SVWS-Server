<template>
	<svws-ui-content-card title="Mehr- / Minderleistung /Anrechnungsstunden">
		<div class="input-wrapper">
			<svws-ui-multi-select
				v-model="mehrleistungsgrund"
				title="Mehrleistung"
				:items="mehrleistungsgruende_liste"
				:item-text="(i: LehrerMehrleistungArt) =>i.daten.text"
			/>
			<svws-ui-multi-select
				v-model="minderleistungsgrund"
				title="Minderleistung"
				:items="minderleistungsgruende_liste"
				:item-text="(i: LehrerMinderleistungArt) =>i.daten.text"
			/>
			<svws-ui-text-input
				v-model="pflichtstundensoll"
				type="text"
				placeholder="Stundensumme"
			/>
			<svws-ui-multi-select
				v-model="anrechnungsgrund"
				title="Nicht unterichtliche Tätigkeiten"
				:items="anrechnungsgruende_liste"
				:item-text="(i: LehrerAnrechnungsgrund) =>i.daten.text"
			/>
			<svws-ui-text-input
				v-model="stammschulnummer"
				type="text"
				placeholder="Stammschulnummer"
			/>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import {
		LehrerAnrechnungsgrund,
		LehrerMehrleistungArt,
		LehrerMinderleistungArt
	} from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.lehrer;

	const mehrleistungsgruende_liste: ComputedRef<LehrerMehrleistungArt[]> =
		computed(() => {
			return LehrerMehrleistungArt.values();
		});

	const minderleistungsgruende_liste: ComputedRef<LehrerMinderleistungArt[]> =
		computed(() => {
			return LehrerMinderleistungArt.values();
		});

	const anrechnungsgruende_liste: ComputedRef<LehrerAnrechnungsgrund[]> =
		computed(() => {
			return LehrerAnrechnungsgrund.values();
		});

	const mehrleistungsgrund: WritableComputedRef<
		LehrerMehrleistungArt | undefined
	> = computed({
		get(): LehrerMehrleistungArt | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return mehrleistungsgruende_liste.value.find(
				(e: LehrerMehrleistungArt) => e.daten.kuerzel === kuerzel
			);
		},
		set(val: LehrerMehrleistungArt | undefined) {
			// TODO
			// this.app.personaldaten.patch({
			//	mehrleistungsgrund: val?.kuerzel
			// });
		}
	});

	const minderleistungsgrund: WritableComputedRef<
		LehrerMinderleistungArt | undefined
	> = computed({
		get(): LehrerMinderleistungArt | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return minderleistungsgruende_liste.value.find(
				(e: LehrerMinderleistungArt) => e.daten.kuerzel === kuerzel
			);
		},
		set(val: LehrerMinderleistungArt | undefined) {
			// TODO
			// this.app.personaldaten.patch({
			//	minderleistungsgrund: val?.kuerzel
			// });
		}
	});

	const anrechnungsgrund: WritableComputedRef<
		LehrerAnrechnungsgrund | undefined
	> = computed({
		get(): LehrerAnrechnungsgrund | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return anrechnungsgruende_liste.value.find(
				e => e.daten.kuerzel === kuerzel
			);
		},
		set(val: LehrerAnrechnungsgrund | undefined) {
			// TODO
			// this.app.personaldaten.patch({
			//	anrechnungsgrund: val?.kuerzel
			// });
		}
	});

	const pflichtstundensoll: WritableComputedRef<number | undefined> =
		computed({
			get(): number | undefined {
				return app.personaldaten?.daten?.pflichtstundensoll?.valueOf();
			},
			set(val: number | undefined) {
				app.personaldaten.patch({ pflichtstundensoll: val });
			}
		});

	const stammschulnummer: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.personaldaten?.daten?.stammschulnummer?.toString();
		},
		set(val: string | undefined) {
			app.personaldaten.patch({ stammschulnummer: val });
		}
	});
</script>
