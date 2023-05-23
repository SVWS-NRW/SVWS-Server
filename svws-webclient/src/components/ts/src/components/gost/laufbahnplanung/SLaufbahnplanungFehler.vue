<template>
	<div>
		<h4 class="gap-1 flex items-center" :class="{'font-bold mt-5': belegungsfehler.size()}">
			<i-ri-checkbox-circle-line v-if="!belegungsfehler.size()" class="flex-shrink-0" style="color: rgb(var(--color-success))" />
			<span v-if="!belegungsfehler.size()">Keine</span>
			<span>Laufbahnfehler</span>
			<span v-if="belegungsfehler.size()">{{ belegpruefungsArt === 'ef1' ? 'EF1' : 'Gesamt' }}</span>
			<svws-ui-badge v-if="belegungsfehler.size()" type="error">
				{{ belegungsfehler.size() }}
			</svws-ui-badge>
		</h4>
		<ul class="mt-1 flex flex-col gap-1.5" v-if="belegungsfehler.size()">
			<li v-for="fehler in belegungsfehler" :key="fehler.code" class="flex gap-1 leading-tight">
				<i-ri-alert-line class="flex-shrink-0 text-error" />
				{{ fehler.beschreibung }}
			</li>
		</ul>
	</div>
</template>

<script setup lang="ts">

	import type { List, GostBelegpruefungErgebnisFehler} from "@svws-nrw/svws-core";
	import { ArrayList, GostBelegungsfehlerArt } from "@svws-nrw/svws-core";
	import type { ComputedRef} from 'vue';
	import { computed } from 'vue';

	const props = defineProps<{
		fehlerliste: List<GostBelegpruefungErgebnisFehler>;
		belegpruefungsArt: 'gesamt' | 'ef1'
	}>();

	const belegungsfehler: ComputedRef<List<GostBelegpruefungErgebnisFehler>> = computed(() => {
		const res = new ArrayList<GostBelegpruefungErgebnisFehler>();
		for (const fehler of props.fehlerliste)
			if (!!fehler &&
				(GostBelegungsfehlerArt.fromKuerzel(fehler.art) ===
					GostBelegungsfehlerArt.BELEGUNG ||
					GostBelegungsfehlerArt.fromKuerzel(fehler.art) ===
					GostBelegungsfehlerArt.SCHRIFTLICHKEIT ||
					GostBelegungsfehlerArt.fromKuerzel(fehler.art) ===
					GostBelegungsfehlerArt.SCHULSPEZIFISCH))
				res.add(fehler);
		return res;
	});

</script>
