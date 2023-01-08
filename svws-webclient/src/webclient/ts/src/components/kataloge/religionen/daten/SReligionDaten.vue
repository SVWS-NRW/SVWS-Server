<template>
	<div v-if="visible" class="app-container">
		<s-card-religion-daten :data="data" />
	</div>
</template>

<script setup lang="ts">

	import { ReligionEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataReligion } from "~/apps/kataloge/religionen/DataReligion";
	import { routeKatalogReligionDaten } from "~/router/apps/religion/RouteKatalogReligionDaten";

	const { item, data } = defineProps<{ 
		item: ShallowRef<ReligionEintrag | undefined>;
		data: DataReligion;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (item.value !== undefined) && (!routeKatalogReligionDaten.hidden);
	});

</script>