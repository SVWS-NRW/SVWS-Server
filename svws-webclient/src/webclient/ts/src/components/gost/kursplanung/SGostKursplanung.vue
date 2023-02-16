<template>
	<div class="content-card--blockungsuebersicht flex content-start">
		<s-card-gost-kursansicht :halbjahr="halbjahr" :faecher-manager="faecherManager"
			:get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager"
			:fachwahlen="fachwahlen" :map-lehrer="mapLehrer" :schueler-filter="schuelerFilter"
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
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungSchiene, GostBlockungsdatenManager, GostBlockungsergebnisManager, GostFaecherManager, GostHalbjahr, GostStatistikFachwahl, LehrerListeEintrag, List, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref } from "vue";
	import { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		patchRegel: (data: Partial<GostBlockungRegel>, id: number) => Promise<void>;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
		updateKursSchienenZuordnung: (idKurs: number, idSchieneAlt: number, idSchieneNeu: number) => Promise<boolean>;
		patchSchiene: (data: Partial<GostBlockungSchiene>, id : number) => Promise<void>;
		addSchiene: () => Promise<GostBlockungSchiene | undefined>;
		removeSchiene: (s: GostBlockungSchiene) => Promise<GostBlockungSchiene | undefined>;
		patchKurs: (data: Partial<GostBlockungKurs>, kurs_id: number) => Promise<void>;
		addKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		removeKurs: (fach_id : number, kursart_id : number) => Promise<GostBlockungKurs | undefined>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		ergebnisHochschreiben: () => Promise<void>;
		ergebnisAktivieren: () => Promise<boolean>;
		schuelerFilter: GostKursplanungSchuelerFilter | undefined;
		faecherManager: GostFaecherManager;
		halbjahr: GostHalbjahr;
		mapLehrer: Map<number, LehrerListeEintrag>;
		fachwahlen: List<GostStatistikFachwahl>;
		mapSchueler: Map<number, SchuelerListeEintrag>;
	}>();

	const collapsed: Ref<boolean> = ref(true);

	const regelzahl: ComputedRef<number> = computed(() => props.getDatenmanager().getRegelAnzahl() || 0);

	const allow_regeln: ComputedRef<boolean> = computed(() => props.getDatenmanager().getErgebnisseSortiertNachBewertung().size() === 1);

	function onToggle() {
		collapsed.value = !collapsed.value;
	}

</script>
