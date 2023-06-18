<template>
	<div v-if="mapStundenplaene.size === 0" class="flex flex-row">
		Derzeit liegt kein Stundenplan für diesen Lernabschnitt vor.
	</div>
	<div v-else class="flex flex-row">
		<svws-ui-data-table clickable :clicked="stundenplan" @update:clicked="gotoStundenplan" :columns="cols" :items="mapStundenplaene.values()" />
		<div> <router-view :key="$route.hash" /> </div>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerStundenplanAuswahlProps } from "./SSchuelerStundenplanAuswahlProps";

	defineProps<SchuelerStundenplanAuswahlProps>();

	const cols = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "gueltigAb", label: "von", span: 1, sortable: false, defaultSort: 'asc' },
		{ key: "gueltigBis", label: "bis", span: 1, sortable: false }
	];

</script>