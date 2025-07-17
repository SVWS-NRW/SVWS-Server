<template>
	<div class="page page-flex-col gap-4">
		<!-- Darstellung der Laufbahninformationen anhand der Leistungsdaten und nicht aus dem persistierten Abiturbereich, da dieser noch nicht vorliegt. -->
		<template v-if="managerAbitur() === null">
			<!-- Zeige einen Hinweis, wenn noch nicht für alle Halbjahre der Qualifikationsphase Leistungsdaten vorliegen -->
			<div class="text-lg font-bold">
				Leistungsübersicht als Vorschau auf die Abitur-Zulassung
			</div>
			<template v-if="!istQPhaseBewertet()">
				<div class="ml-8 flex flex-row">
					<span class="icon i-ri-alert-line icon-ui-danger mr-2 mt-1" />
					<div>
						<div>Es liegen noch nicht für alle Halbjahre der Qualifikationsphase Leistungsdaten vor.</div>
						<div>Eine Abiturberechnung ist daher noch nicht möglich.</div>
					</div>
				</div>
			</template>
			<template v-else>
				<div class="ml-8 flex flex-row">
					<span class="icon i-ri-information-line mr-2 mt-1" />
					<div>
						<div>Es wurden bisher noch keine Leistungsdaten in den Abiturbereich übertragen.</div>
						<div>Drücken Sie den Knopf, um die Leistungsdaten in den Abiturbereich zu übertragen.</div>
						<div>Unten sehen Sie eine Vorschau der zu übertragenden Daten.</div>
					</div>
				</div>
				<div class="w-64">
					<svws-ui-button @click="copyAbiturdatenAusLeistungsdaten(managerLaufbahnplanung().daten().schuelerID)">
						Übertragen der Leistungsdaten
					</svws-ui-button>
				</div>
			</template>

			<!-- Wenn Belegungsfehler vorliegen, sollten diese hier deutlich angezeigt werden -->
			<template v-if="!belegungsfehler.isEmpty()">
				<div> <span class="icon i-ri-alert-line icon-ui-danger" /> Es {{ (belegungsfehler.size() > 1) ? 'sind' : 'ist' }} noch {{ belegungsfehler.size() }} Fehler in der Laufbahnplanung vorhanden. Bitte prüfen Sie die Laufbahn des Schüler manuell. </div>
				<!-- TODO Link zur Laufbahnplanung einbauen -->
				<!-- TODO Hier die Checkbox manuell geprüft anbieten -->
			</template>

			<!-- Übersicht über die Fachbelegungen in der Q-Phase / Block I -->
			<div class="min-w-fit">
				<schueler-abitur-zulassung-tabelle :server-mode :schule :manager="managerLaufbahnplanung" :update-abiturpruefungsdaten="null" />
			</div>
			<div class="flex flex-col" v-if="!managerLaufbahnplanung().getErgebnisMarkierpruefung().erfolgreich">
				<div class="font-bold">Hinweise:</div>
				<template v-for="logentry in managerLaufbahnplanung().getErgebnisMarkierpruefung().log" :key="logentry">
					{{ logentry }}
				</template>
			</div>
		</template>

		<!-- Darstellung der Laufbahninformationen aus dem persistierten Abiturbereich -->
		<template v-else>
			<div class="text-lg font-bold">Abitur-Zulassung</div>

			<!-- Übersicht über die Fachbelegungen in der Q-Phase / Block I aus dem Abiturbereich -->
			<div class="flex flex-row gap-4">
				<div class="min-w-fit">
					<schueler-abitur-zulassung-tabelle :server-mode :schule :manager="() => managerAbitur()!" :update-abiturpruefungsdaten />
				</div>
				<div class="flex flex-col gap-4">
					<div v-if="!managerAbitur()!.getErgebnisMarkierpruefung().erfolgreich">
						<div class="font-bold">Hinweise:</div>
						<template v-for="logentry in managerAbitur()!.getErgebnisMarkierpruefung().log" :key="logentry">
							{{ logentry }}
						</template>
					</div>

					<div v-if="serverMode === ServerMode.DEV" class="bg-ui-warning">
						<div class="flex flex-col gap-4 bg-ui m-1 p-2">
							<div class="text-lg font-bold">Entwickler-Ansicht (dev-Mode): Vergleich mit den Leistungsdaten</div>

							<div class="ml-4 w-64">
								<svws-ui-button @click="copyMarkierungsergebnisToClipboard">Kopiere Markierungsergebnis</svws-ui-button>
							</div>

							<!-- Übersicht über die Fachbelegungen in der Q-Phase / Block I -->
							<div class="flex flex-row">
								<div class="min-w-fit">
									<schueler-abitur-zulassung-tabelle :server-mode :schule :manager="managerLaufbahnplanung" :update-abiturpruefungsdaten="null" />
								</div>
								<div class="flex flex-col" v-if="!managerLaufbahnplanung().getErgebnisMarkierpruefung().erfolgreich">
									<div class="font-bold">Hinweise:</div>
									<template v-for="logentry in managerLaufbahnplanung().getErgebnisMarkierpruefung().log" :key="logentry">
										{{ logentry }}
									</template>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
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
