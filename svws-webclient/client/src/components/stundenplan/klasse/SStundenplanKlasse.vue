<template>
	<div class="page--content page--content--full">
		<template v-if="props.stundenplanManager().klasseGetMengeAsList().isEmpty()">
			<span>Für diesen Stundenplan ist keine Klasse vorhanden.</span>
		</template>
		<template v-else>
			<div @dragover="checkDropZone($event)" @drop="onDrop(undefined)" class="flex flex-col justify-start mb-auto svws-table-offset">
				<svws-ui-input-wrapper class="mb-10">
					<svws-ui-select title="Klasse" v-model="klasse" :items="stundenplanManager().klasseGetMengeAsList()" :item-text="(i: StundenplanKlasse) => i.kuerzel" autocomplete
						:item-filter="(i: StundenplanKlasse[], text: string)=> i.filter(k=>k.kuerzel.includes(text.toLocaleLowerCase()))" :item-sort="()=>0" />
					<svws-ui-select title="Wochentyp" v-model="wochentypAuswahl" :items="wochentypen()"
						class="print:hidden"
						:disabled="wochentypen().size() <= 0"
						:item-text="(wt: number) => stundenplanManager().stundenplanGetWochenTypAsString(wt)" />
				</svws-ui-input-wrapper>
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
				<svws-ui-table :items="stundenplanManager().kursGetMengeByKlasseIdAsList(klasse.id)" :columns="colsKursunterricht">
					<template #body>
						<div v-for="kurs in stundenplanManager().kursGetMengeByKlasseIdAsList(klasse.id)" :key="kurs.id" role="row" class="svws-ui-tr"
							:draggable="isDraggable()" @dragstart="onDrag(kurs, $event)" @dragend="onDrag(undefined)"
							:style="`--background-color: ${(stundenplanManager().kursGetWochenstundenSOLL(kurs.id) - stundenplanManager().kursGetWochenstundenIST(kurs.id) > 0) ? getBgColor(stundenplanManager().fachGetByIdOrException(kurs.idFach).kuerzelStatistik) : ''}`">
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
								<span :class="{'rounded-sm bg-red-400': stundenplanManager().kursGetWochenstundenSOLL(kurs.id) - stundenplanManager().kursGetWochenstundenIST(kurs.id) < 0}">{{ stundenplanManager().kursGetWochenstundenIST(kurs.id) }}/{{ stundenplanManager().kursGetWochenstundenSOLL(kurs.id) }}</span>
							</div>
						</div>
					</template>
				</svws-ui-table>
			</div>
			<!--TODO: Hier kommt das Zeitraster des Stundenplans hin, in welches von der linken Seite die Kurs-Unterrichte oder die Klassen-Unterricht hineingezogen werden können.-->
			<stundenplan-ansicht mode="klasse" mode-pausenaufsichten="tooltip" :id="klasse.id" :manager="stundenplanManager" :wochentyp="() => wochentypAuswahl" :kalenderwoche="() => undefined"
				use-drag-and-drop :drag-data="() => dragData" :on-drag="onDrag" :on-drop="onDrop" />
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { List, StundenplanKlasse } from "@core";
	import type { StundenplanKlasseProps } from "./SStundenplanKlasseProps";
	import type { StundenplanAnsichtDragData, StundenplanAnsichtDropZone } from "@comp";
	import { ArrayList, StundenplanKurs, StundenplanKlassenunterricht, ZulaessigesFach, StundenplanUnterricht, StundenplanZeitraster } from "@core";
	import { ref, computed } from "vue";
	import { cast_java_util_List } from "../../../../../core/src/java/util/List";

	const props = defineProps<StundenplanKlasseProps>();

	const _klasse = ref<StundenplanKlasse | undefined>(undefined);
	const wochentyp = ref<number>(-1);

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
		else if (dragData.value?.isTranspiledInstanceOf("java.util.List"))
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
		if ((dragData.value instanceof StundenplanUnterricht) && (zone instanceof StundenplanZeitraster))
			return await props.patchUnterricht([dragData.value], zone);
		// Fall List<StundenplanUnterricht> -> StundenplanZeitraster
		// Fall List<StundenplanKurs> -> StundenplanZeitraster
		// Fall List<StundenplanUnterricht> -> undefined
		if (dragData.value.isTranspiledInstanceOf("java.util.List")) {
			const listStundenplanUnterricht = new ArrayList<StundenplanUnterricht>();
			const listStundenplanKurs = new ArrayList<StundenplanKurs>();
			const casted: List<unknown> = cast_java_util_List(dragData.value);
			for (const item of casted)
				if (item instanceof StundenplanUnterricht)
					listStundenplanUnterricht.add(item);
				else if (item instanceof StundenplanKurs)
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
		if ((dragData.value instanceof StundenplanKlassenunterricht) && (zone instanceof StundenplanZeitraster))
			return await props.addUnterrichtKlasse([{ idZeitraster: zone.id, wochentyp: wochentyp.value, idKurs: null, idFach: dragData.value.idFach }]);
		// Fall StundenplanUnterricht -> undefined
		if ((dragData.value instanceof StundenplanUnterricht) && (zone === undefined))
			return await props.removeUnterrichtKlasse([dragData.value]);
		// TODO Fall StundenplanKurs -> StundenplanZeitraster
		if ((dragData.value instanceof StundenplanKurs) && (zone instanceof StundenplanZeitraster))
			return await props.addUnterrichtKlasse([{ idZeitraster: zone.id, wochentyp: wochentyp.value, idKurs: dragData.value.id, idFach: dragData.value.idFach }]);
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

	const _wochentyp = ref<number>(0);

	const wochentypAuswahl = computed<number>({
		get: () : number => _wochentyp.value,
		set: (value : number) => _wochentyp.value = value
	});

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
  @apply grid;
  grid-template-columns: minmax(20rem, 0.5fr) 2fr;
}
</style>
