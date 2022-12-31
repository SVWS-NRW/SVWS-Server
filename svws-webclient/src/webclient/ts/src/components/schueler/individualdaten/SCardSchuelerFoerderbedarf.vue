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
	import { FoerderschwerpunktEintrag, SchuelerStammdaten } from "@svws-nrw/svws-core-ts";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { DataKatalogFoerderschwerpunkte } from "~/apps/schueler/DataKatalogFoerderschwerpunkte";

	const props = defineProps<{ stammdaten: DataSchuelerStammdaten, foerderschwerpunkte: DataKatalogFoerderschwerpunkte }>();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());
	
	const inputKatalogFoerderschwerpunkte: ComputedRef<FoerderschwerpunktEintrag[]> = computed(() => props.foerderschwerpunkte.daten?.toArray() as FoerderschwerpunktEintrag[] || []);

	const inputFoerderschwerpunktID: WritableComputedRef<FoerderschwerpunktEintrag | undefined> = computed({
		get: () => inputKatalogFoerderschwerpunkte.value.find(n => n.id === props.stammdaten.daten?.foerderschwerpunktID),
		set: (value) => props.stammdaten.patch({ foerderschwerpunktID: value?.id })
	});

	const inputFoerderschwerpunkt2ID: WritableComputedRef<FoerderschwerpunktEintrag | undefined> = computed({
		get: () => inputKatalogFoerderschwerpunkte.value.find(n => n.id === props.stammdaten.daten?.foerderschwerpunkt2ID),
		set: (value) => props.stammdaten.patch({ foerderschwerpunkt2ID: value?.id })
	});

	const inputIstAOSF: WritableComputedRef<boolean | undefined> = computed({
		get: () => !!daten.value.istAOSF,
		set: (value) => props.stammdaten.patch({ istAOSF: value })
	});

	const inputIstLernenZieldifferent: WritableComputedRef<boolean | undefined> = computed({
		get: () => !!daten.value.istLernenZieldifferent,
		set: (value) => props.stammdaten.patch({ istLernenZieldifferent: value })
	});

</script>
