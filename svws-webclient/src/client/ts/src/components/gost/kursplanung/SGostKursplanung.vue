<template>
	<div class="page--content page--content--full">
		<div v-if="hatBlockung" class="content-card--blockungsuebersicht flex h-full gap-x-8 gap-y-16">
			<s-card-gost-kursansicht :config="config" :halbjahr="halbjahr" :faecher-manager="faecherManager" :hat-ergebnis="hatErgebnis"
				:get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager"
				:map-fachwahl-statistik="mapFachwahlStatistik" :map-lehrer="mapLehrer" :schueler-filter="schuelerFilter"
				:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
				:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer"
				:patch-schiene="patchSchiene" :add-schiene="addSchiene" :remove-schiene="removeSchiene"
				:remove-kurs-lehrer="removeKursLehrer" :ergebnis-aktivieren="ergebnisAktivieren" :ergebnis-hochschreiben="ergebnisHochschreiben"
				:add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :combine-kurs="combineKurs" :split-kurs="splitKurs">
				<template #triggerRegeln>
					<svws-ui-button @click="onToggle" :disabled="!allow_regeln" size="small" type="secondary">
						<i-ri-settings3-line />
						<span class="pr-1">Regeln zur Blockung</span>
						<template #badge v-if="regelzahl">
							{{ regelzahl }}
						</template>
					</svws-ui-button>
				</template>
			</s-card-gost-kursansicht>
			<section class="content-card flex gap-8 min-w-[50%] h-full">
				<div class="min-w-[21rem] w-[21rem] h-full">
					<router-view name="gost_kursplanung_schueler_auswahl" />
				</div>
				<div class="flex-grow">
					<router-view />
				</div>
			</section>
			<Teleport to="body">
				<aside class="app-layout--aside max-w-2xl h-auto" v-if="allow_regeln && !collapsed" :class="{ 'app-layout--aside--collapsed': collapsed }">
					<div class="app-layout--aside-container relative h-auto max-h-full">
						<h2 class="text-headline-md flex justify-between pl-5 pt-4">
							<span class="text-headline">Regeln zur Blockung</span>
							<svws-ui-button type="transparent" @click="onToggle">
								Schließen
								<i-ri-close-line />
							</svws-ui-button>
						</h2>
						<s-card-gost-regelansicht :get-datenmanager="getDatenmanager" :faecher-manager="faecherManager" :map-schueler="mapSchueler"
							:patch-regel="patchRegel" :add-regel="addRegel" :remove-regel="removeRegel" />
					</div>
				</aside>
			</Teleport>
		</div>
		<div v-else>
			<h3 class="text-headline-md mb-2"><i-ri-information-line class="inline align-text-top" /> Es liegt noch keine Planung für dieses Halbjahr vor.</h3>
			<p>Klicken Sie auf „Neue Blockung“, um eine neue Kursplanung zu erstellen oder auf „Wiederherstellen“, um aus bestehenden Leistungsdaten ein Blockung zu restaurieren.</p>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef, Ref} from "vue";
	import { computed, ref } from "vue";
	import type { GostKursplanungProps } from "./SGostKursplanungProps";

	const props = defineProps<GostKursplanungProps>();

	const collapsed: Ref<boolean> = ref(true);

	const regelzahl: ComputedRef<number> = computed(() => props.hatBlockung ? props.getDatenmanager().getRegelAnzahl() : 0);

	const allow_regeln: ComputedRef<boolean> = computed(() => props.hatBlockung ? props.getDatenmanager().getErgebnisseSortiertNachBewertung().size() === 1 : false);

	function onToggle() {
		collapsed.value = !collapsed.value;
	}

</script>
