<template>
	<div class="flex flex-col border border-blue-900 border-solid w-52">
		<svws-ui-drop-data @drop="setKlausurToTermin" class="h-full">
			<div class="flex flex-row-reverse">
				<svws-ui-badge class="-m-2 z-10"
					v-if="(termin === null || dragKlausur === undefined || dragKlausur === null || termin?.quartal === dragKlausur?.quartal) && (konflikteTerminDragKlausur > 0 || konflikteTermin > 0)"
					type="error"
					size="big">
					<span class="text-base">{{ konflikteTerminDragKlausur >= 0 ? konflikteTerminDragKlausur : konflikteTermin }}</span>
				</svws-ui-badge>
			</div>
			<table class="w-full">
				<thead>
					<tr>
						<th colspan="5">{{ terminHeader }}</th>
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
					<tr v-if="termin !== null">
						<th colspan="6"><svws-ui-text-input placeholder="Terminbezeichnung" :model-value="termin.bezeichnung" @update:model-value="patchKlausurtermin !== undefined ? patchKlausurtermin({ bezeichnung: String($event) }, termin!.id) : null" /></th>
					</tr>
					<!--<tr><td colspan="4" class="text-red-600">{{ dropRejectReason }}</td></tr>-->
				</thead>
				<tbody>
					<s-gost-klausurplanung-schienen-klausur v-for="klausur in klausuren"
						:key="klausur.id"
						:kursklausurmanager="kursklausurmanager"
						:klausur="klausur"
						:termin="termin"
						:alle-termine="alleTermine"
						:faecher-manager="faecherManager"
						:map-lehrer="mapLehrer"
						:drag-status="dragStatus" />
				</tbody>
			</table>
		</svws-ui-drop-data>
	</div>
</template>

<script setup lang="ts">

	import type { GostKursklausurManager, GostKursklausur, GostKlausurtermin, GostFaecherManager, LehrerListeEintrag, SchuelerListeEintrag, List } from "@svws-nrw/svws-core";
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
		patchKlausurtermin?: (termin: Partial<GostKlausurtermin>, id: number) => Promise<boolean>;
		quartal?: number;
		alleTermine: List<GostKlausurtermin>;
		dragKlausur?: GostKursklausur | null;
	}>();

	const terminHeader = computed(()=>
		props.termin === null ? "Zu verplanen:" : (props.termin.datum === null ? "Noch kein Datum" : new Date(props.termin.datum).toLocaleString("de-DE").split(",")[0])
	);

	const klausuren = computed(() =>
		props.termin === null ? (props.quartal === undefined || props.quartal <= 0 ? props.kursklausurmanager().getKursklausurenOhneTermin() : props.kursklausurmanager().getKursklausurenOhneTermin(props.quartal)) : props.kursklausurmanager().getKursklausuren(props.termin.id)
	);

	const setKlausurToTermin = async (pKlausur: GostKursklausur) => {
		const klausur = props.kursklausurmanager().gibKursklausur(pKlausur.id)!;
		const terminNeu = props.termin !== null ? props.termin.id : null;
		if (props.termin !== null && props.termin.quartal != klausur.quartal)
			return;
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
		props.termin !== null ? props.kursklausurmanager().gibAnzahlKonflikteZuTermin(props.termin.id) : 0
	);

	const loescheTermin = async() => {
		if (props.loescheKlausurtermin != undefined && props.termin != null)
			await props.loescheKlausurtermin(props.termin);
	};

	const updateTermin = async() => {
		console.log("Hallo");
	}

</script>