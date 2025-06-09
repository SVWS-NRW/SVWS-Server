<template>
	<div class="page page-flex-col pt-0">
		<div v-for="schueler in schuelerListe" :key="schueler.id" class="flex flex-col w-full h-min-fit">
			<!-- Darstellung der Prüfungsergebnisse aus dem persistierten Abiturbereich - Die Zulassung wird hier nur kurz Zusammengefasst -->
			<template v-if="managerMap().get(schueler.id) !== null">
				<template v-if="managerMap().get(schueler.id)?.daten().block1Zulassung ?? false">
					<schueler-abitur-pruefungsuebersicht-tabelle :server-mode :schule :schueler :manager="() => managerMap().get(schueler.id)!" :update-abiturpruefungsdaten />
				</template>
				<template v-else>
					<div class="text-ui-danger font-bold">{{ schueler.nachname }}, {{ schueler.vorname }} hat die Zulassung zum Abitur nicht erreicht.</div>
				</template>
			</template>
			<template v-else>
				<svws-ui-todo>
					{{ schueler.nachname }}, {{ schueler.vorname }}: Es wurde noch keine Berechnung zur Abitur-Zulassung
					in der Datenbank gespeichert. Führen Sie diese zunächst unter dem Reiter <i>Zulassung</i> aus.
				</svws-ui-todo>
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { GostAbiturPruefungsuebersichtProps } from "./GostAbiturPruefungsuebersichtProps";

	const props = defineProps<GostAbiturPruefungsuebersichtProps>();

</script>
