<template>
	<div class="h-full w-full p-8 flex flex-row flex-grow">
		<div class="h-full w-96 mr-2">
			<svws-ui-data-table :items="stundenplanManager().kursGetMengeAsList()" :columns="cols" />
		</div>
		<div class="h-full w-full">
			TODO: Hier kommt das Zeitraster des Stundenplans hin, in welches von der linken Seite die Kurs-Unterrichte oder
			die Klassen-Unterricht hineingezogen werden können.
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanUnterrichtProps } from "./SStundenplanUnterrichtProps";

	const props = defineProps<StundenplanUnterrichtProps>();

	const cols = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "wochenstunden", label: "WS", span: 1, sortable: false }
	];

</script>
