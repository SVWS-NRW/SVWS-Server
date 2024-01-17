<template>
	<svws-ui-content-card title="Migrationshintergrund">
		<template #actions>
			<svws-ui-checkbox :model-value="hatMigrationshintergrund" @update:model-value="hatMigrationshintergrund => patch({hatMigrationshintergrund})">
				Migrationshintergrund vorhanden
			</svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-input-number placeholder="Zuzugsjahr" :model-value="data.zuzugsjahr" @change="zuzugsjahr => patch({zuzugsjahr})"
				:disabled="!hatMigrationshintergrund" statistics hide-stepper />
			<svws-ui-select title="Geburtsland" v-model="geburtsland" :items="Nationalitaeten.values()" :item-text="i => `${i.daten.bezeichnung} (${i.daten.iso3})`"
				:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter"
				:disabled="!hatMigrationshintergrund" autocomplete statistics />
			<svws-ui-select title="Verkehrssprache" v-model="verkehrsprache" autocomplete :items="Verkehrssprache.values()"
				:item-text="i => `${i.daten.bezeichnung} (${i.daten.kuerzel})`" :item-sort="verkehrsspracheKatalogEintragSort"
				:item-filter="verkehrsspracheKatalogEintragFilter" :disabled="!hatMigrationshintergrund" class="col-span-full" statistics />
			<svws-ui-select title="Geburtsland Mutter" v-model="geburtslandMutter" :items="Nationalitaeten.values()"
				:item-text="i => `${i.daten.bezeichnung} (${i.daten.iso3})`" :item-sort="nationalitaetenKatalogEintragSort"
				:item-filter="nationalitaetenKatalogEintragFilter" :disabled="!hatMigrationshintergrund" autocomplete statistics />
			<svws-ui-select title="Geburtsland Vater" v-model="geburtslandVater" :items="Nationalitaeten.values()"
				:item-text="i => `${i.daten.bezeichnung} (${i.daten.iso3})`" :item-sort="nationalitaetenKatalogEintragSort"
				:item-filter="nationalitaetenKatalogEintragFilter" :disabled="!hatMigrationshintergrund" autocomplete statistics />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerListeManager, SchuelerStammdaten } from "@core";
	import type { WritableComputedRef } from "vue";
	import { verkehrsspracheKatalogEintragFilter, verkehrsspracheKatalogEintragSort, nationalitaetenKatalogEintragFilter, nationalitaetenKatalogEintragSort } from "~/utils/helfer";
	import { Nationalitaeten, Verkehrssprache } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		schuelerListeManager: () => SchuelerListeManager;
		patch: (data: Partial<SchuelerStammdaten>) => Promise<void>;
	}>();

	const data = computed<SchuelerStammdaten>(() => props.schuelerListeManager().daten());
	const hatMigrationshintergrund = computed<boolean>(() => props.schuelerListeManager().daten().hatMigrationshintergrund);

	const geburtsland: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(data.value.geburtsland) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ geburtsland: value.daten.iso3 })
	});

	const geburtslandMutter: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(data.value.geburtslandMutter) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ geburtslandMutter: value.daten.iso3 })
	});

	const geburtslandVater: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(data.value.geburtslandVater) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ geburtslandVater: value.daten.iso3 })
	});

	const verkehrsprache: WritableComputedRef<Verkehrssprache> = computed({
		get: () => Verkehrssprache.getByKuerzelAuto(data.value.verkehrspracheFamilie) || Verkehrssprache.DEU,
		set: (value) => void props.patch({ verkehrspracheFamilie: value.daten.kuerzel })
	});

</script>
