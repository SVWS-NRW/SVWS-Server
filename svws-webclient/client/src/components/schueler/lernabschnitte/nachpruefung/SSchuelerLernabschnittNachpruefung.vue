<template>
	<div class="h-full w-full grid gap-4 grid-cols-2">
		<svws-ui-content-card>
			<svws-ui-todo />
			<div v-if="manager().lernabschnittGet().nachpruefungen !== null">
				<div>mögliche Nachprüfungsfächer</div>
				<div v-for="fach in manager().lernabschnittGet().nachpruefungen!.moegliche" :key="fach">
					{{ fach }}
				</div>
				<div>Nachprüfungen</div>
				<div v-for="pruefung in manager().lernabschnittGet().nachpruefungen!.pruefungen" :key="pruefung.fachID">
					{{ `${pruefung.grund} ${pruefung.datum} ${pruefung.fachID} ${pruefung.note}` }}
				</div>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerLernabschnittNachpruefungProps } from "./SSchuelerLernabschnittNachpruefungProps";

	const props = defineProps<SchuelerLernabschnittNachpruefungProps>();

</script>
