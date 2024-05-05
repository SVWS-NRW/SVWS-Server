<template>
	<svws-ui-content-card>
		<template #title>
			<div class="flex flex-col leading-tight text-headline-md">
				<span v-if="termin.datum !== null">Klausuren am {{ DateUtils.gibDatumGermanFormat(termin.datum) }}</span>
				<span v-if="termin.startzeit !== null" class="opacity-50">Startzeit: {{ DateUtils.getStringOfUhrzeitFromMinuten(termin.startzeit) }} Uhr</span>
				<!--<span>{{ kMan().schueleridsGetMengeByTerminid(termin.id)?.size() }} Klausurschreiber:innen</span>-->
			</div>
		</template>
		<svws-ui-content-card v-if="zeigeAlleJahrgaenge() || raummanager().isKlausurenInFremdraeumen()" :has-background="true" :title="'Jahrgangsübergreifende Planung' + (!zeigeAlleJahrgaenge() && raummanager().isKlausurenInFremdraeumen() ? ' aktiviert, da jahrgangsgemischte Räume existieren.' : '')">
			<ul>
				<li class="flex" v-for="pair in raummanager().getFremdTermine()" :key="pair.a.id">
					<span>{{ pair.b!.size() }} Klausuren im Jahrgang {{ GostHalbjahr.fromIDorException(pair.a.halbjahr).jahrgang }},&nbsp;</span>
					<span v-if="raummanager().isTerminAlleSchuelerklausurenVerplant(pair.a)" class="text-green-500">alle zugewiesen.</span>
					<span v-else class="text-red-500">nicht alle zugewiesen.</span>
					<svws-ui-button type="icon" @click="RouteManager.doRoute(routeGostKlausurplanungRaumzeit.getRoute(pair.a.abijahr, pair.a.halbjahr, pair.a.id ))" :title="`Zur Raumplanung des Jahrgangs`" size="small"><span class="icon i-ri-link" /></svws-ui-button>
				</li>
			</ul>
		</svws-ui-content-card>
		<div class="flex flex-wrap gap-1 my-5 py-1 w-full">
			<svws-ui-button @click="createKlausurraum({idTermin: termin.id}, raummanager())"><span class="icon i-ri-add-line -ml-1" /> {{ raummanager()?.raumGetMengeAsList().size() ? 'Raum hinzufügen' : 'Klausurraum anlegen' }}</svws-ui-button>
		</div>
		<div class="grid grid-cols-[repeat(auto-fill,minmax(26rem,1fr))] gap-4">
			<!--<template v-if="raummanager().raumGetMengeTerminOnlyAsList(!zeigeAlleJahrgaenge() || !raummanager().isKlausurenInFremdraeumen()).size()">-->
			<s-gost-klausurplanung-raumzeit-raum v-for="raum in raummanager().raumGetMengeTerminOnlyAsList(!zeigeAlleJahrgaenge() && !raummanager().isKlausurenInFremdraeumen())"
				:key="raum.id"
				:stundenplanmanager="stundenplanmanager"
				:raum="raum"
				:raummanager="raummanager"
				:patch-klausurraum="patchKlausurraum"
				:loesche-klausurraum="loescheKlausurraum"
				:patch-klausur="patchKlausur"
				:k-man="kMan"
				:drag-data="dragData"
				:on-drag="onDrag"
				:on-drop="onDrop" />
			<!-- </template>
			<template v-else>
				<div class="shadow-inner rounded-lg h-48" />
			</template> -->
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { GostKursklausurManager, GostKlausurtermin, StundenplanManager, GostKlausurraumManager, GostKursklausur, GostKlausurraum, GostKlausurenCollectionSkrsKrs } from '@core';
	import { DateUtils, GostHalbjahr } from '@core';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';
	import { RouteManager } from '~/router/RouteManager';
	import { routeGostKlausurplanungRaumzeit } from '~/router/apps/gost/klausurplanung/RouteGostKlausurplanungRaumzeit';

	const props = defineProps<{
		termin: GostKlausurtermin;
		kMan: () => GostKursklausurManager;
		stundenplanmanager: StundenplanManager;
		raummanager: () => GostKlausurraumManager;
		createKlausurraum: (raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<void>;
		loescheKlausurraum: (id: number, manager: GostKlausurraumManager) => Promise<boolean>;
		patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<boolean>;
		patchKlausur: (klausur: GostKursklausur, patch: Partial<GostKursklausur>) => Promise<GostKlausurenCollectionSkrsKrs>;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
		zeigeAlleJahrgaenge: () => boolean;
	}>();

</script>
