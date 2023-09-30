<template>
	<svws-ui-table :items="[]" :no-data="false" no-data-text="Planung ist gültig." :columns="[{key: 'icon', label: '0', fixedWidth: 1.8, align: 'center'},{key: 'beschreibung', label: 'Laufbahnfehler'}]" type="navigation">
		<template #header>
			<div class="svws-ui-tr" role="row">
				<div class="svws-ui-td col-span-full" role="columnheader">
					<i-ri-checkbox-circle-fill v-if="belegungsfehler.size() === 0" class="flex-shrink-0 text-success text-headline-md -my-1 -mx-0.5" />
					{{ (belegungsfehler.size() === 0 ? 'Keine' : belegungsfehler.size()) }} {{ 'Laufbahnfehler' }} {{ belegpruefungsArt().kuerzel }}
				</div>
			</div>
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
