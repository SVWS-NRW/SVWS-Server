<template>
	<svws-ui-content-card>
		<template #title>
			<div class="flex flex-col leading-tight text-headline-md">
				<span v-if="termin.datum !== null">Klausuren am {{ DateUtils.gibDatumGermanFormat(termin.datum) }}</span>
				<span v-if="termin.startzeit !== null" class="opacity-50">Startzeit: {{ DateUtils.getStringOfUhrzeitFromMinuten(termin.startzeit) }} Uhr</span>
				<!--<span>{{ kursklausurmanager().schueleridsGetMengeByTerminid(termin.id)?.size() }} Klausurschreiber:innen</span>-->
			</div>
		</template>
		<div class="flex flex-wrap gap-1 my-5 py-1 w-full">
			<svws-ui-button @click="createKlausurraum({idTermin: termin.id}, raummanager())"><i-ri-add-line class="-ml-1" /> {{ raummanager()?.raumGetMengeAsList().size() ? 'Raum hinzufügen' : 'Klausurraum anlegen' }}</svws-ui-button>
		</div>
		<div class="grid grid-cols-[repeat(auto-fill,minmax(26rem,1fr))] gap-4">
			<template v-if="raummanager()?.raumGetMengeAsList().size()">
				<s-gost-klausurplanung-raumzeit-raum v-for="raum in raummanager()?.raumGetMengeAsList()"
					:key="raum.id"
					:stundenplanmanager="stundenplanmanager"
					:raum="raum"
					:termin-startzeit="termin.startzeit !== null ? DateUtils.getStringOfUhrzeitFromMinuten(termin.startzeit) : undefined"
					:raummanager="raummanager"
					:patch-klausurraum="patchKlausurraum"
					:loesche-klausurraum="loescheKlausurraum"
					:patch-klausur="patchKlausur"
					:faecher-manager="faecherManager"
					:kursklausurmanager="kursklausurmanager"
					:kursmanager="kursmanager"
					:map-lehrer="mapLehrer"
					:drag-data="dragData"
					:on-drag="onDrag"
					:on-drop="onDrop" />
			</template>
			<template v-else>
				<div class="shadow-inner rounded-lg h-48" />
			</template>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { GostKursklausurManager, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, KursManager, StundenplanManager, GostKlausurraumManager, GostKursklausur, GostSchuelerklausur , GostKlausurraum, GostKlausurenCollectionSkrsKrs} from '@core';
	import { DateUtils} from '@core';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';

	const props = defineProps<{
		termin: GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		stundenplanmanager: StundenplanManager;
		raummanager: () => GostKlausurraumManager;
		createKlausurraum: (raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<void>;
		loescheKlausurraum: (id: number, manager: GostKlausurraumManager) => Promise<boolean>;
		patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<boolean>;
		patchKlausur: (klausur: GostKursklausur, patch: Partial<GostKursklausur>) => Promise<GostKlausurenCollectionSkrsKrs>;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;

	}>();

</script>
