<template>
	<div class="h-full w-full p-8 flex flex-row flex-grow">
		<template v-if="props.stundenplanManager().klasseGetMengeAsList().isEmpty()">
			Für den Stundenplan ist keine Klasse vorhanden.
		</template>
		<template v-else>
			<div class="h-full w-96 mr-2 grid grid-cols-1" style="grid-template-rows: 1.8rem 1.8rem 1fr 1.8rem 1fr;">
				<div>
					<svws-ui-multi-select title="Klasse" v-model="klasse" :items="stundenplanManager().klasseGetMengeAsList()" :item-text="(i: StundenplanKlasse) => i.kuerzel" />
				</div>
				<div>Klassenunterricht</div>
				<svws-ui-data-table :items="stundenplanManager().klassenunterrichtGetMengeByKlasseId(klasse.id)" :columns="cols">
					<template #body>
						<div v-for="ku in stundenplanManager().klassenunterrichtGetMengeByKlasseId(klasse.id)" :key="ku.idKlasse + '/' + ku.idFach" role="row" class="data-table__tr data-table__tbody__tr">
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
						<div v-for="kurs in stundenplanManager().kursGetMengeAsList()" :key="kurs.id" role="row" class="data-table__tr data-table__tbody__tr">
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
				<stundenplan-ansicht mode="klasse" :id="klasse.id" :manager="stundenplanManager" :wochentyp="() => 0" :kalenderwoche="() => undefined" />
			</div>
		</template>
	</div>
</template>

<script setup lang="ts">

	import { type StundenplanKlasse } from "@core";
	import { type StundenplanKlasseProps } from "./SStundenplanKlasseProps";
	import { ref, computed, type WritableComputedRef } from "vue";

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

	const cols = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "wochenstunden", label: "WS", span: 1, sortable: false }
	];

</script>
