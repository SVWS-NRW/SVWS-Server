<template>
	<svws-ui-content-card title="Daten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-multi-select
					v-model="inputStatistikKuerzel"
					title="Statistikkürzel"
					:items="inputKatalogReligionenStatistik"
					:item-text="(i: Religion) => i.daten.kuerzel.toString()"
					required
				/>
				<svws-ui-text-input
					v-model="inputKuerzel"
					type="text"
					placeholder="Kürzel"
				/>
				<svws-ui-text-input
					v-model="inputText"
					type="text"
					placeholder="Bezeichnung"
				/>
				<svws-ui-text-input
					v-model="inputTextzeugnis"
					type="text"
					placeholder="Zeugnisbezeichnung"
				/>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { Religion} from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.religionen
	const inputKatalogReligionenStatistik: ComputedRef<
		Religion[] | undefined
	> = computed(() => {
		return Religion.values();
	});

	const id: ComputedRef<number | undefined> = computed(() => {
		return app.dataReligion.daten?.id.valueOf();
	});

	const inputKuerzel: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.dataReligion.daten?.kuerzel?.toString();
		},
		set(val: string | undefined) {
			app.dataReligion.patch({ kuerzel: val });
		}
	});

	const inputText: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.dataReligion.daten?.text?.toString();
		},
		set(val: string | undefined) {
			app.dataReligion.patch({ text: val });
		}
	});

	const inputTextzeugnis: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.dataReligion.daten?.textZeugnis?.toString();
		},
		set(val: string | undefined) {
			app.dataReligion.patch({ textZeugnis: val });
		}
	});

	const inputStatistikKuerzel: WritableComputedRef<Religion | undefined> = computed({
		get(): Religion | undefined {
			return Religion.getByKuerzel(
				app.dataReligion.daten?.kuerzel || null
			) || undefined;
		},
		set(val: Religion | undefined) {
			app.dataReligion.patch({
				kuerzel: val?.daten.kuerzel
			});
		}
	});

</script>
