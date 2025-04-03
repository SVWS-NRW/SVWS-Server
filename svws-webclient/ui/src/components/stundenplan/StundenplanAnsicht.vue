<template>
	<div v-if="manager().getListZeitraster().size()" class="svws-ui-stundenplan"
		:class="`${hideZeitachse ? 'svws-ohne-zeitachse' : 'svws-hat-zeitachse'} svws-zeitraster-${zeitrasterSteps}${hatSchnittPausenzeitenZeitraster ? ' svws-spalte-pausenzeit' : ''}`">
		<!-- Die Überschriften des Stundenplan -->
		<div class="svws-ui-stundenplan--head">
			<span class="icon i-ri-time-line svws-time-icon print:!hidden" v-if="!hideZeitachse" />
			<!-- Das Feld links in der Überschrift beinhaltet den ausgewählten Wochentyp -->
			<div class="inline-flex gap-1 items-center justify-center print:!pl-2 print:!justify-start" :class="{'opacity-50 print:!invisible': wochentyp() === 0}">
				{{ manager().stundenplanGetWochenTypAsString(wochentyp()) }}
			</div>
			<!-- Daneben werden die einzelnen Wochentage des Stundenplans angezeigt -->
			<template v-for="wochentag in wochentagRange" :key="wochentag.id">
				<div class="font-bold text-center inline-flex items-center w-full justify-center"> {{ wochentag.beschreibung }} </div>
				<div v-if="hatSchnittPausenzeitenZeitraster" class="inline-flex items-center justify-center"> <span class="icon i-ri-cup-line" /> </div>
			</template>
		</div>
		<!-- Die Daten des Stundenplans -->
		<div class="svws-ui-stundenplan--body" :style="{'--zeitrasterRows': zeitrasterRows}">
			<!-- Die Zeitachse des Stundenplans auf der linken Seite -->
			<div class="svws-ui-stundenplan--zeitraster svws-zeitachse" v-if="!hideZeitachse">
				<!--TODO: Zeitraster dynamisch mit zeitrasterSteps darstellen-->
				<template v-for="n in zeitrasterRows" :key="n">
					<span v-if="n % 3 === 2" class="svws-ui-stundenplan--einheit" :class="{'svws-extended': n % 4 === 2, 'svws-small': n % 4 === 1 || n % 4 === 3}" :style="`grid-row: ${ n-1 } / ${n+2}; grid-column: 1`">
						<template v-if="n % 4 === 2">
							{{ Math.floor((beginn + (n * 5)) / 60) }}:00
						</template>
					</span>
				</template>
			</div>
			<!-- Zeige auf der linken Seite die Zeitraster- und Pausenzeiten-Einträge an der Zeitachse -->
			<div class="svws-ui-stundenplan--zeitraster svws-ui-stundenplan--zeitangaben">
				<!-- Die Zeitraster-Einträge -->
				<div v-for="stunde in zeitrasterRange" :key="stunde" class="svws-ui-stundenplan--stunde text-center justify-center" :style="getZeitrasterGridPos(undefined, stunde)">
					<div class="text-headline-sm"> {{ stunde }}. Stunde </div>
					<div v-for="zeiten in manager().unterrichtsstundeGetUhrzeitenAsStrings(stunde)" :key="zeiten" class="font-bold text-sm">
						{{ zeiten.replace(' Uhr', '') }}
					</div>
				</div>
				<!-- Die Pausenzeiten -->
				<template v-if="!hideZeitachse && !hideZeitachsePausenzeiten && (modePausenaufsichten !== 'aus')">
					<!--TODO: Pausenzeiten, wenn Zeitachse deaktiviert ist-->
					<template v-for="pause in getPausenzeiten()" :key="pause">
						<div class="svws-ui-stundenplan--pause text-sm text-center justify-center" :style="posPause(pause.id)">
							<div> {{ pause.bezeichnung }} </div>
							<div> {{ (pause.ende! - pause.beginn!) }} Minuten </div>
						</div>
					</template>
				</template>
			</div>
			<!-- Zeige die Unterrichte und Pausenaufsichten des Stundenplans -->
			<template v-for="wochentag in wochentagRange" :key="wochentag.id">
				<div class="svws-ui-stundenplan--zeitraster">
					<!-- Darstellung des Unterrichtes in dem Zeitraster -->
					<template v-for="stunde in zeitrasterRange" :key="stunde">
						<div class="svws-ui-stundenplan--stunde relative" :style="getZeitrasterGridPos(wochentag.id, stunde)"
							@dragover="checkDropZoneZeitraster($event, wochentag.id, stunde)" @dragleave="onDragLeaveInternal($event, wochentag.id, stunde)"
							@drop="onDropInternal(manager().zeitrasterGetByWochentagAndStundeOrNull(wochentag.id, stunde) ?? undefined, dragOverPos.wochentyp)">
							<!-- Unterstütze mehrere Drop-Bereich, um direkt den einzelnen Wochentypen zuweisen zu können ... -->
							<div v-if="(draggedData !== undefined) && ((hatWochentypen) || (!hatWochentypen && isZeitrasterDropZone.getOrException(wochentag.id, stunde, 0)))"
								class="absolute pointer-events-none w-[calc(100%-0.5rem)] h-[calc(100%-0.5rem)] flex flex-col gap-1 z-10 text-center select-none rounded-lg"
								:class="isDragOverPosition(wochentag, stunde) ? ['bg-ui-contrast-10', 'opacity-50']:['opacity-0']">
								<div class="grow flex justify-center items-center p-2 border-2 border-solid rounded-lg border-ui-contrast-75 hover:font-bold"
									:class="{
										'bg-ui-success text-ui-onsuccess opacity-50': (dragOverPos.wochentyp === 0) && isZeitrasterDropZone.getOrException(wochentag.id, stunde, 0),
										'opacity-0': !isZeitrasterDropZone.getOrException(wochentag.id, stunde, 0)
									}">
									Jede Woche
								</div>
								<div v-if="hatWochentypen" class="h-[calc(50%-0.25rem)] flex flex-row gap-1">
									<template v-for="wt, wtIndex in manager().getWochenTypModell()" :key="wtIndex">
										<div class="grow flex justify-center items-center p-2 border-2 border-solid rounded-lg border-ui-contrast-75 hover:font-bold"
											:class="{
												'bg-ui-success text-ui-onsuccess opacity-50': (wtIndex + 1 === dragOverPos.wochentyp) && isZeitrasterDropZone.getOrException(wochentag.id, stunde, wtIndex + 1),
												'opacity-0': !isZeitrasterDropZone.getOrException(wochentag.id, stunde, wtIndex + 1)
											}">
											<span class="w-20">{{ manager().stundenplanGetWochenTypAsString(wtIndex + 1) }}</span>
										</div>
									</template>
								</div>
							</div>
							<!-- Passe die Darstellung je nach ausgewähltem Wochentyp an... -->
							<!-- Allgemeiner Wochentyp ausgewählt -->
							<!-- zunächst die Darstellung des allgemeinen Unterrichtes -->
							<template v-if="!showSchienen">
								<!-- Diese Ansicht hat keine Anzeige der Schienen (Schüler, Lehrer) -->
								<div v-for="unterricht in getUnterrichte(wochentag.id, stunde, 0, null)" :key="unterricht.id"
									class="svws-ui-stundenplan--unterricht"
									:class="{ 'cursor-grab': draggable, 'grow': growUnterricht, 'border-ui-contrast-100 bg-ui-danger text-ui-ondanger border-dashed': isDraggedType(unterricht) }"
									:style="isDraggedType(unterricht) ? '' : `background-color: ${getBgColor(manager().fachGetByIdOrException(unterricht.idFach).kuerzelStatistik)}; color: var(--color-ui-static)`"
									:draggable @dragstart="onDrag(unterricht)" @dragend="onDrag(undefined)" @click="emit('update:click', unterricht)">
									<slot name="unterricht" :unterricht />
								</div>
							</template>
							<template v-else v-for="schiene in [{id: -1}, ...getSchienenListe(wochentag.id, stunde, 0)]" :key="schiene.id">
								<div :class="{'bg-ui-contrast-10 rounded-md pl-1 pr-1 pb-1 mt-1': schiene.id > -1}">
									<div v-if="'bezeichnung' in schiene" class="col-span-full text-sm font-bold pt-1 pb-2 print:!mb-0 flex place-items-center group ml-2.5" :class="{'cursor-grab': draggable}"
										:draggable @dragstart.stop="onDrag(getUnterrichte(wochentag.id, stunde, 0, schiene.id), $event)" @dragend.stop="onDrag(undefined)">
										<span v-if="draggable" class="icon i-ri-draggable inline-block icon-ui-contrast-75 -ml-1 opacity-60 group-hover:opacity-10 group-hover:icon-ui-contrast-75" />
										<span>{{ schiene.bezeichnung }}</span>
									</div>
									<div v-for="unterricht in getUnterrichte(wochentag.id, stunde, 0, schiene.id)" :key="unterricht.id"
										class="svws-ui-stundenplan--unterricht"
										:class="{ 'cursor-grab': draggable, 'grow': growUnterricht, 'border-ui-contrast-100 bg-ui-danger text-ui-ondanger border-dashed ': isDraggedType(unterricht) }"
										:style="isDraggedType(unterricht) ? '' : `background-color: ${getBgColor(manager().fachGetByIdOrException(unterricht.idFach).kuerzelStatistik)}; color: var(--color-ui-static)`"
										:draggable @dragstart.stop="onDrag(unterricht)" @dragend.stop="onDrag(undefined)" @click="emit('update:click', unterricht)">
										<slot name="unterricht" :unterricht="unterricht" />
									</div>
								</div>
							</template>
							<!-- dann die Darstellung des speziellen Unterrichtes der Wochentypen -->
							<div v-if="hatZeitrasterUnterrichtMitWochentyp.getOrNull(wochentag.id, stunde) ?? false" class="svws-multiple gap-1">
								<template v-for="wt in getWochentyp" :key="wt">
									<div :class="{'border-r border-ui-contrast-25 p-1 last:border-r-0 flex flex-col': wochentyp()}" :style="wochentyp() ? `grid-column-start: ${wt}`: ''">
										<template v-if="!showSchienen">
											<!-- Diese Ansicht hat keine Anzeige der Schienen (Schüler, Lehrer) -->
											<template v-if="getUnterrichte(wochentag.id, stunde, wt, null).size() > 0">
												<div class="col-span-full text-sm font-bold text-center mb-1 py-1 print:!mb-0"> {{ manager().stundenplanGetWochenTypAsString(wt) }}</div>
											</template>
											<div v-for="unterricht in getUnterrichte(wochentag.id, stunde, wt, null)" :key="unterricht.id"
												class="svws-ui-stundenplan--unterricht"
												:class="{'cursor-grab': draggable, 'grow': growUnterricht, 'svws-compact': !wochentyp(), 'border-ui-contrast-100 bg-ui-danger text-ui-ondanger border-dashed': isDraggedType(unterricht) }"
												:style="isDraggedType(unterricht) ? '' : `background-color: ${getBgColor(manager().fachGetByIdOrException(unterricht.idFach).kuerzelStatistik)}; color: var(--color-ui-static)`"
												:draggable @dragstart="onDrag(unterricht)" @dragend="onDrag(undefined)" @click="emit('update:click', unterricht)">
												<slot name="unterricht" :unterricht />
											</div>
										</template>
										<template v-else v-for="schiene in [{id: -1}, ...getSchienenListe(wochentag.id, stunde, wt)]" :key="schiene.id">
											<template v-if="getUnterrichte(wochentag.id, stunde, wt, schiene.id).size() > 0">
												<div class="col-span-full text-sm font-bold text-center mb-1 py-1 print:!mb-0"> {{ manager().stundenplanGetWochenTypAsString(wt) }}</div>
											</template>
											<div :class="{'bg-ui rounded-md pl-1 pr-1 pb-1 pt-0': schiene.id > -1}">
												<div v-if="'bezeichnung' in schiene" class="col-span-full text-sm font-bold text-center pt-1 pb-2 print:!mb-0" :class="{'cursor-grab': draggable}"
													:draggable @dragstart.stop="onDrag(getUnterrichte(wochentag.id, stunde, wt, schiene.id), $event)" @dragend.stop="onDrag(undefined)">
													{{ schiene.bezeichnung }}
												</div>
												<div v-for="unterricht in getUnterrichte(wochentag.id, stunde, wt, schiene.id)" :key="unterricht.id"
													class="svws-ui-stundenplan--unterricht"
													:class="{ 'cursor-grab': draggable, 'grow': growUnterricht, 'svws-compact': !wochentyp(), 'border-ui-contrast-100 bg-ui-danger text-ui-ondanger border-dashed': isDraggedType(unterricht) } "
													:style="isDraggedType(unterricht) ? '' : `background-color: ${getBgColor(manager().fachGetByIdOrException(unterricht.idFach).kuerzelStatistik)}; color: var(--color-ui-static)`"
													:draggable @dragstart.stop="onDrag(unterricht)" @dragend.stop="onDrag(undefined)" @click="emit('update:click', unterricht)">
													<slot name="unterricht" :unterricht="unterricht" />
												</div>
											</div>
										</template>
									</div>
								</template>
							</div>
						</div>
					</template>
					<!-- Darstellung der Pausenzeiten und der zugehörigen Aufsichten -->
					<template v-if="!hideZeitachse && !hatSchnittPausenzeitenZeitraster && (modePausenaufsichten !== 'aus')">
						<stundenplan-ansicht-pausenaufsichten :mode="modePausenaufsichten" :kompakt="false" :manager :wochentag="wochentag.id" :get-pausenzeiten-liste-by-wochentag
							:text-pausenzeit :pos-pause :get-pausenaufsichten :hide-pausenaufsicht :draggable :on-drag :on-drop="onDropInternal" :check-drop-zone-pausenzeit />
					</template>
				</div>
				<div v-if="hatSchnittPausenzeitenZeitraster" class="svws-ui-stundenplan--zeitraster">
					<stundenplan-ansicht-pausenaufsichten :mode="'tooltip'" :kompakt="true" :manager :wochentag="wochentag.id" :get-pausenzeiten-liste-by-wochentag
						:text-pausenzeit :pos-pause :get-pausenaufsichten :hide-pausenaufsicht :draggable :on-drag :on-drop="onDropInternal" :check-drop-zone-pausenzeit />
				</div>
			</template>
		</div>
	</div>
	<div v-else class="svws-ui-stundenplan">Es wurden noch keine Zeitraster für diesen Stundenplan angelegt.</div>
</template>

<script setup lang="ts">

	import { computed, shallowRef } from "vue";
	import type { StundenplanAnsichtDragData, StundenplanAnsichtDropZone, StundenplanAnsichtProps } from "./StundenplanAnsichtProps";
	import type { Wochentag } from "../../../../core/src/core/types/Wochentag";
	import type { List } from "../../../../core/src/java/util/List";
	import type { StundenplanKlassenunterricht } from "../../../../core/src/core/data/stundenplan/StundenplanKlassenunterricht";
	import { cast_de_svws_nrw_core_data_stundenplan_StundenplanKlassenunterricht } from "../../../../core/src/core/data/stundenplan/StundenplanKlassenunterricht";
	import { StundenplanPausenaufsicht } from "../../../../core/src/core/data/stundenplan/StundenplanPausenaufsicht";
	import type { StundenplanUnterricht } from "../../../../core/src/core/data/stundenplan/StundenplanUnterricht";
	import { cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterricht } from "../../../../core/src/core/data/stundenplan/StundenplanUnterricht";
	import { StundenplanZeitraster } from "../../../../core/src/core/data/stundenplan/StundenplanZeitraster";
	import type { StundenplanSchiene } from "../../../../core/src/core/data/stundenplan/StundenplanSchiene";
	import { cast_de_svws_nrw_core_data_stundenplan_StundenplanSchiene } from "../../../../core/src/core/data/stundenplan/StundenplanSchiene";
	import type { StundenplanKurs } from "../../../../core/src/core/data/stundenplan/StundenplanKurs";
	import { cast_de_svws_nrw_core_data_stundenplan_StundenplanKurs } from "../../../../core/src/core/data/stundenplan/StundenplanKurs";
	import { Fach } from "../../../../core/src/asd/types/fach/Fach";
	import { ArrayList, cast_java_util_ArrayList } from "../../../../core/src/java/util/ArrayList";
	import { HashMap } from "../../../../core/src/java/util/HashMap";
	import { HashMap2D } from "../../../../core/src/core/adt/map/HashMap2D";
	import { HashMap3D } from "../../../../core/src/core/adt/map/HashMap3D";
	import { HashMap4D } from "../../../../core/src/core/adt/map/HashMap4D";
	import type { StundenplanPausenzeit } from "../../../../core/src/core/data/stundenplan/StundenplanPausenzeit";

	const props = withDefaults(defineProps<StundenplanAnsichtProps>(), {
		showSchienen: false,
		hidePausenaufsicht: false,
		textPausenzeit: undefined,
		growUnterricht: false,
		modePausenaufsichten: 'normal',
		hideZeitachse: false,
		hideZeitachsePausenzeiten: false,
		zeitrasterSteps: 5,
		ignoreEmpty: false,
		useDragAndDrop: false,
		dragData: () => undefined,
		onDrag: (data: StundenplanAnsichtDragData, event?: DragEvent) => {},
		onDrop: (zone: StundenplanAnsichtDropZone, wochentyp?: number) => {},
	});

	defineSlots();

	const emit = defineEmits<{
		"update:click": [value: StundenplanUnterricht];
	}>();

	const dragOverPos = shallowRef<{
		wochentag : number | undefined,
		stunde : number | undefined,
		wochentyp : number | undefined,
	}>({
		wochentag: undefined,
		stunde: undefined,
		wochentyp: undefined,
	});

	const mapUnterrichteBySchiene = computed<HashMap4D<number, number, number, number, List<StundenplanUnterricht>>>(() => {
		const result = new HashMap4D<number, number, number, number, List<StundenplanUnterricht>>();
		for (const wochentag of wochentagRange.value) {
			for (const stunde of zeitrasterRange.value) {
				for (let wt = 0; wt <= props.manager().getWochenTypModell(); wt++) {
					result.put(wochentag.id, stunde, wt, -1, props.getUnterricht(wochentag.id, stunde, wt, -1));
					for (const schiene of getSchienenListe(wochentag.id, stunde, wt))
						result.put(wochentag.id, stunde, wt, schiene.id, props.getUnterricht(wochentag.id, stunde, wt, schiene.id));
				}
			}
		}
		return result;
	});

	const mapUnterrichte = computed<HashMap3D<number, number, number, List<StundenplanUnterricht>>>(() => {
		const result = new HashMap3D<number, number, number, List<StundenplanUnterricht>>();
		for (const wochentag of wochentagRange.value)
			for (const stunde of zeitrasterRange.value)
				for (let wt = 0; wt <= props.manager().getWochenTypModell(); wt++)
					result.put(wochentag.id, stunde, wt, props.getUnterricht(wochentag.id, stunde, wt, null));
		return result;
	});

	function getUnterrichte(wochentag: number, stunde: number, wochentyp: number, schiene: number | null) : List<StundenplanUnterricht> {
		const result = (schiene === null)
			? mapUnterrichte.value.getOrNull(wochentag, stunde, wochentyp)
			: mapUnterrichteBySchiene.value.getOrNull(wochentag, stunde, wochentyp, schiene);
		return (result === null) ? new ArrayList<StundenplanUnterricht>() : result;
	}

	const mapSchienen = computed<HashMap3D<number, number, number, List<StundenplanSchiene>>>(() => {
		const result = new HashMap3D<number, number, number, List<StundenplanSchiene>>();
		for (const wochentag of wochentagRange.value)
			for (const stunde of zeitrasterRange.value)
				for (let wt = 0; wt <= props.manager().getWochenTypModell(); wt++)
					result.put(wochentag.id, stunde, wt, props.getSchienen(wochentag.id, stunde, wt));
		return result;
	});

	function getSchienenListe(wochentag: number, stunde: number, wochentyp: number): List<StundenplanSchiene> {
		const result = mapSchienen.value.getOrNull(wochentag, stunde, wochentyp);
		return (result === null) ? new ArrayList<StundenplanSchiene>() : result;
	}

	const hatZeitrasterUnterrichtMitWochentyp = computed<HashMap2D<number, number, boolean>>(() => {
		const result = new HashMap2D<number, number, boolean>();
		for (const wochentag of wochentagRange.value)
			for (const stunde of zeitrasterRange.value)
				result.put(wochentag.id, stunde, props.zeitrasterHatUnterrichtMitWochentyp(wochentag.id, stunde));
		return result;
	});

	const mapPausenzeitenListeByWochentag = computed<HashMap<number, List<StundenplanPausenzeit>>>(() => {
		const result = new HashMap<number, List<StundenplanPausenzeit>>();
		for (const wochentag of wochentagRange.value)
			result.put(wochentag.id, props.getPausenzeitenWochentag(wochentag.id));
		return result;
	});

	function getPausenzeitenListeByWochentag(wochentag: number): List<StundenplanPausenzeit> {
		const result = mapPausenzeitenListeByWochentag.value.get(wochentag);
		return (result === null) ? new ArrayList<StundenplanPausenzeit>() : result;
	}

	const mapPausenaufsichtenByPausenzeitId = computed<HashMap<number, List<StundenplanPausenaufsicht>>>(() => {
		const result = new HashMap<number, List<StundenplanPausenaufsicht>>();
		for (const wochentag of wochentagRange.value)
			for (const pausenzeit of getPausenzeitenListeByWochentag(wochentag.id))
				result.put(pausenzeit.id, props.getPausenaufsichtenPausenzeit(pausenzeit.id));
		return result;
	});

	function getPausenaufsichten(idPausenzeit: number): List<StundenplanPausenaufsicht> {
		const result = mapPausenaufsichtenByPausenzeitId.value.get(idPausenzeit);
		return (result === null) ? new ArrayList<StundenplanPausenaufsicht>() : result;
	}

	const draggedData = computed<StundenplanAnsichtDragData>(() => props.dragData());

	const isZeitrasterDropZone = computed<HashMap3D<number, number, number, boolean>>(() => {
		const result = new HashMap3D<number, number, number, boolean>();
		for (const wochentag of wochentagRange.value) {
			for (const stunde of zeitrasterRange.value) {
				for (let wt = 0; wt <= props.manager().getWochenTypModell(); wt++) {
					result.put(wochentag.id, stunde, wt, isDropZoneZeitraster(wochentag.id, stunde, wt));
				}
			}
		}
		return result;
	});


	function onDragLeaveInternal(event: DragEvent, wochentag: number, stunde: number) {
		const container = event.currentTarget instanceof HTMLDivElement ? event.currentTarget : null;
		if (container === null)
			return;
		const rect = container.getBoundingClientRect();
		const mouseRelX = (event.clientX - rect.x) / rect.width;
		const mouseRelY = (event.clientY - rect.y) / rect.height;
		if ((mouseRelX < 0) || (mouseRelX > 1) || (mouseRelY < 0) || (mouseRelY > 1))
			resetDragOverPosition();
	}

	function resetDragOverPosition() {
		dragOverPos.value = { wochentag: undefined, stunde: undefined, wochentyp: undefined };
	}

	function updateDragOverPosition(event: DragEvent, wochentag : number, stunde : number, wochentyp : number | undefined) {
		if ((dragOverPos.value.wochentag !== wochentag) || (dragOverPos.value.stunde !== stunde) || (dragOverPos.value.wochentyp !== wochentyp))
			dragOverPos.value = { wochentag, stunde, wochentyp };
	}

	function isDragOverPosition(wochentag : Wochentag, stunde : number) : boolean {
		return (dragOverPos.value.wochentag === wochentag.id) && (dragOverPos.value.stunde === stunde);
	}

	const getWochentyp = computed(() => props.wochentyp() === 0 ? props.manager().getWochenTypModell() : [props.wochentyp()])

	const hatWochentypen = computed<boolean>(() => (props.manager().getWochenTypModell() > 0));

	const beginn = computed(() => {
		if (props.ignoreEmpty)
			return props.manager().pausenzeitUndZeitrasterGetMinutenMinOhneLeere();
		return props.manager().pausenzeitUndZeitrasterGetMinutenMin();
	});

	const ende = computed(() => {
		if (props.ignoreEmpty)
			return props.manager().pausenzeitUndZeitrasterGetMinutenMaxOhneLeere();
		return props.manager().pausenzeitUndZeitrasterGetMinutenMax();
	});

	const wochentagRange = computed(() => {
		const z = props.manager().zeitrasterGetWochentageAlsEnumRange();
		const p = props.manager().pausenzeitGetWochentageAlsEnumRange();
		return z.length > p.length ? z : p;
	});

	const zeitrasterRange = computed(() => {
		if (props.ignoreEmpty)
			return props.manager().zeitrasterGetStundenRangeOhneLeere();
		return props.manager().zeitrasterGetStundenRange();
	});

	const gesamtzeit = computed(() => {
		const tmp = ende.value - beginn.value;
		return tmp <= 0 ? 360 : tmp;
	});

	const zeitrasterRows = computed(() => {
		if (props.hideZeitachse) {
			// TODO: Pausenzeiten, wenn Zeitachse deaktiviert ist
			// props.manager().pausenzeitGetMengeByWochentagOrEmptyList(1).size() || 0
			return zeitrasterRange.value.length;
		}

		// Für alle x Minuten eine Grid Row (zeitrasterSteps)
		return Math.round(gesamtzeit.value / props.zeitrasterSteps);
	});

	const hatSchnittPausenzeitenZeitraster = computed<boolean>(() => {
		for (const wochentag of wochentagRange.value)
			if (props.schneidenPausenzeitenZeitraster(wochentag.id))
				return true;
		return false;
	});

	const mapZeitrasterGridPos = computed<HashMap2D<number, number, string>>(() => {
		const result = new HashMap2D<number, number, string>();
		for (const wochentag of wochentagRange.value)
			for (const stunde of zeitrasterRange.value)
				result.put(wochentag.id, stunde, posZeitraster(wochentag.id, stunde));
		return result;
	});

	const mapZeitrasterStundeGridPos = computed<HashMap<number, string>>(() => {
		const result = new HashMap<number, string>();
		for (const stunde of zeitrasterRange.value)
			result.put(stunde, posZeitraster(undefined, stunde));
		return result;
	});

	function getZeitrasterGridPos(wochentag: number | undefined, stunde: number) : string {
		const result = (wochentag === undefined)
			? mapZeitrasterStundeGridPos.value.get(stunde)
			: mapZeitrasterGridPos.value.getOrNull(wochentag, stunde);
		return (result === null) ? "" : result;
	}

	function posZeitraster(wochentag: number | undefined, stunde: number): string {
		let zbeginn = props.manager().zeitrasterGetMinutenMinDerStunde(stunde);
		let zende = props.manager().zeitrasterGetMinutenMaxDerStunde(stunde);
		if (wochentag !== undefined) {
			const z = props.manager().zeitrasterGetByWochentagAndStundeOrNull(wochentag, stunde);
			if (z !== null) {
				if (z.stundenbeginn !== null)
					zbeginn = z.stundenbeginn;
				if (z.stundenende !== null)
					zende = z.stundenende;
			}
		}
		const rowStart = props.hideZeitachse ? (stunde -1) : (zbeginn - beginn.value) / props.zeitrasterSteps;
		const rowEnd = props.hideZeitachse ? stunde : (zende - beginn.value) / props.zeitrasterSteps;

		return "grid-row-start: " + (Math.round(rowStart) + 1) + "; grid-row-end: " + (Math.round(rowEnd) + 1) + "; grid-column: 1;";
	}

	const mapPauseGridPos = computed<HashMap<number, string>>(() => {
		const result = new HashMap<number, string>();
		for (const pausenzeit of props.getPausenzeiten())
			result.put(pausenzeit.id, posPauseInternal(pausenzeit.id));
		return result;
	});

	function posPause(idPausenzeit: number): string {
		const result = mapPauseGridPos.value.get(idPausenzeit);
		return (result === null) ? "" : result;
	}

	function posPauseInternal(idPausenzeit: number): string {
		const pzeit = props.manager().pausenzeitGetByIdOrException(idPausenzeit);
		let rowStart = 0;
		let rowEnd = 10;
		if ((pzeit.beginn !== null) && (pzeit.ende !== null)) {
			rowStart = (pzeit.beginn - beginn.value) / props.zeitrasterSteps;
			rowEnd = (pzeit.ende - beginn.value) / props.zeitrasterSteps;
		}
		return "grid-row-start: " + (Math.round(rowStart) + 1) + "; grid-row-end: " + (Math.round(rowEnd) + 1) + "; grid-column: 1;";
	}

	function getBgColor(fach: string): string {
		return Fach.getBySchluesselOrDefault(fach).getHMTLFarbeRGB(props.manager().getSchuljahr());
	}

	const draggable = computed<boolean>(() => props.useDragAndDrop);

	function onDropInternal(zone: StundenplanAnsichtDropZone, wochentyp?: number) {
		resetDragOverPosition();
		props.onDrop(zone, wochentyp);
	}

	/**
	 * Prüfe, ob der Kurs bereits in einem Unterricht bei dem Zeitraster existiert. Ist dies der
	 * Fall, so wird geprüft, ob dieser bei dem angegebenen Wochentyp noch eingefügt werden darf.
	 *
	 * @param kurs        der Kurs
	 * @param wochentag   der Wochentag für das Zeitraster-Element
	 * @param stunde      die Stunde für das Zeitraster-Element
	 * @param wt          der zu prüfende Wochentyp
	 */
	function isDropZoneZeitrasterKurs(kurs: StundenplanKurs, wochentag: number, stunde: number, wt : number) : boolean {
		// Prüfe, ob der Kurs in einem der Unterrichte vorkommt. In diesem Fall ist ein Drop hier nicht erlaubt
		for (let w = 0; w < props.manager().getWochenTypModell() + 1; w++) {
			if (hatWochentypen.value && (wt !== 0) && ((w !== 0) && (w !== wt)))
				continue;
			for (const unterricht of getUnterrichte(wochentag, stunde, w, null))
				if (unterricht.idKurs === kurs.id)
					return false;
		}
		return true;
	}

	/**
	 * Prüfe, ob der Klassenunterricht bereits in einem Unterricht bei dem Zeitraster existiert. Ist dies der
	 * Fall, so wird geprüft, ob dieser bei dem angegebenen Wochentyp noch eingefügt werden darf.
	 *
	 * @param kurs        der Kurs
	 * @param wochentag   der Wochentag für das Zeitraster-Element
	 * @param stunde      die Stunde für das Zeitraster-Element
	 * @param wt          der zu prüfende Wochentyp
	 */
	function isDropZoneZeitrasterKlassenunterricht(klassenunterricht: StundenplanKlassenunterricht, wochentag: number, stunde: number, wt : number) : boolean {
		// Prüfe, ob der Klassenunterricht in einem der Unterrichte vorkommt. In diesem Fall ist ein Drop hier nicht erlaubt
		for (let w = 0; w < props.manager().getWochenTypModell() + 1; w++) {
			if (hatWochentypen.value && (wt !== 0) && ((w !== 0) && (w !== wt)))
				continue;
			for (const unterricht of getUnterrichte(wochentag, stunde, w, null)) {
				if (unterricht.idKurs !== null)
					continue;
				for (const idKlasse of unterricht.klassen) {
					const ku = props.manager().klassenunterrichtGetByKlasseIdAndFachIdOrException(idKlasse, unterricht.idFach);
					if ((klassenunterricht.idKlasse === ku.idKlasse) && (klassenunterricht.idFach === ku.idFach))
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * Prüfe, ob die Schiene bei einem der Kurse bereits in einem Unterricht bei dem Zeitraster existiert.
	 * Ist dies der Fall, so wird geprüft, ob dieser bei dem angegebenen Wochentyp noch eingefügt werden darf.
	 *
	 * @param schiene     die Schiene
	 * @param wochentag   der Wochentag für das Zeitraster-Element
	 * @param stunde      die Stunde für das Zeitraster-Element
	 * @param wt          der zu prüfende Wochentyp
	 */
	function isDropZoneZeitrasterSchiene(schiene: StundenplanSchiene, wochentag: number, stunde: number, wt : number) : boolean {
		// Prüfe, ob die Schiene in einem der Unterrichte vorkommt. In diesem Fall ist ein Drop hier nicht erlaubt
		for (let w = 0; w < props.manager().getWochenTypModell() + 1; w++) {
			if (hatWochentypen.value && (wt !== 0) && ((w !== 0) && (w !== wt)))
				continue;
			for (const unterricht of getUnterrichte(wochentag, stunde, w, null))
				if (unterricht.schienen.contains(schiene.id))
					return false;
		}
		return true;
	}

	/**
	 * Prüfe, ob der Kurs- oder Klassenunterricht bereits in einem Unterricht bei dem Zeitraster existiert.
	 * Ist dies der Fall, so wird geprüft, ob dieser bei dem angegebenen Wochentyp noch eingefügt werden darf.
	 *
	 * @param unterricht   das Unterrichtsobjekt mit Bezug zu dem Kurs- oder Klassenunterricht
	 * @param wochentag    der Wochentag für das Zeitraster-Element
	 * @param stunde       die Stunde für das Zeitraster-Element
	 * @param wt           der zu prüfende Wochentyp
	 */
	function isDropZoneZeitrasterUnterricht(unterricht: StundenplanUnterricht, wochentag: number, stunde: number, wt : number) : boolean {
		const z = props.manager().zeitrasterGetByIdOrException(unterricht.idZeitraster);
		const uwt = unterricht.wochentyp;
		// Prüfe, ob der Unterricht in das gleiche Zeitraster-Element gelegt werden soll...
		if ((z.wochentag === wochentag) && (z.unterrichtstunde === stunde)) {
			// ... wenn kein Wochentyp-Modell vorhanden ist, dann darf kein Unterricht doppelt plaziert werden
			if (!hatWochentypen.value)
				return false;
			// ... wenn ein Wochentyp-Modell verwendet wird, dann muss der Wochentyp verändert werden
			if (wt === uwt)
				return false;
		} else {
			// Prüfe, ob es sich um Kurs oder Klassenunterricht handelt und überprüfe die Dropzone anhand der Art des Unterrichts
			if (unterricht.idKurs === null)
				return isDropZoneZeitrasterKlassenunterricht(props.manager().klassenunterrichtGetByKlasseIdAndFachIdOrException(unterricht.klassen.get(0), unterricht.idFach), wochentag, stunde, wt);
			return isDropZoneZeitrasterKurs(props.manager().kursGetByIdOrException(unterricht.idKurs), wochentag, stunde, wt);
		}
		return true;
	}

	/**
	 * Prüfe, ob einer der Kurs- oder Klassenunterrichte bereits in einem Unterricht bei dem Zeitraster existiert.
	 * Ist dies der Fall, so wird geprüft, ob dieser bei dem angegebenen Wochentyp noch eingefügt werden darf.
	 *
	 * @param unterrichte   die Unterrichtsobjekte mit Bezug zu den Kurs- oder Klassenunterrichten
	 * @param wochentag     der Wochentag für das Zeitraster-Element
	 * @param stunde        die Stunde für das Zeitraster-Element
	 * @param wt            der zu prüfende Wochentyp
	 */
	function isDropZoneZeitrasterUnterrichtListe(unterrichte: List<StundenplanUnterricht>, wochentag: number, stunde: number, wt : number) : boolean {
		let z = new StundenplanZeitraster();
		let uwt = 0;
		for (const unterricht of unterrichte) {
			z = props.manager().zeitrasterGetByIdOrException(unterricht.idZeitraster);
			uwt = unterricht.wochentyp;
			break;
		}
		// Prüfe, ob der Unterricht in das gleiche Zeitraster-Element gelegt werden soll...
		if ((z.wochentag === wochentag) && (z.unterrichtstunde === stunde)) {
			// ... wenn kein Wochentyp-Modell vorhanden ist, dann darf kein Unterricht doppelt plaziert werden
			if (!hatWochentypen.value)
				return false;
			// ... wenn ein Wochentyp-Modell verwendet wird, dann muss der Wochentyp verändert werden
			if (wt === uwt)
				return false;
		} else {
			for (const unterricht of unterrichte) {
				// Prüfe, ob es sich um Kurs oder Klassenunterricht handelt und überprüfe die Dropzone anhand der Art des Unterrichts
				if ((unterricht.idKurs === null) && !isDropZoneZeitrasterKlassenunterricht(props.manager().klassenunterrichtGetByKlasseIdAndFachIdOrException(unterricht.klassen.get(0), unterricht.idFach), wochentag, stunde, wt))
					return false;
				if ((unterricht.idKurs !== null) && !isDropZoneZeitrasterKurs(props.manager().kursGetByIdOrException(unterricht.idKurs), wochentag, stunde, wt))
					return false;
			}
		}
		return true;
	}


	/**
	 * Prüfe, ob die aktuelle Daten eines Drag&Drop-Vorgangs einen Bezug zu einem Unterricht bei dem Zeitraster haben,
	 * welcher ein Drop verhindert. Bei der Entscheidung wird der Wochentyp miteinbezogen.
	 *
	 * @param wochentag   der Wochentag für das Zeitraster-Element
	 * @param stunde      die Stunde für das Zeitraster-Element
	 * @param wt          der zu prüfende Wochentyp
	 */
	function isDropZoneZeitraster(wochentag: number, stunde: number, wt : number) {
		const data = draggedData.value;
		if ((data === undefined) || (data instanceof StundenplanPausenaufsicht))
			return false;
		// Prüfe, ob das drag-Objekt die Plazierung in einem Zeitraster-Element und einem Wochentyp erlaubt
		if (data.isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanKlassenunterricht'))
			return isDropZoneZeitrasterKlassenunterricht(cast_de_svws_nrw_core_data_stundenplan_StundenplanKlassenunterricht(data), wochentag, stunde, wt);
		if (data.isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanKurs'))
			return isDropZoneZeitrasterKurs(cast_de_svws_nrw_core_data_stundenplan_StundenplanKurs(data), wochentag, stunde, wt);
		if (data.isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanUnterricht'))
			return isDropZoneZeitrasterUnterricht(cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterricht(data), wochentag, stunde, wt);
		if (data.isTranspiledInstanceOf('java.util.List'))
			return isDropZoneZeitrasterUnterrichtListe(cast_java_util_ArrayList<StundenplanUnterricht>(data), wochentag, stunde, wt);
		if (data.isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanSchiene'))
			return isDropZoneZeitrasterSchiene(cast_de_svws_nrw_core_data_stundenplan_StundenplanSchiene(data), wochentag, stunde, wt);
		return true;
	}

	function hatUnterrichtGemeinsamerTyp(a: StundenplanUnterricht, b: StundenplanUnterricht) {
		if (a.id === b.id)
			return true;
		if (a.idKurs !== null)
			return (a.idKurs === b.idKurs);
		if (a.idFach !== b.idFach)
			return false;
		for (const k of a.klassen)
			if (b.klassen.contains(k))
				return true;
		return false;
	}

	const mapIsDraggedType = computed<HashMap<number, boolean>>(() => {
		const result = new HashMap<number, boolean>();
		for (const unterrichte of mapUnterrichte.value.getNonNullValuesAsList())
			for (const unterricht of unterrichte)
				result.put(unterricht.id, isDraggedTypeInternal(unterricht));
		return result;
	});

	function isDraggedType(unterricht: StundenplanUnterricht) {
		return mapIsDraggedType.value.get(unterricht.id) ?? false;
	}

	function isDraggedTypeInternal(unterricht: StundenplanUnterricht) {
		const data = draggedData.value;
		if ((data === undefined) || (data instanceof StundenplanPausenaufsicht))
			return false;
		if (data.isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanKlassenunterricht')) {
			const ku = cast_de_svws_nrw_core_data_stundenplan_StundenplanKlassenunterricht(data);
			return (unterricht.idFach === ku.idFach) && (unterricht.klassen.contains(ku.idKlasse));
		}
		if (data.isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanKurs'))
			return (unterricht.idKurs === cast_de_svws_nrw_core_data_stundenplan_StundenplanKurs(data).id);
		if (data.isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanUnterricht'))
			return hatUnterrichtGemeinsamerTyp(unterricht, cast_de_svws_nrw_core_data_stundenplan_StundenplanUnterricht(data));
		if (data.isTranspiledInstanceOf('java.util.List')) {
			const unterrichte = cast_java_util_ArrayList<StundenplanUnterricht>(data);
			for (const u of unterrichte)
				if (hatUnterrichtGemeinsamerTyp(unterricht, u))
					return true;
			return false;
		}
		if (data.isTranspiledInstanceOf('de.svws_nrw.core.data.stundenplan.StundenplanSchiene')) {
			const schiene = cast_de_svws_nrw_core_data_stundenplan_StundenplanSchiene(data);
			for (const idSchiene of unterricht.schienen)
				if (schiene.id === idSchiene)
					return true;
			return false;
		}
		return false;
	}

	/**
	 * Aktualisiert infolge eine Drag-Events die aktuelle Position im Zeitraster der Stundenplans.
	 *
	 * @param event       das Drag-Event
	 * @param wochentag   der Wochentag, über dem sich der Mouse-Pointer befindet
	 * @param stunde      die Stunde, über der sich der Mouse-Pointer befindet
	 */
	function checkDropZoneZeitraster(event: DragEvent, wochentag: number, stunde: number) : void {
		const container = event.currentTarget instanceof HTMLDivElement ? event.currentTarget : null;
		if (container === null)
			return;
		const rect = container.getBoundingClientRect();
		const mouseRelX = (event.clientX - rect.x) / rect.width;
		const mouseRelY = (event.clientY - rect.y) / rect.height;
		const wtModell = props.manager().getWochenTypModell();
		const calcWt = Math.min(Math.max(Math.trunc(mouseRelX * wtModell) + 1, 0), wtModell);
		const wt = hatWochentypen.value && (mouseRelY > 0.5) ? calcWt : 0;
		updateDragOverPosition(event, wochentag, stunde, wt);
		if (isZeitrasterDropZone.value.getOrException(wochentag, stunde, wt))
			event.preventDefault();
	}

	function isDropZonePausenzeit(pause : StundenplanPausenzeit) : boolean {
		const data = draggedData.value;
		if ((data === undefined) || (!(data instanceof StundenplanPausenaufsicht)))
			return false;
		if (pause.id === data.idPausenzeit)
			return false;
		return true;
	}

	function checkDropZonePausenzeit(event: DragEvent, pause : StundenplanPausenzeit) {
		if (isDropZonePausenzeit(pause))
			event.preventDefault();
	}

</script>
