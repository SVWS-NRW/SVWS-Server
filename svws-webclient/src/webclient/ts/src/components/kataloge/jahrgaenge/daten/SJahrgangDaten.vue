<template>
	<div v-if="visible" class="app-container">
		<s-card-jahrgang-basisdaten :data="data" :list-jahrgaenge="listJahrgaenge" />
	</div>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataJahrgang } from "~/apps/kataloge/jahrgaenge/DataJahrgang";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeKatalogJahrgaengeDaten } from "~/router/apps/jahrgaenge/RouteKatalogJahrgaengeDaten";

	const props = defineProps<{
		item: ShallowRef<JahrgangsListeEintrag | undefined>;
		data: DataJahrgang,
		schule: DataSchuleStammdaten;
		listJahrgaenge: JahrgangsListeEintrag[];
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKatalogJahrgaengeDaten.hidden()) && (props.item.value !== undefined);
	});

</script>
