<template>
	<svws-ui-content-card title="Staatsangehörigkeit und Konfession">
		<div class="input-wrapper">
			<svws-ui-multi-select v-model="inputStaatsangehoerigkeit" title="1. Staatsangehörigkeit"
				:items="Nationalitaeten.values()" :item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit.toString()"
				:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter"
				required />
			<svws-ui-multi-select v-model="inputStaatsangehoerigkeit2" title="2. Staatsangehörigkeit"
				:items="Nationalitaeten.values()" :item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit.toString()"
				:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" />
			<svws-ui-multi-select v-model="inputReligionID" title="Konfession" :items="inputKatalogReligionen" required />
			<svws-ui-checkbox v-model="inputDruckeKonfessionAufZeugnisse">Konfession aufs Zeugnis</svws-ui-checkbox>
			<svws-ui-text-input v-model="inputReligionabmeldung" type="date"
				placeholder="Abmeldung vom Religionsunterricht" />
			<svws-ui-text-input v-model="inputReligionanmeldung" type="date" placeholder="Wiederanmeldung" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { Nationalitaeten, ReligionEintrag, SchuelerStammdaten } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort } from "../../../helfer";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";

	const props = defineProps<{ stammdaten: DataSchuelerStammdaten }>();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());


	const main: Main = injectMainApp();

	const inputStaatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(daten.value.staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => props.stammdaten.patch({ staatsangehoerigkeitID: value.daten.iso3 })
	});

	const inputStaatsangehoerigkeit2: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(daten.value.staatsangehoerigkeit2ID) || Nationalitaeten.DEU,
		set: (value) => props.stammdaten.patch({ staatsangehoerigkeit2ID: value.daten.iso3 })
	});

	const inputKatalogReligionen: ComputedRef<ReligionEintrag[]> = computed(() => main.kataloge.religionen.toArray() as ReligionEintrag[]);
	const inputReligionID: WritableComputedRef<ReligionEintrag | undefined> = computed({
		get: () => inputKatalogReligionen.value.find(n => n.id === daten.value.religionID),
		set: (value) => props.stammdaten.patch({ religionID: value?.id })
	});

	const inputReligionabmeldung: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.religionabmeldung?.toString(),
		set: (value) => props.stammdaten.patch({ religionabmeldung: value })
	});

	const inputReligionanmeldung: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.religionanmeldung?.toString(),
		set: (value) => props.stammdaten.patch({ religionanmeldung: value })
	});

	const inputDruckeKonfessionAufZeugnisse: WritableComputedRef<boolean> = computed({
		get: () => daten.value.druckeKonfessionAufZeugnisse,
		set: (value) => props.stammdaten.patch({ druckeKonfessionAufZeugnisse: value })
	});

</script>
