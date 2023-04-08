<template>
	<div v-if="belegungsHinweise.size()">
		<h4 class="flex font-bold mt-5">Informationen zur Laufbahn</h4>
		<ul v-if="belegungsHinweise.size()" class="mt-1 flex flex-col gap-1.5">
			<li v-for="fehler in belegungsHinweise" :key="fehler.code" class="flex gap-1 leading-tight">
				<i-ri-information-line class="flex-shrink-0 text-primary" />
				{{ fehler.beschreibung }}
			</li>
		</ul>
	</div>
</template>

<script setup lang="ts">

	import { type List, type GostBelegpruefungErgebnisFehler, ArrayList, GostBelegungsfehlerArt } from "@svws-nrw/svws-core";
	import { type ComputedRef, computed } from 'vue';

	const props = defineProps<{
		fehlerliste: List<GostBelegpruefungErgebnisFehler>;
	}>();

	const belegungsHinweise: ComputedRef<List<GostBelegpruefungErgebnisFehler>> = computed(() => {
		const res = new ArrayList<GostBelegpruefungErgebnisFehler>();
		for (const fehler of props.fehlerliste)
			if (!!fehler && GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.HINWEIS)
				res.add(fehler);
		return res;
	});

</script>
