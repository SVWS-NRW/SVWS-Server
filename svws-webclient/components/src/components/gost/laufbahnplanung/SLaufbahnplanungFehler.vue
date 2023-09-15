<template>
	<svws-ui-table :items="[]" :no-data="belegungsfehler.size() === 0" no-data-text="Planung ist gültig." :columns="[{key: 'icon', label: '0', fixedWidth: 2.25, align: 'center'},{key: 'beschreibung', label: (belegungsfehler.size() === 0 ? 'Keine ' : '') + 'Laufbahnfehler ' + belegpruefungsArt().kuerzel}]" class="overflow-visible">
		<template #header(icon)>
			<span v-if="belegungsfehler.size() > 0" class="rounded w-[1.75rem] inline-flex items-center justify-center bg-error text-white border-2 border-error -m-1">{{ belegungsfehler.size() }}</span>
			<i-ri-checkbox-circle-fill v-else class="flex-shrink-0 text-success text-headline-md" />
		</template>
		<template #body v-if="belegungsfehler.size() > 0">
			<div v-for="fehler in belegungsfehler" :key="fehler.code" class="svws-ui-tr" role="row">
				<div class="svws-ui-td" role="cell">
					<svws-ui-tooltip>
						<i-ri-alert-line class="flex-shrink-0 text-error text-button mt-0.5" />
						<template #content>
							<span class="font-mono">
								{{ fehler.code }}
							</span>
						</template>
					</svws-ui-tooltip>
				</div>
				<div class="svws-ui-td leading-tight select-all" role="cell">
					{{ fehler.beschreibung }}
				</div>
			</div>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import type { List, GostBelegpruefungErgebnisFehler, GostBelegpruefungsArt } from "@core";
	import { ArrayList, GostBelegungsfehlerArt } from "@core";
	import type { ComputedRef} from 'vue';
	import { computed } from 'vue';

	const props = defineProps<{
		fehlerliste: () => List<GostBelegpruefungErgebnisFehler>;
		belegpruefungsArt: () => GostBelegpruefungsArt;
	}>();

	const belegungsfehler: ComputedRef<List<GostBelegpruefungErgebnisFehler>> = computed(() => {
		const res = new ArrayList<GostBelegpruefungErgebnisFehler>();
		for (const fehler of props.fehlerliste())
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
