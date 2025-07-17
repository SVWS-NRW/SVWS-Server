<template>
	<div class="page page-flex-col pt-0">
		<!-- Die Auflistung der Schüler, wo noch keine Zulassungsberechung vorliegt. -->
		<div v-if="!schuelerOhneZulassungsberechnung.isEmpty()" class="flex flex-col w-full h-min-fit">
			<div class="text-ui-danger font-bold">Für die folgenden Schüler liegt noch keine Berechnung der Zulassung vor: </div>
			<div v-for="schueler in schuelerOhneZulassungsberechnung" :key="schueler.id">
				{{ schueler.nachname }}, {{ schueler.vorname }}
			</div>
		</div>

		<!-- Die Auflistung der Schüler, welche keine Zulassung zur Abiturprüfung erhalten haben. -->
		<div v-if="!schuelerOhneZulassung.isEmpty()" class="flex flex-col w-full h-min-fit">
			<div class="text-ui-danger font-bold">Die folgenden Schüler haben keine Zulassung zum Abitur erhalten: </div>
			<div v-for="schueler in schuelerOhneZulassung" :key="schueler.id">
				{{ schueler.nachname }}, {{ schueler.vorname }}
			</div>
		</div>

		<div v-for="schueler in schuelerInPruefung" :key="schueler.id" class="flex flex-col w-full h-min-fit">
			<schueler-abitur-pruefungsuebersicht-tabelle :server-mode :schule :schueler :manager="() => managerMap().get(schueler.id)!" :update-abiturpruefungsdaten />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { ArrayList, type SchuelerListeEintrag } from "@core";
	import type { List } from "@core";
	import type { GostAbiturPruefungsuebersichtProps } from "./GostAbiturPruefungsuebersichtProps";

	const props = defineProps<GostAbiturPruefungsuebersichtProps>();

	const schuelerInPruefung = computed<List<SchuelerListeEintrag>>(() => {
		const result = new ArrayList<SchuelerListeEintrag>();
		for (const schueler of props.schuelerListe)
			if ((props.managerMap().get(schueler.id) !== null) && (props.managerMap().get(schueler.id)?.daten().block1Zulassung === true))
				result.add(schueler);
		return result;
	});

	const schuelerOhneZulassung = computed<List<SchuelerListeEintrag>>(() => {
		const result = new ArrayList<SchuelerListeEintrag>();
		for (const schueler of props.schuelerListe)
			if ((props.managerMap().get(schueler.id) !== null) && (props.managerMap().get(schueler.id)?.daten().block1Zulassung !== true))
				result.add(schueler);
		return result;
	});

	const schuelerOhneZulassungsberechnung = computed<List<SchuelerListeEintrag>>(() => {
		const result = new ArrayList<SchuelerListeEintrag>();
		for (const schueler of props.schuelerListe)
			if (props.managerMap().get(schueler.id) === null)
				result.add(schueler);
		return result;
	});

</script>
