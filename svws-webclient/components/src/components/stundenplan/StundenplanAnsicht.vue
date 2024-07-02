<template>
	<div v-if="manager().getListZeitraster().size()" class="svws-ui-stundenplan" :class="`${showZeitachse ? 'svws-hat-zeitachse' : 'svws-ohne-zeitachse'} svws-zeitraster-${zeitrasterSteps}`">
		<!-- Die Überschriften des Stundenplan -->
		<div class="svws-ui-stundenplan--head">
			<span class="icon i-ri-time-line svws-time-icon print:hidden" v-if="showZeitachse" />
			<!-- Das Feld links in der Überschrift beinhaltet den ausgewählten Wochentyp -->
			<div class="inline-flex gap-1 items-center justify-center print:pl-2 print:justify-start" :class="{'opacity-50 print:invisible': wochentyp() === 0, 'font-bold text-headline-md pb-0.5': wochentyp() !== 0}">
				{{ manager().stundenplanGetWochenTypAsString(wochentyp()) }}
			</div>
			<!-- Daneben werden die einzelnen Wochentage des Stundenplans angezeigt -->
			<div v-for="wochentag in wochentagRange" :key="wochentag.id" class="font-bold text-center inline-flex items-center w-full justify-center">
				<div> {{ wochentage[wochentag.id] }} </div>
			</div>
		</div>
		<!-- Die Daten des Stundenplans -->
		<div class="svws-ui-stundenplan--body" :style="{'--zeitrasterRows': zeitrasterRows}">
			<!-- Die Zeitachse des Stundenplans auf der linken Seite -->
			<div class="svws-ui-stundenplan--zeitraster svws-zeitachse" v-if="showZeitachse">
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
			<div class="svws-ui-stundenplan--zeitraster">
				<!-- Die Zeitraster-Einträge -->
				<div v-for="stunde in zeitrasterRange" :key="stunde"
					class="svws-ui-stundenplan--stunde text-center justify-center"
					:style="posZeitraster(undefined, stunde)">
					<div class="text-headline-sm"> {{ stunde }}. Stunde </div>
					<div v-for="zeiten in manager().unterrichtsstundeGetUhrzeitenAsStrings(stunde)" :key="zeiten" class="font-bold text-sm">
						{{ zeiten.replace(' Uhr', '') }}
					</div>
				</div>
				<!-- Die Pausenzeiten -->
				<template v-if="showZeitachse && (modePausenaufsichten !== 'aus')">
					<!--TODO: Pausenzeiten, wenn Zeitachse deaktiviert ist-->
					<template v-for="pause in pausenzeiten" :key="pause">
						<div class="svws-ui-stundenplan--pause text-sm text-center justify-center" :style="posPause(pause)">
							<div> {{ pause.bezeichnung }} </div>
							<div> {{ (pause.ende! - pause.beginn!) }} Minuten </div>
						</div>
					</template>
				</template>
			</div>
			<!-- Zeige die Unterrichte und Pausenaufsichten des Stundenplans -->
			<div v-for="wochentag in wochentagRange" :key="wochentag.id" class="svws-ui-stundenplan--zeitraster">
				<!-- Darstellung des Unterrichtes in dem Zeitraster -->
				<template v-for="stunde in zeitrasterRange" :key="stunde">
					<div class="svws-ui-stundenplan--stunde relative" :style="posZeitraster(wochentag, stunde)"
						@dragover="checkDropZoneZeitraster($event, wochentag, stunde)" @drop="onDrop(manager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde), dragOverPos.wochentyp)">
						<!-- Unterstütze mehrere Drop-Bereich, um direkt den einzelnen Wochentypen zuweisen zu können ... -->
						<div v-if="(dragData !== undefined) && (dragData() !== undefined)"
							@dragleave="dragOverPos.wochentyp = undefined"
							class="absolute pointer-events-none w-[calc(100%-0.5rem)] h-[calc(100%-0.5rem)] flex flex-col gap-1 z-10 bg-white bg-opacity-75 text-center select-none"
							:class="isDragOverPosition(wochentag, stunde).value ? ['opacity-100']:['opacity-0']">
							<div class="flex-grow flex justify-center items-center p-2 border-2 border-solid rounded-lg border-black/50 hover:font-bold"
								:class="{'bg-success/50': dragOverPos.wochentyp === 0}">
								Jede Woche
							</div>
							<div v-if="manager().getWochenTypModell() > 0" class="h-[calc(50%-0.25rem)] flex flex-row gap-1">
								<template v-for="wt, wtIndex in manager().getWochenTypModell()" :key="wtIndex">
									<div class="flex-grow flex justify-center items-center p-2 border-2 border-solid rounded-lg border-black/50 hover:border-black hover:font-bold"
										:class="{'bg-success/50': wtIndex+1 === dragOverPos.wochentyp}">
										<span class="w-20">{{ manager().stundenplanGetWochenTypAsString(wtIndex+1) }}</span>
									</div>
								</template>
							</div>
						</div>
						<!-- Passe die Darstellung je nach ausgewähltem Wochentyp an... -->
						<!-- Allgemeiner Wochentyp ausgewählt -->
						<!-- zunächst die Darstellung des allgemeinen Unterrichtes -->
						<template v-if="mode !== 'klasse'">
							<!-- Diese Ansicht hat keine Anzeige der Schienen (Schüler, Lehrer) -->
							<div v-for="unterricht in getUnterricht(wochentag, stunde, 0, 0).value" :key="unterricht.id"
								class="svws-ui-stundenplan--unterricht"
								:class="{'flex-grow': getUnterricht(wochentag, stunde, 0, -1).value.size() === 1}"
								:style="`background-color: ${getBgColor(manager().fachGetByIdOrException(unterricht.idFach).kuerzelStatistik)}`"
								:draggable @dragstart="onDrag(unterricht)" @dragend="onDrag(undefined)">
								<div class="font-bold flex place-items-center group" :class="`${mode === 'lehrer' ? 'col-span-3' : 'col-span-2'}`" title="Unterricht">
									<span class="icon i-ri-draggable inline-block icon-dark -ml-1 opacity-60 group-hover:opacity-100 group-hover:icon-dark" />
									<span>{{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }}</span>
								</div>
								<div v-if="mode !== 'lehrer'" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
								<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
							</div>
						</template>
						<template v-else v-for="schiene in [{id: -1}, ...getSchienen(wochentag, stunde, 0).value]" :key="schiene.id">
							<div :class="{'bg-light rounded-md pl-1 pr-1 pb-1 mt-1': schiene.id > -1}" @dragstart="onDrag(getUnterricht(wochentag, stunde, 0, schiene.id).value, $event)" @dragend="onDrag(undefined)" :draggable>
								<div v-if="'bezeichnung' in schiene" class="col-span-full text-sm font-bold pt-1 pb-2 print:mb-0 flex place-items-center group ml-2.5" :class="{'cursor-grab': draggable}">
									<span class="icon i-ri-draggable inline-block icon-dark -ml-1 opacity-60 group-hover:opacity-100 group-hover:icon-dark" />
									<span>{{ schiene.bezeichnung }}</span>
								</div>
								<div v-for="unterricht in getUnterricht(wochentag, stunde, 0, schiene.id).value" :key="unterricht.id"
									class="svws-ui-stundenplan--unterricht"
									:class="{'cursor-grab': draggable, 'flex-grow': getUnterricht(wochentag, stunde, 0, -1).value.size() === 1}"
									:style="`background-color: ${getBgColor(manager().fachGetByIdOrException(unterricht.idFach).kuerzelStatistik)}`"
									:draggable @dragstart.stop="onDrag(unterricht)" @dragend.stop="onDrag(undefined)">
									<div class="font-bold col-span-2 flex place-items-center group" title="Unterricht">
										<span class="icon i-ri-draggable inline-block -ml-1 icon-dark opacity-60 group-hover:opacity-100 group-hover:icon-dark" />
										<span>{{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }}</span>
									</div>
									<div title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
									<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
								</div>
							</div>
						</template>
						<!-- dann die Darstellung des speziellen Unterrichtes der Wochentypen -->
						<div v-if="zeitrasterHatUnterrichtMitWochentyp(wochentag.id, stunde)" class="svws-multiple gap-1">
							<template v-for="wt in getWochentyp" :key="wt">
								<div :class="{'border-r border-black/25 p-1 last:border-r-0 flex flex-col': wochentyp()}" :style="wochentyp() ? `grid-column-start: ${wt}`: ''">
									<template v-if="mode !== 'klasse'">
										<!-- Diese Ansicht hat keine Anzeige der Schienen (Schüler, Lehrer) -->
										<template v-if="getUnterricht(wochentag, stunde, wt, 0).value.size() > 0">
											<div class="col-span-full text-sm font-bold text-center mb-1 py-1 print:mb-0"> {{ manager().stundenplanGetWochenTypAsString(wt) }}</div>
										</template>
										<div v-for="unterricht in getUnterricht(wochentag, stunde, wt, 0).value" :key="unterricht.id"
											class="svws-ui-stundenplan--unterricht"
											:class="{'flex-grow': getUnterricht(wochentag, stunde, wt, 0).value.size() === 1, 'svws-compact': !wochentyp(), 'cursor-grab': draggable}"
											:style="`background-color: ${getBgColor(manager().fachGetByIdOrException(unterricht.idFach).kuerzelStatistik)};`"
											:draggable @dragstart="onDrag(unterricht)" @dragend="onDrag(undefined)">
											<div class="font-bold col-span-2 flex place-items-center group" title="Unterricht">
												<span class="icon i-ri-draggable inline-block -ml-1 icon-dark opacity-60 group-hover:opacity-100 group-hover:icon-dark" />
												<span>{{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }}</span>
											</div>
											<div v-if="mode !== 'lehrer'" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
											<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
										</div>
									</template>
									<template v-else v-for="schiene in [{id: -1}, ...getSchienen(wochentag, stunde, wt).value]" :key="schiene.id">
										<template v-if="getUnterricht(wochentag, stunde, wt, schiene.id).value.size() > 0">
											<div class="col-span-full text-sm font-bold text-center mb-1 py-1 print:mb-0"> {{ manager().stundenplanGetWochenTypAsString(wt) }}</div>
										</template>
										<div :class="{'bg-light rounded-md pl-1 pr-1 pb-1 pt-0': schiene.id > -1}" @dragstart="onDrag(getUnterricht(wochentag, stunde, wt, schiene.id).value, $event)" @dragend="onDrag(undefined)" :draggable>
											<div v-if="'bezeichnung' in schiene" class="col-span-full text-sm font-bold text-center pt-1 pb-2 print:mb-0" :class="{'cursor-grab': draggable}">
												{{ schiene.bezeichnung }}
											</div>
											<div v-for="unterricht in getUnterricht(wochentag, stunde, wt, schiene.id).value" :key="unterricht.id"
												class="svws-ui-stundenplan--unterricht"
												:class="{'cursor-grab': draggable, 'flex-grow': getUnterricht(wochentag, stunde, wt, schiene.id).value.size() === 1, 'svws-compact': !wochentyp()} "
												:style="`background-color: ${getBgColor(manager().fachGetByIdOrException(unterricht.idFach).kuerzelStatistik)};`"
												:draggable @dragstart.stop="onDrag(unterricht)" @dragend.stop="onDrag(undefined)">
												<div class="font-bold col-span-2 flex place-items-center group" title="Unterricht">
													<span class="icon i-ri-draggable inline-block -ml-1 icon-dark opacity-60 group-hover:opacity-100 group-hover:icon-dark" />
													<span>{{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }}</span>
												</div>
												<div title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
												<div title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
											</div>
										</div>
									</template>
								</div>
							</template>
						</div>
					</div>
				</template>
				<!-- Darstellung der Pausenzeiten und der zugehörigen Aufsichten -->
				<template v-if="showZeitachse && (modePausenaufsichten !== 'aus')">
					<!--TODO: Pausenzeiten, wenn Zeitachse deaktiviert ist-->
					<!-- TODO Modi 'normal', 'kurz, 'tooltip' -->
					<template v-for="pause in getPausenzeitenWochentag(wochentag).value" :key="pause">
						<div class="svws-ui-stundenplan--pause" :style="posPause(pause)" @dragover="checkDropZonePausenzeit($event, pause)" @drop="onDrop(pause)">
							<template v-if="modePausenaufsichten === 'normal'">
								<div v-for="pausenaufsicht in getPausenaufsichtenPausenzeit(pause).value" :key="pausenaufsicht.id" class="svws-ui-stundenplan--pausen-aufsicht flex-grow"
									:class="{'svws-lehrkraft': mode === 'lehrer', 'cursor-grab': draggable}"
									:draggable @dragstart="onDrag(pausenaufsicht)" @dragend="onDrag(undefined)">
									<div class="font-bold"> {{ pause.bezeichnung === 'Pause' && mode === 'lehrer' ? 'Aufsicht' : pause.bezeichnung }} </div>
									<div> <span v-if="mode !== 'lehrer'" title="Lehrkraft"> {{ manager().lehrerGetByIdOrException(pausenaufsicht.idLehrer).kuerzel }} </span> </div>
									<div title="Aufsichtsbereiche"> {{ aufsichtsbereiche(pausenaufsicht) }}</div>
								</div>
							</template>
							<template v-if="modePausenaufsichten === 'kurz'">
								<div class="flex flex-col justify-center content-center h-full text-center pt-1 pb-2" :class="{'svws-lehrkraft': mode === 'lehrer'}">
									<div class="font-bold"> {{ pause.bezeichnung === 'Pause' && mode === 'lehrer' ? 'Aufsicht' : pause.bezeichnung }} </div>
									<span v-if="mode !== 'lehrer'" title="Lehrkraft" class="max-w-[24ch] leading-none mx-auto">{{ getStringAufsichten(pause) }}</span>
								</div>
							</template>
							<template v-if="modePausenaufsichten === 'tooltip'">
								<div class="svws-ui-stundenplan--pausen-aufsicht flex-grow flex items-center justify-center">
									<svws-ui-tooltip>
										<span class="inline-flex flex-col items-center leading-tight">
											<span>{{ pause.bezeichnung ? pause.bezeichnung : 'Pause' }}</span>
											<span class="text-sm">{{ getPausenaufsichtenPausenzeit(pause).value.size() }} Aufsichten</span>
										</span>
										<template #content>
											<div v-for="pausenaufsicht in getPausenaufsichtenPausenzeit(pause).value" :key="pausenaufsicht.id" class="grid grid-cols-3 gap-2 items-center" :class="{'svws-lehrkraft': mode === 'lehrer', 'cursor-grab': draggable}"
												:draggable @dragstart="onDrag(pausenaufsicht)" @dragend="onDrag(undefined)">
												<div class="text-sm"> {{ pause.bezeichnung === 'Pause' && mode === 'lehrer' ? 'Aufsicht' : pause.bezeichnung }} </div>
												<div> <span v-if="mode !== 'lehrer'" title="Lehrkraft"> {{ manager().lehrerGetByIdOrException(pausenaufsicht.idLehrer).kuerzel }} </span> </div>
												<div title="Aufsichtsbereiche"> {{ aufsichtsbereiche(pausenaufsicht) }}</div>
											</div>
										</template>
									</svws-ui-tooltip>
								</div>
							</template>
						</div>
					</template>
				</template>
			</div>
		</div>
	</div>
	<div v-else class="svws-ui-stundenplan">Es wurden noch keine Zeitraster für diesen Stundenplan angelegt.</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { StundenplanAnsichtDragData, StundenplanAnsichtDropZone, StundenplanAnsichtProps } from "./StundenplanAnsichtProps";
	import type { StundenplanPausenzeit } from "../../../../core/src/core/data/stundenplan/StundenplanPausenzeit";
	import type { Wochentag } from "../../../../core/src/core/types/Wochentag";
	import type { List } from "../../../../core/src/java/util/List";
	import { DeveloperNotificationException } from "../../../../core/src/core/exceptions/DeveloperNotificationException";
	import { StundenplanKlassenunterricht } from "../../../../core/src/core/data/stundenplan/StundenplanKlassenunterricht";
	import { StundenplanPausenaufsicht } from "../../../../core/src/core/data/stundenplan/StundenplanPausenaufsicht";
	import { StundenplanUnterricht } from "../../../../core/src/core/data/stundenplan/StundenplanUnterricht";
	import { StundenplanZeitraster } from "../../../../core/src/core/data/stundenplan/StundenplanZeitraster";
	import { StundenplanSchiene } from "../../../../core/src/core/data/stundenplan/StundenplanSchiene";
	import { StundenplanKurs } from "../../../../core/src/core/data/stundenplan/StundenplanKurs";
	import { ZulaessigesFach } from "../../../../core/src/core/types/fach/ZulaessigesFach";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";

	const wochentage = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag' ];

	const props = withDefaults(defineProps<StundenplanAnsichtProps>(), {
		mode: 'schueler',
		modePausenaufsichten: 'normal',
		showZeitachse: true,
		zeitrasterSteps: 5,
		ignoreEmpty: false,
		useDragAndDrop: false,
		dragData: () => undefined,
		onDrag: (data: StundenplanAnsichtDragData, event?: DragEvent) => {},
		onDrop: (zone: StundenplanAnsichtDropZone, wochentyp?: number) => {},
	});

	const dragOverPos = ref<{
		wochentag: Wochentag | undefined,
		stunde : number | undefined,
		wochentyp : number | undefined,
	}>({
		wochentag: undefined,
		stunde: undefined,
		wochentyp: undefined,
	});

	function updateDragOverPosition(event: DragEvent, wochentag : Wochentag, stunde : number, wochentyp : number | undefined) {
		if (dragOverPos.value.wochentag !== wochentag)
			dragOverPos.value.wochentag = wochentag;
		if (dragOverPos.value.stunde !== stunde)
			dragOverPos.value.stunde = stunde;
		if (dragOverPos.value.wochentyp !== wochentyp)
			dragOverPos.value.wochentyp = wochentyp;
	}

	const isDragOverPosition = (wochentag : Wochentag, stunde : number) => computed<boolean>(() => (dragOverPos.value.wochentag?.id === wochentag.id) && (dragOverPos.value.stunde === stunde))

	const getWochentyp = computed(()=> props.wochentyp() === 0 ? props.manager().getWochenTypModell() : [props.wochentyp()])

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

	const pausenzeiten = computed(() => {
		if (props.mode === 'schueler')
			return props.manager().pausenzeitGetMengeBySchuelerIdAsList(props.id);
		if (props.mode === 'lehrer')
			return props.manager().pausenzeitGetMengeByLehrerIdAsList(props.id);
		if (props.mode === 'klasse')
			return props.manager().pausenzeitGetMengeByKlasseIdAsList(props.id);
		throw new DeveloperNotificationException("const pausenzeiten: Unbekannter Mode " + props.mode);
	});

	function zeitrasterHatUnterrichtMitWochentyp(wochentag: number, stunde: number): boolean {
		switch (props.mode) {
			case 'klasse':
				return props.manager().zeitrasterHatUnterrichtMitWochentyp1BisNByKlasseIdWochentagAndStunde(props.id, wochentag, stunde);
			case 'schueler':
				return props.manager().zeitrasterHatUnterrichtMitWochentyp1BisNBySchuelerIdWochentagAndStunde(props.id, wochentag, stunde);
			case 'lehrer':
				return props.manager().zeitrasterHatUnterrichtMitWochentyp1BisNByLehrerIdWochentagAndStunde(props.id, wochentag, stunde);
			default:
				return false;
		}
	}

	const gesamtzeit = computed(() => {
		const tmp = ende.value - beginn.value;
		return tmp <= 0 ? 360 : tmp;
	});

	const zeitrasterRows = computed(() => {
		if(!props.showZeitachse) {
			// TODO: Pausenzeiten, wenn Zeitachse deaktiviert ist
			// props.manager().pausenzeitGetMengeByWochentagOrEmptyList(1).size() || 0
			return zeitrasterRange.value.length;
		}

		// Für alle x Minuten eine Grid Row (zeitrasterSteps)
		return Math.round(gesamtzeit.value / props.zeitrasterSteps);
	});

	function aufsichtsbereiche(pausenaufsicht: StundenplanPausenaufsicht): string {
		let result = "";
		for (const zuordnung of pausenaufsicht.bereiche) {
			const bereich = props.manager().aufsichtsbereichGetByIdOrException(zuordnung.idAufsichtsbereich);
			if (result !== "")
				result += ",";
			result += bereich.kuerzel;
		}
		return result;
	}

	const getSchienen = (wochentag: Wochentag, stunde: number, wochentyp: number) => computed<List<StundenplanSchiene>>(() => {
		if (props.mode === 'schueler')
			return props.manager().schieneGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag.id, stunde, wochentyp, false);
		if (props.mode === 'lehrer')
			return props.manager().schieneGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag.id, stunde, wochentyp, false);
		if (props.mode === 'klasse')
			return props.manager().schieneGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag.id, stunde, wochentyp, false);
		throw new DeveloperNotificationException("function getSchienen: Unbekannter Mode " + props.mode);
	})

	const getUnterricht = (wochentag: Wochentag, stunde: number, wochentyp: number, schiene: number) => computed<List<StundenplanUnterricht>>(() => {
		if (props.mode === 'schueler')
			return props.manager().unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag.id, stunde, wochentyp, false);
		if (props.mode === 'lehrer')
			return props.manager().unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag.id, stunde, wochentyp, false);
		if (props.mode === 'klasse')
			return props.manager().unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(props.id, wochentag.id, stunde, wochentyp, schiene, false);
		if (props.mode === 'fach') {
			const unterricht = props.manager().unterrichtGetMengeAsList()
			const list: List<StundenplanUnterricht> = new ArrayList();
			const zeitraster = props.manager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde);
			for (const u of unterricht)
				if ((u.idFach === props.id) && (u.wochentyp === wochentyp) && (u.idZeitraster === zeitraster.id))
					list.add(u);
			return list;
		}
		// return props.manager().unterrichtGetMengeByFachId(props.id);
		if (props.mode === 'raum') {
			const unterricht = props.manager().unterrichtGetMengeAsList()
			const list: List<StundenplanUnterricht> = new ArrayList();
			const zeitraster = props.manager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde);
			for (const u of unterricht)
				if (u.raeume.contains(props.id) && (u.wochentyp === wochentyp) && (u.idZeitraster === zeitraster.id))
					list.add(u);
			return list;
		}
		throw new DeveloperNotificationException("function getUnterricht: Unbekannter Mode " + props.mode);
	})

	const getPausenzeitenWochentag = (wochentag: Wochentag) => computed<List<StundenplanPausenzeit>>(() => {
		if (props.mode === 'schueler')
			return props.manager().pausenzeitGetMengeBySchuelerIdAndWochentagAsList(props.id, wochentag.id);
		if (props.mode === 'lehrer')
			return props.manager().pausenzeitGetMengeByLehrerIdAndWochentagAsList(props.id, wochentag.id);
		if (props.mode === 'klasse')
			return props.manager().pausenzeitGetMengeByKlasseIdAndWochentagAsList(props.id, wochentag.id);
		throw new DeveloperNotificationException("function getPausenzeitenWochentag: Unbekannter Mode " + props.mode);
	})

	const getPausenaufsichtenPausenzeit = (pause: StundenplanPausenzeit) => computed<List<StundenplanPausenaufsicht>>(() => {
		// TODO Pausenaufsicht zusätzlich pro "wochentyp" UND "inklWoche0=true"
		if (props.mode === 'schueler')
			return props.manager().pausenaufsichtGetMengeBySchuelerIdAndPausenzeitIdAndWochentypAndInklusive(props.id, pause.id, props.wochentyp(), true);
		if (props.mode === 'lehrer')
			return props.manager().pausenaufsichtGetMengeByLehrerIdAndPausenzeitIdAndWochentypAndInklusive(props.id, pause.id, props.wochentyp(), true);
		if (props.mode === 'klasse')
			return props.manager().pausenaufsichtGetMengeByKlasseIdAndPausenzeitIdAndWochentypAndInklusive(props.id, pause.id, props.wochentyp(), true);
		throw new DeveloperNotificationException("function getPausenaufsichtenPausenzeit: Unbekannter Mode " + props.mode);
	})

	function posZeitraster(wochentag: Wochentag | undefined, stunde: number): string {
		let zbeginn =  props.manager().zeitrasterGetMinutenMinDerStunde(stunde);
		let zende =  props.manager().zeitrasterGetMinutenMaxDerStunde(stunde);
		if (wochentag !== undefined) {
			const z = props.manager().zeitrasterGetByWochentagAndStundeOrNull(wochentag.id, stunde);
			if (z !== null) {
				if (z.stundenbeginn !== null)
					zbeginn = z.stundenbeginn;
				if (z.stundenende !== null)
					zende = z.stundenende;
			}
		}
		let rowStart = 0;
		let rowEnd = 10;
		if ((zbeginn !== null) && (zende !== null)) {
			rowStart = (zbeginn - beginn.value) / props.zeitrasterSteps;
			rowEnd = (zende - beginn.value) / props.zeitrasterSteps;
		}
		if (!props.showZeitachse) {
			rowStart = stunde - 1;
			rowEnd = stunde;
		}

		return "grid-row-start: " + (Math.round(rowStart) + 1) + "; grid-row-end: " + (Math.round(rowEnd) + 1) + "; grid-column: 1;";
	}

	function posPause(pause: StundenplanPausenzeit): string {
		const pzeit = props.manager().pausenzeitGetByIdOrException(pause.id);
		let rowStart = 0;
		let rowEnd = 10;
		if ((pzeit.beginn !== null) && (pzeit.ende !== null)) {
			rowStart = (pzeit.beginn - beginn.value) / props.zeitrasterSteps;
			rowEnd = (pzeit.ende - beginn.value) / props.zeitrasterSteps;
		}
		return "grid-row-start: " + (Math.round(rowStart) + 1) + "; grid-row-end: " + (Math.round(rowEnd) + 1) + "; grid-column: 1;";
	}

	function getStringAufsichten(pause: StundenplanPausenzeit) {
		const pausenaufsichten = getPausenaufsichtenPausenzeit(pause);
		let text = "";
		for (const pausenaufsicht of pausenaufsichten.value) {
			if (text !== '')
				text += ', ';
			text += props.manager().lehrerGetByIdOrException(pausenaufsicht.idLehrer).kuerzel;
		}
		return text;
	}

	function getBgColor(fach: string): string {
		return ZulaessigesFach.getByKuerzelASD(fach).getHMTLFarbeRGB();
	}

	const draggable = computed<boolean>(() => props.useDragAndDrop);

	function isDropZoneZeitraster(wochentag: Wochentag, stunde: number, hatWt : boolean, wt : number) : boolean {
		const data = props.dragData();
		if ((data === undefined) || (data instanceof StundenplanPausenaufsicht))
			return false;
		if ((data instanceof StundenplanKurs) || (data instanceof StundenplanKlassenunterricht) || (data instanceof StundenplanSchiene))
			return true;
		let z = new StundenplanZeitraster();
		let uwt = 0;
		if (data instanceof StundenplanUnterricht) {
			z = props.manager().zeitrasterGetByIdOrException(data.idZeitraster);
			uwt = data.wochentyp;
		} else {
			for (const d of data) {
				z = props.manager().zeitrasterGetByIdOrException(d.idZeitraster);
				uwt = d.wochentyp;
				break;
			}
		}
		return (!((z.wochentag === wochentag.id) && (z.unterrichtstunde === stunde) && (!hatWt || (wt === uwt))));
	}

	function checkDropZoneZeitraster(event: DragEvent, wochentag: Wochentag, stunde: number) : void {
		const container : HTMLDivElement | null = event.currentTarget instanceof HTMLDivElement ? event.currentTarget as HTMLDivElement : null;
		if (container === null)
			return;
		const rect = container.getBoundingClientRect();
		const mouseRelX = (event.clientX - rect.x) / rect.width;
		const mouseRelY = (event.clientY - rect.y) / rect.height;
		const hatWt = props.manager().getWochenTypModell() > 0;
		const calcWt = Math.trunc(mouseRelX * props.manager().getWochenTypModell()) + 1;
		const wt = hatWt && (mouseRelY > 0.5) ? calcWt : 0;
		updateDragOverPosition(event, wochentag, stunde, wt);
		if (isDropZoneZeitraster(wochentag, stunde, hatWt, wt))
			event.preventDefault();
	}

	function isDropZonePausenzeit(pause : StundenplanPausenzeit) : boolean {
		const data = props.dragData();
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

<style lang="postcss">

	.svws-ui-stundenplan {
		@apply flex flex-col h-full min-w-max flex-grow overflow-y-scroll overflow-x-hidden pr-4;
		--zeitrasterRows: 0;
	}

	.svws-ui-stundenplan--head,
	.svws-ui-stundenplan--body {
		@apply grid grid-flow-col;
		grid-template-columns: 8rem repeat(auto-fit, minmax(10rem, 1fr));

		.svws-hat-zeitachse & {
			@media screen {
				grid-template-columns: 2rem 8rem repeat(auto-fit, minmax(10rem, 1fr));
			}
		}
	}

	.svws-ui-stundenplan--head {
		@apply bg-white dark:bg-black py-1 text-button;
		@apply h-[2.75rem] sticky -top-px z-20;
		@apply border border-black/25 dark:border-white/10;

		.svws-hat-zeitachse & {
			@media screen {
				@apply border-l-0;
			}
		}

		.svws-time-icon {
			@apply opacity-25 text-center self-center w-full;
		}

		> *:not(:first-child) {
			@apply min-w-[10rem];
		}
	}

	.svws-ui-stundenplan--body {
		@apply border-x border-black/25 dark:border-white/10 bg-white dark:bg-black -mt-px print:mt-0 relative;

		.svws-hat-zeitachse & {
			@media screen {
				@apply border-l-0;
			}
		}
	}

	.svws-ui-stundenplan--zeitraster {
		@apply grid grid-cols-1;
		grid-template-rows: repeat(var(--zeitrasterRows), minmax(0.6rem, 1fr));

		.svws-zeitraster-1 & {
			grid-template-rows: repeat(var(--zeitrasterRows), minmax(0.1rem, 1fr));
		}

		&.svws-zeitachse {
			@apply print:hidden h-full border-b-0 border-r border-black/25 dark:border-white/25;
		}
	}

	.svws-ui-stundenplan--stunde {
		@apply border border-l-0 border-black/25 dark:border-white/10;

		.svws-ui-stundenplan--zeitraster:last-child & {
			@apply border-r-0;
		}

		.svws-ohne-zeitachse & {
			+ .svws-ui-stundenplan--stunde {
				@apply border-t-0;
			}
		}
	}

	.svws-ui-stundenplan--stunde,
	.svws-ui-stundenplan--pause {
		@apply bg-white dark:bg-black tabular-nums w-full h-full p-1 leading-tight flex flex-col overflow-hidden;

		.svws-multiple {
			@apply grid h-full grid-flow-col -m-1 flex-grow;
			grid-template-columns: repeat(auto-fit, minmax(0, 1fr));
		}

		.svws-ui-stundenplan--mode-planung & {
			&:hover,
			&:focus-visible {
				.svws-ui-stundenplan--unterricht,
				.svws-ui-stundenplan--pausen-aufsicht,
				&.svws-label {
					@apply bg-light dark:bg-white/5;
				}
			}

			&.svws-selected-stunde {
				@apply text-svws;
			}
		}
	}

	.svws-ui-stundenplan--pause {
		@apply border-y-0;

		.svws-ui-stundenplan--mode-planung &:not(.svws-no-hover) {
			&:hover,
			&:focus-visible {
				@apply bg-light dark:bg-white/5;
			}
		}
	}

	.svws-ui-stundenplan--unterricht,
	.svws-ui-stundenplan--pausen-aufsicht {
		@apply rounded grid grid-cols-4 gap-x-2 w-full border border-black/10 px-2 py-1 content-center leading-none dark:text-black items-center;

		&.svws-compact {
			@apply grid-cols-2 py-1;
		}

		.svws-ui-stundenplan--mode-planung & {
			@apply flex flex-col gap-1 items-center flex-grow justify-center;
		}

		+ .svws-ui-stundenplan--unterricht,
		+ .svws-ui-stundenplan--pausen-aufsicht {
			@apply rounded-t-none;
		}

		&:not(:last-child) {
			@apply rounded-b-none;
		}

		.tooltip-trigger {
			@apply max-w-[14rem];
		}
	}

	.svws-ui-stundenplan--mode-planung {
		.svws-wochentag-label {
			@apply font-bold text-center inline-flex items-center w-full justify-center cursor-pointer;

			&:hover,
			&:focus-visible {
				span {
					@apply bg-light dark:bg-white/5;
				}
			}

			&.svws-selected {
				span {
					@apply bg-svws/5 text-svws font-bold border-svws/25;
				}
			}
		}

		.svws-ui-stundenplan--zeitraster.svws-selected,
		.svws-ui-stundenplan--stunde.svws-selected,
		.svws-ui-stundenplan--pause.svws-selected {
			.svws-ui-stundenplan--unterricht,
			.svws-ui-stundenplan--pausen-aufsicht {
				@apply bg-svws/5 text-svws font-bold;
			}
		}
	}

	.svws-ui-stundenplan--unterricht--warning {
		@apply flex flex-col gap-2 items-center justify-center text-center bg-error text-white rounded p-2 flex-grow print:hidden;

		~ .svws-ui-stundenplan--unterricht {
			@apply flex-grow-0 min-h-[2rem] hidden print:grid;

			&.svws-compact {
				@apply min-h-[5rem];
			}
		}

		&.svws-show {
			@apply hidden;

			~ .svws-ui-stundenplan--unterricht {
				@apply grid;
			}
		}
	}

	.svws-ui-stundenplan--pausen-aufsicht {
		&.svws-lehrkraft {
			@apply bg-white dark:bg-black border-black/25 dark:border-white/25;
		}
	}

	.svws-ui-stundenplan--einheit {
		@apply border-t border-black/50 dark:border-white/50 w-1/2 pr-1 opacity-50 ml-auto text-right h-px;
		font-size: 0.66rem;
	  	letter-spacing: -0.08em;

		&.svws-small {
			@apply w-1/2;
		}

		&.svws-extended {
			@apply w-full opacity-50;
		}
	}

</style>