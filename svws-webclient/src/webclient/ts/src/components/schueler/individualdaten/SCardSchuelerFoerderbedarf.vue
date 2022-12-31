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
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { DataKatalogFoerderschwerpunkte } from "~/apps/schueler/DataKatalogFoerderschwerpunkte";

	const props = defineProps<{ stammdaten: DataSchuelerStammdaten, foerderschwerpunkte: DataKatalogFoerderschwerpunkte }>();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());
	
	const inputKatalogFoerderschwerpunkte: ComputedRef<FoerderschwerpunktEintrag[]> = computed(() => props.foerderschwerpunkte.daten?.toArray() as FoerderschwerpunktEintrag[] || []);

	const inputFoerderschwerpunktID: WritableComputedRef<FoerderschwerpunktEintrag | undefined> = computed({
		get(): FoerderschwerpunktEintrag | undefined {
			const id = daten.value.foerderschwerpunktID;
			for (const schwerpunkt of inputKatalogFoerderschwerpunkte.value)
				if (schwerpunkt.id === id)
					return schwerpunkt
		},
		set(val: FoerderschwerpunktEintrag | undefined) {
			props.stammdaten.patch({ foerderschwerpunktID: val?.id });
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
			props.stammdaten.patch({ foerderschwerpunkt2ID: val?.id });
		}
	});

	const inputIstAOSF: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean { 
			return !!daten.value.istAOSF; 
		},
		set(val) { 
			props.stammdaten.patch({ istAOSF: val });
		}
	});

	const inputIstLernenZieldifferent: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean { 
			return !!daten.value.istLernenZieldifferent; 
		},
		set(val) { 
			props.stammdaten.patch({ istLernenZieldifferent: val }); 
		}
	});

</script>
