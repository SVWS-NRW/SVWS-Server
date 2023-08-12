<template>
	<div class="h-full w-full p-8 flex flex-row flex-grow">
		<div class="h-full w-96 mr-2 grid grid-cols-1" style="grid-template-rows: 1.8rem 1fr 1.8rem 1fr;">
			<div>Klassenunterricht</div>
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
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanUnterrichtProps } from "./SStundenplanUnterrichtProps";

	const props = defineProps<StundenplanUnterrichtProps>();

	const cols = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "wochenstunden", label: "WS", span: 1, sortable: false }
	];

</script>
