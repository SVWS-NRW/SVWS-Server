<template>
	<svws-ui-modal v-model:show="_showModalAutomatischVerteilen" size="small">
		<template #modalTitle>
			Automatisch Verteilen
		</template>
		<template #modalContent>
			<svws-ui-notification type="info" v-if="state.manager.isSchuelerklausurenInRaumByTermin(termin, multijahrgang())">
				Existierende Klausur-Raumzuweisungen werden durch diesen Vorgang neu verteilt.
			</svws-ui-notification>
			<svws-ui-spacing :size="2" />
			<svws-ui-checkbox type="toggle" v-model="config._regel_optimiere_blocke_in_moeglichst_wenig_raeume" class="text-left">
				Verteile in möglichst wenige Räume
			</svws-ui-checkbox>
			<svws-ui-spacing :size="1" />
			<svws-ui-checkbox type="toggle" v-model="config._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume" class="text-left">
				Verteile gleichmäßig auf alle Räume
			</svws-ui-checkbox>
			<svws-ui-spacing :size="1" />
			<svws-ui-checkbox type="toggle" v-model="config._regel_forciere_selbe_klausurdauer_pro_raum" class="text-left">
				Forciere selbe Klausurdauer pro Raum
			</svws-ui-checkbox>
			<svws-ui-spacing :size="1" />
			<svws-ui-checkbox type="toggle" v-model="config._regel_forciere_selben_klausurstart_pro_raum" class="text-left">
				Forciere selbe Startzeit pro Raum
			</svws-ui-checkbox>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="_showModalAutomatischVerteilen = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="verteilen"> Verteilen <svws-ui-spinner :spinning="loading" /> </svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-modal v-model:show="showModalNichtVerteilt" size="small">
		<template #modalTitle>
			Nicht verteilt
		</template>
		<template #modalContent>
			<div>{{ nichtVerteilt }} Klausuren konnten nicht verteilt werden. Bitte erhöhen Sie die Raumkapazität oder ändern Sie die Regeln.</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="primary" @click="showModalNichtVerteilt = false"> OK </svws-ui-button>
		</template>
	</svws-ui-modal>
	<div class="grow flex flex-col gap-8 w-full overflow-hidden">
		<div class="flex flex-col gap-4 w-full">
			<div class="flex flex-col leading-tight text-headline-md">
				<span v-if="termin.datum !== null">Klausuren am {{ DateUtils.gibDatumGermanFormat(termin.datum) }}</span>
				<span v-if="termin.startzeit !== null" class="opacity-50">Startzeit: {{ DateUtils.getStringOfUhrzeitFromMinuten(termin.startzeit) }} Uhr</span>
			</div>
			<div v-if="multijahrgang()" class="flex flex-col gap-4 rounded-lg bg-ui-warning-weak px-6 py-3 min-w-120 w-full">
				<span class="leading-tight text-headline-md gap-1">
					<span v-if="(!state.zeigeAlleJahrgaenge && state.manager.isKlausurenInFremdraeumenByTermin(termin))" class="icon i-ri-alert-fill icon-ui-danger px-4" />
					<span>Jahrgangsübergreifende Planung</span>
					<span v-if="(!state.zeigeAlleJahrgaenge && state.manager.isKlausurenInFremdraeumenByTermin(termin))"> aktiviert, da jahrgangsgemischte Räume existieren</span>
				</span>
				<ul>
					<li class="flex font-bold">
						<span>{{ state.manager.anzahlBenoetigtePlaetzeAlleKlausurenByTermin(termin, false) }} Klausuren im aktuellen Jahrgang,&nbsp;</span>
						<span v-if="state.manager.isTerminAlleSchuelerklausurenVerplant(termin)" class="text-ui-success">alle zugewiesen.</span>
						<span v-else class="text-ui-danger">nicht alle zugewiesen.</span>
					</li>
					<li class="flex" v-for="terminFremd in state.manager.getFremdTermineByTermin(termin)" :key="terminFremd.id">
						<span>{{ state.manager.anzahlBenoetigtePlaetzeAlleKlausurenByTermin(terminFremd, false) }} Klausuren im Jahrgang {{ GostHalbjahr.fromIDorException(terminFremd.halbjahr).jahrgang }},&nbsp;</span>
						<span v-if="state.manager.isTerminAlleSchuelerklausurenVerplant(terminFremd)" class="text-ui-success">alle zugewiesen.</span>
						<span v-else class="text-ui-danger">nicht alle zugewiesen.</span>
						<svws-ui-button type="icon" @click="gotoTermin(terminFremd.abiturjahrgang, GostHalbjahr.fromIDorException(terminFremd.halbjahr), terminFremd.id)" title="Zur Raumplanung des Jahrgangs" size="small"><span class="icon i-ri-link" /></svws-ui-button>
					</li>
				</ul>
			</div>
			<div class="flex flex-wrap gap-2 py-1 w-full">
				<svws-ui-button :disabled="!hatKompetenzUpdate" @click="state.createKlausurraum({idTermin: termin.id})">
					<span class="icon i-ri-add-line" />
					{{ state.manager.raumGetMengeByTerminIncludingFremdtermine(termin, multijahrgang()).size() ? 'Raum hinzufügen' : 'Klausurraum anlegen' }}
				</svws-ui-button>
				<svws-ui-button type="transparent" :disabled="!hatKompetenzUpdate || !state.manager.alleRaeumeHabenStundenplanRaumByTermin(termin, multijahrgang(), false) || !state.manager.isPlatzkapazitaetAusreichendByTermin(termin, multijahrgang())" @click="showModalAutomatischVerteilen">
					<span class="icon i-ri-sparkling-line mr-1" />
					{{ state.manager.isPlatzkapazitaetAusreichendByTermin(termin, multijahrgang()) && state.manager.alleRaeumeHabenStundenplanRaumByTermin(termin, multijahrgang(), false) ? "Automatisch verteilen" : (state.manager.alleRaeumeHabenStundenplanRaumByTermin(termin, multijahrgang(), false) ? "Raumkapazität nicht ausreichend" : "Raumnummern nicht zugewiesen") }}
				</svws-ui-button>
			</div>
		</div>
		<div class="w-full overflow-hidden">
			<div class="h-full w-full grow grid gap-4 overflow-y-auto" style="grid-template-columns: repeat(auto-fill, minmax(30rem, 1fr));">
				<s-gost-klausurplanung-raumzeit-raum v-for="raum in state.manager.raumGetMengeByTerminIncludingFremdtermine(termin, state.zeigeAlleJahrgaenge || state.manager.isKlausurenInFremdraeumenByTermin(termin))"
					:key="raum.id"
					:raum
					:drag-data
					:on-drag
					:multijahrgang
					:on-drop
					:goto-termin
					:termin-selected="termin" />
			</div>
		</div>
	</div>
</template>


<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';
	import { GostKlausurraumblockungKonfiguration } from '@core/core/data/gost/klausuren/GostKlausurraumblockungKonfiguration';
	import { GostKlausurraumRich } from '@core/core/data/gost/klausuren/GostKlausurraumRich';
	import type { GostKlausurtermin } from '@core/core/data/gost/klausuren/GostKlausurtermin';
	import type { GostSchuelerklausurtermin } from '@core/core/data/gost/klausuren/GostSchuelerklausurtermin';
	import type { GostSchuelerklausurterminRich } from '@core/core/data/gost/klausuren/GostSchuelerklausurterminRich';
	import { BenutzerKompetenz } from '@core/core/types/benutzer/BenutzerKompetenz';
	import { GostHalbjahr } from '@core/core/types/gost/GostHalbjahr';
	import { KlausurraumblockungAlgorithmus } from '@core/core/utils/gost/klausuren/KlausurraumblockungAlgorithmus';
	import { ListUtils } from '@core/core/utils/ListUtils';
	import { ArrayList } from '@core/java/util/ArrayList';
	import type { List } from '@core/java/util/List';
	import { useBenutzerState } from '@ui/states/BenutzerState';
	import { useGostKlausurplanungState } from '@ui/states/GostKlausurplanungState';
	import { DateUtils } from '@core/core/utils/DateUtils';

	const props = defineProps<{
		termin: GostKlausurtermin;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (event: DragEvent, data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
		gotoTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
	}>();

	const benutzerState = useBenutzerState();
	const state = useGostKlausurplanungState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const _showModalAutomatischVerteilen = ref<boolean>(false);

	function showModalAutomatischVerteilen() {
		config._regel_forciere_selbe_klausurdauer_pro_raum = state.getConfigValue("raumblockung_regel_forciere_selbe_klausurdauer_pro_raum") === "true";
		config._regel_forciere_selben_klausurstart_pro_raum = state.getConfigValue("raumblockung_regel_forciere_selben_klausurstart_pro_raum") === "true";
		config._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume = state.getConfigValue("raumblockung_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume") === "true";
		config._regel_optimiere_blocke_in_moeglichst_wenig_raeume = state.getConfigValue("raumblockung_regel_optimiere_blocke_in_moeglichst_wenig_raeume") === "true";
		_showModalAutomatischVerteilen.value = true;
	}

	const loading = ref<boolean>(false);
	const showModalNichtVerteilt = ref<boolean>(false);

	const config = new GostKlausurraumblockungKonfiguration();

	function multijahrgang() {
		return state.zeigeAlleJahrgaenge || state.manager.isKlausurenInFremdraeumenByTermin(props.termin);
	}

	let nichtVerteilt = 0;

	function mapIDs(skts: List<GostSchuelerklausurtermin | GostSchuelerklausurterminRich>) {
		const numList = new ArrayList<number>();
		for (const skt of skts) {
			numList.add(skt.id);
		}
		return numList;
	}

	async function verteilen() {
		loading.value = true;
		config._regel_forciere_selbe_kursklausur_im_selben_raum = true;
		state.setConfigValue("raumblockung_regel_forciere_selbe_klausurdauer_pro_raum", config._regel_forciere_selbe_klausurdauer_pro_raum ? "true" : "false").catch(() => {});
		state.setConfigValue("raumblockung_regel_forciere_selben_klausurstart_pro_raum", config._regel_forciere_selben_klausurstart_pro_raum ? "true" : "false").catch(() => {});
		state.setConfigValue("raumblockung_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume", config._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume ? "true" : "false").catch(() => {});
		state.setConfigValue("raumblockung_regel_optimiere_blocke_in_moeglichst_wenig_raeume", config._regel_optimiere_blocke_in_moeglichst_wenig_raeume ? "true" : "false").catch(() => {});
		config.schuelerklausurtermine = state.manager.enrichSchuelerklausurtermine(state.manager.schuelerklausurterminaktuellGetMengeByTerminIncludingFremdtermine(props.termin, multijahrgang()));
		config.raeume = state.manager.enrichKlausurraeume(state.manager.raumGetMengeByTerminIncludingFremdtermine(props.termin, multijahrgang()));
		const algo = new KlausurraumblockungAlgorithmus();
		const raumAlleSkts = new GostKlausurraumRich();
		raumAlleSkts.idsSchuelerklausurtermine = mapIDs(config.schuelerklausurtermine);
		algo.berechne(config);
		nichtVerteilt = config.schuelerklausurtermine.size();
		await state.setzeRaumZuSchuelerklausuren(ListUtils.create1(raumAlleSkts), true);
		await state.setzeRaumZuSchuelerklausuren(config.raeume, false);
		loading.value = false;
		if (nichtVerteilt > 0) {
			showModalNichtVerteilt.value = true;
		}
		_showModalAutomatischVerteilen.value = false;
	}

</script>
