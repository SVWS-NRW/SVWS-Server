<template>
	<svws-ui-content-card title="Migrationshintergrund">
		<template #actions>
			<svws-ui-checkbox v-model="hatMigrationshintergrund">Migrationshintergrund vorhanden</svws-ui-checkbox>
			<!--<svws-ui-toggle v-model="hatMigrationshintergrund">Vorhanden</svws-ui-toggle>-->
		</template>
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Zuzugsjahr" v-model="zuzugsjahr" type="text" :disabled="!hatMigrationshintergrund" />
			<svws-ui-multi-select title="Geburtsland" v-model="geburtsland" :items="Nationalitaeten.values()" :item-text="(i: Nationalitaeten) => `${i.daten.bezeichnung} (${i.daten.iso3})`"
				:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" :disabled="!hatMigrationshintergrund" autocomplete />
			<div class="col-span-2">
				<svws-ui-multi-select title="Verkehrssprache" v-model="verkehrsprache" autocomplete :items="Verkehrssprache.values()"
					:item-text="(i: Verkehrssprache) => `${i.daten.bezeichnung} (${i.daten.kuerzel})`" :item-sort="verkehrsspracheKatalogEintragSort"
					:item-filter="verkehrsspracheKatalogEintragFilter" :disabled="!hatMigrationshintergrund" />
			</div>
			<svws-ui-multi-select title="Geburtsland Mutter" v-model="geburtslandMutter" :items="Nationalitaeten.values()"
				:item-text="(i: Nationalitaeten) => `${i.daten.bezeichnung} (${i.daten.iso3})`" :item-sort="nationalitaetenKatalogEintragSort"
				:item-filter="nationalitaetenKatalogEintragFilter" :disabled="!hatMigrationshintergrund" autocomplete />
			<svws-ui-multi-select title="Geburtsland Vater" v-model="geburtslandVater" :items="Nationalitaeten.values()"
				:item-text="(i: Nationalitaeten) => `${i.daten.bezeichnung} (${i.daten.iso3})`" :item-sort="nationalitaetenKatalogEintragSort"
				:item-filter="nationalitaetenKatalogEintragFilter" :disabled="!hatMigrationshintergrund" autocomplete />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { Nationalitaeten, Verkehrssprache, SchuelerStammdaten } from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef } from "vue";

	import { verkehrsspracheKatalogEintragFilter, verkehrsspracheKatalogEintragSort, nationalitaetenKatalogEintragFilter, nationalitaetenKatalogEintragSort } from "~/helfer";

	const props = defineProps<{
		data: SchuelerStammdaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerStammdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerStammdaten>) {
		emit('patch', data);
	}


	const geburtsland: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.data.geburtsland) || Nationalitaeten.DEU,
		set: (value) => doPatch({ geburtsland: value.daten.iso3 })
	});

	const zuzugsjahr: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.zuzugsjahr ?? undefined,
		set: (value) => doPatch({ zuzugsjahr: value })
	});

	const geburtslandMutter: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.data.geburtslandMutter) || Nationalitaeten.DEU,
		set: (value) => doPatch({ geburtslandMutter: value.daten.iso3 })
	});

	const geburtslandVater: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.data.geburtslandVater) || Nationalitaeten.DEU,
		set: (value) => doPatch({ geburtslandVater: value.daten.iso3 })
	});

	const verkehrsprache: WritableComputedRef<Verkehrssprache> = computed({
		get: () => Verkehrssprache.getByKuerzelAuto(props.data.verkehrspracheFamilie) || Verkehrssprache.DEU,
		set: (value) => doPatch({ verkehrspracheFamilie: value.daten.kuerzel })
	});

	const hatMigrationshintergrund: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.data.hatMigrationshintergrund,
		set: (value) => doPatch({ hatMigrationshintergrund: value })
	});

</script>
