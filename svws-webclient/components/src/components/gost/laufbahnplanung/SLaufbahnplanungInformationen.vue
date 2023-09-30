<template>
	<svws-ui-table v-if="belegungsHinweise.size()" :no-data="false" :items="[]" :columns="[{key: 'icon', label: ' ', fixedWidth: 1.8},{key: 'beschreibung', label: 'Informationen zur Laufbahn'}]" type="navigation">
		<template #header>
			<div class="svws-ui-tr" role="row">
				<div class="svws-ui-td col-span-full" role="columnheader">Informationen zur Laufbahn</div>
			</div>
		</template>
		<template #body>
			<div v-for="hinweis in belegungsHinweise" :key="hinweis.code" class="svws-ui-tr" role="row">
				<div class="svws-ui-td" role="cell">
					<svws-ui-tooltip>
						<i-ri-information-line class="flex-shrink-0 text-svws text-button mt-0.5" />
						<template #content>
							<span class="font-mono">
								{{ hinweis.code }}
							</span>
						</template>
					</svws-ui-tooltip>
				</div>
				<div class="svws-ui-td leading-tight select-all" role="cell">
					{{ hinweis.beschreibung }}
				</div>
			</div>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import { type List, type GostBelegpruefungErgebnisFehler, ArrayList, GostBelegungsfehlerArt } from "@core";
	import { type ComputedRef, computed } from 'vue';

	const props = defineProps<{
		fehlerliste: () => List<GostBelegpruefungErgebnisFehler>;
	}>();

	const belegungsHinweise: ComputedRef<List<GostBelegpruefungErgebnisFehler>> = computed(() => {
		const res = new ArrayList<GostBelegpruefungErgebnisFehler>();
		for (const fehler of props.fehlerliste())
			if (!!fehler && GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.HINWEIS)
				res.add(fehler);
		return res;
	});

</script>
