<template>
	<div class="page page-flex-col pt-0">
		<!-- Darstellung der Prüfungsergebnisse aus dem persistierten Abiturbereich - Die Zulassung wird hier nur kurz Zusammengefasst -->
		<template v-if="manager() !== null">
			<template v-if="hatZulassung">
				<schueler-abitur-pruefungsuebersicht-tabelle :server-mode :schule :schueler :manager="() => manager()!" :update-abiturpruefungsdaten />
			</template>
			<template v-else>
				<div class="text-ui-danger font-bold">Die Zulassung zum Abitur wurde nicht erreicht.</div>
			</template>
		</template>
		<template v-else>
			<svws-ui-todo>
				Es wurde noch keine Berechnung zur Abitur-Zulassung in der Datenbank gespeichert. Führen Sie diese
				zunächst unter dem Reiter <i>Zulassung</i> aus.
			</svws-ui-todo>
		</template>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';

	import type { SchuelerAbiturPruefungsuebersichtProps } from "./SchuelerAbiturPruefungsuebersichtProps";

	const props = defineProps<SchuelerAbiturPruefungsuebersichtProps>();

	const hatZulassung = computed<boolean>(() => props.manager()?.daten().block1Zulassung ?? false);
	const block1PunktSummeNormiert = computed<number>(() => props.manager()?.daten().block1PunktSummeNormiert ?? 0);

</script>
