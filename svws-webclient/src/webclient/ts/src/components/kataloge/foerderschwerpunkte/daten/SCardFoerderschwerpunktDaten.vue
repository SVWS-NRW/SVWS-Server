<template>
	<svws-ui-content-card title="Daten">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="ID" v-model="id" type="text" />
			<svws-ui-text-input placeholder="Kürzel" v-model="inputKuerzel" type="text" />
			<svws-ui-text-input placeholder="Bezeichnung" v-model="inputBezeichnung" type="text" />
			<svws-ui-multi-select title="Statistikkürzel" v-model="inputStatistikKuerzel" :items="inputKatalogFoerderschwerpunkteStatistik"
				:item-text="(i: Foerderschwerpunkt) => i.daten.kuerzel + ' (' + i.daten.beschreibung + ')'" required />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { Foerderschwerpunkt } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.foerderschwerpunkte;

	const inputKatalogFoerderschwerpunkteStatistik: ComputedRef< Foerderschwerpunkt[] | undefined> = computed(() => {
		return Foerderschwerpunkt.values();
	});

	const id: ComputedRef<number | undefined> = computed(() => {
		return app.dataFoerderschwerpunkt.daten?.id.valueOf();
	});

	const inputKuerzel: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.dataFoerderschwerpunkt.daten?.kuerzel?.toString();
		},
		set(val: string | undefined) {
			app.dataFoerderschwerpunkt.patch({ kuerzel: val });
		}
	});

	const inputBezeichnung: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.dataFoerderschwerpunkt.daten?.text?.toString();
		},
		set(val: string | undefined) {
			app.dataFoerderschwerpunkt.patch({ text: val });
		}
	});

	const inputStatistikKuerzel: WritableComputedRef<Foerderschwerpunkt | undefined> = computed({
		get(): Foerderschwerpunkt | undefined {
			return Foerderschwerpunkt.getByKuerzel(
				app.dataFoerderschwerpunkt.daten?.kuerzelStatistik || null
			) || undefined;
		},
		set(val: Foerderschwerpunkt | undefined) {
			app.dataFoerderschwerpunkt.patch({
				kuerzelStatistik: val?.daten.kuerzel
			});
		}
	});

</script>
