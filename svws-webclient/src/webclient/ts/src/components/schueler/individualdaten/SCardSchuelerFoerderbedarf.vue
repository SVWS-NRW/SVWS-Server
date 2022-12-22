<template>
	<svws-ui-content-card title="Sonderpädagogische Förderung">
		<div class="input-wrapper">
			<svws-ui-multi-select title="Haupt-Förderschwerpunkt" v-model="inputFoerderschwerpunktID" :items="inputKatalogFoerderschwerpunkte" />
			<svws-ui-multi-select title="Weiterer-Förderschwerpunkt" v-model="inputFoerderschwerpunkt2ID" :items="inputKatalogFoerderschwerpunkte" />
			<svws-ui-checkbox v-model="inputIstAOSF">AOSF</svws-ui-checkbox>
			<svws-ui-checkbox v-model="inputIstLernenZieldifferent">Zieldifferntes Lernen</svws-ui-checkbox>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { FoerderschwerpunktEintrag, List, SchuelerStammdaten } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const daten: ComputedRef<SchuelerStammdaten> = computed(() => app.stammdaten.daten || new SchuelerStammdaten());

	const inputKatalogFoerderschwerpunkte: ComputedRef<List<FoerderschwerpunktEintrag>> = computed(() => app.katalogFoerderschwerpunkte);

	const inputFoerderschwerpunktID: WritableComputedRef<FoerderschwerpunktEintrag | undefined> = computed({
		get(): FoerderschwerpunktEintrag | undefined {
			const id = daten.value.foerderschwerpunktID;
			for (const schwerpunkt of inputKatalogFoerderschwerpunkte.value)
				if (schwerpunkt.id === id)
					return schwerpunkt
		},
		set(val: FoerderschwerpunktEintrag | undefined) {
			app.stammdaten.patch({ foerderschwerpunktID: val?.id });
		}
	});

	const inputFoerderschwerpunkt2ID: WritableComputedRef<FoerderschwerpunktEintrag | undefined> = computed({
		get(): FoerderschwerpunktEintrag | undefined {
			const id = daten.value.foerderschwerpunkt2ID;
			for (const schwerpunkt of inputKatalogFoerderschwerpunkte.value)
				if (schwerpunkt.id === id)
					return schwerpunkt
		},
		set(val: FoerderschwerpunktEintrag | undefined) {
			app.stammdaten.patch({ foerderschwerpunkt2ID: val?.id });
		}
	});

	const inputIstAOSF: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean { 
			return !!daten.value.istAOSF; 
		},
		set(val) { 
			app.stammdaten.patch({ istAOSF: val });
		}
	});

	const inputIstLernenZieldifferent: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean { 
			return !!daten.value.istLernenZieldifferent; 
		},
		set(val) { 
			app.stammdaten.patch({ istLernenZieldifferent: val }); 
		}
	});

</script>
