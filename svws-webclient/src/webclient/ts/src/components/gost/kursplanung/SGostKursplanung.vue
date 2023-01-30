<template>
	<div v-if="visible" class="content-card--blockungsuebersicht flex h-full content-start">
		<s-card-gost-kursansicht :jahrgangsdaten="jahrgangsdaten" :data-faecher="dataFaecher" :halbjahr="halbjahr.value"
								 :list-blockungen="listBlockungen" :blockung="blockung" :ergebnis="ergebnis"
								 :fachwahlen="fachwahlen" :list-lehrer="listLehrer" :map-lehrer="mapLehrer"
		/>
		<section class="content-card--wrapper flex gap-16" style="flex: 2 1 60%;">
			<!--rounded-xl px-4 shadow-dark-20 shadow-sm border border-dark-20 border-opacity-60-->
			<div class="w-1/4">
				<router-view name="gost_kursplanung_schueler_auswahl"/>
			</div>
			<div class="w-3/4">
				<router-view/>
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
					<s-card-gost-regelansicht v-if="!collapsed" :data-faecher="dataFaecher" :blockung="blockung" :list-schueler="listSchueler" />
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

	import { GostHalbjahr, GostJahrgang, GostStatistikFachwahl, LehrerListeEintrag, List } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, ShallowRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { DataGostKursblockungsergebnis } from "~/apps/gost/DataGostKursblockungsergebnis";
	import { ListAbiturjahrgangSchueler } from "~/apps/gost/ListAbiturjahrgangSchueler";
	import { ListKursblockungen } from "~/apps/gost/ListKursblockungen";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeGostKursplanungSchueler } from "~/router/apps/gost/kursplanung/RouteGostKursplanungSchueler";

	const props = defineProps<{
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
		dataFaecher: DataGostFaecher;
		halbjahr: ShallowRef<GostHalbjahr>;
		listBlockungen: ListKursblockungen;
		blockung: DataGostKursblockung;
		ergebnis: DataGostKursblockungsergebnis;
		listLehrer: ListLehrer;
		mapLehrer: Map<number, LehrerListeEintrag>;
		fachwahlen: List<GostStatistikFachwahl>;
	}>();

	const listSchueler: ComputedRef<ListAbiturjahrgangSchueler> = computed(() => routeGostKursplanungSchueler.data.listSchueler);

	const collapsed: Ref<boolean> = ref(true);

	const regelzahl: ComputedRef<number> = computed(() => props.blockung.datenmanager?.getRegelAnzahl() || 0);

	const allow_regeln: ComputedRef<boolean> = computed(() => props.blockung.daten?.ergebnisse.size() === 1);

	function onToggle() {
		collapsed.value = !collapsed.value;
	}

	const visible: ComputedRef<boolean> = computed(() =>
		(props.blockung.daten !== undefined) && (props.ergebnis.daten !== undefined)
	);

</script>
