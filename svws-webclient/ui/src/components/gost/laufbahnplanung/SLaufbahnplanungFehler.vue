<template>
	<svws-ui-table :items="[]" :no-data="false" no-data-text="" :columns="[{key: 'icon', label: '0', fixedWidth: 1.8, align: 'center'},{key: 'beschreibung', label: 'Laufbahnfehler'}]"
		type="navigation" :scroll class="svws-no-mx">
		<template #header>
			<div class="svws-ui-tr" role="row">
				<div class="svws-ui-td col-span-full leading-5 align-middle" role="columnheader">
					<span class="icon i-ri-checkbox-circle-fill shrink-0 icon-ui-success" v-if="belegungsfehler.size() === 0" />
					{{ (belegungsfehler.size() === 0 ? 'Keine' : belegungsfehler.size()) }} {{ 'Laufbahnfehler' }} {{ belegpruefungsArt().kuerzel }}
				</div>
			</div>
		</template>
		<template #body v-if="belegungsfehler.size() > 0">
			<div v-for="fehler in belegungsfehler" :key="fehler.code" class="svws-ui-tr" role="row">
				<div class="svws-ui-td self-center leading-5 align-middle" role="cell">
					<svws-ui-tooltip>
						<span class="icon i-ri-alert-line shrink-0 icon-error text-button" />
						<template #content>
							<span class="font-mono">
								{{ fehler.code }}
							</span>
						</template>
					</svws-ui-tooltip>
				</div>
				<div class="svws-ui-td select-all leading-5 align-middle" role="cell">
					{{ fehler.beschreibung }}
				</div>
			</div>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { List } from '../../../../../core/src/java/util/List';
	import type { GostBelegpruefungErgebnisFehler } from '../../../../../core/src/core/abschluss/gost/GostBelegpruefungErgebnisFehler';
	import type { GostBelegpruefungsArt } from '../../../../../core/src/core/abschluss/gost/GostBelegpruefungsArt';
	import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
	import { GostBelegungsfehlerArt } from '../../../../../core/src/core/abschluss/gost/GostBelegungsfehlerArt';

	const props = withDefaults(defineProps<{
		fehlerliste: () => List<GostBelegpruefungErgebnisFehler>;
		belegpruefungsArt: () => GostBelegpruefungsArt;
		scroll?: boolean;
	}>(), {
		scroll: false,
	});

	const belegungsfehler = computed<List<GostBelegpruefungErgebnisFehler>>(() => {
		const res = new ArrayList<GostBelegpruefungErgebnisFehler>();
		for (const fehler of props.fehlerliste())
			if (GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.BELEGUNG
				|| GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.SCHRIFTLICHKEIT
				|| GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.SCHULSPEZIFISCH)
				res.add(fehler);
		return res;
	});

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: 1.8rem auto;
	}

</style>