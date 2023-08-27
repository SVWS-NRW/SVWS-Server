<template>
	<div class="page--content page--content--full page--content--gost-grid" :class="{'svws-blockungstabelle-hidden': !blockungstabelleVisible}">
		<template v-if="hatBlockung">
			<s-card-gost-kursansicht :config="config" :halbjahr="halbjahr" :faecher-manager="faecherManager" :hat-ergebnis="hatErgebnis"
				:jahrgangsdaten="jahrgangsdaten"
				:get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager"
				:map-fachwahl-statistik="mapFachwahlStatistik" :map-lehrer="mapLehrer" :schueler-filter="schuelerFilter"
				:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
				:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer"
				:patch-schiene="patchSchiene" :add-schiene="addSchiene" :remove-schiene="removeSchiene"
				:remove-kurs-lehrer="removeKursLehrer" :ergebnis-aktivieren="ergebnisAktivieren" :existiert-schuljahresabschnitt="existiertSchuljahresabschnitt"
				:ergebnis-hochschreiben="ergebnisHochschreiben"
				:toggle-blockungstabelle="toggleBlockungstabelle"
				:blockungstabelle-visible="blockungstabelleVisible"
				:add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :combine-kurs="combineKurs" :split-kurs="splitKurs">
				<template #triggerRegeln>
					<svws-ui-button @click="onToggle" size="small" type="transparent" :disabled="regelzahl < 1 && getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() > 1">
						<i-ri-settings3-line />
						<span class="pr-1">Regeln zur Blockung</span>
						<template #badge v-if="regelzahl"> {{ regelzahl }} </template>
					</svws-ui-button>
				</template>
			</s-card-gost-kursansicht>
			<router-view name="gost_kursplanung_schueler_auswahl" />
			<router-view />
			<Teleport to="body">
				<aside class="app-layout--aside max-w-2xl h-auto" v-if="!collapsed" :class="{ 'app-layout--aside--collapsed': collapsed }">
					<div class="app-layout--aside-container relative h-auto max-h-full">
						<h2 class="text-headline-md flex justify-between pl-5 pt-4">
							<span class="text-headline">Regeln zur Blockung</span>
							<svws-ui-button type="transparent" @click="onToggle"> Schließen <i-ri-close-line /> </svws-ui-button>
						</h2>
						<s-card-gost-regelansicht :get-datenmanager="getDatenmanager" :faecher-manager="faecherManager" :map-schueler="mapSchueler"
							:patch-regel="patchRegel" :add-regel="addRegel" :remove-regel="removeRegel" />
					</div>
				</aside>
			</Teleport>
		</template>
		<div v-else>
			<h3 class="text-headline-md mb-2"><i-ri-information-line class="inline align-text-top" /> Es liegt noch keine Planung für dieses Halbjahr vor.</h3>
			<p>Klicken Sie auf „Neue Blockung“, um eine neue Kursplanung zu erstellen oder auf „Wiederherstellen“, um aus bestehenden Leistungsdaten ein Blockung zu restaurieren.</p>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { GostKursplanungProps } from "./SGostKursplanungProps";
	import { computed, ref, onMounted } from "vue";

	const props = defineProps<GostKursplanungProps>();

	const collapsed = ref<boolean>(true);

	const regelzahl = computed<number>(() => props.hatBlockung ? props.getDatenmanager().regelGetAnzahl() : 0);

	function onToggle() {
		collapsed.value = !collapsed.value;
	}

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

	const blockungstabelleVisible = ref(true);

	const toggleBlockungstabelle = () => {
		blockungstabelleVisible.value = !blockungstabelleVisible.value;
	}

</script>

<style lang="postcss" scoped>
.page--content {
  @apply grid overflow-y-hidden overflow-x-auto h-full pb-8;
  grid-auto-rows: 100%;
  grid-template-columns: minmax(min-content, 1.5fr) minmax(18rem, 0.4fr) 1fr;
  grid-auto-columns: max-content;

  &.svws-blockungstabelle-hidden {
    grid-template-columns: 0 minmax(20rem, 0.15fr) 1fr;

    .s-gost-kursplanung-schueler-auswahl {
      @apply -ml-8 lg:-ml-16;
    }
  }
}
</style>
