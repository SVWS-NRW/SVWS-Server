<template>
	<div class="page--content page--content--full">
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<div class="ml-4 flex gap-0.5 items-center leading-none">
					<div class="text-button font-bold mr-1 -mt-px">Klasse:</div>
					<svws-ui-select headless title="Klasse" v-model="klasse" :items="stundenplanManager().klasseGetMengeSichtbarAsList()" :item-text="(i: StundenplanKlasse) => i.kuerzel" autocomplete
						:item-filter="(i: StundenplanKlasse[], text: string)=> i.filter(k=>k.kuerzel.includes(text.toLocaleLowerCase()))" :item-sort="() => 0"
						type="transparent" />
					<div class="text-button font-bold mr-1 -mt-px">Wochentyp:</div>
					<svws-ui-select headless title="Wochentyp" v-model="wochentypAnzeige" :items="wochentypen()"
						class="print:hidden" type="transparent"
						:disabled="wochentypen().size() <= 0"
						:item-text="(wt: number) => stundenplanManager().stundenplanGetWochenTypAsString(wt)" />
					<svws-ui-button type="transparent" @click.stop="doppelstundenModus = !doppelstundenModus" title="Doppelstundenmodus ein- und ausschalten" class="text-black dark:text-white">
						{{ doppelstundenModus ? 'Doppelstundenmodus' : 'Einzelstundenmodus' }}
					</svws-ui-button>
				</div>
			</svws-ui-sub-nav>
		</Teleport>
		<template v-if="props.stundenplanManager().klasseGetMengeAsList().isEmpty()">
			<span>Für diesen Stundenplan ist keine Klasse vorhanden.</span>
		</template>
		<template v-else>
			<div @dragover="checkDropZone($event)" @drop="onDrop(undefined)" class="flex flex-col justify-start mb-auto svws-table-offset h-full overflow-y-scroll overflow-x-hidden pr-4">
				<svws-ui-table :items="stundenplanManager().klassenunterrichtGetMengeByKlasseIdAsList(klasse.id)" :columns="colsKlassenunterricht">
					<template #body>
						<div v-for="ku in stundenplanManager().klassenunterrichtGetMengeByKlasseIdAsList(klasse.id)" :key="ku.idKlasse + '/' + ku.idFach" role="row" class="svws-ui-tr"
							:draggable="isDraggable()" @dragstart="onDrag(ku, $event)" @dragend="onDrag(undefined)"
							:style="`--background-color: ${stundenplanManager().klassenunterrichtGetWochenminutenREST(klasse.id, ku.idFach) > 0 ? getBgColor(stundenplanManager().fachGetByIdOrException(ku.idFach).kuerzelStatistik) : ''}`">
							<div role="cell" class="select-none svws-ui-td">
								<span :id="`klasse-${ku.idFach}-${ku.idKlasse}`" class="line-clamp-1" :title="ku.bezeichnung">{{ stundenplanManager().fachGetByIdOrException(ku.idFach).kuerzel }}</span>
							</div>
							<div role="cell" class="select-none svws-ui-td">
								<div class="svws-ui-badge select-none flex items-center justify-center relative group cursor-grab"
									:class="{ 'cursor-grabbing': dragData !== undefined }" @dragstart="wochentyp = 0" draggable="true">
									<span class="group-hover:bg-white rounded-sm w-3 absolute top-1/2 transform -translate-y-1/2 left-0">
										<i-ri-draggable class="w-4 -ml-0.5 text-black opacity-60 group-hover:opacity-100 group-hover:text-black" />
									</span>
									<span class="pl-2">Allgemein</span>
								</div>
								<template v-if="stundenplanManager().getWochenTypModell() > 0">
									<div v-for="i in stundenplanManager().getWochenTypModell()" :key="i" @dragstart="wochentyp = i" class="svws-ui-badge select-none flex items-center justify-center relative group cursor-grab"
										:class="{ 'cursor-grabbing': dragData !== undefined }" draggable="true">
										<span class="group-hover:bg-white rounded-sm w-3 absolute top-1/2 transform -translate-y-1/2 left-0">
											<i-ri-draggable class="w-4 -ml-0.5 text-black opacity-60 group-hover:opacity-100 group-hover:text-black" />
										</span>
										<span class="px-3">{{ String.fromCharCode(64 + i) }}</span>
									</div>
								</template>
							</div>
							<div role="cell" class="select-none svws-ui-td svws-align-center">
								{{ stundenplanManager().klassenunterrichtGetWochenstundenIST(ku.idKlasse, ku.idFach) }}/{{ stundenplanManager().klassenunterrichtGetWochenstundenSOLL(ku.idKlasse, ku.idFach) }}
							</div>
						</div>
					</template>
				</svws-ui-table>
				<svws-ui-checkbox type="toggle" v-model="schienSortierung" class="mt-8">Nach Schienen sortieren</svws-ui-checkbox>
				<svws-ui-table :items="stundenplanManager().kursGetMengeByKlasseIdAsList(klasse.id)" :columns="colsKursunterricht">
					<template #body>
						<template v-if="schienSortierung === true">
							<div v-for="schiene of stundenplanManager().schieneGetMengeByKlasseId(klasse.id)" :key="schiene.id" :id="schiene.id > -1 ? `schiene-${schiene.hashCode().toString()}`: ''">
								<!-- Die Schienenzeile -->
								<div role="row" class="svws-ui-tr bg-light" :draggable="isDraggable()" @dragstart="onDrag(schiene, $event)" @dragend="onDrag(undefined)">
									<div role="cell" class="select-none svws-ui-td font-bold"> {{ schiene.bezeichnung }}</div>
									<div role="cell" class="select-none svws-ui-td">
										<div class="svws-ui-badge select-none flex items-center justify-center relative group cursor-grab"
											:class="{ 'cursor-grabbing': dragData !== undefined }" @dragstart="wochentyp = 0" draggable="true">
											<span class="group-hover:bg-white rounded-sm w-3 absolute top-1/2 transform -translate-y-1/2 left-0">
												<i-ri-draggable class="w-4 -ml-0.5 text-black opacity-60 group-hover:opacity-100 group-hover:text-black" />
											</span>
											<span class="pl-2">Allgemein</span>
										</div>
										<template v-if="stundenplanManager().getWochenTypModell() > 0">
											<div v-for="i in stundenplanManager().getWochenTypModell()" :key="i" @dragstart="wochentyp = i" class="svws-ui-badge select-none flex items-center justify-center relative group cursor-grab"
												:class="{ 'cursor-grabbing': dragData !== undefined }" draggable="true">
												<span class="group-hover:bg-white rounded-sm w-3 absolute top-1/2 transform -translate-y-1/2 left-0">
													<i-ri-draggable class="w-4 -ml-0.5 text-black opacity-60 group-hover:opacity-100 group-hover:text-black" />
												</span>
												<span class="px-3">{{ String.fromCharCode(64 + i) }}</span>
											</div>
										</template>
									</div>
									<div role="cell" class="select-none svws-ui-td" />
								</div>
								<!-- Die Kurszeilen -->
								<div v-for="kurs in stundenplanManager().kursGetMengeByKlasseIdAndSchieneId(klasse.id, schiene.id)" :key="kurs.id" role="row" class="svws-ui-tr"
									:draggable="isDraggable()" @dragstart="onDrag(kurs, $event)" @dragend="onDrag(undefined)"
									:style="`--background-color: ${(stundenplanManager().kursGetWochenstundenREST(kurs.id) > 0) ? getBgColor(stundenplanManager().fachGetByIdOrException(kurs.idFach).kuerzelStatistik) : ''}`">
									<div role="cell" class="select-none svws-ui-td">
										<span :id="`kurs-${kurs.id}`" class="line-clamp-1 pl-3">{{ kurs.bezeichnung }}</span>
									</div>
									<div role="cell" class="select-none svws-ui-td">
										<div class="svws-ui-badge select-none flex items-center justify-center relative group cursor-grab"
											:class="{ 'cursor-grabbing': dragData !== undefined }" @dragstart="wochentyp = 0" draggable="true">
											<span class="group-hover:bg-white rounded-sm w-3 absolute top-1/2 transform -translate-y-1/2 left-0">
												<i-ri-draggable class="w-4 -ml-0.5 text-black opacity-60 group-hover:opacity-100 group-hover:text-black" />
											</span>
											<span class="pl-2">Allgemein</span>
										</div>
										<template v-if="stundenplanManager().getWochenTypModell() > 0">
											<div v-for="i in stundenplanManager().getWochenTypModell()" :key="i" @dragstart="wochentyp = i" class="svws-ui-badge select-none flex items-center justify-center relative group cursor-grab"
												:class="{ 'cursor-grabbing': dragData !== undefined }" draggable="true">
												<span class="group-hover:bg-white rounded-sm w-3 absolute top-1/2 transform -translate-y-1/2 left-0">
													<i-ri-draggable class="w-4 -ml-0.5 text-black opacity-60 group-hover:opacity-100 group-hover:text-black" />
												</span>
												<span class="px-3">{{ String.fromCharCode(64 + i) }}</span>
											</div>
										</template>
									</div>
									<div role="cell" class="select-none svws-ui-td svws-align-center">
										<span :class="{'rounded-sm bg-red-400': stundenplanManager().kursGetWochenstundenREST(kurs.id) < 0}">{{ stundenplanManager().kursGetWochenstundenIST(kurs.id) }}/{{ stundenplanManager().kursGetWochenstundenSOLL(kurs.id) }}</span>
									</div>
								</div>
							</div>
						</template>
						<template v-if="schienSortierung === false">
							<div v-for="kurs in stundenplanManager().kursGetMengeByKlasseIdAsList(klasse.id)" :key="kurs.id" role="row" class="svws-ui-tr"
								:draggable="isDraggable()" @dragstart="onDrag(kurs, $event)" @dragend="onDrag(undefined)"
								:style="`--background-color: ${(stundenplanManager().kursGetWochenstundenREST(kurs.id) > 0) ? getBgColor(stundenplanManager().fachGetByIdOrException(kurs.idFach).kuerzelStatistik) : ''}`">
								<div role="cell" class="select-none svws-ui-td">
									<span :id="`kurs-${kurs.id}`" class="line-clamp-1">{{ kurs.bezeichnung }}</span>
								</div>
								<div role="cell" class="select-none svws-ui-td">
									<div class="svws-ui-badge select-none flex items-center justify-center relative group cursor-grab"
										:class="{ 'cursor-grabbing': dragData !== undefined }" @dragstart="wochentyp = 0" draggable="true">
										<span class="group-hover:bg-white rounded-sm w-3 absolute top-1/2 transform -translate-y-1/2 left-0">
											<i-ri-draggable class="w-4 -ml-0.5 text-black opacity-60 group-hover:opacity-100 group-hover:text-black" />
										</span>
										<span class="pl-2">Allgemein</span>
									</div>
									<template v-if="stundenplanManager().getWochenTypModell() > 0">
										<div v-for="i in stundenplanManager().getWochenTypModell()" :key="i" @dragstart="wochentyp = i" class="svws-ui-badge select-none flex items-center justify-center relative group cursor-grab"
											:class="{ 'cursor-grabbing': dragData !== undefined }" draggable="true">
											<span class="group-hover:bg-white rounded-sm w-3 absolute top-1/2 transform -translate-y-1/2 left-0">
												<i-ri-draggable class="w-4 -ml-0.5 text-black opacity-60 group-hover:opacity-100 group-hover:text-black" />
											</span>
											<span class="px-3">{{ String.fromCharCode(64 + i) }}</span>
										</div>
									</template>
								</div>
								<div role="cell" class="select-none svws-ui-td svws-align-center">
									<span :class="{'rounded-sm bg-red-400': stundenplanManager().kursGetWochenstundenREST(kurs.id) < 0}">{{ stundenplanManager().kursGetWochenstundenIST(kurs.id) }}/{{ stundenplanManager().kursGetWochenstundenSOLL(kurs.id) }}</span>
								</div>
							</div>
						</template>
					</template>
				</svws-ui-table>
			</div>
			<!--TODO: Hier kommt das Zeitraster des Stundenplans hin, in welches von der linken Seite die Kurs-Unterrichte oder die Klassen-Unterricht hineingezogen werden können.-->
			<stundenplan-ansicht mode="klasse" mode-pausenaufsichten="tooltip" :id="klasse.id" :manager="stundenplanManager" :wochentyp="()=>wochentypAnzeige" :kalenderwoche="() => undefined"
				use-drag-and-drop :drag-data="() => dragData" :on-drag="onDrag" :on-drop="onDrop" class="h-full overflow-scroll pr-4" />
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { List, StundenplanKlasse } from "@core";
	import type { StundenplanKlasseProps } from "./SStundenplanKlasseProps";
	import type { StundenplanAnsichtDragData, StundenplanAnsichtDropZone } from "@comp";
	import { ArrayList, StundenplanKurs, StundenplanKlassenunterricht, ZulaessigesFach, StundenplanUnterricht, StundenplanZeitraster, HashSet, StundenplanSchiene } from "@core";
	import { ref, computed, onMounted } from "vue";
	import { cast_java_util_List } from "../../../../../core/src/java/util/List";

	const props = defineProps<StundenplanKlasseProps>();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const _klasse = ref<StundenplanKlasse | undefined>(undefined);
	const wochentyp = ref<number>(0);
	const wochentypAnzeige = ref<number>(0);
	const doppelstundenModus = ref<boolean>(false);
	const schienSortierung = ref<boolean>(true);

	const klasse = computed<StundenplanKlasse>({
		get: () : StundenplanKlasse => {
			if (_klasse.value !== undefined)
				try {
					return props.stundenplanManager().klasseGetByIdOrException(_klasse.value.id);
				} catch (e) { /* empty */ }
			return props.stundenplanManager().klasseGetMengeAsList().get(0);
		},
		set: (value : StundenplanKlasse) => _klasse.value = value
	});

	function getBgColor(fach: string): string {
		return ZulaessigesFach.getByKuerzelASD(fach).getHMTLFarbeRGB();
	}

	function wochentypen(): List<number> {
		let modell = props.stundenplanManager().getWochenTypModell();
		if (modell <= 1)
			modell = 0;
		const result = new ArrayList<number>();
		for (let n = 0; n <= modell; n++)
			result.add(n);
		return result;
	}

	const dragData = ref<StundenplanAnsichtDragData>(undefined);

	function onDrag(data: StundenplanAnsichtDragData, event?: DragEvent) {
		dragData.value = data;
		if (event === undefined)
			return;
		let id = '';
		if (data instanceof StundenplanKlassenunterricht)
			id = `klasse-${data.idFach}-${data.idKlasse}`;
		else if (data instanceof StundenplanKurs)
			id = `kurs-${data.id}`;
		else if (dragData.value instanceof StundenplanSchiene || dragData.value?.isTranspiledInstanceOf("java.util.List"))
			id = `schiene-${dragData.value.hashCode().toString()}`;
		const element = document.getElementById(id);
		if ((element !== null) && (event.dataTransfer !== null))
			event.dataTransfer?.setDragImage(element, 0, 0);
	}

	async function onDrop(zone: StundenplanAnsichtDropZone) {
		// else if oder return der api-methode, sonst wird weiter geprüft
		if (dragData.value === undefined)
			return;
		// Fall StundenplanUnterricht -> StundenplanZeitraster
		if ((dragData.value instanceof StundenplanUnterricht) && (zone instanceof StundenplanZeitraster) && props.stundenplanManager().unterrichtIstVerschiebenErlaubt(dragData.value, zone))
			return await props.patchUnterricht([dragData.value], zone);
		// Fall List<StundenplanUnterricht> -> StundenplanZeitraster
		// Fall List<StundenplanKurs> -> StundenplanZeitraster
		// Fall List<StundenplanUnterricht> -> undefined
		if (dragData.value.isTranspiledInstanceOf("java.util.List")) {
			const listStundenplanUnterricht = new ArrayList<StundenplanUnterricht>();
			const listStundenplanKurs = new ArrayList<StundenplanKurs>();
			const casted: List<unknown> = cast_java_util_List(dragData.value);
			for (const item of casted)
				if (item instanceof StundenplanUnterricht && zone instanceof StundenplanZeitraster && props.stundenplanManager().unterrichtIstVerschiebenErlaubt(item, zone))
					listStundenplanUnterricht.add(item);
				else if (item instanceof StundenplanUnterricht && zone === undefined)
					listStundenplanUnterricht.add(item);
				else if (item instanceof StundenplanKurs && zone instanceof StundenplanZeitraster && props.stundenplanManager().kursDarfInZelle(item, zone.wochentag, zone.unterrichtstunde, wochentyp.value))
					listStundenplanKurs.add(item);
			if (listStundenplanKurs.size() > 0)
				return await props.addUnterrichtKlasse(listStundenplanKurs);
			if (listStundenplanUnterricht.size() > 0)
				if (zone instanceof StundenplanZeitraster)
					return await props.patchUnterricht(listStundenplanUnterricht, zone);
				else if (zone === undefined)
					return await props.removeUnterrichtKlasse(listStundenplanUnterricht);
		}
		// Fall StundenplanKlassenunterricht -> StundenplanZeitraster
		if ((dragData.value instanceof StundenplanKlassenunterricht) && (zone instanceof StundenplanZeitraster) && props.stundenplanManager().klassenunterrichtDarfInZelle(dragData.value, zone.wochentag, zone.unterrichtstunde, wochentyp.value)) {
			const klassen = new ArrayList<number>();
			klassen.add(dragData.value.idKlasse);
			const stunde = { idZeitraster: zone.id, wochentyp: wochentyp.value, idKurs: null, idFach: dragData.value.idFach, klassen, lehrer: dragData.value.lehrer, schienen: dragData.value.schienen };
			const arr = [];
			arr.push(stunde);
			if (doppelstundenModus.value === true && props.stundenplanManager().klassenunterrichtGetWochenstundenREST(klasse.value.id, dragData.value.idFach) >= 2) {
				const next = props.stundenplanManager().getZeitrasterNext(zone);
				if (next && props.stundenplanManager().klassenunterrichtDarfInZelle(dragData.value, zone.wochentag, next.unterrichtstunde, wochentyp.value))
					arr.push({ idZeitraster: next.id, wochentyp: wochentyp.value, idKurs: null, idFach: dragData.value.idFach, klassen, lehrer: dragData.value.lehrer, schienen: dragData.value.schienen });
			}
			await props.addUnterrichtKlasse(arr);
			return;
		}
		// Fall StundenplanUnterricht -> undefined
		if ((dragData.value instanceof StundenplanUnterricht) && (zone === undefined))
			return await props.removeUnterrichtKlasse([dragData.value]);
		// TODO Fall StundenplanKurs -> StundenplanZeitraster
		if ((dragData.value instanceof StundenplanKurs) && (zone instanceof StundenplanZeitraster) && props.stundenplanManager().kursDarfInZelle(dragData.value, zone.wochentag, zone.unterrichtstunde, wochentyp.value)) {
			const klassen = new  HashSet<number>();
			const listSchueler = props.stundenplanManager().schuelerGetMengeByKursIdAsListOrException(dragData.value.id);
			for (const schueler of listSchueler)
				klassen.add(schueler.idKlasse);
			const stunde = { idZeitraster: zone.id, wochentyp: wochentyp.value, idKurs: dragData.value.id, idFach: dragData.value.idFach, klassen: new ArrayList(klassen), schienen: dragData.value.schienen, lehrer: dragData.value.lehrer };
			const arr = [];
			arr.push(stunde);
			//stundenplanManager().klassenunterrichtGetWochenstundenIST(ku.idKlasse, ku.idFach) }}/{{ stundenplanManager().klassenunterrichtGetWochenstundenSOLL(ku.idKlasse, ku.idFach)
			if (doppelstundenModus.value === true && props.stundenplanManager().kursGetWochenstundenREST(dragData.value.id) >= 2) {
				const next = props.stundenplanManager().getZeitrasterNext(zone);
				if (next && props.stundenplanManager().kursDarfInZelle(dragData.value, zone.wochentag, next.unterrichtstunde, wochentyp.value))
					arr.push({ idZeitraster: next.id, wochentyp: wochentyp.value, idKurs: dragData.value.id, idFach: dragData.value.idFach, klassen: new ArrayList(klassen), schienen: dragData.value.schienen, lehrer: dragData.value.lehrer });
			}
			return await props.addUnterrichtKlasse(arr);
		}
		// Fall StundenplanSchiene -> StundenplanZeitraster
		if (dragData.value instanceof StundenplanSchiene) {
			const listStundenplanKursRaw = props.stundenplanManager().kursGetMengeByKlasseIdAndSchieneId(klasse.value.id, dragData.value.id);
			const arr = [];
			for (const kurs of listStundenplanKursRaw) {
				if ((zone instanceof StundenplanZeitraster) && props.stundenplanManager().kursDarfInZelle(kurs, zone.wochentag, zone.unterrichtstunde, wochentyp.value)) {
					const klassen = new  HashSet<number>();
					const listSchueler = props.stundenplanManager().schuelerGetMengeByKursIdAsListOrException(kurs.id);
					for (const schueler of listSchueler)
						klassen.add(schueler.idKlasse);
					const stunde = { idZeitraster: zone.id, wochentyp: wochentyp.value, idKurs: kurs.id, idFach: kurs.idFach, klassen: new ArrayList(klassen), schienen: kurs.schienen, lehrer: kurs.lehrer };
					arr.push(stunde);
					if ((doppelstundenModus.value === true) && (props.stundenplanManager().kursGetWochenstundenREST(kurs.id) >= 2)) {
						const next = props.stundenplanManager().getZeitrasterNext(zone);
						if (next && (props.stundenplanManager().kursDarfInZelle(kurs, zone.wochentag, next.unterrichtstunde, wochentyp.value)))
							arr.push({ idZeitraster: next.id, wochentyp: wochentyp.value, idKurs: kurs.id, idFach: kurs.idFach, klassen: new ArrayList(klassen), schienen: kurs.schienen, lehrer: kurs.lehrer });
					}
				}
			}
			await props.addUnterrichtKlasse(arr);
			return;
		}
		// TODO Fall StundenplanZeitraster -> undefined
		// TODO Fall StundenplanPausenaufsicht -> StundenplanPausenzeit
		// TODO Fall StundenplanPausenaufsicht -> undefined
		// TODO Fall Lehrer -> StundenplanPausenzeit
	}

	function isDraggable() : boolean {
		return dragData.value === undefined;
	}

	function isDropZone() : boolean {
		if ((dragData.value === undefined) || (dragData.value instanceof StundenplanKurs) || (dragData.value instanceof StundenplanKlassenunterricht))
			return false;
		return true;
	}

	function checkDropZone(event: DragEvent) {
		if (isDropZone())
			event.preventDefault();
	}

	const colsKlassenunterricht: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Klassenunterricht", tooltip: "Klassenunterricht", span: 1 },
		{ key: "irgendwas", label: "Wochentyp", span: 2 },
		{ key: "wochenstunden", label: "WS", tooltip: "Wochenstunden", fixedWidth: 3, align: "center" }
	];

	const colsKursunterricht: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Kursunterricht", tooltip: "Klassenunterricht", span: 1 },
		{ key: "irgendwas", label: "Wochentyp", span: 2 },
		{ key: "wochenstunden", label: "WS", tooltip: "Wochenstunden", fixedWidth: 3, align: "center" }
	];

</script>

<style lang="postcss" scoped>
	.page--content {
		@apply grid overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 lg:gap-x-8;
		grid-auto-rows: 100%;
		grid-template-columns: minmax(20rem, 0.5fr) 2fr;
		grid-auto-columns: max-content;
	}

</style>
