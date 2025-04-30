<template>
	<div class="page page-flex-row pt-0">
		<!-- Übersicht über die Fachbelegungen in der Q-Phase / Block I -->
		<schueler-abitur-zulassung :server-mode :schule :manager="managerLaufbahnplanung" />

		<!-- Darstellung der Information, ob bereits Daten im Abiturbereich vorliegen und ggf. Abweichungen zwischen den Daten deutlich hervorheben -->
		<template v-if="managerAbitur() !== null">
			<div>Es liegen bereits Daten zum Abitur vor.</div>
		</template>

		<!-- Zeige einen Hinweis, wenn noch nicht für alle Halbjahre der Qualifikationsphase Leistungsdaten vorliegen -->
		<template v-if="!istQPhaseBewertet()">
			<div> <span class="icon i-ri-alert-line icon-ui-danger" /> Es liegen noch nicht für alle Halbjahre der Qualifikationsphase Leistungsdaten vor.</div>
		</template>

		<!-- Wenn Belegungsfehler vorliegen, sollten diese hier deutlich angezeigt werden -->
		<template v-if="!belegungsfehler.isEmpty()">
			<div> <span class="icon i-ri-alert-line icon-ui-danger" /> Es {{ (belegungsfehler.size() > 1) ? 'sind' : 'ist' }} noch {{ belegungsfehler.size() }} Fehler in der Laufbahnplanung vorhanden. Bitte prüfen Sie die Laufbahn des Schüler manuell. </div>
			<!-- TODO Link zur Laufbahnplanung einbauen -->
			<!-- TODO Hier die Checkbox manuelle geprüft anbieten -->
		</template>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import { GostBelegpruefungErgebnisFehler, List } from "@core";
	import { ArrayList, GostBelegungsfehlerArt, GostHalbjahr } from "@core";

	import type { SchuelerAbiturLeistungsuebersichtProps } from "./SchuelerAbiturLeistungsuebersichtProps";

	const props = defineProps<SchuelerAbiturLeistungsuebersichtProps>();

	const belegungsfehler = computed<List<GostBelegpruefungErgebnisFehler>>(() => {
		const res = new ArrayList<GostBelegpruefungErgebnisFehler>();
		for (const fehler of props.ergebnisBelegpruefung().fehlercodes)
			if (GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.BELEGUNG
				|| GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.SCHRIFTLICHKEIT
				|| GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.SCHULSPEZIFISCH)
				res.add(fehler);
		return res;
	});

	function istQPhaseBewertet() : boolean {
		const man = props.managerLaufbahnplanung();
		return (man.istBewertet(GostHalbjahr.Q11) && man.istBewertet(GostHalbjahr.Q12)
			&& man.istBewertet(GostHalbjahr.Q21) && man.istBewertet(GostHalbjahr.Q22))
	}

</script>
