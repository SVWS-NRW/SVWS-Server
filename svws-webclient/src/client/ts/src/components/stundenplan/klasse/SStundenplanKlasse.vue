<template>
	<div class="page--content page--content--full">
		<template v-if="props.stundenplanManager().klasseGetMengeAsList().isEmpty()">
			<span>Für diesen Stundenplan ist keine Klasse vorhanden.</span>
		</template>
		<template v-else>
			<div @dragover="checkDropZone($event)" @drop="onDrop(undefined)" class="flex flex-col gap-y-8 justify-start mb-auto">
				<svws-ui-input-wrapper>
					<svws-ui-multi-select title="Klasse" v-model="klasse" :items="stundenplanManager().klasseGetMengeAsList()" :item-text="i => i.kuerzel" />
					<svws-ui-multi-select title="Wochentyp" v-model="wochentypAuswahl" :items="wochentypen()"
						class="print:hidden"
						:disabled="wochentypen().size() <= 0"
						:item-text="wt => stundenplanManager().stundenplanGetWochenTypAsString(wt)" />
				</svws-ui-input-wrapper>
				<svws-ui-data-table :items="stundenplanManager().klassenunterrichtGetMengeByKlasseIdAsList(klasse.id)" :columns="colsKlassenunterricht">
					<template #body>
						<div v-for="ku in stundenplanManager().klassenunterrichtGetMengeByKlasseIdAsList(klasse.id)" :key="ku.idKlasse + '/' + ku.idFach" role="row" class="data-table__tr data-table__tbody__tr"
							:draggable="isDraggable()" @dragstart="onDrag(ku, $event)" @dragend="onDrag(undefined)" :style="`background-color: ${getBgColor(stundenplanManager().unterrichtGetByIDStringOfFachOderKursKuerzel(ku.idFach).split('-')[0])}`">
							<div role="cell" class="select-none data-table__td">
								<span :id="`klasse-${ku.idFach}-${ku.idKlasse}`">{{ ku.bezeichnung }}</span>
							</div>
							<div role="cell" class="select-none data-table__td">
								{{ ku.wochenstunden }}
							</div>
						</div>
					</template>
				</svws-ui-data-table>
				<svws-ui-data-table :items="kursliste" :columns="colsKursunterricht">
					<template #body>
						<div v-for="kurs in kursliste" :key="kurs.id" role="row" class="data-table__tr data-table__tbody__tr"
							:draggable="isDraggable()" @dragstart="onDrag(kurs, $event)" @dragend="onDrag(undefined)" :style="`background-color: ${getBgColor(kurs.bezeichnung.split('-')[1])}`">
							<div role="cell" class="select-none data-table__td">
								<span :id="`kurs-${kurs.id}`">{{ kurs.bezeichnung }}</span>
							</div>
							<div role="cell" class="select-none data-table__td">
								{{ kurs.wochenstunden }}
							</div>
						</div>
					</template>
				</svws-ui-data-table>
			</div>
			<!--TODO: Hier kommt das Zeitraster des Stundenplans hin, in welches von der linken Seite die Kurs-Unterrichte oder die Klassen-Unterricht hineingezogen werden können.-->
			<stundenplan-ansicht mode="klasse" :id="klasse.id" :manager="stundenplanManager" :wochentyp="() => wochentypAuswahl" :kalenderwoche="() => undefined"
				use-drag-and-drop :drag-data="() => dragData" :on-drag="onDrag" :on-drop="onDrop" />
		</template>
	</div>
</template>

<script setup lang="ts">

	import { ArrayList, type List, type StundenplanKlasse, StundenplanKurs, StundenplanKlassenunterricht, ZulaessigesFach, StundenplanUnterricht, StundenplanZeitraster } from "@core";
	import { type StundenplanKlasseProps } from "./SStundenplanKlasseProps";
	import { ref, computed, type WritableComputedRef } from "vue";
	import { type StundenplanAnsichtDragData, type StundenplanAnsichtDropZone } from "@comp";

	const props = defineProps<StundenplanKlasseProps>();

	const _klasse = ref<StundenplanKlasse | undefined>(undefined);

	const kursliste = computed(()=>{
		const list = props.stundenplanManager().kursGetMengeByKlasseIdAsList(klasse.value.id);
		return list;
	})

	const klasse: WritableComputedRef<StundenplanKlasse> = computed({
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

	const onDrag = (data: StundenplanAnsichtDragData, event?: DragEvent) => {
		dragData.value = data;
		let id;
		if (data instanceof StundenplanKlassenunterricht)
			id = `klasse-${data.idFach}-${data.idKlasse}`
		else if (data instanceof StundenplanKurs)
			id = `kurs-${data.id}`
		if (id) {
			const img = document.getElementById(id);
			if (img && event?.dataTransfer)
				event.dataTransfer.setDragImage(img,0,0)
		}
		// console.log("drag", data);
	};

	const onDrop = async (zone: StundenplanAnsichtDropZone) => {
		console.log("drop", zone, dragData.value);
		// Fall StundenplanUnterricht -> StundenplanZeitraster
		if ((dragData.value instanceof StundenplanUnterricht) && (zone instanceof StundenplanZeitraster)) {
			await props.patchUnterricht(dragData.value, zone);
			console.log("Manager fehlt noch, neu laden")
		}
		// Fall StundenplanKlassenunterricht -> StundenplanZeitraster
		if ((dragData.value instanceof StundenplanKlassenunterricht) && (zone instanceof StundenplanZeitraster)) {
			await props.addUnterrichtKlasse(dragData.value, zone);
			console.log("API, Manager fehlt noch, neu laden")
		}
		// Fall StundenplanUnterricht -> undefined
		if ((dragData.value instanceof StundenplanUnterricht) && (zone === undefined)) {
			await props.removeUnterrichtKlasse([dragData.value]);
			console.log("API fehlt noch")
		}
		// TODO Fall StundenplanKurs -> StundenplanZeitraster
		// TODO Fall StundenplanZeitraster -> undefined
		// TODO Fall StundenplanPausenaufsicht -> StundenplanPausenzeit
		// TODO Fall StundenplanPausenaufsicht -> undefined
		// TODO Fall Lehrer -> StundenplanPausenzeit
	};

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

	const wochentypAuswahl : WritableComputedRef<number> = computed({
		get: () : number => _wochentyp.value,
		set: (value : number) => _wochentyp.value = value
	});

	const colsKlassenunterricht = [
		{ key: "bezeichnung", label: "Klassenunterricht", span: 2, sortable: false },
		{ key: "wochenstunden", label: "WS", tooltip: "Wochenstunden", span: 1, sortable: false }
	];

	const colsKursunterricht = [
		{ key: "bezeichnung", label: "Kursunterricht", span: 2, sortable: false },
		{ key: "wochenstunden", label: "WS", tooltip: "Wochenstunden", span: 1, sortable: false }
	];

</script>

<style lang="postcss" scoped>
.page--content {
  @apply grid;
  grid-template-columns: minmax(20rem, 0.5fr) 2fr;
}
</style>
