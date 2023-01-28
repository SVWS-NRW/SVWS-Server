<template>
	<table class="border-collapse text-sm">
		<thead class="bg-slate-100">
			<tr>
				<td class="px-2">
					Laufbahnfehler
					<svws-ui-badge v-if="belegungsfehler.size()" type="error" size="tiny">
						{{ belegungsfehler.size() }}
					</svws-ui-badge>
				</td>
			</tr>
		</thead>
		<tbody>
			<tr v-for="fehler in belegungsfehler" :key="fehler.code.toString()" class="border border-[#7f7f7f]/20 text-left">
				<td v-if="belegungsfehler.size()" class="px-2"> {{ fehler.beschreibung }} </td>
			</tr>
			<tr v-if="!belegungsfehler.size()">
				<td class="px-2">Keine</td>
			</tr>
		</tbody>
	</table>
</template>

<script setup lang="ts">

	import { List, GostBelegpruefungErgebnisFehler, Vector, GostBelegungsfehlerArt } from '@svws-nrw/svws-core-ts';
	import { ComputedRef, computed } from 'vue';

	const props = defineProps<{
		fehlerliste: List<GostBelegpruefungErgebnisFehler>;
	}>();

	const belegungsfehler: ComputedRef<List<GostBelegpruefungErgebnisFehler>> = computed(() => {
		const res = new Vector<GostBelegpruefungErgebnisFehler>();
		for (const fehler of props.fehlerliste)
			if (!!fehler &&
				(GostBelegungsfehlerArt.fromKuerzel(fehler.art) ===
					GostBelegungsfehlerArt.BELEGUNG ||
					GostBelegungsfehlerArt.fromKuerzel(fehler.art) ===
					GostBelegungsfehlerArt.SCHRIFTLICHKEIT))
				res.add(fehler);
		return res;
	});

</script>