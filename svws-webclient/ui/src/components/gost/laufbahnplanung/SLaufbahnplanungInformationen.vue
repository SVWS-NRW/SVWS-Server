<template>
	<svws-ui-table v-if="belegungsHinweise.size()" :no-data="false" :items="[]" :columns="[{key: 'icon', label: ' ', fixedWidth: 1.8},{key: 'beschreibung', label: 'Informationen zur Laufbahn'}]"
		type="navigation" :scroll class="svws-no-mx">
		<template #header>
			<div class="svws-ui-tr" role="row">
				<div class="svws-ui-td col-span-full align-middle" role="columnheader">Informationen zur Laufbahn</div>
			</div>
		</template>
		<template #body>
			<div v-for="hinweis in belegungsHinweise" :key="hinweis.code" class="svws-ui-tr" role="row">
				<div class="svws-ui-td self-center align-middle" role="cell">
					<svws-ui-tooltip>
						<span class="icon i-ri-information-line shrink-0 icon-ui-brand text-button" />
						<template #content>
							<span class="font-mono">
								{{ hinweis.code }}
							</span>
						</template>
					</svws-ui-tooltip>
				</div>
				<div class="svws-ui-td select-all leading-5! align-middle" role="cell">
					{{ hinweis.beschreibung }}
				</div>
			</div>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import { type ComputedRef, computed } from 'vue';
	import type { GostBelegpruefungErgebnisFehler } from '../../../../../core/src/core/abschluss/gost/GostBelegpruefungErgebnisFehler';
	import type { List } from '../../../../../core/src/java/util/List';
	import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
	import { GostBelegungsfehlerArt } from '../../../../../core/src/core/abschluss/gost/GostBelegungsfehlerArt';

	const props = withDefaults(defineProps<{
		fehlerliste: () => List<GostBelegpruefungErgebnisFehler>;
		scroll?: boolean;
	}>(), {
		scroll: false,
	});

	const belegungsHinweise: ComputedRef<List<GostBelegpruefungErgebnisFehler>> = computed(() => {
		const res = new ArrayList<GostBelegpruefungErgebnisFehler>();
		for (const fehler of props.fehlerliste())
			if (GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.HINWEIS)
				res.add(fehler);
		return res;
	});

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: 1.8rem auto;
	}

</style>