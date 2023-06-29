<template>
	<div v-if="sprachendaten().pruefungen.size()" class="inline-block py-2 align-middle sm:px-6 lg:px-8 w-full">
		<div class="overflow-hidden rounded-lg shadow">
			<table class="border-collapse text-sm w-full">
				<thead class="bg-slate-100">
					<tr>
						<td colspan="5" class="px-2">Sprachprüfungen</td>
					</tr>
					<tr>
						<td class="px-2">Sprache</td>
						<td class="px-2">Typ</td>
						<td class="px-2">Niveau</td>
						<td class="px-2">Ersetzt</td>
						<td class="px-2 text-center">Note</td>
					</tr>
				</thead>
				<tbody>
					<tr v-for="pruefung in sprachendaten().pruefungen" :key="pruefung.sprache || ''" class="border bottom-1  border-[#7f7f7f]/20">
						<td class="px-2">{{ pruefung.sprache }}</td>
						<td class="px-2">{{ pruefung.istHSUPruefung ? "HSU":'' }}{{ pruefung.istFeststellungspruefung ? 'SFP':'' }}</td>
						<td class="px-2">{{ Sprachpruefungniveau.getByID(pruefung.anspruchsniveauId || null)?.daten.beschreibung }}</td>
						<td class="px-2">{{ pruefung.istHSUPruefung?'–':'' }}{{ pruefung.kannErstePflichtfremdspracheErsetzen ? 'Erste Fremdsprache':'' }}{{ pruefung.kannZweitePflichtfremdspracheErsetzen ? 'Zweite Fremdsprache':'' }}{{ pruefung.kannWahlpflichtfremdspracheErsetzen ? 'Wahlpflichtfremdsprache':'' }}</td>
						<td class="text-center">{{ pruefung.note }}</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { Sprachendaten} from "@core";
	import { Sprachpruefungniveau } from "@core";

	const props = defineProps<{
		sprachendaten: () => Sprachendaten;
	}>();

</script>