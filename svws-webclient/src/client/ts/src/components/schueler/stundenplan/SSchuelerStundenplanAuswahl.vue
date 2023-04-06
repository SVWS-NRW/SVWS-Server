<template>
	<svws-ui-data-table clickable :clicked="stundenplan" @update:clicked="gotoStundenplan" :columns="cols" :items="mapStundenplaene.values()" />
</template>

<script setup lang="ts">

	import { SchuelerStundenplanAuswahlProps } from "./SSchuelerStundenplanAuswahlProps";

	const props = defineProps<SchuelerStundenplanAuswahlProps>();

	const cols = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "gueltigAb", label: "von", span: 1, sortable: false, defaultSort: 'asc' },
		{ key: "gueltigBis", label: "bis", span: 1, sortable: false }
	];

</script>