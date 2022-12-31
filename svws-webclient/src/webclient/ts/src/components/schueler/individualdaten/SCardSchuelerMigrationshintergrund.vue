<template>
	<svws-ui-content-card title="Migrationshintergrund">
		<div class="input-wrapper">
			<svws-ui-checkbox v-model="inputHatMigrationshintergrund">Migrationshintergrund vorhanden</svws-ui-checkbox>
			<svws-ui-text-input placeholder="Zuzugsjahr" v-model="inputZuzugsjahr" type="text" :disabled="!inputHatMigrationshintergrund" />
			<svws-ui-multi-select title="Geburtsland" v-model="inputGeburtsland" :items="Nationalitaeten.values()" :item-text="(i: Nationalitaeten) => `${i.daten.bezeichnung} (${i.daten.iso3})`"
				:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" :disabled="!inputHatMigrationshintergrund" />
			<svws-ui-multi-select title="Verkehrssprache" v-model="inputVerkehrspracheFamilie" autocomplete :items="Verkehrssprache.values()"
				:item-text="(i: Verkehrssprache) => `${i.daten.bezeichnung} (${i.daten.kuerzel})`" :item-sort="verkehrsspracheKatalogEintragSort"
				:item-filter="verkehrsspracheKatalogEintragFilter" :disabled="!inputHatMigrationshintergrund" />
			<svws-ui-multi-select title="Geburtsland Mutter" v-model="inputGeburtslandMutter" :items="Nationalitaeten.values()"
				:item-text="(i: Nationalitaeten) => `${i.daten.bezeichnung} (${i.daten.iso3})`" :item-sort="nationalitaetenKatalogEintragSort"
				:item-filter="nationalitaetenKatalogEintragFilter" :disabled="!inputHatMigrationshintergrund" autocomplete />
			<svws-ui-multi-select title="Geburtsland Vater" v-model="inputGeburtslandVater" :items="Nationalitaeten.values()"
				:item-text="(i: Nationalitaeten) => `${i.daten.bezeichnung} (${i.daten.iso3})`" :item-sort="nationalitaetenKatalogEintragSort"
				:item-filter="nationalitaetenKatalogEintragFilter" :disabled="!inputHatMigrationshintergrund" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { Nationalitaeten, Verkehrssprache, SchuelerStammdaten } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";

	import { verkehrsspracheKatalogEintragFilter, verkehrsspracheKatalogEintragSort, nationalitaetenKatalogEintragFilter, nationalitaetenKatalogEintragSort } from "~/helfer";

	const props = defineProps<{ stammdaten: DataSchuelerStammdaten }>();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());


	const inputGeburtsland: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(daten.value.geburtsland) || Nationalitaeten.DEU,
		set: (value) => props.stammdaten.patch({ geburtsland: value.daten.iso3 })
	});

	const inputZuzugsjahr: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.zuzugsjahr?.toString(),
		set: (value) => props.stammdaten.patch({ zuzugsjahr: value })
	});

	const inputGeburtslandMutter: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(daten.value.geburtslandMutter) || Nationalitaeten.DEU,
		set: (value) => props.stammdaten.patch({ geburtslandMutter: value.daten.iso3 })
	});

	const inputGeburtslandVater: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(daten.value.geburtslandVater) || Nationalitaeten.DEU,
		set: (value) => props.stammdaten.patch({ geburtslandVater: value.daten.iso3 })
	});

	const inputVerkehrspracheFamilie: WritableComputedRef<Verkehrssprache> = computed({
		get: () => Verkehrssprache.getByKuerzelAuto(daten.value.verkehrspracheFamilie) || Verkehrssprache.DEU,
		set: (value) => props.stammdaten.patch({ verkehrspracheFamilie: value.daten.kuerzel })
	});

	const inputHatMigrationshintergrund: WritableComputedRef<boolean | undefined> = computed({
		get: () => daten.value.hatMigrationshintergrund,
		set: (value) => props.stammdaten.patch({ hatMigrationshintergrund: value })
	});

</script>
