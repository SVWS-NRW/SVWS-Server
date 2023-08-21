<template>
	<div class="h-full w-full p-8 flex flex-row flex-grow">
		<template v-if="props.stundenplanManager().klasseGetMengeAsList().isEmpty()">
			Für den Stundenplan ist keine Klasse vorhanden.
		</template>
		<template v-else>
			<div class="h-full w-96 mr-2 grid grid-cols-1" style="grid-template-rows: 3rem 3rem 1.8rem 1fr 1.8rem 1fr;"
				@dragover="checkDropZone($event)" @drop="onDrop(undefined)">
				<div>
					<svws-ui-multi-select title="Klasse" v-model="klasse" :items="stundenplanManager().klasseGetMengeAsList()" :item-text="(i: StundenplanKlasse) => i.kuerzel" />
				</div>
				<div>
					<svws-ui-multi-select title="Wochentyp" v-model="wochentypAuswahl" :items="wochentypen()"
						class="print:hidden"
						:disabled="wochentypen().size() <= 0"
						:item-text="(wt: number) => stundenplanManager().stundenplanGetWochenTypAsString(wt)" />
				</div>
				<div>Klassenunterricht</div>
				<svws-ui-data-table :items="stundenplanManager().klassenunterrichtGetMengeByKlasseIdAsList(klasse.id)" :columns="cols">
					<template #body>
						<div v-for="ku in stundenplanManager().klassenunterrichtGetMengeByKlasseIdAsList(klasse.id)" :key="ku.idKlasse + '/' + ku.idFach" role="row" class="data-table__tr data-table__tbody__tr"
							:draggable="isDraggable()" @dragstart="onDrag(ku)" @dragend="onDrag(undefined)">
							<div role="cell" class="select-none data-table__td">
								{{ ku.bezeichnung }}
							</div>
							<div role="cell" class="select-none data-table__td">
								{{ ku.wochenstunden }}
							</div>
						</div>
					</template>
				</svws-ui-data-table>
				<div>Kursunterricht</div>
				<svws-ui-data-table :items="stundenplanManager().kursGetMengeAsList()" :columns="cols">
					<template #body>
						<div v-for="kurs in stundenplanManager().kursGetMengeAsList()" :key="kurs.id" role="row" class="data-table__tr data-table__tbody__tr"
							:draggable="isDraggable()" @dragstart="onDrag(kurs)" @dragend="onDrag(undefined)">
							<div role="cell" class="select-none data-table__td">
								{{ kurs.bezeichnung }}
							</div>
							<div role="cell" class="select-none data-table__td">
								{{ kurs.wochenstunden }}
							</div>
						</div>
					</template>
				</svws-ui-data-table>
			</div>
			<div class="h-full w-full">
				TODO: Hier kommt das Zeitraster des Stundenplans hin, in welches von der linken Seite die Kurs-Unterrichte oder
				die Klassen-Unterricht hineingezogen werden können.
				<stundenplan-ansicht mode="klasse" :id="klasse.id" :manager="stundenplanManager" :wochentyp="() => wochentypAuswahl" :kalenderwoche="() => undefined"
					use-drag-and-drop :drag-data="() => dragData" :on-drag="onDrag" :on-drop="onDrop" />
			</div>
		</template>
	</div>
</template>

<script setup lang="ts">

	import { ArrayList, type List, type StundenplanKlasse, StundenplanKurs, StundenplanKlassenunterricht } from "@core";
	import { type StundenplanKlasseProps } from "./SStundenplanKlasseProps";
	import { ref, computed, type WritableComputedRef } from "vue";
	import { type StundenplanAnsichtDragData, type StundenplanAnsichtDropZone } from "@comp";

	const props = defineProps<StundenplanKlasseProps>();

	const _klasse = ref<StundenplanKlasse | undefined>(undefined);

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

	const onDrag = (data: StundenplanAnsichtDragData) => {
		dragData.value = data;
		// console.log("drag", data);
	};

	const onDrop = (zone: StundenplanAnsichtDropZone) => {
		// console.log("drop", zone);
		// TODO Fall StundenplanKlassenunterricht -> StundenplanZeitraster
		// TODO Fall StundenplanKurs -> StundenplanZeitraster
		// TODO Fall StundenplanUnterricht -> StundenplanZeitraster
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

	const cols = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "wochenstunden", label: "WS", span: 1, sortable: false }
	];

</script>
