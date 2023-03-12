<template>
	<svws-ui-data-table clickable :clicked="stundenplan" @update:clicked="setStundenplan" :columns="cols" :items="getStundenplaene()" />
</template>

<script setup lang="ts">

	import { StundenplanListeEintrag } from "@svws-nrw/svws-core";
	import { DataTableColumn, DataTableItem } from "@ui";
	import { SchuelerStundenplanAuswahlProps } from "./SSchuelerStundenplanAuswahlProps";

	const props = defineProps<SchuelerStundenplanAuswahlProps>();

	async function setStundenplan(value: DataTableItem) {
		return await props.setStundenplan(value as StundenplanListeEintrag | undefined);
	}

	function getStundenplaene() {
		return props.stundenplaene.toArray() as StundenplanListeEintrag[];
	}

	const cols: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "gueltigAb", label: "von", span: 1, sortable: false, defaultSort: 'asc' },
		{ key: "gueltigBis", label: "bis", span: 1, sortable: false }
	];

</script>