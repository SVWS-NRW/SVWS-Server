<template>
	<div class="page page-flex-col pt-0">
		<div v-for="schueler in schuelerListe" :key="schueler.id" class="flex flex-col w-full h-min-fit">
			<div class="h-fit w-fit text-xl font-bold pb-2"> {{ schueler.nachname }}, {{ schueler.vorname }} </div>
			<template v-if="managerLaufbahnplanungMap().get(schueler.id) !== null">
				<!-- Darstellung der Laufbahninformationen anhand der Leistungsdaten und nicht aus dem persistierten Abiturbereich, da dieser noch nicht vorliegt. -->
				<template v-if="managerAbiturMap().get(schueler.id) === null">
					<!-- Zeige einen Hinweis, wenn noch nicht für alle Halbjahre der Qualifikationsphase Leistungsdaten vorliegen -->
					<template v-if="!(managerLaufbahnplanungMap().get(schueler.id)?.istBewertetQualifikationsPhase() ?? false)">
						<div> <span class="icon i-ri-alert-line icon-ui-danger" /> Es liegen noch nicht für alle Halbjahre der Qualifikationsphase Leistungsdaten vor. Eine Abiturberechnung ist daher noch nicht möglich. </div>
					</template>
					<template v-else>
						<div> <span class="icon i-ri-information-line" /> Es wurden bisher noch keine Leistungsdaten in den Abiturbereich übertragen. Unten sehen Sie das Ergebnis einer automatischen Berechnung für die Übertragung in den Abiturbereich. </div>
					</template>

					<!-- Wenn Belegungsfehler vorliegen, sollten diese hier deutlich angezeigt werden -->
					<template v-if="belegungsfehler.get(schueler.id) === null">
						<div> <span class="icon i-ri-alert-line icon-ui-danger" /> Für {{ schueler.vorname }} {{ schueler.nachname }} konnte keine Laufbahnprüfung durchgeführt werden. </div>
					</template>
					<template v-else-if="!(belegungsfehler.get(schueler.id)?.isEmpty() )">
						<div> <span class="icon i-ri-alert-line icon-ui-danger" /> Für {{ schueler.vorname }} {{ schueler.nachname }} {{ (belegungsfehler.size() > 1) ? 'sind' : 'ist' }} noch {{ belegungsfehler.size() }} Fehler in der Laufbahnplanung vorhanden. Bitte prüfen Sie die Laufbahn des Schüler manuell. </div>
						<!-- TODO Link zur Laufbahnplanung einbauen -->
						<!-- TODO Hier die Checkbox manuelle geprüft anbieten -->
					</template>

					<!-- Übersicht über die Fachbelegungen in der Q-Phase / Block I -->
					<div class="h-fit w-fit">
						<schueler-abitur-zulassung-tabelle :server-mode :schule :manager="() => managerLaufbahnplanungMap().get(schueler.id)!" :update-abiturpruefungsdaten="null" />
					</div>

					<div class="w-64">
						<svws-ui-button @click="copyAbiturdatenAusLeistungsdaten(managerLaufbahnplanungMap().get(schueler.id)!.daten().schuelerID)">
							Übertrage in Abiturbereich
						</svws-ui-button>
					</div>
				</template>

				<!-- Darstellung der Laufbahninformationen aus dem persistierten Abiturbereich -->
				<template v-else>
					<div><span class="icon i-ri-information-line icon-ui-danger" />Es liegen bereits Daten zum Abitur vor.</div>

					<!-- Übersicht über die Fachbelegungen in der Q-Phase / Block I -->
					<schueler-abitur-zulassung-tabelle :server-mode :schule :manager="() => managerAbiturMap().get(schueler.id)!" :update-abiturpruefungsdaten />
				</template>
			</template>
			<template v-else>
				<svws-ui-todo>
					Es konnten keine Laufbahnplanungsdaten für {{ schueler.vorname }} {{ schueler.nachname }} bestimmt werden.
				</svws-ui-todo>
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { List, GostBelegpruefungErgebnisFehler, JavaMap } from "@core";
	import { ArrayList, HashMap, GostBelegungsfehlerArt } from "@core";

	import type { GostAbiturZulassungProps } from "./GostAbiturZulassungProps";

	const props = defineProps<GostAbiturZulassungProps>();

	const belegungsfehler = computed<JavaMap<number, List<GostBelegpruefungErgebnisFehler>>>(() => {
		const result = new HashMap<number, List<GostBelegpruefungErgebnisFehler>>();
		for (const schueler of props.schuelerListe) {
			const ergebnisBelegpruefung = props.ergebnisBelegpruefungMap().get(schueler.id);
			if (ergebnisBelegpruefung === null)
				continue;
			const res = new ArrayList<GostBelegpruefungErgebnisFehler>();
			for (const fehler of ergebnisBelegpruefung.fehlercodes)
				if (GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.BELEGUNG
					|| GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.SCHRIFTLICHKEIT
					|| GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.SCHULSPEZIFISCH)
					res.add(fehler);
			result.put(schueler.id, res);
		}
		return result;
	});

</script>
