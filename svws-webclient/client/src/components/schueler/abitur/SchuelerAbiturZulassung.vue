<template>
	<div class="page page-flex-col pt-0">
		<!-- Darstellung der Laufbahninformationen anhand der Leistungsdaten und nicht aus dem persistierten Abiturbereich, da dieser noch nicht vorliegt. -->
		<template v-if="managerAbitur() === null">
			<!-- Zeige einen Hinweis, wenn noch nicht für alle Halbjahre der Qualifikationsphase Leistungsdaten vorliegen -->
			<template v-if="!istQPhaseBewertet()">
				<div> <span class="icon i-ri-alert-line icon-ui-danger" /> Es liegen noch nicht für alle Halbjahre der Qualifikationsphase Leistungsdaten vor. Eine Abiturberechnung ist daher noch nicht möglich. </div>
			</template>
			<template v-else>
				<div> <span class="icon i-ri-information-line" /> Es wurden bisher noch keine Leistungsdaten in den Abiturbereich übertragen. Unten sehen Sie das Ergebnis einer automatischen Berechnung für die Übertragung in den Abiturbereich. </div>
			</template>

			<!-- Wenn Belegungsfehler vorliegen, sollten diese hier deutlich angezeigt werden -->
			<template v-if="!belegungsfehler.isEmpty()">
				<div> <span class="icon i-ri-alert-line icon-ui-danger" /> Es {{ (belegungsfehler.size() > 1) ? 'sind' : 'ist' }} noch {{ belegungsfehler.size() }} Fehler in der Laufbahnplanung vorhanden. Bitte prüfen Sie die Laufbahn des Schüler manuell. </div>
				<!-- TODO Link zur Laufbahnplanung einbauen -->
				<!-- TODO Hier die Checkbox manuelle geprüft anbieten -->
			</template>

			<!-- Übersicht über die Fachbelegungen in der Q-Phase / Block I -->
			<div class="h-fit w-fit">
				<schueler-abitur-zulassung-tabelle :server-mode :schule :manager="managerLaufbahnplanung" :update-abiturpruefungsdaten="null" />
			</div>

			<div class="w-64">
				<svws-ui-button @click="copyAbiturdatenAusLeistungsdaten(managerLaufbahnplanung().daten().schuelerID)">
					Übertrage in Abiturbereich
				</svws-ui-button>
			</div>
		</template>

		<!-- Darstellung der Laufbahninformationen aus dem persistierten Abiturbereich -->
		<template v-else>
			<div><span class="icon i-ri-information-line icon-ui-danger" />Es liegen bereits Daten zum Abitur vor.</div>

			<div v-if="serverMode === ServerMode.DEV" class="w-64">
				<svws-ui-button @click="copyMarkierungsergebnisToClipboard">Kopiere Markierungsergebnis</svws-ui-button>
			</div>

			<!-- Übersicht über die Fachbelegungen in der Q-Phase / Block I -->
			<schueler-abitur-zulassung-tabelle :server-mode :schule :manager="managerLaufbahnplanung" :update-abiturpruefungsdaten="null" />

			<!-- Übersicht über die Fachbelegungen in der Q-Phase / Block I -->
			<schueler-abitur-zulassung-tabelle :server-mode :schule :manager="() => managerAbitur()!" :update-abiturpruefungsdaten />
		</template>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { GostBelegpruefungErgebnisFehler, List } from "@core";
	import { ArrayList, GostAbiturMarkierungsalgorithmusErgebnis, GostBelegungsfehlerArt, GostHalbjahr, ServerMode } from "@core";

	import type { SchuelerAbiturZulassungProps } from "./SchuelerAbiturZulassungProps";

	const props = defineProps<SchuelerAbiturZulassungProps>();

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

	async function copyMarkierungsergebnisToClipboard() {
		try {
			const ergebnis = props.managerLaufbahnplanung().getErgebnisMarkierungsalgorithmus();
			const json = GostAbiturMarkierungsalgorithmusErgebnis.transpilerToJSON(ergebnis);
			await navigator.clipboard.writeText(JSON.stringify(JSON.parse(json), null, 2));
		} catch(e) {
			// do nothing
		}
	}

</script>
