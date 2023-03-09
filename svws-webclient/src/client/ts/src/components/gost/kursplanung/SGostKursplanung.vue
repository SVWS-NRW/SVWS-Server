<template>
	<div v-if="hatBlockung" class="content-card--blockungsuebersicht flex content-start">
		<s-card-gost-kursansicht :config="config" :halbjahr="halbjahr" :faecher-manager="faecherManager" :hat-ergebnis="hatErgebnis"
			:get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager"
			:map-fachwahl-statistik="mapFachwahlStatistik" :map-lehrer="mapLehrer" :schueler-filter="schuelerFilter"
			:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
			:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer"
			:patch-schiene="patchSchiene" :add-schiene="addSchiene" :remove-schiene="removeSchiene"
			:remove-kurs-lehrer="removeKursLehrer" :ergebnis-aktivieren="ergebnisAktivieren" :ergebnis-hochschreiben="ergebnisHochschreiben" />
		<section class="content-card--wrapper flex gap-16" style="flex: 2 1 60%;">
			<!--rounded-xl px-4 shadow-dark-20 shadow-sm border border-dark-20 border-opacity-60-->
			<div class="w-1/4">
				<router-view name="gost_kursplanung_schueler_auswahl" />
			</div>
			<div class="w-3/4">
				<router-view />
			</div>
		</section>
		<div v-if="allow_regeln" class="app-layout--main-sidebar" :class="{ 'app-layout--main-sidebar--collapsed': collapsed }">
			<div class="app-layout--main-sidebar--container">
				<div class="app-layout--main-sidebar--trigger" @click="onToggle">
					<div class="sidebar-trigger--text">
						<svws-ui-button type="icon" class="mr-1 p-[0.1em]" v-if="!collapsed">
							<svws-ui-icon> <i-ri-close-line /> </svws-ui-icon>
						</svws-ui-button>
						<svws-ui-icon v-if="collapsed" class="mr-2"> <i-ri-equalizer-line /> </svws-ui-icon>
						<span>Regeln zur Blockung</span>
					</div>
					<div v-if="collapsed" class="app-layout--main-sidebar--trigger-count"> {{ regelzahl }} </div>
				</div>
				<div class="app-layout--main-sidebar--content">
					<s-card-gost-regelansicht v-if="!collapsed" :get-datenmanager="getDatenmanager" :faecher-manager="faecherManager" :map-schueler="mapSchueler"
						:patch-regel="patchRegel" :add-regel="addRegel" :remove-regel="removeRegel" />
				</div>
			</div>
		</div>
	</div>
	<div v-else>
		Es liegt noch keine Planung für dieses Halbjahr vor. Klicken Sie auf
		"Blockung hinzufügen", um eine neue Kursplanung zu erstellen.
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, Ref, ref } from "vue";
	import { GostKursplanungProps } from "./SGostKursplanungProps";

	const props = defineProps<GostKursplanungProps>();

	const collapsed: Ref<boolean> = ref(true);

	const regelzahl: ComputedRef<number> = computed(() => props.hatBlockung ? props.getDatenmanager().getRegelAnzahl() : 0);

	const allow_regeln: ComputedRef<boolean> = computed(() => props.hatBlockung ? props.getDatenmanager().getErgebnisseSortiertNachBewertung().size() === 1 : false);

	function onToggle() {
		collapsed.value = !collapsed.value;
	}

</script>
