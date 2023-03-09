<template>
	<div class="flex flex-col border border-blue-900 border-solid w-52">
		<div class="flex flex-row-reverse">
			<svws-ui-badge class="-m-2 z-10" v-if="konflikteTerminDragKlausur > 0 || konflikteTermin > 0" type="error" size="big"><span class="text-base">{{ konflikteTerminDragKlausur >= 0 ? konflikteTerminDragKlausur : konflikteTermin }}</span></svws-ui-badge>
		</div>
		<svws-ui-drop-data @drop="setKlausurToTermin">
			<table class="w-full">
				<thead>
					<tr>
						<th colspan="4">{{ terminHeader }}</th>
						<td class="float-right">
							<svws-ui-popover v-if="loescheKlausurtermin != undefined" :hover="false" placement="left-end" :disable-click-away="false">
								<template #trigger>
									<svws-ui-button class="action-button">
										<i-ri-more2-line />
									</svws-ui-button>
								</template>
								<template #content>
									<div class="action-items">
										<div>
											<svws-ui-button v-if="loescheKlausurtermin != undefined" class="action-item"
												type="transparent"
												@click="loescheTermin">
												Löschen
											</svws-ui-button>
										</div>
									</div>
								</template>
							</svws-ui-popover>
						</td>
					</tr>
					<!--<tr><td colspan="4" class="text-red-600">{{ dropRejectReason }}</td></tr>-->
				</thead>
				<tbody>
					<s-gost-klausurplanung-schienen-klausur v-for="klausur in klausuren" :key="klausur.id" :klausur="klausur" :faecher-manager="faecherManager" :map-lehrer="mapLehrer" :drag-status="dragStatus" />
				</tbody>
			</table>
		</svws-ui-drop-data>
	</div>
</template>

<script setup lang="ts">

	import { GostKursklausurManager, GostKursklausur, GostKlausurtermin, GostFaecherManager, LehrerListeEintrag, SchuelerListeEintrag } from "@svws-nrw/svws-core";
	import { computed } from 'vue';

	const props = defineProps<{
		termin: GostKlausurtermin | null;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapSchueler: Map<number, SchuelerListeEintrag>;
		setTerminToKursklausur: (idTermin: number | null, klausur: GostKursklausur) => Promise<boolean>;
		loescheKlausurtermin?: (termin: GostKlausurtermin) => Promise<boolean>;
		dragStatus: (klausur: GostKursklausur | null) => void;
		quartal?: number;
		dragKlausur?: GostKursklausur | null;
	}>();

	const terminHeader = computed(()=>
		props.termin === null ? "Zu verplanen:" : (props.termin.datum === null ? "Noch kein Datum" : props.termin.datum)
	);

	const klausuren = computed(() =>
		props.termin === null ? (props.quartal === undefined || props.quartal <= 0 ? props.kursklausurmanager().getKursklausurenOhneTermin() : props.kursklausurmanager().getKursklausurenOhneTermin(props.quartal)) : props.kursklausurmanager().getKursklausuren(props.termin.id)
	);

	const setKlausurToTermin = async (id: number) => {
		const klausur = props.kursklausurmanager().gibKursklausur(id)!;
		const terminNeu = props.termin !== null ? props.termin.id : null;
		if (props.termin !== null && props.termin.quartal != klausur.quartal)
			return;
		klausur.idTermin = terminNeu;
		const result = await props.setTerminToKursklausur(terminNeu, klausur);
	}

	// const dropRejectReason = computed(() => {
	// 	if (props.termin !== null && props.dragKlausur !== null && props.dragKlausur !== undefined) {
	// 		const konflikte = props.kursklausurmanager().gibKonfliktTerminKursklausur(props.termin.id, props.dragKlausur.id);
	// 		if (konflikte.size() > 0) {
	// 			let retVal = "";
	// 			(konflikte.toArray() as number[]).forEach(k => {
	// 				const s = props.mapSchueler.get(k);
	// 				retVal += `${s?.nachname} ${s?.vorname}, `;
	// 			});
	// 			return retVal;
	// 			// return "Konflikte: " + konflikte.toArray().forEach(element => {
	// 			// 	return "d"
	// 			// });
	// 		}
	// 	}
	// 	return "";
	// });

	const konflikteTerminDragKlausur = computed(() =>
		props.termin !== null && props.dragKlausur !== null ? props.kursklausurmanager().gibKonfliktTerminKursklausur(props.termin.id, props.dragKlausur!.id).size() : -1
	);

	const konflikteTermin = computed(() =>
		props.termin !== null ? props.kursklausurmanager().gibAnzahlKonflikteZuTermin(props.termin.id,) : 0
	);

	const loescheTermin = async() => {
		if (props.loescheKlausurtermin != undefined && props.termin != null)
			await props.loescheKlausurtermin(props.termin);
	};

</script>