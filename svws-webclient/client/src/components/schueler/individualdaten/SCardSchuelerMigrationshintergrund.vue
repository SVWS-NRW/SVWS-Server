<template>
	<svws-ui-content-card title="Migrationshintergrund">
		<template #actions>
			<svws-ui-checkbox v-model="hatMigrationshintergrund" statistics>Migrationshintergrund vorhanden</svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Zuzugsjahr" :model-value="data().zuzugsjahr" @change="zuzugsjahr=>doPatch({zuzugsjahr})" type="text" :disabled="!hatMigrationshintergrund" statistics />
			<svws-ui-select title="Geburtsland" v-model="geburtsland" :items="Nationalitaeten.values()" :item-text="(i: Nationalitaeten) => `${i.daten.bezeichnung} (${i.daten.iso3})`" :item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" :disabled="!hatMigrationshintergrund" autocomplete statistics />
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

	import type { SchuelerStammdaten } from "@core";
	import type { WritableComputedRef } from "vue";
	import { verkehrsspracheKatalogEintragFilter, verkehrsspracheKatalogEintragSort, nationalitaetenKatalogEintragFilter, nationalitaetenKatalogEintragSort } from "~/utils/helfer";
	import { Nationalitaeten, Verkehrssprache } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		data: () => SchuelerStammdaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerStammdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerStammdaten>) {
		emit('patch', data);
	}

	const geburtsland: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.data().geburtsland) || Nationalitaeten.DEU,
		set: (value) => doPatch({ geburtsland: value.daten.iso3 })
	});

	const geburtslandMutter: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.data().geburtslandMutter) || Nationalitaeten.DEU,
		set: (value) => doPatch({ geburtslandMutter: value.daten.iso3 })
	});

	const geburtslandVater: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.data().geburtslandVater) || Nationalitaeten.DEU,
		set: (value) => doPatch({ geburtslandVater: value.daten.iso3 })
	});

	const verkehrsprache: WritableComputedRef<Verkehrssprache> = computed({
		get: () => Verkehrssprache.getByKuerzelAuto(props.data().verkehrspracheFamilie) || Verkehrssprache.DEU,
		set: (value) => doPatch({ verkehrspracheFamilie: value.daten.kuerzel })
	});

	const hatMigrationshintergrund: WritableComputedRef<boolean> = computed({
		get: () => props.data().hatMigrationshintergrund,
		set: (value) => doPatch({ hatMigrationshintergrund: value })
	});

</script>
