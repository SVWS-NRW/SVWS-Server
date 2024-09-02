<template>
	<svws-ui-modal :show="showModalAutomatischVerteilen" size="small">
		<template #modalTitle>
			Automatisch Verteilen
		</template>
		<template #modalContent>
			<svws-ui-notification type="info" v-if="kMan().isSchuelerklausurenInRaumByTermin(termin, multijahrgang())">
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
			<svws-ui-button type="secondary" @click="showModalAutomatischVerteilen().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="verteilen"> Verteilen <svws-ui-spinner :spinning="loading" /> </svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-modal :show="showModalNichtVerteilt" size="small">
		<template #modalTitle>
			Nicht verteilt
		</template>
		<template #modalContent>
			<div>{{ nichtVerteilt }} Klausuren konnten nicht verteilt werden. Bitte erhöhen Sie die Raumkapazität oder ändern Sie die Regeln.</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="primary" @click="showModalNichtVerteilt().value = false"> OK </svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-content-card>
		<template #title>
			<div class="flex flex-col leading-tight text-headline-md">
				<span v-if="termin.datum !== null">Klausuren am {{ DateUtils.gibDatumGermanFormat(termin.datum) }}</span>
				<span v-if="termin.startzeit !== null" class="opacity-50">Startzeit: {{ DateUtils.getStringOfUhrzeitFromMinuten(termin.startzeit) }} Uhr</span>
				<!--<span>{{ kMan().schueleridsGetMengeByTerminid(termin.id)?.size() }} Klausurschreiber:innen</span>-->
			</div>
		</template>
		<svws-ui-content-card class="rounded-lg" v-if="multijahrgang()" :has-background="true">
			<template #title>
				<span class="leading-tight text-headline-md gap-1">
					<span v-if="(!zeigeAlleJahrgaenge() && kMan().isKlausurenInFremdraeumenByTermin(termin))" class="icon i-ri-alert-fill icon-error px-4" />
					<span>Jahrgangsübergreifende Planung</span>
					<span v-if="(!zeigeAlleJahrgaenge() && kMan().isKlausurenInFremdraeumenByTermin(termin))"> aktiviert, da jahrgangsgemischte Räume existieren</span>
				</span>
			</template>
			<ul>
				<li class="flex font-bold">
					<span>{{ kMan().anzahlBenoetigtePlaetzeAlleKlausurenByTermin(termin, false) }} Klausuren im akutellen Jahrgang,&nbsp;</span>
					<span v-if="kMan().isTerminAlleSchuelerklausurenVerplant(termin)" class="text-green-500">alle zugewiesen.</span>
					<span v-else class="text-red-500">nicht alle zugewiesen.</span>
				</li>
				<li class="flex" v-for="terminFremd in kMan().getFremdTermineByTermin(termin)" :key="terminFremd.id">
					<span>{{ kMan().anzahlBenoetigtePlaetzeAlleKlausurenByTermin(terminFremd, false) }} Klausuren im Jahrgang {{ GostHalbjahr.fromIDorException(terminFremd.halbjahr).jahrgang }},&nbsp;</span>
					<span v-if="kMan().isTerminAlleSchuelerklausurenVerplant(terminFremd)" class="text-green-500">alle zugewiesen.</span>
					<span v-else class="text-red-500">nicht alle zugewiesen.</span>
					<svws-ui-button type="icon" @click="RouteManager.doRoute(routeGostKlausurplanungRaumzeit.getRoute(terminFremd.abijahr, terminFremd.halbjahr, terminFremd.id ))" :title="`Zur Raumplanung des Jahrgangs`" size="small"><span class="icon i-ri-link" /></svws-ui-button>
				</li>
			</ul>
		</svws-ui-content-card>
		<div class="flex flex-wrap gap-1 my-5 py-1 w-full">
			<svws-ui-button @click="createKlausurraum({idTermin: termin.id})"><span class="icon i-ri-add-line -ml-1" /> {{ kMan().raumGetMengeByTerminIncludingFremdtermine(termin, multijahrgang()).size() ? 'Raum hinzufügen' : 'Klausurraum anlegen' }}</svws-ui-button>
			<svws-ui-button type="transparent" :disabled="!kMan().alleRaeumeHabenStundenplanRaumByTermin(termin, multijahrgang(), false) || !kMan().isPlatzkapazitaetAusreichendByTermin(termin, multijahrgang())" @click="showModalAutomatischVerteilen().value = true"><span class="icon i-ri-sparkling-line -ml-1 mr-1" />{{ kMan().isPlatzkapazitaetAusreichendByTermin(termin, multijahrgang()) && kMan().alleRaeumeHabenStundenplanRaumByTermin(termin, multijahrgang(), false) ? "Automatisch verteilen" : (kMan().alleRaeumeHabenStundenplanRaumByTermin(termin, multijahrgang(), false) ? "Raumkapazität nicht ausreichend" : "Raumnummern nicht zugewiesen") }} </svws-ui-button>
		</div>
		<div class="grid grid-cols-[repeat(auto-fill,minmax(26rem,1fr))] gap-4">
			<!--<template v-if="raummanager().raumGetMengeTerminOnlyAsList(!zeigeAlleJahrgaenge() || !raummanager().isKlausurenInFremdraeumen()).size()">-->
			<s-gost-klausurplanung-raumzeit-raum v-for="raum in kMan().raumGetMengeByTerminIncludingFremdtermine(termin, zeigeAlleJahrgaenge() || kMan().isKlausurenInFremdraeumenByTermin(termin))"
				:key="raum.id"
				:raum
				:patch-klausurraum
				:loesche-klausurraum
				:patch-klausur
				:k-man
				:drag-data
				:on-drag
				:multijahrgang
				:on-drop
				:termin-selected="termin" />
			<!-- </template>
			<template v-else>
				<div class="shadow-inner rounded-lg h-48" />
			</template> -->
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { GostKlausurplanManager, GostKlausurtermin, GostKursklausur, GostKlausurraum, GostKlausurenCollectionSkrsKrsData, List, GostSchuelerklausurTermin, GostSchuelerklausurTerminRich } from '@core';
	import { GostKlausurraumRich, ListUtils, ArrayList } from '@core';
	import { DateUtils, GostHalbjahr, GostKlausurraumblockungKonfiguration, KlausurraumblockungAlgorithmus } from '@core';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';
	import { RouteManager } from '~/router/RouteManager';
	import { routeGostKlausurplanungRaumzeit } from '~/router/apps/gost/klausurplanung/RouteGostKlausurplanungRaumzeit';
	import { ref } from 'vue';

	const props = defineProps<{
		termin: GostKlausurtermin;
		kMan: () => GostKlausurplanManager;
		createKlausurraum: (raum: Partial<GostKlausurraum>) => Promise<void>;
		loescheKlausurraum: (id: number) => Promise<boolean>;
		patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>) => Promise<boolean>;
		patchKlausur: (klausur: GostKursklausur, patch: Partial<GostKursklausur>) => Promise<GostKlausurenCollectionSkrsKrsData>;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
		zeigeAlleJahrgaenge: () => boolean;
		setzeRaumZuSchuelerklausuren: (raeume: List<GostKlausurraumRich>, deleteFromRaeume: boolean) => Promise<GostKlausurenCollectionSkrsKrsData>;
		getConfigValue: (value: string) => string;
		setConfigValue: (key: string, value: string) => void;
	}>();

	const _showModalAutomatischVerteilen = ref<boolean>(false);
	const showModalAutomatischVerteilen = () => {
		config._regel_forciere_selbe_klausurdauer_pro_raum = props.getConfigValue("raumblockung_regel_forciere_selbe_klausurdauer_pro_raum") === "true";
		config._regel_forciere_selben_klausurstart_pro_raum = props.getConfigValue("raumblockung_regel_forciere_selben_klausurstart_pro_raum") === "true";
		config._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume = props.getConfigValue("raumblockung_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume") === "true";
		config._regel_optimiere_blocke_in_moeglichst_wenig_raeume = props.getConfigValue("raumblockung_regel_optimiere_blocke_in_moeglichst_wenig_raeume") === "true";
		return _showModalAutomatischVerteilen;
	}
	const loading = ref<boolean>(false);
	const _showModalNichtVerteilt = ref<boolean>(false);
	const showModalNichtVerteilt = () => _showModalNichtVerteilt;

	const config = new GostKlausurraumblockungKonfiguration();

	const multijahrgang = () => props.zeigeAlleJahrgaenge() || props.kMan().isKlausurenInFremdraeumenByTermin(props.termin);
	let nichtVerteilt = 0;

	function mapIDs(skts: List<GostSchuelerklausurTermin | GostSchuelerklausurTerminRich>) {
		const numList = new ArrayList<number>();
		for (const skt of skts)
			numList.add(skt.id);
		return numList;
	}

	const verteilen = async () => {
		loading.value = true;
		config._regel_forciere_selbe_kursklausur_im_selben_raum = true;
		props.setConfigValue("raumblockung_regel_forciere_selbe_klausurdauer_pro_raum", config._regel_forciere_selbe_klausurdauer_pro_raum ? "true" : "false");
		props.setConfigValue("raumblockung_regel_forciere_selben_klausurstart_pro_raum", config._regel_forciere_selben_klausurstart_pro_raum ? "true" : "false");
		props.setConfigValue("raumblockung_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume", config._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume ? "true" : "false");
		props.setConfigValue("raumblockung_regel_optimiere_blocke_in_moeglichst_wenig_raeume", config._regel_optimiere_blocke_in_moeglichst_wenig_raeume ? "true" : "false");
		config.schuelerklausurtermine = props.kMan().enrichSchuelerklausurtermine(props.kMan().schuelerklausurterminGetMengeByTerminIncludingFremdtermine(props.termin, multijahrgang()));
		config.raeume = props.kMan().enrichKlausurraeume(props.kMan().raumGetMengeByTerminIncludingFremdtermine(props.termin, multijahrgang()));
		const algo = new KlausurraumblockungAlgorithmus();
		const raumAlleSkts = new GostKlausurraumRich();
		raumAlleSkts.schuelerklausurterminIDs = mapIDs(config.schuelerklausurtermine);
		algo.berechne(config);
		nichtVerteilt = config.schuelerklausurtermine.size();
		await props.setzeRaumZuSchuelerklausuren(ListUtils.create1(raumAlleSkts), true);
		await props.setzeRaumZuSchuelerklausuren(config.raeume, false);
		loading.value = false;
		if (nichtVerteilt > 0)
			showModalNichtVerteilt().value = true;
		showModalAutomatischVerteilen().value = false;
	}

</script>

<style lang="postcss" scoped>

.svws-ui-tab-content {
	@apply overflow-y-hidden items-start;

	.page--content {
		@apply h-full py-0 auto-rows-auto;

		.content-card {
			@apply max-h-full pt-8 pb-8 px-4 -mx-4 overflow-y-auto h-[unset];
			scrollbar-gutter: stable;
		}
	}
}
</style>
